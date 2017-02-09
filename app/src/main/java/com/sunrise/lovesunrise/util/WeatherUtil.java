package com.sunrise.lovesunrise.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;

/**
 * @PACKAGE com.sunrise.lovesunrise.util
 * @DESCRIPTION: TODO
 * @AUTHOR dongen_wang
 * @DATE 9/9/16 16:55
 * @VERSION V1.0
 */
public class WeatherUtil {

    public static byte[] decrypt(byte[] in){
        try{
            String a = "Crackers and the thief will suffer misfortune";
            byte[] ab = a.getBytes("UTF-8");
            int len = in.length;
            int m = 0;
            for(int i = 0;i<len;i++){
                in[i] = (byte)(in[i] ^ ab[m]);
                if(m == ab.length-1)
                    m = 0;
                else
                    m++;
            }
        }catch(Exception ex){

        }
        return in;
    }

    public static byte[] a(byte[] paramArrayOfByte)
    {
        if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0))
            return paramArrayOfByte;
        try
        {
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            ByteArrayInputStream bis = new ByteArrayInputStream(paramArrayOfByte);

            GZIPInputStream gzip = new GZIPInputStream(bis);
            byte[] arrayOfByte = new byte[256];
            while (true)
            {
                int i = gzip.read(arrayOfByte);
                if (i < 0)
                    return localByteArrayOutputStream.toByteArray();
                localByteArrayOutputStream.write(arrayOfByte, 0, i);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}
