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
	final static String AUTH = "UiF";
	private static boolean running = false;
	private static boolean exit = false;
	protected static Shell shell;
	static Display display;
	
	private static int cmd_last;
	
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public static void start() {
		if(exit) return;
		try {
			display = Display.getDefault();
			initComponents();
			scaleCompToDpi(shell);			
			addEventListenersToShell();		
			shell.open();
			shell.layout();	
			
			running = true;
			clogger.info(AUTH, "start", "UiFrontend started");
		} catch (Exception e) {
			clogger.err(AUTH, "start", e);
			e.printStackTrace();
			return;
		}

		run();
	}
	private static void run() {
		while (!shell.isDisposed() && running) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}	
		display.dispose();
		shell = null;
		clogger.info(AUTH, "run", "UiFrontend stopped");
	}
	
	public static void stop(boolean permanently) {
		running = false;
		exit = permanently;
	}
	
	public static boolean running() {
		return running;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	/**
	 * async modifiers 
	 */		
	static Button btnStartWis;
	static Button btnOpenPage;
	static Label lblWisStatusContent;
	static StyledText stLog;
	static StyledText stCmd;
	

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
		
	
	

	
	
	
	
	/**
	 * Event Listeners
	 */
	final static int DOUBLECLICK_THRESHOLD = 1000;
	
	
	static KeyAdapter keyadapter = new KeyAdapter() {
		@Override
		public void keyReleased(KeyEvent e) {
			if (e.keyCode==SWT.F12){
				if((boolean)UiBackend.cmd(AUTH, "wis get status")) {
					lblWisStatusContent.setForeground(SWTResourceManager.getColor(255, 153, 0));
					lblWisStatusContent.setText("Stopping");
					UiBackend.cmd(AUTH, "stop wis");	
				} else {
					lblWisStatusContent.setForeground(SWTResourceManager.getColor(255, 153, 0));
					lblWisStatusContent.setText("Starting");
					UiBackend.cmd(AUTH, "start wis");	
				}
				
			}
			if (e.keyCode==SWT.F11){
				if((boolean)UiBackend.cmd(AUTH, "wis get status")) {
					UiBackend.cmd(AUTH, "wis open webgui");
				} 
			}
		}
	};
	
	
	private static void addEventListenersToShell() {
		shell.addShellListener(new ShellListener() {
			@Override
			public void shellActivated(ShellEvent e) {
				// TODO Auto-generated method stub				
			}

			@Override
			public void shellClosed(ShellEvent e) {
				// TODO Auto-generated method stub
		        MessageBox messageBox = new MessageBox(shell, SWT.APPLICATION_MODAL | SWT.YES | SWT.NO);
		        messageBox.setText("Warning");
		        messageBox.setMessage("You are about to close the program. Continue?");
		        if (messageBox.open() == SWT.YES) {
		    		running = false;
					UiBackend.cmd(AUTH, "exit");
		        	e.doit = true;
		        } else
		        	e.doit = false;				
			}

			@Override
			public void shellDeactivated(ShellEvent e) {
				// TODO Auto-generated method stub				
			}

			@Override
			public void shellDeiconified(ShellEvent e) {
				// TODO Auto-generated method stub				
			}

			@Override
			public void shellIconified(ShellEvent e) {
				// TODO Auto-generated method stub				
			}
		    });

		shell.addKeyListener(keyadapter);
	}
	
	
	
	/**
	 * Create Window Elements 
	 */
	protected static void initComponents() {
		shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN & (~SWT.RESIZE));
		shell.setImage(SWTResourceManager.getImage(UiFrontend.class, "/icon.png"));
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setSize(600, 550);
		shell.setText("Metaheuristic Optimization");
		
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
		btnStartWis.addKeyListener(keyadapter);
		btnStartWis.addMouseListener(new MouseAdapter() {
			long last_click;
			@Override
			public void mouseUp(MouseEvent e) {
				if(last_click + DOUBLECLICK_THRESHOLD > System.currentTimeMillis()) return;
				
				if((boolean)UiBackend.cmd(AUTH, "wis get status")) {
					lblWisStatusContent.setForeground(SWTResourceManager.getColor(255, 153, 0));
					lblWisStatusContent.setText("Stopping");
					UiBackend.cmd(AUTH, "stop wis");	
				} else {
					lblWisStatusContent.setForeground(SWTResourceManager.getColor(255, 153, 0));
					lblWisStatusContent.setText("Starting");
					UiBackend.cmd(AUTH, "start wis");	
				}			
				last_click = System.currentTimeMillis();	
			}
		});		
		btnStartWis.setForeground(SWTResourceManager.getColor(34, 139, 34));
		btnStartWis.setBounds(448, 21, 110, 23);
		btnStartWis.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		btnStartWis.setText("Start WebGUI");
		
		btnOpenPage = new Button(grpWebgui, SWT.FLAT);
		btnOpenPage.setToolTipText("F11");
		btnOpenPage.setEnabled(false);
		btnOpenPage.addKeyListener(keyadapter);
		btnOpenPage.addMouseListener(new MouseAdapter() {
			long last_click;
			@Override
			public void mouseUp(MouseEvent e) {
				if(last_click + DOUBLECLICK_THRESHOLD > System.currentTimeMillis()) return;

				UiBackend.cmd(AUTH, "wis open webgui");
				last_click = System.currentTimeMillis();
			}
		});
		btnOpenPage.setText("Open in Browser");
		btnOpenPage.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		btnOpenPage.setBounds(332, 21, 110, 23);
		
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
		stLog.addKeyListener(keyadapter);
		stLog.setEditable(false);
		stLog.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		stLog.setSelectionBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_FOREGROUND));
		stLog.setFont(SWTResourceManager.getFont("Lucida Console", 10, SWT.NORMAL));
		stLog.setBounds(0, 25, 568, 119);
		
		stCmd = new StyledText(grpCmdLog, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		stCmd.setSelectionForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		stCmd.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		stCmd.addVerifyKeyListener(new VerifyKeyListener() {
			public void verifyKey(VerifyEvent e) {
				if(specialFnKeys(e, true)) return;
								
				if (e.keyCode==99) {
					if(CTRL_pressed) return; //allow ctrl+c
					if(ALT_pressed) return;	//allow alt+c for direct copy
				}
				if (e.keyCode==SWT.ARROW_LEFT || e.keyCode==SWT.ARROW_RIGHT) {
					if(SHIFT_pressed) return; //allow marking
				}
				if (e.keyCode==97) {
					if(CTRL_pressed) {
						stCmd.setSelection(cmd_last, stCmd.getText().length());
					}
				}
				if (e.keyCode==118) {
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
		});
		stCmd.setText(">> ");
		stCmd.addKeyListener(keyadapter);
		stCmd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				specialFnKeys(e,true);
			}
			@Override
			public void keyReleased(KeyEvent e) {
				//System.out.println(e.character + " ("+e.keyCode+")");
				specialFnKeys(e,false);
				
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
					readCommand(e);
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
		});
		stCmd.setSelectionBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		stCmd.setFont(SWTResourceManager.getFont("Lucida Console", 10, SWT.NORMAL));
		stCmd.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		stCmd.setMargins(5, 0, 5, 0);;
		stCmd.setBounds(0, 150, 568, 207);
		
		cmd_last = stCmd.getText().length();
	}
	
	
	private static ArrayList<String> command_history  = new ArrayList<String>();
	
	private static void readCommand(KeyEvent e) {
		String cmd = stCmd.getText().substring(cmd_last,stCmd.getText().length()).replace("\n", "").replace("\r", "");
		if(cmd.equals("")) return;
		if(command_history.contains(cmd)) command_history.remove(cmd);
		command_history.add(cmd);		
		travLastCommands_relativePosition = 0;
		String ans = "";
		try {
			ans = UiBackend.cmd(AUTH, cmd).toString();
		} catch(Exception ex) {
			clogger.err(AUTH, "readCommand", ex);
		}
		stCmd.setText(stCmd.getText()+"\r"+ans+"\r\r>> ");
		setCaretPosEnd("stCmd");
		cmd_last = stCmd.getText().length();
	}
	
	static int travLastCommands_relativePosition = 0;
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
	
	private static void setCaretPosEnd(String st) {
		if(st.equals("stCmd")) {
			stCmd.setCaretOffset(stCmd.getText().length());
			stCmd.setTopIndex(stCmd.getLineCount() - 1);		
		} else if(st.equals("stLog")) {
			stLog.setCaretOffset(stLog.getText().length());
			stLog.setTopIndex(stLog.getLineCount() - 1);		
		} 
	}
	
	
	

	private static boolean CTRL_pressed = false;
	private static boolean ALT_pressed = false;
	private static boolean SHIFT_pressed = false;
	private static boolean specialFnKeys(KeyEvent e, boolean pressed) {
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



	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Helper Function to parse RGB from string "rrr,ggg,bbb"
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
	
	
	
	/**
	 * Scaling the Window (for scaled Windows Screens) 
	 */
	public static final int DPI_CURRENT = Display.getDefault().getDPI().x;
	public static final float DPI_DEFAULT = 82.0f;//96
	public static final float DPI_SCALE = DPI_CURRENT / DPI_DEFAULT;
		
	public static void scaleCompToDpi(Composite composite) {
		// Scale the shell
		scaleShellToDpi(composite);
		
		// Scale all components
		scaleToDpi(composite);
	}
	
	public static void scaleShellToDpi(Composite composite) {
		Point size = composite.getSize();
		size.x *= DPI_SCALE;
		size.y *= DPI_SCALE;
		composite.setSize(size);
	}
	
	public static void scaleToDpi(Composite composite) {
	    for(Control control : composite.getChildren()) {
	        if(control instanceof Composite) {
	            scaleToDpi((Composite) control);
	        }
	        scaleControl(control);
	    }
	}

	private static void scaleControl(Control control) {
	    int x = (int) (control.getLocation().x * DPI_SCALE);
	    int y = (int) (control.getLocation().y * DPI_SCALE);
	    int w = (int) (control.getSize().x * DPI_SCALE);
	    int h = (int) (control.getSize().y * DPI_SCALE);

	    control.setBounds(x, y, w, h);
	}	
}
