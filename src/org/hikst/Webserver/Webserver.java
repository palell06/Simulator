package org.hikst.Webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Webserver extends Thread
{
	private int port;
	
	public Webserver(int port)
	{
		this.port = port;
	}
	
	public void run()
	{
		ServerSocket serverSocket = null;
		
		try
		{
			System.out.println("Trying to bind to localhost on port "+port+"...");
			
			serverSocket = new ServerSocket(port);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return;
		}
		
		while(true)
		{
			System.out.println("Waiting for requests...\n");
			
			try
			{
				Socket connectionSocket = serverSocket.accept();
				InetAddress client = connectionSocket.getInetAddress();
				
				System.out.println("Client with the ip-adress " + client.getHostAddress() + " and hostname "+client.getHostName()+" has connected itself to the server");
				
				System.out.println("Reading the http-request...\n");
				BufferedReader input = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				
				System.out.print("Preparing an output-stream to the client with the ip-adress "+client.getHostAddress());
				DataOutputStream output = new DataOutputStream(connectionSocket.getOutputStream());
				
				handleRequest(input, output);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		
	}
	
	private void handleRequest(BufferedReader input, DataOutputStream output)
	{
		try {
			JSONObject jsonObject = HTTP.toJSONObject(getRequest(input));
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getRequest(BufferedReader input)
	{
		StringBuilder builder = new StringBuilder();
		String string = "";
		
		try {
			while((string = input.readLine()) != null){
				builder.append(string);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return builder.toString();
	}
}
