package com.deltaqin.p08designPattern.d07_builder;

public class Person {
    int id;
    String name;
    int age;
    double weight;
    int score;

    private Person() {}

    public static class PersonBuilder {
        Person p = new Person();

        public PersonBuilder basicInfo(int id, String name, int age) {
            p.id = id;
            p.name = name;
            p.age = age;
            return this;
        }

        public PersonBuilder weight(double weight) {
            p.weight = weight;
            return this;
        }

        public PersonBuilder score(int score) {
            p.score = score;
            return this;
        }


        public Person build() {
            return p;
        }
    }
}
