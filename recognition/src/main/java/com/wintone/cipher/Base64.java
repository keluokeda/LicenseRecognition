package com.wintone.cipher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64 {
    public String encodeStrFromArray(byte[] src) {
        return new BASE64Encoder().encode(src);
    }

    public byte[] decodeArrayFromStr(String enc) throws IOException {
        return new BASE64Decoder().decodeBuffer(enc);
    }

    public String encodeStrFromStr(String src) {
        return new BASE64Encoder().encode(src.getBytes());
    }

    public String decodeStrFromStr(String enc) throws IOException {
        return new String(new BASE64Decoder().decodeBuffer(enc));
    }

    public static String GetBase64StrFromImage(String imagePath) {
        IOException e;
        byte[] data = null;
        try {
            InputStream in = new FileInputStream(imagePath);
            InputStream inputStream;
            try {
                data = new byte[in.available()];
                in.read(data);
                in.close();
                inputStream = in;
            } catch (IOException e2) {
                e = e2;
                inputStream = in;
                e.printStackTrace();
                return new BASE64Encoder().encode(data);
            }
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            return new BASE64Encoder().encode(data);
        }
        return new BASE64Encoder().encode(data);
    }

    public static boolean GenerateImageFromBase64Str(String imgBase64Str, String objImgStr) {
        if (imgBase64Str == null) {
            return false;
        }
        try {
            byte[] b = new BASE64Decoder().decodeBuffer(imgBase64Str);
            for (int i = 0; i < b.length; i++) {
                if (b[i] < (byte) 0) {
                    b[i] = (byte) (b[i] + 256);
                }
            }
            OutputStream out = new FileOutputStream(objImgStr);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        Base64 base64 = new Base64();
        System.out.println(base64.decodeStrFromStr(base64.encodeStrFromStr("何培新")));
    }
}
