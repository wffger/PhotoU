package wffgerYu.photoU.common;

import android.net.Uri;
import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by Kingsun on 14-6-22.
 */
public class FileUtils {
    /*参数：原文件路径
    * 功能：移动文件到e加密目录*/
    public void move2E(String src) {
        try{
            //确定存储解密文件的目录存在
            String destPath = Environment.getExternalStorageDirectory().getPath() + "/.photoU/e/";
            File test = new File(destPath);
            if (!test.exists()) test.mkdirs();  //创建多级目录记得使用mkdirs

            File sourceFile = new File(src);
            File whitherFile = new File(destPath+sourceFile.getName());
            copyFile(sourceFile, whitherFile);
            sourceFile.delete();

        }catch (Exception e){e.printStackTrace();}
    }
    public void move2D(String src) {
        try{
            //确定存储解密文件的目录存在
            String destPath = Environment.getExternalStorageDirectory().getPath() + "/.photoU/d/";
            File test = new File(destPath);
            if (!test.exists()) test.mkdirs();  //创建多级目录记得使用mkdirs

            File sourceFile = new File(src);
            File whitherFile = new File(destPath + sourceFile.getName());
            copyFile(sourceFile, whitherFile);
        }catch (Exception e){e.printStackTrace();}
    }

    /*This is Rigo Vides's Answer in the Stack Overflow*/
    public static void copyFile(File sourceFile, File whitherFile) throws IOException {
        if(!whitherFile.exists()) {
            whitherFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(whitherFile).getChannel();

            // previous code: destination.transferFrom(source, 0, source.size());
            // to avoid infinite loops, should be:
            long count = 0;
            long size = source.size();
            while((count += destination.transferFrom(source, count, size-count))<size);
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }
    }

    public void delete(String source) {
        File srcFile = new File(source);
        srcFile.delete();
    }

    public Uri getUri(String destination) {
        File file = new File(destination);
        return Uri.fromFile(file);
    }
}
