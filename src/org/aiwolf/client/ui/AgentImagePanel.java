package org.aiwolf.client.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import org.aiwolf.client.ui.res.AIWolfResource;
import org.aiwolf.client.ui.util.ResourceReader;
import org.aiwolf.common.data.Agent;
import org.aiwolf.common.data.Role;

public class AgentImagePanel extends AgentPanel {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	ImageIcon imageIcon;
	JLabel iconLabel;
	
	static public final int IMG_WIDTH = 40;
	
	public AgentImagePanel(Agent agent, Role role, boolean isPlayer, AIWolfResource resource) {
		init(agent, role, isPlayer, resource);

		imageIcon = resource.getImageIcon(agent);

		int h = imageIcon.getIconHeight();
		int w = imageIcon.getIconWidth();
		if(h > w){
			h = IMG_WIDTH*h/w;
			w = IMG_WIDTH;
		}
		imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(w, h, Image. SCALE_SMOOTH));
		
		iconLabel = new JLabel(imageIcon);
		addLabel();
//		setSize(w, h);
//		
//		Graphics g = getGraphics();
//		g.drawImage(imageIcon.getImage(), 0, 0, this);
		
	}
	
	protected void addLabel() {
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		
		layout.putConstraint(SpringLayout.NORTH, nameLabel, 0, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, nameLabel, 0, SpringLayout.WEST, this);

		layout.putConstraint(SpringLayout.NORTH, iconLabel, 1, SpringLayout.SOUTH, nameLabel);
		layout.putConstraint(SpringLayout.WEST, iconLabel, 0, SpringLayout.WEST, nameLabel);

		layout.putConstraint(SpringLayout.NORTH, statusLabel, 0, SpringLayout.NORTH, iconLabel);
		layout.putConstraint(SpringLayout.WEST, statusLabel, 1, SpringLayout.EAST, iconLabel);

		layout.putConstraint(SpringLayout.NORTH, roleLabel, 0, SpringLayout.SOUTH, statusLabel);
		layout.putConstraint(SpringLayout.WEST, roleLabel, 1, SpringLayout.EAST, iconLabel);

		layout.putConstraint(SpringLayout.NORTH, coLabel, 0, SpringLayout.SOUTH, roleLabel);
		layout.putConstraint(SpringLayout.WEST, coLabel, 1, SpringLayout.EAST, iconLabel);

		layout.putConstraint(SpringLayout.NORTH, infoLabel, 0, SpringLayout.SOUTH, iconLabel);
		layout.putConstraint(SpringLayout.WEST, infoLabel, 0, SpringLayout.WEST, this);

		add(nameLabel);
		add(iconLabel);
		add(statusLabel);
		add(roleLabel);
		add(coLabel);
		add(infoLabel);
		
	}

}
