package wffgerYu.photoU.ui;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import wffgerYu.photoU.bean.CryptoTools;
import wffgerYu.photoU.common.FileUtils;
import wffgerYu.photoU.common.ImgUtils;
import wffgerYu.photoU.R;

/**
 * Created by Kingsun on 14-6-17.
 */
public class PicActivity extends Activity{
    private Button decryptImg = null;
    private Button encryptImg = null;
    private Button btn_encode = null;
    private Button btn_decode =null;
    private EditText edt_input = null;
    private EditText edt_output = null;
    public static Uri imgUri = null;    //使得可以传递给子线程函数
    public static String imgName;
    public static String imgPath;

    private EditText edt_passwd = null;
    private EditText edt_passwd2 = null;
    private Button btn_passwd = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picactivity);

        decryptImg = (Button)findViewById(R.id.btn_decryptImg);
        encryptImg = (Button)findViewById(R.id.btn_encryptImg);
        btn_encode = (Button)findViewById(R.id.btn_encode);
        btn_decode = (Button)findViewById(R.id.btn_decode);
        edt_input = (EditText)findViewById(R.id.edt_input);
        edt_output = (EditText)findViewById(R.id.edt_output);
        edt_passwd = (EditText)findViewById(R.id.edt_passwd);
        edt_passwd2 = (EditText)findViewById(R.id.edt_passwd2);
        btn_passwd = (Button)findViewById(R.id.btn_passwd);
        
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

        encryptImg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        btn_encode.setOnClickListener(new OnClickListener() {   //加密文本
            @Override
            public void onClick(View view) {
                String inputText = edt_input.getText().toString();
                try
                {
                    Context context = PicActivity.this;
                    SharedPreferences photoUsp = context.getSharedPreferences("photoUsp", MODE_PRIVATE);
                    byte[] textKey = Base64.decode(photoUsp.getString("textKey", null), Base64.DEFAULT);
                    CryptoTools cryptoTools = new CryptoTools();
                    cryptoTools.setDESKey(textKey);
                    String result = new CryptoTools().encode(inputText).trim(); //用trim去除末尾换行符
                    edt_output.setText(result);
                    ClipboardManager cm = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                    cm.setText(result);
                }catch (Exception e){}
            }
        });
        btn_decode.setOnClickListener(new OnClickListener() {   //解密文本
            @Override
            public void onClick(View view) {
                String inputText = edt_input.getText().toString();
                try
                {
                    String result = new CryptoTools().decode(inputText);
                    edt_output.setText(result);
                }catch (Exception e){}
            }
        });

        btn_passwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwd = edt_passwd.getText().toString().trim();
                String passwd2 = edt_passwd2.getText().toString().trim();

                if(passwd.isEmpty() || passwd.getBytes().length<10 || !passwd.equals(passwd2))
                {
                    Toast toast = Toast.makeText(PicActivity.this, "请确保长度和匹配", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 10, 10);
                    toast.show();
                }
                else{
                    Context context = PicActivity.this;
                    SharedPreferences photoUsp = context.getSharedPreferences("photoUsp", MODE_PRIVATE);
                    SharedPreferences.Editor editor = photoUsp.edit();
                    editor.putString("textKey", Base64.encodeToString(passwd.getBytes(), Base64.DEFAULT));
                    editor.commit();
                }
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
    }
}