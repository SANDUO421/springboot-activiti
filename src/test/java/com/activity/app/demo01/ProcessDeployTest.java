package com.activity.app.demo01;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * 流程部署
 * 模拟流程审批： 张三->李四->王五
 * @author 三多
 * @Time 2019/6/17
 */
public class ProcessDeployTest {
    private  ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义
     */
    @Test
    public  void  deployProcessEngineer(){
        //与流程部署定义相关的service
        Deployment deploy = engine.getRepositoryService()
                //创建一个部署对象
                .createDeployment()
                .name("helloWord,入门程序")
                //从classpath中一次只能加载一个文件
                .addClasspathResource("activiti/hellowordProcess.bpmn")
                .addClasspathResource("activiti/hellowordProcess.png")
                .deploy();//完成部署
        System.out.println("部署相关信息：id:"+ deploy.getId()+"，Category:"+deploy.getCategory()+"，Name:"+deploy.getName()
                +"，TenantId:"+deploy.getTenantId()+"，DeploymentTime:"+deploy.getDeploymentTime());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void runHellwordProcess(){
        //流程定义的key
        String processkey = "hellowordProcess";
        //与正在执行的流程实例和执行对象相关的service
        ProcessInstance instance = engine.getRuntimeService()
                //通过流程id或者流程key启动都行,key 对应  hellowordProcess.bpmn id 的值，或者act_re_procdef表中的key
                //因为按照key启动时，默认是按照最新的流程版本启动
                .startProcessInstanceByKey(processkey);
        //启动流程实例，id：2501,流程定义的id：hellowordProcess:1:4
        System.out.println("启动流程实例，id："+instance.getId() + ",流程定义的id："+ instance.getProcessDefinitionId());


    }

    /**
     * 查询当前人的个人任务（张三）
     *
     */
    @Test
    public void listZhangSanTask(){
        String assignee = "张三";
        List<Task> tasks = engine.getTaskService()
                .createTaskQuery()
                .taskAssignee(assignee)
                .list();
        if (tasks != null && tasks.size() >0){
            for (Task task : tasks) {
                System.out.println("任务ID："+ task.getId());
                System.out.println("任务名称："+ task.getName());
                System.out.println("任务创建时间："+ task.getCreateTime());
                System.out.println("任务的办理人："+ task.getAssignee());
                System.out.println("流程实例："+ task.getProcessInstanceId());
                System.out.println("执行对象ID："+ task.getExecutionId());
                System.out.println("流程定义ID："+ task.getProcessDefinitionId());
            }
        }

    }
    /**
     * 完成我的任务
     *
     */
    @Test
    public void completeZhangSanTask() {
        //任务ID
        String taskId = "12504";
        engine.getTaskService()
                .complete(taskId);
        System.out.println("张三完成审批，任务id为:"+ taskId);
    }

    /**
     * 查询当前人的个人任务（李四）
     *
     */
    @Test
    public void listLisiTask(){
        String assignee = "李四";
        List<Task> tasks = engine.getTaskService()
                .createTaskQuery()
                .taskAssignee(assignee)
                .list();
        if (tasks != null && tasks.size() >0){
            for (Task task : tasks) {
                System.out.println("任务ID："+ task.getId());
                System.out.println("任务名称："+ task.getName());
                System.out.println("任务创建时间："+ task.getCreateTime());
                System.out.println("任务的办理人："+ task.getAssignee());
                System.out.println("流程实例："+ task.getProcessInstanceId());
                System.out.println("执行对象ID："+ task.getExecutionId());
                System.out.println("流程定义ID："+ task.getProcessDefinitionId());
            }
        }

    }

    /**
     * 完成我的任务（李四）
     *
     */
    @Test
    public void completeLisiTask() {
        //任务ID
        String taskId = "15002";
        engine.getTaskService()
                .complete(taskId);
        System.out.println("李四完成审批，任务id为:"+ taskId);
    }
    /**
     * 查询当前人的个人任务（王五）
     *
     */
    @Test
    public void listWangWuTask(){
        String assignee = "王五";
        List<Task> tasks = engine.getTaskService()
                .createTaskQuery()
                .taskAssignee(assignee)
                .list();
        if (tasks != null && tasks.size() >0){
            for (Task task : tasks) {
                System.out.println("任务ID："+ task.getId());
                System.out.println("任务名称："+ task.getName());
                System.out.println("任务创建时间："+ task.getCreateTime());
                System.out.println("任务的办理人："+ task.getAssignee());
                System.out.println("流程实例："+ task.getProcessInstanceId());
                System.out.println("执行对象ID："+ task.getExecutionId());
                System.out.println("流程定义ID："+ task.getProcessDefinitionId());
            }
        }

    }

    /**
     * 完成我的任务（王五）
     *
     */
    @Test
    public void completeWangWuTask() {
        //任务ID
        String taskId = "17502";
        engine.getTaskService()
                .complete(taskId);
        System.out.println("王五完成审批，任务id为:"+ taskId);
    }

}
