package com.taobao.finance.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	
	public static List<String> fileList=new ArrayList<String>();
	static {
		/*fileList.add(FetchUtil.FILE_NORMAL_CONVERT);
		fileList.add(FetchUtil.FILE_NORMAL_CONVERT1);
		fileList.add(FetchUtil.FILE_NORMAL_CONVERT2);
		fileList.add(FetchUtil.FILE_NORMAL_CONVERT3);
		fileList.add(FetchUtil.FILE_NORMAL_CONVERT4);
		
		fileList.add(FetchUtil.FILE_HOLDING_CONVERT);
		fileList.add(FetchUtil.FILE_HOLDING_CONVERT1);
		fileList.add(FetchUtil.FILE_HOLDING_CONVERT2);
		fileList.add(FetchUtil.FILE_HOLDING_CONVERT3);
		fileList.add(FetchUtil.FILE_HOLDING_CONVERT4);
		
		fileList.add(FetchUtil.FILE_LOAN_CONVERT);
		fileList.add(FetchUtil.FILE_LOAN_CONVERT1);
		fileList.add(FetchUtil.FILE_LOAN_CONVERT2);
		fileList.add(FetchUtil.FILE_LOAN_CONVERT3);
		fileList.add(FetchUtil.FILE_LOAN_CONVERT4);*/
		
		fileList.add(FetchUtil.FILE_HARDEN);
		fileList.add(FetchUtil.FILE_HARDEN1);
		fileList.add(FetchUtil.FILE_HARDEN2);
		fileList.add(FetchUtil.FILE_HARDEN3);
		fileList.add(FetchUtil.FILE_HARDEN4);
		
		fileList.add(FetchUtil.FILE_HARDEN_YOU);
		fileList.add(FetchUtil.FILE_HARDEN_YOU1);
		fileList.add(FetchUtil.FILE_HARDEN_YOU2);
		fileList.add(FetchUtil.FILE_HARDEN_YOU3);
		fileList.add(FetchUtil.FILE_HARDEN_YOU4);
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
	public static List<String> listStatisDateDir(){
		List<String> l=new ArrayList<String>();
		File f=new File(FetchUtil.FILE_STOCK_STATIS_BASE);
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
