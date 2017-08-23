package com.wintone.cipher;

import com.sun.crypto.provider.SunJCE;
import java.security.SecureRandom;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AES {
    public String strkey;

    static {
        Security.addProvider(new SunJCE());
    }

    public byte[] Encrypt(byte[] src) throws Exception {
        SecretKey deskey = KeyGenerator.getInstance("AES").generateKey();
        this.strkey = new BASE64Encoder().encode(deskey.getEncoded());
        Cipher c = Cipher.getInstance("AES");
        c.init(1, deskey);
        return c.doFinal(src);
    }

    public byte[] decrypt(byte[] enc) throws Exception {
        SecretKey deskey1 = new SecretKeySpec(new BASE64Decoder().decodeBuffer(this.strkey), "AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(2, deskey1);
        return c.doFinal(enc);
    }

    public byte[] Encrypt(byte[] src, byte[] key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128, new SecureRandom(key));
        SecretKey keySpec = kgen.generateKey();
        this.strkey = new String(keySpec.getEncoded());
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(1, keySpec);
        return c.doFinal(src);
    }

    public byte[] decrypt(byte[] enc, byte[] key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128, new SecureRandom(key));
        SecretKey keySpec = kgen.generateKey();
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(2, keySpec);
        return c.doFinal(enc);
    }

    public static void main(String[] args) throws Exception {
        Base64 base64 = new Base64();
        String msg = "123";
        System.out.println(new StringBuffer("明文============").append(msg).toString());
        byte[] src = msg.getBytes();
        AES aes = new AES();
        byte[] keys = "123".getBytes();
        byte[] enc = aes.Encrypt(src, keys);
        System.out.println(new StringBuffer("密文============").append(new String(enc)).toString());
        System.out.println(new StringBuffer("密文 base64============").append(base64.encodeStrFromArray(enc)).toString());
        System.out.println(new StringBuffer("密钥============").append(aes.strkey).toString());
        System.out.println(new StringBuffer("解密============").append(new String(new AES().decrypt(enc, keys))).toString());
    }
}
