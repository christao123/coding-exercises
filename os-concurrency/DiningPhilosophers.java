import java.util.ArrayList;

public class DiningPhilosophers {
    public static void main(String[] args) {
        ForksMonitor monitor = new ForksMonitor(5);
        ArrayList<Fork> forks = monitor.getForks();
        Philosoper p1 = new Philosoper("Aristotle", monitor, forks.get(0));
        Philosoper p2 = new Philosoper("Plato", monitor, forks.get(1));
        Philosoper p3 = new Philosoper("Nietzsche", monitor, forks.get(2));
        Philosoper p4 = new Philosoper("Kant", monitor, forks.get(3));
        Philosoper p5 = new Philosoper("Schopenahuer", monitor, forks.get(4));

        p1.start();
        p2.start();
        p3.start();
        p4.start();
        p5.start();

    }
}

public class Philosoper extends Thread {
    private String name;
    private Fork leftFork;
    private Fork rightFork;
    private ForksMonitor monitor;

    public Philosoper(String name, ForksMonitor monitor, Fork leftFork) {
        this.name = name;
        this.monitor = monitor;
        this.leftFork = leftFork;
        this.rightFork = leftFork.getRightFork();
    }

    @Override
    public void run() {
        for (int i = 0; i < 4; ++i) {
            monitor.takeForks(this);
        }
    }

    public Fork getLeftFork() {
        return leftFork;
    }

    public Fork getRightFork() {
        return rightFork;
    }

    public void eat() {
        leftFork.setUse(true);
        rightFork.setUse(true);
        System.out.println(name + " is eating!");
        try {
            Thread.sleep(50);
        } catch (Exception e) {
            e.printStackTrace();
        }
        leftFork.setUse(false);
        rightFork.setUse(false);
    }

    public void think() {
        System.out.println(name + " is thinking...");
        try {
            Thread.sleep(20);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

public class ForksMonitor {
    private int numberOfForks;
    private ArrayList<Fork> forks;

    public ForksMonitor(int numberOfForks) {
        this.numberOfForks = numberOfForks;
        forks = new ArrayList<>();
        for (int i = 0; i < numberOfForks; ++i) {
            forks.add(new Fork());
        }
        /**
         * Perdoname papa por mi code loco
         */
        forks.get(0).setLeftFork(forks.get(numberOfForks - 1));
        forks.get(0).setRightFork(forks.get(1));

        forks.get(numberOfForks - 1).setLeftFork(forks.get(numberOfForks - 2));
        forks.get(numberOfForks - 1).setRightFork(forks.get(0));

        for (int i = 1; i < numberOfForks - 1; ++i) {
            forks.get(i).setLeftFork(forks.get(i - 1));
            forks.get(i).setRightFork(forks.get(i + 1));
        }
    }

    public ArrayList<Fork> getForks() {
        return forks;
    }

    public void takeForks(Philosoper p) {
        Fork lf = p.getLeftFork();
        Fork rf = p.getRightFork();
        while (lf.isInUse() || rf.isInUse()) {
            p.think();
        }
        p.eat();
    }
}

public class Fork {

    private Fork leftFork;
    private Fork rightFork;

    boolean inUse;

    public Fork() {
        leftFork = null;
        rightFork = null;
        inUse = false;
    }

    public Fork(Fork leftFork, Fork rightFork) {
        this.inUse = false;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    public void setLeftFork(Fork f) {
        leftFork = f;
    }

    public void setRightFork(Fork f) {
        rightFork = f;
    }

    public boolean isInUse() {
        return inUse;
    }

    public synchronized void setUse(boolean b) {
        inUse = b;
    }

    public Fork getLeftFork() {
        return leftFork;
    }

    public Fork getRightFork() {
        return rightFork;
    }
}