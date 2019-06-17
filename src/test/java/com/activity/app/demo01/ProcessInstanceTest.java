package com.activity.app.demo01;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * 流程审批
 *
 * @author 三多
 * @Time 2019/6/17
 */
public class ProcessInstanceTest {
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
                .name("请假流程")
                //从classpath中一次只能加载一个文件
                .addClasspathResource("activiti/hellowordProcess.bpmn")
                .addClasspathResource("activiti/hellowordProcess.png")
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
        String processkey = "hellowordProcess";
        //与正在执行的流程实例和执行对象相关的service
        ProcessInstance instance = engine.getRuntimeService()
                //通过流程id或者流程key启动都行,key 对应  hellowordProcess.bpmn id 的值，或者act_re_procdef表中的key
                //因为按照key启动时，默认是按照最新的流程版本启动
                .startProcessInstanceByKey(processkey);
        //启动流程实例，id：2501,流程定义的id：hellowordProcess:1:4
        System.out.println("启动流程实例，id：" + instance.getId() + ",流程定义的id：" + instance.getProcessDefinitionId());


    }

    /**
     * 查询当前人的个人任务（张三）
     */
    @Test
    public void listZhangSanTask() {
        String assignee = "张三";
        List<Task> tasks = engine.getTaskService()
                .createTaskQuery()
                .taskAssignee(assignee)

                //.taskCandidateUser(candidateUser)//组任务的办理人查询
                //.processDefinitionId(processDefinitionId//使用流程定义Id查询
                //.processInstanceId(processInstanceId)//使用流程实例ID查询
                //.executionId(executionId)//使用执行对象ID查询
                /**排序*/
                //使用创建时间的升序排列
                .orderByTaskCreateTime().asc()
                /**返回结果集*/
                //.listPage(irstResult, maxResults)//分页查询
                //.count()//返回结果集数量
                //.singleResult() //返回唯一结果集
                .list();

        if (tasks != null && tasks.size() > 0) {
            for (Task task : tasks) {
                System.out.println("任务ID：" + task.getId());
                System.out.println("任务名称：" + task.getName());
                System.out.println("任务创建时间：" + task.getCreateTime());
                System.out.println("任务的办理人：" + task.getAssignee());
                System.out.println("流程实例：" + task.getProcessInstanceId());
                System.out.println("执行对象ID：" + task.getExecutionId());
                System.out.println("流程定义ID：" + task.getProcessDefinitionId());
            }
        }
    }

    /**
     * 完成我的任务张三
     */
    @Test
    public void completeZhangSanTask() {
        //任务ID
        String taskId = "32504";
        engine.getTaskService()
                .complete(taskId);
        System.out.println("张三完成审批，任务id为:" + taskId);
    }

    /**
     * 查询当前人的个人任务（李四）
     */
    @Test
    public void listLisiTask() {
        String assignee = "李四";
        List<Task> tasks = engine.getTaskService()
                .createTaskQuery()
                .taskAssignee(assignee)
                .list();
        if (tasks != null && tasks.size() > 0) {
            for (Task task : tasks) {
                System.out.println("任务ID：" + task.getId());
                System.out.println("任务名称：" + task.getName());
                System.out.println("任务创建时间：" + task.getCreateTime());
                System.out.println("任务的办理人：" + task.getAssignee());
                System.out.println("流程实例：" + task.getProcessInstanceId());
                System.out.println("执行对象ID：" + task.getExecutionId());
                System.out.println("流程定义ID：" + task.getProcessDefinitionId());
            }
        }

    }

    /**
     * 完成我的任务（李四）
     */
    @Test
    public void completeLisiTask() {
        //任务ID
        String taskId = "35002";
        engine.getTaskService()
                .complete(taskId);
        System.out.println("李四完成审批，任务id为:" + taskId);
    }

    /**
     * 查询当前人的个人任务（王五）
     */
    @Test
    public void listWangWuTask() {
        String assignee = "王五";
        List<Task> tasks = engine.getTaskService()
                .createTaskQuery()
                .taskAssignee(assignee)
                .list();
        if (tasks != null && tasks.size() > 0) {
            for (Task task : tasks) {
                System.out.println("任务ID：" + task.getId());
                System.out.println("任务名称：" + task.getName());
                System.out.println("任务创建时间：" + task.getCreateTime());
                System.out.println("任务的办理人：" + task.getAssignee());
                System.out.println("流程实例：" + task.getProcessInstanceId());
                System.out.println("执行对象ID：" + task.getExecutionId());
                System.out.println("流程定义ID：" + task.getProcessDefinitionId());
            }
        }

    }

    /**
     * 完成我的任务（王五）
     */
    @Test
    public void completeWangWuTask() {
        //任务ID
        String taskId = "37502";
        engine.getTaskService()
                .complete(taskId);
        System.out.println("王五完成审批，任务id为:" + taskId);
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

    /**
     * 查询历史任务
     */
    @Test
    public void queryHistoryTask() {
        String taskAssignee = "张三";
        //与历史表相关的service
        List<HistoricTaskInstance> list = engine.getHistoryService()
                //创建历史任务实例查询
                .createHistoricTaskInstanceQuery()
                //指定历史任务办理人
                .taskAssignee(taskAssignee)
                .list();
        if (list != null && list.size() > 0) {
            for (HistoricTaskInstance instance : list) {

                System.out.println(instance.getId() + " " + instance.getName()
                        + " " + instance.getProcessInstanceId() + " " + instance.getStartTime()
                        + " " + instance.getDurationInMillis());
                System.out.println("###############################");
            }

        }

    }

    /**
     * 查询历史流程实例
     */
    @Test
    public void queryHistoryProcessInstance() {
        String processInstanceId = "32501";
        HistoricProcessInstance singleResult = engine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        if (singleResult != null) {
            System.out.println(singleResult.getId() + " " + singleResult.getName() + " "
                    + singleResult.getProcessDefinitionId() + " " + singleResult.getStartTime()
                    + " " + singleResult.getEndTime() + " " + singleResult.getDurationInMillis());
        }
    }

}
