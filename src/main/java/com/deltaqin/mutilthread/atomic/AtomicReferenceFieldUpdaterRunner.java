package com.deltaqin.mutilthread.atomic;

import lombok.Data;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.UnaryOperator;

// 自定义类型的自定义成员变量的修改
// 注意成员变量使用volatile，别人需要看到，而且必须是public的
public class AtomicReferenceFieldUpdaterRunner {

    static AtomicReferenceFieldUpdater atomic = AtomicReferenceFieldUpdater.newUpdater(Document.class,String.class,"name");

    public static void main(String[] args) {
        Document document = new Document("杨过",1);

        System.out.println(atomic.get(document));

        atomic.getAndSet(document,"xiaolongnv");

        System.out.println(atomic.get(document));

        //另一种方式修改
        UnaryOperator<String> uo = s->{
            System.out.println("UnaryOperator:-->"+s);
            return "小龙女";
        };
        System.out.println(atomic.getAndUpdate(document, uo));
        System.out.println(atomic.get(document));

    }

    @Data
    static class Document{
        // 注意使用volatile，别人需要看到，而且必须是public的
        public volatile String name;
        private int version;

        Document(String obj,int v){
            name = obj;
            version = v;
        }

    }
}
