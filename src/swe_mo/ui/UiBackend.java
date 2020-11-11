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
		//cut the command string
		Queue<String> cmd_queue = new LinkedList<String>();
		String[] cmd_split = cmd.replace("    "," ").replace("   "," ").replace("  "," ").split(" ");
		for(String s : cmd_split) {
			if(s != "" && s != null)
				cmd_queue.offer(s);
		}
		
		if(cmd_queue.isEmpty()) {
			throw new Exception("No command given.");
		}
		
				

		//help	
		if(cmd_queue.peek().equals("help")) {
			cmd_queue.remove();
			
			if(cmd_queue.isEmpty()) {
				return     "List of modules\r"
						 + "\t" + "app \t\tApplication Control\r"
						 + "\t" + "cfg \t\tApplication Configuration\r"
						 + "\t" + "uif \t\tUiFrontend\r"
						 + "\t" + "wis \t\tWebInterfaceServer\r"
						 + "\t" + "sm  \t\tSolverManager\r"
						 + "\t" + "om  \t\tOptimizer";
			} else {
				return cmd(AUTH, cmd_queue.poll()+" help");
			}
			
			

		// app
		} else if(cmd_queue.peek().equals("app")) {
			cmd_queue.remove();
			
			if(cmd_queue.isEmpty() || cmd_queue.peek().equals("help")) {
				return     "app - List of commands\r"
						 + "\t" + "exit \t\tStop and exit Application";		
				
			} else if(cmd_queue.peek().equals("exit")) {
				cmd_queue.remove();
				
				if(wis.status()) wis.stop(true);
				UiFrontend.stop(true);
				UiBackend.stop(true);
				return "Exiting application.";
			}
			
			

		//cfg
		} else if(cmd_queue.peek().equals("cfg")) {
			cmd_queue.remove();

			if(cmd_queue.isEmpty() || cmd_queue.peek().equals("help")) {
				return     "cfg - List of commands\r"
						 + "Not implemented yet.";
				
			}	
			return "Not implemented yet.";
			
			

		// uif	
		} else if(cmd_queue.peek().equals("uif")) {
			cmd_queue.remove();

			if(cmd_queue.isEmpty() || cmd_queue.peek().equals("help")) {
				return     "uif - List of commands\r"
						 + "\t" + "start \t\tStart UiFrontend\r"
						 + "\t" + "min   \t\tMinimize Window\r"
						 + "\t" + "show  \t\tShow Window\r"
						 + "\t" + "status\t\tGet Status of UiFrontend";	
				
			} else if(cmd_queue.peek().equals("start")) {
				cmd_queue.remove();				

				if(Thread_UiFrontend!=null && Thread_UiFrontend.isAlive()) return "UiFrontend is already running.";
				try {
					Thread_UiFrontend = new Thread() { public void run() {try {UiFrontend.start();} catch (Exception e) {clogger.err(AUTH, "Thread_UiFrontend", e);}}};
					Thread_UiFrontend.start();
					return "Starting UiFrontend.";
				} catch(Exception e) {
					throw new Exception("Could not start UiFrontend. ("+e.getMessage()+")");
				}
				
			} else if(cmd_queue.peek().equals("min")) {
				cmd_queue.remove();				

				if(Thread_UiFrontend!=null && Thread_UiFrontend.isAlive()) {
					UiFrontend.setMinimized(true);
					return "Minimized UiFrontend Window.";
				} else {
					throw new Exception("UiFrontend not running.");
				}
				
			} else if(cmd_queue.peek().equals("show")) {
				cmd_queue.remove();			
				
				if(Thread_UiFrontend!=null && Thread_UiFrontend.isAlive()) {
					UiFrontend.setMinimized(false);
					return "Show UiFrontend Window.";
				} else {
					throw new Exception("UiFrontend not running.");
				}
				
			} else if(cmd_queue.peek().equals("status")) {
				cmd_queue.remove();		
				
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
			
			}
			
								

		// wis	
		} else if(cmd_queue.peek().equals("wis")) {
			cmd_queue.remove();

			if(cmd_queue.isEmpty() || cmd_queue.peek().equals("help")) {
				return     "wis - List of commands\r"
						 + "\t" + "start \t\tStart WebInterfaceServer\r"
						 + "\t" + "stop  \t\tStop WebInterfaceServer\r"
						 + "\t" + "open  \t\tOpen WebGUI in Browser\r"
						 + "\t" + "status\t\tGet Status of WebInterfaceServer";
				
			} else if(cmd_queue.peek().equals("start")) {
				cmd_queue.remove();		
				
				if(!cmd_queue.isEmpty() && cmd_queue.peek().equals("-n")) {
					cmd_queue.remove();

					if(wis.status()) UiBackend.cmd(_AUTH, "wis stop");
					
					if(cmd_queue.isEmpty()) {
						wis = new WebInterfaceServer();
					} else {
						int port = Integer.parseInt(cmd_queue.poll());
						
						if(!cmd_queue.isEmpty()) {
							wis = new WebInterfaceServer(port, Integer.parseInt(cmd_queue.poll()));
						} else {
							wis = new WebInterfaceServer(port);
						}
					}					
					
					return UiBackend.cmd(_AUTH, "wis start");
					
				} else if(!cmd_queue.isEmpty() && cmd_queue.peek().equals("-r")) {
					if(wis.status()) UiBackend.cmd(_AUTH, "wis stop");
					return UiBackend.cmd(_AUTH, "wis start");					
				}
				
				if(cmd_queue.isEmpty()) {
					Thread_WebInterfaceServer = new Thread() { public void run() {try {wis.start();} catch (Exception e) {clogger.err(AUTH, "Thread_WebInterfaceServer", e);}}};
					Thread_WebInterfaceServer.start();
					return "Starting WIS (Port: "+wis.getPort()+")";
				}
				
			} else if(cmd_queue.peek().equals("stop")) {
				cmd_queue.remove();		

				if(wis.status()) {
					wis.stop(false);
					cmd(AUTH, "uif show");
					return "Stopping WIS.";
				} else {
					throw new Exception("WIS already stopped.");
				}
				
			} else if(cmd_queue.peek().equals("open")) {
				cmd_queue.remove();						

				try {
					wis.open();
				} catch(Exception e) {
					throw new Exception("Could not open WebGUI. ("+e.getMessage()+")");
				}
				
				if(!cmd_queue.isEmpty() && cmd_queue.peek().equals("-m")) {
					cmd_queue.remove();
					cmd(AUTH, "uif min");	
				}

				return "Opening WebGUI";
				
				
			} else if(cmd_queue.peek().equals("status")) {
				cmd_queue.remove();		
				
				if(!cmd_queue.isEmpty() && cmd_queue.peek().equals("-a")) {
					cmd_queue.remove();
					return wis.status();	
					
				} else if(!cmd_queue.isEmpty() && cmd_queue.peek().equals("-p")) {
					cmd_queue.remove();
					return wis.getPort();
				}

				return (wis.status()?"running":"not running")+". Configured port: "+wis.getPort();	
			}
			
			

		//sm
		} else if(cmd_queue.peek().equals("sm")) {
			cmd_queue.remove();

			if(cmd_queue.isEmpty() || cmd_queue.peek().equals("help")) {
				return     "sm - List of commands\r"
						 + "\t" + "list \t\tList all Solvers\r"
						 + "\t" + "create \t\tCreate new Solver-Instance\r"
						 + "\t" + "config \t\tConfigure Solver\r"
						 + "\t" + "start \t\tStart Solving\r"
						 + "\t" + "term \t\tTerminate Solver\r"
						 + "\t" + "status \t\tGet Status of Solver(-Manager)\r"
						 + "\t" + "result \t\tGet Result\r"
						 + "\t" + "delete \t\tDelete Solver-Instance\r";
								
			} else if(cmd_queue.peek().equals("list")) {
				cmd_queue.remove();		
				
				if(!cmd_queue.isEmpty()) {
					boolean show_running = true;
					boolean show_notrunning = true;
					String type = "";
					double status = -5;
					double status_max = 105;
					boolean json = false;
					
					int i = 5;
					while(cmd_queue.size()>0 && i>=0) {
						if(!cmd_queue.isEmpty() && cmd_queue.peek().equals("-r")) {
							cmd_queue.remove();		
							show_running = true;
							show_notrunning = false;
						}
						if(!cmd_queue.isEmpty() && cmd_queue.peek().equals("-nr")) {
							cmd_queue.remove();		
							show_running = false;
							show_notrunning = true;
						}
						if(!cmd_queue.isEmpty() && cmd_queue.peek().equals("-json")) {
							cmd_queue.remove();		
							json = true;
						}
						if(!cmd_queue.isEmpty() && cmd_queue.peek().equals("-t")) {
							cmd_queue.remove();		
							
							if(!cmd_queue.isEmpty()) {
								type = cmd_queue.poll();					
							} else {
								throw new Exception("Specify Solver type.");
							}	
						}
						if(!cmd_queue.isEmpty() && cmd_queue.peek().equals("-s")) {
							cmd_queue.remove();		
							
							if(!cmd_queue.isEmpty()) {
								try {
									status = Double.parseDouble(cmd_queue.peek());
									status_max = status;
									cmd_queue.remove();
									if(!cmd_queue.isEmpty()) {
										try {
											status_max = Double.parseDouble(cmd_queue.peek());
											cmd_queue.remove();
										} catch(Exception e) {}
									}
									if(status_max < status) {
										double m = status;
										status = status_max;
										status_max = m;
									}
								} catch(Exception e) {}
							}						
						}
						i--;
					}					
					return SolverManager.list(show_running, show_notrunning, type, status, status_max,json);
					
				} else {
					return SolverManager.list();
				}
				
			} else if(cmd_queue.peek().equals("create")) {
				cmd_queue.remove();		
				
				if(!cmd_queue.isEmpty()) {					
					return SolverManager.create(cmd_queue.poll());					
				}
				
				return SolverManager.create();
				
			} else if(cmd_queue.peek().equals("config")) {
				cmd_queue.remove();		

				try {
					
					int id = -1;					
					try {
						id = Integer.parseInt(cmd_queue.peek());
						cmd_queue.poll();
					} catch(Exception e) {}

					if(cmd_queue.isEmpty()) throw new Exception("Not enough parameters. You need at least one.");
					
					String config = "";
					while(!cmd_queue.isEmpty()) {	
						config += cmd_queue.poll()+" ";
					}
					if(id > -1) 
						SolverManager.configure(id, config);
					else
						SolverManager.configure(config);
					return "Solver configured.";
				} catch(Exception e) {
					throw e;
				}
								
			} else if(cmd_queue.peek().equals("start") || cmd_queue.peek().equals("solve")) {
				cmd_queue.remove();		
				
				try {
					if(!cmd_queue.isEmpty()) {							
						int id = Integer.parseInt(cmd_queue.poll());								
						if(!cmd_queue.isEmpty()) {								
							SolverManager.start(id, Integer.parseInt(cmd_queue.poll()));	
							return "Solvers started.";								
						} else {
							SolverManager.start(id);		
							return "Solver started.";					
						}
					} else {					
						SolverManager.start();
						return "Solver started.";
					}
				} catch(Exception e) {
					throw e;
				}
				
			} else if(cmd_queue.peek().equals("term")) {
				cmd_queue.remove();		
				
				try {
					if(!cmd_queue.isEmpty()) {	
						if(cmd_queue.peek().contains("-")) {
							if(cmd_queue.poll().equals("-all")) {
								SolverManager.terminateAll();
								return "All Solvers terminated.";
							}
						} else {			
							int id = Integer.parseInt(cmd_queue.poll());								
							if(!cmd_queue.isEmpty()) {								
								SolverManager.terminate(id, Integer.parseInt(cmd_queue.poll()));	
								return "Solvers terminated.";								
							} else {
								SolverManager.terminate(id);		
								return "Solver terminated.";					
							}
						}
					} else {					
						SolverManager.terminate();						
						return "Solver terminated.";
					}
				} catch(Exception e) {
					throw e;
				}
				
			} else if(cmd_queue.peek().equals("status")) {
				cmd_queue.remove();					
				
				if(!cmd_queue.isEmpty()) {		
					return "Status: "+SolverManager.round(SolverManager.status(Integer.parseInt(cmd_queue.poll())),3);	
				} else {	
					return "Status: "+SolverManager.round(SolverManager.status(),3);	
				}	
				
			} else if(cmd_queue.peek().equals("result")) {
				cmd_queue.remove();	

				int id = -1;					
				try {
					id = Integer.parseInt(cmd_queue.peek());
					cmd_queue.poll();
				} catch(Exception e) {}
						
				if(!cmd_queue.isEmpty() && cmd_queue.peek().equals("-json")) {	
					if(id>-1)
						return SolverManager.result(id).toJSON();
					else
						return SolverManager.result().toJSON();
				} else {
					if(id>-1)
						return "Result: "+SolverManager.result(id);
					else
						return "Result: "+SolverManager.result();
				}
				
				
			} else if(cmd_queue.peek().equals("delete")) {
				cmd_queue.remove();		
				
				try {
					if(!cmd_queue.isEmpty()) {	
						if(cmd_queue.peek().contains("-")) {
							if(cmd_queue.poll().equals("-all")) {
								SolverManager.deleteAll();
								return "All Solvers deleted.";
							}
						} else {	
							int id = Integer.parseInt(cmd_queue.poll());								
							if(!cmd_queue.isEmpty()) {	
								SolverManager.delete(id, Integer.parseInt(cmd_queue.poll()));					
								return "Solvers deleted.";								
							} else {
								SolverManager.delete(id);					
								return "Solver deleted.";					
							}
						}
					} else {					
						SolverManager.delete();						
						return "Solver deleted.";
					}
				} catch(Exception e) {
					throw e;
				}						
			}
			
			

		//om
		} else if(cmd_queue.peek().equals("om")) {
			cmd_queue.remove();

			if(cmd_queue.isEmpty() || cmd_queue.peek().equals("help")) {
				return     "om - List of commands\r"
						 + "Not implemented yet.";
				
			}	
			return "Not implemented yet.";
			
			

		}

		throw new Exception("Command unknown ("+cmd+"). Try help for list of commands.");		
	}
}
