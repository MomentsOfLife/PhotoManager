#!/bin/sh
# author zixie
#/bin/bash ~/zixie/github/PhotoManager/shell/rename.sh /Volumes/Document/Documents/temp/2/3/201802

cd $1
pwd
echo "============ zixe check photos start ======================"
for file in ./*.JPG
do
	fineNameWithExt=${file##*/}
	fineName=${fineNameWithExt%%.*}
	realTime=`expr $fineName / 1000`
	finalName=`date -r $realTime "+%Y-%m-%d_%H-%M-%S"`
	mv $file $finalName.JPG
done