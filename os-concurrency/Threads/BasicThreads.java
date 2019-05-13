package Threads;
/**
 * Examples on use of threads in Java
 Thread has a method start() which tells the JVM to create a new
thread and then call the run() method in that new thread;
    – Do not call run() directly: otherwise the creating a new thread part is missed
out and the run() method runs just as any other method.
    – Slight Detour: similarly never call paint() always call repaint(): repaint
starts a new thread then calls paint().
 */
public class BasicThreads {

    /**
     * Should randomly print out P, R and Q depending on the
     * scheduling algorithm
     */
    public static void main(String[] args){
        Thread p = new PrintP(); //with extends Thread
        Thread q = new Thread(new PrintQ()); //with Runnable

        p.start();
        q.start();
        try{
            q.join();  //we are asking the cpu to finish q before everything else (P and R in this case)
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        for(int i = 0; i < 200; ++i){
            System.out.print("R");
        }
    }

    /**
    * Create Thread that prints P by extending Thread
    */
    public static class PrintP extends Thread{
        @Override
        public void run() {
            for (int i = 0; i< 100; ++i){
                System.out.print("P");
                try{
                    Thread.sleep(3);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Crate Thread that prints Q by implementing Runnable
     */
    public static class PrintQ implements Runnable{
        @Override
        public void run() {
            for(int i = 0; i < 30; ++i){
                System.out.print("Q");
            }
        }
    }
}
