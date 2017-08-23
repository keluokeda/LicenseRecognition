package com.wintone.cipher;

import com.sun.crypto.provider.SunJCE;
import java.security.SecureRandom;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.util.encoders.Hex;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AESofSUN {
    private byte[] iv = new byte[]{(byte) 56, (byte) 55, (byte) 54, (byte) 53, (byte) 52, (byte) 51, (byte) 50, (byte) 49, (byte) 56, (byte) 55, (byte) 54, (byte) 53, (byte) 52, (byte) 51, (byte) 50, (byte) 49};
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
        String msg = "12345678";
        System.out.println(new StringBuffer("明文============").append(msg).toString());
        byte[] keys = new byte[]{(byte) 49, (byte) 50, (byte) 49, (byte) 50, (byte) 49, (byte) 50, (byte) 49, (byte) 50, (byte) 49, (byte) 50, (byte) 49, (byte) 50, (byte) 49, (byte) 50, (byte) 49, (byte) 50};
        byte[] enc = new AESofSUN().Encrypt(msg.getBytes(), keys);
        System.out.println(new String(Hex.encode(enc)));
        System.out.println(new StringBuffer("解密============").append(new String(new AESofSUN().decrypt(enc, keys))).toString());
    }
}
