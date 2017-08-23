package com.wintone.cipher;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Line {
    public String encodeStrFromArray(byte[] src) {
        return getOneLine(new BASE64Encoder().encode(src));
    }

    public byte[] decodeArrayFromStr(String enc) throws IOException {
        return new BASE64Decoder().decodeBuffer(enc);
    }

    public String encodeStrFromStr(String src) {
        return getOneLine(new BASE64Encoder().encode(src.getBytes()));
    }

    public String decodeStrFromStr(String enc) throws IOException {
        return new String(new BASE64Decoder().decodeBuffer(enc));
    }

    public static String GetBase64StrFromImage(String imagePath) {
        InputStream inputStream;
        IOException e;
        byte[] data = null;
        try {
            InputStream in = new FileInputStream(imagePath);
            try {
                data = new byte[in.available()];
                in.read(data);
                in.close();
                inputStream = in;
            } catch (IOException e2) {
                e = e2;
                inputStream = in;
                e.printStackTrace();
                return getOneLine(new BASE64Encoder().encode(data));
            }
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            return getOneLine(new BASE64Encoder().encode(data));
        }
        return getOneLine(new BASE64Encoder().encode(data));
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

    public static String getOneLine(String withr) {
        StringReader sr = new StringReader(withr);
        BufferedReader br = new BufferedReader(sr);
        StringBuffer temp = new StringBuffer();
        while (true) {
            try {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                temp.append(line);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        br.close();
        sr.close();
        return temp.toString();
    }

    public static void main(String[] args) throws Exception {
        byte[] temp8 = new Base64Line().decodeArrayFromStr("IlcF4O7D2++PwpWLXZdK1vugLz6x1U4zk0OJ8aSc/OfM9W8CCbh5kYEGTGF6B2+aqlTbx+L76fN5fdf8CGwILmJ7s6Qm6ZXZwgvyJ+Z0ttmXTd1FUynSWJlGHGxell");
        System.out.println();
    }
}
