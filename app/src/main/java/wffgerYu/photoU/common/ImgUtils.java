package wffgerYu.photoU.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by Kingsun on 14-6-17.
 */
public class ImgUtils {

    public static final int XOR_CONST=0x99;
    public static Bitmap convert2Bitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;     //只获取图大小
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeFile(path, opts);   //f返回为空
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth, scaleHeight);
        opts.inSampleSize = (int)scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }

    public static  void encryptImg(String uri) throws IOException {
        //创建加密文件，扩展名为usnea
        String uri2 = uri+".usnea";
        File newfile = new File(uri2);
        try{
            newfile.createNewFile();
        }catch (IOException e){e.printStackTrace();}

        FileInputStream fis = new FileInputStream(uri);
        BufferedInputStream bis = new BufferedInputStream(fis);

        FileOutputStream fos = new FileOutputStream(uri2);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        int read;
        while ((read=bis.read()) > -1) {
            bos.write(read^XOR_CONST);
        }
        bos.close();
        fos.close();
        bis.close();
        fis.close();
    }
    public static  void decryptImg() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            String Epath = sd.getPath() + "/.photoU/e/";
            String Dpath = sd.getPath() + "/.photoU/d/";
            File test1 = new File(Epath);
            if (!test1.exists()) test1.mkdirs();  //创建多级目录记得使用mkdirs
            File test2 = new File(Dpath);
            if (!test2.exists()) test2.mkdirs();  //创建多级目录记得使用mkdirs
            File srcFolder = new File(Epath);
            File[] files = srcFolder.listFiles();
            for(File file : files)
            {
                if(file.isFile())
                {
                    encryptImg(file.getPath());
                    String currentFilePath =  file.getPath()+".usnea";
                    String newName = file.getName().substring(0, file.getName().length()-6);
                    file.delete();
                    File currentFile = new File(currentFilePath);
                    File newFile = new File(Dpath+newName);
                    currentFile.renameTo(newFile);
                }
            }

        }catch (IOException e){e.printStackTrace();}
    }

    public static void ShowImg(String uri, ImageView iv) throws IOException {
        FileInputStream fs = new FileInputStream(uri);
        BufferedInputStream bs = new BufferedInputStream(fs);
        Bitmap btp = BitmapFactory.decodeStream(bs);
        iv.setImageBitmap(btp);
        bs.close();
        fs.close();
        btp = null;
    }
}
