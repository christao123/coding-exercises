package Threads;
import java.util.Random;

/**
Two threads share same array. They access is at random different
intervals and therefore modify the same resource unintentionally
*/
public class SharedResources{
    private static int[] buffer = new int[10];
    private static Random rand = new Random();

    public static void main(String[] args){
        Thread p = new MyThread();
        Thread q = new MyThread();
        p.start();
        q.start();
    }

    public static class MyThread extends Thread{
        @Override
        public void run() {
            try{
                Thread.sleep(rand.nextInt(100));
            } catch (InterruptedException e){
                e.printStackTrace();
               }
            if(buffer[0] == 3){
                System.out.println("Element is already 3, can't change it.");
            }
            else{
            System.out.println("Changing element to 3 from " + buffer[0]);
            buffer[0] = 3;
            }
        }
    }
}