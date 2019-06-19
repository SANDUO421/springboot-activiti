package com.activity.app.connectline.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 动态监听
 * @author 三多
 * @Time 2019/6/19
 */
public class UserTask1ListenerImpl implements TaskListener {
    /**
     * 用于指定任务的代理人
     * @param delegateTask
     */
    @Override
    public void notify(DelegateTask delegateTask) {

        //可以指定个人任务的办理人、也可以指定组任务的办理人
        String assignee="灭绝师太";
        //个人任务：通过类去查询数据库，将下一个任务的办理人获取，然后通过setAssignee()的方法指定办理人。
        delegateTask.setAssignee(assignee);
        //组任务
        //List<String> candidateUsers = new ArrayList<>();
        //candidateUsers.add("A");
        //candidateUsers.add("B");
        //candidateUsers.add("C");
        //delegateTask.addCandidateUsers(candidateUsers);
        //或者
        //delegateTask.addCandidateUser("A");
        //delegateTask.addCandidateUser("B");

    }
}
