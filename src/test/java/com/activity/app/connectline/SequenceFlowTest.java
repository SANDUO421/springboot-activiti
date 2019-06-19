package com.activity.app.connectline;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 连接，测试
 *
 * @author 三多
 * @Time 2019/6/17
 */
public class SequenceFlowTest {
    private ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     */
    @Test
    public void deployProcessEngineer() {
        //与流程部署定义相关的service
        Deployment deploy = engine.getRepositoryService()
                //创建一个部署对象
                .createDeployment()
                .name("连线")
                //从classpath中一次只能加载一个文件
                .addClasspathResource("activiti/sequenceFlowProcess.bpmn")
                .addClasspathResource("activiti/sequenceFlowProcess.png")
                .deploy();//完成部署
        System.out.println("部署相关信息：id:" + deploy.getId() + "，Category:" + deploy.getCategory() + "，Name:" + deploy.getName()
                + "，TenantId:" + deploy.getTenantId() + "，DeploymentTime:" + deploy.getDeploymentTime());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void runHellwordProcess() {
        //流程定义的key
        String processkey = "sequenceFlowProcess";
        //与正在执行的流程实例和执行对象相关的service
        ProcessInstance instance = engine.getRuntimeService()
                //通过流程id或者流程key启动都行,key 对应  hellowordProcess.bpmn id 的值，或者act_re_procdef表中的key
                //因为按照key启动时，默认是按照最新的流程版本启动
                .startProcessInstanceByKey(processkey);
        //启动流程实例，id：2501,流程定义的id：hellowordProcess:1:4
        System.out.println("启动流程实例，id：" + instance.getId() + ",流程定义的id：" + instance.getProcessDefinitionId());


    }

    /**
     * 完成潘总的任务
     */
    @Test
    public void completePanZongTask() {
        //任务ID
        String taskId = "92503";
        //完成任务的同时，设置流程变量，使用流程变量来指定完成任务后，下一个连线，对应sequenceFlowProcess.bpmn 中的${message=='不重要'}
        Map<String, Object> variables = new HashMap<>();
        variables.put("message","重要");
        engine.getTaskService()
                .complete(taskId,variables);
        System.out.println("潘总完成审批，任务id为:" + taskId);
    }


    /**
     * 查询流程状态（判断流程正在执行，还是结束）
     */
    @Test
    public void isProcessEnd() {

        String processInstanceId = "32501";
        //表示正在执行的流程实例和执行对象
        ProcessInstance singleResult = engine.getRuntimeService()
                //创建流程实例对象
                .createProcessInstanceQuery()
                //使用流程实例Id查询
                .processInstanceId(processInstanceId)
                .singleResult();
        if (singleResult == null) {
            System.out.println("流程已经结束");
        } else {
            System.out.println("流程还在执行");
        }
    }

}
