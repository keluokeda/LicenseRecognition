package com.wintone.cipher;

import com.sun.crypto.provider.SunJCE;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateFactory;
import javax.crypto.Cipher;

public class RSA {
    public PrivateKey priKey;
    public PublicKey pubKey;

    static {
        Security.addProvider(new SunJCE());
    }

    public RSA() throws Exception {
        getKeyPair();
    }

    public RSA(PublicKey pubKey, PrivateKey priKey) throws Exception {
        this.priKey = priKey;
        this.pubKey = pubKey;
    }

    public RSA(String pubfile) throws Exception {
        readPubKey(pubfile);
    }

    public RSA(String certFile, String pfxPWD) throws Exception {
        String ext = certFile.substring(certFile.length() - 3, certFile.length());
        if (ext.equals("p12")) {
            KeyStore ks = KeyStore.getInstance("pkcs12");
            FileInputStream fis = new FileInputStream(certFile);
            ks.load(fis, null);
            this.priKey = (PrivateKey) ks.getKey(ks.aliases().nextElement().toString(), pfxPWD.toCharArray());
            fis.close();
        }
        if (ext.equals("p12") || ext.equals("crt")) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            FileInputStream in = new FileInputStream(new StringBuffer(String.valueOf(certFile.substring(0, certFile.length() - 3))).append("crt").toString());
            this.pubKey = cf.generateCertificate(in).getPublicKey();
            in.close();
        }
    }

    public void getKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024, new SecureRandom());
        KeyPair kp = kpg.generateKeyPair();
        this.priKey = kp.getPrivate();
        this.pubKey = kp.getPublic();
    }

    public void readKey(String pubfile, String prifile) throws Exception {
        FileInputStream f = new FileInputStream(pubfile);
        ObjectInputStream b = new ObjectInputStream(f);
        this.pubKey = (PublicKey) b.readObject();
        b.close();
        f.close();
        FileInputStream f2 = new FileInputStream(prifile);
        ObjectInputStream b2 = new ObjectInputStream(f2);
        this.priKey = (PrivateKey) b2.readObject();
        b2.close();
        f2.close();
    }

    public void writeKey(String pubfile, String prifile) throws Exception {
        FileOutputStream f2 = new FileOutputStream(pubfile);
        ObjectOutputStream b2 = new ObjectOutputStream(f2);
        b2.writeObject(this.pubKey);
        b2.flush();
        b2.close();
        f2.close();
        FileOutputStream f1 = new FileOutputStream(prifile);
        ObjectOutputStream b1 = new ObjectOutputStream(f1);
        b1.writeObject(this.priKey);
        b1.flush();
        b1.close();
        f1.close();
    }

    public void readPubKey(String pubfile) throws Exception {
        FileInputStream f = new FileInputStream(pubfile);
        ObjectInputStream b = new ObjectInputStream(f);
        this.pubKey = (PublicKey) b.readObject();
        b.close();
        f.close();
    }

    public void writePubKey(String pubfile) throws Exception {
        FileOutputStream f2 = new FileOutputStream(pubfile);
        ObjectOutputStream b2 = new ObjectOutputStream(f2);
        b2.writeObject(this.pubKey);
        b2.flush();
        b2.close();
        f2.close();
    }

    public byte[] Encrypt(byte[] src) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, this.pubKey);
        return cipher.doFinal(src);
    }

    public byte[] Decrypt(byte[] crypt) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, this.priKey);
        return cipher.doFinal(crypt, 0, crypt.length);
    }

    public byte[] Sign(byte[] src) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, this.priKey);
        return cipher.doFinal(src);
    }

    public byte[] Verify(byte[] crypt) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, this.pubKey);
        return cipher.doFinal(crypt, 0, crypt.length);
    }

    public static void main(String[] args) throws Exception {
        byte[] src = "123456789abcdef".getBytes("UTF-8");
        RSA rsaSign = new RSA("E:/website/cert/wtqinxue/wtqinxue_cert.p12", "XrMlcKKsqG+IKzJEAfvERA==");
        rsaSign.writePubKey("E:/website/cert/wtqinxue/wtqinxue_pub.ppk");
        byte[] enc = rsaSign.Sign(src);
        System.out.println();
        byte[] des = new RSA("E:/website/cert/wtqinxue/wtqinxue_cert.crt", "").Encrypt(src);
        System.out.println();
    }
}
