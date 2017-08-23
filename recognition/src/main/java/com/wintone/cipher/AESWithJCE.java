package com.wintone.cipher;

import java.security.Key;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public class AESWithJCE {
    public static void main(String[] args) {
        byte[] keybytes = new byte[]{(byte) 49, (byte) 50, (byte) 49, (byte) 50, (byte) 49, (byte) 50, (byte) 49, (byte) 50, (byte) 49, (byte) 50, (byte) 49, (byte) 50, (byte) 49, (byte) 50, (byte) 49, (byte) 50};
        byte[] iv = new byte[]{(byte) 56, (byte) 55, (byte) 54, (byte) 53, (byte) 52, (byte) 51, (byte) 50, (byte) 49, (byte) 56, (byte) 55, (byte) 54, (byte) 53, (byte) 52, (byte) 51, (byte) 50, (byte) 49};
        String content = "12345678";
        System.out.println("Original content:");
        System.out.println(content);
        try {
            Security.addProvider(new BouncyCastleProvider());
            Key key = new SecretKeySpec(keybytes, "AES");
            Cipher in = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            in.init(1, key, new IvParameterSpec(iv));
            byte[] enc = in.doFinal(content.getBytes());
            System.out.println("Encrypted Content:");
            System.out.println(new String(Hex.encode(enc)));
            Cipher out = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            out.init(2, key, new IvParameterSpec(iv));
            byte[] dec = out.doFinal(enc);
            System.out.println("Decrypted Content:");
            System.out.println(new String(dec));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
