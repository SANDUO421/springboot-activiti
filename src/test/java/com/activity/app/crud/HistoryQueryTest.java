package com.activity.app.crud;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.junit.Test;

import java.util.List;

/**
 * 历史查询
 *
 * @author 三多
 * @Time 2019/6/18
 */
public class HistoryQueryTest {

    private ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 查询历史流程实例
     */
    @Test
    public void queryHistoryProcessInstance() {
        String processInstanceId = "62501";
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

    /**
     * 查询历史活动
     */
    @Test
    public void queryHistoryActivity() {
        String processInstanceId = "32501";
        List<HistoricActivityInstance> list = engine.getHistoryService()
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
        for (HistoricActivityInstance instance : list) {
            System.out.println(instance.getId() + " " + instance.getProcessDefinitionId() + " " +
                    instance.getExecutionId() + " " + instance.getStartTime() + " " + instance.getEndTime() + " "
                    + instance.getDurationInMillis());
            System.out.println("####################################");
        }
    }

    /**
     * 查询历史任务
     */
    @Test
    public void queryHistoryTask() {
        String processInstanceId = "32501";
        //与历史表相关的service
        List<HistoricTaskInstance> list = engine.getHistoryService()
                //创建历史任务实例查询
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByTaskCreateTime().asc()
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

    /**查询历史流程变量*/
    @Test
    public void queryHistoryProcessVariable() {
        String processInstanceId = "50001";
        //与历史表相关的service
        List<HistoricVariableInstance> list = engine.getHistoryService()
                //创建历史任务实例查询
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
        if (list != null && list.size() > 0) {
            for (HistoricVariableInstance instance : list) {

                System.out.println(instance.getId() + " " + instance.getVariableTypeName()
                        + " " + instance.getProcessInstanceId() + " " + instance.getTaskId()
                        + " " + instance.getLastUpdatedTime());
                System.out.println("###############################");
            }

        }

    }


}
