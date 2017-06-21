package org.kit.mainapp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;
import java.util.UUID;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.xerces.dom.DeepNodeListImpl;
import org.apache.xerces.dom.ElementNSImpl;
import org.kit.parse.ScuflMain;
import org.kit.pojo.ProcessorInOutMap;
import org.kit.pojo.arangodb.ProcessorDoc;
import org.kit.pojo.arangodb.ProcessorInOutMapArango;
import org.kit.rdf.RDFUtility;
import org.kit.scufl.api.Activity;
import org.kit.scufl.api.AnnotatedGranularDepthPort;
import org.kit.scufl.api.AnnotatedPort;
import org.kit.scufl.api.DataLink;
import org.kit.scufl.api.Dataflow;
import org.kit.scufl.api.DepthPort;
import org.kit.scufl.api.GranularDepthPort;
import org.kit.scufl.api.Link;
import org.kit.scufl.api.Processor;
import org.kit.scufl.api.Workflow;
import org.kit.utility.ArangoDBUtil;
import org.w3c.dom.Node;

public class ScuflToProv {

	private String DATABASE_NAME = "Scufl";
	private String GRAPH_NAME = "tavernaSCUFL";
	private Stack<String> currentProceess = new Stack<String>();

	private HashMap<String, Dataflow> dataFlwRefMap = new HashMap<String, Dataflow>();
	private HashMap<String, ProcessorInOutMap> processInOutMap = new HashMap<String, ProcessorInOutMap>();
	private HashMap<String, ProcessorInOutMapArango> processInOutMap2 = new HashMap<String, ProcessorInOutMapArango>();
	private HashSet<String> seqLinkSet = new HashSet<String>();
	
	ArangoDBUtil arangoDb = new ArangoDBUtil(DATABASE_NAME, GRAPH_NAME);
	private RDFUtility rdfUtility = new RDFUtility();

	/**
	 * 
	 * @param filePath
	 *            T@FLOW file path
	 */
	public void parseCreateGraph(String filePath) {

		ScuflMain scuflMain = new ScuflMain();
		Workflow wrkFlw = scuflMain.parseScufl(filePath);
		arangoDb.createCollectionss();

		for (Dataflow datflw : wrkFlw.getDataflow()) {
			dataFlwRefMap.put(datflw.getId(), datflw);
		}

		for (Dataflow dtflw : wrkFlw.getDataflow()) {
			if (dtflw.getRole().toString().equalsIgnoreCase("top")) {
				boolean nestedFlag = false;
				currentProceess.push(dtflw.getName());
				Prov2ONE_SCUFL(dtflw, nestedFlag);
			}
		}

		File file = new File("C:/Users/Vaibhav/Desktop/SCUFLNew.rdf");
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		try {
			rdfUtility.getModel().write(new FileWriter(file));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param dtflw
	 *            main or Nested workflow called recursively
	 * @param nestedFlag
	 *            to check weather the dataflow is nested or main i.e. Role =
	 *            TOP / Nested
	 */
	private void Prov2ONE_SCUFL(Dataflow dtflw, boolean nestedFlag) {

		Resource processDoc = null;
		String processDocA = null;
		String uuid = UUID.randomUUID().toString();

		if (nestedFlag) {
			processDoc = processInOutMap.get(currentProceess.peek()).getProcessDoc();
			processDocA = processInOutMap2.get(currentProceess.peek()).getProcessorID();

		} else {

			// RDF code
			processDoc = rdfUtility.createWorkflow(dtflw.getId(), dtflw.getName(),dtflw.getRole().toString());
			processDocA = dtflw.getId();
			// ArangoDB code
			// creating workflow node
			ProcessorDoc wrkPojo = new ProcessorDoc();
			wrkPojo.setGraphName("WorkFlow");
			wrkPojo.setName("WorkFlow");
			arangoDb.createNode(wrkPojo, uuid, "WorkFlow");
			// creating processor node
			ProcessorDoc processorPojo = new ProcessorDoc();
			processorPojo.setGraphName("P:" + dtflw.getName());
			processorPojo.setName(dtflw.getName());
			arangoDb.createNode(processorPojo, dtflw.getId(), "Processor");
			// creating relations between processor and workflow
			arangoDb.createEdge("wasDerivedFrom", uuid, dtflw.getId(), "WorkFlow", "Processor");

			ATTACHPORTS(dtflw, processDoc, nestedFlag);
		}

		// below code is for parsing each process of the workflow

		for (Processor processor : dtflw.getProcessors().getProcessor()) {
			boolean normalFlag = true;

			// special condition for checking if it is a nested workflow and if
			// yes then special operations has to be done for the same.
			for (Activity activity : processor.getActivities().getActivity()) {
				if (null != activity.getConfigBean() && activity.getConfigBean().getEncoding().equals("dataflow")) {
					
					String nestedWrkFlw = UUID.randomUUID().toString();
					String uuidWorkflowNode = UUID.randomUUID().toString();
					ElementNSImpl config = (ElementNSImpl) activity.getConfigBean().getAny();
					
					// RDF code
					Resource processorDoc = rdfUtility.createWorkflow(nestedWrkFlw, processor.getName(),dataFlwRefMap.get(config.getAttribute("ref")).getRole().toString());
					rdfUtility.hasSubProcess(processDoc, processorDoc);
					// ArangoDB code
					// creating workflow node
					ProcessorDoc wrkPojo = new ProcessorDoc();
					wrkPojo.setGraphName("WorkFlow");
					wrkPojo.setName("WorkFlow");
					arangoDb.createNode(wrkPojo, uuidWorkflowNode, "WorkFlow");
					// creating processor node
					ProcessorDoc processorPojo = new ProcessorDoc();
					processorPojo.setGraphName("P:" + processor.getName());
					processorPojo.setName(processor.getName());
					arangoDb.createNode(processorPojo, nestedWrkFlw, "Processor");
					// creating relations between processor and workflow
					arangoDb.createEdge("wasDerivedFrom", uuidWorkflowNode, nestedWrkFlw, "WorkFlow", "Processor");
					arangoDb.createEdge("hasSubProcess", processDocA, nestedWrkFlw, "Processor", "Processor");
					
					ATTACHPORTS(processor, processorDoc, nestedWrkFlw);
					
					
					currentProceess.push(processor.getName());
					Prov2ONE_SCUFL(dataFlwRefMap.get(config.getAttribute("ref")), true);
					currentProceess.pop();
//					currentProceess.push(dtflw.getName());

					normalFlag = false;

				}
			}
			if (normalFlag) {

				// common Method for creating all the listed process and
				// relations is of type hasSubProcess
				String processUUID = UUID.randomUUID().toString();
				// code for rdf
				ElementNSImpl config = (ElementNSImpl) processor.getActivities().getActivity().get(0).getConfigBean().getAny();
				String scriptText = null;
				for (int i = 0; i < config.getChildNodes().getLength(); i++) {
					if(config.getChildNodes().item(i).getNodeName().toString().equalsIgnoreCase("script")){
						scriptText = config.getChildNodes().item(i).getTextContent();
						break;
					}
				}
				Resource processorDoc = rdfUtility.createProcess(processUUID, processor.getName(),processor.getActivities().getActivity().get(0).getClazz(),scriptText);
				rdfUtility.hasSubProcess(processDoc, processorDoc);

				// code for ArangoDB
				ProcessorDoc subprocessor = new ProcessorDoc();
				subprocessor.setGraphName("P:" + processor.getName());
				subprocessor.setName(processor.getName());
				arangoDb.createNode(subprocessor, processUUID, "Processor");
				arangoDb.createEdge("hasSubProcess", processDocA, processUUID, "Processor", "Processor");

				// common for RDF and ArangoDb
				ATTACHPORTS(processor, processorDoc, processUUID);
			}

		}
//		datalink logic for each workflow
		for (DataLink dtlinks : dtflw.getDatalinks().getDatalink()) {

			Resource dataLinkDoc = null;
			String dataLinkDocA = null;
			Link sinkObj = dtlinks.getSink();
			Link sourceObj = dtlinks.getSource();

			ProcessorInOutMap processorInOutSourceObj = null;
			ProcessorInOutMap processorInOutSinkObj = null;
			
			ProcessorInOutMapArango processorInOutSourceObjA = null;
			ProcessorInOutMapArango processorInOutSinkObjA = null;
			
			String sourcePName = null;
			String destinationPName = null;
			boolean sourcePOP = false;
			boolean destinationPOP = false;
			boolean sourceIsProcessor = true;
			boolean sinkIsProcessor = true;
			
			String datalinkUUID = UUID.randomUUID().toString();
			dataLinkDoc = rdfUtility.createDataLink(datalinkUUID);
			
			if (sourceObj.getType().toString().equalsIgnoreCase("processor")) {
				
//				code for RDF
				processorInOutSourceObj = processInOutMap.get(sourceObj.getProcessor());
				
//				code for ArangoDB
				sourcePName = sourceObj.getProcessor();
				ProcessorDoc wrkPojo = new ProcessorDoc();
				wrkPojo.setGraphName("DL:"+sourcePName);
				wrkPojo.setName("DL:"+sourcePName);
				arangoDb.createNode(wrkPojo, datalinkUUID, "DataLink");
				processorInOutSourceObjA = processInOutMap2.get(sourcePName);
				
				if (processorInOutSourceObj.getInportMap().containsKey(sourceObj.getPort())) {
					// code for RDF
					Resource inport = processorInOutSourceObj.getInportMap().get(sourceObj.getPort());
					rdfUtility.inPortToDL(inport, dataLinkDoc);
					
					// code for ArangoDB
					String inportid = processorInOutSourceObjA.getInputPortMap().get(sourceObj.getPort());
					arangoDb.createEdge("inPortToDL", inportid, datalinkUUID, "inport", "DataLink");

				} else if (processorInOutSourceObj.getOutportMap().containsKey(sourceObj.getPort())) {
//					code for RDF
					Resource outport = processorInOutSourceObj.getOutportMap().get(sourceObj.getPort());
					rdfUtility.outPortToDL(outport, dataLinkDoc);
					
					// code for ArangoDB
					String outputid = processorInOutSourceObjA.getOutputPortMap().get(sourceObj.getPort());
					arangoDb.createEdge("outPortToDL", outputid, datalinkUUID, "outport", "DataLink");
					sourcePOP = true;
				}

			} else if (sourceObj.getType().toString().equalsIgnoreCase("dataflow")) {
				sourceIsProcessor = false;
//				code for RDF
				processorInOutSourceObj = processInOutMap.get(currentProceess.peek());
				
//				code for ArangoDB
				sourcePName = currentProceess.peek();
				processorInOutSourceObjA = processInOutMap2.get(sourcePName);
//				code for ArangoDB
//				sourcePName = sourceObj.getProcessor();
				ProcessorDoc wrkPojo = new ProcessorDoc();
				wrkPojo.setGraphName("DL:"+sourcePName);
				wrkPojo.setName("DL:"+sourcePName);
				arangoDb.createNode(wrkPojo, datalinkUUID, "DataLink");
				
				if (nestedFlag) {
					if (processorInOutSourceObj.getInportMap().containsKey(sourceObj.getPort())) {
//						code for RDF
						Resource inport = processorInOutSourceObj.getInportMap().get(sourceObj.getPort());
						rdfUtility.inPortToDL(inport, dataLinkDoc);
//						code for ArangoDB
						String inportid = processorInOutSourceObjA.getInputPortMap().get(sourceObj.getPort());
						arangoDb.createEdge("inPortToDL", inportid, datalinkUUID, "inport", "DataLink");
						
					} else if (processorInOutSourceObj.getOutportMap().containsKey(sourceObj.getPort())) {
//						code for RDF
						Resource outport = processorInOutSourceObj.getOutportMap().get(sourceObj.getPort());
						rdfUtility.outPortToDL(outport, dataLinkDoc);
//						code for ArangoDB
						String outputid = processorInOutSourceObjA.getOutputPortMap().get(sourceObj.getPort());
						arangoDb.createEdge("outPortToDL", outputid, datalinkUUID, "outport", "DataLink");
						sourcePOP = true;
					}
				} else {
					if (processorInOutSourceObj.getInportMap().containsKey(sourceObj.getPort())) {
//						code for RDF
						Resource inport = processorInOutSourceObj.getInportMap().get(sourceObj.getPort());
						rdfUtility.inPortToDL(inport, dataLinkDoc);
//						code for ArangoDB
						String inportid = processorInOutSourceObjA.getInputPortMap().get(sourceObj.getPort());
						arangoDb.createEdge("inPortToDL", inportid, datalinkUUID, "inport", "DataLink");
						
					} else if (processorInOutSourceObj.getOutportMap().containsKey(sourceObj.getPort())) {
//						code for RDF
						Resource outport = processorInOutSourceObj.getOutportMap().get(sourceObj.getPort());
						rdfUtility.outPortToDL(outport, dataLinkDoc);
//						code for ArangoDB
						String outputid = processorInOutSourceObjA.getOutputPortMap().get(sourceObj.getPort());
						arangoDb.createEdge("outPortToDL", outputid, datalinkUUID, "outport", "DataLink");
						sourcePOP = true;
					}
				}
			}
			if (sinkObj.getType().toString().equalsIgnoreCase("processor")) {
				destinationPName = sinkObj.getProcessor();
//				code for RDF
				processorInOutSinkObj = processInOutMap.get(sinkObj.getProcessor());
//				code for ArangoDB
				processorInOutSinkObjA = processInOutMap2.get(sinkObj.getProcessor());
				
				if (processorInOutSinkObj.getInportMap().containsKey(sinkObj.getPort())) {
//					code for RDF
					Resource inputPort = processorInOutSinkObj.getInportMap().get(sinkObj.getPort());
					rdfUtility.DLToInPort(dataLinkDoc, inputPort);
//					code for ArangoDB
					String inportid = processorInOutSinkObjA.getInputPortMap().get(sinkObj.getPort());
					arangoDb.createEdge("DLToInPort", datalinkUUID, inportid, "DataLink", "inport" );
					
				} else if (processorInOutSinkObj.getOutportMap().containsKey(sinkObj.getPort())) {
//					code for RDF
					Resource outputPort = processorInOutSinkObj.getOutportMap().get(sinkObj.getPort());
					rdfUtility.DLToOutPort(dataLinkDoc, outputPort);

//					code for ArangoDB
					String outputid = processorInOutSinkObjA.getOutputPortMap().get(sinkObj.getPort());
					arangoDb.createEdge("DlToOutPort", datalinkUUID, outputid, "DataLink","outport");
					
					destinationPOP= true;
				}
			}else if (sinkObj.getType().toString().equalsIgnoreCase("dataflow")) {
				sinkIsProcessor = false;
				destinationPName = currentProceess.peek();
				processorInOutSinkObj = processInOutMap.get(currentProceess.peek());
				processorInOutSinkObjA = processInOutMap2.get(currentProceess.peek());
				
				if (nestedFlag) {
					if (processorInOutSinkObj.getInportMap().containsKey(sinkObj.getPort())) {
//						code for RDF
						Resource inport = processorInOutSinkObj.getInportMap().get(sinkObj.getPort());
						rdfUtility.DLToInPort(dataLinkDoc, inport);
//						code for ArangoDB
						String inportid = processorInOutSinkObjA.getInputPortMap().get(sinkObj.getPort());
						arangoDb.createEdge("DLToInPort", datalinkUUID, inportid, "DataLink", "inport" );

					} else if (processorInOutSinkObj.getOutportMap().containsKey(sinkObj.getPort())) {
						Resource outport = processorInOutSinkObj.getOutportMap().get(sinkObj.getPort());
						rdfUtility.DLToOutPort(dataLinkDoc, outport);

//						code for ArangoDB
						String outputid = processorInOutSinkObjA.getOutputPortMap().get(sinkObj.getPort());
						arangoDb.createEdge("DlToOutPort", datalinkUUID, outputid, "DataLink","outport");
						
						destinationPOP= true;

					}
				} else {
					if (processorInOutSinkObj.getInportMap().containsKey(sinkObj.getPort())) {
						Resource inport = processorInOutSinkObj.getInportMap().get(sinkObj.getPort());
						rdfUtility.DLToInPort(dataLinkDoc, inport);

//						code for ArangoDB
						String inportid = processorInOutSinkObjA.getInputPortMap().get(sinkObj.getPort());
						arangoDb.createEdge("DLToInPort", datalinkUUID, inportid, "DataLink", "inport" );
						
					} else if (processorInOutSinkObj.getOutportMap().containsKey(sinkObj.getPort())) {
						Resource outport = processorInOutSinkObj.getOutportMap().get(sinkObj.getPort());
						rdfUtility.DLToOutPort(dataLinkDoc, outport);

//						code for ArangoDB
						String outputid = processorInOutSinkObjA.getOutputPortMap().get(sinkObj.getPort());
						arangoDb.createEdge("DlToOutPort", datalinkUUID, outputid, "DataLink","outport");
						
						destinationPOP= true;
					}
				}
			}
			
			// insert map to avoid implementating multiple sequance ctrl link in
			// graph eg Source.name-target.name as key to avoid the duplicates
			if (!(sourcePOP && destinationPOP) && sourceIsProcessor && sinkIsProcessor) {
				if (!seqLinkSet.contains(sourcePName + "" + destinationPName)) {
					String seqLinkUUID = UUID.randomUUID().toString();
//					code for RDF
					Resource seqCtrlLink = rdfUtility.createSeqCtrlLink(seqLinkUUID);
					rdfUtility.sourcePToCL(processorInOutSourceObj.getProcessDoc(), seqCtrlLink);
					rdfUtility.CLtoDestP(processorInOutSinkObj.getProcessDoc(), seqCtrlLink);
//					code for AranogDB
					ProcessorDoc wrkPojo = new ProcessorDoc();
					wrkPojo.setGraphName("SEQCTRLLINK");
					wrkPojo.setName("SEQCTRLLINK");
					arangoDb.createNode(wrkPojo, seqLinkUUID, "SEQCTRLLINK");
					arangoDb.createEdge("sourcePToCL", processorInOutSourceObjA.getProcessorID(), seqLinkUUID, "Processor", "SEQCTRLLINK");
					arangoDb.createEdge("CLtoDestP", seqLinkUUID, processorInOutSinkObjA.getProcessorID(), "SEQCTRLLINK", "Processor");
				}
			}
		}

	}

	private void ATTACHPORTS(Dataflow dtflw, Resource processDoc, boolean nestedFlag, String processUUID) {

		HashMap<String, Resource> inportMap = new HashMap<String, Resource>();
		HashMap<String, Resource> outportMap = new HashMap<String, Resource>();

		HashMap<String, String> inputPortMap = new HashMap<String, String>();
		HashMap<String, String> outputPortMap = new HashMap<String, String>();

		for (AnnotatedGranularDepthPort inPort : dtflw.getInputPorts().getPort()) {
			String uuid = UUID.randomUUID().toString();
			// code for RDF
			Resource inportDoc = rdfUtility.createInputPort(uuid, inPort.getName());
			inportMap.put(inPort.getName(), inportDoc);
			rdfUtility.hasInPort(processDoc, inportDoc);

			// code for ArangoDB
			ProcessorDoc inportPojo = new ProcessorDoc();
			inportPojo.setGraphName("IP:" + inPort.getName());
			inportPojo.setName(inPort.getName());
			arangoDb.createNode(inportPojo, uuid, "inport");
			arangoDb.createEdge("hasInPort", processUUID, uuid, "Processor", "inport");
			inputPortMap.put(inPort.getName(), uuid);

		}
		for (AnnotatedPort outPort : dtflw.getOutputPorts().getPort()) {
			String uuid = UUID.randomUUID().toString();
			// code for RDF
			Resource outportDoc = rdfUtility.createOutputPort(uuid, outPort.getName());
			outportMap.put(outPort.getName(), outportDoc);
			rdfUtility.hasOutPort(processDoc, outportDoc);

			// code for ArangoDB
			ProcessorDoc inportPojo = new ProcessorDoc();
			inportPojo.setGraphName("OP:" + outPort.getName());
			inportPojo.setName(outPort.getName());
			arangoDb.createNode(inportPojo, uuid, "outport");
			arangoDb.createEdge("hasOutPort", processUUID, uuid, "Processor", "outport");
			outputPortMap.put(outPort.getName(), uuid);

		}

		ProcessorInOutMap inOutMap = new ProcessorInOutMap();
		inOutMap.setProcessDoc(processDoc);
		inOutMap.setInportMap(inportMap);
		inOutMap.setOutportMap(outportMap);

		ProcessorInOutMapArango inOutMapA = new ProcessorInOutMapArango();
		inOutMapA.setProcessorID(processUUID);
		inOutMapA.setInputPortMap(inputPortMap);
		inOutMapA.setOutputPortMap(outputPortMap);

		// if (nestedFlag) {
		// processInOutMap.put(currentProceess.peek(), inOutMap);
		// processInOutMap2.put(currentProceess.peek(), inOutMapA);
		// } else {
		processInOutMap.put(dtflw.getName(), inOutMap);
		processInOutMap2.put(dtflw.getName(), inOutMapA);
		// }
	}

	private void ATTACHPORTS(Processor processor, Resource processorDoc, String processUUID) {

		HashMap<String, Resource> inportMap = new HashMap<String, Resource>();
		HashMap<String, Resource> outportMap = new HashMap<String, Resource>();

		HashMap<String, String> inputPortMap = new HashMap<String, String>();
		HashMap<String, String> outputPortMap = new HashMap<String, String>();

		for (DepthPort inPort : processor.getInputPorts().getPort()) {
			String uuid = UUID.randomUUID().toString();
			// code for RDF
			Resource inportDoc = rdfUtility.createInputPort(uuid, inPort.getName());
			inportMap.put(inPort.getName(), inportDoc);
			rdfUtility.hasInPort(processorDoc, inportDoc);

			// code for ArangoDB
			ProcessorDoc inportPojo = new ProcessorDoc();
			inportPojo.setGraphName("IP:" + inPort.getName());
			inportPojo.setName(inPort.getName());
			arangoDb.createNode(inportPojo, uuid, "inport");
			arangoDb.createEdge("hasInPort", processUUID, uuid, "Processor", "inport");
			inputPortMap.put(inPort.getName(), uuid);
		}
		for (GranularDepthPort outPort : processor.getOutputPorts().getPort()) {
			String uuid = UUID.randomUUID().toString();
			// code for RDF
			Resource outportDoc = rdfUtility.createOutputPort(uuid, outPort.getName());
			outportMap.put(outPort.getName(), outportDoc);
			rdfUtility.hasOutPort(processorDoc, outportDoc);

			// code for ArangoDB
			ProcessorDoc inportPojo = new ProcessorDoc();
			inportPojo.setGraphName("OP:" + outPort.getName());
			inportPojo.setName(outPort.getName());
			arangoDb.createNode(inportPojo, uuid, "outport");
			arangoDb.createEdge("hasOutPort", processUUID, uuid, "Processor", "outport");
			outputPortMap.put(outPort.getName(), uuid);
		}
		ProcessorInOutMap inOutMap = new ProcessorInOutMap();
		inOutMap.setProcessDoc(processorDoc);
		inOutMap.setInportMap(inportMap);
		inOutMap.setOutportMap(outportMap);

		ProcessorInOutMapArango inOutMapA = new ProcessorInOutMapArango();
		inOutMapA.setProcessorID(processUUID);
		inOutMapA.setInputPortMap(inputPortMap);
		inOutMapA.setOutputPortMap(outputPortMap);

		processInOutMap.put(processor.getName(), inOutMap);
		processInOutMap2.put(processor.getName(), inOutMapA);

	}

	private void ATTACHPORTS(Dataflow dtflw, Resource processDoc, boolean nestedFlag) {

		HashMap<String, Resource> inportMap = new HashMap<String, Resource>();
		HashMap<String, Resource> outportMap = new HashMap<String, Resource>();

		HashMap<String, String> inputPortMap = new HashMap<String, String>();
		HashMap<String, String> outputPortMap = new HashMap<String, String>();

		for (AnnotatedGranularDepthPort inPort : dtflw.getInputPorts().getPort()) {
			String uuid = UUID.randomUUID().toString();
			// code for RDF
			Resource inportDoc = rdfUtility.createInputPort(uuid, inPort.getName());
			inportMap.put(inPort.getName(), inportDoc);
			rdfUtility.hasInPort(processDoc, inportDoc);

			// code for ArangoDB
			ProcessorDoc inportPojo = new ProcessorDoc();
			inportPojo.setGraphName("IP:" + inPort.getName());
			inportPojo.setName(inPort.getName());
			arangoDb.createNode(inportPojo, uuid, "inport");
			arangoDb.createEdge("hasInPort", dtflw.getId(), uuid, "Processor", "inport");
			inputPortMap.put(inPort.getName(), uuid);

		}
		for (AnnotatedPort outPort : dtflw.getOutputPorts().getPort()) {
			String uuid = UUID.randomUUID().toString();
			// code for RDF
			Resource outportDoc = rdfUtility.createOutputPort(uuid, outPort.getName());
			outportMap.put(outPort.getName(), outportDoc);
			rdfUtility.hasOutPort(processDoc, outportDoc);

			// code for ArangoDB
			ProcessorDoc inportPojo = new ProcessorDoc();
			inportPojo.setGraphName("OP:" + outPort.getName());
			inportPojo.setName(outPort.getName());
			arangoDb.createNode(inportPojo, uuid, "outport");
			arangoDb.createEdge("hasOutPort", dtflw.getId(), uuid, "Processor", "outport");
			outputPortMap.put(outPort.getName(), uuid);

		}

		ProcessorInOutMap inOutMap = new ProcessorInOutMap();
		inOutMap.setProcessDoc(processDoc);
		inOutMap.setInportMap(inportMap);
		inOutMap.setOutportMap(outportMap);

		ProcessorInOutMapArango inOutMapA = new ProcessorInOutMapArango();
		inOutMapA.setProcessorID(dtflw.getId());
		inOutMapA.setInputPortMap(inputPortMap);
		inOutMapA.setOutputPortMap(outputPortMap);

		if (nestedFlag) {
			processInOutMap.put(currentProceess.peek(), inOutMap);
			processInOutMap2.put(currentProceess.peek(), inOutMapA);
		} else {
			processInOutMap.put(dtflw.getName(), inOutMap);
			processInOutMap2.put(dtflw.getName(), inOutMapA);
		}
	}

}
