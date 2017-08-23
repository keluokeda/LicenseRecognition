package com.wintone.cert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MyProcess {

    class StreamGobbler extends Thread {
        InputStream is;
        final MyProcess this$0;
        String type;

        public StreamGobbler(MyProcess myProcess, InputStream is, String type) {
            this.this$0 = myProcess;
            this.is = is;
            this.type = type;
        }

        public void run() {
            InputStreamReader isr = new InputStreamReader(this.is);
            BufferedReader br = new BufferedReader(isr);
            while (true) {
                try {
                    String line = br.readLine();
                    if (line == null) {
                        br.close();
                        isr.close();
                        return;
                    } else if (this.type.equals("err")) {
                        System.out.println(line);
                    } else {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public void doProcess(String strcmd) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(new StringBuffer().append(strcmd).toString());
        StreamGobbler errorGobbler = new StreamGobbler(this, process.getErrorStream(), "err");
        StreamGobbler outputGobbler = new StreamGobbler(this, process.getInputStream(), "output");
        errorGobbler.start();
        outputGobbler.start();
        process.waitFor();
    }
}
