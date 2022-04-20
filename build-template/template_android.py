import os, sys
import shutil
import glob

dstDir = '../build/Jewel'
srcDir = "android"

unityLibrary = 'unityLibrary/'


if (len(sys.argv) > 1):
    isSync = sys.argv[1]
    if (isSync == "sync"):
        dstDir = "android"
        srcDir = '../build/Jewel'

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
    # '.idea',
    'unityLibrary/src/main/java',
    'unityLibrary/src/main/res'
];
for d in dirs:
    copyDir(srcDir + '/' + d, dstDir + '/' + d);

#copy file
files = [
    
    unityLibrary + 'proguard-unity.txt',
    unityLibrary + 'build.gradle',
    unityLibrary + 'src/main/assets/alert.jst',
    unityLibrary + 'src/main/assets/config-d.ya',
    unityLibrary + 'src/main/assets/config-r.ya',
    unityLibrary + 'src/main/assets/config.jst',
    unityLibrary + 'src/main/assets/Autopilot_Config_v1.json',
    unityLibrary + 'src/main/AndroidManifest.xml',

    'launcher/src/main/AndroidManifest.xml',
    'launcher/build.gradle',

    'build.gradle',
];
for f in files:
    copy_function(srcDir + '/' + f, dstDir + '/' + f);

# del files
delFiles = [
    'libs/com.google.code.gson.gson-2.8.5.jar',
    'libs/androidx.lifecycle.lifecycle-common-2.1.0.jar',
    'libs/androidx.collection.collection-1.1.0.jar',
    'libs/androidx.arch.core.core-common-2.1.0.jar',
    'libs/androidx.annotation.annotation-1.1.0.jar',
    'libs/com.google.auto.value.auto-value-annotations-1.6.5.jar',
    'libs/com.google.guava.listenablefuture-1.0.jar',
    'libs/com.google.dagger.dagger-2.27.jar',
    'libs/androidx.room.room-common-2.2.5.jar',
    'libs/androidx.annotation.annotation-1.2.0.jar',
    'libs/androidx.concurrent.concurrent-futures-1.0.0.jar',
    'libs/com.mopub.volley.mopub-volley-2.1.0.jar',
    'src/main/res/mipmap-mdpi/ic_launcher_foreground.png',
    'src/main/res/mipmap-mdpi/ic_launcher_background.png',
    'src/main/res/mipmap-mdpi/app_icon.png',
    
    'libs/com.google.guava.guava-27.1-android.jar',
    

];
for delf in delFiles:
    f = dstDir + '/'  + unityLibrary + delf;
    if os.path.exists(f):
        os.remove(f)

# del folders
delFolders = [
	'src/main/res/mipmap-anydpi-v26'
];
for delFo in delFolders:
	fo = dstDir + '/' + delFo;
	if (os.path.exists(fo)):
		shutil.rmtree(fo)









