/**
 * Examples on use of threads in Java
 */
public class Threads {

    /**
     * Should randomly print out P, R and Q depending on the
     * scheduling algorithm
     */
    public static void main(String[] args){
        Thread p = new PrintP(); //with extends Thread
        Thread q = new Thread(new PrintQ()); //with Runnable

        p.start();
        q.start();

        //start a new thread with main
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
