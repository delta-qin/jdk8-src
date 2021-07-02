package com.deltaqin.designPattern.d04_visitor;


// 根据不同的访问者访问的不同商品，给不同的价格
// 计算的规则定义在访问者上面，不同访问者对从商品获得的相同的信息实现自己的逻辑
// 同一个被访问者传递给不同访问者的消息是一样的。
public class Computer {
    ComputerPart cpu = new CPU();
    ComputerPart memory = new Memory();
    ComputerPart board = new Board();

    public void acccept(Visitor v) {
        this.cpu.accept(v);
        this.memory.accept(v);
        this.board.accept(v);
    }

    public static void main(String[] args) {
        //将访问者传递给被访问者，被访问者根据访问者接口调用他的visit方法，并且传递自己的对象
        //
        //访问者收到传递过来的被访问者，在visitA内部会调用被访问者的方法以及属性得到属于自己的结果
        PersonelVisitor p = new PersonelVisitor();

        new Computer().acccept(p);
        System.out.println(p.totalPrice);
    }
}

abstract class ComputerPart {
    abstract void accept(Visitor v);
    //some other operations eg:getName getBrand
    abstract double getPrice();
}

class CPU extends ComputerPart {

    @Override
    void accept(Visitor v) {
        v.visitCpu(this);
    }

    @Override
    double getPrice() {
        return 500;
    }
}

class Memory extends ComputerPart {

    @Override
    void accept(Visitor v) {
        v.visitMemory(this);
    }

    @Override
    double getPrice() {
        return 300;
    }
}

class Board extends ComputerPart {

    @Override
    void accept(Visitor v) {
        v.visitBoard(this);
    }

    @Override
    double getPrice() {
        return 200;
    }
}

interface Visitor {
    void visitCpu(CPU cpu);
    void visitMemory(Memory memory);
    void visitBoard(Board board);
}

class PersonelVisitor implements Visitor {
    double totalPrice = 0.0;

    @Override
    public void visitCpu(CPU cpu) {
        totalPrice += cpu.getPrice()*0.9;
    }

    @Override
    public void visitMemory(Memory memory) {
        totalPrice += memory.getPrice()*0.85;
    }

    @Override
    public void visitBoard(Board board) {
        totalPrice += board.getPrice()*0.95;
    }
}

class CorpVisitor implements Visitor {
    double totalPrice = 0.0;

    @Override
    public void visitCpu(CPU cpu) {
        totalPrice += cpu.getPrice()*0.6;
    }

    @Override
    public void visitMemory(Memory memory) {
        totalPrice += memory.getPrice()*0.75;
    }

    @Override
    public void visitBoard(Board board) {
        totalPrice += board.getPrice()*0.75;
    }
}
