package org.aiwolf.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.aiwolf.client.ui.res.AIWolfResource;
import org.aiwolf.client.ui.res.DefaultResource;
import org.aiwolf.client.ui.res.JapaneseResource;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.data.Vote;
import org.aiwolf.common.net.GameInfo;
import org.aiwolf.common.net.GameSetting;
import org.aiwolf.common.util.Counter;

/**
 * Talk Viewer
 * @author tori
 *
 */
public class TalkPanel2 extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	JTabbedPane dailyTalkPane;

	/**
	 * 
	 */
	JTabbedPane dailyWhisperPane;

	/**
	 * 
	 */
//	Map<Integer, List<Talk>> talkListMap;

	/**
	 * 
	 */
//	Map<Integer, List<Talk>> whisperListMap;

	/**
	 * 
	 */
	Map<Integer, JTextArea> talkAreaMap;
	
	/**
	 * Whisper
	 */
	Map<Integer, JTextArea> whisperAreaMap;

	JPanel talkPanel;
	
	JPanel whisperPanel;
	
	int lastTalkIdx = 0;
	int lastWhisperIdx = 0;

	AIWolfResource talkConverter;
	
	static final Comparator<Vote> voteComparator = new Comparator<Vote>() {
		@Override
		public int compare(Vote o1, Vote o2) {
			return o1.getAgent().compareTo(o2.getAgent());
		}
	};
	
	public TalkPanel2(AIWolfResource talkConverter) {
		this.talkConverter = talkConverter;
//		talkConverter = new DefaultTalkConverter();
		talkConverter = new JapaneseResource();
		
		dailyTalkPane = new JTabbedPane();
//		talkListMap = new TreeMap<Integer, List<Talk>>();
		talkAreaMap = new TreeMap<Integer, JTextArea>();

		talkPanel = new JPanel();
		talkPanel.setLayout(new BoxLayout(talkPanel, BoxLayout.Y_AXIS));
		talkPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Talk History"));
		talkPanel.add(dailyTalkPane);

		dailyWhisperPane = new JTabbedPane();
//		whisperListMap = new TreeMap<Integer, List<Talk>>();
		whisperAreaMap = new TreeMap<Integer, JTextArea>();

		whisperPanel = new JPanel();
		whisperPanel.setLayout(new BoxLayout(whisperPanel, BoxLayout.Y_AXIS));
		whisperPanel.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Whisper History"));
//		whisperPanel.add(new JLabel("Whisper History"));
		whisperPanel.add(dailyWhisperPane);
	}

	public void initialize(GameInfo gameInfo, GameSetting gameSetting){
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(talkPanel);
		if(gameInfo.getRole() == Role.WEREWOLF){
			add(whisperPanel);
		}
		
		String settingInformation = getInitialInformation(gameInfo, gameSetting);
		addText(gameInfo.getDay(), settingInformation);

//		String information = getDailyInformation(gameInfo);
//		addText(gameInfo.getDay(), information);
	}

	/**
	 * Called from update
	 * @param gameInfo
	 */
	public void update(GameInfo gameInfo){
		String information = getDailyInformation(gameInfo);
		addText(gameInfo.getDay(), information);
	}
	
	
	/**
	 * add information to the day panel
	 * @param day
	 * @param information
	 */
	public void addText(int day, String information){
		JTextArea talkArea = getTalkArea(day);
		talkArea.append(information);
		talkArea.append("\n");
		JScrollPane scrollPane = (JScrollPane)dailyTalkPane.getSelectedComponent();
		scrollToTail(scrollPane);
		
	}
	
	/**
	 * create daily information
	 * @param gameInfo
	 * @return
	 */
	protected String getDailyInformation(GameInfo gameInfo) {
		
		StringBuffer buf = new StringBuffer();
		buf.append("Day "+gameInfo.getDay());
		buf.append("\n");
//		buf.append(settingInformation);
//		buf.append("\n");
		
		
		/////////////////////////////////////////////////////////Vote
		TreeSet<Vote> voteSet = new TreeSet<>(voteComparator);
		voteSet.addAll(gameInfo.getVoteList());
		for(Vote vote:voteSet){
//			Agent agent = vote.getAgent();
//			Agent target = vote.getTarget();
//			buf.append(agent+" voted to "+target);
			buf.append(talkConverter.convertVote(vote));
			buf.append("\n");
		}
		if(gameInfo.getExecutedAgent() != null){
			buf.append(talkConverter.convertExecuted(gameInfo.getExecutedAgent()));
			buf.append("\n");
		}

		/////////////////////////////////////////////////////////Attack
		if(gameInfo.getGuardedAgent() != null){
			buf.append(talkConverter.convertGuarded(gameInfo.getGuardedAgent()));
			buf.append("\n");
		}
		TreeSet<Vote> attackVoteSet = new TreeSet<>(voteComparator);
		attackVoteSet.addAll(gameInfo.getAttackVoteList());
		for(Vote vote:attackVoteSet){
//			Agent agent = vote.getAgent();
//			Agent target = vote.getTarget();
//			buf.append(agent+" attacked to "+target);
			buf.append(talkConverter.convertAttackVote(vote));
			buf.append("\n");
		}
		if(gameInfo.getAttackedAgent() != null){
			buf.append(talkConverter.convertAttacked(gameInfo.getAttackedAgent()));
			buf.append("\n");
		}

		/////////////////////////////////////////////////////////Divine Medium
		if(gameInfo.getDivineResult() != null){
			Agent agent = gameInfo.getDivineResult().getAgent();
			Agent target = gameInfo.getDivineResult().getTarget();
			buf.append(talkConverter.convertDivined(gameInfo.getDivineResult()));
			buf.append("\n");
		}
		if(gameInfo.getMediumResult() != null){
			Agent target = gameInfo.getMediumResult().getTarget();
			buf.append(talkConverter.convertMedium(gameInfo.getMediumResult()));
			buf.append("\n");
		}
		
		buf.append(talkConverter.aliveRemain(gameInfo.getAliveAgentList().size()));
		buf.append("\n");

		return buf.toString();
	}

	protected String getInitialInformation(GameInfo gameInfo, GameSetting gameSetting) {
		StringBuffer buf = new StringBuffer();
		Counter<Role> counter = new Counter<Role>(gameSetting.getRoleNumMap());
		for(Role role:counter.getReverseList()){
			if(counter.get(role) == 0){
				continue;
			}
			buf.append(talkConverter.convert(role)+":"+counter.get(role)+", ");
		}
		buf.append("\n");
		buf.append(String.format("%s(%s)\n",talkConverter.convert(gameInfo.getAgent()), talkConverter.convert(gameInfo.getRole())));
		return buf.toString();
	}

	/**
	 * update Talk of Agents
	 * @param gameInfo
	 */
	public void updateTalk(int day, List<Talk> talkList){
		JTextArea talkArea = getTalkArea(day);

		for(int i = lastTalkIdx; i < talkList.size(); i++){
			Talk talk = talkList.get(i);
			if(!talk.getContent().equals(Talk.OVER) && !talk.getContent().equals(Talk.SKIP)){
				String text = String.format("%03d\t%s\t%s\n", talk.getIdx(), talkConverter.convert(talk.getAgent()), talkConverter.convertTalk(talk));
				talkArea.append(text);
			}
		}
		lastTalkIdx = talkList.size();
		
		dailyTalkPane.setSelectedIndex(dailyTalkPane.getComponentCount()-1);
		
		JScrollPane scrollPane = (JScrollPane)dailyTalkPane.getSelectedComponent();
		scrollToTail(scrollPane);

	}

	/**
	 * Scroll Bar to Tail
	 * @param scrollPane
	 */
	protected void scrollToTail(JScrollPane scrollPane) {
		if(scrollPane != null){
			JScrollBar bar = scrollPane.getVerticalScrollBar();
			if(bar != null){
				bar.setValue(bar.getMaximum());
				scrollPane.repaint();
			}
		}
	}

	/**
	 * get Talk TextArea
	 * @param day
	 * @return
	 */
	protected JTextArea getTalkArea(int day) {
		if(!talkAreaMap.containsKey(day)){
			JTextArea talkArea = new JTextArea();
			talkArea.setEditable(false);
			talkArea.setBackground(Color.WHITE);
			talkAreaMap.put(day, talkArea);
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportView(talkAreaMap.get(day));
			dailyTalkPane.addTab("Day"+day, scrollPane);
			lastTalkIdx = 0;
		}
		JTextArea textArea = talkAreaMap.get(day);
		return textArea;
	}
	
	
	/**
	 * 
	 * @param gameInfo
	 */
	public void updateWhisper(int day, List<Talk> whisperList){

		JTextArea textArea = getWhisperArea(day);

		for(int i = lastWhisperIdx; i < whisperList.size(); i++){
			Talk talk = whisperList.get(i);
			if(!talk.getContent().equals(Talk.OVER) && !talk.getContent().equals(Talk.SKIP)){
				String text = String.format("%03d\t%s\t%s\n", talk.getIdx(), talkConverter.convert(talk.getAgent()), talk.getContent());
//				String text = String.format("%03d\t%s\t%s\n", talk.getIdx(), talkConverter.convert(talk.getAgent()), talkConverter.convertTalk(talk));
				textArea.append(text);
			}
		}
		lastWhisperIdx = whisperList.size();
		
//		System.out.println(dailyWhisperPane.getTabCount());
		dailyWhisperPane.setSelectedIndex(dailyWhisperPane.getComponentCount()-1);
		
		JScrollPane scrollPane = (JScrollPane)dailyWhisperPane.getSelectedComponent();
		scrollToTail(scrollPane);

	}

	protected JTextArea getWhisperArea(int day) {
		if(!whisperAreaMap.containsKey(day)){
			JTextArea whisperArea = new JTextArea();
			whisperArea.setEditable(false);
			whisperArea.setBackground(new Color(255,196,196));
			whisperAreaMap.put(day, whisperArea);
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportView(whisperAreaMap.get(day));
			dailyWhisperPane.addTab("Day"+day, scrollPane);
			lastWhisperIdx = 0;
		}
		
		JTextArea textArea = whisperAreaMap.get(day);
		return textArea;
	}
	
}
