package Threads;
import java.util.Random;

/**
Two threads share same array. They access is at random different
intervals but the call is synchronized so they never give wrong
results
*/
public class SharedResSynchronized{
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
           runIt();
        }
        //by using the keyword synchronized, we force the program to process
        //every thread separately (waiting for one to be over, never running more than
        //one instance of this method at a time)
        public synchronized void runIt(){
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
