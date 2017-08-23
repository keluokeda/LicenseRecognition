package com.wintone.Adaptor;

import com.wintone.cipher.AESWithoutJCE;
import com.wintone.cipher.AESofC;
import com.wintone.cipher.Base64;
import com.wintone.cipher.Base64Line;
import com.wintone.cipher.MD5;
import com.wintone.cipher.RSA;
import java.io.IOException;
import java.util.Arrays;

public class CipherAdaptor {
    public String setRecgnPlainParam(String imgfilename, String type, String option, String password) {
        return new StringBuffer(String.valueOf(Base64Line.GetBase64StrFromImage(imgfilename))).append("==##").append(type).append("==##").append(option).append("==##").append(password).toString();
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
        return new StringBuffer(String.valueOf(datasource)).append("==##").append(param).append("==##").append(password).toString();
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
        return new Base64Line().decodeStrFromStr(username);
    }

    public void sentEncode(String username, String mycert, String certkey, String otherpubkey, String strsrc, DataPackage data) throws Exception {
        Base64Line base64 = new Base64Line();
        data.username = base64.encodeStrFromStr(username);
        byte[] bytesrc = strsrc.getBytes("UTF-8");
        byte[] digesta = new MD5().doMD5(bytesrc);
        data.signdata = new StringBuffer(String.valueOf(base64.encodeStrFromArray(new RSA(mycert, certkey).Sign(digesta)))).append("==##").append(base64.encodeStrFromArray(digesta)).toString();
        AESofC aes = new AESofC();
        data.paramdata = new StringBuffer(String.valueOf(base64.encodeStrFromArray(new RSA(otherpubkey).Encrypt(aes.strkey.getBytes())))).append("==##").append(base64.encodeStrFromArray(aes.Encrypt(bytesrc))).toString();
    }

    public boolean receiveDecode(String username, String mycert, String certkey, String otherpubkey, String paramdata, String signdata, DataPlain param) throws Exception {
        Base64Line base64 = new Base64Line();
        param.username = base64.decodeStrFromStr(username);
        String[] arraysign = signdata.split("==##");
        byte[] temp1 = new RSA(otherpubkey).Verify(base64.decodeArrayFromStr(arraysign[0]));
        byte[] temp4 = new byte[16];
        for (int k = 0; k < 16; k++) {
            temp4[k] = temp1[k];
        }
        if (!Arrays.equals(temp4, base64.decodeArrayFromStr(arraysign[1]))) {
            return false;
        }
        String[] array = paramdata.split("==##");
        byte[] key = new RSA(mycert, certkey).Decrypt(base64.decodeArrayFromStr(array[0]));
        AESofC aes = new AESofC();
        aes.strkey = new String(key);
        param.strsrc = new String(aes.decrypt(base64.decodeArrayFromStr(array[1])), "UTF-8");
        return true;
    }

    public void sentEncodeOnce(String username, String passwordMD5, String strsrc, DataPackage data) throws Exception {
        Base64 base64 = new Base64();
        data.username = base64.encodeStrFromStr(username);
        data.paramdata = base64.encodeStrFromArray(new AESWithoutJCE().Encrypt(strsrc.getBytes("UTF-8"), passwordMD5.getBytes()));
    }

    public void receiveDecodeOnce(String username, String passwordMD5, String paramdata, DataPlain param) throws Exception {
        Base64 base64 = new Base64();
        param.username = base64.decodeStrFromStr(username);
        param.strsrc = new String(new AESWithoutJCE().decrypt(base64.decodeArrayFromStr(paramdata), passwordMD5.getBytes()), "UTF-8");
    }
}
