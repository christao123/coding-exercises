/**
* Simulate atomic assignments and the implementation of Peterson's algorithm using a buffer
*/
public class PetersonAlgorithm{

    public static void main(String[] args){
        Buffer buffer = new Buffer(10);
    }
    public static class Buffer {
        AtomicInt[] buffer;

        public Buffer(size){
            buffer = AtomicInt[size];
        }

        public void assign(index, value){
            buffer[i].assign(value);
        }
    }
    public static class AtomicInt{
        int value;
        
        public AtomicInt(int initialValue){
            value = initialValue;
        }
        public synchronized void assign(int newValue){
            value = newValue;
        }
    }
    public static class AtomicBoolean{
        Boolean value;
        
        public AtomicBoolean(Boolean b){
            value = b;
        }
        public synchronized void assign(Boolean newBool){
            value = newBool;
        }
    }
}