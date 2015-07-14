package org.aiwolf.client.ui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import org.aiwolf.client.ui.res.JapaneseResource;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.util.Pair;

public class EventPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SpringLayout springLayout;

	Map<AgentPanel, Pair<Integer, Integer>> locationMap;
	Map<Agent, AgentPanel> agentMap;

	Map<Pair<Agent, Agent>, Color> arrowMap;
	
	private JPanel centerPanel;
	
	static public void main(String[] args){
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try{
			  UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
			  SwingUtilities.updateComponentTreeUI(frame);
			}catch(Exception e){
			  e.printStackTrace();
		}
		
		
		EventPanel panel = new EventPanel();

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

	
//	JLayeredPane layerPane;
	public EventPanel() {
		super();
		setOpaque(false);
		setDoubleBuffered(true);
		
		locationMap = new LinkedHashMap<AgentPanel, Pair<Integer,Integer>>();
		agentMap= new HashMap<>();
		arrowMap = new HashMap<>();
	
		setSize(AgentPanel.PANEL_HEIGHT*10,(int)(AgentPanel.PANEL_HEIGHT*3.1));
		setPreferredSize(getSize());
		setMinimumSize(getSize());
		setMaximumSize(getSize());


		springLayout = new SpringLayout();
		setLayout(springLayout);
		
		setBackground(Color.WHITE);

//		layerPane = new JLayeredPane();
//		add(layerPane, BorderLayout.CENTER);
//		
//		JPanel arrowPanel = new JPanel();
//		arrowPanel.setSize(getSize());
//		arrowPanel.setPreferredSize(getSize());
//		arrowPanel.setBackground(Color.ORANGE);
//		layerPane.add(arrowPanel, 0, 0);
//		layerPane.setLayer(arrowPanel, 50);

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

//		for(AgentPanel ap:panelList){
//			layerPane.setLayer(ap, 1);
//		}
		
		if(centerPanel == null){
			centerPanel = new JPanel(new BorderLayout());
			centerPanel.setOpaque(false);
			//			centerPanel.setBackground(new Color(255, 255, 255, 0));
			Dimension d = new Dimension(getWidth()/2, h*3/4);
			centerPanel.setSize(d);
			centerPanel.setPreferredSize(d);
			centerPanel.setMinimumSize(d);
			centerPanel.setMaximumSize(d);
//			talkPanel.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
			addPanel(centerPanel, (getWidth()-centerPanel.getWidth())/2, (getHeight()-centerPanel.getHeight())/2);
		}
		
		for(AgentPanel ap:panelList){
			agentMap.put(ap.getAgent(), ap);
		}
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
	public void addPanel(JComponent panel, int x, int y){
//		System.out.println("Add Panel");
		remove(panel);
		springLayout.putConstraint(SpringLayout.NORTH, panel, y, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, panel, x, SpringLayout.WEST, this);
		add(panel);
		
		if(panel instanceof AgentPanel){
			x += panel.getWidth()/2;
			y += panel.getHeight()/2;
			locationMap.put((AgentPanel)panel, new Pair<Integer, Integer>(x, y));
//		System.out.println(panel.getAgent()+"\t"+x+"\t"+y);
		}
	}

	
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
//		System.out.println("Paint");
//		g.setColor(Color.white);
//		g.fillRect(0, 0, getWidth(), getHeight());

		synchronized (arrowMap) {
			for(Pair<Agent, Agent> agentPair:arrowMap.keySet()){
				g.setColor(arrowMap.get(agentPair));

				AgentPanel fromPanel = agentMap.get(agentPair.getKey());
				AgentPanel toPanel = agentMap.get(agentPair.getValue());
				
				Graphics2D g2 = (Graphics2D)g;
				
				double fcx = fromPanel.getBounds().getCenterX();
				double fcy = fromPanel.getBounds().getCenterY();
				if(fcy < getHeight()/3){
					fcy = fromPanel.getBounds().getMaxY();
				}
				else if(fcy > getHeight()*2/3){
					fcy = fromPanel.getBounds().getMinY();
				}
				
				double tcx = toPanel.getBounds().getCenterX();
				double tcy = toPanel.getBounds().getCenterY();
				if(tcy < getHeight()/3){
					tcy = toPanel.getBounds().getMaxY();
				}
				else if(tcy > getHeight()*2/3){
					tcy = toPanel.getBounds().getMinY();
				}
				else{
					if(tcx < getWidth()/2){
						tcx = toPanel.getBounds().getMaxX();
					}
					else{
						tcx = toPanel.getBounds().getMinX();
					}
				}
				
				double cx = getWidth()/2;
				double cy = getHeight()/2;
				
				Stroke defaultStroke = g2.getStroke();

				BasicStroke stroke = new BasicStroke(4.0f);
				g2.setStroke(stroke);
				QuadCurve2D.Double curve = new QuadCurve2D.Double(fcx, fcy, cx, cy, tcx, tcy);
				g2.draw(curve);

				if(tcx == cx){
					cx+=1;
				}
				double theta = Math.atan2((tcy-cy), (tcx-cx));
				
				double r = 10;
				double dt = Math.PI/4;
				Polygon p = new Polygon();
				p.addPoint((int)tcx, (int)tcy);
//				p.addPoint((int)(tcx-Math.cos(theta)*r),(int)(tcy-Math.sin(theta)*r));
				p.addPoint((int)(tcx-Math.cos(theta+dt)*r),(int)(tcy-Math.sin(theta+dt)*r));
				p.addPoint((int)(tcx-Math.cos(theta-dt)*r),(int)(tcy-Math.sin(theta-dt)*r));
//				p.addPoint((int)(tcx+Math.cos(theta+Math.PI/8+Math.PI)*r), (int)(tcy+Math.sin(theta+Math.PI/8+Math.PI)*r));
//				p.addPoint((int)(tcx-Math.cos(theta-Math.PI/8+Math.PI)*r), (int)(tcy+Math.sin(theta-Math.PI/8+Math.PI)*r));
				p.addPoint((int)tcx, (int)tcy);

//				System.out.println("theta="+theta/Math.PI*180+"\t"+theta );
//				System.out.println(Arrays.toString(p.xpoints));
//				System.out.println(Arrays.toString(p.ypoints));
//				g.setColor(Color.BLUE);
				g2.drawPolygon(p);
				g2.fillPolygon(p);
				
				g2.setStroke(defaultStroke);

			}
		}
		super.paintComponent(g);

	}

	JComponent lastComponent;
	public void addCenterItem(JComponent component) {
		
		synchronized (centerPanel) {
			centerPanel.setVisible(false);
			if(lastComponent != null){
				centerPanel.remove(lastComponent);
			}

			Color c = component.getBackground();
//			centerPanel.setBackground(new Color(c.getRed(), c.getGreen(), c.getBlue(), 128));
//			component.setBorder(new LineBorder(Color.BLACK));
			centerPanel.add(component, BorderLayout.CENTER);
			component.setBackground(new Color(c.getRed(), c.getGreen(), c.getBlue(), 128));
			
			lastComponent = component;

//			try {
//				Thread.sleep(20);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}	
			centerPanel.setVisible(true);
			centerPanel.repaint();
		}
//		arrowList.clear();
		
		this.repaint();
	}
	
	/**
	 * 
	 * @param from
	 * @param to
	 */
	public void addArrow(Agent from, Agent to, Color color){
//		arrowMap.clear();
		arrowMap.put(new Pair<Agent, Agent>(from, to), color);
		repaint();
//		agentMap.get(from).setBackground(Color.CYAN);
//		agentMap.get(to).setBackground(Color.ORANGE);
//		System.out.println(from+"=>"+to);
//		this.repaint();
	}


	public void clearArrow() {
		arrowMap.clear();
//		centerPanel.setVisible(false);
//		repaint();
	}


	public void clearCenterPanel() {
		centerPanel.removeAll();
		
	}
	
}
//
//class ArrowPaintPanel extends JPanel{
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	List<Pair<Agent, Agent>> arrowList;
//	
//	AgentPanel fromPanel;
//	AgentPanel toPanel;
//	
//	
//	@Override
//	public void paintComponent(Graphics g){
//		super.paintComponent(g);
////		System.out.println("Paint");
////		g.setColor(Color.white);
////		g.fillRect(0, 0, getWidth(), getHeight());
//
//		g.setColor(Color.RED);
//		synchronized (arrowList) {
//			for(Pair<Agent, Agent> agentPair:arrowList){
////				AgentPanel fromPanel = agentMap.get(agentPair.getKey());
////				AgentPanel toPanel = agentMap.get(agentPair.getValue());
//				
//				Graphics2D g2 = (Graphics2D)g;
//				BasicStroke stroke = new BasicStroke(4.0f);
//				g2.setStroke(stroke);
//				
//				double fcx = fromPanel.getBounds().getCenterX();
//				double fcy = fromPanel.getBounds().getCenterY();
//				if(fcy < getHeight()/3){
//					fcy = fromPanel.getBounds().getMaxY();
//				}
//				else if(fcy > getHeight()*2/3){
//					fcy = fromPanel.getBounds().getMinY();
//				}
//				
//				double tcx = toPanel.getBounds().getCenterX();
//				double tcy = toPanel.getBounds().getCenterY();
//				if(tcy < getHeight()/3){
//					tcy = toPanel.getBounds().getMaxY();
//				}
//				else if(tcy > getHeight()*2/3){
//					tcy = toPanel.getBounds().getMinY();
//				}
//
//				
//				double cx = getWidth()/2;
//				double cy = getHeight()/2;
//				
//				QuadCurve2D.Double curve = new QuadCurve2D.Double(fcx, fcy, cx, cy, tcx, tcy);
//				g2.draw(curve);
//
//			}
//		}
//		super.paintComponent(g);
//
//	}
//}


