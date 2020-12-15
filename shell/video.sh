#!/bin/sh
# author zixie
#/bin/bash ~/zixie/github/PhotoManager/shell/video.sh /Volumes/Document/Documents/temp/2/3/201802
function renameVideo(){
	photsName=$1
	echo $photsName
	# sourceTime=`~/zixie/lib/ffprobe -v quiet quiet ./$photsName -show_entries stream=index,codec_type:stream_tags=creation_time:format_tags=creation_time | grep creation_time`
	sourceTime=`~/zixie/lib/ffprobe -v quiet quiet -show_format ./$photsName | grep creation_time`
	# echo $sourceTime
	realTime=${sourceTime##*=}
	# echo $realTime
	realTimeWithoutExt=${realTime%%.*}
	# echo $realTimeWithoutExt
	realTimeFormatTime=${realTimeWithoutExt//:/-} 
	finalTime=${realTimeFormatTime/T/_} 
	echo $finalTime
	echo $finalTime.${photsName##*.}
	mv $photsName $finalTime.${photsName##*.}
}

cd $1
pwd
echo "============ zixe check video start ======================"
for photsName in *.MP4;
do 
	renameVideo $photsName
done
for photsName in *.MOV;
do 
	renameVideo $photsName
done
for photsName in *.M4V;
do 
	renameVideo $photsName
done
echo "----------- delete DS_Store-----------"
find $1 -name .DS_Store -delete
echo "----------- find bad name video -----------"
find $1 -not -name "????-??-??_??-??-??*"
echo "============ zixe check video finished ======================"