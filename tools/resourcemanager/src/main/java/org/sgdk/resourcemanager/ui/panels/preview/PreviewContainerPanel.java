package org.sgdk.resourcemanager.ui.panels.preview;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sgdk.resourcemanager.entities.SGDKElement;
import org.sgdk.resourcemanager.ui.ResourceManagerFrame;

public class PreviewContainerPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SoundPlayer soundPlayer;
	private PreviewPanel previewPanel;
	private PreviewToolbar toolBar;
	
	private static final Logger logger = LogManager.getLogger("UILogger");
	
	public PreviewContainerPanel(ResourceManagerFrame parent) throws IOException {
		super(new GridBagLayout());		
		setBorder(BorderFactory.createTitledBorder("Preview"));
		
		soundPlayer = new SoundPlayer();
		previewPanel = new PreviewPanel();
		toolBar = new PreviewToolbar(previewPanel, soundPlayer);
		
		GridBagConstraints c = new GridBagConstraints();
		
		JScrollPane scrollPaneProjectExplorerTree = new JScrollPane(previewPanel);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.weighty = 1.0/12.0;	
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		add(toolBar, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 11.0/12.0;	
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 11;
		add(scrollPaneProjectExplorerTree, c);
	}
	
	public void setPreview(SGDKElement sgdkElement) {
		cleanPanel();
		stopSound();
		toolBar.clean();
		try {
			switch(sgdkElement.getType()) {
			case SGDKBackground:
			case SGDKSprite:
				paintImage(sgdkElement);
				toolBar.showImageButtons();
				break;
			case SGDKEnvironmentSound:
			case SGDKFXSound:
				paintImage(sgdkElement);
				playSound(sgdkElement);
				toolBar.showSoundButtons();
				break;
			case SGDKFolder:
			case SGDKProject:
				break;
			default:
				break;				
			}
		} catch (IOException e) {
			logger.error("", e);	
		}
	}

	private void stopSound() {
		try {
			soundPlayer.stopSound();
		}catch (Exception e) {
			logger.error("", e);
		}
	}

	private void playSound(SGDKElement sgdkElement) {			
		try {
			soundPlayer.playSound(sgdkElement);
        } catch (Exception e) {
        	logger.error("", e);
        }	     
	}

	private void paintImage(SGDKElement sgdkElement) throws IOException {
		switch (sgdkElement.getType()) {
		case SGDKBackground:
		case SGDKSprite:
			previewPanel.paintImage(ImageIO.read(new File(sgdkElement.getPath())));
			break;
		case SGDKEnvironmentSound:
		case SGDKFXSound:
		case SGDKFolder:
		case SGDKProject:
			previewPanel.paintImage(((ImageIcon)sgdkElement.getIcon()).getImage());
			previewPanel.setZoom(previewPanel.getZoom() * 5);
			break;
		default:
			break;		
		}
	}

	private void cleanPanel() {
		previewPanel.clean();
	}

}
