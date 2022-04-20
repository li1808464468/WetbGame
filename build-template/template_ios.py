import os, sys
import shutil
import glob

dstDir = '../build/JewelSliding'
srcDir = "iOS"

if (len(sys.argv) > 1):
	isSync = sys.argv[1]
	if (isSync == "sync"):
		dstDir = "iOS"
		srcDir = '../build/JewelSliding'

def listdir_nohidden(path):
	fileList = []
	for file in os.listdir(path):
		if not file.startswith('.') and os.path.isfile(os.path.join(path, file)):
			fileList.append(file)
		if os.path.isdir(os.path.join(path, file)):
			for item in listdir_nohidden(os.path.join(path, file)):
				fileList.append(os.path.join(file, item))

	return fileList

def copy_function(copyFile, newFile):
	if os.path.isdir(copyFile):
		if os.path.exists(newFile):
			shutil.rmtree(newFile)
		shutil.copytree(copyFile, newFile)

		
	if os.path.isfile(copyFile):
		if os.path.exists(newFile):
			os.remove(newFile)
		(filepath,tempfilename) = os.path.split(newFile)
		if not os.path.exists(filepath):
			os.makedirs(filepath)
		shutil.copyfile( copyFile, newFile)

def copyDir(sDir, dDir):
	fileList = listdir_nohidden(sDir);
	for file in fileList:
		copy_function(sDir + '/' + file, dDir + '/' + file)


#copy dir
dirs = [
	'Frameworks/HSAppFramework.framework',
	'Resource',
	'Unity-iPhone',
	'Libraries/Plugins/iOS/H5Adapter'
];
for d in dirs:
	copyDir(srcDir + '/' + d, dstDir + '/' + d);

#copy file
files = [
	'Classes/UnityAppController.h',
	'Classes/UnityAppController.mm',
	'Classes/UI/UnityViewControllerBase+iOS.mm',

	'Libraries/Plugins/iOS/AppsFlyerWrapper.mm',
	'Libraries/Plugins/iOS/AppsFlyerAppController.mm',
	'Libraries/Plugins/iOS/H5UnityPlugin.h',
	'Libraries/Plugins/iOS/H5UnityPlugin.mm',

	'info.plist',
	'LaunchScreen-iPhone.xib',
	'LaunchScreenBackground.png',
	'LaunchScreenBackground.jpg',
	'Unity-iPhone.xcodeproj',
	'Unity-iPhone.xcworkspace',
];
for f in files:
	copy_function(srcDir + '/' + f, dstDir + '/' + f);

# del files
delFiles = [
	'LaunchScreen-iPhoneLandscape.png',
	'LaunchScreen-iPhonePortrait.png'
];
for delf in delFiles:
	f = dstDir + '/' + delf;
	if os.path.exists(f):
		os.remove(f)


