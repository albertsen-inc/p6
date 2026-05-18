package com.albertsen.core.utilFunctions;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class TCP {

	public static ServerSocket startServer(int port) {

		try {

			ServerSocket serverSocket = new ServerSocket();

			serverSocket.setReuseAddress(true);

			serverSocket.bind(new InetSocketAddress(port));

			System.out.println("Server started on port: " + port);

			return serverSocket;

		} catch (IOException e) {

			e.printStackTrace();

		}

		return null;
	}

	public static void closeServer(ServerSocket serverSocket) {

		try {

			if (serverSocket != null && !serverSocket.isClosed()) {

				serverSocket.close();

			}

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	public static Socket acceptClient(ServerSocket serverSocket) {

		try {

			Socket clientSocket = serverSocket.accept();

			System.out.println("Client connected: "
					+ clientSocket.getInetAddress().getHostAddress());

			return clientSocket;

		} catch (IOException e) {

			if (!serverSocket.isClosed()) {

				e.printStackTrace();

			}

		}

		return null;
	}

	public static void closeClient(Socket clientSocket) {

		try {

			if (clientSocket != null && !clientSocket.isClosed()) {

				clientSocket.close();

			}

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	public static Socket connectAsClient(String addr, int port) {

		try {

			Socket socket = new Socket(addr, port);

			System.out.println("Connected to server: " + addr);

			return socket;

		} catch (UnknownHostException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		return null;
	}

	public static String findIP(Socket socket) {

		try {

			SocketAddress socketAddress =
					socket.getRemoteSocketAddress();

			if (socketAddress instanceof InetSocketAddress) {

				InetAddress inetAddress =
						((InetSocketAddress) socketAddress).getAddress();

				String ip = inetAddress.getHostAddress();

				if (inetAddress instanceof Inet4Address) {

					System.out.println("IPv4: " + ip);

				} else if (inetAddress instanceof Inet6Address) {

					System.out.println("IPv6: " + ip);

				}

				return ip;
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return "invalid";
	}

	public static String receiveMessage(Socket socket) {

		try {

			BufferedReader in = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));

			return in.readLine();

		} catch (IOException e) {

			e.printStackTrace();

		}

		return null;
	}

	public static void sendMessage(Socket socket, String message) {

		try {

			PrintWriter out =
					new PrintWriter(socket.getOutputStream(), true);

			out.println(message);

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	public static byte[] receiveBytes(Socket socket) throws IOException {

		DataInputStream in =
				new DataInputStream(socket.getInputStream());

		int length = in.readInt();

		if (length <= 0) {

			throw new IOException("Invalid byte array length");

		}

		byte[] data = new byte[length];

		in.readFully(data);

		return data;
	}

	public static void sendBytes(Socket socket, byte[] data)
			throws IOException {

		DataOutputStream out =
				new DataOutputStream(socket.getOutputStream());

		out.writeInt(data.length);

		out.write(data);

		out.flush();
	}
}