package gui.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import backend.data.constant.*;

import gui.controller.JMenuController;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import java.awt.Insets;

import java.awt.CardLayout;

public class MainView extends JFrame {

	private JPanel contentPane;
	private ArrayList<JMenu> jMenus;
	private ArrayList<JMenuItem> menuItems;
	private JMenuBar menuBar;
	private JMenu humanMenu;
	private JMenu eventMenu;
	private JMenu dynastyMenu;
	private JMenu siteMenu;
	private JMenu festivalMenu;
	private CardLayout contentCardLayout;
	private ContentPanel contentPanel;

	public ContentPanel getContentPanel() {
		return contentPanel;
	}

	public CardLayout getContentCardLayout() {
		return contentCardLayout;
	}

	public JMenu getHumanMenu() {
		return humanMenu;
	}

	public JMenu getEventMenu() {
		return eventMenu;
	}

	public ArrayList<JMenuItem> getMenuItems() {
		return menuItems;
	}

	public JMenu getDynastyMenu() {
		return dynastyMenu;
	}

	public JMenu getSiteMenu() {
		return siteMenu;
	}

	public JMenu getFestivalMenu() {
		return festivalMenu;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView frame = new MainView();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
					frame.setJMenuBar(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1034, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel();
		contentPane.add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new BorderLayout());

		// menu
		JPanel menuPanel = new JPanel();
		menuPanel.setBackground(new Color(9, 15, 57));
		menuPanel.setPreferredSize(new Dimension(10, 55));
		mainPanel.add(menuPanel, BorderLayout.NORTH);
		menuPanel.setLayout(new BorderLayout(0, 0));

		JLabel menuLabel = new JLabel("LỊCH SỬ");
		menuLabel.setBorder(new EmptyBorder(0, 0, 0, 40));
		menuLabel.setIcon(new ImageIcon(Constant.HISTORY_ICON));
		menuLabel.setForeground(Color.WHITE);
		menuLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
		menuPanel.add(menuLabel, BorderLayout.WEST);

		menuBar = new JMenuBar();
		menuBar.setBorder(new EmptyBorder(0, 0, 0, 0));
		menuBar.setMargin(new Insets(0, 10, 0, 0));
		menuBar.setOpaque(false);
		menuBar.setBackground(new Color(9, 15, 57));
		menuPanel.add(menuBar, BorderLayout.CENTER);

		UIManager.put("Menu.selectionBackground", new Color(108, 125, 253));

		humanMenu = new JMenu("Nhân vật");
		humanMenu.setForeground(new Color(100, 107, 134));
		humanMenu.setBorder(new EmptyBorder(0, 20, 0, 20));
		humanMenu.setFont(new Font("SansSerif", Font.PLAIN, 20));
		menuBar.add(humanMenu);

		JMenuItem humanMenuItem = new JMenuItem("Nhân vật");
		humanMenuItem.setBorder(new EmptyBorder(0, 0, 0, 0));
		humanMenuItem.setForeground(new Color(100, 107, 134));
		humanMenuItem.setFont(new Font("SansSerif", Font.PLAIN, 16));
		humanMenuItem.setBackground(new Color(9, 15, 57));
		humanMenu.add(humanMenuItem);

		JMenuItem kingMenuItem = new JMenuItem("Vua");
		kingMenuItem.setBorder(null);
		kingMenuItem.setBackground(new Color(9, 15, 57));
		kingMenuItem.setForeground(new Color(100, 107, 134));
		kingMenuItem.setFont(new Font("SansSerif", Font.PLAIN, 16));
		humanMenu.add(kingMenuItem);

		JMenuItem poinsettiaMenuItem = new JMenuItem("Trạng nguyên");
		poinsettiaMenuItem.setBorder(null);
		poinsettiaMenuItem.setBackground(new Color(9, 15, 57));
		poinsettiaMenuItem.setForeground(new Color(100, 107, 134));
		poinsettiaMenuItem.setFont(new Font("SansSerif", Font.PLAIN, 16));
		humanMenu.add(poinsettiaMenuItem);

		eventMenu = new JMenu("Sự kiện");
		eventMenu.setForeground(new Color(100, 107, 134));
		eventMenu.setBorder(new EmptyBorder(0, 20, 0, 20));
		eventMenu.setFont(new Font("SansSerif", Font.PLAIN, 20));
		menuBar.add(eventMenu);

		JMenuItem eventMenuItem = new JMenuItem("Sự kiện");
		eventMenuItem.setBackground(new Color(9, 15, 57));
		eventMenuItem.setBorder(null);
		eventMenuItem.setForeground(new Color(100, 107, 134));
		eventMenuItem.setFont(new Font("SansSerif", Font.PLAIN, 16));
		eventMenu.add(eventMenuItem);

		dynastyMenu = new JMenu("Triều đại");
		dynastyMenu.setForeground(new Color(100, 107, 134));
		dynastyMenu.setBorder(new EmptyBorder(0, 20, 0, 20));
		dynastyMenu.setHorizontalAlignment(SwingConstants.CENTER);
		dynastyMenu.setFont(new Font("SansSerif", Font.PLAIN, 20));
		dynastyMenu.setBackground(new Color(9, 15, 57));
		menuBar.add(dynastyMenu);

		JMenuItem dynastyMenuItem = new JMenuItem("Triều đại");
		dynastyMenuItem.setBackground(new Color(9, 15, 57));
		dynastyMenuItem.setBorder(null);
		dynastyMenuItem.setForeground(new Color(100, 107, 134));
		dynastyMenuItem.setFont(new Font("SansSerif", Font.PLAIN, 16));
		dynastyMenu.add(dynastyMenuItem);

		siteMenu = new JMenu("Di tích");
		siteMenu.setForeground(new Color(100, 107, 134));
		siteMenu.setBorder(new EmptyBorder(0, 20, 0, 20));
		siteMenu.setHorizontalAlignment(SwingConstants.CENTER);
		siteMenu.setFont(new Font("SansSerif", Font.PLAIN, 20));
		siteMenu.setBackground(new Color(9, 15, 57));
		menuBar.add(siteMenu);

		JMenuItem siteMenuItem = new JMenuItem("Di tích");
		siteMenuItem.setBackground(new Color(9, 15, 57));
		siteMenuItem.setBorder(null);
		siteMenuItem.setForeground(new Color(100, 107, 134));
		siteMenuItem.setFont(new Font("SansSerif", Font.PLAIN, 16));
		siteMenu.add(siteMenuItem);

		festivalMenu = new JMenu("Lễ hội");
		festivalMenu.setForeground(new Color(100, 107, 134));
		festivalMenu.setBorder(new EmptyBorder(0, 20, 0, 20));
		festivalMenu.setHorizontalAlignment(SwingConstants.CENTER);
		festivalMenu.setFont(new Font("SansSerif", Font.PLAIN, 20));
		festivalMenu.setBackground(new Color(9, 15, 57));
		menuBar.add(festivalMenu);

		JMenuItem festivalmenuItem = new JMenuItem("Lễ hội");
		festivalmenuItem.setBackground(new Color(9, 15, 57));
		festivalmenuItem.setBorder(null);
		festivalmenuItem.setForeground(new Color(100, 107, 134));
		festivalmenuItem.setFont(new Font("SansSerif", Font.PLAIN, 16));
		festivalMenu.add(festivalmenuItem);

		menuItems = new ArrayList<>();
		menuItems.add(humanMenuItem);
		menuItems.add(kingMenuItem);
		menuItems.add(poinsettiaMenuItem);
		menuItems.add(eventMenuItem);
		menuItems.add(dynastyMenuItem);
		menuItems.add(festivalmenuItem);
		menuItems.add(siteMenuItem);

		JMenuController jMenuController = new JMenuController(this);
		for (JMenuItem jMenuItem : menuItems) {
			jMenuItem.addActionListener(jMenuController);
		}

		jMenus = new ArrayList<>();
		jMenus.add(humanMenu);
		jMenus.add(dynastyMenu);
		jMenus.add(siteMenu);
		jMenus.add(eventMenu);
		jMenus.add(festivalMenu);

		// content panel
		contentCardLayout = new CardLayout();
		contentPanel = new ContentPanel(contentCardLayout);
		contentPanel.setBackground(new Color(255, 255, 255));
		contentPanel.setBorder(new EmptyBorder(20, 20, 0, 20));
		mainPanel.add(contentPanel, BorderLayout.CENTER);

		// searchPanel
		SearchPanel searchPanel = new SearchPanel(contentPanel);
		menuPanel.add(searchPanel, BorderLayout.EAST);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(null);
		panel_1.setBackground(new Color(255, 255, 255));
		panel_1.setPreferredSize(new Dimension(70, 10));
		mainPanel.add(panel_1, BorderLayout.EAST);

		JPanel panel = new JPanel();
		panel.setBorder(null);
		panel.setBackground(new Color(255, 255, 255));
		panel.setPreferredSize(new Dimension(70, 10));
		mainPanel.add(panel, BorderLayout.WEST);

		addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowLostFocus(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowGainedFocus(WindowEvent e) {
				// TODO Auto-generated method stub
				requestFocusInWindow();
			}
		});
	}

	public void changeDefaultJMenu() {
		for (JMenu jMenu : jMenus) {
			jMenu.setForeground(new Color(100, 107, 134));
			jMenu.setBackground(new Color(9, 15, 57));
		}
	}

	public void changeSeclectedJMenu(JMenu jMenu) {
		changeDefaultJMenu();

		jMenu.setOpaque(true);
		jMenu.setContentAreaFilled(true);
		jMenu.setBackground(new Color(108, 125, 253));
		jMenu.setForeground(new Color(255, 255, 255));
	}
}
