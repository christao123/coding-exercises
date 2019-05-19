public class TrainCrossing {
    public static void main(String[] args) {
        LevelCrossing levelCrossing = new LevelCrossing();
        
        for (int i = 0; i < 3; ++i) {
            new Vehicle("train", "east", "train" + i, levelCrossing).start();
        }
        for (int i = 0; i < 3; ++i) {
            new Vehicle("car", "north", "car" + i, levelCrossing).start();
        }
        for (int i = 0; i < 2; ++i) {
            new Vehicle("car", "south", "car" + (i + 3), levelCrossing).start();
        }
        for (int i = 0; i < 2; ++i) {
            new Vehicle("train", "west", "train" + (i + 3), levelCrossing).start();
        }
    }
}

public class LevelCrossing {
    private boolean roadClosed = false;
    private String trainDirection = "west";
    private int vehiclesOnCrossing = 0;

    public synchronized void useCrossing(Vehicle v) {
        System.out.println("Vehicle " + v.vehicleName() + " Using Crossing in Direction " + v.getDirection());
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Vehicle " + v.vehicleName() + " Leaving Crossing in Direction " + v.getDirection());
    }

    public void enterCrossing(Vehicle v) {
        while (vehiclesOnCrossing > 0 && (roadClosed || v.getType().equals("train"))) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (v.getType().equals("car")) {
            roadClosed = false;
            vehiclesOnCrossing++;
            System.out.println(v.vehicleName() + " has entered the crossing. Direction: " + v.getDirection());
        } else if (v.getType().equals("train")) {
            roadClosed = true;
            vehiclesOnCrossing++;
            trainDirection = v.getDirection();
            System.out.println(v.vehicleName() + " has entered the crossing. Direction: " + trainDirection);
        }
    }

    public synchronized void exitCrossing(Vehicle v){
        System.out.println(v.vehicleName() + " has exited the crossing. Direction:" + v.getDirection());
        if(--vehiclesOnCrossing == 0){
            notifyAll();
        }
    }
}

public class Vehicle extends Thread {
    String type, direction, name;
    LevelCrossing crossing;

    public Vehicle(String typeIn, String directionIn, String nameIn, LevelCrossing levelCrossing) {
        type = typeIn;
        direction = directionIn;
        name = nameIn;
        crossing = levelCrossing;
    }

    @Override
    public void run() {
        cross();
    }

    public void cross() {
        crossing.enterCrossing(this);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        crossing.exitCrossing(this);
    }

    public String getType() {
        return type;
    }

    public String getDirection() {
        return direction;
    }

    public String vehicleName() {
        return name;
    }
}