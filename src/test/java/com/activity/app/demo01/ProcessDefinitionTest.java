package com.activity.app.demo01;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

/**
 * 流程定义
 * @author 三多
 * @Time 2019/6/17
 */
public class ProcessDefinitionTest {

    private ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义 classpath 部署
     */
    @Test
    public void deployProcessEngineer_class() {
        //与流程部署定义相关的service
        Deployment deploy = engine.getRepositoryService()
                //创建一个部署对象
                .createDeployment()
                .name("流程定义")
                //从classpath中一次只能加载一个文件
                .addClasspathResource("activiti/hellowordProcess.bpmn")
                .addClasspathResource("activiti/hellowordProcess.png")
                .deploy();//完成部署
        System.out.println("部署id:" + deploy.getId());
        System.out.println("部署名称Name:" + deploy.getName());
    }

    /**
     * 部署流程定义 zip 部署
     */
    @Test
    public void deployProcessEngineer_zip() {
        //与流程部署定义相关的service
        ZipInputStream zin = new ZipInputStream(this.getClass().getClassLoader().getResourceAsStream("activiti/helloword.zip"));
        Deployment deploy = engine.getRepositoryService()
                //创建一个部署对象
                .createDeployment()
                .name("流程定义")
                //zip部署
                .addZipInputStream(zin)
                .deploy();//完成部署
        System.out.println("部署id:" + deploy.getId());
        System.out.println("部署名称Name:" + deploy.getName());
    }

    /**
     * 查找流程定义
     */
    @Test
    public void listProcessDefine() {
        //创建流程定义的相关service
        List<ProcessDefinition> list = engine.getRepositoryService()
                //创建流程定义查询
                .createProcessDefinitionQuery()
                //.deploymentId(deploymentId) //使用部署Id查询
                //.processDefinitionId(processDefinitionId)//使用流程ID查询
                //.processDefinitionKey(processDefinitionKey)//使用流程定义的key查询
                //.processDefinitionName(processDefinitionName)//使用流程定义的名称查询
                //.processDefinitionNameLike(processDefinitionNameLike)//使用流程定义的名称模糊查询

                //排序
                .orderByProcessDefinitionVersion().asc()
                //.orderByProcessDefinitionName().desc()

                // 结果集
                .list();
        //.singleResult()
        //.count()
        //.listPage(firstResult,maxResults)

        if (list != null && list.size() > 0) {
            for (ProcessDefinition definition : list) {
                System.out.println("流程定义的ID:" + definition.getId());
                System.out.println("流程定义的名称:" + definition.getName());
                System.out.println("流程定义的Key:" + definition.getKey());
                System.out.println("流程定义版本:" + definition.getVersion());
                System.out.println("部署对象ID:" + definition.getDeploymentId());
                System.out.println("流资源名称 bpmn:" + definition.getResourceName());
                System.out.println("流资源名称 png:" + definition.getDiagramResourceName());
                System.out.println("########################################");
            }
        }

    }

    /**
     * 删除流程定义
     */
    @Test
    public void deleteProcessDefine() {

        String deploymentId = "10001";
        engine.getRepositoryService()
                // 只能删除未启动的流程定义，如果启动就会报异常，不带级联删除。
                //.deleteDeployment(deploymentId);
                //级联删除，不管流程是否启动，都会删除
                .deleteDeployment(deploymentId, true);
        System.out.println("删除流程成功：" + deploymentId);
    }

    /**
     * 将资源文件写到本地磁盘
     */
    @Test
    public void outputPng() throws IOException {
        //部署iD
        String deploymentId = "22501";
        //定义图片资源的名称
        String resourceName = "";
        List<String> list = engine.getRepositoryService()
                .getDeploymentResourceNames(deploymentId);
        if (list != null && list.size() > 0) {
            for (String name : list) {
                if (name.indexOf(".png") >= 0) {
                    resourceName = name;
                }
            }
        }
        InputStream in = engine.getRepositoryService()
                .getResourceAsStream(deploymentId, resourceName);

        File out = new File("out/" + resourceName);
        FileUtils.copyInputStreamToFile(in, out);
    }

    /**
     * 查询最新版本的流程定义
     */
    @Test
    public void queryLastVersionProcessDefine() {
        //获取流程版本定义列表，并对版本做升序排列
        List<ProcessDefinition> list = engine.getRepositoryService()
                .createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion().asc()
                .list();
        /**
         *  Map<String,ProcessDefinition>
         *  map集合的key：流程定义的key
         *  map集合的value：流程定义对象
         *  map 集合的特点：当map集合的key值相同时，后一次会替换前一次的值
         */
        Map<String, ProcessDefinition> map = new LinkedHashMap<String, ProcessDefinition>();
        if (list != null && list.size() > 0) {
            for (ProcessDefinition definition : list) {
                map.put(definition.getKey(), definition);

            }
        }
        List<ProcessDefinition> toList = new ArrayList(map.values());
        if (toList != null && toList.size() > 0) {
            for (ProcessDefinition definition : toList) {
                System.out.println("流程定义的ID:" + definition.getId());
                System.out.println("流程定义的名称:" + definition.getName());
                System.out.println("流程定义的Key:" + definition.getKey());
                System.out.println("流程定义版本:" + definition.getVersion());
                System.out.println("部署对象ID:" + definition.getDeploymentId());
                System.out.println("流资源名称 bpmn:" + definition.getResourceName());
                System.out.println("流资源名称 png:" + definition.getDiagramResourceName());
                System.out.println("########################################");
            }
        }
    }

    /**
     * 删除流程定义（删除key相同的所有不同版本的流程定义）
     */
    @Test
    public void deleteProcessDefineByKey() {

        String key = "hellowordProcess";
        String deploymentId = "";
        List<ProcessDefinition> list = engine.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionKey(key)
                .list();
        if (list != null && list.size() > 0) {
            for (ProcessDefinition definition : list) {
                deploymentId = definition.getDeploymentId();
                engine.getRepositoryService()
                        // 只能删除未启动的流程定义，如果启动就会报异常，不带级联删除。
                        //.deleteDeployment(deploymentId);
                        //级联删除，不管流程是否启动，都会删除
                        .deleteDeployment(deploymentId, true);
                System.out.println("删除流程成功：" + deploymentId);
            }
        }

    }
}
