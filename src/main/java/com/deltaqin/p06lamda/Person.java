package com.deltaqin.p06lamda;

import lombok.Data;

@Data
public class Person implements Comparable{
    private String name;
    private Integer age;

    public Person(String name , Integer age){
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Person){
            Person p1 = (Person) o;
            if (p1.age > this.age){
                return -1;
            } else if (p1.age < this.age) {
                return 1;
            }
        }
        return -1;
    }
}
