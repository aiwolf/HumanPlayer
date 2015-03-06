package org.aiwolf.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

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
import org.aiwolf.server.util.MultiGameLogger;

public class GameFrame extends JFrame implements GameLogger, ActionListener{

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
		for(int i = 0; i < 15; i++){
			list.add(new KajiRoleAssignPlayer());
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
	protected JPanel agentPanel;
	protected UserActionPanel userActionPanel;
	protected TalkPanel talkPanel;

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

	protected boolean skip;

	/**
	 * 
	 */
	protected boolean step;

	protected long waitTime = DEFAULT_WAIT_TIME;

	protected JButton autoButton;
	
	protected JButton nextButton;

	protected JButton skipAllButton;

	protected JPanel stepActionPanel;

	
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
//		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		mainPanel.setLayout(new BorderLayout());

		infoPanel = new InformationPanel(resource);
//		infoPanel.setPreferredSize(new Dimension(HumanPlayerFrame.PANEL_WIDTH, AgentPanel.PANEL_HEIGHT*5));
		talkPanel = new TalkPanel(resource);
		talkPanel.setPreferredSize(new Dimension(HumanPlayer.PANEL_WIDTH, 200));
//		talkPanel.setMaximumSize(new Dimension(HumanPlayerFrame.PANEL_WIDTH, 200));
		userActionPanel = new UserActionPanel(resource);
		userActionPanel.setPreferredSize(new Dimension(HumanPlayer.PANEL_WIDTH, 50));
		
		mainPanel.add(infoPanel, BorderLayout.NORTH);
		mainPanel.add(talkPanel, BorderLayout.CENTER);
		mainPanel.add(userActionPanel, BorderLayout.SOUTH);

		getContentPane().add(mainPanel, BorderLayout.CENTER);
	
		nextButton = new JButton("NEXT");
		nextButton.addActionListener(this);
		
		autoButton = new JButton("Auto");
		autoButton.addActionListener(this);

		skipAllButton = new JButton("SKIP ALL");
		skipAllButton.addActionListener(this);
		stepActionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		stepActionPanel.add(nextButton);
		stepActionPanel.add(autoButton);
		stepActionPanel.add(skipAllButton);
		step = false;
		skip = false;
		
		this.mainPanel.add(stepActionPanel, BorderLayout.SOUTH);

	
	}
	
	public void initialize(GameInfo gameInfo, GameSetting gameSetting){
		this.gameSetting = gameSetting;
		infoPanel.initialize(gameInfo, gameSetting);
		userActionPanel.initialize(gameInfo, gameSetting);
		talkPanel.initialize(gameInfo, gameSetting);
		setVisible(true);
		update(gameInfo);
		
//		samplePlayer.initialize(gameInfo, gameSetting);
	}
	
	public void update(GameInfo gameInfo){
		infoPanel.update(gameInfo);
		talkPanel.update(gameInfo);
//		updateTalk(gameInfo);
		userActionPanel.update(gameInfo);
		talkPanel.scrollToTail();
		
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
		for(int i = talkPanel.getLastTalkIdx(); i < gameInfo.getTalkList().size(); i++){
			List<Talk> subList = gameInfo.getTalkList().subList(0, i+1);
			boolean isUpdated = talkPanel.updateTalk(gameInfo.getDay(), subList);
			if(isUpdated){
				waitForNext();
//				waitSecond();
			}
		}
//		talkPanel.updateTalk(gameInfo.getDay(), gameInfo.getTalkList());
		for(int i = talkPanel.getLastWhisperIdx(); i < gameInfo.getWhisperList().size(); i++){
			List<Talk> subList = gameInfo.getWhisperList().subList(0, i+1);
			boolean isUpdated = talkPanel.updateWhisper(gameInfo.getDay(), subList);
			if(isUpdated){
//				waitSecond();
				waitForNext();
			}
		}
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		talkPanel.scrollToTail();
//		talkPanel.updateWhisper(gameInfo.getDay(), gameInfo.getWhisperList());
	}

	
	public void dayStart() {
		userActionPanel.clear();
		talkPanel.dayStart(gameInfo);
		userActionPanel.dayStart(gameInfo);
	}

	/**
	 * Wait until next button is pushed
	 */
	protected void waitForNext() {
		if(skip){
			waitSecond();
			return;
		}
		step = false;
		nextButton.setEnabled(true);
		while(!step){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		step = false;
		nextButton.setEnabled(false);
	}

	protected void waitSecond(){
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

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
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void close() {
		talkPanel.dayStart(gameInfo);
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

		talkPanel.setWinner(gameInfo.getDay(), winner);
		waitSecond();
		talkPanel.scrollToTail();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == nextButton){
			step = true;
			nextButton.setEnabled(false);
		}
		else if(e.getSource() == autoButton){
			if(skip){
				skip = false;
				step = false;
				waitTime = DEFAULT_WAIT_TIME;
				autoButton.setText("Auto");
			}
			else{
				step = true;
				skip = true;
				autoButton.setText("Stop");
			}
		}
		else if(e.getSource() == skipAllButton){
			step = true;
			skip = true;
			waitTime = 0;
		}
		
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

}
