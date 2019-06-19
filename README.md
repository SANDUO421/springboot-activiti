# Activiti 流

## 参考资料

1. [IDEA中安装activiti并使用](https://blog.csdn.net/qq_41728540/article/details/79506463)
2. [201９最全Activiti６.０框架自动创建表的３种方式](https://blog.csdn.net/x15011238662/article/details/86503913)

## 实战
### 基本的CRUD (主要是针对sql/table.sql中的表操作)
1. 表上的创建：com.activity.app.test.HelloWord
2. 流程部署：com.activity.app.crud.ProcessDeployTest
3. 流程定义：com.activity.app.crud.ProcessDefinitionTest
4. 流程实例：com.activity.app.crud.ProcessInstanceTest
5. 流程实例：com.activity.app.crud.ProcessVariableTest
6. 流程实例：com.activity.app.crud.HistoryQueryTest

### 提高

1. 连线：com.activity.app.connectline.SequenceFlowTest
2. 排他网关：com.activity.app.connectline.ExclusiveGateWayTest
3. 并行网关：com.activity.app.connectline.ParallelGateWayTest
4. 开始流程节点：com.activity.app.connectline.StartProcessTest
5. 接受任务测试：com.activity.app.connectline.ReceiveTaskTest
6. 个人任务测试（第一、二种方式）：com.activity.app.connectline.UserTaskTest
7. 个人任务测试（第三种方式，查数据库）：com.activity.app.connectline.UserTask1Test
8. 组+角色+拾取的管理：com.activity.app.connectline.GroupRoleTest
9. 组任务+拾取：com.activity.app.connectline.GroupTaskTest


## 问题

1. 连接Mysql编码问题

```
//连接Mysql数据库的时候
　　　　 String url = "jdbc:mysql://localhost:3306/credit?autoReconnect=true&useUnicode=true&characterEncoding=utf8";
   　  String userName = "root";
        String password="root";
//应该注意设置字符的编码；
//如果是在XML文件中配置,则是 jdbc:mysql://localhost:3306/credit?autoReconnect=true&amp;useUnicode=true&amp;characterEncoding=utf8； 因为&amp;就是&符号。
```