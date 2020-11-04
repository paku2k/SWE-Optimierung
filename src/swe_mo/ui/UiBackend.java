package swe_mo.ui;

import java.util.LinkedList;
import java.util.Queue;

//import swe_mo.optimizer.*;
//import swe_mo.solver.*;


public class UiBackend {	
	final static String AUTH = "UiB";
	final static String _AUTH = AUTH;
	private static boolean running = false;
	private static boolean exit = false;
	private static WebInterfaceServer wis = new WebInterfaceServer();
	private static Thread Thread_UiFrontend;
	private static Thread Thread_WebInterfaceServer;
	
	
	
	public static void start() {
		if(exit) return;
		running = true;
		clogger.info(AUTH, "start", "UiBackend started");
		UiBackend.cmd(AUTH, "start uif");			
		run();
	}
		
	private static void run() {
		while(running) {
			while_run();

			try {
				Thread.sleep(10);
			} catch (Exception e) {
				clogger.err(AUTH, "run", e);	
			}
		}
				
		/* wait for Threads to stop */
		try {
			if(Thread_WebInterfaceServer != null) Thread_WebInterfaceServer.join();
			if(Thread_UiFrontend != null) Thread_UiFrontend.join();
		} catch (InterruptedException e) {
			clogger.err(AUTH, "run", e);			
		} catch (Exception e) {
			clogger.err(AUTH, "run",  e);		
			
		}
		
		clogger.info(AUTH, "run", "UiBackend stopped");
	}
	
	public static void stop(boolean exit_now) {
		running = false;
		exit = exit_now;
	}
	
	

	private static Queue<String> to_stLog_buffer = new LinkedList<String>();
	public static void writeTo_stLog(String string) {
		to_stLog_buffer.offer(string);
	}
	
	
	
	
	
	
	
	
	
	
	

	//try {Thread.sleep(1000);}catch(Exception e) {}
	
	private static void while_run() {			

		//detect change of wis and send change command to UiF
		if(wis.statechange()) {
			UiFrontend.wisStatusChange(wis.running(),wis.getPort());
		}
				
		//try data logging to stLog
		try {
			if(Thread_UiFrontend!=null) {
				if(UiFrontend.running()) {
					while (to_stLog_buffer.size()>0)
						UiFrontend.stLog_newEntry(to_stLog_buffer.poll());
				}
			}
		} catch (Exception e) {
			clogger.err(_AUTH, "while_run", e);
		}
				
	}
	
	
	

	
	
	
	public static int cmd_cnt = 0;
	public static Object cmd(String AUTH, String cmd) {
		int token = cmd_cnt++;
		clogger.cmd(_AUTH, "cmd", "$("+AUTH+"-"+token+") "+cmd);
		Object status = cmd_interprete(AUTH, cmd); 
		clogger.cmd(_AUTH, "cmd", "$("+AUTH+"-"+token+") --> "+status);
		return status;
	}
	
	public static Object cmd_interprete(String AUTH, String cmd) {
		if(cmd.equals("exit")) {
			if(wis.running()) wis.stop(true);
			UiFrontend.stop(true);
			UiBackend.stop(true);
			
		} else if(cmd.equals("start uif")) {
			if(Thread_UiFrontend!=null && Thread_UiFrontend.isAlive()) return "uif already running";
			Thread_UiFrontend = new Thread() { public void run() {UiFrontend.start();}};
			Thread_UiFrontend.start();
			return "uif started";
			
		} else if(cmd.equals("stop uif")) {
			UiFrontend.stop(false);
			return "uif stopped";

		} else if(cmd.equals("start wis")) {
			Thread_WebInterfaceServer = new Thread() { public void run() {wis.start();}};
			Thread_WebInterfaceServer.start();
			return "wis started (port: "+wis.getPort()+")";

		} else if(cmd.equals("start wis -newport")) {
			if(wis.running()) UiBackend.cmd(_AUTH, "stop wis");
			wis = new WebInterfaceServer();
			return UiBackend.cmd(_AUTH, "start wis");

		} else if(cmd.equals("stop wis")) {
			wis.stop(false);
			if(!wis.running()) UiFrontend.setMinimized(false);

		} else if(cmd.equals("wis get status")) {
			return wis.running();
			
		} else if(cmd.equals("wis get port")) {
			return wis.getPort();
			
		} else if(cmd.equals("wis open webgui")) {
			wis.open();
			if(wis.running()) {
				UiFrontend.setMinimized(true);
				return "opening WebGui";
			}else {
				return "could not open (wis not running)";
			}
			
		} else if(cmd.equals("wow")) {
			wis.open();
			if(wis.running()) {
				return "opening WebGui";
			}else {
				return "could not open (wis not running)";
			}
			
		} else if(cmd.equals("help")) {
			return     "List of commands\r"
					 + "\t" + "start\r"
					 + "\t\t" + "uif\r"
					 + "\t\t" + "wis\r"
					 + "\t\t\t" + "-newport\r"
					 + "\t" + "stop\r"
					 + "\t\t" + "uif\r"
					 + "\t\t" + "wis\r"
					 + "\t" + "exit\r"
					 + "\t" + "wis\r"
					 + "\t\t" + "start webgui\r"
					 + "\t\t" + "get\r"
					 + "\t\t\t" + "port\r"
					 + "\t\t\t" + "status\r";
			
			
		} else {
			clogger.err(AUTH, "cmd_interprete", "command unknown ("+cmd+")");
			return "ERR: command unknown. Try help for list of commands.";
		}
		return "ok";		
	}
	
	
	
	
}
