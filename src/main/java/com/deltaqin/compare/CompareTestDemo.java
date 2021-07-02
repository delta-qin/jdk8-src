package com.deltaqin.compare;

import lombok.Data;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author deltaqin
 * @date 2021/3/24 3:07 下午
 */

public class CompareTestDemo {



    @Data
    class DTO implements Comparable<DTO> {

        private Integer id;

        public DTO (Integer id){
            this.id = id;
        }

        @Override
        public int compareTo(DTO o) {
            // 从小到大
            return id - o.id;
        }
    }

    @Test
    public void test() {
        List<DTO> list = new ArrayList<>();
        for (int i = 5; i > 0; i--) {
            list.add(new DTO(i));
        }
        Collections.sort(list);
        System.out.println(list.toString());

        Comparator com = (Comparator<DTO>) (o1,o2) -> {
            return o1.getId() - o2.getId();
        };

        List<DTO> list1 = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            list1.add(new DTO(i));
        }
        Collections.sort(list1,com);
        System.out.println(list1.toString());
    }

}
