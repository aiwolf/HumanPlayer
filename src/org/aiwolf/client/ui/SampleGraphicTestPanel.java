package org.aiwolf.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.aiwolf.client.ui.res.JapaneseResource;
import org.aiwolf.common.AIWolfRuntimeException;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.util.Pair;

public class SampleGraphicTestPanel extends JPanel{

	private SpringLayout springLayout;

	Map<AgentPanel, Pair<Integer, Integer>> locationMap;
	
	static public void main(String[] args){
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try{
			  UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
			  SwingUtilities.updateComponentTreeUI(frame);
			}catch(Exception e){
			  e.printStackTrace();
		}
		
		
		SampleGraphicTestPanel panel = new SampleGraphicTestPanel();

		JapaneseResource resource = new JapaneseResource();
		List<AgentPanel> list = new ArrayList<AgentPanel>();
		for(int i = 0; i < 5; i++){
			AgentPanel agentPanel = new AgentImagePanel(Agent.getAgent(i), Role.SEER, false, resource);
			list.add(agentPanel);
		}
		panel.setAgentPanelList(list);

		frame.setLayout(new BorderLayout());
		frame.setSize(panel.getWidth(), panel.getHeight());
		
		frame.getContentPane().add(new JPanel(), BorderLayout.NORTH);
		frame.getContentPane().add(panel, BorderLayout.CENTER);

		//		frame.pack();

		frame.setVisible(true);
		
	}

	public SampleGraphicTestPanel() {
		super();
		locationMap = new LinkedHashMap<AgentPanel, Pair<Integer,Integer>>();
		
	
		setSize(AgentPanel.PANEL_HEIGHT*10,(int)(AgentPanel.PANEL_HEIGHT*3.1));
		setPreferredSize(getSize());
//		setMinimumSize(getSize());
		setMaximumSize(getSize());


		springLayout = new SpringLayout();
		setLayout(springLayout);


//		addComponentListener(this);

	}
	
	
	/**
	 *      0
	 * ------------
	 * |          |
	 * ------------
	 * @param panelList
	 */
	public void setAgentPanelList(List<AgentPanel> panelList){
		int w = panelList.get(0).getWidth();
		int h = panelList.get(0).getHeight();
		
		int width = getWidth()-2*w;
		int height = getHeight()-h-4;
		
		List<AgentPanel> upperPanel = new ArrayList<>();
		List<AgentPanel> downerPanel = new ArrayList<>();
		AgentPanel leftPanel;
		AgentPanel rightPanel;
		
		int i;
		for(i = 0; i < (panelList.size()-2)/2; i++){
			upperPanel.add(panelList.get(i));
		}
		leftPanel = panelList.get(i++);
		for(; i < panelList.size()-1; i++){
			downerPanel.add(panelList.get(i));
		}
		rightPanel = panelList.get(panelList.size()-1);
		
		///

		if(!upperPanel.isEmpty()){
			int urange = width/(upperPanel.size());
			for(int j = 0; j < upperPanel.size(); j++){
				addPanel(upperPanel.get(j), urange*(j)+urange/2, 0);
			}
		}		
		int drange = width/(downerPanel.size());
		for(int j = 0; j < downerPanel.size(); j++){
			addPanel(downerPanel.get(j), width-(drange*(j)+drange/2), height);
		}
		
		addPanel(leftPanel, width, (getHeight()-h)/2);
		addPanel(rightPanel, 0, (getHeight()-h)/2);

		
		//		
//		
//		
//		int w = panelList.get(0).getWidth();
//		int h = panelList.get(0).getHeight();
//		int x = getWidth()/2;
//		int y = 0;
//		
//		int width = getWidth()-w*2;
//		int height = getHeight()-h*3/2;
//		int len =(width+height)*2/panelList.size();
//		int pos = 0;
//		
//		for(int i = 0; i < panelList.size(); i++){
//			if(pos < width/2){
//				x = pos+width/2;
//				y = 0;
//			}
//			else if(pos < width/2+height){
//				x = width;
//				y = (pos-width/2);
//			}
//			else if(pos < width*3/2+height){
//				x = width-(pos-(width/2+height));
//				y = height;
//			}
//			else if(pos < width*3/2+height*2){
//				x = 0;
//				y = height-(pos-(width*3/2+height));
//			}
//			else{
//				x = pos-(width*3/2+height*2);
//				y = 0;
//			}
//			addPanel(panelList.get(i), x, y);
//			pos += len;
//		}		
		
	}
	
	public void setAgentPanelListCircle(List<AgentPanel> panelList){
		int w = panelList.get(0).getWidth();
		int h = panelList.get(0).getWidth();
		double rx = getWidth()/2;
		double ry = getHeight()/2;
		int cx = getWidth()/2;
		int cy = getHeight()/2-h/4;
		
		for(int i = 0; i < panelList.size(); i++){
			AgentPanel panel = panelList.get(i);
			double theta = 2*Math.PI/panelList.size()*i-Math.PI/2;
			int x = (int)(Math.cos(theta)*rx)+cx;
			int y = (int)(Math.sin(theta)*ry)+cy;
			x = Math.max(w*3/4, Math.min(x, getWidth()-w*3/4));
			y = Math.max(h*3/4, Math.min(y, getHeight()-h));
			addPanel(panel, x-w/2, y-h/2);
		}
		
	}
	
	/**
	 * 任意の位置にパネルを配置するはずなのに
	 * @param panel
	 * @param x
	 * @param y
	 */
	public void addPanel(AgentPanel panel, int x, int y){
		this.remove(panel);
		springLayout.putConstraint(SpringLayout.NORTH, panel, y, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, panel, x, SpringLayout.WEST, this);
		this.add(panel);

		x += panel.getWidth()/2;
		y += panel.getHeight()/2;
		locationMap.put(panel, new Pair<Integer, Integer>(x, y));
		System.out.println(panel.getAgent()+"\t"+x+"\t"+y);

	}

	
	
	@Override
	public void paintComponent(Graphics g){
		g.setColor(Color.black);
		g.drawLine(0, 0, getWidth(), getHeight());
		
//		System.out.println(springLayout);
//		setAgentPanelList(new ArrayList<AgentPanel>(locationMap.keySet()));
	}
	
	
	
}
