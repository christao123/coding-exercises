import java.util.LinkedList;

/**
 * An example of deadlock induced in java
 */
public class Deadlock {

    public static void main(String[] args) {
      LinkedList<Friend> friends = new LinkedList();
      Friend alphonse = new Friend("Alphonse", friends);
      Friend gaston = new Friend("Gaston",friends);
      friends.add(alphonse);
      friends.add(gaston);
      alphonse.start();
      gaston.start();
    }

}

public class Friend extends Thread {
    private String name;
    LinkedList<Friend> friends;
    public Friend(String nameIn, LinkedList<Friend> allFriends) {
      name = nameIn;
      friends = allFriends;
    }
    public String getFriendName(){
      return name;
    }
    public synchronized void bow(Friend bower) {
      System.out.println(name + ": " + bower.getFriendName() 
                          + " has bowed to me!");
       bower.bowBack(this);
    }
    public synchronized void bowBack(Friend bower) {
      System.out.println(bower.getFriendName() + ": " + name
                         + " has bowed back to me!");
    }
    public void run(){
        for(Friend f : friends){
            if(!this.equals(f)){
                bow(f);
            }
        }
    }
}
    