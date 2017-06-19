package edu.kit.scufl.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.jena.rdf.model.Resource;

import edu.kit.scufl.arangoPojo.DataPOJO;
import edu.kit.scufl.arangoPojo.ProcessExecPOJO;
import edu.kit.scufl.arangoPojo.UsedInputsPojo;
import edu.kit.scufl.arangoPojo.WasDerivedFromPOJO;
import edu.kit.scufl.arangoPojo.WasInformedByPOJO;
import edu.kit.scufl.core.Artifact;
import edu.kit.scufl.core.Entity;
import edu.kit.scufl.core.HadDictionaryMember;
import edu.kit.scufl.core.HadMember;
import edu.kit.scufl.core.HasPart;
import edu.kit.scufl.core.QualifiedUsage;
import edu.kit.scufl.core.RDF;
import edu.kit.scufl.core.Used;
import edu.kit.scufl.core.UsedInput;
import edu.kit.scufl.core.WasGeneratedBy;
import edu.kit.scufl.core.WasOutputFrom;
import edu.kit.scufl.core.WorkflowRun;

public class RetorspectiveMapper {

	private RDF rdfObj;
	private ArangoDBUtil arangoDBUtil;
	private RDFUtility rdfUtil;
	private Utility util;

	private Stack<Resource> processorStack = new Stack<Resource>();
	private Stack<String> processorStackA = new Stack<String>();

	private List<UsedInputsPojo> usedInputsList = new ArrayList<UsedInputsPojo>();

	private Map<String, Resource> resouceMap = new HashMap<String, Resource>();
	private Map<String, WasInformedByPOJO> wasInformedByMap = new HashMap<String, WasInformedByPOJO>();
	private Map<String, WasDerivedFromPOJO> wasDerivedFromMap = new HashMap<String, WasDerivedFromPOJO>();

	public RetorspectiveMapper(RDF rdfObj) {
		this.rdfObj = rdfObj;
		rdfUtil = new RDFUtility();
		arangoDBUtil = new ArangoDBUtil("retrospective", "retroGraph");
		util = new Utility();
	}

	// start of the programs
	public void startRetrospective() {

		arangoDBUtil.createCollections();

		SCUFLRetroSpective(rdfObj.getDictionary().getWasOutputFrom().getWorkflowRun(), false);
		
		// code for creating all the links of wasInformedBy from ProcessExe to ProcessExe Output to Input link assocations
		for (String keys : wasInformedByMap.keySet()) {
			for (String wgbAsOutPut : wasInformedByMap.get(keys).getWasGeneratedBy()) {
				for (String usedAsInput : wasInformedByMap.get(keys).getUsedBy()) {
					connectByWasInformedBy(wgbAsOutPut,usedAsInput);
				}
			}
		}

		// code for creating all the links of wasDerivedFrom Data to Data >> Used to GeneratedBy link associations
		for (String keys : wasDerivedFromMap.keySet()) {
			for (String used : wasDerivedFromMap.get(keys).getUsedBy()) {
				for (String generatedBy : wasDerivedFromMap.get(keys).getWasGeneratedBy()) {
					connectByWasDerivedFrom(used,generatedBy);
				}
			}
		}
		/*
		 * File file = new File("C:/Users/VB/Desktop/SCUFL_retor.rdf"); try {
		 * rdfUtil.getModel().write(new FileWriter(file)); } catch (IOException
		 * e) { e.printStackTrace(); }
		 */

	}

	private void connectByWasDerivedFrom(String used, String generatedBy) {
		rdfUtil.wasDerivedFrom(resouceMap.get(used), resouceMap.get(generatedBy));
		arangoDBUtil.createEdge("wasDerivedFrom", used, generatedBy, "Data", "Data");
	}

	private void connectByWasInformedBy(String wgbAsOutPut, String usedAsInput) {
		rdfUtil.wasInformedBy(resouceMap.get(wgbAsOutPut), resouceMap.get(usedAsInput));
		arangoDBUtil.createEdge("wasInformedBy", wgbAsOutPut, usedAsInput, "ProcessExe", "ProcessExe");
	}

	// this is the recursive method call for building the workflow node and its
	// associated prosses nodes. Default nested value is false and execution
	// uses two ways for creating data nodes as the values are present in
	// different locations

	private void SCUFLRetroSpective(WorkflowRun workflowRun, boolean nestedflag) {

		String workflowID = util.getID(workflowRun.getAbout());
		String toReplace = (nestedflag ? "Processor execution " : "Workflow run of ");

		Resource wrkflowResource = rdfUtil.createProcessExec(workflowID,
				workflowRun.getLabel().replaceAll(toReplace, ""), workflowID, workflowRun.getStartedAtTime(),
				workflowRun.getEndedAtTime(), "completed");
		createArangoProcessExec(workflowID, workflowRun.getLabel().replaceAll(toReplace, ""),
				workflowRun.getStartedAtTime(), workflowRun.getEndedAtTime());

		resouceMap.put(workflowID, wrkflowResource);

		for (HasPart hasPart : workflowRun.getHasPart()) {
			if (null != hasPart.getProcessRun()) {

				String proceesorID = util.getID(hasPart.getProcessRun().getAbout());

				Resource processResource = rdfUtil.createProcessExec(proceesorID,
						hasPart.getProcessRun().getLabel().replaceAll("Processor execution ", ""), proceesorID,
						hasPart.getProcessRun().getStartedAtTime(), hasPart.getProcessRun().getEndedAtTime(),
						"completed");
				resouceMap.put(proceesorID, processResource);
				createArangoProcessExec(proceesorID,
						hasPart.getProcessRun().getLabel().replaceAll("Processor execution ", ""),
						hasPart.getProcessRun().getStartedAtTime(), hasPart.getProcessRun().getEndedAtTime());

				rdfUtil.isPartOf(processResource, wrkflowResource);
				createArangoIsPartOf(proceesorID, workflowID);

			} else if (null != hasPart.getWorkflowRun()) {
				processorStack.push(wrkflowResource);
				processorStackA.push(workflowID);
				SCUFLRetroSpective(hasPart.getWorkflowRun(), true);
			}
		}

		if (nestedflag) {
			rdfUtil.isPartOf(wrkflowResource, processorStack.peek());
			createArangoIsPartOf(workflowID, processorStackA.peek());

			// List<DataPOJO> dataPojoList = new ArrayList<DataPOJO>();

			for (UsedInput usedInputs : workflowRun.getUsedInput()) {
				if (null != usedInputs.getEntity()) {
					DataPOJO dataPojo = new DataPOJO();
					dataPojo.setDataNodeID(util.getID(usedInputs.getEntity().getAbout()));
					dataPojo.setTaverna_Content(usedInputs.getEntity().getContent().getContent().getChars());
					dataPojo.setWasGeneratedBy(usedInputs.getEntity().getWasGeneratedBy());
					dataPojo.setWasOutPutFrom(usedInputs.getEntity().getWasOutputFrom());
					generateDataNode(dataPojo);
					// dataPojoList.add(dataPojo);
				} else if (null != usedInputs.getResource()) {
					usedInputsList.add(new UsedInputsPojo(util.getID(usedInputs.getResource()), workflowID));
				}
			}

			for (QualifiedUsage qUsage : workflowRun.getQualifiedUsage()) {
				Entity entity = qUsage.getUsage().getEntity().getEntity();
				if (null != entity) {
					DataPOJO dataPojo = new DataPOJO();
					dataPojo.setDataNodeID(util.getID(entity.getAbout()));
					dataPojo.setTaverna_Content(entity.getContent().getContent().getChars());
					dataPojo.setWasGeneratedBy(entity.getWasGeneratedBy());
					dataPojo.setWasOutPutFrom(entity.getWasOutputFrom());
					generateDataNode(dataPojo);
					// dataPojoList.add(dataPojo);
				} else if (null != qUsage.getUsage().getEntity().getResource()) {
					usedInputsList.add(
							new UsedInputsPojo(util.getID(qUsage.getUsage().getEntity().getResource()), workflowID));
				}
			}

			for (Used used : workflowRun.getUsed()) {
				if (null != used.getEntity()) {
					Entity entity = used.getEntity();
					DataPOJO dataPojo = new DataPOJO();
					dataPojo.setDataNodeID(util.getID(entity.getAbout()));
					dataPojo.setTaverna_Content(entity.getContent().getContent().getChars());
					dataPojo.setWasGeneratedBy(entity.getWasGeneratedBy());
					dataPojo.setWasOutPutFrom(entity.getWasOutputFrom());
					generateDataNode(dataPojo);
					// dataPojoList.add(dataPojo);
				} else if (null != used.getResource()) {
					usedInputsList.add(new UsedInputsPojo(util.getID(used.getResource()), workflowID));
				}
			}

		} else if (!nestedflag) {
			createDataNodesMainWrkFlw(rdfObj.getDictionary().getHadMember(),
					rdfObj.getDictionary().getHadDictionaryMember());
			for (Entity entity : rdfObj.getEntity()) {
				createDataNodesMainWrkFlw(entity.getHadMember(), entity.getHadDictionaryMember());
			}

			for (Entity entity : rdfObj.getEntity()) {
				String wgbyId = util.getID(entity.getWasGeneratedBy().get(0).getResource());
				for (HadMember hadMember : entity.getHadMember()) {
					wasGeneratedByLink(util.getID(hadMember.getResource()), wgbyId);
				}
			}

			for (Artifact artificat : rdfObj.getArtifact()) {
				String wgbyId = util.getID(artificat.getWasGeneratedBy().get(0).getResource());
				for (HadMember hadMember : artificat.getHadMembers()) {
					wasGeneratedByLink(util.getID(hadMember.getResource()), wgbyId);
				}
			}
			for (UsedInputsPojo usedInputsPojo : usedInputsList) {
				connectDataEntity(usedInputsPojo.getDataEntity(), usedInputsPojo.getProcessExeNodeId());
			}
		}

	}

	private void wasGeneratedByLink(String dataId, String wgbyId) {
		rdfUtil.wasGeneratedBy(resouceMap.get(dataId), resouceMap.get(wgbyId));
		arangoDBUtil.createEdge("wasGeneratedBY", dataId, wgbyId, "Data", "ProcessExe");
		wasInformedByWGB(dataId, wgbyId);
	}

	private void connectDataEntity(String dataEntityID, String workflowID) {
		rdfUtil.used(resouceMap.get(workflowID), resouceMap.get(dataEntityID));
		arangoDBUtil.createEdge("used", workflowID, dataEntityID, "ProcessExe", "Data");
		wasInformedByUBY(dataEntityID, workflowID);
	}

	private void wasInformedByUBY(String dataEntityID, String workflowID) {
		if (wasInformedByMap.containsKey(dataEntityID)) {
			wasInformedByMap.get(dataEntityID).setUsedBy(workflowID);
		} else {
			WasInformedByPOJO informedByPOJO = new WasInformedByPOJO();
			informedByPOJO.setUsedBy(workflowID);
			wasInformedByMap.put(dataEntityID, informedByPOJO);
		}
		
		if(wasDerivedFromMap.containsKey(workflowID)){
			wasDerivedFromMap.get(workflowID).setUsedBy(dataEntityID);
		}else{
			WasDerivedFromPOJO deFromPOJO = new WasDerivedFromPOJO();
			deFromPOJO.setUsedBy(dataEntityID);
			wasDerivedFromMap.put(workflowID, deFromPOJO);
		}
	}

	private void createDataNodesMainWrkFlw(List<HadMember> hadMemberList,
			List<HadDictionaryMember> HadDictionaryMemberList) {

		for (HadMember hadMember : hadMemberList) {
			if (null != hadMember.getEntity()) {

				DataPOJO dataPojo = new DataPOJO();
				dataPojo.setDataNodeID(util.getID(hadMember.getEntity().getAbout()));
				dataPojo.setTaverna_Content(hadMember.getEntity().getContent().getContent().getChars());
				// dataPojo.setWasDerivedFrom(wasDerivedFro
				dataPojo.setWasGeneratedBy(hadMember.getEntity().getWasGeneratedBy());
				dataPojo.setWasOutPutFrom(hadMember.getEntity().getWasOutputFrom());

				generateDataNode(dataPojo);

			}
		}

		for (HadDictionaryMember hDictMember : HadDictionaryMemberList) {
			if (null != hDictMember.getKeyEntityPair().getPairEntity().getEntity()) {

				DataPOJO dataPojo = new DataPOJO();
				dataPojo.setDataNodeID(
						util.getID(hDictMember.getKeyEntityPair().getPairEntity().getEntity().getAbout()));
				dataPojo.setTaverna_Content(hDictMember.getKeyEntityPair().getPairEntity().getEntity().getContent()
						.getContent().getChars());
				// data.setWasDerivedFrom(wasDerivedFrom);
				dataPojo.setWasGeneratedBy(
						hDictMember.getKeyEntityPair().getPairEntity().getEntity().getWasGeneratedBy());
				dataPojo.setWasOutPutFrom(
						hDictMember.getKeyEntityPair().getPairEntity().getEntity().getWasOutputFrom());

				generateDataNode(dataPojo);
			}
		}

	}

	// creating data node and building its relation wasGeneratedBY processExec
	// nodes. This function only operates for the non-nested block of workflows.
	private void generateDataNode(DataPOJO dataPojo) {
		Set<String> wasGeneratedByOPSet = new HashSet<String>();

		String entityID = dataPojo.getDataNodeID();
		Resource dataResource;
		if (!resouceMap.containsKey(entityID)) {
			dataResource = rdfUtil.createData(entityID, "data", "wasGeneratedBY", dataPojo.getTaverna_Content());
			arangoDBUtil.createNode(dataPojo, entityID, "Data");
			resouceMap.put(entityID, dataResource);
		} else {
			dataResource = resouceMap.get(entityID);
		}

		if (null != dataPojo.getWasGeneratedBy()) {
			for (WasGeneratedBy wgby : dataPojo.getWasGeneratedBy()) {
				if (!wasGeneratedByOPSet.contains(util.getID(wgby.getResource()))) {
					rdfUtil.wasGeneratedBy(dataResource, resouceMap.get(util.getID(wgby.getResource())));
					arangoDBUtil.createEdge("wasGeneratedBY", entityID, util.getID(wgby.getResource()), "Data",
							"ProcessExe");
					wasGeneratedByOPSet.add(util.getID(wgby.getResource()));

					wasInformedByWGB(entityID, util.getID(wgby.getResource()));

				}
			}
		}

		if (null != dataPojo.getWasOutPutFrom()) {
			for (WasOutputFrom wopf : dataPojo.getWasOutPutFrom()) {
				if (!wasGeneratedByOPSet.contains(util.getID(wopf.getResource()))) {
					rdfUtil.wasGeneratedBy(dataResource, resouceMap.get(util.getID(wopf.getResource())));
					arangoDBUtil.createEdge("wasGeneratedBY", entityID, util.getID(wopf.getResource()), "Data",
							"ProcessExe");
					wasGeneratedByOPSet.add(util.getID(wopf.getResource()));
					wasInformedByWGB(entityID, util.getID(wopf.getResource()));
				}
			}
		}
	}

	private void wasInformedByWGB(String dataEntityID, String workflowID) {
		if (wasInformedByMap.containsKey(dataEntityID)) {
			wasInformedByMap.get(dataEntityID).setWasGeneratedBy(workflowID);
		} else {
			WasInformedByPOJO informedByPOJO = new WasInformedByPOJO();
			informedByPOJO.setWasGeneratedBy(workflowID);
			wasInformedByMap.put(dataEntityID, informedByPOJO);
		}
		if(wasDerivedFromMap.containsKey(workflowID)){
			wasDerivedFromMap.get(workflowID).setWasGeneratedBy(dataEntityID);
		}else{
			WasDerivedFromPOJO deFromPOJO = new WasDerivedFromPOJO();
			deFromPOJO.setWasGeneratedBy(dataEntityID);
			wasDerivedFromMap.put(workflowID, deFromPOJO);
		}
	}

	private void createArangoIsPartOf(String processId, String workFlowToprocess) {
		arangoDBUtil.createEdge("isPartOf", workFlowToprocess, processId, "ProcessExe", "ProcessExe");
	}

	private void createArangoProcessExec(String processId, String processName, String startedAtTime,
			String endedAtTime) {
		ProcessExecPOJO pojo = new ProcessExecPOJO(processId, processName, startedAtTime, endedAtTime);
		arangoDBUtil.createNode(pojo, processId, "ProcessExe");
	}

}
