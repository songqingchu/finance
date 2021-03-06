package com.taobao.finance.graphic;

import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.taobao.finance.dataobject.Stock;
import com.taobao.finance.util.FetchUtil;

public class StockFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private Point loc = null;
    private Point tmp = null;
    private boolean isDragged = false;
    private JLabel pic;
    private List<JLabel> stockList=new ArrayList<JLabel>();
    private Map<String,JLabel> stockMap=new HashMap<String,JLabel>();
 
    public StockFrame() {
    	Image imgae=null;
		try {
			imgae = ImageIO.read(this.getClass().getResource("/pic/aaaaa.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	this.setIconImage(imgae);
        pic = new JLabel();
        pic.setIcon(getIcon("aaa.jpg"));
        pic.setBounds(0, 0, 510, 200);
        setAlwaysOnTop(true);
        // 初始化窗体
        setResizable(false);
        // 将窗体设置成无标题栏的语句，setUndecorated();注意此语句一定要放在setVisible之前，否则会报错
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
       
      
        //setSize(120, Toolkit.getDefaultToolkit().getScreenSize().height);
        setVisible(true);
        add(pic);
 
        // 设置窗体为屏幕的中央位置
        //int w = Toolkit.getDefaultToolkit().getScreenSize().width - 100;
        int w = 0;
        int h = 100;
        this.setLocation(w, h);
        this.setLayout(new FlowLayout());
        //this.add(new JLabel("        "));
 
        // 为窗体添加鼠标事件
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                isDragged = false;
                // 为指定的光标设置光标图像
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
 
            public void mousePressed(MouseEvent e) {
                tmp = new Point(e.getX(), e.getY());
                isDragged = true;
                setCursor(new Cursor(Cursor.MOVE_CURSOR));
            }
        });
 
        this.addMouseMotionListener(new MouseMotionAdapter() {
            // 鼠标按键在组件上按下并拖动时调用。
            public void mouseDragged(MouseEvent e) {
                if (isDragged) {
                    loc = new Point(getLocation().x + e.getX() - tmp.x,
                            getLocation().y + e.getY() - tmp.y);
                    setLocation(loc);
                }
            }
        });
    }
 
    public void showStock(List<Stock> l){
    	setSize(120, l.size()*22);
        for(Stock s:l){
        	String symbol=s.getSymbol();
        	if(!stockMap.containsKey(s.getSymbol())){
        		JLabel la=null;
        		if(symbol.contains("sh000001")||symbol.contains("sz399001")||symbol.contains("sz399101")||symbol.contains("sz399006")){
        			la=new JLabel("  "+s.getNameShot().trim()+"  "+s.getRatePercent()+"             ");
        		}else{
        			la=new JLabel("  "+s.getNameShot().trim()+"  "+s.getRatePercent()+" "+FetchUtil.formatRate2(s.getEndPriceFloat()));
        		}
        		
        		la.setToolTipText(s.getCode());
        		stockList.add(la);
        		stockMap.put(s.getSymbol(), la);
        		this.add(la);
        	}else{
        		
        		if(symbol.contains("sh000001")||symbol.contains("sz399001")||symbol.contains("sz399101")||symbol.contains("sz399006")){
        			stockMap.get(s.getSymbol()).setText("  "+s.getNameShot().trim()+"  "+s.getRatePercent()+"             ");
        		}else{
        			stockMap.get(s.getSymbol()).setText("  "+s.getNameShot().trim()+"  "+s.getRatePercent()+" "+FetchUtil.formatRate2(s.getEndPriceFloat()));
        		}
        		stockMap.get(s.getSymbol()).setToolTipText(s.getCode());
        	}
        }
    }
    // 获取图片的方法
    public Icon getIcon(String path) {
        return new ImageIcon(path);
    }
 
    public static void main(String[] args) {
        new StockFrame();
    }
}