package org.aiwolf.client.ui.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;



/**
 * http://www.my-notebook.net/b7a54b6e-7782-4654-9000-6496915586be.html
 * 
 * @author tori
 *
 */
public class ResourceReader {
	
	private static final int SMALL_ICON_SIZE = 30;
	static private ResourceReader resourceReader = new ResourceReader();
	
	static public ResourceReader getInstance(){
		return resourceReader;
	}
	
	
	Map<Integer, ImageIcon> iconMap = new HashMap<>();
	private ResourceReader(){
		iconMap = new HashMap<>();
		
	}
	
	public ImageIcon getImageIcon(int imgIdx){
		if(!iconMap.containsKey(imgIdx)){
			URL url=getClass().getClassLoader().getResource(String.format("img/%02d_body.png", imgIdx));
			ImageIcon icon = new ImageIcon(url);
			iconMap.put(imgIdx, icon);
		}
		return iconMap.get(imgIdx);
	}
	
	public ImageIcon getSmallImageIcon(int imgIdx){
		ImageIcon icon = getImageIcon(imgIdx);
		int h = icon.getIconHeight();
		int w = icon.getIconWidth();
		if(h > w){
			h = SMALL_ICON_SIZE*h/w;
			w = SMALL_ICON_SIZE;
		}
		else{
			h = SMALL_ICON_SIZE;
			w = SMALL_ICON_SIZE*w/h;
		}
		return new ImageIcon(icon.getImage().getScaledInstance(w, h, Image. SCALE_SMOOTH));
		
	}
	
	
}
