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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import org.aiwolf.client.lib.Topic;
import org.aiwolf.client.lib.Utterance;
import org.aiwolf.client.lib.TemplateTalkFactory.TalkType;
import org.aiwolf.client.ui.res.AIWolfResource;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Status;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.data.Team;
import org.aiwolf.common.data.Vote;
import org.aiwolf.common.net.GameInfo;
import org.aiwolf.common.net.GameSetting;

public class InformationPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final public static Color PLAYER_COLOR = HumanPlayer.PLAYER_COLOR;
	final public static Color TALK_COLOR = HumanPlayer.TALK_COLOR;
	final public static Color ACTION_COLOR = HumanPlayer.ACTION_COLOR;
	final public static Color WHISPER_COLOR = HumanPlayer.WHISPER_COLOR;
	final public static Color FRIEND_COLOR = HumanPlayer.FRIEND_COLOR;
	final public static Color FOCUS_COLOR = ACTION_COLOR;
	
//	protected JPanel titlePanel;
	protected EventPanel eventPanel;
	protected TalkPanel talkPanel;
//	private JPanel informationPanel;
	
	private Map<Agent, AgentPanel> agentPanelMap;
	
	private GameSetting gameSetting;
//	private JLabel remainLabel;
	
	private AIWolfResource resource;
	
	GameInfo gameInfo;

	WaitListener waitListener;
	
	static final Comparator<Vote> voteComparator = new Comparator<Vote>() {
		@Override
		public int compare(Vote o1, Vote o2) {
			if(o1.getTarget() == o2.getTarget()){
				return o1.getAgent().compareTo(o2.getAgent());
			}
			else{
				return o1.getTarget().compareTo(o2.getTarget());
			}
		}
	};


	
	public InformationPanel(AIWolfResource resource) {
		this.resource = resource;

		setLayout(new BorderLayout());

		eventPanel = new EventPanel();
		talkPanel = new TalkPanel(resource);

		add(talkPanel, BorderLayout.CENTER);
		add(eventPanel, BorderLayout.NORTH);

//		JPanel mainPanel = new JPanel(new BorderLayout());
//		mainPanel.setOpaque(false);
//		mainPanel.add(talkPanel, BorderLayout.CENTER);
//		mainPanel.add(eventPanel, BorderLayout.NORTH);
//
//		add(mainPanel, BorderLayout.CENTER);

	}

	public void initialize(GameInfo gameInfo, GameSetting gameSetting){
		this.gameSetting = gameSetting;
		this.gameInfo = gameInfo;

//		FlowLayout flowLayout = new FlowLayout();
//		flowLayout.setAlignment(FlowLayout.LEFT);
//		flowLayout.setAlignOnBaseline(true);
//		agentListPanel.setLayout(flowLayout);
		eventPanel.setBorder(new LineBorder(Color.BLACK));
		agentPanelMap = new HashMap<>();
		List<AgentPanel> panelList = new ArrayList<AgentPanel>();
		for(Agent agent:new TreeSet<Agent>(gameInfo.getAgentList())){
//			AgentPanel panel = new AgentPanel(agent, gameInfo.getRoleMap().get(agent), agent.equals(gameInfo.getAgent()), talkConverter);
			AgentPanel agentPanel = new AgentImagePanel(agent, gameInfo.getRoleMap().get(agent), agent.equals(gameInfo.getAgent()), resource);
			panelList.add(agentPanel);
			
			eventPanel.add(agentPanel);
			agentPanelMap.put(agent, agentPanel);
		}
		
		((EventPanel)eventPanel).setAgentPanelList(panelList);

		talkPanel.initialize(gameInfo, gameSetting);
//		inform(String.format("%s\n",resource.getFirstText(gameInfo.getAgent(), gameInfo.getRole())), HumanPlayer.PLAYER_COLOR);

	}
	
	public void firstInformation(GameInfo gameInfo, GameSetting gameSetting){
		inform(resource.getRoleInformation(gameSetting.getRoleNumMap()), PLAYER_COLOR);
		
	}
	
	/**
	 * 
	 * @param gameInfo
	 */
	public void update(GameInfo gameInfo) {
		this.gameInfo = gameInfo;
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
	 * @param day
	 * @param talkList
	 * @return
	 * @see org.aiwolf.client.ui.TalkPanel#updateTalk(int, java.util.List)
	 */
	public boolean updateTalk(int day, List<Talk> talkList) {
		return talkPanel.updateTalk(day, talkList);
	}
	
	/**
	 * 
	 * @param day
	 * @param talk
	 * @param talkType
	 * @return
	 */
	public boolean updateTalk(int day, Talk talk, TalkType talkType) {
		if(!talk.getContent().equals(Talk.OVER) && !talk.getContent().equals(Talk.SKIP)){
			talkPanel.addTalk(day, talk, talkType);
			if(talkType == TalkType.TALK){
				talkPanel.lastTalkIdx = talk.getIdx();
			}
			else{
				talkPanel.lastWhisperIdx = talk.getIdx();
			}
			JPanel tp = talkPanel.createTalkPanel(talk, talkType);
			eventPanel.addCenterItem(tp);

			agentPanelMap.get(talk.getAgent()).setBackground(FOCUS_COLOR);
			
			Utterance u = new Utterance(talk.getContent());
			
			if(talkType == TalkType.TALK){
				switch(u.getTopic()){
				case ATTACK:
				case DIVINED:
				case INQUESTED:
				case GUARDED:
				case VOTE:
					eventPanel.addArrow(talk.getAgent(), u.getTarget());
					break;
				default:
					break;
				}
			}			
			return true;
		}
		return false;
	}
	
//	public boolean updateWhisper(int day, Talk talk) {
//		if(!talk.getContent().equals(Talk.OVER) && !talk.getContent().equals(Talk.SKIP)){
//			talkPanel.addTalk(day, talk, TalkType.WHISPER);
//			talkPanel.lastWhisperIdx = talk.getIdx();
//			return true;
//		}
//		return false;
//	}

	

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
//		talkPanel.dayStart(gameInfo);
		
		
		int day = gameInfo.getDay();
		talkPanel.addText(day, resource.dayStart(gameInfo.getDay()));

		///////////////////////////////////////////////////////
		//Vote
		TreeSet<Vote> voteSet = new TreeSet<>(voteComparator);
		voteSet.addAll(gameInfo.getVoteList());
		for(Vote vote:voteSet){
			Color color = HumanPlayer.TALK_COLOR;
			if(vote.getAgent() == gameInfo.getAgent()){
				color = PLAYER_COLOR;
			}
			else if(gameInfo.getRoleMap().get(vote.getAgent()) == gameInfo.getRole()){
				color = FRIEND_COLOR;
			}
			
			talkPanel.addText(day, resource.convertVote(vote), color);
		}
		if(gameInfo.getExecutedAgent() != null){
			inform(gameInfo.getExecutedAgent(), resource.convertExecuted(gameInfo.getExecutedAgent()), ACTION_COLOR);
		}

		///////////////////////////////////////////////////////
		//Divine Medium
		if(gameInfo.getDivineResult() != null){
//			Agent agent = gameInfo.getDivineResult().getAgent();
			Agent target = gameInfo.getDivineResult().getTarget();
			
			inform(target, resource.convertDivined(gameInfo.getDivineResult()), ACTION_COLOR);
		}
		if(gameInfo.getMediumResult() != null){
			Agent target = gameInfo.getMediumResult().getTarget();
			inform(target, resource.convertMedium(gameInfo.getMediumResult()), ACTION_COLOR);
//			talkPanel.addAgentInformation(day, target, resource.convertMedium(gameInfo.getMediumResult()));
		}
		
		///////////////////////////////////////////////////////
		//Attack
		TreeSet<Vote> attackVoteSet = new TreeSet<>(voteComparator);
		attackVoteSet.addAll(gameInfo.getAttackVoteList());
		for(Vote attackVote:attackVoteSet){
			if(gameInfo.getStatusMap().get(attackVote.getAgent()) == Status.ALIVE){
				talkPanel.addText(day, resource.convertAttackVote(attackVote), WHISPER_COLOR);
			}
		}
		///////////////////////////////////////////////////////
		//Guard
		if(gameInfo.getGuardedAgent() != null){
			inform(gameInfo.getGuardedAgent(), resource.convertGuarded(gameInfo.getGuardedAgent()), ACTION_COLOR);
//			talkPanel.addAgentInformation(day, gameInfo.getGuardedAgent(), resource.convertGuarded(gameInfo.getGuardedAgent()));
		}

		if(gameInfo.getAttackedAgent() != null){
			inform(gameInfo.getAttackedAgent(), resource.convertAttacked(gameInfo.getAttackedAgent()), WHISPER_COLOR);
		}
		else if(gameInfo.getDay() > 1){
			inform(resource.convertAttacked(gameInfo.getAttackedAgent()), PLAYER_COLOR);
		}
//		addText(day, attackText.toString(), HumanPlayer.FRIEND_COLOR);
		

		talkPanel.addText(day, resource.aliveRemain(gameInfo.getAliveAgentList().size()));

	}



	protected void inform(String text,Color color) {
		inform(null, text, color);
	}

	/**
	 * 
	 * @param agent
	 * @param text
	 * @param color
	 */
	protected void inform(Agent agent, String text,Color color) {
		int day = gameInfo.getDay();
		if(agent != null){
			JPanel panel = talkPanel.createLogPanel(day, agent, text, color);
			talkPanel.addItem(day, panel);
			JPanel panel2 = talkPanel.createLogPanel(day, agent, text, color);
			eventPanel.addCenterItem(panel2);
		}
		else{
			JTextArea textArea = talkPanel.createTextPanel(day, text, color);
			talkPanel.addItem(day, textArea);
			JTextArea textArea2 = talkPanel.createTextPanel(day, text, color);
			eventPanel.addCenterItem(textArea2);
		}

		if(waitListener != null){
			waitListener.waitForNext();
		}
		
	}

	
	public void setWinner(int day, Team winner) {
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		Color color = PLAYER_COLOR;
		if(gameInfo.getRole() != null && winner != gameInfo.getRole().getTeam()){
			if(winner == Team.WEREWOLF){
				color = HumanPlayer.WHISPER_COLOR;
			}
			else{
				color = HumanPlayer.TALK_COLOR;
			}
		}
		inform(resource.convertWinner(winner), color);
		scrollToTail();
//		dailyTalkPane.setSelectedIndex(dailyTalkPane.getComponentCount()-1);
//		
//		talkPanel.setWinner(day, winner);
		
	}

	/**
	 * @return waitListener
	 */
	public WaitListener getWaitListener() {
		return waitListener;
	}

	/**
	 * @param waitListener セットする waitListener
	 */
	public void setWaitListener(WaitListener waitListener) {
		this.waitListener = waitListener;
	}

	public void clearArrow() {
		eventPanel.clearArrow();
		for(AgentPanel ap:agentPanelMap.values()){
			ap.setStatus(gameInfo.getStatusMap().get(ap.getAgent()));
		}
	}

	

}
