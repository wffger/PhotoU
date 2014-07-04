package wffgerYu.photoU.common;

import android.renderscript.Int2;

import java.math.BigInteger;

/**
 * Created by Kingsun on 14-7-1.
 */
public class StringUtils {
    public String toHex(String arg) {
        return String.format("%040x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
    }


    public int hex2Int(String arg) {
        return Integer.parseInt(convertStringToHex(arg), 16);
    }

    public String convertStringToHex(String str){

        char[] chars = str.toCharArray();

        StringBuffer hex = new StringBuffer();
        for(int i = 0; i < chars.length; i++){
            hex.append(Integer.toHexString((int)chars[i]));
        }

        return hex.toString();
    }
}
