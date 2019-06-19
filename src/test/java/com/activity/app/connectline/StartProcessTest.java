package com.activity.app.connectline;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

/**
 * 开始流程节点
 *
 * @author 三多
 * @Time 2019/6/19
 */
public class StartProcessTest {

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
                .addClasspathResource("activiti/startProcess.bpmn")
                .addClasspathResource("activiti/startProcess.png")
                .deploy();//完成部署
        System.out.println("部署相关信息：id:" + deploy.getId() + "，Category:" + deploy.getCategory() + "，Name:" + deploy.getName()
                + "，TenantId:" + deploy.getTenantId() + "，DeploymentTime:" + deploy.getDeploymentTime());
    }

    /**
     * 启动流程实例+ 判断流程实例状态（是否结束）+查询历史
     */
    @Test
    public void runProcessInstance() {
        //流程定义的key
        String processkey = "startProcess";
        //与正在执行的流程实例和执行对象相关的service
        ProcessInstance instance = engine.getRuntimeService()
                //通过流程id或者流程key启动都行,key 对应  startProcess.bpmn id 的值，或者act_re_procdef表中的key
                //因为按照key启动时，默认是按照最新的流程版本启动
                .startProcessInstanceByKey(processkey);
        //启动流程实例，id：2501,流程定义的id：hellowordProcess:1:4
        System.out.println("启动流程实例，id：" + instance.getId() + ",流程定义的id：" + instance.getProcessDefinitionId());

        /**判断流程是否结束，查询执行对象的对象表*/
        ProcessInstance rpi = engine.getRuntimeService()
                .createProcessInstanceQuery()
                .processInstanceId(instance.getId())
                .singleResult();
        /**流程实例结束*/
        if (rpi == null) {
            HistoricProcessInstance hpi = engine.getHistoryService()
                    .createHistoricProcessInstanceQuery()
                    .processInstanceId(instance.getId())
                    .singleResult();
            System.out.println(hpi.getId()+" "+ hpi.getStartTime()+" "+hpi.getEndTime()+" "+hpi.getDurationInMillis());
        }
    }


}
