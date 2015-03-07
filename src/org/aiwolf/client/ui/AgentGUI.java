package org.aiwolf.client.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.aiwolf.client.base.smpl.SampleRoleAssignPlayer;
import org.aiwolf.client.ui.res.AIWolfResource;
import org.aiwolf.client.ui.res.DefaultResource;
import org.aiwolf.client.ui.res.JapaneseResource;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Player;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.net.GameInfo;
import org.aiwolf.common.net.GameSetting;
import org.aiwolf.kajiClient.player.KajiRoleAssignPlayer;
import org.aiwolf.server.bin.RoleRequestStarter;

/**
 * @deprecated
 * @author tori
 *
 */
public class AgentGUI extends HumanPlayer implements ActionListener{

	/**
	 * Start Human Agent Starter
	 * 
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
		Role role = null;
//		Role role = Role.WEREWOLF;
		Player player = new KajiRoleAssignPlayer();
		AgentGUI gui = new AgentGUI(new JapaneseResource(), player);
//		gui.setVisible(true);
		String defaultClassName = SampleRoleAssignPlayer.class.getName();
//		String defaultClassName = InabaPlayer.class.getName();
//		String logDir = null;
		String logDir = "./log/";
		int agentNum = 7;
		
		for(int i = 0; i < args.length; i++){
			if(args[i].startsWith("-")){
				if(args[i].equals("-r")){
					i++;
					role = Role.valueOf(args[i]);
				}
				else if(args[i].equals("-n")){
					i++;
					agentNum = Integer.parseInt(args[i]);
				}
				else if(args[i].equals("-d")){
					i++;
					defaultClassName = args[i];
				}
				else if(args[i].equals("-l")){
					i++;
					logDir = args[i];
				}
			}
		}
		
		RoleRequestStarter.start(gui, role, agentNum, defaultClassName, logDir);
//		start(humanPlayer, role, agentNum, defaultClassName, logDir);
	}
	
	Player player;
	
	JPanel stepActionPanel;
	JButton skipAllButton;
	JButton stepButton;
	boolean step = false;
	int waitTime = 1000;
	boolean skip = false;
	
	public AgentGUI(Player player) {
		this(new DefaultResource(), player);
	}

	public AgentGUI(AIWolfResource resource, Player player) {
		super(resource);
		this.player = player;
		
		this.mainPanel.remove(userActionPanel);
		
		stepButton = new JButton("NEXT");
		stepButton.addActionListener(this);
		skipAllButton = new JButton("SKIP ALL");
		skipAllButton.addActionListener(this);
		stepActionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		stepActionPanel.add(stepButton);
		stepActionPanel.add(skipAllButton);
		
		this.mainPanel.add(stepActionPanel, BorderLayout.SOUTH);
	}
	
	

	public String getName() {
		return player.getName();
	}

	public void update(GameInfo gameInfo) {
		super.update(gameInfo);
		player.update(gameInfo);
//		waitForStep();
	}
	
	/**
	 * 
	 * @param gameInfo
	 */
	protected void updateTalk(GameInfo gameInfo) {
		for(int i = infoPanel.getLastTalkIdx(); i < gameInfo.getTalkList().size(); i++){
			List<Talk> subList = gameInfo.getTalkList().subList(0, i+1);
			boolean isUpdated = infoPanel.updateTalk(gameInfo.getDay(), subList);
			if(isUpdated){
				waitForNext();
//				waitSecond();
			}
		}
//		talkPanel.updateTalk(gameInfo.getDay(), gameInfo.getTalkList());
		for(int i = infoPanel.getLastWhisperIdx(); i < gameInfo.getWhisperList().size(); i++){
			List<Talk> subList = gameInfo.getWhisperList().subList(0, i+1);
			boolean isUpdated = infoPanel.updateWhisper(gameInfo.getDay(), subList);
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
		infoPanel.scrollToTail();
//		talkPanel.updateWhisper(gameInfo.getDay(), gameInfo.getWhisperList());
	}
	public void initialize(GameInfo gameInfo, GameSetting gameSetting) {
		player.initialize(gameInfo, gameSetting);
		super.initialize(gameInfo, gameSetting);
	}

	public void dayStart() {
		super.dayStart();
		player.dayStart();
		waitForNext();
	}

	public String talk() {
		return player.talk();
	}

	public String whisper() {
		return player.whisper();
	}

	public Agent vote() {
//		waitForNext();
		return player.vote();
	}

	public Agent attack() {
		return player.attack();
	}

	public Agent divine() {
		return player.divine();
	}

	public Agent guard() {
		return player.guard();
	}

	public void finish() {
		super.finish();
		player.finish();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == stepButton){
			step = true;
			stepButton.setEnabled(false);
		}
		else if(e.getSource() == skipAllButton){
			step = true;
			skip = true;
			waitTime = 0;
		}
		
	}
	
	private void waitForNext() {
		if(skip){
			return;
		}
		step = false;
		stepButton.setEnabled(true);
		while(!step){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		stepButton.setEnabled(false);
	}

	protected void waitSecond(){
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
