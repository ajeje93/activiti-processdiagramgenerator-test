package org.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.GraphicInfo;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.apache.commons.io.FileUtils;

public class Test {

	public static void main(String[] args) throws IOException {
		// create process engine
		ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();
		ProcessEngine processEngine = cfg.buildProcessEngine();

		// create a new process
		Process p = new Process();
		p.setId("test");
		p.setName("test");

		// create start event
		StartEvent startEvent = new StartEvent();
		startEvent.setName("start");
		startEvent.setId("start");
		// create end event
		EndEvent endEvent = new EndEvent();
		endEvent.setName("end");
		endEvent.setId("end");
		// create a new task
		UserTask task = new UserTask();
		task.setName("Task1");
		task.setId("task1");
		// create a flow from start to task
		SequenceFlow flow = new SequenceFlow();
		flow.setId("flow1");
		flow.setSourceRef("start");
		flow.setTargetRef("task1");
		// create a flow from task to end
		SequenceFlow flow1 = new SequenceFlow();
		flow1.setId("flow2");
		flow1.setSourceRef("task1");
		flow1.setTargetRef("end");

		// add the elements to the process
		p.addFlowElement(flow1);
		p.addFlowElement(flow);
		p.addFlowElement(task);
		p.addFlowElement(endEvent);
		p.addFlowElement(startEvent);
		
		// add outgoing flows
		List<SequenceFlow> startEventoutgoingFlows = new ArrayList<SequenceFlow>();
		startEventoutgoingFlows.add(flow);
		startEvent.setOutgoingFlows(startEventoutgoingFlows);
		List<SequenceFlow> task1EventoutgoingFlows = new ArrayList<SequenceFlow>();
		task1EventoutgoingFlows.add(flow1);
		task.setOutgoingFlows(task1EventoutgoingFlows);
		
		// create a bpmn model
		BpmnModel model = new BpmnModel();
		// add the process to the model
		model.addProcess(p);
		
		// add graphic info
		GraphicInfo startEventGraphicInfo = new GraphicInfo();
		startEventGraphicInfo.setX(70.0);
		startEventGraphicInfo.setY(170.0);
		startEventGraphicInfo.setHeight(35.0);
		startEventGraphicInfo.setWidth(35.0);
		model.addGraphicInfo("start", startEventGraphicInfo);
		
		GraphicInfo task1GraphicInfo = new GraphicInfo();
		task1GraphicInfo.setX(180.0);
		task1GraphicInfo.setY(160.0);
		task1GraphicInfo.setHeight(55.0);
		task1GraphicInfo.setWidth(105.0);
		model.addGraphicInfo("task1", task1GraphicInfo);
		
		GraphicInfo endEventGraphicInfo = new GraphicInfo();
		endEventGraphicInfo.setX(370.0);
		endEventGraphicInfo.setY(170.0);
		endEventGraphicInfo.setHeight(35.0);
		endEventGraphicInfo.setWidth(35.0);
		model.addGraphicInfo("end", endEventGraphicInfo);
		
		// add flow graphic info
		List<GraphicInfo> flow1GraphicInfoList = new ArrayList<GraphicInfo>();
		GraphicInfo point1 = new GraphicInfo();
		point1.setX(105.0);
		point1.setY(187.0);
		flow1GraphicInfoList.add(point1);
		GraphicInfo point2 = new GraphicInfo();
		point2.setX(180.0);
		point2.setY(187.0);
		flow1GraphicInfoList.add(point2);
		model.addFlowGraphicInfoList("flow1", flow1GraphicInfoList);
		
		List<GraphicInfo> flow2GraphicInfoList = new ArrayList<GraphicInfo>();
		point1 = new GraphicInfo();
		point1.setX(285.0);
		point1.setY(187.0);
		flow2GraphicInfoList.add(point1);
		point2 = new GraphicInfo();
		point2.setX(370.0);
		point2.setY(187.0);
		flow2GraphicInfoList.add(point2);
		model.addFlowGraphicInfoList("flow2", flow2GraphicInfoList);


		// write the file
		FileUtils.copyInputStreamToFile(cfg.getProcessDiagramGenerator().generatePngDiagram(model),
				new File("target/test.png"));
	}
}
