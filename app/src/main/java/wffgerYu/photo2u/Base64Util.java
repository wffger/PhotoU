package wffgerYu.photo2u;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
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
public class Base64Util {

    public static final int XOR_CONST=0x99;
    public void Base64Encoder() {

    }

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

/*
    public static  void encryptImg(File src, File destination) throws Exception {
        ImageInputStream ips = new FileImageInputStream(src);
        ImageOutputStream ops = new FileImageOutputStream(destination);
        int read;
        while ((read=ips.read()) > -1) {
            ops.write(read^XOR_CONST);
        }
        ops.flush();
        ops.close();
        ips.close();
    }*/

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
