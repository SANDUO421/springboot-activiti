package com.activity.app.connectline;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

/**
 * 接收活动
 *
 * @author 三多
 * @Time 2019/6/19
 */
public class ReceiveTaskTest {

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
                .name("接收活动任务")
                //从classpath中一次只能加载一个文件
                .addClasspathResource("activiti/receiveTaskProcess.bpmn")
                .addClasspathResource("activiti/receiveTaskProcess.png")
                .deploy();//完成部署
        System.out.println("部署相关信息：id:" + deploy.getId() + "，Category:" + deploy.getCategory() + "，Name:" + deploy.getName()
                + "，TenantId:" + deploy.getTenantId() + "，DeploymentTime:" + deploy.getDeploymentTime());
    }

    /**
     * 启动流程实例+设置流程变量+获取流程变量+向后执行一步
     */
    @Test
    public void runProcessInstance() {
        //流程定义的key
        String processkey = "receiveTaskProcess";
        //与正在执行的流程实例和执行对象相关的service
        ProcessInstance instance = engine.getRuntimeService()
                //通过流程id或者流程key启动都行,key 对应  receiveTaskProcess.bpmn id 的值，或者act_re_procdef表中的key
                //因为按照key启动时，默认是按照最新的流程版本启动
                .startProcessInstanceByKey(processkey);
        //启动流程实例，id：2501,流程定义的id：hellowordProcess:1:4
        System.out.println("启动流程实例，id：" + instance.getId() + ",流程定义的id：" + instance.getProcessDefinitionId());

        /**查询执行对象ID*/
        Execution execution = engine.getRuntimeService()
                .createExecutionQuery()//创建执行对象查询
                .processInstanceId(instance.getId())//使用流程实例ID查询
                .activityId("_4")//当前活动的ID，对应receiveTaskProcess.bpmn 活动节点id 的值
                .singleResult();

        /**使用流程变量设置当日销售额，用来传递业务参数*/
        engine.getRuntimeService()
                .setVariable(execution.getId(), "当日销售额", 10000);
        /**向后执行一步，如果流程处于等待状态，让流程继续执行*/
        engine.getRuntimeService()
                .signal(execution.getId());
        /**查询执行对象*/
        Execution execution2 = engine.getRuntimeService()
                .createExecutionQuery()//创建执行对象查询
                .processInstanceId(instance.getId())//使用流程实例ID查询
                .activityId("_5")//当前活动的ID，对应receiveTaskProcess.bpmn 活动节点id 的值
                .singleResult();
        /**获取流程变量*/
        Integer money = (Integer) engine.getRuntimeService()
                .getVariable(execution2.getId(), "当日销售额");
        /**向后执行一步，向老板发短信*/
        System.out.println("老板你好：当日销售额：" + money);
        /**向后执行一步，如果流程处于等待状态，让流程继续执行*/
        engine.getRuntimeService()
                .signal(execution2.getId());
        /**判断任务是否执行完成*/
        ProcessInstance pi = engine.getRuntimeService()
                .createProcessInstanceQuery()
                .processInstanceId(instance.getId())
                .singleResult();
        if (pi == null){
            System.out.println("完成销售额汇报，并发短信告知！");
        }

    }

}
