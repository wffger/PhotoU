package wffgerYu.photo2u;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Kingsun on 14-6-17.
 */
public class PicActivity extends Activity{
    /** Called when the activity is first created. */
    private Button moveImg = null;
    private Button choseImg = null;
    private Button btn_encode = null;
    private Button btn_decode =null;
    private EditText editText1 = null;
    private EditText editText2 = null;
    private ImageView iv = null;

    public static Uri imgUri = null;    //使得可以传递给子线程函数
    public static String imgName;
    public static String imgPath;
    public static int threadCount = 0;
    public static final Object lock = new Object();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picactivity);

        iv = (ImageView)findViewById(R.id.iv);
        iv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);    //关闭硬件加速

        moveImg = (Button)findViewById(R.id.btn_moveImg);
        moveImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        choseImg = (Button)findViewById(R.id.btn_choseImg);
        choseImg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });

        editText1 = (EditText)findViewById(R.id.editText1);
        editText2 = (EditText)findViewById(R.id.editText2);
        
        btn_encode = (Button)findViewById(R.id.btn_encode);
        btn_encode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String plaintext = editText1.getText().toString();
                try
                {
                    String result = new CryptoTools().encode(plaintext);
                    editText2.setText(result);
//                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
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
//                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
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
                    String plaintext = editText1.getText().toString();
/*                    try
                    {
                        String result = new CryptoTools().encode(imgName);
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    }catch (Exception e){}*/
//                    threadCount++;
/*                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            FileHelper fileHelper = new FileHelper();
                            Looper.loop();
                        }
                    });
                    thread.start();*/
                    break;
                case 2:
                    Base64Util bsf = new Base64Util();
                    try
                    {
                        bsf.ShowImg(imgPath, iv);
                        bsf.encryptImg(imgPath);
                    }catch (IOException e) {e.printStackTrace();}
                    break;
            }
        }
/*        if (resultCode == Activity.RESULT_OK)
        {
            Uri uri = data.getData();
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            Base64Util bsf = new Base64Util();
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