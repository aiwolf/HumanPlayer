package org.aiwolf.client.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.aiwolf.client.lib.TemplateTalkFactory.TalkType;
import org.aiwolf.client.lib.Topic;
import org.aiwolf.client.lib.Utterance;
import org.aiwolf.client.ui.res.AIWolfResource;
import org.aiwolf.client.ui.res.DefaultResource;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Player;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Species;
import org.aiwolf.common.data.Status;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.data.Team;
import org.aiwolf.common.net.GameInfo;
import org.aiwolf.common.net.GameSetting;

public class HumanPlayer extends JFrame implements Player, WaitListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 79019670290568874L;

	public static final int PANEL_WIDTH = 960;
	public static final int PANEL_HEIGHT = 800;

	/**
	 * Color of werewolves
	 */
	public static final Color WHISPER_COLOR = new Color(255, 128, 128);

	/**
	 * Color of Villegers
	 */
	public static final Color TALK_COLOR = Color.WHITE;

	/**
	 * color of action and information
	 */
	public static final Color ACTION_COLOR = new Color(128, 255, 128);

	/**
	 * Color of player agent
	 */
	public static final Color PLAYER_COLOR = new Color(255, 255, 128);
	
	/**
	 * Color of agent who have same role
	 */
	public static final Color FRIEND_COLOR = new Color(255, 255, 196);

	/**
	 * Color of agent who have same role
	 */
	public static final Color IMPORTANT_COLOR = new Color(240, 240, 255);

	InformationPanel infoPanel;
	JPanel agentPanel;
	UserActionPanel userActionPanel;

	/**
	 * 自動ボタン
	 */
	NextButtonPanel nextButtonPanel;

//	TalkPanel talkPanel;
	
	GameInfo gameInfo;
	GameSetting gameSetting;
	
//	SampleRoleAssignPlayer samplePlayer;
	
	/**
	 * Num of Talk
	 */
	int remainTalk;

	/**
	 * Num of Whisper
	 */
	private int remainWhisper;

	/**
	 * Resource
	 */
	AIWolfResource resource;

	/**
	 * Main panel for GUI
	 */
	protected JPanel mainPanel;

	private CardLayout buttonCardLayout;

	private JPanel userActionAreaPanel;

	/**
	 * 
	 */
	public HumanPlayer(){
		this(new DefaultResource());
	}
	
	/**
	 * 
	 * @param resource
	 */
	public HumanPlayer(AIWolfResource resource){
		this.resource = resource;
//		samplePlayer = new SampleRoleAssignPlayer();
		try{
			  UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
			  SwingUtilities.updateComponentTreeUI(this);
			}catch(Exception e){
			  e.printStackTrace();
		}
		
		setBounds(10, 10, PANEL_WIDTH, PANEL_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainPanel = new JPanel();
//		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		mainPanel.setLayout(new BorderLayout());

		infoPanel = new InformationPanel(resource);
		userActionAreaPanel = new JPanel();
		buttonCardLayout = new CardLayout();
		userActionAreaPanel.setLayout(buttonCardLayout);
		
		userActionPanel = new UserActionPanel(resource);
		userActionPanel.setPreferredSize(new Dimension(HumanPlayer.PANEL_WIDTH, 50));

		nextButtonPanel = new NextButtonPanel();
		nextButtonPanel.setPreferredSize(new Dimension(HumanPlayer.PANEL_WIDTH, 50));

		userActionAreaPanel.setPreferredSize(new Dimension(HumanPlayer.PANEL_WIDTH, 50));
		userActionAreaPanel.add(userActionPanel);
		userActionAreaPanel.add(nextButtonPanel);
		//		mainPanel.add(infoPanel, BorderLayout.NORTH);
////		mainPanel.add(talkPanel, BorderLayout.CENTER);
//		mainPanel.add(userActionPanel, BorderLayout.SOUTH);
		mainPanel.add(infoPanel, BorderLayout.CENTER);
//		mainPanel.add(userActionPanel, BorderLayout.SOUTH);
		mainPanel.add(userActionAreaPanel, BorderLayout.SOUTH);
		
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		
		infoPanel.setWaitListener(this);
		
		
//		stepActionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//		nextButton = new JButton("NEXT");
//		nextButton.addActionListener(this);
//		autoButton = new JButton("Auto");
//		autoButton.addActionListener(this);
//		skipAllButton = new JButton("SKIP ALL");
//		skipAllButton.addActionListener(this);
//		stepActionPanel.add(nextButton);
//		stepActionPanel.add(autoButton);
//		stepActionPanel.add(skipAllButton);
		
	}

	@Override
	public void initialize(GameInfo gameInfo, GameSetting gameSetting){
		this.gameSetting = gameSetting;
		infoPanel.initialize(gameInfo, gameSetting);
		userActionPanel.initialize(gameInfo, gameSetting);
//		talkPanel.initialize(gameInfo, gameSetting);
		setVisible(true);
		update(gameInfo);
		
//		samplePlayer.initialize(gameInfo, gameSetting);
	}
	
	@Override
	public void update(GameInfo gameInfo){
		infoPanel.update(gameInfo);
//		talkPanel.update(gameInfo);
		updateTalk(gameInfo);
		userActionPanel.update(gameInfo);
		infoPanel.scrollToTail();
		
		for(Talk talk:gameInfo.getTalkList()){
			Utterance u = new Utterance(talk.getContent());
			if(u.getTopic() == Topic.COMINGOUT){
				infoPanel.setComingOut(talk.getAgent(), u.getRole());
			}
		}
		
		this.gameInfo = gameInfo;

		repaint();
	}

	
	
	/**
	 * 
	 * @param gameInfo
	 */
	protected void updateTalk(GameInfo gameInfo) {
//		infoPanel.updateTalk(gameInfo.getDay(), gameInfo.getTalkList());
//		infoPanel.updateWhisper(gameInfo.getDay(), gameInfo.getWhisperList());
		for(int i = infoPanel.getLastTalkIdx()+1; i < gameInfo.getTalkList().size(); i++){
//			List<Talk> subList = gameInfo.getTalkList().subList(0, i+1);
//			boolean isUpdated = infoPanel.updateTalk(gameInfo.getDay(), subList);
			boolean isUpdated = infoPanel.updateTalk(gameInfo.getDay(), gameInfo.getTalkList().get(i), TalkType.TALK);
			if(isUpdated){
//				waitForNext();
				infoPanel.scrollToTail();
				waitForNext();
				infoPanel.clearArrow();
			}
		}


		for(int i = infoPanel.getLastWhisperIdx()+1; i < gameInfo.getWhisperList().size(); i++){
//			List<Talk> subList = gameInfo.getWhisperList().subList(0, i+1);
//			boolean isUpdated = infoPanel.updateWhisper(gameInfo.getDay(), subList);
			boolean isUpdated = infoPanel.updateTalk(gameInfo.getDay(), gameInfo.getWhisperList().get(i), TalkType.WHISPER);
			if(isUpdated){
				infoPanel.scrollToTail();
				waitForNext();
				infoPanel.clearArrow();
//				waitForNext();
			}
		}
//		talkPanel.updateWhisper(gameInfo.getDay(), gameInfo.getWhisperList());
	}

	@Override
	public void waitForNext() {
		
		buttonCardLayout.last(userActionAreaPanel);
//		mainPanel.remove(userActionPanel);
//		mainPanel.add(nextButtonPanel, BorderLayout.SOUTH);
//		System.out.println("Add Next Button Panel");

//		nextButtonPanel.repaint();

		nextButtonPanel.waitForNext();

		buttonCardLayout.first(userActionAreaPanel);

		userActionPanel.repaint();

//		System.out.println("Add User Action Panel");
		
	}

	@Override
	public String talk(){
//		System.out.println("Call Talk!");
		userActionPanel.setRemainTalk(remainTalk);
		userActionPanel.talk();
		remainTalk--;
		remainWhisper = gameSetting.getMaxTalk();
//		System.out.println(buttonPanel.getTalk());
		return userActionPanel.getTalk();
	}

	@Override
	public void dayStart() {
		if(gameInfo.getDay() != 0){
			nextButtonPanel.auto(false);
		}
		
		userActionPanel.clear();
		infoPanel.dayStart(gameInfo);
		userActionPanel.dayStart(gameInfo);

		remainTalk = gameSetting.getMaxTalk();
		
//		repaint();

	}

	@Override
	public String whisper() {
		int wolfNum = 0;
		for(Agent agent:gameInfo.getAliveAgentList()){
			if(gameInfo.getRoleMap().get(agent) == Role.WEREWOLF){
				wolfNum++;
			}
		}
		
		if(wolfNum > 1){
			userActionPanel.setRemainWhisper(remainWhisper);
			userActionPanel.whisper();
			remainWhisper--;
	//		System.out.println(buttonPanel.getTalk());
			return userActionPanel.getTalk();
		}
		else{
			return Talk.OVER;
		}
	}

	@Override
	public Agent vote() {
		userActionPanel.action();
		return userActionPanel.getVote();
	}

	@Override
	public Agent attack() {
		userActionPanel.action();
		return userActionPanel.getAttack();
	}

	@Override
	public Agent divine() {
		userActionPanel.action();
		return userActionPanel.getDivine();
	}

	@Override
	public Agent guard() {
		userActionPanel.action();
		return userActionPanel.getGuard();
	}

	@Override
	public void finish() {
		infoPanel.dayStart(gameInfo);
		int humanSide = 0;
		int wolfSide = 0;
		for(Agent agent:gameInfo.getAgentList()){
			if(gameInfo.getStatusMap().get(agent) == Status.DEAD){
				continue;
			}
			if(gameInfo.getRoleMap().get(agent).getSpecies() == Species.HUMAN){
				humanSide++;
			}
			else{
				wolfSide++;
			}
		}

		Team winner = null;
		if(wolfSide == 0){
			winner = Team.VILLAGER;
		}
		else if(wolfSide >= humanSide){
			winner = Team.WEREWOLF;
		}
		infoPanel.setWinner(gameInfo.getDay(), winner);
	}

	/**
	 * @return talkConverter
	 */
	public AIWolfResource getTalkConverter() {
		return resource;
	}

	/**
	 * @param talkConverter セットする talkConverter
	 */
	public void setTalkConverter(AIWolfResource talkConverter) {
		this.resource = talkConverter;
	}


	
	
}
