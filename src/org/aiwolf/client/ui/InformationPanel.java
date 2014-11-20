package org.aiwolf.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.aiwolf.client.ui.res.AIWolfResource;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Species;
import org.aiwolf.common.data.Status;
import org.aiwolf.common.data.Team;
import org.aiwolf.common.data.Vote;
import org.aiwolf.common.net.GameInfo;
import org.aiwolf.common.net.GameSetting;
import org.aiwolf.common.util.Counter;

public class InformationPanel extends JPanel {

	JPanel titlePanel;
	JPanel agentListPanel;
	JPanel informationPanel;
	
	
	JLabel dayLabel;
	
//	JTextField executedField;
//	JTextField attackedField;
	
//	Map<Agent, JTextField> statusFieldMap;
//	Map<Agent, JTextField> voteFieldMap;
	Map<Agent, AgentPanel> agentPanelMap;
	
//	GridBagLayout gridBagLayout;
	
	GameSetting gameSetting;
	private JLabel remainLabel;
	private JTextArea informationTextArea;
	private String settingInformation;
	
	AIWolfResource resource;
	
	public InformationPanel(AIWolfResource resource) {
		this.resource = resource;
//		setPreferredSize(new Dimension(HumanPlayerFrame.PANEL_WIDTH, AgentPanel.PANEL_HEIGHT*5));
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		titlePanel = new JPanel();
		agentListPanel = new JPanel();
		agentListPanel.setPreferredSize(new Dimension(getWidth(), (int)(AgentPanel.PANEL_HEIGHT*2.2)));
		informationPanel = new JPanel();
		informationPanel.setPreferredSize(new Dimension(getWidth(), (int)(AgentPanel.PANEL_HEIGHT)));
		
		add(titlePanel);
		add(agentListPanel);
//		add(informationPanel);
		
		
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		
		dayLabel = new JLabel("          ");
		dayLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 24));
		remainLabel = new JLabel("          ");
//		executedField = new JTextField("          ");
//		executedField.setEditable(false);
//		attackedField = new JTextField("          ");
//		attackedField.setEditable(false);

		JPanel dayPanel = new JPanel(new FlowLayout());

		URL url=getClass().getClassLoader().getResource("img/aiwolfLogo.png");
		ImageIcon logoIcon = new ImageIcon(url);
		JLabel logoLabel = new JLabel(logoIcon);

		dayPanel.add(logoLabel);
//		dayPanel.add(dayLabel);
		dayLabel.setBorder(new BevelBorder(BevelBorder.RAISED));
		dayPanel.setBackground(new Color(196,196, 196));
		titlePanel.add(dayPanel);
		
//		JPanel remainPanel = new JPanel(new FlowLayout());
//		remainPanel.add(new JLabel("Remains"));
//		remainPanel.add(remainLabel);
//		remainPanel.setAlignmentX(RIGHT_ALIGNMENT);
//		informationPanel.add(remainPanel);
		
//		JPanel executePanel = new JPanel(new FlowLayout());
//		executePanel.add(new JLabel("Executed"));
//		executePanel.add(executedField);
//		executePanel.add(new JLabel("Attacked"));
//		executePanel.add(attackedField);
//		executePanel.setAlignmentX(RIGHT_ALIGNMENT);
//		informationPanel.add(executePanel);
	}

	public void initialize(GameInfo gameInfo, GameSetting gameSetting){
		this.gameSetting = gameSetting;

		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		flowLayout.setAlignOnBaseline(true);
		agentListPanel.setLayout(flowLayout);
		agentListPanel.setBorder(new LineBorder(Color.BLACK));
		agentPanelMap = new HashMap<>();
		for(Agent agent:new TreeSet<Agent>(gameInfo.getAgentList())){
//			AgentPanel panel = new AgentPanel(agent, gameInfo.getRoleMap().get(agent), agent.equals(gameInfo.getAgent()), talkConverter);
			AgentPanel agentPanel = new AgentImagePanel(agent, gameInfo.getRoleMap().get(agent), agent.equals(gameInfo.getAgent()), resource);
			agentListPanel.add(agentPanel);
			agentPanelMap.put(agent, agentPanel);
		}
		
	}
	
	/**
	 * 
	 * @param gameInfo
	 */
	public void update(GameInfo gameInfo) {
		dayLabel.setText("Day "+gameInfo.getDay()+"");
		remainLabel.setText(gameInfo.getAliveAgentList().size()+"");
		
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

	public void setComingOut(Agent agent, Role role) {
		agentPanelMap.get(agent).setComingOut(role);
	}
}
