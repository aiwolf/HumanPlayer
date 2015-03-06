package org.aiwolf.client.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ItemListener;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SpringLayout;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.xml.ws.handler.MessageContext.Scope;

import org.aiwolf.client.lib.Topic;
import org.aiwolf.client.lib.Utterance;
import org.aiwolf.client.lib.TemplateTalkFactory.TalkType;
import org.aiwolf.client.ui.res.AIWolfResource;
import org.aiwolf.client.ui.res.JapaneseResource;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Talk;
import org.aiwolf.common.data.Team;
import org.aiwolf.common.data.Vote;
import org.aiwolf.common.net.GameInfo;
import org.aiwolf.common.net.GameSetting;
import org.aiwolf.common.util.Counter;

/**
 * Talk Viewer
 * @author tori
 *
 */
public class TalkPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Width of image
	 */
	private static final int IMG_WIDTH = 40;
	
	/**
	 * currentGameInfo
	 */
	GameInfo gameInfo;
	
	/**
	 * 
	 */
	Map<Integer, GameInfo> gameInfoMap;
	
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
	Map<Integer, JList> talkAreaMap;
	
	/**
	 * Whisper
	 */
	Map<Integer, JTextArea> whisperAreaMap;

	JPanel talkPanel;
	
	JPanel whisperPanel;
	
	int lastTalkIdx = 0;
	int lastWhisperIdx = 0;

	AIWolfResource resource;
	Map<Agent, ImageIcon> imageIconMap;

	TalkItemListener itemListener;
	
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

	public TalkPanel(AIWolfResource resource) {
		this.resource = resource;
//		talkConverter = new DefaultTalkConverter();
		resource = new JapaneseResource();
		imageIconMap = new HashMap<Agent, ImageIcon>();
		gameInfoMap = new HashMap<>();
		
		dailyTalkPane = new JTabbedPane();
//		talkListMap = new TreeMap<Integer, List<Talk>>();
		talkAreaMap = new TreeMap<Integer, JList>();

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
		this.gameInfo = gameInfo;
		gameInfoMap.put(gameInfo.getDay(), gameInfo);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(talkPanel);
//		if(gameInfo.getRole() == Role.WEREWOLF){
//			add(whisperPanel);
//		}
		
		Counter<Role> counter = new Counter<Role>(gameSetting.getRoleNumMap());
		addText(gameInfo.getDay(), resource.getRoleInformation(counter));

		addAgentInformation(gameInfo.getDay(), gameInfo.getAgent(), String.format("%s\n",resource.getFirstText(gameInfo.getAgent(), gameInfo.getRole())), HumanPlayer.PLAYER_COLOR);

		
//		String information = getDailyInformation(gameInfo);
//		addText(gameInfo.getDay(), information);
	}

	/**
	 * Called from update
	 * @param gameInfo
	 */
	public void dayStart(GameInfo gameInfo){
		this.gameInfo = gameInfo;
		gameInfoMap.put(gameInfo.getDay(), gameInfo);

		int day = gameInfo.getDay();
//		buf.append("Day "+gameInfo.getDay());
//		buf.append("\n");
		
		addText(day, "Day "+gameInfo.getDay());
		
		///////////////////////////////////////////////////////
		//Vote
//		StringBuffer voteText = new StringBuffer();
		TreeSet<Vote> voteSet = new TreeSet<>(voteComparator);
		voteSet.addAll(gameInfo.getVoteList());
		for(Vote vote:voteSet){
			Color color = HumanPlayer.TALK_COLOR;
			if(vote.getAgent() == gameInfo.getAgent()){
				color = HumanPlayer.PLAYER_COLOR;
			}
			else if(gameInfo.getRoleMap().get(vote.getAgent()) == gameInfo.getRole()){
				color = HumanPlayer.FRIEND_COLOR;
			}
			
			addText(day, resource.convertVote(vote), color);
//			voteText.append(resource.convertVote(vote));
//			voteText.append("\n");
		}
		if(gameInfo.getExecutedAgent() != null){
//			voteText.append(resource.convertExecuted(gameInfo.getExecutedAgent()));
			addAgentInformation(day, gameInfo.getExecutedAgent(), resource.convertExecuted(gameInfo.getExecutedAgent()));
		}
//		addText(day, voteText.toString());
		
		///////////////////////////////////////////////////////
		//Attack
		if(gameInfo.getGuardedAgent() != null){
			addAgentInformation(day, gameInfo.getGuardedAgent(), resource.convertGuarded(gameInfo.getGuardedAgent()));
		}
		///////////////////////////////////////////////////////
		//Guard
		TreeSet<Vote> attackVoteSet = new TreeSet<>(voteComparator);
		attackVoteSet.addAll(gameInfo.getAttackVoteList());
		for(Vote attackVote:attackVoteSet){
//			Agent agent = vote.getAgent();
//			Agent target = vote.getTarget();
//			buf.append(agent+" attacked to "+target);
			addText(day, resource.convertAttackVote(attackVote), HumanPlayer.WHISPER_COLOR);
//			attackText.append("\n");
		}
//		addText(day, attackText.toString(), HumanPlayer.FRIEND_COLOR);
		if(gameInfo.getAttackedAgent() != null){
			addAgentInformation(day, gameInfo.getAttackedAgent(), resource.convertAttacked(gameInfo.getAttackedAgent()));
		}
		else if(gameInfo.getDay() > 1){
			addText(day, resource.convertAttacked(gameInfo.getAttackedAgent()));
		}
		
		///////////////////////////////////////////////////////
		//Divine Medium
		if(gameInfo.getDivineResult() != null){
//			Agent agent = gameInfo.getDivineResult().getAgent();
			Agent target = gameInfo.getDivineResult().getTarget();
			
			addAgentInformation(day, target, resource.convertDivined(gameInfo.getDivineResult()));
//			divineMediumText.append(resource.convertDivined(gameInfo.getDivineResult()));
//			divineMediumText.append("\n");
		}
		if(gameInfo.getMediumResult() != null){
			Agent target = gameInfo.getMediumResult().getTarget();
			addAgentInformation(day, target, resource.convertMedium(gameInfo.getMediumResult()));
		}
		
		StringBuffer divineMediumText = new StringBuffer();
		divineMediumText.append(resource.aliveRemain(gameInfo.getAliveAgentList().size()));
//		divineMediumText.append("\n");

		addText(day, divineMediumText.toString());
	}
	
	/**
	 * Add Item to TalkList
	 * @param day
	 * @param component
	 */
	public void addItem(int day, JComponent component) {
		JList talkList = getTalkArea(day);
		DefaultListModel<JComponent> listModel = (DefaultListModel<JComponent>) talkList.getModel();
		listModel.addElement(component);
		JScrollPane scrollPane = (JScrollPane)dailyTalkPane.getSelectedComponent();
		scrollToTail(scrollPane);
		
		if(itemListener != null){
			itemListener.addItem(component);
		}
	}

	/**
	 * add information to the day panel
	 * @param day
	 * @param information
	 */
	public void addText(int day, String information){
		addText(day, information, HumanPlayer.ACTION_COLOR);
	}
	
	/**
	 * add information to the day panel
	 * @param day
	 * @param information
	 * @param color 
	 */
	public void addText(int day, String information, Color color){
		if(information != null && !information.trim().isEmpty()){
			JTextArea textArea = new JTextArea(information);
//			textArea.setBorder(new LineBorder(Color.BLACK, 1));
			textArea.setBackground(color);
			addItem(day, textArea);
		}
	}

	/**
	 * add information to the day panel
	 * @param day
	 * @parma agent targetAgent
	 * @param information
	 */
	public void addAgentInformation(int day, Agent agent, String information){
		addAgentInformation(day, agent, information, HumanPlayer.ACTION_COLOR);
	}
	
	/**
	 * add information to the day panel
	 * @param day
	 * @parma agent targetAgent
	 * @param information
	 * @param color 
	 */
	public void addAgentInformation(int day, Agent agent, String information, Color color){
		if(information != null && !information.trim().isEmpty()){
			JPanel logPanel = new JPanel();
			SpringLayout layout = new SpringLayout();
			logPanel.setLayout(layout);

			ImageIcon imageIcon = getAgentIcon(agent);
			JLabel iconLabel = new JLabel(imageIcon);
			JTextArea textLabel = new JTextArea(information);
			textLabel.setBackground(color);
			
			layout.putConstraint(SpringLayout.NORTH, iconLabel, 0, SpringLayout.NORTH, logPanel);
			layout.putConstraint(SpringLayout.WEST, iconLabel, 0, SpringLayout.WEST, logPanel);

			layout.putConstraint(SpringLayout.NORTH, textLabel, 0, SpringLayout.NORTH, iconLabel);
			layout.putConstraint(SpringLayout.WEST, textLabel, 0, SpringLayout.EAST, iconLabel);
			
			logPanel.add(iconLabel);
			logPanel.add(textLabel);
			
			logPanel.setBackground(color);
			
			int width = talkAreaMap.get(day).getWidth();
			int height = Math.max(imageIcon.getIconHeight(), (textLabel.getHeight()+1));
			
			logPanel.setSize(width, height);
			logPanel.setPreferredSize(new Dimension(width, height));

			logPanel.setVisible(true);
//			panel.setBorder(new LineBorder(Color.BLACK, 1));
			addItem(day, logPanel);
		}
	}

	
	/**
	 * Add talk to day panel
	 * @param day
	 * @param talk
	 */
	public void addTalk(int day, Talk talk, TalkType talkType) {
		JPanel talkPanel = createTalkPanel(talk, talkType);
		
		addItem(day, talkPanel);
	}

	/**
	 * Create Talk Panel from Talk and TalkType
	 * @param day
	 * @param talk
	 * @param talkType
	 * @return
	 */
	public JPanel createTalkPanel(Talk talk, TalkType talkType) {
		Utterance utterance = new Utterance(talk.getContent());

		JPanel talkPanel = new JPanel();
		SpringLayout layout = new SpringLayout();
		talkPanel.setLayout(layout);

		ImageIcon imageIcon = getAgentIcon(talk.getAgent());
		
		JLabel iconLabel = new JLabel(imageIcon);
		JLabel nameLabel = new JLabel(String.format("%03d:%s", talk.getIdx(), resource.convert(talk.getAgent())));
		StringBuffer text = new StringBuffer();
		if(talkType == TalkType.TALK){
			text.append(String.format("%s", resource.convertTalk(talk)));
		}
		else{
			text.append(String.format("%s", resource.convertWhisper(talk)));
		}
		
		if(utterance.getTopic() == Topic.AGREE || utterance.getTopic() == Topic.DISAGREE){
			GameInfo gi = gameInfoMap.get(utterance.getTalkDay());
			if(utterance.getTalkType() == TalkType.TALK){
				Talk reference = gi.getTalkList().get(utterance.getTalkID());
				text.append("\n");
				text.append(String.format(" > %03d:%s", reference.getIdx(), resource.convert(reference.getAgent())));
				text.append("\n");
				text.append(" > "+resource.convertTalk(reference));
			}
			else{
				Talk reference = gi.getWhisperList().get(utterance.getTalkID());
				text.append("\n");
				text.append(String.format(" > %03d:%s", reference.getIdx(), resource.convert(reference.getAgent())));
				text.append("\n");
				text.append(" > "+resource.convertWhisper(reference));
			}
		}
		
		JTextArea textArea = new JTextArea(text.toString());
		textArea.setBackground(new Color(0,0,0,0));
		textArea.setFont(nameLabel.getFont());
		
		layout.putConstraint(SpringLayout.NORTH, iconLabel, 0, SpringLayout.NORTH, talkPanel);
		if(talkType == TalkType.TALK){
			layout.putConstraint(SpringLayout.WEST, iconLabel, 0, SpringLayout.WEST, talkPanel);
		}
		else{
			layout.putConstraint(SpringLayout.WEST, iconLabel, 30, SpringLayout.WEST, talkPanel);
		}
				
		
		layout.putConstraint(SpringLayout.NORTH, nameLabel, 0, SpringLayout.NORTH, iconLabel);
		layout.putConstraint(SpringLayout.WEST, nameLabel, 0, SpringLayout.EAST, iconLabel);

		layout.putConstraint(SpringLayout.NORTH, textArea, 0, SpringLayout.SOUTH, nameLabel);
		layout.putConstraint(SpringLayout.WEST, textArea, 0, SpringLayout.EAST, iconLabel);
		
		
		if(talkType == TalkType.WHISPER){
			talkPanel.setBackground(HumanPlayer.WHISPER_COLOR);
		}
		else if(talk.getAgent() == gameInfo.getAgent()){
			talkPanel.setBackground(HumanPlayer.PLAYER_COLOR);
		}
		else if(gameInfo.getRoleMap().get(talk.getAgent()) == gameInfo.getRole()){
			talkPanel.setBackground(HumanPlayer.FRIEND_COLOR);
		}
		else if(utterance.getTopic() == Topic.COMINGOUT || utterance.getTopic() == Topic.DIVINED || utterance.getTopic() == Topic.GUARDED || utterance.getTopic() == Topic.INQUESTED){
			talkPanel.setBackground(HumanPlayer.IMPORTANT_COLOR);
		}
		else{
			talkPanel.setBackground(HumanPlayer.TALK_COLOR);
		}
		
		talkPanel.add(iconLabel);
		talkPanel.add(nameLabel);
		talkPanel.add(textArea);
		

		int width = getTalkArea(0).getWidth();
		int height = Math.max(imageIcon.getIconHeight(), (nameLabel.getHeight()+textArea.getHeight()));
		
		talkPanel.setSize(width, height);
		talkPanel.setPreferredSize(new Dimension(width, height));
		talkPanel.setVisible(true);
		talkPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
		
		return talkPanel;
	}

	/**
	 * 
	 * @param agent
	 * @return
	 */
	protected ImageIcon getAgentIcon(Agent agent) {
		if(!imageIconMap.containsKey(agent)){
			ImageIcon imageIcon = resource.getImageIcon(agent);
			int h = imageIcon.getIconHeight();
			int w = imageIcon.getIconWidth();
			if(h > w){
				h = IMG_WIDTH*h/w;
				w = IMG_WIDTH;
			}
			imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(w, h, Image. SCALE_SMOOTH));
			imageIconMap.put(agent, imageIcon);
		}
		ImageIcon imageIcon = imageIconMap.get(agent);
		return imageIcon;
	}
	

	/**
	 * update Talk of Agents
	 * 
	 * @param gameInfo
	 * @return is updated
	 */
	public boolean updateTalk(int day, List<Talk> talkList){
//		JTextArea talkArea = getTalkArea(day);

		boolean isUpdated = false;
		for(int i = lastTalkIdx; i < talkList.size(); i++){
			Talk talk = talkList.get(i);
//			if(!talk.getContent().equals(Talk.OVER)){
			if(!talk.getContent().equals(Talk.OVER) && !talk.getContent().equals(Talk.SKIP)){
				addTalk(day, talk, TalkType.TALK);
//				addText(day, text);
//				talkArea.append(text);
				isUpdated = true;
			}
		}
		lastTalkIdx = talkList.size();
		
		dailyTalkPane.setSelectedIndex(dailyTalkPane.getComponentCount()-1);
		
		JScrollPane scrollPane = (JScrollPane)dailyTalkPane.getSelectedComponent();
		scrollToTail(scrollPane);

		return isUpdated;
	}

	
	/**
	 * 
	 * @param gameInfo
	 */
	public boolean updateWhisper(int day, List<Talk> whisperList){

		boolean isUpdated = false;
		for(int i = lastWhisperIdx; i < whisperList.size(); i++){
			Talk whisper = whisperList.get(i);
			if(!whisper.getContent().equals(Talk.OVER) && !whisper.getContent().equals(Talk.SKIP)){
				addTalk(day, whisper, TalkType.WHISPER);
				isUpdated = true;
			}
		}
		lastWhisperIdx = whisperList.size();
		
//		System.out.println(dailyWhisperPane.getTabCount());
		dailyWhisperPane.setSelectedIndex(dailyWhisperPane.getComponentCount()-1);
		
		JScrollPane scrollPane = (JScrollPane)dailyWhisperPane.getSelectedComponent();
		scrollToTail(scrollPane);
		return isUpdated;
	}
	/**
	 * scroll last panel to tail
	 * @param day
	 */
	public void scrollToTail(){
		repaint();
		JScrollPane scrollPane = (JScrollPane)dailyTalkPane.getComponent(dailyTalkPane.getComponentCount()-1);
		scrollToTail(scrollPane);
	}
	/**
	 * Scroll Bar to Tail
	 * @param scrollPane
	 */
	protected void scrollToTail(JScrollPane scrollPane) {
		if(scrollPane != null){
			JScrollBar bar = scrollPane.getVerticalScrollBar();
//			JViewport viewPort = scrollPane.getViewport();
//			viewPort.set
			if(bar != null){
				bar.setValue(bar.getMaximum());
				scrollPane.repaint();
//				System.err.println(bar.getMaximum()+"\t"+bar.getValue());
			}
		}
	}

	/**
	 * get Talk TextArea
	 * @param day
	 * @return
	 */
	protected JList getTalkArea(int day) {
		if(!talkAreaMap.containsKey(day)){
			DefaultListModel<JComponent> model = new DefaultListModel<>(); 
			JList talkArea = new JList<>(model);
			talkArea.setCellRenderer(new TalkListRender());
			
			talkArea.setBackground(Color.WHITE);
			talkAreaMap.put(day, talkArea);
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportView(talkAreaMap.get(day));
			dailyTalkPane.addTab("Day"+day, scrollPane);
			lastTalkIdx = 0;
		}
		JList textArea = talkAreaMap.get(day);
		return textArea;
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

	public void setWinner(int day, Team winner) {
		
		Color color = HumanPlayer.PLAYER_COLOR;
		if(gameInfo.getRole() != null && winner != gameInfo.getRole().getTeam()){
			if(winner == Team.WEREWOLF){
				color = HumanPlayer.WHISPER_COLOR;
			}
			else{
				color = HumanPlayer.TALK_COLOR;
			}
		}
		addText(day, resource.convertWinner(winner), color);
		scrollToTail();
		dailyTalkPane.setSelectedIndex(dailyTalkPane.getComponentCount()-1);
	}

	public void update(GameInfo gameInfo) {
		this.gameInfo = gameInfo;
		gameInfoMap.put(gameInfo.getDay(), gameInfo);
	}

	public int getLastTalkIdx() {
		return lastTalkIdx;
	}

	public int getLastWhisperIdx() {
		return lastWhisperIdx;
	}

	/**
	 * @return itemListener
	 */
	public TalkItemListener getItemListener() {
		return itemListener;
	}

	/**
	 * @param itemListener セットする itemListener
	 */
	public void setItemListener(TalkItemListener itemListener) {
		this.itemListener = itemListener;
	}
	
}
