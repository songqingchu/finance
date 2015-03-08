package com.taobao.finance.choose.local.thread.base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.FetchUtil;
import com.taobao.finance.util.ThreadUtil;

public abstract class Local_Choose_MultiThread_Base {

	public List<Stock> choose() {
		int threadNum = Runtime.getRuntime().availableProcessors();
		List<List<Stock>> l = ThreadUtil.split(threadNum);
		ExecutorService service = Executors.newFixedThreadPool(threadNum);
		CompletionService<List<Stock>> con = new ExecutorCompletionService<List<Stock>>(service);
		List<Callable<List<Stock>>> ll = prepareTask(l);
		for (Callable<List<Stock>> c : ll) {
			con.submit(c);
		}
		List<Stock> r = new ArrayList<Stock>();
		int i = 1;
		while (i <= threadNum) {
			try {
				r.addAll(con.take().get());
				i++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		service.shutdown();
		save(r);
		for (Stock s : r) {
			System.out.println(s.getCode());
		}
		return r;
	}

	public abstract List<Callable<List<Stock>>> prepareTask(List<List<Stock>> ll);

	public void save(List<Stock> l) {
		String path = getPath();
		Date d = new Date();
		path = path + FetchUtil.FILE_FORMAT.format(d)+".txt";
		File f = new File(path);
		if(!f.exists()){
			FetchUtil.createFile(path);
		}
		
		BufferedWriter br = null;
		try {
			br = new BufferedWriter(new FileWriter(f));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Stock s : l) {
			try {
				br.write(s.getSymbol()+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Stock> filter(List<Stock> l) {
		String path = getPath();
		File f = new File(path);
		String[] files=f.list();
		List<Date> ld=new ArrayList<Date>();
		for(String d:files){
			d=d.replace(".txt", "");
			Date date=null;
			try {
				date=FetchUtil.FILE_FORMAT.parse(d);
			} catch (ParseException e) {
				e.printStackTrace();
				continue;
			}
			ld.add(date);
		}
		Collections.sort(ld);
		String path2=path+FetchUtil.FILE_FORMAT.format(ld.get(ld.size()-2))+".txt";
		BufferedReader br = null;
		Set<String> s = new HashSet<String>();
		List<Stock> filter = new ArrayList<Stock>();
		try {
			br = new BufferedReader(new FileReader(path2));
			String line = null;
			line = br.readLine();
			while (line != null&&line!="") {
				s.add(line);
				line=br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Set<String> s2=new HashSet<String>();
		for (Stock st : l) {
			if (!s.contains(st.getSymbol())) {
				filter.add(st);
			}
			s2.add(st.getSymbol());
		}
		return filter;
	}
	
	public abstract String getPath();
}
