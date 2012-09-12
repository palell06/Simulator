package org.hikst.Simulator;

import java.io.DataOutputStream;

import org.hikst.Webserver.JSONObject;
import org.hikst.Webserver.Server;

public class SimulatorServer extends Server
{
	
	public SimulatorServer(int port) {
		super(port);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void parseRequest(JSONObject jsonObject, DataOutputStream output) {
		// TODO Auto-generated method stub
		
	}

}
