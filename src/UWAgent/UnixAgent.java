import java.util.*; // Vector, Date
import java.io.*; // InputStream. BufferedReader, Serializable
import java.net.*; // InetAddress
import UWAgent.*;

public class UnixAgent extends UWAgent implements Serializable {      
    boolean print = false;

    String[] servers;
    String[] commands;

    int index;
    String orgPlace;
    Date startTime;

    public UnixAgent() {
        System.out.println("No arguments.");
    }

    public UnixAgent(String[] args) {
        // Set the print variable
        // Init the itinerary server[]
        // Init commands[]
    }

    public void init() {
        // Record startTime
        // Memorize where this agent was injected
        // Go to servers[0] and call unix()
    }

    public void unix() {
        // Execute all commands
        // Store all outputs in allOutputs
        
        // Go to server[++i] and call unix if you stil have servers
        // Otherwise, go back to orgPlace and call results()
    }

    public void results() {
        // print out results
        // get the endTime
    }

    public Vector<String> execute(String command) {
        Vector<String> output = new Vector<String>;
        // Borrow from UnixServer's execute
        return output
    }
}
