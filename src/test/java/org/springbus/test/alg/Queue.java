package org.springbus.test.alg;

public class Queue {

    private int MAXQSIZE;
    private int base[];
    private int front;
    private int rear;

    public Queue(int maxSize) {
        this.MAXQSIZE = maxSize;
        base = new int[maxSize];
        front = 0;
        rear = 0;
    }

    public boolean insert(int data) {
        if ((rear + 1) % MAXQSIZE == front)
            return false;
        base[rear] = data;
        rear = (rear + 1) % MAXQSIZE;
        return true;
    }

    public boolean remove() {
        if (front == rear) {
            return false;
        }
        front = (front + 1) % MAXQSIZE;
        return true;
    }

    public int length() {
        return (rear - front + MAXQSIZE) % MAXQSIZE;
    }

    public boolean isEmpty() {
        return front == rear;
    }

    public boolean isFull() {
        return front == (rear + 1) % MAXQSIZE;
    }

    public static void main(String[] args) {
        Queue q = new Queue(5);
        q.insert(1);
        q.insert(2);
        q.insert(3);
        q.insert(4);
        q.remove();
        q.remove();
        q.insert(5);
        q.insert(6);
        q.remove();
        System.out.println(q.isEmpty() + "--->" + q.isFull() + "---》" + q.length());
    }
}
