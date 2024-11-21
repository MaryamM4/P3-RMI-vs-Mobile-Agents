import java.rmi.*;

import java.util.*; // Vector, Date
improt java.io.*; // InputStream, BufferedReader, Serializable
import java.net.*; // InetAddress
import UWAgent.*; 

public class UnixClient {
    private boolean printCommandOutputs;

    private int port;
    private String[] servers;
    private String[] commands;

    private Vector<String> allOutputs;

    /**
     * Initailizes client and provides interaction interface.
     * @param args
     */
    public static void main(String[] args) {
        UnixClient client = new UnixClient();

        if (!client.parseArgs(args)) {
            return;
        }

        // Execute all commands for every server
        client.executeAll();
    }

    /**
     * Execute all commands for every server.
     */
    void executeAll() {
        int outputCount = 0;
        Date startTime = new Date();

        for (String serverName : servers) {
            ServerInterface server = connect(serverName, port);

            if (server != null) {
                System.out.println("============================================================ ");

                for (String command: commands) {
                    Vector<String> outputs = sendCommand(server, command);

                    if (printCommandOutputs) {
                        System.out.println(serverName + " command(" + command + "):.............................. ");
                    
                        for (String output: outputs) {
                            System.out.println(output);
                        }
                    } else {
                        outputCount += outputs.size();
                    }

                }

            } else {
                System.out.println("UnixClient Error: Failed to connect to server '" + severName + "'. ");
            }
        }

        Date endTime = new Date();

        if (!printCommandOutputs) {
            System.out.println("count = " + outputCount);
        }

        long duration = endTime.getTime() - startTime.getTime();
        System.out.println("\nExecution Time = " + duration);
    }

    /**
     * @return True if parsing is succesful. False on bad input.
     */
    Boolean parseArgs(String[] args) {
        int argc = args.length;

        // Check minimum size
        if (argc < 6) {
            System.out.println("UnixClient Error: Must have 6 params minimum: option, serverPort, numServers, serverName/s, numCommands, command/s.");
            return false;
        }


        // If P, print all command outputs at the end
        // Otherwise, print only the number of line recieved by the servers.    
        printCommandOutputs = args[0].equals("P");
        port = Integer.parseInt(args[1]);
     
        int numServers = Integer.parseInt(args[2]);
        if (numServers < 1 || numServers > argc - 5) {
            System.out.println("UnixClient Error: Paramater numServers " + numServers + " cannot be < 1 or exceed param count.");
            return false;
        } 

        servers = new String[numServers];
        for (int i = 0; i < numServers; i++) {
            servers[i] = args[i + 3];
        }

        int numCommands = Integer.parseInt(args[1+numServers]);
        if (numCommands < 1 || numCommands > argc - (5 + numServers)) {
            System.out.println("UnixClient Error: Paramater numCommands " + numCommands + " cannot be < 1 or exceed param count.");
            return false;
        } 

        commands = new String[numCommands];
        for (int i = 0; i < numCommands; i++) {
            commands[i] = args[i + 4 + numServers];
        }

        // Display outcome
        String option = "count";
        if (printCommandOutputs) {
            option = "print";
        }

        System.out.println("print/count = " + option + " port = " + port + ", nServers = " + numServers + ", server1 = " + servers[0] + ", command1 = " + commands[0]);

        return true;
    }

    /**
     * Connects to the RMI registry on the given host and port
     * to retrieve the server stub.
     * @param host
     * @param port
     * @return True if the connection was succesfful. 
     */
    private ServerInterface connect(String host, int port) {
        try {
            // Lookup server in RMI registry
            ServerInterface server = (ServerInterface) Naming.lookup("rmi://" + host + ":" + port + "/unixserver");
            return server;

        } catch (Exception e) { // Connection failed
            return null; 
        }
    }

    /**
     * @param command To send to the server.
     * @return The command output from the UnixServer.
     */
    private Vector<String> sendCommand(ServerInterface server, String command) {
        Vector returnValue = new Vector<String>(); 

        try { 
            returnValue = server.execute(command); 

        } catch (Exception e) { 
            System.out.println("UnixClient Error: Failed to send command or recieve a response.");
        }

        return returnValue;
    }
    
}


