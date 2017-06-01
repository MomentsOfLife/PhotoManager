### 查看帮助

	➜  getPNGInfo git:(master) ✗ java -jar ./getImgInfo.jar
	usage:
	
		java -jar ./getImgInfo.jar <command> [filePath]
		java -jar ./getImgInfo.jar --version
		java -jar ./getImgInfo.jar --help
	
	such as:
	
	
		java -jar ./getImgInfo.jar ./test.png
		java -jar ./getImgInfo.jar --version
		java -jar ./getImgInfo.jar --help
	
	after check,the result will be a string json such as:
	
		 {"ret":0,"msg":"图片是否有渐变: true ,图片尺寸为(宽*高): 432 * 168 , 图片类型: 6","hasAlpha":true,"type":6,"width":432,"height":168}
			
			
### 查看版本

	➜  getPNGInfo git:(master) ✗ java -jar ./getImgInfo.jar --version
	class com.bihe0832.png.getImgInfo version 1.0.1 (2)
	
	homepage : https://github.com/bihe0832/getImageInfo
	blog : http://blog.bihe0832.com
	github : https://github.com/bihe0832
		
### 查看图片信息

	➜  getPNGInfo git:(master) ✗ java -jar ./getImgInfo.jar head.jpg
	{"ret":0,"msg":"图片是否有渐变: false ,图片尺寸为(宽*高): 344 * 344 , 图片大小: 7 KB,图片类型: 5","hasAlpha":false,"type":5,"width":344,"height":344,"size":7}
	
	➜  getPNGInfo git:(master) ✗ java -jar ./getImgInfo.jar head.png
	{"ret":0,"msg":"图片是否有渐变: true ,图片尺寸为(宽*高): 344 * 344 , 图片大小: 33 KB,图片类型: 6","hasAlpha":true,"type":6,"width":344,"height":344,"size":33}
		
	
## 返回参数说明

### hasAlpha

图片是否有透明度，是否有渐变效果

### width & height

图片的宽和高的像素数值

### size

图片的大小

### type

图片的编码方案，具体值参考官网介绍：[https://docs.oracle.com/javase/7/docs/api/java/awt/image/BufferedImage.html](https://docs.oracle.com/javase/7/docs/api/java/awt/image/BufferedImage.html)，为了方便查看，个人用google翻译做了简单整理。

- `TYPE_CUSTOM` = 0

	图像类型无法识别，因此它必须是自定义图像。
	
- `TYPE_INT_RGB` = 1

	表示具有8位RGB颜色分量的整数像素的图像，他没有alpha值。

- `TYPE_INT_ARGB` = 2

	表示具有8位RGBA颜色成分的整数像素的图像，她有alpha值。
	
- `TYPE_INT_ARGB_PRE` = 3

	表示具有8位RGBA颜色成分的整数像素的图像。
	
- `TYPE_INT_BGR` = 4

	表示具有8位RGB颜色分量的图像，对应于Windows或Solaris样式的BGR颜色模型，蓝色，绿色和红色颜色打包成整数像素。
		
- `TYPE_3BYTE_BGR` = 5

	表示具有8位RGB颜色分量的图像，对应于Windows风格的BGR颜色模型，蓝色，绿色和红色以3个字节存储。

- `TYPE_4BYTE_ABGR` = 6

	表示具有蓝色，绿色和红色的8位RGBA颜色分量的图像，存储在3个字节和1个字节的alpha中。

- `TYPE_4BYTE_ABGR_PRE` = 7

	表示具有蓝色，绿色和红色的8位RGBA颜色分量的图像，存储在3个字节和1个字节的alpha中。

- `TYPE_USHORT_565_RGB` = 8

	表示具有5-6-5 RGB颜色分量（5位红色，6位绿色，5位蓝色），无alpha的图像。

- `TYPE_USHORT_555_RGB` = 9

	表示5-5-5 RGB颜色分量（5位红色，5位绿色，5位蓝色）的图像，无alpha。

- `TYPE_BYTE_GRAY` = 10

	表示无符号字节灰度图像，无索引。

- `TYPE_USHORT_GRAY` = 11

	表示无符号短灰度图像，非索引）。
	
- `TYPE_BYTE_BINARY` = 12 

	表示不透明的字节打包的1,2或4位图像。
	
- `TYPE_BYTE_INDEXED` = 13

	表示索引的字节图像。

