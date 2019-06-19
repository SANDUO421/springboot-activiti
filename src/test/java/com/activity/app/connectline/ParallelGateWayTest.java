package com.activity.app.connectline;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

/**
 * 并行网关测试
 * @author 三多
 * @Time 2019/6/19
 */
public class ParallelGateWayTest {
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
                .name("并行网关")
                //从classpath中一次只能加载一个文件
                .addClasspathResource("activiti/parallelGateWayProcess.bpmn")
                .addClasspathResource("activiti/parallelGateWayProcess.png")
                .deploy();//完成部署
        System.out.println("部署相关信息：id:" + deploy.getId() + "，Category:" + deploy.getCategory() + "，Name:" + deploy.getName()
                + "，TenantId:" + deploy.getTenantId() + "，DeploymentTime:" + deploy.getDeploymentTime());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void runProcessInstance() {
        //流程定义的key
        String processkey = "parallelGateWayProcess";
        //与正在执行的流程实例和执行对象相关的service
        ProcessInstance instance = engine.getRuntimeService()
                //通过流程id或者流程key启动都行,key 对应  hellowordProcess.bpmn id 的值，或者act_re_procdef表中的key
                //因为按照key启动时，默认是按照最新的流程版本启动
                .startProcessInstanceByKey(processkey);
        //启动流程实例，id：2501,流程定义的id：hellowordProcess:1:4
        System.out.println("启动流程实例，id：" + instance.getId() + ",流程定义的id：" + instance.getProcessDefinitionId());


    }

    /**
     * 1. 开始->并行网关1->付款->发货->并行网关2
     * 2. 开始->并行网关1->收货->收款->并行网关2
     * 3. 并行网关2->结束
     */
    @Test
    public void completeTask() {
        //任务ID
        String taskId = "157502";
        engine.getTaskService()
                .complete(taskId);
        System.out.println("完成审批，任务id为:" + taskId);
    }

}
