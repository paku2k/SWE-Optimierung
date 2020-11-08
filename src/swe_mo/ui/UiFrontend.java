package swe_mo.ui;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.util.ArrayList;
import java.util.Arrays;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.*;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;




public class UiFrontend {
	private final static String AUTH = "UiF";
	
	private static boolean running = false;
	private static boolean exit = false;
	
	private static int cmd_last;
	
 	
	
	
	
	
/*
 * SSS FUNCTIONS (public)
 * 
 * start
 * stop
 * status
 * 
 */
	
	/**
	 * start the UiFrontend
	 * run the UiFrontend 
	 * and dispose
	 * 
	 * @wbp.parser.entryPoint
	 */
	public static void start() throws Exception{
		if(exit) return;
		try {
			display = Display.getDefault();
			initComponents();				
			shell.open();
			shell.layout();	
			
			running = true;
			clogger.info(AUTH, "start", "UiFrontend started");
		} catch (Exception e) {
			throw e;
		}


		while (!shell.isDisposed() && running) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}	
		display.dispose();
		shell = null;
		clogger.info(AUTH, "run", "UiFrontend stopped");
	}
	
	/**
	 * stop the UiFrontend
	 * 
	 * @param permanently used for disabling UiF at app exit
	 */
	public static void stop(boolean permanently) {
		running = false;
		exit = permanently;
	}
	
	/**
	 * returns the status 
	 * 
	 * @return status (1=running, 0=not running, -1=exit)
	 */
	public static int status() {
		if(exit) {
			return -1;
		} else if(running) {
			return 1;
		}
		return 0;
	}
	
	 	
	
	
	
	
/*
 * MODIFIERS FOR ASYNC ACTIONS (public)
 * 
 * wisStatusChange
 * stLog_newEntry
 * setMinimized 
 * 
 */
	
	/**
	 * update status of WIS (via asyncExec)
	 * 
	 * @param state started or not started
	 * @param port	port of running wis
	 */		
	public static void wisStatusChange(boolean state, int port) {
		if(!running) return;
		btnStartWis.getDisplay().asyncExec(new Runnable() {
			public void run() {
				if(state) {
					btnStartWis.setText("Stop WebGUI");
					lblWisStatusContent.setForeground(SWTResourceManager.getColor(0, 204, 0));
					lblWisStatusContent.setText("Running at: localhost:"+port+"/");
				} else {
					btnStartWis.setText("Start WebGUI");		
					lblWisStatusContent.setForeground(SWTResourceManager.getColor(255, 0, 0));			
					lblWisStatusContent.setText("Not Started");
				}
				btnOpenPage.setEnabled(state);
			}
		});
	}
	
	/**
	 * write an entry line to log
	 * 
	 * @param entry the log string with color information (§§rrr,ggg,bbb§)
	 */
	public static void stLog_newEntry(String entry) {
		if(!running) return;
		stLog.getDisplay().asyncExec(new Runnable() {
			public void run() {
				try {
					if(stLog.getText().length()>0)
						stLog.append("\n");
					
					//split by §§ and delete all empty entries
					ArrayList<String> colorsplit = new ArrayList<String>(Arrays.asList(entry.split("§§")));
					for(int i=0; i < colorsplit.size(); i++) {
						if(colorsplit.get(i).isEmpty()) colorsplit.remove(i);
					}

					//write every item with correct color
					for(String s : colorsplit) {
						String[] str = s.split("§");
						stLog.append(str[1]);

						StyleRange styleRange = new StyleRange();
						styleRange.length = str[1].length();
						styleRange.start = stLog.getText().length()-styleRange.length;
						styleRange.foreground = new Color(stLog.getDisplay(), parseRGB(str[0]));
						stLog.setStyleRange(styleRange);
					}				
					
					setCaretPosEnd("stLog");
				} catch(Exception e) {
					clogger.err(AUTH, "stLog_newEntry", e);
				}
			}
		});
	}
	
	/**
	 * minimize or show application window
	 * 
	 * @param minimize true or false
	 */
	public static void setMinimized(boolean minimize) {
		if(!running) return;
		shell.getDisplay().asyncExec(new Runnable() {
			public void run() {
				shell.setMinimized(minimize);
				if(!minimize) {
					shell.setSize(600, 550);	
					scaleShellToDpi(shell);
				}
			}
		});
	}
	
 	
	
	
	
	
/*
 * EVENT LISTENERS (private)
 * 
 * -> keyadapter_fnkeys
 * -> keyadapter_cmd
 * -> verifykeyadapter_cmd
 * -> mouseadapter_btnStartWis
 * -> mouseadapter_btnOpenPage
 * -> shelllistener
 * 
 * specialKeys
 * 
 */	
	
	/** doubleclick filter threshold in ms
	 *  two clicks are counted as doubleclick when less than the threshold apart */
	private final static int DOUBLECLICK_THRESHOLD = 1000; 

	
	
	private static KeyAdapter keyadapter_fnkeys = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			specialKeys(e,true);
		}
		@Override
		public void keyReleased(KeyEvent e) {
			specialKeys(e,false);
			
			if (e.keyCode==SWT.F12){
				toggleWIS();
			}
			if (e.keyCode==SWT.F11){
				try {
					if((boolean)UiBackend.cmd(AUTH, "wis status -a")) {
						UiBackend.cmd(AUTH, "wis open");
					} 
				} catch(Exception ex) {
					clogger.err(AUTH, "keyadapter_fnkeys", ex);
				}
			}
		}
	};
	
	private static KeyAdapter keyadapter_cmd = new KeyAdapter() {
		@Override
		public void keyReleased(KeyEvent e) {
			//System.out.println(e.character + " ("+e.keyCode+")");
			
			if (e.keyCode==99) {
				if(ALT_pressed) {
					Point sel = stCmd.getSelection();
					String selectedText = stCmd.getText().substring(sel.x, sel.y).replace("> ","").replace(">","").replace("\n", "").replace("\r", "");
					stCmd.setText(stCmd.getText()+selectedText);
					setCaretPosEnd("stCmd");
				}
			}
			
			if (e.keyCode==13 || //return
				e.keyCode==SWT.KEYPAD_CR){
				readCommand();
			}
			if (e.keyCode==SWT.ARROW_UP ||
				e.keyCode==SWT.ARROW_DOWN) {
				stCmd.setText(stCmd.getText().substring(0,cmd_last)+travLastCommands(e.keyCode));	
				setCaretPosEnd("stCmd");
			}
			if (e.keyCode==SWT.ESC) {
				stCmd.setText(stCmd.getText().substring(0,cmd_last));
				setCaretPosEnd("stCmd");
			}
		}
	};
	
	private static VerifyKeyListener verifykeyadapter_cmd = new VerifyKeyListener() {
		public void verifyKey(VerifyEvent e) {
			if(specialKeys(e, true)) return; //if pressed key is special key
							
			if (e.keyCode==99) { //c
				if(CTRL_pressed) return; //allow ctrl+c
				if(ALT_pressed) return;	//allow alt+c for direct copy
			}
			if (e.keyCode==SWT.ARROW_LEFT || e.keyCode==SWT.ARROW_RIGHT) {
				if(SHIFT_pressed) return; //allow marking
			}
			if (e.keyCode==97) { //a
				if(CTRL_pressed) {
					stCmd.setSelection(cmd_last, stCmd.getText().length());
				}
			}
			if (e.keyCode==118) { //v
				e.doit = false;
				if(stCmd.getCaretOffset() >= cmd_last) 
					e.doit = true;
				else
					setCaretPosEnd("stCmd");
					
				if(!CTRL_pressed) return;
				//check clipboard text before pasting
				try {
					e.doit = false;
					String clipboard = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
					clipboard = clipboard.replace("> ","").replace(">","").replace("\n", "").replace("\r", "");
					stCmd.setText(stCmd.getText()+clipboard);	
				} catch (Exception ex) {

				}					
				setCaretPosEnd("stCmd");
			}
			
			if (stCmd.getCaretOffset() < cmd_last) {
				setCaretPosEnd("stCmd");
			}
			if (e.keyCode==SWT.ARROW_UP ||
				e.keyCode==SWT.ARROW_DOWN ||
				e.keyCode==13 || //return
				e.keyCode==SWT.KEYPAD_CR ||
				e.keyCode==16777223 || //pos1
				e.keyCode==SWT.PAGE_UP) {
				e.doit = false;				
			}
			if (e.keyCode==SWT.ARROW_LEFT ||
				e.keyCode==8) {//backspace
				if(stCmd.getCaretOffset() > cmd_last) {
					e.doit = true;
				} else {
					e.doit = false;
				}
			}
		}
	};
	
	private static MouseAdapter mouseadapter_btnStartWis = new MouseAdapter() {
		long last_click;
		@Override
		public void mouseUp(MouseEvent e) {
			if(last_click + DOUBLECLICK_THRESHOLD > System.currentTimeMillis()) return;
			toggleWIS();
			last_click = System.currentTimeMillis();	
		}
	};
	
	private static MouseAdapter mouseadapter_btnOpenPage = new MouseAdapter() {
		long last_click;
		@Override
		public void mouseUp(MouseEvent e) {
			if(last_click + DOUBLECLICK_THRESHOLD > System.currentTimeMillis()) return;
			
			try {
				UiBackend.cmd(AUTH, "wis open -m");
			} catch(Exception ex) {
				clogger.err(AUTH, "mouseadapter_btnOpenPage", ex);
			}
			last_click = System.currentTimeMillis();
		}
	};
	
	private static ShellListener shelllistener = new ShellListener() {
		@Override
		public void shellActivated(ShellEvent e) {}

		@Override
		public void shellClosed(ShellEvent e) {
			// TODO Auto-generated method stub
	        MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
	        messageBox.setText("Warning");
	        messageBox.setMessage("You are about to stop and close the application. Continue?");
        	e.doit = false;	
	        if (messageBox.open() == SWT.YES) {
	    		try {
					UiBackend.cmd(AUTH, "app exit");
		    		running = false;
		        	e.doit = true;
	    		} catch(Exception ex) {
	    			clogger.err(AUTH, "shelllistener", ex);
	    		}
	        }	
		}

		@Override
		public void shellDeactivated(ShellEvent e) {}

		@Override
		public void shellDeiconified(ShellEvent e) {}

		@Override
		public void shellIconified(ShellEvent e) {}
	};
	
	
	/**
	 * track special keys (CTRL, ALT, SHIFT) for correct handling of shortcuts (e.g. copy paste actions)	
	 */
	private static boolean CTRL_pressed = false;
	private static boolean ALT_pressed = false;
	private static boolean SHIFT_pressed = false;
	private static boolean specialKeys(KeyEvent e, boolean pressed) {
		if (e.keyCode==SWT.CTRL) {
			CTRL_pressed = pressed;
			e.doit = true;
		} else if (e.keyCode==SWT.ALT) {
			ALT_pressed = pressed;
			e.doit = true;
		} else if (e.keyCode==SWT.SHIFT) { 
			SHIFT_pressed = pressed; 
			e.doit = true;
		} else {
			return false;
		}
		return true;
	}
	
 	
	
	
	
	
/*
 * EVENT HANDLERS (private)
 * 
 * toggleWIS
 * readCommand
 * travLastCommands
 * setCaretPosEnd
 * 
 */
	
	/**
	 * toggle WebInterfaceServer on/off
	 */
	private static void toggleWIS() {
		try {
			if((boolean)UiBackend.cmd(AUTH, "wis status -a")) {
				lblWisStatusContent.setForeground(SWTResourceManager.getColor(255, 153, 0));
				lblWisStatusContent.setText("Stopping");
				UiBackend.cmd(AUTH, "wis stop");	
			} else {
				lblWisStatusContent.setForeground(SWTResourceManager.getColor(255, 153, 0));
				lblWisStatusContent.setText("Starting");
				UiBackend.cmd(AUTH, "wis start");	
			}	
		} catch(Exception e) {
			clogger.err(AUTH, "toggleWIS", e);
		}
	}	
	
	/**
	 * read the command from stCmd and return the answer
	 */
	private static void readCommand() {
		String cmd = stCmd.getText().substring(cmd_last,stCmd.getText().length()).replace("\n", "").replace("\r", "");
		if(cmd.equals("")) return;
		if(command_history.contains(cmd)) command_history.remove(cmd);
		command_history.add(cmd);		
		travLastCommands_relativePosition = 0;
		String ans = "";
		try {
			ans = UiBackend.cmd(AUTH, cmd).toString();
		} catch(Exception e) {
			clogger.err(AUTH, "readCommand", e);
			ans = "ERR: "+e.getMessage();
		}
		stCmd.setText(stCmd.getText()+"\r"+ans+"\r\r>> ");
		setCaretPosEnd("stCmd");
		cmd_last = stCmd.getText().length();
	}
	
	
	static int travLastCommands_relativePosition = 0;
	private static ArrayList<String> command_history  = new ArrayList<String>();

	/**
	 * traverse last commands with arrow_up and arrow_down keys
	 */
	private static String travLastCommands(int keyCode) {
		if(keyCode==SWT.ARROW_UP) {
			travLastCommands_relativePosition++;
		}else if(keyCode==SWT.ARROW_DOWN) {
			travLastCommands_relativePosition--;
		}

		if(travLastCommands_relativePosition > command_history.size())
			travLastCommands_relativePosition = command_history.size();
		if(travLastCommands_relativePosition < 1) 
			travLastCommands_relativePosition = 0;
		
		if(travLastCommands_relativePosition < 1) {
			return "";
		}
		
		try {
			return command_history.get(command_history.size()-travLastCommands_relativePosition);
		} catch(Exception e) {
			clogger.err(AUTH, "travLastCommands", e);
			return "";
		}
	}
	
	/**
	 * set caret position to end of entry for stCmd or stLog
	 * 
	 * @param st String defining of which styledText field the caret is to be set 
	 */
	private static void setCaretPosEnd(String st) {
		if(st.equals("stCmd")) {
			stCmd.setCaretOffset(stCmd.getText().length());
			stCmd.setTopIndex(stCmd.getLineCount() - 1);		
		} else if(st.equals("stLog")) {
			stLog.setCaretOffset(stLog.getText().length());
			stLog.setTopIndex(stLog.getLineCount() - 1);		
		} 
	}
	
 	
	
	
	
	
/*
 * INIT FUNCTIONS (private)
 * 
 * initComponents
 * 
 */
	
	private static Shell shell;
	private static Display display;
	private static Button btnStartWis;
	private static Button btnOpenPage;
	private static Label lblWisStatusContent;
	private static StyledText stLog;
	private static StyledText stCmd;
	
	
	/**
	 * initialize components for window
	 */
	private static void initComponents() {
		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN & (~SWT.RESIZE));
		shell.setImage(SWTResourceManager.getImage(UiFrontend.class, "/icon.png"));
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setSize(600, 550);
		shell.setText("Metaheuristic Optimization");
		shell.addShellListener(shelllistener);
		shell.addKeyListener(keyadapter_fnkeys);
		
		Label lblHeader = new Label(shell, SWT.CENTER);
		lblHeader.setFont(SWTResourceManager.getFont("Segoe UI Semibold", 18, SWT.NORMAL));
		lblHeader.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblHeader.setBounds(10, 10, 568, 29);
		lblHeader.setText("Metaheuristic Optimization UI");

		Group grpWebgui = new Group(shell, SWT.NONE);
		grpWebgui.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		grpWebgui.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		grpWebgui.setText("WebGUI");
		grpWebgui.setBounds(10, 64, 568, 58);
		
		btnStartWis = new Button(grpWebgui, SWT.FLAT);
		btnStartWis.setToolTipText("F12");
		btnStartWis.setForeground(SWTResourceManager.getColor(34, 139, 34));
		btnStartWis.setBounds(448, 21, 110, 23);
		btnStartWis.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		btnStartWis.setText("Start WebGUI");
		btnStartWis.addKeyListener(keyadapter_fnkeys);
		btnStartWis.addMouseListener(mouseadapter_btnStartWis);		
		
		btnOpenPage = new Button(grpWebgui, SWT.FLAT);
		btnOpenPage.setToolTipText("F11");
		btnOpenPage.setEnabled(false);
		btnOpenPage.setText("Open in Browser");
		btnOpenPage.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		btnOpenPage.setBounds(332, 21, 110, 23);
		btnOpenPage.addKeyListener(keyadapter_fnkeys);
		btnOpenPage.addMouseListener(mouseadapter_btnOpenPage);
		
		Label lblWisStatus = new Label(grpWebgui, SWT.NONE);
		lblWisStatus.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblWisStatus.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblWisStatus.setBounds(10, 29, 49, 15);
		lblWisStatus.setText("Status:");
		
		lblWisStatusContent = new Label(grpWebgui, SWT.NONE);
		lblWisStatusContent.setForeground(SWTResourceManager.getColor(255, 0, 0));
		lblWisStatusContent.setText("Not Started");
		lblWisStatusContent.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		lblWisStatusContent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblWisStatusContent.setBounds(65, 29, 250, 15);
		
		Group grpCmdLog = new Group(shell, SWT.NONE);
		grpCmdLog.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		grpCmdLog.setText("CMD / LOG");
		grpCmdLog.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		grpCmdLog.setBounds(10, 151, 568, 357);
		
		stLog = new StyledText(grpCmdLog, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		stLog.setEditable(false);
		stLog.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		stLog.setSelectionBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		stLog.setFont(SWTResourceManager.getFont("Lucida Console", 10, SWT.NORMAL));
		stLog.setBounds(0, 25, 568, 119);
		stLog.addKeyListener(keyadapter_fnkeys);
		
		stCmd = new StyledText(grpCmdLog, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		stCmd.setSelectionForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		stCmd.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		stCmd.setText(">> ");
		stCmd.setSelectionBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		stCmd.setFont(SWTResourceManager.getFont("Lucida Console", 10, SWT.NORMAL));
		stCmd.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		stCmd.setMargins(5, 0, 5, 0);;
		stCmd.setBounds(0, 150, 568, 207);
		stCmd.addVerifyKeyListener(verifykeyadapter_cmd);
		stCmd.addKeyListener(keyadapter_fnkeys);
		stCmd.addKeyListener(keyadapter_cmd);
		
		cmd_last = stCmd.getText().length();
		
		scaleCompToDpi(shell);	
	}
	
 	
	
	
	
	
/*
 * HELPER FUNCTIONS (private)
 * 
 * parseRGB
 * 
 * scaleCompToDpi
 * scaleShellToDpi
 * scaleToDpi
 * scaleControl 
 * 
 */	
	
	/**
	 * parses a RGB Object from a String with RGB values
	 * 
	 * @param 	input	a String with RGB values in the form of "rrr,ggg,bbb"
	 * @return			the RGB object, if error while parsing (0,0,0)
	 */
	public static RGB parseRGB(String input) {	
		String[] values = input.split(",");
		try {
	        return new RGB(Integer.valueOf(values[0]),  // r
	                       Integer.valueOf(values[1]),  // g
	                       Integer.valueOf(values[2])); // b 
		} catch(Exception e) {
			return new RGB(0,0,0);
		}
	}
	
	
	/** current screen resolution (dpi) */
	private static final int DPI_CURRENT = Display.getDefault().getDPI().x;
	/** default screen resolution of the project (dpi) */
	private static final float DPI_DEFAULT = 82.0f;
	/** scale factor of screen resolution (current/default) */
	private static final float DPI_SCALE = DPI_CURRENT / DPI_DEFAULT;

	/**
	 * scales the window (shell + components) to the screen resolution
	 * 
	 * @input	composite	the to be scaled composite	   
	 */
	private static void scaleCompToDpi(Composite composite) {		
		scaleShellToDpi(composite); // Scale the shell		
		scaleToDpi(composite); // Scale all components
	}

	/**
	 * scales the shell to the screen resolution
	 * 
	 * @input	composite	the to be scaled shell   
	 */
	private static void scaleShellToDpi(Composite composite) {
		Point size = composite.getSize();
		size.x *= DPI_SCALE;
		size.y *= DPI_SCALE;
		composite.setSize(size);
	}

	/**
	 * scales the composite and its children to the screen resolution recursively
	 * 
	 * @input	composite	the to be scaled composite (parent)
	 */	
	private static void scaleToDpi(Composite composite) {
	    for(Control control : composite.getChildren()) {
	        if(control instanceof Composite) {
	            scaleToDpi((Composite) control);
	        }
	        scaleControl(control);
	    }
	}

	/**
	 * scales size and position of the control element
	 * 
	 * @input	control		the to be scaled control object
	 */	
	private static void scaleControl(Control control) {
	    int x = (int) (control.getLocation().x * DPI_SCALE);
	    int y = (int) (control.getLocation().y * DPI_SCALE);
	    int w = (int) (control.getSize().x * DPI_SCALE);
	    int h = (int) (control.getSize().y * DPI_SCALE);

	    control.setBounds(x, y, w, h);
	}	
}
