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

    /**
     * 
     * @param args [0] P or C option, 
     *             [1] Number of servers, [2] List of server IP names, 
     *             [3] Number of Unix commands, [4] List of Unix commands.
     * 
     * @throws IllegalArgumentException On invalid parameters or failed initialization. 
     */
    public UnixAgent(String[] args) throws IllegalArgumentException {
        int argc = args.length;

        // Check minimum size
        if (argc < 6) {
            throw new IllegalArgumentException("UnixAgent Error: Must have 5 params minimum: option, numServers, serverName/s, numCommands, command/s.");
        }

        // Set the print variable:
        // If P, print all command outputs at the end
        // Otherwise, print only the number of line recieved by the servers.    
        print = args[0].equals("P");
     
        // Init the itinerary server[]: 
        int numServers = Integer.parseInt(args[1]);
        if (numServers < 1 || numServers > argc - 4) {
            throw new IllegalArgumentException("UnixAgent Error: Paramater numServers " + numServers + " cannot be < 1 or exceed param count.");
        } 

        servers = new String[numServers];
        for (int i = 0; i < numServers; i++) {
            servers[i] = args[i + 2];
        }

        // Init commands[]:
        int numCommands = Integer.parseInt(args[1+numServers]);
        if (numCommands < 1 || numCommands > argc - (4 + numServers)) {
            throw new IllegalArgumentException("UnixAgent Error: Paramater numCommands " + numCommands + " cannot be < 1 or exceed param count.");
        } 

        commands = new String[numCommands];
        for (int i = 0; i < numCommands; i++) {
            commands[i] = args[i + 3 + numServers];
        }

        // Display outcome
        String option = "count";
        if (print) {
            option = "print";
        }

        System.out.println("print/count = " + option + ", nServers = " + numServers + ", server1 = " + servers[0] + ", command1 = " + commands[0]);
    }

    public void init() {
        startTime = new Date(); 

        try { // Memorize where this agent was injected
            InetAddress inetaddr = InetAddress.getLocalHost(); 
            orgPlace = inetaddr.getHostAddress();   // Hostname

        } catch (UnknownHostException hostExc) {
            System.out.println("UnixAgent failed to get local host address: ");
            hostExc.printStackTrace(System.out);
        }

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
