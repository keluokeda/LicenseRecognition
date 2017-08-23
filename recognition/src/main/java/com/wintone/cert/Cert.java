package com.wintone.cert;

public class Cert {
    public String days = "";
    public String myCSR = "";
    public String myP12 = "";
    public String myPrivKey = "";
    public String myPrivKeyPWD = "";
    public String myPubKey = "";
    public String myX509 = "";
    public String rootCert = "";
    public String rootPrivKey = "";
    public String rootPrivKeyPWD = "";
    public String username = "";

    public Cert(String username) {
        this.username = username;
    }

    public static void main(String[] args) {
    }
}
