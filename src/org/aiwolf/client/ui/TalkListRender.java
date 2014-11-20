package org.aiwolf.client.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

public class TalkListRender implements ListCellRenderer<JComponent> {

	@Override
	public Component getListCellRendererComponent(JList<? extends JComponent> arg0, JComponent panel, int index, boolean isSelected, boolean cellHasFocus) {
//		panel.setBorder(new LineBorder(Color.red, 2));
//		if(isSelected){
//			panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
//		}
//		else{
//			panel.setBorder(new SoftBevelBorder(BevelBorder.RAISED));
//		}
		return panel;
	}

}
