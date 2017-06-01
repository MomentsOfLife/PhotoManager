## 简介

一款简单的照片整理的工具，主要用于查看照片的基本信息以及将多个不同目录的照片合并在一起。

## 使用事例

### 查看帮助

	➜  PhotoManager git:(master) ✗ java -jar ./managePhoto.jar
	usage:
	
		java -jar ./managePhoto.jar --<show|manage> [filePath]
		java -jar ./managePhoto.jar --version
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

	➜  PhotoManager git:(master) ✗ java -jar ./managePhoto.jar --manage /Volumes/Document/Document/temp/1/ /Volumes/Document/Document/temp/5/
	
	
	******************************************************
	照片整理已经开始……
		如果照片较多，持续时间较长，请耐心等待。
		整理过程中您可以在目标目录实时查看整理效果
	
	
	正在努力整理中，已完成: /Volumes/Document/Document/temp/1/目录下 5 张照片
	
	
	
	******************************************************
	照片整理结果如下：共计从: /Volumes/Document/Document/temp/1/整理了 8 张照片，其中：
	整理成功: 8 张
		: 0 张为已经存在的照片
		: 8 张已经按时间重新命名
		: 0 张保留了原名称
	整理失败: 0 张
		: 0 张照片没有找到
		: 0 张照片解析时间错误，需要手动整理
	******************************************************