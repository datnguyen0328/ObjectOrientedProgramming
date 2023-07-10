package gui.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import backend.data.constant.Constant;
import gui.component.RoundedButton;

public class ButtonsPanel extends JPanel {

	private CardLayout containerPanelCardLayout;
	private JPanel containerPanel;
	private int currentPage = 0;
	private int totalButtons;
	private ArrayList<RoundedButton> cardButtons = new ArrayList<>();
	private RoundedButton previousButton;
	private RoundedButton nextButton;
	private RoundedButton backButton;
	
	public ButtonsPanel(CardLayout containerPanelCardLayout, JPanel containerPanel) {
		this.containerPanelCardLayout = containerPanelCardLayout;
		this.containerPanel = containerPanel;

		totalButtons = containerPanel.getComponentCount() - 2;

		backButton = new RoundedButton("");
		backButton.setIcon(new ImageIcon(Constant.BACK_ICON));
		previousButton = new RoundedButton("<");
		nextButton = new RoundedButton(">");

		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				containerPanelCardLayout.show(containerPanel, "jPanel" + currentPage);
				requestFocusInWindow();
			}
		});

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

			updatePanel(this, nextButton, previousButton, cardButtons, 5);
		} else {
			cardButtons.clear();
			int start = 0;
			int end = Math.min(10, totalButtons);

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

			updatePanel(this, nextButton, previousButton, cardButtons, currentPage);
		}
	}

	public void updatePanel(JPanel jPanel, RoundedButton btnNext, RoundedButton btnPrevious,
			ArrayList<RoundedButton> roundedButtons, int myCurrentPage) {
		jPanel.setLayout(new BorderLayout());
		jPanel.removeAll();

		JPanel centerButtonPanel = new JPanel();
		centerButtonPanel.setBackground(new Color(255, 255, 255));
		centerButtonPanel.setLayout(new FlowLayout());
		centerButtonPanel.add(btnPrevious);
		for (RoundedButton roundedButton : roundedButtons) {
			centerButtonPanel.add(roundedButton);
		}
		centerButtonPanel.add(btnNext);

		jPanel.add(backButton, BorderLayout.WEST);
		jPanel.add(centerButtonPanel, BorderLayout.CENTER);

		changeSeclectedButton(roundedButtons, myCurrentPage);
	}

	public void changeDefaultButton() {
		for (RoundedButton roundedButton : cardButtons) {
			roundedButton.setForeground(new Color(9, 15, 57));
			roundedButton.setContentAreaFilled(false);
			roundedButton.setOpaque(false);
			roundedButton.setFocusPainted(false);
		}
	}

	public void changeSeclectedButton(ArrayList<RoundedButton> roundedButtons, int index) {
		changeDefaultButton();

		RoundedButton roundedButton = roundedButtons.get(index);

		roundedButton.changeBackground(new Color(108, 125, 253));
		roundedButton.setForeground(new Color(255, 255, 255));
	}
}
