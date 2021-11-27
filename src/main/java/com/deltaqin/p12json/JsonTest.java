package com.deltaqin.p12json;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shiqiao.qzt
 * @Classname JsonTest
 * @Description TODO
 * @Date 2021/11/26 1:36 下午
 */
public class JsonTest {
    @Test
    public void test2() {
        HashMap<Integer, Integer> map1 = new HashMap<>();
        map1.put(1, 1);
        map1.put(2, 2);
        HashMap<Integer, Integer> map2 = new HashMap<>();
        map2.put(3, 3);
        map2.put(4, 4);

        // 可以直接将对象设置为对应的属性，即使是复杂的对象
        JsonDO jsonDO = new JsonDO(1, map1, map2);
        String s = JSONObject.toJSONString(jsonDO);

        JsonDO objDO = JSONObject.parseObject(s, JsonDO.class);
        System.out.println(objDO.toString());

        // 即使 字段是自定义的对象，只要是指定了对象的class
        JsonDoDO jsonDoDO = new JsonDoDO();
        jsonDoDO.setJsonDO(jsonDO);
        String s1 = JSONObject.toJSONString(jsonDoDO);
        JsonDoDO jsonDoDO1 = JSONObject.parseObject(s1, JsonDoDO.class);
        System.out.println(jsonDoDO1.toString());
    }

    /**
     * 将json对象转换为HashMap
     * @param json
     * @return
     */
    public static Map<Long, Long> parseJSON2Map(JSONObject json) {
        Map<Long, Long> map = new HashMap<>();
        // 最外层解析
        for (Object k : json.keySet()) {
            long v = (long)json.get(k);
            map.put((long)k, v);
        }
        return map;
    }
}
