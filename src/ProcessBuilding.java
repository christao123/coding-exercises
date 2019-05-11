import java.util.ArrayList;
import java.util.Map;

/**
 * Create a new process.
 * In this example we execute the command "code" which runs
 * and instance of visual studio code and we give it the argument
 * "test.txt" which will create a new file called test.txt
 */
public class ProcessBuilding {
    public static void main(String[] args){
        ArrayList<String> commands = new ArrayList<>();
        
        commands.add("echo");
        commands.add("hello");
        commands.add(">");
        commands.add("test.log");
        executeWithWaitFor(commands);
        //? doesn't work

        commands.clear();

        commands.add("code");
        commands.add("test.txt");
        execute(commands);
    }

    private static void execute(ArrayList<String> command) {
        try{
            //start a ProcessBuilder and give it the array containing the commands
            ProcessBuilder builder = new ProcessBuilder(command);
            //this is the environment
            Map<String, String> env = builder.environment();
            //start the process
            Process p = builder.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void executeWithWaitFor(ArrayList<String> command){
        try{
            ProcessBuilder builder = new ProcessBuilder(command);
            Map<String, String> env = builder.environment();
            Process p = builder.start();
            p.waitFor();  //wait for the process to end
            System.out.println("Process " + command + " Terminated");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
