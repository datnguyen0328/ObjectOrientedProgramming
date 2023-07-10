package gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.Normalizer;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import backend.data.constant.Constant;
import backend.data.model.IObject;
import gui.component.RoundedJTextField;

public class SearchPanel extends JPanel {
	private ContentPanel contentPanel;
	private JTextField searchTextField;

	public SearchPanel(ContentPanel contentPanel) {
		this.contentPanel = contentPanel;

		setBorder(new EmptyBorder(0, 0, 0, 0));
		setBackground(new Color(9, 15, 57));
		setPreferredSize(new Dimension(300, 10));
		setLayout(null);

		searchTextField = new RoundedJTextField(30);

		searchTextField.setForeground(new Color(9, 15, 57));
		searchTextField.setBorder(null);
		searchTextField.setFont(new Font("SansSerif", Font.PLAIN, 16));
		searchTextField.setBounds(29, 11, 217, 33);
		add(searchTextField);
		searchTextField.setColumns(10);
		searchTextField.setText(" Tìm kiếm");

		addPlaceHolderStyle(searchTextField);

		searchTextField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchTextField.getText().equals(" Tìm kiếm")) {
					searchTextField.setText(" ");
					searchTextField.requestFocus();
					removePlaceHolderStyle(searchTextField);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (!searchTextField.getText().equals(" Tìm kiếm")) {
					addPlaceHolderStyle(searchTextField);
					searchTextField.setText(" Tìm kiếm");
				}
			}
		});

		searchTextField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String currentPanel = contentPanel.getCurrentPanel();
				String searchString = searchTextField.getText();
				
				// TODO Auto-generated method stub
				if (currentPanel.equals(Constant.HUMAN_PANEL_NAME) || currentPanel.equals(Constant.KING_PANEL_NAME)
						|| currentPanel.equals(Constant.POINSETTIA_PANEL_NAME)) {
					addSearchLandingPanel(searchString, searchObject(searchString, contentPanel.getFigures()),
							contentPanel);
				}else if(currentPanel.equals(Constant.DYNASTY_PANEL_NAME)) {
					addSearchLandingPanel(searchString, searchObject(searchString, contentPanel.getDynasties()), contentPanel);
				}else if(currentPanel.equals(Constant.RELIC_PANEL_NAME)) {
					addSearchLandingPanel(searchString, searchObject(searchString, contentPanel.getRelics()), contentPanel);
				}else if(currentPanel.equals(Constant.FESTIVAL_PANEL_NAME)) {
					addSearchLandingPanel(searchString, searchObject(searchString, contentPanel.getFestivals()), contentPanel);
				}else if(currentPanel.equals(Constant.EVENT_PANEL_NAME)) {
					addSearchLandingPanel(searchString, searchObject(searchString, contentPanel.getEvents()), contentPanel);
				}
			}
		});

		JLabel searchLabel = new JLabel("");
		searchLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		searchLabel.setIcon(new ImageIcon(Constant.SEARCH_ICON));
		searchLabel.setBounds(0, 11, 29, 33);
		add(searchLabel);
	}

	public <T> void addSearchLandingPanel(String searchString, ArrayList<T> objects, ContentPanel contentPanel) {
		contentPanel.getSearchLandingTextArea().setText("Kết quả tìm kiếm cho \"" + searchString + "\":");

		// remove searchLandingPanel's center component
		BorderLayout searchLandingPanelLayout = (BorderLayout) contentPanel.getSearchLandingPanel().getLayout();
		if (searchLandingPanelLayout.getLayoutComponent(BorderLayout.CENTER) != null) {
			contentPanel.getSearchLandingPanel()
					.remove(searchLandingPanelLayout.getLayoutComponent(BorderLayout.CENTER));
		}

		JPanel searchLandingContainerPanel = new JPanel();
		searchLandingContainerPanel.setLayout(new GridLayout(objects.size(), 1));
		
		JScrollPane searchLandingContainerJScrollPane = new JScrollPane(searchLandingContainerPanel);
		contentPanel.getSearchLandingPanel().add(searchLandingContainerJScrollPane, BorderLayout.CENTER);
		for (T object : objects) {
			JLabel resultJLabel = new JLabel(((IObject) object).getName());
			resultJLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
			resultJLabel.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub

					contentPanel.getSearchTextArea().setText(object.toString().replaceAll("null", "Không rõ").replace("Trình soạn thảo sẽ tải bây giờ. Nếu một chốc nữa thông điệp này vẫn xuất hiện, xin hãy tải lại trang.", "") + "\n" + ((IObject) object).getDesc());
					contentPanel.getContentCardLayout().show(contentPanel, "searchResultPanel");
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub

				}
			});

			searchLandingContainerPanel.add(resultJLabel);
		}

		contentPanel.getContentCardLayout().show(contentPanel, "searchLandingPanel");
	}

	public void addPlaceHolderStyle(JTextField textField) {
		Font font = textField.getFont();
		font = font.deriveFont(Font.ITALIC);
		textField.setFont(font);
		textField.setForeground(Color.gray);
	}

	public void removePlaceHolderStyle(JTextField textField) {
		Font font = textField.getFont();
		font = font.deriveFont(Font.PLAIN | Font.BOLD);
		textField.setFont(font);
		textField.setForeground(Color.black);
	}
	
	public void setStyleJTextArea(JTextArea jTextArea) {
		jTextArea.setFont(new Font("SansSerif", Font.PLAIN, 13));

		jTextArea.setLineWrap(true);
		jTextArea.setWrapStyleWord(true);
		jTextArea.setEditable(false);
	}

	public void setStyleJLabel(JLabel jLabel) {
		jLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
		jLabel.setForeground(new Color(9, 15, 57));
	}

	public <T> ArrayList<T> searchObject(String inputString, ArrayList<T> objects) {
		ArrayList<T> foundObjects = new ArrayList<>();
		String inputId = normalizeString(inputString);

		for (T object : objects) {
			if (((IObject) object).getId().contains(inputId)) {
				foundObjects.add(object);
			}
		}

		return foundObjects;
	}

	public String normalizeString(String s) {
		return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "").replace("Đ", "D").replace("đ", "d")
				.replace(" – ", "-").replace("- ", "-").replace(" -", "-").replace("–", "-").replace("—", "-")
				.toLowerCase().replace(" ", "");
	}
}
