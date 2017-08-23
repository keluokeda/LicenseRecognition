package com.wintone.cipher;

import com.wintone.Adaptor.CipherAdaptor;
import com.wintone.Adaptor.DataPackage;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class AESWithoutJCE {
    private byte[] iv = new byte[]{(byte) 56, (byte) 55, (byte) 54, (byte) 53, (byte) 52, (byte) 51, (byte) 50, (byte) 49, (byte) 56, (byte) 55, (byte) 54, (byte) 53, (byte) 52, (byte) 51, (byte) 50, (byte) 49};
    public String strkey;

    public byte[] Encrypt(byte[] src, byte[] keybytes) throws Exception {
        keybytes = to128bits(keybytes);
        byte[] encryptedContent = null;
        try {
            BufferedBlockCipher engine = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));
            engine.init(true, new ParametersWithIV(new KeyParameter(keybytes), this.iv));
            byte[] enc = new byte[engine.getOutputSize(src.length)];
            int size1 = engine.processBytes(src, 0, src.length, enc, 0);
            encryptedContent = new byte[(size1 + engine.doFinal(enc, size1))];
            System.arraycopy(enc, 0, encryptedContent, 0, encryptedContent.length);
            return encryptedContent;
        } catch (Exception ex) {
            ex.printStackTrace();
            return encryptedContent;
        }
    }

    public byte[] decrypt(byte[] enc, byte[] keybytes) throws Exception {
        keybytes = to128bits(keybytes);
        byte[] decryptedContent = null;
        try {
            BufferedBlockCipher engine = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));
            engine.init(false, new ParametersWithIV(new KeyParameter(keybytes), this.iv));
            byte[] dec = new byte[engine.getOutputSize(enc.length)];
            int size1 = engine.processBytes(enc, 0, enc.length, dec, 0);
            decryptedContent = new byte[(size1 + engine.doFinal(dec, size1))];
            System.arraycopy(dec, 0, decryptedContent, 0, decryptedContent.length);
            return decryptedContent;
        } catch (Exception ex) {
            ex.printStackTrace();
            return decryptedContent;
        }
    }

    private byte[] to128bits(byte[] key) {
        if (key.length >= 16) {
            return key;
        }
        int i;
        byte[] keys = new byte[16];
        for (i = 0; i < key.length; i++) {
            keys[i] = (byte) 0;
        }
        for (i = key.length; i < 16; i++) {
            keys[i] = (byte) 0;
        }
        return keys;
    }

    public static void main(String[] args) throws Exception {
        AESWithoutJCE aes = new AESWithoutJCE();
        MD5 md5 = new MD5();
        System.out.println(new StringBuffer("base64.encodeStrFromArray(enc)=============").append(new Base64().encodeStrFromArray(aes.Encrypt("data".getBytes("UTF-8"), "12".getBytes()))).toString());
        CipherAdaptor serverSentAdaptor = new CipherAdaptor();
        DataPackage serverdata = new DataPackage();
        try {
            serverSentAdaptor.sentEncodeOnce("", "12", "data", serverdata);
        } catch (Exception e) {
            System.out.println("en error");
            e.printStackTrace();
        }
        System.out.println(serverdata.paramdata);
    }
}
