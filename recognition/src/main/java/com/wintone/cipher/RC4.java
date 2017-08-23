package com.wintone.cipher;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class RC4 {
    public String strkey;

    public byte[] Encrypt(byte[] src, byte[] key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("RC4");
        SecretKeySpec keySpec = new SecretKeySpec(key, "RC4");
        this.strkey = new String(keySpec.getEncoded());
        Cipher c = Cipher.getInstance("RC4");
        c.init(1, keySpec);
        return c.doFinal(src);
    }

    public byte[] decrypt(byte[] enc, byte[] key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("RC4");
        SecretKeySpec keySpec = new SecretKeySpec(key, "RC4");
        Cipher c = Cipher.getInstance("RC4");
        c.init(2, keySpec);
        return c.doFinal(enc);
    }

    public static void main(String[] args) throws Exception {
        Base64 base64 = new Base64();
        String msg = "abcdefg";
        System.out.println(new StringBuffer("明文============").append(msg).toString());
        byte[] src = msg.getBytes();
        RC4 aes = new RC4();
        byte[] keys = new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5};
        byte[] enc = aes.Encrypt(src, keys);
        System.out.println(new StringBuffer("密文============").append(new String(enc)).toString());
        System.out.println(new StringBuffer("密文 base64============").append(base64.encodeStrFromArray(enc)).toString());
        System.out.println(new StringBuffer("密钥============").append(aes.strkey).toString());
        System.out.println(new StringBuffer("解密============").append(new String(new RC4().decrypt(enc, keys))).toString());
    }
}
