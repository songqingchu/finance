package com.taobao.finance.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	
	public static List<String> fileList=new ArrayList<String>();
	static {
		

	}

	public static List<String> listAnasysDateDir(){
		List<String> l=new ArrayList<String>();
		File f=new File(FetchUtil.FILE_STOCK_ANASYS_BASE);
		File[] child=f.listFiles();
		for(File c:child){
			if(c.isDirectory()){
				l.add(c.getName());
			}
		}
		return l;
	}

	public static void main(String args[]){
		System.out.println(listAnasysDateDir());
	}
}
