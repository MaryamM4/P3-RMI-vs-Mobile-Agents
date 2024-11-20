import java.rmi.*;
import java.util.Vector; 

public class UnixClient {
    ServerInterface server;

    /** 
     * Constructor. 
    */
    UnixClient(String host, int port) {
        connect(host, port);
    }

    /**
     * Connects to the RMI registry on the given host and port
     * to retrieve the server stub.
     * @param host
     * @param port
     * @return True if the connection was succesfful. 
     */
    boolean connect(String host, int port) {
        try {
            // Lookup server in RMI registry
            server = (ServerInterface) Naming.lookup("rmi://" + host + ":" + port + "/unixserver");

        } catch (Exception e) { // Connection failed
            return false; 
        }

        return true; 
    }

    /**
     * @param command To send to the server.
     * @return The command output from the UnixServer.
     */
    Vector sendCommand(String command) {
        Vector returnValue = new Vector<>(); 

        try { 
            returnValue = server.execute(command); 

        } catch (Exception e) { 
            // print error message
        }

        return returnValue;
    }

    /**
     * Initailizes client and provides interaction interface.
     * @param args
     */
    public static void main(String[] args) {
        
    }
    
}
