package com.bihe0832.photo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import mediautil.image.jpeg.Entry;
import mediautil.image.jpeg.Exif;
import mediautil.image.jpeg.LLJTran;

public class ManagePhoto {

	public static void showPhotoInfo(String filepath, boolean showDetail){
		File tempImg = new File(filepath);
		if(tempImg.exists()){
			try {
				PhotoInfo photoInfo = PhotoInfo.getPhotoInfoByPath(filepath,showDetail);
				System.out.println(
						"\n照片信息如下：\n"+  
						"******************************************************\n"+  
						"照片的当前名称: "+ tempImg.getName() + "\n"+  
						"照片的当前路径: "+ tempImg.getAbsolutePath() + "\n"+  
						"照片的空间大小: "+ tempImg.length() / 1024 + " KB" + "\n"+  
						"照片的像素大小: "+ photoInfo.getWidth() + " * " +  photoInfo.getHeight() + "\n"+  
						"拍摄时当地时间: "+ photoInfo.getDateTime() + "\n"+  
						"拍摄时标准时间: "+ photoInfo.getDateTimeStamp() + "\n"+  
						"拍摄时地点经纬: "+ photoInfo.getLatitude() + "," +  photoInfo.getLongitude() + "\n"+  
						"拍摄时地点海拔: "+ photoInfo.getAltitude() + "\n" +
						"拍摄时使用设备: "+ photoInfo.getMake() + " (" +photoInfo.getModel() + ")\n"+
						"******************************************************\n"
					); 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("照片：" + filepath + " 不存在"); 
		}
	}
	
	public static void modifyPhotoDateTime(String sourcePath,String dateString){
		try{
			InputStream fip = new BufferedInputStream(new FileInputStream(sourcePath)); 
	        LLJTran llj = new LLJTran(fip);
            llj.read(LLJTran.READ_INFO, true);
	        Exif exif = (Exif) llj.getImageInfo();      
	        
			Entry e = new Entry(Exif.ASCII);
			e.setValue(0, dateString);
			exif.setTagValue(Exif.DATETIME, -1, e, true);

			llj.refreshAppx(); 
			String targetFile = 
					sourcePath.substring(0,sourcePath.lastIndexOf("/")) +
					sourcePath.toString().substring(sourcePath.toString().lastIndexOf("/"),sourcePath.toString().lastIndexOf("."))+ "_" + dateString +
					sourcePath.toString().substring(sourcePath.toString().lastIndexOf("."),sourcePath.length());
			targetFile = targetFile.replace(":","-").replace(" ","_");
			OutputStream out = new BufferedOutputStream(new FileOutputStream(targetFile));
			
			llj.xferInfo(null, out, LLJTran.REPLACE, LLJTran.REPLACE);
	        fip.close();
	        out.close();
	        llj.freeMemory();
	        System.out.println("\n照片信息修改成功，修改后的照片为：" + targetFile); 
	        showPhotoInfo(targetFile, false);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("照片：" + sourcePath + " 修改失败"); 
		}

        
    }
}
