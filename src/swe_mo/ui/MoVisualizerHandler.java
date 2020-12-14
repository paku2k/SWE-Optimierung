package swe_mo.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import swe_mo.Main;

public class MoVisualizerHandler {
	
	private static String tempFileLocation = "";
	
	public static void start() throws IOException{
		Thread mvThread = new Thread(new Runnable() {	
			@Override
			public void run() {
				try {
					tempCopy("/mv","splash.png");
					ProcessBuilder p = new ProcessBuilder();
			        p.command(tempCopy("/mv","MoVisualizer_App.exe"));
			        p.start();
				} catch(Exception e) {
					clogger.err("MV","mvThread_start","Could not start MO Visualizer. ("+e.getMessage()+")");
				}
			}
		});
		mvThread.start();
	}
	
	public static void install() throws IOException {					
		Thread mvThread = new Thread(new Runnable() {	
			@Override
			public void run() {
				try {
					tempCopy("/mv","splash.png");
					String[] commands = {tempCopy("/mv","Elevate.exe"),tempCopy("/mv","MoVisualizer_Installer.exe")};
			        ProcessBuilder p = new ProcessBuilder(commands);
			        p.start();
				} catch(IOException e) {
					clogger.err("MV","mvThread_start","Could not start MO Visualizer. (Restart this application as admin)");
				} catch(Exception e) {
					clogger.err("MV","mvThread_install","Could not start MO Visualizer. ("+e.getMessage()+")");
				}
			}
		});
		mvThread.start();
	}
	
	
	public static String tempCopy(String relPath, String filename) throws IOException {
		InputStream is = Main.class.getResourceAsStream(relPath+"/"+filename);
		File exeFile;
		if(tempFileLocation.isEmpty()) {
			Path tempDir = Files.createTempDirectory("mOptimizer_tempfiles");
			tempFileLocation = tempDir.toString(); 
		}		
        exeFile = new File(tempFileLocation+"/"+filename); 
        FileOutputStream fos = new FileOutputStream(exeFile);
        byte bytes[] = new byte[1000];
        int k = 0;
        while((k = is.read(bytes)) != -1){
            fos.write(bytes, 0, k);
        }
        fos.close();
        exeFile.deleteOnExit();
		return exeFile.toString();
	}
}
