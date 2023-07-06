package gui.view;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class ButtonsPanel extends JPanel{

	private CardLayout containerPanelCardLayout;
    private JPanel containerPanel;
    private int currentPage = 0;
    private int totalButtons;
    private ArrayList<RoundedButton> cardButtons = new ArrayList<>();
	private RoundedButton previousButton;
	private RoundedButton nextButton;
    
    public ButtonsPanel(CardLayout containerPanelCardLayout, JPanel containerPanel) {
    	this.containerPanelCardLayout = containerPanelCardLayout;
    	this.containerPanel = containerPanel;
    	
    	
    	
    	setLayout(new FlowLayout());
    	
    	totalButtons = containerPanel.getComponentCount() - 2;
    	
    	previousButton = new RoundedButton("<");
		nextButton = new RoundedButton(">");
		
		previousButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentPage > 0) {
					currentPage--;
					containerPanelCardLayout.show(containerPanel, "jPanel" + currentPage);
					updateCardButtons();
				}
				requestFocusInWindow();
			}
		});

		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (currentPage < totalButtons - 1) {
					currentPage++;
					containerPanelCardLayout.show(containerPanel, "jPanel" + currentPage);
					updateCardButtons();
				}
				requestFocusInWindow();
			}
		});
		
		updateCardButtons();
    }
    
    public void updateCardButtons() {
		if (currentPage >= 6) {
			cardButtons.clear();

			int start = currentPage - 5;
			int end = Math.min(currentPage + 5, totalButtons);

			for (int i = start; i < end; i++) {
				RoundedButton cardButton = new RoundedButton(i + 1 + "");
				String jPanelNameString = "jPanel" + i;
				int myCurrentPage = i;

				cardButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						containerPanelCardLayout.show(containerPanel, jPanelNameString);
						currentPage = myCurrentPage;
						updateCardButtons();
						
						requestFocusInWindow();
					}
				});

				cardButtons.add(cardButton);
			}

			updatePanel(this, nextButton, previousButton, cardButtons);
		} else {
			cardButtons.clear();
			int start = 0;
			int end = Math.min(currentPage + 5, totalButtons);

			for (int i = start; i < end; i++) {
				RoundedButton cardButton = new RoundedButton(i + 1 + "");
				String jPanelNameString = "jPanel" + i;
				int myCurrentPage = i;

				cardButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						containerPanelCardLayout.show(containerPanel, jPanelNameString);
						currentPage = myCurrentPage;
						updateCardButtons();
						
						requestFocusInWindow();
					}
				});

				cardButtons.add(cardButton);
			}

			updatePanel(this, nextButton, previousButton, cardButtons);
		}
	}

	public void updatePanel(JPanel jPanel, RoundedButton btnNext, RoundedButton btnPrevious, ArrayList<RoundedButton> roundedButtons) {
		jPanel.removeAll();
		jPanel.add(btnPrevious);
		for (RoundedButton roundedButton : roundedButtons) {
			jPanel.add(roundedButton);
		}
		jPanel.add(btnNext);
	}
}
