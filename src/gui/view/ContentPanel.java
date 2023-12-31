package gui.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import backend.data.constant.Constant;
import backend.data.model.dynasty.Dynasty;
import backend.data.model.event.Event;
import backend.data.model.festival.Festival;
import backend.data.model.figure.Figure;
import backend.data.model.relic.Relic;
import backend.data.service.decode.Decode;
import backend.data.service.decode.HelperFunctions;
import backend.data.service.decode.Utils;

public class ContentPanel extends JPanel {
	private int index;
	private String currentPanel;

	private CardLayout contentCardLayout;

	private JPanel searchLandingPanel;
	private JPanel searchResultPanel;

	private JPanel humanPanel;
	private JPanel kingPanel;
	private JPanel poinsettiaPanel;
	private JPanel eventPanel;
	private JPanel dynastyPanel;
	private JPanel festivalPanel;
	private JPanel relicPanel;
	private ArrayList<Figure> figures;
	private JTextArea searchTextArea;
	private JTextArea searchLandingTextArea;
	private JPanel searchLandingContainerPanel;
	private ArrayList<Dynasty> dynasties;
	private ArrayList<Festival> festivals;
	private ArrayList<Relic> relics;
	private ArrayList<Event> events;

	public ArrayList<Event> getEvents() {
		return events;
	}

	public ArrayList<Dynasty> getDynasties() {
		return dynasties;
	}

	public ArrayList<Festival> getFestivals() {
		return festivals;
	}

	public ArrayList<Relic> getRelics() {
		return relics;
	}

	public JPanel getSearchLandingPanel() {
		return searchLandingPanel;
	}

	public void setSearchLandingPanel(JPanel searchLandingPanel) {
		this.searchLandingPanel = searchLandingPanel;
	}

	public JTextArea getSearchLandingTextArea() {
		return searchLandingTextArea;
	}

	public JPanel getSearchLandingContainerPanel() {
		return searchLandingContainerPanel;
	}

	public JTextArea getSearchTextArea() {
		return searchTextArea;
	}

	public ArrayList<Figure> getFigures() {
		return figures;
	}

	public void setCurrentPanel(String currentPanel) {
		this.currentPanel = currentPanel;
	}

	public String getCurrentPanel() {
		return currentPanel;
	}

	public CardLayout getContentCardLayout() {
		return contentCardLayout;
	}

	public JPanel getHumanPanel() {
		return humanPanel;
	}

	public JPanel getKingPanel() {
		return kingPanel;
	}

	public JPanel getPoinsettiaPanel() {
		return poinsettiaPanel;
	}

	public JPanel getEventPanel() {
		return eventPanel;
	}

	public JPanel getDynastyPanel() {
		return dynastyPanel;
	}

	public JPanel getFestivalPanel() {
		return festivalPanel;
	}

	public JPanel getRelicPanel() {
		return relicPanel;
	}

	public ContentPanel(CardLayout contentCardLayout) {
		this.contentCardLayout = contentCardLayout;

		setLayout(contentCardLayout);

		// human
		add(createHumanContentPanel(), "humanPanel");

		// king
		add(createKingContentPanel(), "kingPanel");

		// poinsettia
		add(createPoinsettiaContentPanel(), "poinsettiaPanel");

		// event
		add(createEventContentPanel(), "eventPanel");

		// dynasty
		add(createDynastyContentPanel(), "dynastyPanel");

		// festival
		add(createFestivalContentPanel(), "festivalPanel");

		// relic
		add(createRelicContentPanel(), "relicPanel");

		// searchLandingPanel
		add(createSearchLandingPanel(), "searchLandingPanel");

		// searchPanel
		add(createSearchResultPanel(), "searchResultPanel");
	}

	public JPanel createSearchLandingPanel() {
		searchLandingPanel = new JPanel();
		searchLandingPanel.setLayout(new BorderLayout());

		searchLandingTextArea = new JTextArea();
		searchLandingTextArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
		searchLandingTextArea.setLineWrap(true);
		searchLandingTextArea.setWrapStyleWord(true);
		searchLandingTextArea.setEditable(false);
		
		searchLandingPanel.add(searchLandingTextArea, BorderLayout.NORTH);

		return searchLandingPanel;
	}

	public JPanel createSearchResultPanel() {
		searchResultPanel = new JPanel();
		searchResultPanel.setLayout(new BorderLayout());
		searchResultPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

		searchTextArea = new JTextArea();
		searchTextArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
		searchTextArea.setLineWrap(true);
		searchTextArea.setWrapStyleWord(true);
		searchTextArea.setEditable(false);
		
		JScrollPane searchTextAreaJScrollPane = new JScrollPane(searchTextArea);
		
		searchResultPanel.add(searchTextAreaJScrollPane, BorderLayout.CENTER);
		
		return searchResultPanel;
	}

	public JPanel createHumanContentPanel() {
		index = 0;
		humanPanel = new JPanel();
		humanPanel.setLayout(new BorderLayout());
		setFontStyleForComponents(humanPanel);

		figures = HelperFunctions.decodeFromJson(Constant.FIGURE_FILE_NAME);

		CardLayout humanCardLayout = new CardLayout();
		ContainerPanel humanContainerPanel = new ContainerPanel(humanCardLayout, 50, figures);
		humanPanel.add(humanContainerPanel, BorderLayout.CENTER);

		ButtonsPanel figureButtonsPanel = new ButtonsPanel(humanCardLayout, humanContainerPanel);
		figureButtonsPanel.setBackground(new Color(255, 255, 255));
		figureButtonsPanel.setPreferredSize(new Dimension(10, 45));
		humanPanel.add(figureButtonsPanel, BorderLayout.SOUTH);

		return humanPanel;
	}

	public JPanel createKingContentPanel() {
		index = 0;
		kingPanel = new JPanel();
		kingPanel.setLayout(new BorderLayout());

		ArrayList<Figure> kings = HelperFunctions.decodeFromJson(Constant.KING_FILE_NAME);

		CardLayout kingCardLayout = new CardLayout();
		ContainerPanel kingContainerPanel = new ContainerPanel(kingCardLayout, 5, kings);
		kingPanel.add(kingContainerPanel, BorderLayout.CENTER);

		ButtonsPanel kingButtonsPanel = new ButtonsPanel(kingCardLayout, kingContainerPanel);
		kingButtonsPanel.setBackground(new Color(255, 255, 255));
		kingButtonsPanel.setPreferredSize(new Dimension(10, 45));
		kingPanel.add(kingButtonsPanel, BorderLayout.SOUTH);

		return kingPanel;
	}

	public JPanel createPoinsettiaContentPanel() {
		index = 0;
		poinsettiaPanel = new JPanel();
		poinsettiaPanel.setLayout(new BorderLayout());

		ArrayList<Figure> poinsettias = HelperFunctions.decodeFromJson(Constant.POINSETTIA_FILE_NAME);
		CardLayout poinsettiaCardLayout = new CardLayout();
		ContainerPanel poinsettiaContainerPanel = new ContainerPanel(poinsettiaCardLayout, 5, poinsettias);
		poinsettiaPanel.add(poinsettiaContainerPanel, BorderLayout.CENTER);

		ButtonsPanel poinsettiaButtonsPanel = new ButtonsPanel(poinsettiaCardLayout, poinsettiaContainerPanel);
		poinsettiaButtonsPanel.setBackground(new Color(255, 255, 255));
		poinsettiaButtonsPanel.setPreferredSize(new Dimension(10, 45));
		poinsettiaPanel.add(poinsettiaButtonsPanel, BorderLayout.SOUTH);

		return poinsettiaPanel;
	}

	public JPanel createDynastyContentPanel() {
		index = 0;
		dynastyPanel = new JPanel();
		dynastyPanel.setLayout(new BorderLayout());

		dynasties = HelperFunctions.decodeDynastiesFromJson(Constant.DYNASTY_FILE_NAME);
		CardLayout dynastyCardLayout = new CardLayout();
		ContainerPanel dynastyContainerPanel = new ContainerPanel(dynastyCardLayout, 5, dynasties);
		dynastyPanel.add(dynastyContainerPanel, BorderLayout.CENTER);

		ButtonsPanel dynastyButtonsPanel = new ButtonsPanel(dynastyCardLayout, dynastyContainerPanel);
		dynastyButtonsPanel.setBackground(new Color(255, 255, 255));
		dynastyButtonsPanel.setPreferredSize(new Dimension(10, 45));
		dynastyPanel.add(dynastyButtonsPanel, BorderLayout.SOUTH);

		return dynastyPanel;
	}

	public JPanel createFestivalContentPanel() {
		index = 0;
		festivalPanel = new JPanel();
		festivalPanel.setLayout(new BorderLayout());

		festivals = Utils.festivalDecode(Constant.FESTIVAL_FILE_NAME);
		CardLayout festivalCardLayout = new CardLayout();
		ContainerPanel festivalContainerPanel = new ContainerPanel(festivalCardLayout, 5, festivals);
		festivalPanel.add(festivalContainerPanel, BorderLayout.CENTER);

		ButtonsPanel festivalButtonsPanel = new ButtonsPanel(festivalCardLayout, festivalContainerPanel);
		festivalButtonsPanel.setBackground(new Color(255, 255, 255));
		festivalButtonsPanel.setPreferredSize(new Dimension(10, 45));
		festivalPanel.add(festivalButtonsPanel, BorderLayout.SOUTH);

		return festivalPanel;
	}

	public JPanel createRelicContentPanel() {
		index = 0;
		relicPanel = new JPanel();
		relicPanel.setLayout(new BorderLayout());

		relics = Decode.decode(Constant.RELIC_FILE_NAME);

		CardLayout relicCardLayout = new CardLayout();
		ContainerPanel relicContainerPanel = new ContainerPanel(relicCardLayout, 50, relics);
		relicPanel.add(relicContainerPanel, BorderLayout.CENTER);

		ButtonsPanel relicButtonsPanel = new ButtonsPanel(relicCardLayout, relicContainerPanel);
		relicButtonsPanel.setBackground(new Color(255, 255, 255));
		relicButtonsPanel.setPreferredSize(new Dimension(10, 45));
		relicPanel.add(relicButtonsPanel, BorderLayout.SOUTH);
		return relicPanel;
	}

	public JPanel createEventContentPanel() {
		index = 0;
		eventPanel = new JPanel();
		eventPanel.setLayout(new BorderLayout());

		events = HelperFunctions.decodeEventFromJson(Constant.EVENT_FILE_NAME);

		CardLayout eventCardLayout = new CardLayout();
		ContainerPanel eventContainerPanel = new ContainerPanel(eventCardLayout, 10, events);
		eventPanel.add(eventContainerPanel, BorderLayout.CENTER);

		ButtonsPanel eventButtonsPanel = new ButtonsPanel(eventCardLayout, eventContainerPanel);
		eventButtonsPanel.setBackground(new Color(255, 255, 255));
		eventButtonsPanel.setPreferredSize(new Dimension(10, 45));
		eventPanel.add(eventButtonsPanel, BorderLayout.SOUTH);
		return eventPanel;
	}

	public void setFontStyleForComponents(JPanel jPanel) {
		Font font = new Font("Monospace", Font.PLAIN, 16);

		for (Component component : jPanel.getComponents()) {
			component.setFont(font);
		}
	}
}
