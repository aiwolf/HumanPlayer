package org.aiwolf.client.ui.log;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;
 
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.TransferHandler;

public class GUILogViewerStarter extends JFrame{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private JFrame frame;
	private JPanel mainPanel;
 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUILogViewerStarter window = new GUILogViewerStarter();
					window.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
 
	/**
	 * Create the application.
	 */
	public GUILogViewerStarter() {
		initialize();
	}
 
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
//		JScrollPane scrollPane = new JScrollPane();
//		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		frame.setLayout(new BorderLayout());
		mainPanel = new JPanel(){

			/* (非 Javadoc)
			 * @see javax.swing.JComponent#paint(java.awt.Graphics)
			 */
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Font font = g.getFont();
				Font nfont = new Font(font.getFontName(), NORMAL, 20);
				g.setFont(nfont);
				String text = "Drop LogFile Here!";
				g.drawString(text, 225-nfont.getSize()*text.length()/2, 150);
			}
			
		};
//		scrollPane.setViewportView(mainPanel);
		frame.add(mainPanel, BorderLayout.CENTER);
		// ドロップ操作を有効にする
		mainPanel.setTransferHandler(new DropFileHandler());
	}
	
	public void start(){
		frame.setVisible(true);
	}
 
	
	
	/**
	 * ドロップ操作の処理を行うクラス
	 */
	private class DropFileHandler extends TransferHandler {
 
		/**
		 * ドロップされたものを受け取るか判断 (ファイルのときだけ受け取る)
		 */
		@Override
		public boolean canImport(TransferSupport support) {
			if (!support.isDrop()) {
				// ドロップ操作でない場合は受け取らない
		        return false;
		    }
 
			if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				// ドロップされたのがファイルでない場合は受け取らない
		        return false;
		    }
 
			return true;
		}
 
		/**
		 * ドロップされたファイルを受け取る
		 */
		@SuppressWarnings("unchecked")
		@Override
		public boolean importData(TransferSupport support) {
			// 受け取っていいものか確認する
			if (!canImport(support)) {
		        return false;
		    }
 
			// ドロップ処理
			Transferable t = support.getTransferable();
			try {
				// ファイルを受け取る
				List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
 
				for(final File file:files){

					Runnable runner = new Runnable() {
						
						@Override
						public void run() {
							try {
								GUILogViewer guiLogViewer = new GUILogViewer(file);
								System.out.println(file);
								guiLogViewer.setCloseOnExist(false);
								guiLogViewer.start();
							} catch (NumberFormatException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					};

					Thread th = new Thread(runner);
					th.start();
					break;
				}
			
			} catch (UnsupportedFlavorException | IOException e) {
				e.printStackTrace();
			}
			return true;
		}
	}
	
}
