package com.deltaqin.p12json;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * @author shiqiao.qzt
 * @Classname DO
 * @Description TODO
 * @Date 2021/11/26 11:11 上午
 */
@Data
@AllArgsConstructor
public class JsonDO {
    private Integer Id;
    private Map<Integer, Integer> map1;
    private Map<Integer, Integer> map2;
}
