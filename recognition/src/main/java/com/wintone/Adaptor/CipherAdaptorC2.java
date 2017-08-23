package com.wintone.Adaptor;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.wintone.cipher.AES;
import com.wintone.cipher.Base64;
import java.io.FileWriter;
import java.io.IOException;

public class CipherAdaptorC2 {
    static Class class$0;

    public interface PKI extends Library {
        public static final PKI INSTANCE;

        int DLL_getUserName(char[] cArr, int i, char[] cArr2, int i2);

        int DLL_receiveDecode(char[] cArr, int i, char[] cArr2, int i2, char[] cArr3, int i3, char[] cArr4, int i4, char[] cArr5, int i5);

        int DLL_sentEncode(char[] cArr, int i, char[] cArr2, int i2, char[] cArr3, int i3, char[] cArr4, int i4, char[] cArr5, int i5, char[] cArr6, int i6);

        int DLL_setRecgnPlainParam(char[] cArr, int i, char[] cArr2, int i2, char[] cArr3, int i3, char[] cArr4, int i4, char[] cArr5, int i5);

        int DLL_setVerifyPlainParam(char[] cArr, int i, char[] cArr2, int i2, char[] cArr3, int i3, char[] cArr4, int i4);

        static {
            String str = "PKI";
            Class cls = CipherAdaptorC2.class$0;
            if (cls == null) {
                try {
                    cls = Class.forName("com.wintone.Adaptor.CipherAdaptorC2$PKI");
                    CipherAdaptorC2.class$0 = cls;
                } catch (Throwable e) {
                    throw new NoClassDefFoundError(e.getMessage());
                }
            }
            INSTANCE = (PKI) Native.loadLibrary(str, cls);
        }
    }

    public String setRecgnPlainParam(String imgfilename, String type, String option, String password) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>in setRecgnPlainParam");
        if (option == null || option == "") {
            option = "";
        }
        int outlen = PKI.INSTANCE.DLL_setRecgnPlainParam(imgfilename.toCharArray(), imgfilename.length(), type.toCharArray(), type.length(), option.toCharArray(), option.length(), password.toCharArray(), password.length(), new char[1], 0);
        char[] chrT = null;
        char[] chr = new char[outlen];
        int rs = PKI.INSTANCE.DLL_setRecgnPlainParam(imgfilename.toCharArray(), imgfilename.length(), type.toCharArray(), type.length(), option.toCharArray(), option.length(), password.toCharArray(), password.length(), chr, outlen);
        if (rs == 0 || rs <= 0) {
            chr = null;
            return null;
        }
        String result = String.valueOf(chr).substring(0, rs);
        chr = null;
        return result;
    }

    public RecgnParam getRecgnPlainParam(String strsrc) {
        String[] array = strsrc.split("==##");
        RecgnParam recgnParam = new RecgnParam();
        recgnParam.imgBase64Str = array[0];
        recgnParam.type = array[1];
        recgnParam.option = array[2];
        recgnParam.password = array[3];
        return recgnParam;
    }

    public String setVerifyPlainParam(String datasource, String param, String password) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>in setRecgnPlainParam");
        int outlen = PKI.INSTANCE.DLL_setVerifyPlainParam(datasource.toCharArray(), datasource.length(), param.toCharArray(), param.length(), password.toCharArray(), password.length(), new char[1], 0);
        char[] chrT = null;
        char[] chr = new char[outlen];
        int rs = PKI.INSTANCE.DLL_setVerifyPlainParam(datasource.toCharArray(), datasource.length(), param.toCharArray(), param.length(), password.toCharArray(), password.length(), chr, outlen);
        if (rs == 0 || rs <= 0) {
            chr = null;
            return null;
        }
        String result = String.valueOf(chr).substring(0, rs);
        chr = null;
        return result;
    }

    public VerifyParam getVerifyPlainParam(String strsrc) {
        String[] array = strsrc.split("==##");
        VerifyParam Param = new VerifyParam();
        Param.datasource = array[0];
        Param.param = array[1];
        Param.password = array[2];
        return Param;
    }

    public String getUserName(String username) throws IOException {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>in getUserName");
        int outlen = PKI.INSTANCE.DLL_getUserName(username.toCharArray(), username.length(), new char[1], 0);
        char[] chrT = null;
        char[] chr = new char[outlen];
        int rs = PKI.INSTANCE.DLL_getUserName(username.toCharArray(), username.length(), chr, outlen);
        if (rs == 0 || rs <= 0) {
            chr = null;
            return null;
        }
        String result = String.valueOf(chr).substring(0, rs);
        chr = null;
        return result;
    }

    public void writeFile(String fileName, String content) {
        try {
            FileWriter writer = new FileWriter(fileName, false);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sentEncode(String username, String mycert, String certkey, String otherpubkey, String strsrc, DataPackage data) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>in sentEncode");
        System.out.println(new StringBuffer("strsrc:").append(strsrc).toString());
        String type = otherpubkey.replace("pub.ppk", "cert.crt");
        int outlen = PKI.INSTANCE.DLL_sentEncode(username.toCharArray(), username.length(), mycert.toCharArray(), mycert.length(), certkey.toCharArray(), certkey.length(), type.toCharArray(), type.length(), strsrc.toCharArray(), strsrc.length(), new char[1], 0);
        char[] chrT = null;
        char[] chr = new char[outlen];
        int rs = PKI.INSTANCE.DLL_sentEncode(username.toCharArray(), username.length(), mycert.toCharArray(), mycert.length(), certkey.toCharArray(), certkey.length(), type.toCharArray(), type.length(), strsrc.toCharArray(), strsrc.length(), chr, outlen);
        if (rs == 0 || rs <= 0) {
            chr = null;
            return;
        }
        String[] str = String.valueOf(chr).substring(0, rs).split("@@@@");
        data.username = str[0];
        data.signdata = str[1];
        data.paramdata = str[2];
        chr = null;
    }

    public boolean receiveDecode(String username, String mycert, String certkey, String otherpubkey, String paramdata, String signdata, DataPlain param) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>in receiveDecode");
        String type = otherpubkey.replace("pub.ppk", "cert.crt");
        String strreceive = new StringBuffer(String.valueOf(username)).append("@@@@").append(signdata).append("@@@@").append(paramdata).toString();
        int outlen = PKI.INSTANCE.DLL_receiveDecode(strreceive.toCharArray(), strreceive.length(), mycert.toCharArray(), mycert.length(), certkey.toCharArray(), certkey.length(), type.toCharArray(), type.length(), new char[1], 0);
        char[] chrT = null;
        char[] chr = new char[outlen];
        int rs = PKI.INSTANCE.DLL_receiveDecode(strreceive.toCharArray(), strreceive.length(), mycert.toCharArray(), mycert.length(), certkey.toCharArray(), certkey.length(), type.toCharArray(), type.length(), chr, outlen);
        if (rs == 0 || rs <= 0) {
            chr = null;
            return false;
        }
        String str = String.valueOf(chr).substring(0, rs);
        if (str != null) {
            if (!str.equals("")) {
                String[] temp = str.split("@@@@");
                param.username = temp[0];
                param.strsrc = temp[1];
                chr = null;
                return true;
            }
        }
        param.username = "";
        param.strsrc = "";
        chr = null;
        return false;
    }

    public void sentEncodeOnce(String username, String passwordMD5, String strsrc, DataPackage data) throws Exception {
        Base64 base64 = new Base64();
        data.username = base64.encodeStrFromStr(username);
        data.paramdata = base64.encodeStrFromArray(new AES().Encrypt(strsrc.getBytes("UTF-8"), passwordMD5.getBytes()));
    }

    public void receiveDecodeOnce(String username, String passwordMD5, String paramdata, DataPlain param) throws Exception {
        Base64 base64 = new Base64();
        param.username = base64.decodeStrFromStr(username);
        param.strsrc = new String(new AES().decrypt(base64.decodeArrayFromStr(paramdata), passwordMD5.getBytes()), "UTF-8");
    }
}
