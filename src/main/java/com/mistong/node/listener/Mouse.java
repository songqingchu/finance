package com.mistong.node.listener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.BaseTSD.ULONG_PTR;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.HOOKPROC;
import com.sun.jna.platform.win32.WinUser.MSG;
import com.sun.jna.platform.win32.WinUser.POINT;

public class Mouse implements Runnable{
	  
	  public static final int WM_MOUSEMOVE = 512;
	  private static HHOOK hhk;
	  private static LowLevelMouseProc mouseHook;
	  final static User32 lib = User32.INSTANCE;
	  private boolean [] on_off=null;

	  public Mouse(boolean [] on_off){
	    this.on_off = on_off;
	  }

	  public interface LowLevelMouseProc extends HOOKPROC {
	    LRESULT callback(int nCode, WPARAM wParam, MOUSEHOOKSTRUCT lParam);
	  }

	  public static class MOUSEHOOKSTRUCT extends Structure {

	    
		 public static class ByReference extends MouseHookStruct implements Structure.ByReference {};  
		    public POINT pt; //点坐标  
		    public HWND hwnd;//窗口句柄  
		    public int wHitTestCode;  
		    public ULONG_PTR dwExtraInfo; //扩展信息  
		     
		    //返回属性顺序  
		 @Override  
		 protected List getFieldOrder() {  
		  return Arrays.asList("dwExtraInfo","hwnd","pt","wHitTestCode");  
		 }  
	  }

	  public void run() {
	    HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
	    mouseHook = new LowLevelMouseProc() {
	      public LRESULT callback(int nCode, WPARAM wParam,
	          MOUSEHOOKSTRUCT info) {
	        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
	        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String fileName=df1.format(new Date());
	        String time=df2.format(new Date());
	        BufferedWriter bw1=null;
	        BufferedWriter bw2=null;
	        /*  try {
	         bw1=new BufferedWriter(new FileWriter(new File(".//log//"+fileName+"_Mouse.txt"),true));
	          bw2=new BufferedWriter(new FileWriter(new File(".//log//"+fileName+"_Common.txt"),true));
	        } catch (IOException e) {
	          e.printStackTrace();
	        }
	        if (on_off[0] == false) {
	          System.exit(0);
	        }*/
	        if (nCode >= 0) {
	          switch (wParam.intValue()) {
	          case MouseHook.WM_MOUSEMOVE:
	              System.out.println(time+"  ####  "+"x=" + info.pt.x
	                  + " y=" + info.pt.y+"\r\n");
	              System.out.println(time+"  ####  "+"x=" + info.pt.x
	                  + " y=" + info.pt.y+"\r\n");
	              /*bw1.flush();
	              bw2.flush();*/
	          }
	        }
	        return lib
	        .CallNextHookEx(hhk, nCode, wParam, info.getPointer());
	      }
	    };
	    hhk = lib.SetWindowsHookEx(User32.WH_MOUSE_LL, mouseHook, hMod, 0);
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
	    lib.UnhookWindowsHookEx(hhk);
	  }
	}