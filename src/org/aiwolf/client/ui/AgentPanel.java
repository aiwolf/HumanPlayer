package org.aiwolf.client.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import org.aiwolf.client.ui.res.AIWolfResource;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;
import org.aiwolf.common.data.Status;
import org.aiwolf.common.data.Team;

public class AgentPanel extends JPanel {
	
	private static final Color FRIEND_ALIVE = HumanPlayer.FRIEND_COLOR;
	
	private static final Color NON_PLAYER_ALIVE = Color.WHITE;

	private static final Color PLAYER_ALIVE = HumanPlayer.PLAYER_COLOR;

	private static final Color WOLF_ALIVE = new Color(240, 240, 96);

	private static final Color SKILLED_ALIVE = new Color(255, 220, 160);

	private static final Color NON_PLAYER_DEAD = new Color(128, 128, 128);

	private static final Color FRIEND_DEAD = new Color(128, 128, 64);

	private static final Color PLAYER_DEAD = new Color(196, 196, 64);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static final int PANEL_WIDTH = 100;


	public static final int PANEL_HEIGHT = 100;



	protected Agent agent;
	protected Role role;
	protected Status status;
	
	protected JLabel nameLabel;
	protected JLabel statusLabel;
	protected JLabel infoLabel;


	protected JLabel roleLabel;
	protected JLabel coLabel;


	protected boolean isPlayer;

	AIWolfResource resource;
	public AgentPanel(Agent agent, Role role, boolean isPlayer, AIWolfResource resource) {
		init(agent, role, isPlayer, resource);

		addLabel();
		
	}


	/**
	 * Initialize
	 * @param agent
	 * @param role
	 * @param isPlayer
	 * @param resource
	 */
	protected void init(Agent agent, Role role, boolean isPlayer, AIWolfResource resource) {
		this.agent = agent;
		this.role = role;
		this.resource = resource;
		
		nameLabel = new JLabel(agent.getAgentIdx()+":"+resource.convert(agent));
		statusLabel = new JLabel(" ");
		infoLabel = new JLabel(" ");
		roleLabel = new JLabel();
		coLabel = new JLabel(" ");
		if(role != null){
			roleLabel.setText(resource.convert(role));
		}
		
//		Border border = new BevelBorder(BevelBorder.RAISED);
//		setBorder(border);
		
		Dimension dimension = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
		setSize(dimension);
		setPreferredSize(dimension);
		setMaximumSize(dimension);
		setMinimumSize(dimension);
		
		this.isPlayer = isPlayer;
		setStatus(Status.ALIVE);
	}



	protected void addLabel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(nameLabel);
		add(statusLabel);
		add(roleLabel);
		add(coLabel);
		add(infoLabel);
	}

	
	
	protected AgentPanel() {
	}



	/**
	 * @return agent
	 */
	public Agent getAgent() {
		return agent;
	}

	
	/**
	 * 
	 * @param status
	 */
	public void setStatus(Status status) {
		this.status = status;
		statusLabel.setText(status.toString());
		if(status == Status.DEAD){
			if(isPlayer){
				setBackground(PLAYER_DEAD);
			}
			else if(role != null){
				setBackground(FRIEND_DEAD);
			}
			else{
				setBackground(NON_PLAYER_DEAD);
			}
			Border border = new BevelBorder(BevelBorder.LOWERED);
			setBorder(border);
		}
		else{
			if(isPlayer){
				setBackground(PLAYER_ALIVE);
			}
			else if(role != null){
				setBackground(FRIEND_ALIVE);
			}
			else if(role == Role.WEREWOLF){
				setBackground(WOLF_ALIVE);
			}
			else{
				setBackground(NON_PLAYER_ALIVE);
			}
			Border border = new BevelBorder(BevelBorder.RAISED);
			setBorder(border);
		}
	}
	
	public void setInformation(String information){
		infoLabel.setText(information);
	}
	
	public void setRole(Role role){
		if(role != null){
			roleLabel.setText(resource.convert(role));
			if(role.getTeam() == Team.WEREWOLF){
				roleLabel.setForeground(Color.RED);
			}
				
			if(role == Role.WEREWOLF && status == Status.ALIVE){
				setBackground(WOLF_ALIVE);
			}
			else if(role != Role.VILLAGER && status == Status.ALIVE){
				setBackground(SKILLED_ALIVE);
			}
		}
	}

	public void setComingOut(Role role){
		if(role != null){
			coLabel.setText("CO:"+resource.convert(role));
		}
	}


	public void setAttacked(int day) {
		setInformation(resource.convertAttackedDay(day));
	}


	public void setExecuted(int day) {
		setInformation(resource.convertExecutedDay(day));
	}
}
