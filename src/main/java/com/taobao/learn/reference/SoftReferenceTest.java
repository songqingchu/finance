package com.taobao.learn.reference;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;


/**
 * 设置参数：-Xmx10M -Xms10M -verbose:gc
 * @author Auser
 *
 */
public class SoftReferenceTest {
	
	 private ImageCacheManager cacheMgr = ImageCacheManager.instance();  

	    public static void main(String[] args) {  
	        SoftReferenceTest tester = new SoftReferenceTest();  
	        //先把数据load进来  
	        for (int i = 0; i < 10; i++) {  
	            tester.cacheMgr.get(i);  
	        }  
	          
	        //一张特殊的图片，这里保存了一个对这张图片的强引用，所以该对象不会被回收  
	        Image myPhoto = tester.cacheMgr.get(7);  
	        System.out.println("--------------------------");  
	        System.out.println(myPhoto);  
	        System.out.println("--------------------------");  
	          
	        //打印图片  
	        for (int i = 0; i < 10; i++) {  
	            tester.printImage(i);  
	        }  
	    }  
	      
	    public void printImage(int id) {  
	        System.out.println(cacheMgr.get(id));  
	    }  
}


class ImageCacheManager {  
    private static ImageCacheManager imageCache = null;  
    private ImageCacheManager() {}  
    /** 
     * 作为一个管理器，当然要单例了，不允许随便new一个就当做管理器 
     * @return 
     */  
    public static ImageCacheManager instance() {  
        if (imageCache == null) {  
            synchronized (ImageCacheManager.class) {  
                if (imageCache == null) {  
                    imageCache = new ImageCacheManager();  
                }  
            }  
        }  
        return imageCache;  
    }  
  
    // 保存数据的地方  
    private Map<Integer, SoftReference<Image>> cache = new HashMap<Integer, SoftReference<Image>>();  
  
    public void put(Image image) {  
        cache.put(image.getId(), new SoftReference<Image>(image));  
    }  
  
    private Image loadImage(int id) {  
        byte[][] data = new byte[1024][1024];  
        data[1023][1023] = 1;  
        return new Image(id, data);  
    }  
  
    public Image get(int id) {  
        SoftReference<Image> ref = cache.get(id);  
        // 没有放入过缓存，第一次加载  
        if (ref == null) {  
            Image loadFirst = loadImage(id);  
            // 放入缓存  
            put(loadFirst);  
            return loadFirst;  
        }  
  
        // 下面的情况是该图片已经被加载过，但是可能由于内存不足，又被回收了  
        // 为了便于理解，使用了if else分支方式，其实else不是必须的。  
        // 这两个分支，不管哪一个返回的都是对image的强引用  
        Image cachedImage = ref.get();  
        if (cachedImage == null) {  
            // 从真实介质中读取(此处模拟这个操作）  
            Image imageGetFromMedia = loadImage(id);  
            // 缓存起来  
            put(imageGetFromMedia);  
  
            System.out.println("get image:" + id + " from media.");  
            return imageGetFromMedia;  
        } else {  
            System.out.println("get image:" + id + " from cache.");  
            return cachedImage;  
        }  
    }  
}  

class Image {  
    private byte[][] buffer;  
    private int id;   
    public Image(int id, byte[][] data) {  
        this.id = id;  
        buffer = data;  
    }     
    public int getId() {  
        return this.id;  
    }     
    public String toString() {  
        return "image[" + this.id + "]";  
    }  
}  