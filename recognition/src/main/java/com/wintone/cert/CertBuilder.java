package com.wintone.cert;

import com.wintone.cipher.RSA;
import java.io.File;

public class CertBuilder {
    private Cert cert;

    public CertBuilder(Cert cert) throws Exception {
        this.cert = cert;
    }

    public void userCertBuilder() throws Exception {
        deleteFile(this.cert.myPrivKey);
        deleteFile(this.cert.myCSR);
        deleteFile(new StringBuffer(String.valueOf(this.cert.myX509.substring(0, this.cert.myX509.length() - 3))).append("pem").toString());
        deleteFile(this.cert.myX509);
        deleteFile(this.cert.myP12);
        deleteFile(this.cert.myPubKey);
        String cmd = "";
        MyProcess myProcess = new MyProcess();
        myProcess.doProcess(new StringBuffer("openssl genrsa -out ").append(this.cert.myPrivKey).append(" -des3 -passout pass:").append(this.cert.myPrivKeyPWD).append(" 1024").toString());
        myProcess.doProcess(new StringBuffer("openssl req -new -key  ").append(this.cert.myPrivKey).append(" -passin pass:").append(this.cert.myPrivKeyPWD).append(" -subj /C=CN/O=").append(this.cert.username).append("/CN=").append(this.cert.username).append(" -out ").append(this.cert.myCSR).toString());
        myProcess.doProcess(new StringBuffer("openssl x509 -req -in ").append(this.cert.myCSR).append(" -out ").append(this.cert.myX509.substring(0, this.cert.myX509.length() - 3)).append("pem  -CA ").append(this.cert.rootCert).append(" -CAkey ").append(this.cert.rootPrivKey).append(" -passin pass:").append(this.cert.rootPrivKeyPWD).append(" -CAcreateserial  -days ").append(this.cert.days).toString());
        myProcess.doProcess(new StringBuffer("openssl x509 -in ").append(this.cert.myX509.substring(0, this.cert.myX509.length() - 3)).append("pem -out ").append(this.cert.myX509).toString());
        myProcess.doProcess(new StringBuffer("openssl pkcs12 -export -in ").append(this.cert.myX509).append(" -inkey ").append(this.cert.myPrivKey).append(" -passin pass:").append(this.cert.myPrivKeyPWD).append(" -out ").append(this.cert.myP12).append(" -passout pass:").append(this.cert.myPrivKeyPWD).toString());
        new RSA(this.cert.myX509, "").writePubKey(this.cert.myPubKey);
    }

    public void serverCertBuilder(String cmdfile) throws Exception {
        String path = getPath(cmdfile);
        deleteFile(new StringBuffer(String.valueOf(path)).append("\\server_1_cert.crt").toString());
        deleteFile(new StringBuffer(String.valueOf(path)).append("\\server_1_cert.p12").toString());
        deleteFile(new StringBuffer(String.valueOf(path)).append("\\server_1_cert.pem").toString());
        deleteFile(new StringBuffer(String.valueOf(path)).append("\\server_1_privkey.pem").toString());
        deleteFile(new StringBuffer(String.valueOf(path)).append("\\server_1_pub.ppk").toString());
        deleteFile(new StringBuffer(String.valueOf(path)).append("\\server_1_req.csr").toString());
        new MyProcess().doProcess(cmdfile);
        new RSA(this.cert.myX509, "").writePubKey(this.cert.myPubKey);
    }

    public void caCertBuilder(String cmdfile) throws Exception {
        String path = getPath(cmdfile);
        deleteFile(new StringBuffer(String.valueOf(path)).append("\\ca_cert.crt").toString());
        deleteFile(new StringBuffer(String.valueOf(path)).append("\\ca_cert.pem").toString());
        deleteFile(new StringBuffer(String.valueOf(path)).append("\\ca_cert.srl").toString());
        deleteFile(new StringBuffer(String.valueOf(path)).append("\\ca_privkey.pem").toString());
        deleteFile(new StringBuffer(String.valueOf(path)).append("\\ca_pub.ppk").toString());
        new MyProcess().doProcess(cmdfile);
        new RSA(new StringBuffer(String.valueOf(path)).append("\\ca_cert.crt").toString(), "").writePubKey(new StringBuffer(String.valueOf(path)).append("\\ca_pub.ppk").toString());
    }

    private void deleteFile(String filename) throws Exception {
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
    }

    private String getPath(String filepath) throws Exception {
        return filepath.substring(0, filepath.lastIndexOf("\\"));
    }
}
