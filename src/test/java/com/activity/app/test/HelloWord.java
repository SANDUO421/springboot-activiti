package com.activity.app.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

/**
 * @author 三多
 * @Time 2019/6/4
 */
public class HelloWord {
    /**
     * 使用activiti框架完成自动创建表（不设置配置文件）
     */
    @Test
    public void test01() {
        //创建一个流程成引擎对像
        ProcessEngineConfiguration conf = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration();

        //设置数据源
        conf.setJdbcDriver("com.mysql.jdbc.Driver");
        conf.setJdbcUrl("jdbc:mysql://192.168.1.166:3306/activity");
        conf.setJdbcUsername("root");
        conf.setJdbcPassword("developer@lydsj");

        //设置自动创建表
        conf.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        //在创建引擎对象的时候自动创建表
        ProcessEngine processEngine = conf.buildProcessEngine();
        System.out.println(processEngine);
    }

    @Test
    public void test02() {
        ProcessEngineConfiguration conf = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        //在创建引擎对象的时候自动创建表
        ProcessEngine processEngine = conf.buildProcessEngine();
        System.out.println(processEngine);
    }

}
