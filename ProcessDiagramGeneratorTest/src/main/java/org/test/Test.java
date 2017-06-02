package org.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
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
		
		// Add auto layout
		new BpmnAutoLayout(model).execute();

		// write the file
		FileUtils.copyInputStreamToFile(cfg.getProcessDiagramGenerator().generatePngDiagram(model),
				new File("target/test.png"));
	}
}
