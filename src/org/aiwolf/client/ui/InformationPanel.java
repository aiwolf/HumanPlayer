package org.aiwolf.client.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.aiwolf.client.ui.res.AIWolfResource;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.net.GameInfo;
import org.aiwolf.common.net.GameSetting;

public class InformationPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel titlePanel;
	private JPanel agentListPanel;
//	private JPanel informationPanel;
	
	private Map<Agent, AgentPanel> agentPanelMap;
	
	private GameSetting gameSetting;
//	private JLabel remainLabel;
	
	private AIWolfResource resource;
	
	public InformationPanel(AIWolfResource resource) {
		this.resource = resource;
//		setPreferredSize(new Dimension(HumanPlayerFrame.PANEL_WIDTH, AgentPanel.PANEL_HEIGHT*5));
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		titlePanel = new JPanel();
//		agentListPanel = new JPanel();
		agentListPanel = new SampleGraphicTestPanel();
//		agentListPanel.setPreferredSize(new Dimension(getWidth(), (int)(AgentPanel.PANEL_HEIGHT*4)));
		
//		informationPanel = new JPanel();
//		informationPanel.setPreferredSize(new Dimension(getWidth(), (int)(AgentPanel.PANEL_HEIGHT)));
		
		add(titlePanel);
		add(agentListPanel);
		
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		
		JPanel logoPanel = new JPanel(new FlowLayout());

		URL url=getClass().getClassLoader().getResource("img/aiwolfLogo.png");
		ImageIcon logoIcon = new ImageIcon(url);
		JLabel logoLabel = new JLabel(logoIcon);

		logoPanel.add(logoLabel);
		logoPanel.setBackground(new Color(196,196, 196));
		titlePanel.add(logoPanel);
		

	}

	public void initialize(GameInfo gameInfo, GameSetting gameSetting){
		this.gameSetting = gameSetting;

		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setAlignOnBaseline(true);
//		agentListPanel.setLayout(flowLayout);
		agentListPanel.setBorder(new LineBorder(Color.BLACK));
		agentPanelMap = new HashMap<>();
		List<AgentPanel> panelList = new ArrayList<AgentPanel>();
		for(Agent agent:new TreeSet<Agent>(gameInfo.getAgentList())){
//			AgentPanel panel = new AgentPanel(agent, gameInfo.getRoleMap().get(agent), agent.equals(gameInfo.getAgent()), talkConverter);
			AgentPanel agentPanel = new AgentImagePanel(agent, gameInfo.getRoleMap().get(agent), agent.equals(gameInfo.getAgent()), resource);
			panelList.add(agentPanel);
			
			agentListPanel.add(agentPanel);
			agentPanelMap.put(agent, agentPanel);
		}
		
		((SampleGraphicTestPanel)agentListPanel).setAgentPanelList(panelList);

	}
	
	/**
	 * 
	 * @param gameInfo
	 */
	public void update(GameInfo gameInfo) {
//		dayLabel.setText("Day "+gameInfo.getDay()+"");
//		remainLabel.setText(gameInfo.getAliveAgentList().size()+"");
		
		for(Agent agent:gameInfo.getAgentList()){
			agentPanelMap.get(agent).setStatus(gameInfo.getStatusMap().get(agent));
			agentPanelMap.get(agent).setRole(gameInfo.getRoleMap().get(agent));
		}

		if(gameInfo.getAttackedAgent() != null){
//			agentPanelMap.get(gameInfo.getAttackedAgent()).setInformation("Attacked(day"+gameInfo.getDay()+")");
			agentPanelMap.get(gameInfo.getAttackedAgent()).setAttacked(gameInfo.getDay());
		}
		if(gameInfo.getExecutedAgent() != null){
			agentPanelMap.get(gameInfo.getExecutedAgent()).setExecuted(gameInfo.getDay());
		}
		
	}

	/**
	 * 
	 * @param gameInfo
	 */
	public void finish(GameInfo gameInfo) {

	}

	/**
	 * 
	 * @param agent
	 * @param role
	 */
	public void setComingOut(Agent agent, Role role) {
		agentPanelMap.get(agent).setComingOut(role);
	}
	
	/**
	 * Show Talk Result
	 * @param talk
	 */
	public void showTalk(Talk talk){
		String text = resource.convertTalk(talk);
		
	}
	
	
}
