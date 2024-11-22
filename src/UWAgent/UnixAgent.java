import java.util.*; // Vector, Date
import java.io.*; // InputStream. BufferedReader, Serializable
import java.net.*; // InetAddress
import UWAgent.*;

public class UnixAgent extends UWAgent implements Serializable {      
    boolean print = false;

    String[] servers;
    String[] commands;

    Vector<String>[] allOutputs;
    int outputCount;

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

        allOutputs = new Vector[numServers * numCommands];
        outputCount = 0;

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
        index = 0;
        hop(servers[0], "unix", null);

        // hints2 (might have a mistake)
        //hop(orgPlace, "results", null);
    }

    public void unix() {
        // Execute all commands & store outputs in allOutputs
        for (int i = 0; i < commands.length; i++) {
            Vector<String> output = execute(commands[i]);
            
            //allOutputs[(index + 1) * i] = output;
            allOutputs[index * commands.length + i] = output;

            outputCount += output.size();
        }
        
        // If there are next servers, go to server[++i] and call unix
        if (++index < servers.length) {
            hop(servers[index], "unix", null);

        } else { // Otherwise, go back to orgPlace and call results()
            hop(orgPlace, "results", null);
        }
    }

    public void results() {
        // Print out results
        if (print) {
            for (int serverCount = 0; serverCount < servers.length; serverCount++) {
                System.out.println("============================================================ ");

                for (int commCount = 0; commCount < commands.length; commCount++) {
                    System.out.println(servers[serverCount] + " command(" + commands[commCount] + "):.............................. ");
                
                    for (String output: allOutputs[serverCount * commands.length + commCount]) {
                    //for (String output: allOutputs[(serverCount + 1) * outputCount]) {
                        System.out.println(output);
                    }
                }
            }

        } else {
            System.out.println("count = " + outputCount);
        }

        // Get the endTime and print the duration 
        Date endTime = new Date();
        long duration = endTime.getTime() - startTime.getTime();
        System.out.println("\nExecution Time = " + duration);
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
                /*
                if (print) {
                    System.out.println(line);
                } 
                */
                output.addElement(line); 
            } 

        } catch (IOException e) { 
            e.printStackTrace(); 
            return output; 
        } 

        return output; 
    } 
}
