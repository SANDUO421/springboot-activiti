package com.activity.app.demo01;

import com.activity.app.entity.Person;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 流程变量测试
 *
 * @author 三多
 * @Time 2019/6/18
 */
public class ProcessVariableTest {

    private ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 流的方式，部署流程定义
     */
    @Test
    public void deployProcessEngineer() {
        //第二种资源加载方式：通过类的加载器，路径必须没有 /,如果不加，就是从当前包地下找配置文件。
        InputStream inbpmn = this.getClass().getResourceAsStream("/activiti/processVariable.bpmn");
        InputStream inpng = this.getClass().getResourceAsStream("/activiti/processVariable.png");
        //与流程部署定义相关的service
        Deployment deploy = engine.getRepositoryService()
                //创建一个部署对象
                .createDeployment()
                .name("请假流程")
                .addInputStream("processVariable.bpmn", inbpmn)
                .addInputStream("processVariable.png", inpng)
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
        String processkey = "processVariable";
        //与正在执行的流程实例和执行对象相关的service
        ProcessInstance instance = engine.getRuntimeService()
                //通过流程id或者流程key启动都行,key 对应  processVariable.bpmn id 的值，或者act_re_procdef表中的key
                //因为按照key启动时，默认是按照最新的流程版本启动
                .startProcessInstanceByKey(processkey);
        //启动流程实例，id：2501,流程定义的id：processVariable:1:4
        System.out.println("启动流程实例，id：" + instance.getId() + ",流程定义的id：" + instance.getProcessDefinitionId());


    }

    /**
     * 设置流程变量
     */
    @Test
    public void setProcessVariable() {
        TaskService taskService = engine.getTaskService();
        String taskId = "50004";
        //setVariableLocal ：该参数只与当前的任务id绑定，其他任务id无法获取
        taskService.setVariableLocal(taskId, "请假天数", 3);
        //setVariable：可以传递到下一个流程任务id，但是如果下一个流程任务id可以重新设置并覆盖原先的值。
        taskService.setVariable(taskId, "请假日期", new Date());
        taskService.setVariable(taskId, "请假原因", "回家探亲!");
        System.out.println("设置流程变量成功！");
    }

    /**
     * 获取流程变量
     */
    @Test
    public void getProcessVariable() {
        TaskService taskService = engine.getTaskService();
        String taskId = "50004";
        /**使用基本了类型，获取流程变量
         * getVariableLocal ：该参数只与当前的任务id绑定，其他任务id无法获取
         * */
        Integer day = (Integer) taskService.getVariableLocal(taskId, "请假天数");
        System.out.println("请假天数：" + day);
        Map<String, Object> variables = taskService.getVariables(taskId);
        for (Map.Entry<String, Object> entity : variables.entrySet()) {
            System.out.println(entity.getKey() + "-> " + entity.getValue());
        }
        System.out.println("--------------------------");
        List<String> variableNames = new ArrayList<>();
        variableNames.add("请假日期");
        variableNames.add("请假原因");
        System.out.println("--------------------------");
        variables = taskService.getVariables(taskId, variableNames);
        for (Map.Entry<String, Object> entity : variables.entrySet()) {
            Object value = entity.getValue();
            String key = entity.getKey();
            if (key.equalsIgnoreCase("请假日期")) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                String format = dateFormat.format((Date) value);
                System.out.println(key + "-> " + format);
            }
            if (key.equalsIgnoreCase("请假原因")) {
                System.out.println(key + "-> " + value.toString());
            }
        }


    }

    /**
     * 模拟设置获取流程变量
     */
    @Test
    public void setAndGetProcessVariable() {
        //流程实例，执行对象（正在执行）
        RuntimeService runtimeService = engine.getRuntimeService();
        //任务（正在执行）
        TaskService taskService = engine.getTaskService();

        /**设置流程变量*/
        //runtimeService.setVariable(executionId, variableName, value);//表示使用执行ID，和流程变量名称，设置流程变量的值（一次只能设置一个流程变量）
        //runtimeService.setVariables(executionId, variables);//标识使用执行ID，和Map集合设置流程变量，map集合的可以就是流程变量的名称，value就是流程集合的值

        //taskService.setVariable(taskId, variableName, value);//表示使用任务ID，和流程变量名称，设置流程变量的值（一次只能设置一个流程变量）
        //taskService.setVariables(taskId, variables);//标识使用任务ID，和Map集合设置流程变量，map集合的可以就是流程变量的名称，value就是流程集合的值

        //runtimeService.startProcessInstanceByKey(processDefinitionKey,variables);//启动流程实例时，可以设置流程变量，map集合
        //taskService.complete(taskId, variables);//完成任务的同时，设置流程变量，用map集合

        /**获取流程变量*/
        //runtimeService.getVariable(executionId, variableName);//使用执行对象Id，和流程变量名称 ，获取流程变量的值
        //runtimeService.getVariables(executionId);//使用执行对象id，获取所有的流程变量的值。将流程变量放到Map集合中，map的可key是流程变量的名称，value是流程变量的值
        //runtimeService.getVariables(executionId, variableNames)//会用执行对象id，和流程变量名称的集合获取部分流程变量的值。将流程变量放到Map集合中，map的可key是流程变量的名称，value是流程变量的值。

        //taskService.getVariable(taskId, variableName);//使用任务Id，和流程变量名称 ，获取流程变量的值
        //taskService.getVariables(taskId);//使用任务id，获取所有的流程变量的值。将流程变量放到Map集合中，map的可key是流程变量的名称，value是流程变量的值
        //taskService.getVariables(taskId, variableNames)//会用任务id，和流程变量名称的集合获取部分流程变量的值。将流程变量放到Map集合中，map的可key是流程变量的名称，value是流程变量的值。
    }


    /**
     * java 设置流程变量：
     * 注意：当javaBean（实现了序列化） 放到流程变量中时，要求javaBean的属性不能再发生变化
     * 如果发生变化，再次获取的时候，就会抛出异常
     * 解决方案：
     * 在JavaBean中添加一个序列化ID，同时实现序列化接口
     */
    @Test
    public void setProcessVariableByJavaBean() {
        TaskService taskService = engine.getTaskService();
        String taskId = "62504";
        Person person = new Person();
        person.setId(10);
        person.setName("sanduo");
        taskService.setVariable(taskId, "人员基本信息", person);
        System.out.println("设置流程变量成功！");
    }

    /**
     * java 获取流程变量
     */
    @Test
    public void getProcessVariableByJavaBean() {
        TaskService taskService = engine.getTaskService();
        String taskId = "62504";
        Person person = (Person) taskService.getVariable(taskId, "人员基本信息");
        System.out.println("获取流程变量成功，值为：" + person);
    }


    /**
     * 完成我的任务张晓
     */
    @Test
    public void completeZhangXiaoTask() {
        //任务ID
        String taskId = "67502";
        engine.getTaskService()
                .complete(taskId);
        System.out.println("张晓完成审批，任务id为:" + taskId);
    }

    /**
     * 查询流程变量的历史表
     */
    @Test
    public void queryHistoryProcessVariable() {
        List<HistoricVariableInstance> list = engine.getHistoryService()
                .createHistoricVariableInstanceQuery()
                //模糊查询
                .variableNameLike("请假天数")
                .list();
        for (HistoricVariableInstance hiv : list) {
            System.out.println(hiv.getId() + " " + hiv.getVariableName() + " " + hiv.getVariableTypeName() + " " + hiv.getValue() + " " + hiv.getProcessInstanceId() + " " + hiv.getCreateTime());
            System.out.println("#######################");
        }

    }


}
