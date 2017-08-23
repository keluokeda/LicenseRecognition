package com.wintone.Adaptor;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.WString;
import com.wintone.cipher.AES;
import com.wintone.cipher.Base64;
import java.io.IOException;

public class CipherAdaptorC1 {
    static Class class$0;

    public interface PKI extends Library {
        public static final PKI INSTANCE;

        WString DLL_getUserName(WString wString);

        WString DLL_receiveDecode(WString wString, WString wString2, WString wString3, WString wString4);

        WString DLL_sentEncode(WString wString, WString wString2, WString wString3, WString wString4, WString wString5);

        WString DLL_setRecgnPlainParam(WString wString, WString wString2, WString wString3, WString wString4);

        WString DLL_setVerifyPlainParam(WString wString, WString wString2, WString wString3);

        static {
            String str = "PKI";
            Class cls = CipherAdaptorC1.class$0;
            if (cls == null) {
                try {
                    cls = Class.forName("com.wintone.Adaptor.CipherAdaptorC1$PKI");
                    CipherAdaptorC1.class$0 = cls;
                } catch (Throwable e) {
                    throw new NoClassDefFoundError(e.getMessage());
                }
            }
            INSTANCE = (PKI) Native.loadLibrary(str, cls);
        }
    }

    public String setRecgnPlainParam(String imgfilename, String type, String option, String password) {
        if (option == null || option == "") {
            option = "";
        }
        System.out.println(new StringBuffer("imgfilename:").append(imgfilename).toString());
        System.out.println(new StringBuffer("type:").append(type).toString());
        System.out.println(new StringBuffer("option:").append(option).toString());
        System.out.println(new StringBuffer("password:").append(password).toString());
        WString temp = PKI.INSTANCE.DLL_setRecgnPlainParam(new WString(imgfilename), new WString(type), new WString(option), new WString(password));
        System.out.println(new StringBuffer("setRecgnPlainParam_result:").append(temp.toString()).toString());
        return temp.toString();
    }

    public RecgnParam getRecgnPlainParam(String strsrc) {
        String[] array = new WString(strsrc).toString().split("==##");
        RecgnParam recgnParam = new RecgnParam();
        recgnParam.imgBase64Str = array[0];
        recgnParam.type = array[1];
        recgnParam.option = array[2];
        recgnParam.password = array[3];
        return recgnParam;
    }

    public String setVerifyPlainParam(String datasource, String param, String password) {
        return PKI.INSTANCE.DLL_setVerifyPlainParam(new WString(datasource), new WString(param), new WString(password)).toString();
    }

    public VerifyParam getVerifyPlainParam(String strsrc) {
        String[] array = new WString(strsrc).toString().split("==##");
        VerifyParam Param = new VerifyParam();
        Param.datasource = array[0];
        Param.param = array[1];
        Param.password = array[2];
        return Param;
    }

    public String getUserName(String username) throws IOException {
        return PKI.INSTANCE.DLL_getUserName(new WString(username)).toString();
    }

    public void sentEncode(String username, String mycert, String certkey, String otherpubkey, String strsrc, DataPackage data) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>in sentEncode<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        System.out.println(new StringBuffer("username:").append(username).toString());
        System.out.println(new StringBuffer("mycert:").append(mycert).toString());
        System.out.println(new StringBuffer("certkey:").append(certkey).toString());
        System.out.println(new StringBuffer("otherpubkey:").append(otherpubkey).toString());
        System.out.println(new StringBuffer("strsrc:").append(strsrc).toString());
        String type = otherpubkey.replace("pub.ppk", "cert.crt");
        System.out.println(new StringBuffer("otherpubkey.crt:").append(type).toString());
        String temp = PKI.INSTANCE.DLL_sentEncode(new WString(username), new WString(mycert), new WString(certkey), new WString(type), new WString(strsrc)).toString();
        System.out.println(new StringBuffer("temp:").append(temp).toString());
        String[] str = temp.split("@@@@");
        data.username = str[0];
        data.signdata = str[1];
        data.paramdata = str[2];
        System.out.println(new StringBuffer("data.username:").append(data.username).toString());
        System.out.println(new StringBuffer("data.signdata:").append(data.signdata).toString());
        System.out.println(new StringBuffer("data.paramdata:").append(data.paramdata).toString());
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>out sentEncode<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }

    public boolean receiveDecode(String username, String mycert, String certkey, String otherpubkey, String paramdata, String signdata, DataPlain param) throws Exception {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>in receiveDecode<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        String type = otherpubkey.replace("pub.ppk", "cert.crt");
        String strreceive = new StringBuffer(String.valueOf(username)).append("@@@@").append(signdata).append("@@@@").append(paramdata).toString();
        System.out.println(new StringBuffer("strreceive\n").append(strreceive).toString());
        System.out.println(new StringBuffer("mycert\n").append(mycert).toString());
        System.out.println(new StringBuffer("certkey\n").append(certkey).toString());
        System.out.println(new StringBuffer("type\n").append(type).toString());
        String str = PKI.INSTANCE.DLL_receiveDecode(new WString(strreceive), new WString(mycert), new WString(certkey), new WString(type)).toString();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>out receiveDecode<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        if (str == null || str.equals("")) {
            param.username = "";
            param.strsrc = "";
            return false;
        }
        String[] temp = str.split("@@@@");
        param.username = temp[0];
        param.strsrc = temp[1];
        return true;
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
