package wffgerYu.photoU.common;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Kingsun on 14-6-22.
 */
public class FileUtils {
    public void move2E(String src) {
        try{
            //确定存储解密文件的目录存在
            File sd = Environment.getExternalStorageDirectory();
            String path = sd.getPath() + "/.photoU/e/";
            File test = new File(path);
            if (!test.exists()) test.mkdirs();  //创建多级目录记得使用mkdirs

            //
            File oldFile = new File(src);
            File newPath = new File(path);
            File newFile = new File(path + oldFile.getName());
            oldFile.renameTo(newFile);
        }catch (Exception e){e.printStackTrace();}
    }
    public void move2D(String src) {
        try{
            //确定存储解密文件的目录存在
            File sd = Environment.getExternalStorageDirectory();
            String path = sd.getPath() + "/.photoU/d/";
            File test = new File(path);
            if (!test.exists()) test.mkdirs();  //创建多级目录记得使用mkdirs

            //
            File oldFile = new File(src);
            File newPath = new File(path);
            File newFile = new File(path + oldFile.getName());
            oldFile.renameTo(newFile);
        }catch (Exception e){e.printStackTrace();}
    }
/*    public void save2SDCard(String content) {
        FileOutputStream fos = null;
        String hh = "/r";
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
            String fileName=formatter.format(new Date());
            fileName = "qb_" + fileName + ".txt";
            File sd = Environment.getExternalStorageDirectory();
            String path=sd.getPath()+"/QrBike";
            File test = new File(path);
            if (!test.exists()) test.mkdir();
            File file = new File(path,fileName);
            fos = new FileOutputStream(file,true);
            if(file.length()==0)
            {
                fos.write(content.getBytes());
            }else
            {
                content="\r"+content;
                fos.write(content.getBytes());
            }
        }catch (Exception e){e.printStackTrace();}
        finally {
            try{fos.close();}catch (IOException e){e.printStackTrace();}
        }
    }

    public static ArrayList ReadTxt(String path) {
        ArrayList content = new ArrayList();
        File file = new File(path);
        try {
            InputStream ips = new FileInputStream(file);
            if(ips!= null) {
                InputStreamReader ipsReader = new InputStreamReader(ips);
                BufferedReader bReader = new BufferedReader(ipsReader);
                String line;
                while((line=bReader.readLine()) != null) {
                    content.add(line);
                }
                ips.close();
            }
        }catch (IOException e){}
        return content;
    }*/
}
