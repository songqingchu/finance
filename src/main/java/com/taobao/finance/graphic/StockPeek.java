package com.taobao.finance.graphic;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sun.jna.Platform;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.MSG;
import com.taobao.finance.common.cache.ICacheService;

public class StockPeek {
	// 鼠标事件编码
	public static final int WM_MOUSEMOVE = 512;
	public static final int WM_LBUTTONDOWN = 513;
	public static final int WM_LBUTTONUP = 514;
	public static final int WM_RBUTTONDOWN = 516;
	public static final int WM_RBUTTONUP = 517;
	public static final int WM_MBUTTONDOWN = 519;
	public static final int WM_MBUTTONUP = 520;
	public User32 lib;
	private static HHOOK hhk;
	private MouseHookListener mouseHook;
	private HMODULE hMod;
	private boolean isWindows = false;
	ScheduledExecutorService executor=Executors.newScheduledThreadPool(1);
	
	public StockFrame frame=null;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	StockService stockService=new StockService();
	
	@Autowired
	ICacheService cacheService;
	
	public StockPeek() {
		frame=new StockFrame();
		frame.hide();
		isWindows = Platform.isWindows();
		if (isWindows) {
			lib = User32.INSTANCE;
			hMod = Kernel32.INSTANCE.GetModuleHandle(null);
		}

	}

	// 添加钩子监听
	public void addMouseHookListener(MouseHookListener mouseHook) {
		this.mouseHook = mouseHook;
		this.mouseHook.lib = lib;
	}

	// 启动
	public void startWindowsHookEx() {
		if (isWindows) {
			lib.SetWindowsHookEx(WinUser.WH_MOUSE_LL, mouseHook, hMod, 0);
			int result;
			MSG msg = new MSG();
			while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {
				if (result == -1) {
					System.err.println("error in get message");
					break;
				} else {
					System.err.println("got message");
					lib.TranslateMessage(msg);
					lib.DispatchMessage(msg);
				}
			}
		}

	}

	// 关闭
	public void stopWindowsHookEx() {
		if (isWindows) {
			lib.UnhookWindowsHookEx(hhk);
		}

	}

	public static void main(String[] args) {
		try {
			
			ApplicationContext ctx=new ClassPathXmlApplicationContext();
			StockPeek mouseHook = new StockPeek();
			mouseHook.executor.scheduleWithFixedDelay(new FetchThread(mouseHook.frame), 0, 4, TimeUnit.SECONDS);
			MouseHookListener listener=new StockMouseListener(mouseHook.frame);
			mouseHook.addMouseHookListener(listener);
			mouseHook.startWindowsHookEx();
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
