## 简介

### 背景

最近在将各种云、各种设备上的照片统一在一起，结果发现遇到几个问题：

1. 由于不停的来回拷贝过，导致部分照片重复了（不是相似，是完全相同）
2. 由于拍摄来源不同，导致照片的名称很混乱，不方便浏览
3. 由于拍摄来源不同，导致有可能是不同的照片但是文件名相同

基于上面的原因，想把所有的照片整理一遍发现工程量巨大，尤其是当照片有60+G的时候。本来想网上找个工具，但是不太放心，因此最终自己写了一个简单的照片整理工具。

### 产品功能

PhotoManager主要有以下功能：

1. 查看照片的基本信息，包括大小（占用空间、像素大小）、拍摄时间、地点、海拔、使用设备等
2. 整理不同目录的照片，将不同目录的照片逐个合并到相同目录。合并过程中会
	
	1. 将所有照片按照拍摄时间重新命名，对于无法获取到拍摄时间的照片，保留原名称，例如：

			2013-03-09_11-51-23.JPG
	
	2. 对于文件名相同的照片，通过MD5检查是否为相同照片，如果是，则删除重复照片；如果不是，则在文件名后面增加序号

			2013-03-09_11-51-23-[1].JPG
			2013-03-09_11-51-23-[2].JPG
	
	3. **如果照片可以被成功重新命名，在整理到目标目录后，原目录的照片会被删除；如果照片无法被重命名，照片还是会被整理，但是不会删除原目录照片**
	
### 待解决问题

通过上面的问题，初步解决了照片整理的最麻烦的问题，但是还有几个问题没有解决

1. 之前照片上云的时候，发现部分云盘私自压缩了照片，导致即使同一张照片，MD5并不相同，合并时都会被保留

2. 有时候会使用连拍等方式，导致会有很多很相似的照片，这部分照片其实可以选择后保留一张

3. 由于压缩、截图、保存等方式有几率会丢失照片的拍摄时间、地点等信息，这部分照片无法自动归类和整理

这几个问题都只能通过一些其他算法来对比优化，由于怕出现误删因此没有深入实践，这两个问题，本人打算在浏览照片的时候手动处理。

## 使用事例

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