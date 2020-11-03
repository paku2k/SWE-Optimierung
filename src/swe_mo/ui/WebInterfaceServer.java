package swe_mo.ui;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.awt.*;
import java.util.*;

import org.apache.commons.io.IOUtils;

import com.sun.net.httpserver.*;



public class WebInterfaceServer{
	final static String AUTH = "WIS";
	private HttpServer server;
	private boolean server_running;
	private boolean server_running_statechange;
	private static boolean exit = false;
	private int port;
	
	
	public WebInterfaceServer(int lowbd, int highbd){
		port = createRandomPort(lowbd, highbd);
		server_running = false;
		server_running_statechange = false;
	}
	
	public WebInterfaceServer(int port){
		this(port,port);
	}
	
	public WebInterfaceServer(){
		this(29170,29998);	//largest "unused" port interval
		
	}


        
    
    public void start() {
    	if(exit) return;
    	try {
    		if(!server_running) {
	    		server = HttpServer.create(new InetSocketAddress(port), 0);
	            server.createContext("/", WebInterfaceServer::handleRequest);
	            server.start(); 
	            server_running = true;
	            
	            clogger.info(AUTH, "start", "WIS started");
    		} else {
    			clogger.warn(AUTH, "start", "WIS already started"); 
    		}
		}catch(Exception e){
			clogger.err(AUTH, "start", e);
		} 	    	
    }
    
    public void open(){
    	try {
    		if(server_running) {
    			openWebpage(new URL("http://localhost:"+port)); 
    		} else {
    			clogger.warn(AUTH, "open", "WIS not running");
    		}
    	}catch(MalformedURLException e){
    		clogger.err(AUTH, "open", e);
    	}
    }
    
    public void stop(boolean permanently) {
    	try {
    		if(server_running) {
	            server.stop(1);  
	            server_running = false;
	            clogger.info(AUTH, "stop", "WIS stopped");
    		}else {
    			clogger.warn(AUTH, "stop", "WIS already stopped");
    		}
    		exit = permanently;
		}catch(Exception e){
			clogger.err(AUTH, "stop", e);
		} 	     	
    }
    
    public boolean running() {
    	return server_running;    	
    }
    
    public boolean statechange() {
    	boolean sc = (server_running != server_running_statechange);
    	server_running_statechange = server_running;
    	return sc;
    }

    public int getPort() {
    	return port;
    }
    
    
    
    
    
    
    
    
    
    private static void handleRequest(HttpExchange httpExchange) throws IOException {
		httpRequestParameters requestParameters = new httpRequestParameters();
    	try {
    		requestParameters.resolve(httpExchange);
			//WI_REQUEST event -> transmit request to main(?) and receive a response
			
			/* trigger http response */
			handleResponse(httpExchange,requestParameters); 
    	} catch(Exception e) {
    		clogger.err(AUTH, "handleRequest", e);
    	}
    }
    
      
    private static void handleResponse(HttpExchange httpExchange, httpRequestParameters requestParameters) {
    	handleResponse(httpExchange, requestParameters, "");
    }    
    private static void handleResponse(HttpExchange httpExchange, httpRequestParameters requestParameters, String cmdSendData) {
		//StringBuilder htmlBuilder = new StringBuilder();
		String responseString = "";

	    try {
	    	if(requestParameters.getPath().equals("/")) {
	    		//redirect to index    		
	    	    httpExchange.getResponseHeaders().set("Location", "/index.html");
		        sendHttpResponse(httpExchange, 301, null);
	    	    
	    	} else if(requestParameters.getPath().equals("/xhr_update")) {
		        String r = "NEW-DATA_"+createRandomPort(0,70000); 
	
	    	    httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
		        sendHttpResponse(httpExchange, 200, r.getBytes());
					
		        
	    	} else if(requestParameters.getPath().equals("/xhr_action")) {
		        String r = "DATA-RECEIVED_"+requestParameters.toString();
		        
	    	    httpExchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
		        sendHttpResponse(httpExchange, 200, r.getBytes());
				
	
	    	} else if(requestParameters.getPath().equals("/favicon.ico")){	
	    	    httpExchange.getResponseHeaders().set("Content-Type", "text/x-icon");
		        sendHttpResponse(httpExchange, 200, readFileBytes("/_wisFiles/img/icon.ico"));	
		        
			} else if(requestParameters.getPath().equals("/index.html")){
		    	responseString = readFileString("/_wisFiles/index.html");
		    	
		    	//fill server side scripts
		    	responseString.replaceAll("<?wis FILL_DATA ?>", ""); 
		    	
	    	    httpExchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
		        sendHttpResponse(httpExchange, 200, responseString.getBytes());
		        
	    	} else {
	    		//check dir _wisFiles for requested file
	    		String dir = "/_wisFiles";
	    		for(String p : requestParameters.getPathSep()) {
	    			dir += "/"+p;
	    		}
	    		if(getContentType(requestParameters.getFileType()) != null) {
	    			//append file and try to find it
	    			dir += "/"+requestParameters.getFileName()+"."+requestParameters.getFileType();

		    		try {
		    			byte[] bytes = readFileBytes(dir);
	    	    	    httpExchange.getResponseHeaders().set("Content-Type", getContentType(requestParameters.getFileType()));
	    	        	sendHttpResponse(httpExchange, 200, bytes);  
		    		} catch(Exception e) {
				        sendHttpResponse(httpExchange, 404, null);	    			
		    		}    
	    		} else {
	    			//try index.html otherwise 403 forbidden
	    			if(requestParameters.getFileType() != "") {
	    				dir += "/"+requestParameters.getFileName();
	    				if(requestParameters.getFileType() != "")
	    					dir += "."+requestParameters.getFileType();
	    			}
	    			if(dir.substring(dir.length()-1).equals(".")) dir = dir.substring(0,dir.length()-1); //foldername must not end with .
	    			dir += "/index.html";

		    		try {
		    			byte[] bytes = readFileBytes(dir);
	    	    	    httpExchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
	    	        	sendHttpResponse(httpExchange, 200, bytes);  
		    		} catch(Exception e) {
				        sendHttpResponse(httpExchange, 403, null);	    			
		    		}    
	    		}
	    				
	    	}
		} catch(Exception e) {
			clogger.err(AUTH, "handleResponse", e);
	        sendHttpResponse(httpExchange, 500, null);  
		}
    }
    
    private static void sendHttpResponse(HttpExchange httpExchange, int code, byte[] body) {
		try {
	        if(body!=null) {
	        	httpExchange.sendResponseHeaders(code, body.length);  
		        OutputStream os = httpExchange.getResponseBody();
		        os.write(body);
		        os.close();    		     
	        } else {
	        	httpExchange.sendResponseHeaders(code, 0);  	
	            httpExchange.close();	      
	        }
		} catch(Exception e) {
			clogger.err(AUTH, "sendHttpResponse", e);
		}
    	
    }
    

        
    
    /* read file as string with filepath as relative path to class */
    static String readFileString(String filepath) throws IOException{
    	InputStream in = WebInterfaceServer.class.getResourceAsStream(filepath);
    	try{
    		in.available(); 
    		String s = IOUtils.toString(in,StandardCharsets.UTF_8.name());
    		in.close();
    		return s;
    	} catch(Exception e) {
    		in.close();
    		throw new IOException("file not found ("+filepath+")");
    	}
    	
	}
    static byte[] readFileBytes(String filepath) throws IOException{
    	InputStream in = WebInterfaceServer.class.getResourceAsStream(filepath); 
    	try{
    		in.available(); 
    		byte[] b = IOUtils.toByteArray(in);
    		in.close();
    		return b;
    	} catch(Exception e) {
    		in.close();
    		throw new IOException("file not found ("+filepath+")");
    	}
	}
    
    
    
    /* return Content-Type for file ending. returns null if file not supported */
    static String getContentType(String filetype) {
    	if(filetype.equals("aac")) {
    		return "audio/aac";
    	} else if(filetype.equals("css")) {
    		return "text/css; charset=UTF-8";
    	} else if(filetype.equals("gif")) {
    		return "image/gif";
    	} else if(filetype.equals("html")) {
    		return "text/html; charset=UTF-8";
    	} else if(filetype.equals("htm")) {
        	return "text/html; charset=UTF-8";
    	} else if(filetype.equals("ico")) {
    		return "text/x-icon";
    	} else if(filetype.equals("jpg")) {
    		return "image/jpeg";
    	} else if(filetype.equals("jpeg")) {
    		return "image/jpeg";
    	} else if(filetype.equals("js")) {
    		return "text/js; charset=UTF-8";
    	} else if(filetype.equals("json")) {
    		return "text/json; charset=UTF-8";
    	} else if(filetype.equals("mp3")) {
    		return "audio/mpeg";
    	} else if(filetype.equals("mpeg")) {
    		return "video/mpeg";
    	} else if(filetype.equals("oga")) {
    		return "audio/ogg";
    	} else if(filetype.equals("ogv")) {
    		return "video/ogg";
    	} else if(filetype.equals("ogx")) {
    		return "application/ogg";
    	} else if(filetype.equals("opus")) {
    		return "audio/opus";
    	} else if(filetype.equals("otf")) {
    		return "font/otf";
    	} else if(filetype.equals("png")) {
    		return "image/png";
    	} else if(filetype.equals("pdf")) {
    		return "application/pdf";
    	} else if(filetype.equals("svg")) {
    		return "image/svg+xml";
    	} else if(filetype.equals("tif")) {
    		return "image/tiff";
    	} else if(filetype.equals("ttf")) {
    		return "font/ttf";
    	} else if(filetype.equals("txt")) {
    		return "text/plain; charset=UTF-8";
    	} else if(filetype.equals("wav")) {
    		return "audio/wav";
    	} else if(filetype.equals("woff")) {
    		return "font/woff";
    	} else if(filetype.equals("woff2")) {
    		return "font/woff2";
    	} else if(filetype.equals("xml")) {
    		return "text/xml; charset=UTF-8";
    	} else if(filetype.equals("zip")) {
    		return "application/zip";
    	}
    	return null;
    }
    
    
    
    
     
    
    /* create a random number for port in given and absolute boundaries */
    private static int createRandomPort(int lowerbound, int upperbound) {
    	try {
        	if(lowerbound < 1024) lowerbound = 1024;
        	if(upperbound > 65535) upperbound = 65535;
        	
        	if(lowerbound >= upperbound) return lowerbound;
        	
        	return new Random().nextInt(upperbound-lowerbound)+lowerbound;    	    		
    	} catch (Exception e) {	
    		clogger.err(AUTH, "createRandomPort", e);
    	}
    	return 8000;
    }
    
    /* functions for starting the webpage on default browser */
	private boolean openWebpage(URI uri) {
	  	try {
	  		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	  		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	  			desktop.browse(uri);
	  			return true;
	  		}
        } catch (Exception e) {
        	clogger.err(AUTH, "openWebpage_uri", e);
        }        
        return false;
    }
    private boolean openWebpage(URL url) {
        try {
            return openWebpage(url.toURI());
        } catch (URISyntaxException e) {
        	clogger.err(AUTH, "openWebpage_url", e);
        } catch (Exception e) {
        	clogger.err(AUTH, "openWebpage_url", e);        	
        }
        return false;
    }
    
    
    
    
    
    
    
    
    private static class httpRequestParameters{
    	
    	private String path;
    	private Queue<String> pathsep;
    	private String filename;
    	private String filetype;
    	private String method;
    	private String parstring;
    	private ArrayList<String[]> par;
    	
    	
    	public httpRequestParameters() {
    		path = "";
    		filename = "";
    		filetype = "";
    		method = "";
    		parstring = "";
    		par = new ArrayList<String[]>();
    		
    	}
    	
    	public String getPath() {
    		return path;
    	}    	
    	
    	@SuppressWarnings("unused")
		public String getParstring() {
    		return parstring;
    	}
    	
    	@SuppressWarnings("unused")
		public ArrayList<String[]> getPar(){
    		return par;
    	}
    	
    	@SuppressWarnings("unused")
		public String getParValue(String parameter_name) {
    		for(int i=0; i < par.size(); i++) {
    			if(par.get(i)[0].equals(parameter_name)) {
    				return par.get(i)[1];
    			}
    		}
    		return null;
    	}
    	
    	public Queue<String> getPathSep(){
    		return pathsep;
    	}
    	
    	public String getFileName() {
    		return filename;
    	}
    	public String getFileType() {
    		return filetype;
    	}
    	
    	
    	public void resolve(HttpExchange httpExchange) throws Exception {
    		URI uri = httpExchange.getRequestURI();
    		String method = httpExchange.getRequestMethod();
    		String parstring = "";
    		
    		//get path and parameterstring (parstring)
    		if("GET".equals(method)) { 
    			this.method = "GET";
    			
    			String uri_string = uri.toString();
    			
    		    String[] split = uri_string.split("\\?");
    			
    			path = split[0];	
    			if(split.length > 1)
    				parstring = split[1];
	
    		}else if("POST".equals(method)) {
    			this.method = "POST";

    		    StringBuilder sb = new StringBuilder();
                InputStream ios = httpExchange.getRequestBody();
                int i;
                while ((i = ios.read()) != -1) {
                    sb.append((char) i);
                }
    			
    		    path = uri.toString();
                parstring = sb.toString();
    		    
    		}else {
    			throw new Exception("HTTP request method not supported or unknown ("+method+")");
    			//HEAD, PUT, DELETE, CONNECT, OPTIONS, TRACE, PATCH not supported
    		}    		
    		parstring = URLDecoder.decode(parstring, StandardCharsets.UTF_8.name());    		
    		this.parstring = parstring;
    		
    		
    		//get seperated path
    		path = URLDecoder.decode(path, StandardCharsets.UTF_8.name());  
    		
    		pathsep = new LinkedList<String>(Arrays.asList(path.split("/")));
    		if(!pathsep.isEmpty()) pathsep.remove(); //first element is empty (the first char in path is /)
    		
    		
    		//if last pathsep is a file name, get filename and filetype
    		if(!path.substring(path.length()-1).equals("/")) {
	    		for(int i=0; i<pathsep.size()-1; i++) {
	    			pathsep.offer(pathsep.poll());
	    		}
	    		String f = pathsep.poll();
	    		int i = f.lastIndexOf(".");
	    		if(i>0 && i<f.length()) {
	    			filename = f.substring(0,i);
	    			filetype = f.substring(i+1);
	    		} else {
	    			pathsep.offer(f);
	    		}
    		}
    		
    		
    		//read pars in parstring
    		ArrayList<String[]> par = new ArrayList<String[]>();    		
			
    		for(String s : parstring.split("&")) {
    			String[] split = s.split("=",1);
				if(split.length > 1) {
	    			par.add(new String[]{split[0],split[1]});
				} else {
	    			par.add(new String[]{split[0],""});
				}
    		}
    		this.par = par;
    	}
    	
    	
    	@Override
    	public String toString() {
    		return "method=:"+method+" | path=:"+path+" | parstring=:"+parstring+"";
    	}
    
    }
}