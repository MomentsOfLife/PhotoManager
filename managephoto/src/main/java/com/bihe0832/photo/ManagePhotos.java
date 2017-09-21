package com.bihe0832.photo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class ManagePhotos {
	
	private static final int RESULT_FAIL_NOT_FOUND = -1;
	private static final int RESULT_FAIL_COPY_EXCEPTION = -2;
	private static final int RESULT_FAIL_NOINFO = -3;
	private static final int RESULT_SUCC_RENAME = 1;
	private static final int RESULT_SUCC_SAME = 2;
	
	private static Path sLastPhotoSourcePath = null;
	private static Path sLastPhotoTargetPath = null;
	
	private final static String EXT_JPEG = "jpeg";
	private final static String EXT_JPG = "jpg";
	private final static String EXT_PNG = "png";
	private final static String EXT_TIFF = "tiff";
	private final static String EXT_BMP  = "bmp";
	private final static String EXT_GIF  = "gif";
    
	private static final ArrayList<String> IMG_PREFIX_LIST = new ArrayList<String>(
			Arrays.asList(EXT_JPEG,EXT_JPG,EXT_PNG,EXT_TIFF,EXT_BMP,EXT_GIF));

	
	public static void copyPhoto(String sourceFolder,String targetFolder){
		if(null != sourceFolder && sourceFolder.length() > 0){
			sourceFolder = sourceFolder + "/";
		}else{
			System.out.println("要整理的照片文件夹：" + sourceFolder + " 不存在"); 
			return;
		}
		
		if(null != targetFolder && targetFolder.length() > 0){
			targetFolder = targetFolder + "/";
		}else{
			System.out.println("整理后保存照片的文件夹：" + targetFolder + " 不存在"); 
			return;
		}
		
		int succRename = 0;
		int succSame = 0;
		int failNotFound = 0;
		int failException = 0;
		int failNoInfo = 0;
		System.out.println(	
				"\n\n******************************************************\n"+  
					"照片整理已经开始……\n"+ 
					"\t如果照片较多，持续时间较长，请耐心等待。\n"+ 
					"\t整理过程中您可以在目标目录实时查看整理效果");
		File[] listFiles = new File(sourceFolder).listFiles();
	    for(File f: listFiles){
	        if(f.isFile() ){
	        	if(IMG_PREFIX_LIST.contains(f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf(".") + 1).toLowerCase())){
	        		switch(copyPhotoOnce(f.getAbsolutePath(),targetFolder)){
		    			case RESULT_SUCC_RENAME:
		    				succRename++;
		    				f.delete();
		    				break;
		    			case RESULT_SUCC_SAME:
		    				succSame++;
		    				f.delete();
		    				break;
		    			case RESULT_FAIL_NOINFO:
		    				failNoInfo++;
		    				break;
		    			case RESULT_FAIL_NOT_FOUND:
		    				failNotFound++;
		    				break;
		    			case RESULT_FAIL_COPY_EXCEPTION:
		    				failException++;
		    				break;
	        		}
	        		if((succRename + succSame + failNoInfo + failNotFound + failException)%5 == 0){
	        			System.out.println("\n正在努力整理中，已完成: "+ sourceFolder + "目录下 "+ (succRename + failNoInfo + succSame + failNotFound + failException) +" 张照片\n");  
	        		}
	        	}
	        }else if(f.isDirectory()){
	        	copyPhoto(f.getAbsolutePath(), targetFolder);
	        }
	    }
	   
		System.out.println(	
				"******************************************************\n"+  
				"照片整理结果如下：共计从: "+ sourceFolder + " 整理了 "+ (succRename + succSame + failNoInfo + failNotFound + failException) +" 张照片到 "+ targetFolder +"，其中：\n"+  
				"整理成功: "+ (succRename + succSame + failNoInfo) + " 张，其中\n"+  
				"\t: "+ succSame  + " 张照片为已经存在的照片\n"+  
				"\t: "+ succRename  + " 张照片已经按时间重新命名\n"+  
				"整理失败: "+ (failNotFound + failException) + " 张，其中\n"+  
				"\t: "+ failNotFound + " 张照片没有找到\n"+  
				"\t: "+ failNoInfo + " 张照片解析时间错误放弃整理，需要手动整理\n"+  
				"\t: "+ failException + " 张照片解析时间错误放弃整理，需要手动整理\n"+  
				"******************************************************\n"
			); 
	}
	
	private static int copyPhotoOnce(String sourcePath,String targetFolder){
		int result = 0;
		File sourceImg = new File(sourcePath);
		File targetImg = null;
		String prefix= sourcePath.substring(sourcePath.lastIndexOf(".") + 1);
		int imgNum = 0;
		if(sourceImg.exists()){
			try {
				PhotoInfo photoInfo = PhotoInfo.getPhotoInfoByPath(sourcePath,false);
				if(photoInfo.getDateTime().length() > 0){
					String targetBasePath = targetFolder + photoInfo.getDateTime();
					targetBasePath = targetBasePath.replace(":","-").replace(" ","_");
					String targetPath = targetBasePath;
					do{
						targetPath = targetPath + "."+ prefix;
						targetImg = new File(targetPath);
						if(targetImg.exists()){
							String targetImgMd5= getFileMD5(targetImg);
							String sourceImgMd5= getFileMD5(sourceImg);
							if(targetImgMd5.equalsIgnoreCase(sourceImgMd5)){
								targetImg.delete();
								result = RESULT_SUCC_SAME;
							}
						}
						imgNum++;
						targetPath = targetBasePath +"-(" + imgNum+ ")";
					}while(targetImg.exists());
					sLastPhotoTargetPath = targetImg.toPath();
					sLastPhotoSourcePath = sourceImg.toPath();
					Files.copy(sourceImg.toPath(),targetImg.toPath());
					result = RESULT_SUCC_RENAME;
				}else{
					if(null != sLastPhotoTargetPath && sLastPhotoSourcePath.startsWith(sourcePath.substring(0,sourcePath.lastIndexOf("/")))){
						String backImgPath =
								sourcePath.substring(0,sourcePath.lastIndexOf("/")+1) +
								sLastPhotoSourcePath.toString().substring(sLastPhotoSourcePath.toString().lastIndexOf("/"),sLastPhotoSourcePath.toString().lastIndexOf("."))+ "_" +
								sLastPhotoTargetPath.toString().substring(sLastPhotoTargetPath.toString().lastIndexOf("/")+1);
						File tempImg = new File(backImgPath);
						if(!tempImg.exists()){
							Files.copy(sLastPhotoTargetPath,tempImg.toPath());
						}
					}
					result = RESULT_FAIL_NOINFO;
				}
				
			} catch (Exception e) {
				System.out.println("照片：" + sourcePath + " 整理失败" );
				e.printStackTrace();
				result = RESULT_FAIL_COPY_EXCEPTION;
			}
		}else{
			System.out.println("照片：" + sourcePath + " 不存在"); 
			result = RESULT_FAIL_NOT_FOUND;
		}
		return result;
	}
	
	private static String getFileMD5(File sourceFile) {
        String ret = "";
        if (sourceFile.exists() && sourceFile.length() > 0) {
            BufferedInputStream is = null;
            try {
                is = new BufferedInputStream(new FileInputStream(sourceFile));
                ret = getInputStreamMd5(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return ret;
    }
    
    private static String getInputStreamMd5(InputStream is) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            byte[] buffer = new byte[4196];
            int len;
            int readSize = 0;
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                if (len > 0) {
                    md5.update(buffer, 0, len);
                    readSize += len;
                }
            }
            if (readSize == 0)
                return "";
            byte[] md5Bytes = md5.digest();
            return bytesToHexString(md5Bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    private static String bytesToHexString(byte md5Bytes[]) {
        if (md5Bytes != null && md5Bytes.length == 16) {
        	StringBuffer hexString = new StringBuffer();
            String tempStr = "";
            for (int i=0; i<md5Bytes.length; i++){
                if(Integer.toHexString(0xFF & md5Bytes[i]).length() < 2){
                    tempStr = "0"+Integer.toHexString(0xFF & md5Bytes[i]);
                }else{
                    tempStr = Integer.toHexString(0xFF & md5Bytes[i]);
                }
                hexString.append(tempStr);
            }
            return hexString.toString();
        }else{
        	return "";
        }
    }

}
