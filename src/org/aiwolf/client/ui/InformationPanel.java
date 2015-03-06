package org.aiwolf.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
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
import org.aiwolf.common.data.Team;
import org.aiwolf.common.net.GameInfo;
import org.aiwolf.common.net.GameSetting;

public class InformationPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected JPanel titlePanel;
	protected JPanel agentListPanel;
	private TalkPanel talkPanel;
//	private JPanel informationPanel;
	
	private Map<Agent, AgentPanel> agentPanelMap;
	
	private GameSetting gameSetting;
//	private JLabel remainLabel;
	
	private AIWolfResource resource;
	
	public InformationPanel(AIWolfResource resource) {
		this.resource = resource;
//		setPreferredSize(new Dimension(HumanPlayerFrame.PANEL_WIDTH, AgentPanel.PANEL_HEIGHT*5));
		
//		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(new BorderLayout());
		titlePanel = new JPanel();
//		agentListPanel = new JPanel();
		agentListPanel = new SampleGraphicTestPanel();
//		agentListPanel.setPreferredSize(new Dimension(getWidth(), (int)(AgentPanel.PANEL_HEIGHT*4)));
		
		
//		informationPanel = new JPanel();
//		informationPanel.setPreferredSize(new Dimension(getWidth(), (int)(AgentPanel.PANEL_HEIGHT)));
		
		talkPanel = new TalkPanel(resource);
//		talkPanel.setPreferredSize(new Dimension(HumanPlayer.PANEL_WIDTH, 200));		
		
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		JPanel logoPanel = new JPanel(new FlowLayout());
		URL url=getClass().getClassLoader().getResource("img/aiwolfLogo.png");
		ImageIcon logoIcon = new ImageIcon(url);
		JLabel logoLabel = new JLabel(logoIcon);

		logoPanel.add(logoLabel);
		logoPanel.setBackground(new Color(196,196, 196));
		titlePanel.add(logoPanel);
//		titlePanel.setSize(getWidth(), logoIcon.getIconHeight());
//		titlePanel.setPreferredSize(new Dimension(getWidth(), logoIcon.getIconHeight()));
////		titlePanel.setMaximumSize(titlePanel.getSize());
//		titlePanel.setMinimumSize(titlePanel.getSize());
		
//		System.out.println(logoIcon.getIconHeight()+"\t"+titlePanel.getSize());
		
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(talkPanel, BorderLayout.CENTER);
		centerPanel.add(agentListPanel, BorderLayout.NORTH);

		add(centerPanel, BorderLayout.CENTER);
		add(titlePanel, BorderLayout.NORTH);
//		add(agentListPanel, BorderLayout.SOUTH);
//		add(talkPanel, BorderLayout.CENTER);
		

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

		talkPanel.initialize(gameInfo, gameSetting);
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
	
		talkPanel.update(gameInfo);
		talkPanel.scrollToTail();
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

	/**
	 * @param day
	 * @param talkList
	 * @return
	 * @see org.aiwolf.client.ui.TalkPanel#updateTalk(int, java.util.List)
	 */
	public boolean updateTalk(int day, List<Talk> talkList) {
		return talkPanel.updateTalk(day, talkList);
	}
	
	

	/**
	 * @return
	 * @see org.aiwolf.client.ui.TalkPanel#getLastTalkIdx()
	 */
	public int getLastTalkIdx() {
		return talkPanel.getLastTalkIdx();
	}

	/**
	 * @return
	 * @see org.aiwolf.client.ui.TalkPanel#getLastWhisperIdx()
	 */
	public int getLastWhisperIdx() {
		return talkPanel.getLastWhisperIdx();
	}

	/**
	 * @param day
	 * @param whisperList
	 * @return
	 * @see org.aiwolf.client.ui.TalkPanel#updateWhisper(int, java.util.List)
	 */
	public boolean updateWhisper(int day, List<Talk> whisperList) {
		return talkPanel.updateWhisper(day, whisperList);
	}
	
	/**
	 * 
	 */
	public void scrollToTail(){
		talkPanel.scrollToTail();
	}

	/**
	 * @param gameInfo
	 * @see org.aiwolf.client.ui.TalkPanel#dayStart(org.aiwolf.common.net.GameInfo)
	 */
	public void dayStart(GameInfo gameInfo) {
		talkPanel.dayStart(gameInfo);
	}

	public void setWinner(int day, Team winner) {
		talkPanel.setWinner(day, winner);
		
	}
	
}
