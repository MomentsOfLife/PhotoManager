## 简介

### 产品功能

PhotoManager主要有以下功能：

1. 查看照片的基本信息，包括大小（占用空间、像素大小）、拍摄时间、地点、海拔、使用设备等
2. 整理不同目录的照片，将不同目录的照片逐个合并到相同目录。合并过程中会
	
	1. 将所有照片按照拍摄时间重新命名，对于无法获取到拍摄时间的照片，保留原名称，例如：

			2013-03-09_11-51-23.JPG
	
	2. 对于文件名相同的照片，通过MD5检查是否为相同照片，如果是，则删除重复照片；如果不是，则在文件名后面增加序号

			2013-03-09_11-51-23-[1].JPG
			2013-03-09_11-51-23-[2].JPG
	
	3. **对于可以被按照拍摄时间成功重新命名的照片，在整理到目标目录后，原目录的照片会被删除；对于无法被重命名的照片，不做整理，同时保留该文件夹内上一张可以被重命名的照片做对比参照。**

3. 对于由于压缩、截图、保存等方式丢失照片的拍摄时间、地点等信息的照片，提供添加拍摄时间的方法。添加以后就可以继续用工具自动整理。

### 待添加功能

1. 查看照片目前只能反映出经纬度，无法转换为地理位置，后续增加转换功能

## 具体实现

### 实现原理

目前基本上所有拍摄的照片，都会基于Exif(可交换图像文件格式常被简称为Exif,Exchangeable image file format)格式，这种格式是专门为数码相机的照片设定的，可以记录数码照片的属性信息和拍摄数据。因此我们就通过java程序获取照片文件的Exif中保存的数据来进行照片归类和整理。

### 兼容性问题

1. 不同的设备拍摄照片是保存的参数并不一样，目前发现拍摄时间兼容性问题比较严重，先后遇到了以下几种时间格式：

		Date/Time Original:2017:05:22 18:19:28
		
		Date/Time Original:04/12/2011 10:39:39

	目前代码中仅对这两种时间格式做了兼容。
	
## 相关文档

- 照片整理系列之整理及归档的总体方案： 

	[https://blog.bihe0832.com/photos.html](https://blog.bihe0832.com/photos.html) 

- 照片整理系列之单次整理流程： 

	[https://blog.bihe0832.com/photos-process.html](https://blog.bihe0832.com/photos-process.html) 

- 照片整理系列之基于命令行的照片整理及查看工具： 

	[https://blog.bihe0832.com/photomanage.html](https://blog.bihe0832.com/photomanage.html) 

- 照片整理系之视频归档整理方案：

	[https://blog.bihe0832.com/video-process.html](https://blog.bihe0832.com/video-process.html)

## 使用事例

### 构建Jar

在根目录运行构建命令即可：

	➜  PhotoManager git:(master) ✗ ./gradlew clean copyJar
	Java HotSpot(TM) 64-Bit Server VM warning: ignoring option MaxPermSize=1g; support was removed in 8.0
	Parallel execution with configuration on demand is an incubating feature.
	Using the 'clean' task in combination with parallel execution may lead to unexpected runtime behavior.
	:managephoto:clean
	:app:clean
	:clean
	:managephoto:compileJava
	注: /Volumes/Document/Document/github/PhotoManager/managephoto/src/main/java/mediautil/image/jpeg/BasicJpeg.java使用或覆盖了已过时的 API。
	注: 有关详细信息, 请使用 -Xlint:deprecation 重新编译。
	注: /Volumes/Document/Document/github/PhotoManager/managephoto/src/main/java/mediautil/image/jpeg/BasicJpeg.java使用了未经检查或不安全的操作。
	注: 有关详细信息, 请使用 -Xlint:unchecked 重新编译。
	:managephoto:processResources
	:managephoto:classes
	:managephoto:jar
	:managephoto:assemble
	:managephoto:compileTestJava UP-TO-DATE
	:managephoto:processTestResources UP-TO-DATE
	:managephoto:testClasses UP-TO-DATE
	:managephoto:test UP-TO-DATE
	:managephoto:check UP-TO-DATE
	:managephoto:build
	:managephoto:copyJar
	
	BUILD SUCCESSFUL
	
	Total time: 1.838 secs
	➜  PhotoManager git:(master) ✗

### 查看帮助

	➜  PhotoManager git:(master) ✗ java -jar ./managePhoto.jar
	usage:
	
		java -jar ./managePhoto.jar --<show|manage> [sourceFilePath] [targetFilePath]		java -jar ./managePhoto.jar --version
		java -jar ./managePhoto.jar --help
	
	such as:
	
		java -jar ./getImgInfo.jar --version
	
		java -jar ./managePhoto.jar --show ~/temp/IMG_3555.jpg

### 查看版本


	➜  PhotoManager git:(master) ✗ java -jar ./managePhoto.jar --version
	class com.bihe0832.photo.getPhotoInfo version 1.0.0 (1)
	
	homepage : https://github.com/bihe0832/PhotoManager
	blog : http://blog.bihe0832.com
	github : https://github.com/bihe0832
	
	
### 查看照片信息

	➜  PhotoManager git:(master) ✗ java -jar ./managePhoto.jar --show ~/temp/IMG_3555.jpg
	照片信息如下：
	******************************************************
	照片的当前名称: IMG_3555.jpg
	照片的当前路径: /Volumes/Document/Document/temp/IMG_3555.jpg
	照片的空间大小: 2020 KB
	照片的像素大小: 3264 pixels * 2448 pixels
	拍摄时当地时间: 2017:05:22 18:19:28
	拍摄时标准时间: 2017:05:22 10:19:28.00 UTC
	拍摄时地点经纬: 100° 16' 51.73" E ,28° 53' 55.01" N
	拍摄时地点海拔: Sea level:4519 metres
	拍摄时使用设备: Apple (iPhone 6)
	******************************************************
	
### 整理照片

	➜  PhotoManager git:(master) ✗ java -jar ./managePhoto.jar --manage /Volumes/Document/Document/temp/5/ /Volumes/Document/Document/temp/1/
	
	******************************************************
	照片整理已经开始……
		如果照片较多，持续时间较长，请耐心等待。
		整理过程中您可以在目标目录实时查看整理效果
	
	正在努力整理中，已完成: /Volumes/Document/Document/temp/5/ 目录下 5 张照片
	
	******************************************************
	照片整理结果如下：共计从: /Volumes/Document/Document/temp/5/ 整理了 8 张照片到/Volumes/Document/Document/temp/1/，其中：
	整理成功: 8 张
		: 0 张照片为已经存在的照片
		: 8 张照片已经按时间重新命名
	整理失败: 0 张
		: 0 张照片没有找到
		: 0 张照片解析时间错误放弃整理，需要手动整理
		: 0 张照片因为无法重命名放弃整理，需要手动整理
	******************************************************

### 添加拍摄时间


	➜  PhotoManager git:(master) ✗ java -jar ./managePhoto.jar --mdtime /Volumes/Document/Document/temp/5/IMG_3050.jpg 2017-06-08-18:30:31

	照片信息修改成功，修改后的照片为：/Volumes/Document/Document/temp/5/IMG_3050_2017-06-08_18-30-31.jpg
	
	照片信息如下：
	******************************************************
	照片的当前名称: IMG_3050_2017-06-08_18-30-31.jpg
	照片的当前路径: /Volumes/Document/Document/temp/5/IMG_3050_2017-06-08_18-30-31.jpg
	照片的空间大小: 2893 KB
	照片的像素大小: 3264 pixels * 2448 pixels
	拍摄时当地时间: 2017:06:08 18:30:31
	拍摄时标准时间: 2017:05:20 09:14:25.06 UTC
	拍摄时地点经纬: 29° 32' 48.93" N,103° 46' 6.6" E
	拍摄时地点海拔: Sea level:371 metres
	拍摄时使用设备: Apple (iPhone 6)
	******************************************************