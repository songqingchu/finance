package com.mistong.node.listener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class A {
	public static void main(String[] args){
		try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("F:\\test\\ffmpeg\\bin\\ffmpeg.exe -i \"F:\\test\\ffmpeg\\bin\\b.mp4\" -vf \"[in] scale=iw/2:ih/2, pad=2*iw:ih [left]; movie=\"F:\\test\\ffmpeg\\bin\\c.mp4\", scale=iw/2:ih/2 [right];[left][right] overlay=main_w/2:0 [out]\" -b:v 768k d.mp4");
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
 
            while ( (line = br.readLine()) != null){
            	System.out.println(line);
            }
            int exitVal = proc.waitFor();
            System.out.println("Process exitValue: " + exitVal);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
