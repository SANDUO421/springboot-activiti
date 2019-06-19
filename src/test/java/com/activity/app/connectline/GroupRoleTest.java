package com.activity.app.connectline;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

/**
 * 组角色: 一个角色可以对应多个人
 * 相当一个组任务
 * @author 三多
 * @Time 2019/6/19
 */
public class GroupRoleTest {
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
                //从classpath中一次只能加载一个文件
                .addClasspathResource("activiti/groupRoleProcess.bpmn")
                .addClasspathResource("activiti/groupRoleProcess.png")
                .deploy();//完成部署
        System.out.println("部署相关信息：id:" + deploy.getId() + " " + deploy.getName());

        /**组织架构：创建组，用户，并建立关联关系*/
        IdentityService identityService = engine.getIdentityService();
        //创建部门
        identityService.saveGroup(new GroupEntity("总经理"));
        identityService.saveGroup(new GroupEntity("部门经理"));
        //创建角色
        identityService.saveUser(new UserEntity("张三"));
        identityService.saveUser(new UserEntity("李四"));
        identityService.saveUser(new UserEntity("王五"));
        //创建关联关系
        identityService.createMembership("张三","总经理");
        identityService.createMembership("李四","部门经理");
        identityService.createMembership("王五","部门经理");

        System.out.println("添加组织机构成功");

    }

    /**
     * 启动流程实例+设置流程变量+获取流程变量+向后执行一步
     */
    @Test
    public void runProcessInstance() {
        //流程定义的key
        String processkey = "groupRoleProcess";
        //使用 监听指定流程办理人
        ProcessInstance instance = engine.getRuntimeService()
                //通过流程id或者流程key启动都行,key 对应  userTaskProcess.bpmn id 的值，或者act_re_procdef表中的key
                //因为按照key启动时，默认是按照最新的流程版本启动
                .startProcessInstanceByKey(processkey);
        //启动流程实例，id：2501,流程定义的id：userTaskProcess:1:4
        System.out.println("启动流程实例，id：" + instance.getId() + ",流程定义的id：" + instance.getProcessDefinitionId());

    }

    /**
     * 相当一个组任务
     * 拾取(领取)任务，将组任务分配给个人任务，指定任务的办理人字段
     * 认领任务
     */
    @Test
    public void claim(){
        //任务ID
        String taskId="237504";
        //分配的个人任务（可以是组任务中的组员，也可以是非组任务的组员）
        String userId="李四";
        engine.getTaskService()
                .claim(taskId,userId);

    }

    /**
     * 结束
     */
    @Test
    public void completeTask() {
        //任务ID
        String taskId = "237504";
        engine.getTaskService()
                .complete(taskId);
        System.out.println("完成审批，任务id为:" + taskId);
    }

}
