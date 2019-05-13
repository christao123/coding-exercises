/**
* Simulate atomic assignments and the implementation of Peterson's algorithm using a buffer
*/
public class PetersonAlgorithm{

    public static void main(String[] args){
        Buffer buffer = new Buffer(10);
        Producer p1 = new Producer(buffer, 1);
        Producer p2 = new Producer(buffer, 2);
        //TODO: IMPLEMENT PETERSON'S ALGORITHM HERE
        p1.start();
        p2.start();
    }
}

public class Producer extends Thread{
    Buffer buffer;
    int value;
    public Producer(Buffer buffer, int value){
        this.buffer = buffer;
        this.value = value;
    }
    public void run(){
        for(int i = 0; i < 5; ++i){
            buffer.assign(i, value);
            buffer.print();
            System.out.println("");
        }
    }
}

public class Buffer {
    AtomicInt[] buffer;
    int size;
    public Buffer(int size){
        this.size = size;
        buffer = new AtomicInt[size];
        for(int i = 0; i<size; ++i){
            buffer[i] = new AtomicInt(0);
        }
    }
    public void assign(int index,int value){
        buffer[index].assign(value);
    }
    public int get(int index){
        return buffer[index].get();
    }
    public synchronized void print(){
        System.out.print("[");
        for(int i = 0; i< size - 1; ++i){
            System.out.print(buffer[i].get() + ",");
        }
        System.out.print(buffer[size-1].get());
        System.out.print("]");
    }
}
public  class AtomicInt{
    int value;
    
    public AtomicInt(int initialValue){
        value = initialValue;
    }
    public synchronized void assign(int newValue){
        value = newValue;
    }
    public synchronized int get(){
        return value;
    }
}
public  class AtomicBoolean{
    Boolean value;
    
    public AtomicBoolean(Boolean b){
        value = b;
    }
    public synchronized void assign(Boolean newBool){
        value = newBool;
    }
}