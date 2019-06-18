# Activiti 流

## 参考资料

1. [IDEA中安装activiti并使用](https://blog.csdn.net/qq_41728540/article/details/79506463)
2. [201９最全Activiti６.０框架自动创建表的３种方式](https://blog.csdn.net/x15011238662/article/details/86503913)

## 实战
### 基本的CRUD (主要是针对sql/table.sql中的表操作)
1. 表上的创建：com.activity.app.test.HelloWord
2. 流程部署：com.activity.app.demo01.ProcessDeployTest
3. 流程定义：com.activity.app.demo01.ProcessDefinitionTest
4. 流程实例：com.activity.app.demo01.ProcessInstanceTest
5. 流程实例：com.activity.app.demo01.ProcessVariableTest
6. 流程实例：com.activity.app.demo01.HistoryQueryTest

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