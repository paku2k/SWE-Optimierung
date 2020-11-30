package swe_mo.solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class FileGenerator {
 
	private static String directory = "data/file";
	FileWriter f;

	

	public FileGenerator(String filename) throws IOException{
		filename+="_"+(new SimpleDateFormat("yyMMdd_HH-mm-ss").format(new java.util.Date()))+".csv";
		try {
		    f = new FileWriter(directory+"/"+filename);
		} catch(FileNotFoundException e) {
			new File(directory).mkdir();
			f = new FileWriter(directory+"/"+filename);
		} 	  
	}
	
	public FileGenerator(String filename, String header) throws IOException{
		this(filename);
		f.append(header+"\n");
	}	
	
	
	public void write(String text) throws IOException {
		f.append(text+"\n");
		
	}
	
	public void close() throws IOException {
		f.flush();
		f.close();
	}
	
}
