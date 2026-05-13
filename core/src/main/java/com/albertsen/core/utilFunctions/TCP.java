package com.albertsen.core.utilFunctions;

import com.albertsen.core.dataObjs.Peer;
import com.albertsen.core.handlers.PeerHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
	private static final Map<Socket, String> connectionToPeer =
			new ConcurrentHashMap<>();
	public static ServerSocket startServer()
	{
		try{
			ServerSocket serverSocket = new ServerSocket(9090);

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
		String ip = findIP(clientSocket);

	    System.out.println("Client connected!");
		connectionToPeer.put(clientSocket, ip);
	    return clientSocket;
	} catch(IOException e){
	    System.out.println(e);
	}
	return null;
    }

	public static String findIP(Socket socket){

		SocketAddress socketAddress = socket.getRemoteSocketAddress();
		String ip = "invalid";
		if (socketAddress instanceof InetSocketAddress) {
			InetAddress inetAddress = ((InetSocketAddress)socketAddress).getAddress();
			ip = inetAddress.toString();
			if (inetAddress instanceof Inet4Address)
				System.out.println("IPv4: " + inetAddress);
			else if (inetAddress instanceof Inet6Address)
				System.out.println("IPv6: " + inetAddress);
			else{
				System.err.println("Not an IP address.");
				Logging.log("Not an ip address", Logging.LogLevel.error);
			}
		} else {
			Logging.log("Not an internet protocol socket.", Logging.LogLevel.error);
			System.err.println("Not an internet protocol socket.");
		}

		if(ip.equals("invalid")){
			Logging.log("invalid peer ip", Logging.LogLevel.error);
		}

		return ip;
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

	public static byte[] receiveBytes(Socket socket) throws IOException {

		DataInputStream in =
				new DataInputStream(socket.getInputStream());

		int length = in.readInt();

		byte[] data = new byte[length];

		in.readFully(data);

		return data;
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

	public static void sendMessage(Socket socket, byte[] data) throws IOException {

		DataOutputStream out =
				new DataOutputStream(socket.getOutputStream());

		out.writeInt(data.length);
		out.write(data);
		out.flush();
	}


}
