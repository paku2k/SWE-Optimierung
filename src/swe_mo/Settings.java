package swe_mo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import swe_mo.ui.clogger;





public class Settings {
	final static String AUTH = "SET";
	private static String directory = "data/cfg";
	private static String filename = "swe_mo.mocfg";

	private static Map<String, Object> settings = new HashMap<String, Object>();
	
	
	

	public static void factorySettings() {		
		//define all application settings here
		settings.put("minUifOnWebguiOpen", true);
		settings.put("openWebguiOnStartup", false);
		settings.put("defaultAlgorithm", "DEbest1");
		settings.put("defaultOptimizer", "DeepRand");
		settings.put("WebGUIshowDelSolvers", true);
		settings.put("WebGUIshowDelOptims", true);
		
	}
	
	
	

	@SuppressWarnings("unchecked")
	public static String listAll(boolean json) {  		
        if(json) {		
        	JSONArray listOfSettings = new JSONArray();
            for(String key : settings.keySet()) {  
            	JSONObject obj = new JSONObject();
            	obj.put("key", key);
            	obj.put("value", settings.get(key));
            	listOfSettings.add(obj);	
            }    	
            JSONObject jsonobj = new JSONObject();	
            jsonobj.put("cfg_list", listOfSettings);
    		return jsonobj.toJSONString();
        	
        } else {
            String s = "List of application settings\n";
            for(String key : settings.keySet()) {  
            	String val = settings.get(key).toString();
            	s += "\n ";
            	for(int i=0; i<22-key.length(); i++)
            		s += " ";
            	s += key+"\t"+val;
            }  
    		return s;        	
        }
	}
	
	
	

	public static Object get(String key) throws Exception {
		if(settings.get(key)==null) throw new Exception("Setting not found ("+key+").");

		Class<?> cl = settings.get(key).getClass();
		
		if(cl.equals(byte.class) || cl.equals(Byte.class)){
			return (byte) settings.get(key);
			
		} else if(cl.equals(short.class) || cl.equals(Short.class)){
			return (short) settings.get(key);
			
		} else if(cl.equals(int.class) || cl.equals(Integer.class)){
			return (int) settings.get(key);
			
		} else if(cl.equals(long.class) || cl.equals(Long.class)){
			return (long) settings.get(key);
			
		} else if(cl.equals(float.class) || cl.equals(Float.class)){
			return (float) settings.get(key);
			
		} else if(cl.equals(double.class) || cl.equals(Double.class)){
			return (double) settings.get(key);
			
		} else if(cl.equals(boolean.class) || cl.equals(Boolean.class)){
			return (boolean) settings.get(key);
			
		} else if(cl.equals(String.class)){
			return (String) settings.get(key);
			
		}		
		return settings.get(key);
	}

	public static void set(String key, Object value) throws Exception {
		if(settings.get(key)==null) throw new Exception("Setting not found ("+key+").");
		
		Class<?> cl = settings.get(key).getClass();
		
		try {			
			if(cl.equals(byte.class) || cl.equals(Byte.class)){
				settings.put(key, Byte.parseByte(value.toString()));
				
			} else if(cl.equals(short.class) || cl.equals(Short.class)){
				settings.put(key, Short.parseShort(value.toString()));
				
			} else if(cl.equals(int.class) || cl.equals(Integer.class)){
				settings.put(key, Integer.parseInt(value.toString()));
				
			} else if(cl.equals(long.class) || cl.equals(Long.class)){
				settings.put(key, Long.parseLong(value.toString()));
				
			} else if(cl.equals(float.class) || cl.equals(Float.class)){
				settings.put(key, Float.parseFloat(value.toString()));
				
			} else if(cl.equals(double.class) || cl.equals(Double.class)){
				settings.put(key, Double.parseDouble(value.toString()));
				
			} else if(cl.equals(boolean.class) || cl.equals(Boolean.class)){
				settings.put(key, Boolean.parseBoolean(value.toString()));
				
			} else if(cl.equals(String.class)){
				settings.put(key, value.toString());
				
			} else {
				settings.put(key, value);
				
			}
		} catch(Exception e) {
			throw e;
		}
	}	
	
	
	

	public static void load() {		
		try {
			//JSON parser object to parse read file	         
	        FileReader f = new FileReader(directory+"/"+filename);
	        		
            //Read JSON file
	        JSONObject listOfParameters = (JSONObject) new JSONParser().parse(f);      
	        f.close();	  

	        for(String key : settings.keySet()) {
	        	try{
	        		if(listOfParameters.get(key) != null)
	        			settings.put(key, listOfParameters.get(key));
	        	} catch(Exception e) {
	    			clogger.err(AUTH, "load", e);}
	        }
			
			clogger.info(AUTH, "load", "Settings loaded");
		} catch(FileNotFoundException e) {
			clogger.warn(AUTH, "load", "No settings file found. Used default.");
		} catch(Exception e) {
			clogger.err(AUTH, "load", e);
		}
	}

	@SuppressWarnings("unchecked")
	public static void save() {
		try {
			//assemble json object			
	        JSONObject listOfSettings = new JSONObject();	        
	        
	        for(String key : settings.keySet()) 
	        	listOfSettings.put(key, settings.get(key));	                
			
	        //write json to file
        	FileWriter f;
	        try {
	        	f = new FileWriter(directory+"/"+filename);
	        } catch(FileNotFoundException e) {
	        	new File(directory).mkdir();
	        	f = new FileWriter(directory+"/"+filename);
	        }
	 
	        f.write(listOfSettings.toString());
	        f.flush();	        
	        f.close();	      
	        
			clogger.info(AUTH, "save", "Settings saved");
		} catch(Exception e) {
			clogger.err(AUTH, "save", e);
		}
	}
}