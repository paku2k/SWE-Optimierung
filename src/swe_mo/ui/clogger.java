package swe_mo.ui;

import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;



public class clogger {
	final static String AUTH = "CLG";

	final private static LogType COMD  = new LogType("COMD ","220,220,220");
	final private static LogType DEBUG = new LogType("DEBUG","0,197,205");
	final private static LogType INFO  = new LogType("INFO ","0,0,255");
	final private static LogType WARN  = new LogType("WARN ","238,118,0");
	final private static LogType ERROR = new LogType("ERROR","255,48,48");
	final private static LogType FATAL = new LogType("FATAL","205,0,0");

	static int unsaved_logs = 0;
	
	
	public static void start() {
		ExcelWB.createLogFile();
		clogger.info(AUTH, "start", "Logging started");
	}
	public static void stop() {
		ExcelWB.save();
		clogger.info(AUTH, "start", "Logging stopped");
		ExcelWB.save();
	}
	public static void save() {
		if(unsaved_logs>0)
			try{ExcelWB.trySave();}catch(Exception e) {}
	}
	
	
	
	static String logStartupBuffer = "";
	private static void excecuteLog(LogType type, String AUTH, String sourceMethod, String msg) {
		Date timeStamp = new Date();
				
		if(filterLog(type,"UIlog")) {			
			UiBackend.writeTo_stLog( colorToUiF("60,60,60")+(new SimpleDateFormat("HH:mm:ss.SSS").format(timeStamp))
									+colorToUiF(type.getColor())+" | "+type.getName()+" | "
																+msg
																/*+"  @ "+AUTH+" <"+sourceMethod+">"*/);
		}	
		if(filterLog(type,"System.out")) {	
			//for testing
			System.out.println((new SimpleDateFormat("HH:mm:ss.SSS").format(timeStamp))
					+" | "+type.getName()+" | "
					+msg
					+"  @ "+AUTH+" <"+sourceMethod+">");
		}
		if(filterLog(type,"LogFile")) {
			ExcelWB.addLine_logData((new SimpleDateFormat("yy.MM.dd HH:mm:ss.SSS").format(timeStamp)), type.getName(), AUTH, sourceMethod, msg);
		}
		
		unsaved_logs++;
		save();
	}
	
	private static boolean filterLog(LogType type, String outputstream) {
		if(outputstream.equals("UIlog"))
			return type.getLevel() >= DEBUG.getLevel();
			else if(outputstream.equals("LogFile"))
				return true;
			else if(outputstream.equals("System.out"))
				return true;
		else 
			return true;
	}
	
	


	public static void dbg(String AUTH, String sourceMethod, String msg) {
		excecuteLog(DEBUG, AUTH, sourceMethod, msg);
	}	
	public static void info(String AUTH, String sourceMethod, String msg) {
		excecuteLog(INFO, AUTH, sourceMethod, msg);
	}
	public static void cmd(String AUTH, String sourceMethod, String msg) {
		excecuteLog(COMD, AUTH, sourceMethod, msg);
	}
	public static void warn(String AUTH, String sourceMethod, String msg) {
		excecuteLog(WARN, AUTH, sourceMethod, msg);
	}
	public static void err(String AUTH, String sourceMethod, String msg) {
		excecuteLog(ERROR, AUTH, sourceMethod, msg);
	}
	public static void err(String AUTH, String sourceMethod, Exception e) {
		excecuteLog(ERROR, AUTH, sourceMethod, e.getClass().getCanonicalName()+": "+e.getMessage());
	}
	public static void ftl(String AUTH, String sourceMethod, String msg) {
		excecuteLog(FATAL, AUTH, sourceMethod, msg);
	}
	
	
	
	
	
	
	
	
	private static String colorToUiF(String color) {
		return "§§"+color+"§";
	}
	
	

	private static class LogType{
		private static int level_cnt = 0;	
		
		private int level;
		private String name;
		private String color;

		public LogType(final String name, final int level, final String color) {
			this.name = name;
			this.level = level;
			this.color = color;
		}
		public LogType(final String name, final String color) {
			this(name,level_cnt++,color);
		}
		public LogType(final String name, final int level) {
			this(name,level,"0,0,0");
		}
		@SuppressWarnings("unused")
		public LogType(final String name) {
			this(name,level_cnt++);
		}
		

		public int getLevel() {
			return level;
		}
		public String getName() {
			return name;
		}		
		public String getColor() {
			return color;
		}
	}
	
	
	public static class ExcelWB{
		private static String directory = "data/log";
		private static XSSFWorkbook workbook;
		private static XSSFSheet sheet_logData;
		private static String filename;
		private static int row_cnt = 0;

		
		
		public static void createLogFile() {
	        filename = "MO_logdata_"+(new SimpleDateFormat("yyMMdd_HH-mm-ss").format(new java.util.Date()))+".xlsx";
	        
			workbook = new XSSFWorkbook();
	        sheet_logData = workbook.createSheet("logData");
	        sheet_logData.setColumnWidth(0, 5000);
	        sheet_logData.setColumnWidth(1, 2000);
	        sheet_logData.setColumnWidth(2, 6000);
	        sheet_logData.setColumnWidth(3, 6000);
	        sheet_logData.setColumnWidth(4, 20000);
	        addLine_logData("Timestamp","Type","sourceClass","sourceMethod","message");
	        addLine_logData("","","","","");
	        save();
		}         
	        
	    public static void addLine_logData(String timestamp, String type, String sourceClass, String sourceMethod, String message) {  
	    	Row r = sheet_logData.createRow(row_cnt++);
            Cell Ctimestamp 	= r.createCell(0);
            Cell Ctype 			= r.createCell(1);
            Cell CsourceClass 	= r.createCell(2);
            Cell CsourceMethod 	= r.createCell(3);
            Cell Cmessage 		= r.createCell(4);

            Ctimestamp.setCellValue(timestamp);
            Ctype.setCellValue(type);
            CsourceClass.setCellValue(sourceClass);
            CsourceMethod.setCellValue(sourceMethod);
            Cmessage.setCellValue(message);
	    }
	    
	    public static void trySave() throws IOException {
	    	try {
	    		save_checkedPath();
	    	} catch (Exception e) {
	    		throw e;
	    	}
	    	unsaved_logs = 0;
	    }
	    
	    public static void save() {
	    	try {
	    		Path path = Paths.get(directory);
	    		if(Files.notExists(path)) {
	    			Files.createFile(Files.createDirectories(path));
	    		}
	    		
	    		trySave();
	        } catch(Exception a) {
		    	try {
		    		trySave();
		        } catch(Exception e) {
		        	clogger.err(AUTH, "ExcelWB-save", e);
		        	if(!filename.substring(filename.length()-8).equals("(1).xlsx")) {
			        	clogger.warn(AUTH, "ExcelWB-save", "Trying to save a copy of "+filename+".");
			        	filename = filename.substring(0, filename.length()-5).concat(" (1).xlsx");
			        	save();
		        	} else {
			        	clogger.err(AUTH, "ExcelWB-save", "Failed to save "+filename+".");		        		
		        	}
		        }
	        }
	    }
	    private static void save_checkedPath() throws IOException {    		
            FileOutputStream out = new FileOutputStream(new File(directory, filename));
            workbook.write(out);
            out.close();	    	
	    }
	}
		
}