#!/bin/sh
# author zixie
#/bin/bash ~/zixie/github/PhotoManager/shell/optimize.sh /Volumes/Document/Documents/temp/2/3/201802

function renamePhotos(){
	echo "----------- process photo start -----------"
	echo `pwd`"/ start to check:"
	for photsName in *.jpg;do echo "process $photsName" && mv  $photsName `echo $photsName |  tr 'a-z' 'A-Z'`;done
	for photsName in *.png;do echo "process $photsName" && mv  $photsName `echo $photsName |  tr 'a-z' 'A-Z'`;done
	for photsName in *.mp4;do echo "process $photsName" && mv  $photsName `echo $photsName |  tr 'a-z' 'A-Z'`;done
	for photsName in *.mov;do echo "process $photsName" && mv $photsName `echo $photsName |  tr 'a-z' 'A-Z'`;done
	for photsName in *.m4v;do echo "process $photsName" && mv $photsName `echo $photsName |  tr 'a-z' 'A-Z'`;done
	echo "----------- process photo end -----------"
}
export -f renamePhotos

cd $1
pwd
echo "============ zixe check photos start ======================"
renamePhotos
for file in ./*
do
	if [ -d "$1/$file" ]; then
        cd $1/$file
		renamePhotos
    fi
done
echo "----------- delete DS_Store-----------"
find $1 -name .DS_Store -delete
echo "----------- find bad name photo -----------"
find $1 -not -name "????-??-??_??-??-??*"
echo "============ zixe check photos finished ======================"
