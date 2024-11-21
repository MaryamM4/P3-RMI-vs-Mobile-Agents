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
        startTime = new Date(); 

        // Memorize where this agent was injected
        InetAddress inetaddr = InetAddress.getLocalHost(); 
        orgPlace = inetaddr.getHostAddress();   // Hostname

        // Go to servers[0] and call unix()
        //hop(orgPlace, "result", ...);
    }

    public void unix() {
        // Execute all commands
        // Store all outputs in allOutputs
        
        // Go to server[++i] and call unix if you stil have servers
        // Otherwise, go back to orgPlace and call results()
    }

    public void results() {
        // Print out results

        // Get the endTime
        Date endTime = new Date();
        long duration = endTime.getTime() - startTime.getTime();
    }

    public Vector execute(String command) { 
        Vector<String> output = new Vector<String>(); 
        String line; 

        try { 
            Runtime runtime = Runtime.getRuntime(); 
            Process process = runtime.exec(command); 
            InputStream input = process.getInputStream(); 

            BufferedReader bufferedInput 
                = new BufferedReader(new InputStreamReader(input)); 

            while ((line = bufferedInput.readLine()) != null) { 
                if (print) {
                    System.out.println(line);
                } 
                output.addElement(line); 
            } 

        } catch (IOException e) { 
            e.printStackTrace(); 
            return output; 
        } 

        return output; 
    } 
}
