package com.wintone.cipher;

import java.security.MessageDigest;

public class MD5 {
    public byte[] doMD5(byte[] src) throws Exception {
        MessageDigest alga = MessageDigest.getInstance("MD5");
        alga.update(src);
        return alga.digest();
    }

    public boolean verify(byte[] src, byte[] digesta) throws Exception {
        MessageDigest alga = MessageDigest.getInstance("MD5");
        alga.update(src);
        return MessageDigest.isEqual(digesta, alga.digest());
    }

    public String transfer(byte[] md) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] str = new char[(j * 2)];
        int k = 0;
        for (byte byte0 : md) {
            int i = k + 1;
            str[k] = hexDigits[(byte0 >>> 4) & 15];
            k = i + 1;
            str[i] = hexDigits[byte0 & 15];
        }
        return new String(str).toUpperCase();
    }

    public static void main(String[] args) throws Exception {
        String msg = "何培新uiahfliajhf;lkjahglkajhgkaljhf;ooiwoiwfjoihjghkgjkdsjgjfoijuoihfoishjhoiu798yutygtyrrhyguyut";
        System.out.println(new StringBuffer("明文是：").append(msg).toString());
        byte[] src = msg.getBytes();
        MD5 md5 = new MD5();
        byte[] digesta = md5.doMD5(src);
        System.out.println(new StringBuffer("digesta[0]:").append(digesta[0]).toString());
        System.out.println(new StringBuffer("摘要是:").append(new String(digesta)).toString());
        System.out.println(new StringBuffer("转换后:").append(md5.transfer(digesta)).toString());
        System.out.println(new StringBuffer("比较后:").append(md5.verify(src, digesta)).toString());
    }
}
