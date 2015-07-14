package org.aiwolf.client.ui;

import inaba.player.InabaPlayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.aiwolf.client.lib.TemplateTalkFactory.TalkType;
import org.aiwolf.client.lib.Topic;
import org.aiwolf.client.lib.Utterance;
import org.aiwolf.client.ui.res.AIWolfResource;
import org.aiwolf.client.ui.res.JapaneseResource;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Player;
import org.aiwolf.common.data.Species;
import org.aiwolf.common.data.Status;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.data.Team;
import org.aiwolf.common.net.GameInfo;
import org.aiwolf.common.net.GameSetting;
import org.aiwolf.common.util.CalendarTools;
import org.aiwolf.kajiClient.player.KajiRoleAssignPlayer;
import org.aiwolf.server.AIWolfGame;
import org.aiwolf.server.net.DirectConnectServer;
import org.aiwolf.server.util.GameLogger;

public class GameFrame extends JFrame implements GameLogger, ActionListener{

	private static final int ACTION_PANEL_HEIGHT = 30;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException{

		String timeString = CalendarTools.toDateTime(System.currentTimeMillis()).replaceAll("[\\s-/:]", "");
		
		List<Player> list = new ArrayList<Player>();
		for(int i = 0; i < 13; i++){
			list.add(new InabaPlayer());
//			list.add(new KajiRoleAssignPlayer());
		}
		String logDir = "./log";
		
		DirectConnectServer gameServer = new DirectConnectServer(list);
		GameSetting gameSetting = GameSetting.getDefaultGame(list.size());
		AIWolfGame game = new AIWolfGame(gameSetting, gameServer);
//		if(logDir != null){
//			File logFile = new File(String.format("%s/contest%s.log", logDir, timeString));
//			game.setLogFile(logFile);
//		}
		game.setRand(new Random(0));
//		game.setShowConsoleLog(false);
//		game.start();

		GameFrame gf = new GameFrame(new JapaneseResource(), game);
		game.setGameLogger(gf);
	
		game.start();
//		gf.initialize(gameInfo, gameSetting);
//		gf.setVisible(true);

	
	}

	
	public static final int DEFAULT_WAIT_TIME = 500;
	/**
	 * 
	 */
	public static final int PANEL_WIDTH = 960;
	
	/**
	 * 
	 */
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

	/**
	 *  
	 */
	protected boolean isInitialized;
	
	protected AIWolfGame game;
	
	protected InformationPanel infoPanel;
//	protected JPanel agentPanel;
	protected UserActionPanel userActionPanel;
//	protected TalkPanel talkPanel;

	/**
	 * Resource
	 */
	protected AIWolfResource resource;

	/**
	 * Main panel for GUI
	 */
	protected JPanel mainPanel;
	
	protected GameSetting gameSetting;
	
	protected GameInfo gameInfo;

	/**
	 * 
	 */
//	protected boolean skip;
//	protected boolean step;
//	protected long waitTime = DEFAULT_WAIT_TIME;
//	protected JButton autoButton;
//	protected JButton nextButton;
//	protected JButton skipAllButton;
	protected NextButtonPanel stepActionPanel;

	
	/**
	 * 
	 * @param resource
	 * @game 
	 */
	public GameFrame(AIWolfResource resource, AIWolfGame game){
		setGame(game);
		this.resource = resource;
		
		isInitialized = false;
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
		mainPanel.setOpaque(false);
		mainPanel.setDoubleBuffered(true);
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(Color.WHITE);
//		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		infoPanel = new InformationPanel(resource);
		infoPanel.setPreferredSize(new Dimension(HumanPlayer.PANEL_WIDTH, getHeight()-ACTION_PANEL_HEIGHT));
		
//		talkPanel = new TalkPanel(resource);
//		talkPanel.setPreferredSize(new Dimension(HumanPlayer.PANEL_WIDTH, 200));

//		userActionPanel = new UserActionPanel(resource);
//		userActionPanel.setPreferredSize(new Dimension(HumanPlayer.PANEL_WIDTH, ACTION_PANEL_HEIGHT));

		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		JPanel logoPanel = new JPanel(new FlowLayout());
		URL url=getClass().getClassLoader().getResource("img/aiwolfLogo.png");
		ImageIcon logoIcon = new ImageIcon(url);
		JLabel logoLabel = new JLabel(logoIcon);
		logoPanel.add(logoLabel);
		logoPanel.setBackground(new Color(196,196, 196));
//		titlePanel.add(logoLabel);
//		titlePanel.setBackground(new Color(196,196, 196));
		titlePanel.add(logoPanel);

		
		mainPanel.add(titlePanel, BorderLayout.NORTH);
		mainPanel.add(infoPanel, BorderLayout.CENTER);
//		mainPanel.add(talkPanel, BorderLayout.CENTER);
//		mainPanel.add(userActionPanel, BorderLayout.SOUTH);

//		nextButton = new JButton("NEXT");
//		nextButton.addActionListener(this);
//		
//		autoButton = new JButton("Auto");
//		autoButton.addActionListener(this);

		stepActionPanel = new NextButtonPanel();
		stepActionPanel.setPreferredSize(new Dimension(HumanPlayer.PANEL_WIDTH, ACTION_PANEL_HEIGHT));
		this.mainPanel.add(stepActionPanel, BorderLayout.SOUTH);
		
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		

	}
	
	public void initialize(GameInfo gameInfo, GameSetting gameSetting){
		this.gameSetting = gameSetting;

		infoPanel.initialize(gameInfo, gameSetting);
//		userActionPanel.initialize(gameInfo, gameSetting);
//		infoPanel.talkPanel.initialize(gameInfo, gameSetting);
		update(gameInfo);
		
		setVisible(true);
//		infoPanel.setWaitListener(this);
		infoPanel.setWaitListener(stepActionPanel);
		
		infoPanel.firstInformation(gameInfo, gameSetting);
		
//		samplePlayer.initialize(gameInfo, gameSetting);
	}
	
	public void update(GameInfo gameInfo){
		infoPanel.update(gameInfo);
//		infoPanel.talkPanel.update(gameInfo);
//		updateTalk(gameInfo);
//		userActionPanel.update(gameInfo);
//		infoPanel.talkPanel.scrollToTail();
		
		for(Talk talk:gameInfo.getTalkList()){
			Utterance u = new Utterance(talk.getContent());
			if(u.getTopic() == Topic.COMINGOUT){
				infoPanel.setComingOut(talk.getAgent(), u.getRole());
			}
		}
		
		this.gameInfo = gameInfo;

//		repaint();
	}

	
	
	Talk lastTalk;
	Talk lastWhisper;
	/**
	 * update Talk
	 * @param gameInfo
	 */
	protected void updateTalk(GameInfo gameInfo) {
		for(int i = infoPanel.getLastTalkIdx(); i < gameInfo.getTalkList().size(); i++){
			Talk talk = gameInfo.getTalkList().get(i);
			
			if(lastTalk != null && lastTalk.getAgent() == talk.getAgent() && lastTalk.getContent().equals(talk.getContent()) && lastTalk.getDay() == talk.getDay()){
				continue;
			}
			boolean isUpdated = infoPanel.updateTalk(gameInfo.getDay(), talk, TalkType.TALK);
			if(isUpdated){
				infoPanel.update(gameInfo);
				lastTalk = talk;
				stepActionPanel.waitForNext();
//				waitSecond();
			}
		}


		for(int i = infoPanel.getLastWhisperIdx()+1; i < gameInfo.getWhisperList().size(); i++){
			Talk whisper = gameInfo.getWhisperList().get(i);

			if(lastWhisper != null){
				if(lastWhisper.getAgent() == whisper.getAgent() && lastWhisper.getContent().equals(whisper.getContent()) && lastWhisper.getDay() == whisper.getDay()){
					continue;
				}
			}
			
			boolean isUpdated = infoPanel.updateTalk(gameInfo.getDay(), whisper, TalkType.WHISPER);
			if(isUpdated){
				infoPanel.scrollToTail();
				lastWhisper = whisper;
//				waitSecond();
				stepActionPanel.waitForNext();
			}
		}
//		talkPanel.updateWhisper(gameInfo.getDay(), gameInfo.getWhisperList());
	}

	
	public void dayStart() {
		if(gameInfo.getDay() != 0){
			stepActionPanel.auto(false);
		}
//		userActionPanel.clear();
		infoPanel.dayStart(gameInfo);
//		if(infoPanel.voteResult(gameInfo)){
//			waitForNext();
//		}
		
//		userActionPanel.dayStart(gameInfo);
	}
//
//	/**
//	 * Wait until next button is pushed
//	 * 
//	 */
//	public void waitForNext() {
//		stepActionPanel.waitForNext();
////		if(skip){
////			waitSecond();
////			return;
////		}
////		step = false;
////		nextButton.setEnabled(true);
////		while(!step){
////			try {
////				Thread.sleep(10);
////			} catch (InterruptedException e) {
////				e.printStackTrace();
////			}
////		}
////		step = false;
////		nextButton.setEnabled(false);
//	}
//
//	protected void waitSecond(){
//		stepActionPanel.waitSecond();
//
////		try {
////			Thread.sleep(waitTime);
////		} catch (InterruptedException e) {
////			e.printStackTrace();
////		}
//	}
//	
//
//	/**
//	 * @param setAuto
//	 */
//	private void auto(boolean setAuto) {
//		stepActionPanel.auto(setAuto);
////		if(!setAuto){
////			skip = false;
////			step = false;
////			waitTime = DEFAULT_WAIT_TIME;
////			autoButton.setText("Auto");
////		}
////		else{
////			step = true;
////			skip = true;
////			autoButton.setText("Stop");
////		}
//	}
//	

	int lastDay = -1;
	@Override
	public void log(String log) {
		GameInfo gameInfo = game.getGameData().getGameInfo();
		if(!isInitialized){
			initialize(gameInfo, game.getGameSetting());
			isInitialized = true;
		}

		update(gameInfo);
		if(gameInfo.getDay() != lastDay){
			dayStart();
			lastDay = gameInfo.getDay();
		}

		
		updateTalk(gameInfo);
		System.out.println(log);
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() {
//		infoPanel.dayStart(gameInfo);
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
		stepActionPanel.waitSecond();
		infoPanel.scrollToTail();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
//		if(e.getSource() == nextButton){
//			step = true;
//			nextButton.setEnabled(false);
//		}
//		else if(e.getSource() == autoButton){
//			if(!skip){
//				stepActionPanel.auto(true);
//			}
//			else{
//				stepActionPanel.auto(false);
//			}
//		}
//		else if(e.getSource() == skipAllButton){
////			step = true;
////			skip = true;
//			waitTime = 0;
//			stepActionPanel.auto(true);
//		}
//		
	}

	
	/**
	 * @return game
	 */
	public AIWolfGame getGame() {
		return game;
	}

	/**
	 * @param game セットする game
	 */
	public void setGame(AIWolfGame game) {
		this.game = game;
//		if(game.getGameLogger() != null){
//			game.setGameLogger(new MultiGameLogger(game.getGameLogger(), this));
//		}
//		else{
//			game.setGameLogger(this);
//		}
	}

	/**
	 * @return resource
	 */
	public AIWolfResource getResource() {
		return resource;
	}

	/**
	 * @param resource セットする resource
	 */
	public void setResource(AIWolfResource resource) {
		this.resource = resource;
		infoPanel.setResource(resource);
	}

}
