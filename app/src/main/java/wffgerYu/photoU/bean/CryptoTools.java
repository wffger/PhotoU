package wffgerYu.photoU.bean;
import android.content.SharedPreferences;
import android.util.Base64;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by Kingsun on 14-6-22.
 */
public class CryptoTools {
    protected static byte[] DESKey = "PhotoUdefaultKey".getBytes(); //密钥，不能太短
    protected static byte[] DESIv = "PhotoUIv".getBytes();  //向量,必须为8byte

    static AlgorithmParameterSpec iv = null;    //加密算法参数接口
    private static Key key = null;

    public CryptoTools(){
        try{
            DESKeySpec keySpec = new DESKeySpec(DESKey);    //密钥参数设置
            iv = new IvParameterSpec(DESIv);    //向量设置
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  //密钥工厂
            key = keyFactory.generateSecret(keySpec);   //获得密钥对象
        }catch (Exception e){e.printStackTrace();}
    }

    public void setDESKey(byte[] textKey){
        this.DESKey=textKey;
    }

    public String encode(String data) {
        try {
            Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");   //获得加密对象Cipher
            enCipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] pasByte = enCipher.doFinal(data.getBytes("utf-8"));
            return Base64.encodeToString(pasByte,Base64.DEFAULT);
        }catch (Exception e){e.printStackTrace();}
        return "Wrong";
    }

    public String decode(String data) throws Exception {
        Cipher deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        deCipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] pasByte = deCipher.doFinal(Base64.decode(data, Base64.DEFAULT));
        return new String(pasByte, "UTF-8");
    }
}
