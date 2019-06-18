package com.activity.app.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 员工实体类
 * @author 三多
 * @Time 2019/6/18
 */
@Data
public class Person implements Serializable {

    /**
     * 编号
     */
    private Integer id;
    /**
     * 姓名
     */
    private String name;
}
