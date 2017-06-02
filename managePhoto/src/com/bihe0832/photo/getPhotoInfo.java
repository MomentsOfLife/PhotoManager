package com.bihe0832.photo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class getPhotoInfo {
	private static final int VERSION_CODE = 1;
	private static final String VERSION_NAME = "1.0.0";
	private static final String HELP_PAGE_GENERAL = "help.txt";
	private static final String VERSION_PAGE_GENERAL = "help_version.txt";	
	
	private static final int RESULT_FAIL_NOT_FOUND = -1;
	private static final int RESULT_FAIL_COPY_EXCEPTION = -2;
	private static final int RESULT_SUCC_NOINFO = -3;
	private static final int RESULT_SUCC_RENAME = 1;
	private static final int RESULT_SUCC_SAME = 2;
	
	
	private static final ArrayList<String> IMG_PREFIX_LIST = new ArrayList<String>(Arrays.asList("jpg","jpeg","png"));

	public static void main(String[] params) throws Exception {
        if ((params.length == 0)) {
            printUsage(HELP_PAGE_GENERAL);
            return;
        }
        if (params[0].toLowerCase().startsWith("--help")) {
            printUsage(HELP_PAGE_GENERAL);
            return;
        } else if (params[0].toLowerCase().startsWith("--version")) {
    		System.out.println(getPhotoInfo.class.toString() + " version " + VERSION_NAME + " (" + VERSION_CODE + ")\n");
    		printUsage(VERSION_PAGE_GENERAL);
            return;
        } else if (params[0].toLowerCase().startsWith("--detail")) {
        	if(params.length > 1){
        		showPhotoInfo(params[1],true);
        	}else{
        		printUsage(HELP_PAGE_GENERAL);
        	}
            return;
        }else if (params[0].toLowerCase().startsWith("--show")) {
        	if(params.length > 1){
        		showPhotoInfo(params[1],false);
        	}else{
        		printUsage(HELP_PAGE_GENERAL);
        	}
            return;
        }else if (params[0].toLowerCase().startsWith("--manage")) {
        	if(params.length > 2){
        		copyPhoto(params[1],params[2]);
        	}else{
        		printUsage(HELP_PAGE_GENERAL);
        	}
            return;
        } else{
        	printUsage(HELP_PAGE_GENERAL);
			return;
		}
    } 
	
	private static void showPhotoInfo(String filepath, boolean showDetail){
		File tempImg = new File(filepath);
		if(tempImg.exists()){
			try {
				PhotoInfo photoInfo = getPhotoInfoByPath(filepath,showDetail);
				System.out.println(
						"照片信息如下：\n"+  
						"******************************************************\n"+  
						"照片的当前名称: "+ tempImg.getName() + "\n"+  
						"照片的当前路径: "+ tempImg.getAbsolutePath() + "\n"+  
						"照片的空间大小: "+ tempImg.length() / 1024 + " KB" + "\n"+  
						"照片的像素大小: "+ photoInfo.getWidth() + " * " +  photoInfo.getHeight() + "\n"+  
						"拍摄时当地时间: "+ photoInfo.getDateTime() + "\n"+  
						"拍摄时标准时间: "+ photoInfo.getDateTimeStamp() + "\n"+  
						"拍摄时地点经纬: "+ photoInfo.getLongitude() + " ," +  photoInfo.getLatitude() + "\n"+  
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

	private static void copyPhoto(String sourceFolder,String targetFolder){
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
		int succNoInfo = 0;
		int succSame = 0;
		int failNotFound = 0;
		int failException = 0;
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
		    			case RESULT_SUCC_NOINFO:
		    				succNoInfo++;
		    				break;
		    			case RESULT_FAIL_NOT_FOUND:
		    				failNotFound++;
		    				break;
		    			case RESULT_FAIL_COPY_EXCEPTION:
		    				failException++;
		    				break;
	
	        		}
	        		if((succRename + succSame + succNoInfo + failNotFound + failException)%5 == 0){
	        			System.out.println("\n正在努力整理中，已完成: "+ sourceFolder + "目录下 "+ (succRename + succNoInfo + succSame + failNotFound + failException) +" 张照片\n");  
	        		}
	        	}
	        }else if(f.isDirectory()){
	        	copyPhoto(f.getAbsolutePath(), targetFolder);
	        }
	    }
	   
		System.out.println(	
				"******************************************************\n"+  
				"照片整理结果如下：共计从: "+ sourceFolder + " 整理了 "+ (succRename + succSame + succNoInfo + failNotFound + failException) +" 张照片到 "+ targetFolder +"，其中：\n"+  
				"整理成功: "+ (succRename + succSame + succNoInfo) + " 张，其中\n"+  
				"\t: "+ succSame  + " 张照片为已经存在的照片\n"+  
				"\t: "+ succRename  + " 张照片已经按时间重新命名\n"+  
				"\t: "+ succNoInfo + " 张照片保留原名称，原目录没有删除，需要手动整理\n"+  
				"整理失败: "+ (failNotFound + failException) + " 张，其中\n"+  
				"\t: "+ failNotFound + " 张照片没有找到\n"+  
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
				PhotoInfo photoInfo = getPhotoInfoByPath(sourcePath,false);
				String targetBasePath = "";
				if(photoInfo.getDateTime().length() > 0){
					targetBasePath = targetFolder + photoInfo.getDateTime();
					result = RESULT_SUCC_RENAME;
				}else{
					targetBasePath = targetFolder + sourceImg.getName().substring(0,sourceImg.getName().lastIndexOf("."));
					result = RESULT_SUCC_NOINFO;
				}
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
				Files.copy(sourceImg.toPath(),targetImg.toPath());
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
	
	private static void printUsage(String page) {
        try (BufferedReader in =
                new BufferedReader(
                        new InputStreamReader(
                        		getPhotoInfo.class.getResourceAsStream(page),
                                StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read " + page + " resource");
        }
    }
    
    private static PhotoInfo getPhotoInfoByPath(String imgFile, boolean showDetail) {
    	PhotoInfo photoInfo = new PhotoInfo();
        InputStream is = null;  
        try {  
            is = new FileInputStream(imgFile);  
        } catch (FileNotFoundException e1) {  
            e1.printStackTrace();  
            return photoInfo;
        }  
        
        try {  
        	Metadata metadata = ImageMetadataReader.readMetadata(is);  
            Iterable<Directory> iterable = metadata.getDirectories();  
            for (Iterator<Directory> iter = iterable.iterator();iter.hasNext();) {  
                Directory dr = iter.next();  
                Collection<Tag> tags = dr.getTags();  
                for (Tag tag : tags) {  
                	if(PhotoInfo.keyList.contains(tag.getTagName())){
                		photoInfo.valueList.put(tag.getTagName(), tag.getDescription());
                	}
                	if(showDetail){
                		System.out.println(tag.getTagName() +":" + tag.getDescription());
                	}
                }  
            }  
        } catch (ImageProcessingException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return photoInfo;
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

