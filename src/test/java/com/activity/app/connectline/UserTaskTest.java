package com.activity.app.connectline;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人任务测试
 * 1. 第一种方式：设置的模板中写死(userTaskProcess-1.bpmn)
 * 2. 第二种：使用#{userID}设置流程变量(userTaskProcess-2.bpmn)
 *
 * @author 三多
 * @Time 2019/6/19
 */
public class UserTaskTest {
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
                .name("个人任务")
                //从classpath中一次只能加载一个文件，使用的是userTaskProcess-2中配置
                .addClasspathResource("activiti/userTaskProcess.bpmn")
                .addClasspathResource("activiti/userTaskProcess.png")
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
        String processkey = "userTaskProcess";
        //启动任务，同时设置流程变量
        Map<String,Object> variables = new HashMap<>();
        //map 的可以对应userTaskProcess.bpmn 中任务的代理人 #{userID}
        variables.put("userID","周芷若");
        ProcessInstance instance = engine.getRuntimeService()
                //通过流程id或者流程key启动都行,key 对应  userTaskProcess.bpmn id 的值，或者act_re_procdef表中的key
                //因为按照key启动时，默认是按照最新的流程版本启动
                .startProcessInstanceByKey(processkey,variables);
        //启动流程实例，id：2501,流程定义的id：userTaskProcess:1:4
        System.out.println("启动流程实例，id：" + instance.getId() + ",流程定义的id：" + instance.getProcessDefinitionId());

    }

    /**
     * 结束
     */
    @Test
    public void completeTask() {
        //任务ID
        String taskId = "185005";
        engine.getTaskService()
                .complete(taskId);
        System.out.println("完成审批，任务id为:" + taskId);
    }

}
