import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.UnknownHostException;

/* Basic examples of how to use the class
   
   public static void ClientExample()
   {
	Socket client = connectAsClient("localhost", 9090);
	sendMessage(client, "Hello from client");
	System.out.println("Response from server: " + receiveMessage(client));
	closeClient(client);
	
    }

    public static void ServerExample()
    {
	ServerSocket server = startServer();
	Socket clientSocket = null;
	if (server != null)
	    clientSocket = acceptClient(server);
	if (clientSocket != null){
	    sendMessage(clientSocket, "Hello from server");
	    System.out.println("Response from client: " + receiveMessage(clientSocket));
	    closeClient(clientSocket);
	    closeServer(server);
	}
    }

 */




public class TCP
{
    public static ServerSocket startServer()
    {
	try(ServerSocket serverSocket = new ServerSocket(9090)){
	    System.out.println("Server is running and waiting for client connection...");
	    return serverSocket;
	} catch(IOException e){
	    System.out.println(e);
	}
	return null;
    }

    public static void closeServer(ServerSocket serverSocket)
    {
	try{
	    serverSocket.close();
	} catch(IOException e){
	    System.out.println(e);
	}
    }

    public static Socket acceptClient(ServerSocket serverSocket)
    {
	try{
	    Socket clientSocket = serverSocket.accept();
	    System.out.println("Client connected!");
	    return clientSocket;
	} catch(IOException e){
	    System.out.println(e);
	}
	return null;
    }
    
    public static void closeClient(Socket clientSocket)
    {
	try{
	    clientSocket.close();
	} catch(IOException e){
	    System.out.println(e);
	}
    }
    public static Socket connectAsClient(String addr, int port)
    {
	try{
	    Socket socket = new Socket(addr, port);
	    return socket;
	}catch (UnknownHostException e){
	    System.out.println(e);
	}catch (IOException e){
	    System.out.println(e);
	}
	return null;
    }
 
public static String receiveMessage(Socket ClientSocket)
{
    try{
	BufferedReader in = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
	return in.readLine();
    }catch(IOException e){
	System.out.println(e);
    }
    return "ERROR";
}
    
public static void sendMessage(Socket ClientSocket, String Message)
{
    try{
	PrintWriter out = new PrintWriter(ClientSocket.getOutputStream(), true);
	out.println(Message);
    } catch(IOException e){
	System.out.println(e);
    }
}

}
