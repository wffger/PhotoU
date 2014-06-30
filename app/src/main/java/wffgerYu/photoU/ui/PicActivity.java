package wffgerYu.photoU.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import wffgerYu.photoU.bean.CryptoTools;
import wffgerYu.photoU.common.FileUtils;
import wffgerYu.photoU.common.ImgUtils;
import wffgerYu.photoU.R;

/**
 * Created by Kingsun on 14-6-17.
 */
public class PicActivity extends Activity{
    /** Called when the activity is first created. */
    private Button decryptImg = null;
    private Button encryptImg = null;
    private Button btn_encode = null;
    private Button btn_decode =null;
    private EditText editText1 = null;
    private EditText editText2 = null;
    public static Uri imgUri = null;    //使得可以传递给子线程函数
    public static String imgName;
    public static String imgPath;
    public static int threadCount = 0;
    public static final Object lock = new Object();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picactivity);

        decryptImg = (Button)findViewById(R.id.btn_decryptImg);
        decryptImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        ImgUtils deImg = new ImgUtils();
                        deImg.decryptImg();
                        Looper.loop();
                    }
                });
                thread.start();
            }
        });

        encryptImg = (Button)findViewById(R.id.btn_encryptImg);
        encryptImg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        editText1 = (EditText)findViewById(R.id.editText1);
        editText2 = (EditText)findViewById(R.id.edt_pswd2);
        
        btn_encode = (Button)findViewById(R.id.btn_encode);
        btn_encode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String plaintext = editText1.getText().toString();
                try
                {
                    String result = new CryptoTools().encode(plaintext);
                    editText2.setText(result);
                }catch (Exception e){}
            }
        });
        btn_decode = (Button)findViewById(R.id.btn_decode);
        btn_decode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String plaintext = editText1.getText().toString();
                try
                {
                    String result = new CryptoTools().decode(plaintext);
                    editText2.setText(result);
                }catch (Exception e){}
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(data != null && data.getData() != null)  //要判断是否返回，用户可能Back操作
        {
            imgUri = data.getData();
            Cursor cursor = this.getContentResolver().query(imgUri, null, null, null, null);
            cursor.moveToFirst();
            imgName = cursor.getString(0);
            imgPath = cursor.getString(1);
            cursor.close();     //要关闭

            switch (requestCode){
                case 1:
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            ImgUtils bsf = new ImgUtils();
                            try
                            {
                                bsf.encryptImg(imgPath);
                            }catch (IOException e) {e.printStackTrace();}
                            FileUtils fileUtils = new FileUtils();
                            fileUtils.move2E(imgPath+".usnea");
                            Looper.loop();
                        }
                    });
                    thread.start();
                    break;
            }
        }
/*        if (resultCode == Activity.RESULT_OK)
        {
            Uri uri = data.getData();
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            ImgUtils bsf = new ImgUtils();
            String imgName = cursor.getString(0);
            String imgPath = cursor.getString(1);
            try
            {
                bsf.ShowImg(imgPath, iv);
                bsf.encryptImg(imgPath);
            }catch (IOException e) {e.printStackTrace();}
*//*            String result = "";
            for (int i = 0; i < cursor.getColumnCount(); i++)
            {// 取得图片uri的列名和此列的详细信息
//                System.out.println(i + "-" + cursor.getColumnName(i) + "-" + cursor.getString(i));
                result=result+i + "-" + cursor.getColumnName(i) + "-" + cursor.getString(i) + "\n";
            }
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();*//*

        }*/
    }
}