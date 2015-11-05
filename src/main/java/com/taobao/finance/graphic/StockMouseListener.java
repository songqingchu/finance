package com.taobao.finance.graphic;

import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;

public class StockMouseListener extends MouseHookListener {
	
	public StockFrame frame;
	
	int times=0;
	public StockMouseListener(StockFrame frame){
		this.frame=frame;
	}
	
	
	@SuppressWarnings("deprecation")
	public LRESULT callback(int nCode, WPARAM wParam, MouseHookStruct lParam) {
		if (nCode >= 0) {
			switch (wParam.intValue()) {
			case StockPeek.WM_MOUSEMOVE:
				if (lParam.pt.y < 50) {
					frame.show();	
				} /*else {
					frame.hide();
				}*/
				//System.out.println(lParam.pt.x+":"+lParam.pt.y);
				break;
			case StockPeek.WM_LBUTTONDOWN:
				break;
			case StockPeek.WM_LBUTTONUP:
				break;
			case StockPeek.WM_MBUTTONDOWN:
				System.out.println("------------------");
				break;
			case StockPeek.WM_MBUTTONUP:
				System.out.println("------------------");
				break;
			case StockPeek.WM_RBUTTONDOWN:
				frame.hide();	
				break;
			case StockPeek.WM_RBUTTONUP:
				break;
			}
		}
		// 将钩子信息传递到当前钩子链中的下一个子程，一个钩子程序可以调用这个函数之前或之后处理钩子信息
		// hhk：当前钩子的句柄
		// nCode ：钩子代码;
		// 就是给下一个钩子要交待的，钩传递给当前Hook过程的代码。下一个钩子程序使用此代码，以确定如何处理钩的信息。
		// wParam：要传递的参数; 由钩子类型决定是什么参数，此参数的含义取决于当前的钩链与钩的类型。
		// lParam：Param的值传递给当前Hook过程。此参数的含义取决于当前的钩链与钩的类型。
		return lib.CallNextHookEx(hhk, nCode, wParam, lParam.getPointer());
	}
}
