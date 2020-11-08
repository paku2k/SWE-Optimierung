package swe_mo.ui;

import java.util.LinkedList;
import java.util.Queue;

import swe_mo.solver.SolverManager;



public class UiBackend {	
	private final static String AUTH = "UiB";
	private final static String _AUTH = AUTH;
	
	private static boolean running = false;
	private static boolean exit = false;
	
	private static WebInterfaceServer wis = new WebInterfaceServer();
	private static Thread Thread_UiFrontend;
	private static Thread Thread_WebInterfaceServer;	
	private static int solver_id;
	
 	
	
	
	
	
/*
 * SSS FUNCTIONS (public)
 * 
 * start
 * stop
 * 
 */
	
	/**
	 * start UiBackend
	 * run it
	 * stop it
	 */
	public static void start() {
		if(exit) return;
		running = true;
		clogger.info(AUTH, "start", "UiBackend started");
		try {
			UiBackend.cmd(AUTH, "uif start");			
		} catch(Exception e) {
			clogger.err(_AUTH, "start", e);
		}

		while(running) {
			while_run();

			try {
				Thread.sleep(10);
			} catch (Exception e) {
				clogger.err(AUTH, "run", e);	
			}
		}
		//stopping
				
		// wait for Threads to stop
		try {
			if(Thread_WebInterfaceServer != null) Thread_WebInterfaceServer.join();
			if(Thread_UiFrontend != null) Thread_UiFrontend.join();
		} catch (InterruptedException e) {
			clogger.err(AUTH, "run", e);			
		} catch (Exception e) {
			clogger.err(AUTH, "run",  e);		
			
		}
		
		SolverManager.terminateAll();
		
		clogger.info(AUTH, "run", "UiBackend stopped");
	}
	
	/**
	 * stop the UiBackend
	 * 
	 * @param permanently used for disabling UiB at app exit
	 */
	public static void stop(boolean permanently) {
		running = false;
		exit = permanently;
	}
	
 	
	
	
	
	
/*
 * RUN FUNCTIONS (private/public)
 * 
 * while_run
 * writeTo_stLog
 * 
 */

	private static Queue<String> to_stLog_buffer = new LinkedList<String>();
	
	/**
	 * recurring actions while UiB is running
	 */
	private static void while_run() {			

		//detect change of wis and send change command to UiF
		if(wis.statechange()) {
			UiFrontend.wisStatusChange(wis.status(),wis.getPort());
		}
				
		//try data logging to stLog
		try {
			if(Thread_UiFrontend!=null) {
				if(UiFrontend.status()==1) {
					while (to_stLog_buffer.size()>0)
						UiFrontend.stLog_newEntry(to_stLog_buffer.poll());
				}
			}
		} catch (Exception e) {
			clogger.err(_AUTH, "while_run", e);
		}
				
		//solver test status
		if(SolverManager.status(solver_id) >= 0 && SolverManager.status(solver_id) <= 100) {
			if(SolverManager.statusChng(solver_id)) clogger.dbg(AUTH, "while_run", "Status is "+(int)SolverManager.status(solver_id)+"%");			
		}
				
	}
	
	/**
	 * save stLog entry to queue (to be processed by while_run)
	 * 
	 * @param string stLog entry
	 */
	public static void writeTo_stLog(String string) {
		to_stLog_buffer.offer(string);
	}
	
 	
	
	
	
	
/*
 * COMMANDLINE INTERPRETER (public/private)
 * 
 * cmd
 * cmd_interprete
 * 
 */

	private static int cmd_cnt = 0;
	
	/**
	 * function to be called by every function for commandline interpreter access
	 * logs cmd request and answer, calls cmd_interprete 
	 * 
	 * @param AUTH 	authenticator for submitting class
	 * @param cmd	command as String
	 * 
	 * @return returns Object with answer
	 * 
	 * @throws Exception
	 */
	public static Object cmd(String AUTH, String cmd) throws Exception {
		int token = cmd_cnt++;
		Object status;
		clogger.cmd(_AUTH, "cmd", "$("+AUTH+"-"+token+") "+cmd);
		try {
			status = cmd_interprete(AUTH, cmd); 
		} catch(Exception e){
			clogger.cmd(_AUTH, "cmd", "$("+AUTH+"-"+token+") --> ERR: "+e.getMessage());
			throw e;
		}
		clogger.cmd(_AUTH, "cmd", "$("+AUTH+"-"+token+") --> "+status);
		return status;
	}
	
	/**
	 * interprete cmd line command
	 * 
	 * @param AUTH 	authenticator for submitting class
	 * @param cmd	command as String
	 * 
	 * @return returns Object with answer
	 * 
	 * @throws Exception
	 */	
	private static Object cmd_interprete(String AUTH, String cmd) throws Exception {

		if(cmd.equals("ts tst")) {
			cmd(AUTH,"ts create");
			cmd(AUTH,"ts config");
			cmd(AUTH,"ts start");
			
			
		} else if(cmd.equals("ts create")) {
				solver_id = SolverManager.newSolver("");
				return solver_id;
				
		} else if(cmd.equals("ts config")) {
				try {
					SolverManager.configure(solver_id);
					return "(Test-)Solver configured";
				} catch(Exception e) {
					clogger.err(AUTH, "cmd_interprete", e);
					return "Could not configure (Test-)Solver";
				}
				
		} else if(cmd.equals("ts start")) {
				try {
					SolverManager.start(solver_id);
					return "(Test-)Solver started";
				} catch(Exception e) {
					clogger.err(AUTH, "cmd_interprete", e);
					return "Could not start (Test-)Solver";
				}
				
		} else if(cmd.equals("ts term")) {
				try {
					SolverManager.terminate(solver_id);
					return "(Test-)Solver terminated";
				} catch(Exception e) {
					clogger.err(AUTH, "cmd_interprete", e);
					return "Could not terminate (Test-)Solver";
				}

				
		} else if(cmd.equals("ts getres")) {
				try {
					return "Result is: "+SolverManager.result(solver_id);
				} catch(Exception e) {
					clogger.err(_AUTH, "cmd_interprete", e);
				}
				return "No result available";

				
		} else if(cmd.equals("ts getstat")) {
				try {
					return "Status: "+(int)SolverManager.status(solver_id);					
				} catch(Exception e) {
					clogger.err(_AUTH, "cmd_interprete", e);
				}
				return "Error while reading status";
				
				
		} else if(cmd.equals("ts delete")) {
				try {
					SolverManager.delete(solver_id);
					return "Solver deleted";
				} catch(Exception e) {
					clogger.err(_AUTH, "cmd_interprete", e);
				}
				return "Could not delete";
				

				
				
				
	//new
				
		//help
		} else if(cmd.equals("help")) {
			return     "List of modules\r"
					 + "\t" + "app \t\tApplication Control\r"
					 + "\t" + "cfg \t\tApplication Configuration\r"
					 + "\t" + "uif \t\tUiFrontend\r"
					 + "\t" + "wis \t\tWebInterfaceServer\r"
					 + "\t" + "sm  \t\tSolverManager\r"
					 + "\t" + "om  \t\tOptimizer";
			
			
			
		// app
		} else if(cmd.equals("app") || cmd.equals("app help") || cmd.equals("help app")) {
			return     "app - List of commands\r"
					 + "\t" + "exit \t\tStop and exit Application";
			
		} else if(cmd.equals("app exit")) {
			if(wis.status()) wis.stop(true);
			UiFrontend.stop(true);
			UiBackend.stop(true);
			return "Exiting application.";
					
			

		// uif	
		} else if(cmd.equals("uif") || cmd.equals("uif help") || cmd.equals("help uif")) {
			return     "uif - List of commands\r"
					 + "\t" + "start \t\tStart UiFrontend\r"
					 + "\t" + "min   \t\tMinimize Window\r"
					 + "\t" + "show  \t\tShow Window\r"
					 + "\t" + "status\t\tGet Status of UiFrontend";	
			
		} else if(cmd.equals("uif start")) {
			if(Thread_UiFrontend!=null && Thread_UiFrontend.isAlive()) return "UiFrontend is already running.";
			try {
				Thread_UiFrontend = new Thread() { public void run() {try {UiFrontend.start();} catch (Exception e) {clogger.err(AUTH, "Thread_UiFrontend", e);}}};
				Thread_UiFrontend.start();
				return "Starting UiFrontend.";
			} catch(Exception e) {
				throw new Exception("Could not start UiFrontend. ("+e.getMessage()+")");
			}
			
		} else if(cmd.equals("uif min")) {
			if(Thread_UiFrontend!=null && Thread_UiFrontend.isAlive()) {
				UiFrontend.setMinimized(true);
				return "Minimized UiFrontend Window.";
			} else {
				throw new Exception("UiFrontend not running.");
			}
			
		} else if(cmd.equals("uif show")) {
			if(Thread_UiFrontend!=null && Thread_UiFrontend.isAlive()) {
				UiFrontend.setMinimized(false);
				return "Show UiFrontend Window.";
			} else {
				throw new Exception("UiFrontend not running.");
			}
			
		} else if(cmd.equals("uif status")) {
			if(Thread_UiFrontend!=null && Thread_UiFrontend.isAlive()) {
				switch(UiFrontend.status()) {
				case -1:
					return "exit";
				case 0:
					return "not running";
				case 1:
					return "running";
				}
			} else {
				throw new Exception("UiFrontend not running.");
			}
			
								

		// wis	
		} else if(cmd.equals("wis") || cmd.equals("wis help") || cmd.equals("help wis")) {
			return     "wis - List of commands\r"
					 + "\t" + "start \t\tStart WebInterfaceServer\r"
					 + "\t" + "stop  \t\tStop WebInterfaceServer\r"
					 + "\t" + "open  \t\tOpen WebGUI in Browser\r"
					 + "\t" + "status\t\tGet Status of WebInterfaceServer";
			
		} else if(cmd.equals("wis start")) {
			Thread_WebInterfaceServer = new Thread() { public void run() {try {wis.start();} catch (Exception e) {clogger.err(AUTH, "Thread_WebInterfaceServer", e);}}};
			Thread_WebInterfaceServer.start();
			return "Starting WIS (Port: "+wis.getPort()+")";
			
		} else if(cmd.equals("wis start -n")) {
			if(wis.status()) UiBackend.cmd(_AUTH, "wis stop");
			wis = new WebInterfaceServer();
			return UiBackend.cmd(_AUTH, "wis start");
			
		} else if(cmd.equals("wis stop")) {
			if(wis.status()) {
				wis.stop(false);
				cmd(AUTH, "uif show");
				return "Stopping WIS.";
			} else {
				throw new Exception("WIS already stopped.");
			}

		} else if(cmd.equals("wis open")) {
			try {
				wis.open();
				return "Opening WebGUI";
			} catch(Exception e) {
				throw new Exception("Could not open WebGUI. ("+e.getMessage()+")");
			}
			
		} else if(cmd.equals("wis open -m")) {
			cmd(AUTH, "wis open");
			cmd(AUTH, "uif min");			

		} else if(cmd.equals("wis status")) {
			return (wis.status()?"running":"not running")+". Configured port: "+wis.getPort();			

		} else if(cmd.equals("wis status -a")) {
			return wis.status();			

		} else if(cmd.equals("wis status -p")) {
			return wis.getPort();	
			
			
			
			
			
		} else {
			throw new Exception("Command unknown ("+cmd+"). Try help for list of commands.");
		}
		return "ok";		
	}
	
}
