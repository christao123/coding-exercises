package Threads;

public class SharedResources{
    public static void main(String[] args){
        int[] buffer = new int[10];
        Thread p = new PrintP(buffer);
        Thread q = new PrintP(buffer);
        p.start();
        q.start();
    }

}