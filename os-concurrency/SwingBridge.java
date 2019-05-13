import java.util.*;
public class SwingBridge{
    public static void main(String[] args){
        java.util.concurrent.Semaphore sem = new java.util.concurrent.Semaphore(1);
        Vehicle[] vehicles = new Vehicle[8];
        for(int i  = 0; i< 5; ++i){
            vehicles[i] = new Car("car" + (i+1), sem);
        }
        for(int i  = 5; i< 8; ++i){
            vehicles[i] = new Ship("ship" + (i-4), sem);
        }
        for(Vehicle v : vehicles){
            new Thread(v).start();
        }
    }
}
public abstract class Vehicle implements Runnable{
    protected boolean needsBridgeOpen;
    protected String name;
    protected java.util.concurrent.Semaphore sem;
    protected boolean bridgeOpen;

    public Vehicle(String nameIn, boolean landBasedIn, java.util.concurrent.Semaphore sem){
        name = nameIn;
        needsBridgeOpen = !landBasedIn;
        this.sem = sem;
        bridgeOpen = false;
    }
    public abstract void run();
}
public class Car extends Vehicle{
    public Car(String name, java.util.concurrent.Semaphore sem){
        super(name, true, sem);
    }
    public void run(){
        for(int i = 0; i < 5; ++i){
            try{
                sem.acquire();
            } catch(Exception e){
                e.printStackTrace();
            }
            crossBridge();
            sem.release();
        }
    }
    public void crossBridge(){
        if(bridgeOpen)
            bridgeOpen = false;
        System.out.println(name + " is crossing the Bridge");
        try{
            Thread.sleep(100);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
public class Ship extends Vehicle{
    public Ship(String name, java.util.concurrent.Semaphore sem){
        super(name, false, sem);
    }
    public void run(){
        for(int i = 0; i < 3; ++i){
            try{
                sem.acquire();
            } catch(Exception e){
                e.printStackTrace();
            }
            crossBridge();
            sem.release();
        }
    }
    public void crossBridge(){
        if(!bridgeOpen)
            bridgeOpen = true;
        System.out.println(name + " is sailing underneath the Bridge");
        try{
            Thread.sleep(150);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
