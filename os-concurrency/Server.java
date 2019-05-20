public class Server {
    public static void main(String[] args) {
        WebServer server = new WebServer();
        for (int i = 0; i < 10; ++i) {
            new User(server, "user" + i, false).start();
        }
        for (int i = 0; i < 2; ++i) {
            new User(server, "admin" + i, true).start();
        }
    }
}

public class WebServer {
    private int activeUsers = 0;
    private int userCount = 0;
    private int adminsWaiting = 0;
    boolean adminUsing = false;

    public synchronized void connectToServer(User u) {
        if (u.isAdmin()) {
            ++adminsWaiting;
        }
        while((adminsWaiting > 0 && !u.isAdmin()) || (userCount > 0 && u.isAdmin()) || adminUsing) {
            try {
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (u.isAdmin()) {
            adminUsing = true;
            --adminsWaiting;
        } else {
            ++userCount;
        }
        System.out.println(u.getUserName() + " connected");
    }

    public void interact(User u) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void disconnectFromServer(User u) {
        if (u.isAdmin()) {
            adminUsing = false;
        } else {
            --userCount;
        }
        System.out.println(u.getUserName() + " disconnected");
        notifyAll();
    }
}

public class User extends Thread {
    WebServer server;
    String name;
    boolean adminUser;

    public User(WebServer toUse, String nameIn, boolean isAnAdmin) {
        server = toUse;
        name = nameIn;
        adminUser = isAnAdmin;
    }

    public boolean isAdmin() {
        return adminUser;
    }

    public String getUserName() {
        return name;
    }

    public void run() {
        server.connectToServer(this);
        server.interact(this);
        server.disconnectFromServer(this);
    }
}