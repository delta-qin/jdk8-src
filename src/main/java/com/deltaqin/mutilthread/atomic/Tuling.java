package com.deltaqin.mutilthread.atomic;

import lombok.Data;


@Data
public class Tuling {

    private Integer sequence;

    public Tuling(Integer seq){
        sequence = seq;
    }
}
