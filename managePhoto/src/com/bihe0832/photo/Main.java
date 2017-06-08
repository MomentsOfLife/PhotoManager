package com.bihe0832.photo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Main {
	private static final int VERSION_CODE = 4;
	private static final String VERSION_NAME = "1.2.0";
	private static final String HELP_PAGE_GENERAL = "help.txt";
	private static final String VERSION_PAGE_GENERAL = "help_version.txt";	

	public static void main(String[] params) throws Exception {
        if ((params.length == 0)) {
            printUsage(HELP_PAGE_GENERAL);
            return;
        }
        if (params[0].toLowerCase().startsWith("--help")) {
            printUsage(HELP_PAGE_GENERAL);
            return;
        } else if (params[0].toLowerCase().startsWith("--version")) {
    		System.out.println(Main.class.toString() + " version " + VERSION_NAME + " (" + VERSION_CODE + ")\n");
    		printUsage(VERSION_PAGE_GENERAL);
            return;
        } else if (params[0].toLowerCase().startsWith("--detail")) {
        	if(params.length > 1){
        		ManagePhoto.showPhotoInfo(params[1],true);
        	}else{
        		printUsage(HELP_PAGE_GENERAL);
        	}
            return;
        }else if (params[0].toLowerCase().startsWith("--show")) {
        	if(params.length > 1){
        		ManagePhoto.showPhotoInfo(params[1],false);
        	}else{
        		printUsage(HELP_PAGE_GENERAL);
        	}
            return;
        }else if (params[0].toLowerCase().startsWith("--mdtime")) {
        	if(params.length > 2){
        		String timeStr = params[2];
        		try{
        			SimpleDateFormat formatterafter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
    				Date date = formatterafter.parse(timeStr); 
    				SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
    		        String ctime = formatter.format(date);
    				ManagePhoto.modifyPhotoDateTime(params[1],ctime);
        		}catch(Exception e){
        			printBadTimeFormatTips();
        		}
        	}else{
        		printBadTimeFormatTips();
        	}
            return;
        }else if (params[0].toLowerCase().startsWith("--manage")) {
        	if(params.length > 2){
        		ManagePhotos.copyPhoto(params[1],params[2]);
        	}else{
        		printUsage(HELP_PAGE_GENERAL);
        	}
            return;
        } else{
        	printUsage(HELP_PAGE_GENERAL);
			return;
		}
    } 
	
	private static void printUsage(String page) {
        try (BufferedReader in =
                new BufferedReader(
                        new InputStreamReader(
                        		Main.class.getResourceAsStream(page),
                                StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read " + page + " resource");
        }
    }
	
	
	private static void printBadTimeFormatTips() {
		System.out.println("时间格式错误，时间格式必须为 yyyy-MM-dd-HH:mm:ss");
    }
}

