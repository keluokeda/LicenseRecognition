package com.wintone.cipher;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class AESofC {
    static Class class$0;
    public String strkey;

    public interface AES extends Library {
        public static final AES INSTANCE;

        int DLL_Encrypt(char[] cArr, int i, char[] cArr2, char[] cArr3, int i2);

        int DLL_decrypt(char[] cArr, int i, char[] cArr2, char[] cArr3, int i2);

        static {
            String str = "AES";
            Class cls = AESofC.class$0;
            if (cls == null) {
                try {
                    cls = Class.forName("com.wintone.cipher.AESofC$AES");
                    AESofC.class$0 = cls;
                } catch (Throwable e) {
                    throw new NoClassDefFoundError(e.getMessage());
                }
            }
            INSTANCE = (AES) Native.loadLibrary(str, cls);
        }
    }

    public byte[] Encrypt(byte[] src) throws Exception {
        String strsrc = new String(src, "UTF-8");
        char[] key = new char[6];
        int outlen = AES.INSTANCE.DLL_Encrypt(strsrc.toCharArray(), strsrc.length(), key, new char[1], 0);
        char[] out = new char[(outlen + 1)];
        int rs = AES.INSTANCE.DLL_Encrypt(strsrc.toCharArray(), strsrc.length(), key, out, outlen);
        this.strkey = String.valueOf(key).substring(0, 5);
        if (rs != 0 && rs > 0) {
            return String.valueOf(out).substring(0, rs).getBytes("UTF-8");
        }
        out = null;
        return null;
    }

    public byte[] decrypt(byte[] enc) throws Exception {
        String strenc = new String(enc, "UTF-8");
        char[] chrT = new char[1];
        char[] key = this.strkey.toCharArray();
        int outlen = AES.INSTANCE.DLL_decrypt(strenc.toCharArray(), strenc.length(), key, chrT, 0);
        char[] out = new char[outlen];
        int rs = AES.INSTANCE.DLL_decrypt(strenc.toCharArray(), strenc.length(), key, out, outlen);
        if (rs != 0 && rs > 0) {
            return String.valueOf(out).substring(0, rs).getBytes("UTF-8");
        }
        out = null;
        return null;
    }

    public static void main(String[] args) throws Exception {
        String msg = "hepeixin";
        System.out.println(new StringBuffer("明文:").append(msg).toString());
        byte[] src = msg.getBytes();
        AESofC aes3 = new AESofC();
        byte[] enc = aes3.Encrypt(src);
        AESofC aes4 = new AESofC();
        aes4.strkey = aes3.strkey;
        System.out.println(new StringBuffer("解密:").append(new String(aes4.decrypt(enc))).toString());
    }
}
