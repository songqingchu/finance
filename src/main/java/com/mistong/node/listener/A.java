package com.mistong.node.listener;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class A {
	public static void main(String[] args){
        final JFrame frame = new JFrame("forbiddenChinese");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JLabel label =  new JLabel(new ImageIcon("F:/aaa.jpg"));
        label.setPreferredSize(new Dimension((int)screenSize.getWidth(), 50));
        frame.add(label,BorderLayout.CENTER);
        frame.setBackground(null);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        /*frame.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				char a=e.getKeyChar();
				if((e.getKeyChar()+"").equals("d")){
					frame.hide();
				}
                if((e.getKeyChar()+"").equals("f")){
					frame.show();
				}
				
			}
		});
        frame.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				System.out.println(e.getPoint().getY()+":"+e.getPoint().getY());
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});*/
        
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //得到屏幕的尺寸 
        System.out.println(screenSize.width); //宽度
        System.out.println(screenSize.height); //高度
        
        
/*        Window a=new Window(frame);
    	WindowAutoHide h=new WindowAutoHide(a);*/
    }
}
