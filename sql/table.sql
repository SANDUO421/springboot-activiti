-- 部署对象和流程定义相关表--

# 部署对象表
select  * from  act_re_deployment
#流程定义表
select * from act_re_procdef
#资源文件表
select * from act_ge_bytearray
#文件生成表
select * from act_ge_property


-- 流程实例、执行对象，任务

#正在执行对象表
select * from act_ru_execution

#流程实例历史表
select * from act_hi_procinst

#正在执行的任务表（只有节点是UserTask的时候,该表才有数据）
select * from act_ru_task

#任务的历史表（只有节点是UserTask的时候,该表才有数据）
select * from act_hi_taskinst

#所有活动节点的历史表
select * from act_hi_actinst


--流程变量

#正在执行的流程变量表
select * from act_ru_variable
#历史的流程变量表
select * from act_hi_varinst
