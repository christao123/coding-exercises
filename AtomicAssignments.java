/**
* Simulate atomic assignments.
* Should always print:
* 42
* true
* in the given order
*/
public class AtomicAssignments{
    public static void main(String[] args){
        AtomicInt ai = new AtomicInt(1);
        AtomicBoolean ab = new AtomicBoolean(false);

        ai.start();
        ab.start();
    }
    public static class AtomicInt extends Thread{
        int value;

        @Override
        public void run(){
            assign(42);
        }

        public AtomicInt(int initialValue){
            value = initialValue;
        }

        public synchronized void assign(int newValue){
            value = newValue;
            System.out.println(value);
        }
    }
    public static class AtomicBoolean extends Thread{
        Boolean value;

        @Override
        public void run(){
            assign(true);
        }

        public AtomicBoolean(Boolean b){
            value = b;
        }
        public synchronized void assign(Boolean newBool){
            value = newBool;
            System.out.println(value);
        }
    }
}