package com.deltaqin.p11rpc.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author deltaqin
 * @date 2021/10/14 下午1:00
 */
@Data
public class User implements Serializable {
    private long uid;
    private short age;
    private short sex;
    private String name;
}
