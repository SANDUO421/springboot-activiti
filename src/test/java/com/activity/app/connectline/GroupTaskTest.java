package com.activity.app.connectline;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * 组任务测试代理人
 *
 * @author 三多
 * @Time 2019/6/19
 */
public class GroupTaskTest {
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
                .name("组任务")
                //从classpath中一次只能加载一个文件
                .addClasspathResource("activiti/groupTaskProcess.bpmn")
                .addClasspathResource("activiti/groupTaskProcess.png")
                .deploy();//完成部署
        System.out.println("部署相关信息：id:" + deploy.getId() + " " + deploy.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void runProcessInstance() {
        //流程定义的key
        String processkey = "groupTaskProcess";
        //使用 监听指定流程办理人
        ProcessInstance instance = engine.getRuntimeService()
                //通过流程id或者流程key启动都行,key 对应  userTaskProcess.bpmn id 的值，或者act_re_procdef表中的key
                //因为按照key启动时，默认是按照最新的流程版本启动
                .startProcessInstanceByKey(processkey);
        //启动流程实例，id：2501,流程定义的id：userTaskProcess:1:4
        System.out.println("启动流程实例，id：" + instance.getId() + ",流程定义的id：" + instance.getProcessDefinitionId());

    }

    /**
     * 查询组任务
     */
    @Test
    public void queryGroupTask() {
        String candidateUser = "小A";
        //与历史表相关的service
        List<Task> list = engine.getTaskService()
                //创建历史任务实例查询
                .createTaskQuery()
                //指定历史任务办理人
                .taskCandidateUser(candidateUser)
                .list();
        if (list != null && list.size() > 0) {
            for (Task instance : list) {
                System.out.println("任务ID" + instance.getId());
                System.out.println("任务名称" + instance.getName());
                System.out.println("任务执行ID" + instance.getExecutionId());
                System.out.println("代理人" + instance.getAssignee());
                System.out.println("创建时间" + instance.getCreateTime());

                System.out.println("###############################");
            }
        }
    }



    /**
     * 重新指定代理人：从一个人到另一个人
     */
    @Test
    public void setAssigneeTask() {
        //任务ID
        String taskId = "200004";
        String userId = "张翠山";
        engine.getTaskService()
                .setAssignee(taskId, userId);
        System.out.println("完成审批，任务id为:" + taskId);
    }

    /**
     * 查询正在执行的任务办理人表
     */
    @Test
    public void queryRunPersonTask() {
        String taskId = "215004";
        List<IdentityLink> list = engine.getTaskService()
                .getIdentityLinksForTask(taskId);
        if (list != null && list.size() > 0) {
            for (IdentityLink link : list) {
                System.out.println(link.getUserId()+" "+link.getTaskId()+" "+link.getProcessInstanceId()+" "+link.getType());
            }
        }
    }

    /**
     * 查询历史任务的办理人
     */
    @Test
    public void queryHistoryPersonTask(){
        String processInstanceId="215001";
        List<HistoricIdentityLink> list = engine.getHistoryService()
                .getHistoricIdentityLinksForProcessInstance(processInstanceId);
        if (list != null && list.size() > 0) {
            for (HistoricIdentityLink link : list) {
                System.out.println(link.getTaskId()+" "+ link.getUserId()+" "+ link.getProcessInstanceId()+" "+link.getType());
            }
        }
    }
    /**
     * 拾取(领取)任务，将组任务分配给个人任务，指定任务的办理人字段
     * 认领任务
     */
    @Test
    public void claim(){
        //任务ID
        String taskId="215004";
       //分配的个人任务（可以是组任务中的组员，也可以是非组任务的组员）
        String userId="小A";
        engine.getTaskService()
                .claim(taskId,userId);

    }

    /**
     * 将个人任务回退到组任务，前提是，之前一定是一个组任务
     */
    @Test
    public void setAssignee(){
        //任务ID
        String taskId="215004";
        engine.getTaskService()
                .setAssignee(taskId,null);
    }
    /**
     * 向组任务中添加成员
     */
    @Test
    public void addGroupUser(){
        //任务ID
        String taskId="215004";
        //添加组员
        String userId="大H";
        engine.getTaskService()
                .addCandidateUser(taskId,userId);

    }
    /**
     * 从组任务中删除成员
     */
    @Test
    public void deleteGroupUser(){
        //任务ID
        String taskId="215004";
        //添加组员
        String userId="大H";
        engine.getTaskService()
                .deleteCandidateUser(taskId,userId);
    }
    /**
     * 结束
     */
    @Test
    public void completeTask() {
        //任务ID
        String taskId = "215004";
        engine.getTaskService()
                .complete(taskId);
        System.out.println("完成审批，任务id为:" + taskId);
    }
}
