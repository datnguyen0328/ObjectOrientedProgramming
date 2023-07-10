package gui.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import backend.data.model.IObject;
import backend.data.model.dynasty.Dynasty;
import backend.data.model.event.Event;
import backend.data.model.festival.Festival;
import backend.data.model.figure.Figure;
import backend.data.model.relic.Relic;

public class ContainerPanel extends JPanel {
	private CardLayout containerCardLayout;
	private int index;
	private int numberOfPage;
	private ArrayList<?> objects;
	private JPanel detailJPanel;
	private JPanel relatedObjectJPanel;

	public ContainerPanel(CardLayout containerCardLayout, int numberOfPage, ArrayList<?> objects) {
		this.containerCardLayout = containerCardLayout;
		this.numberOfPage = numberOfPage;
		this.objects = objects;

		setBackground(new Color(255, 255, 255));

		setLayout(containerCardLayout);

		createPagination(numberOfPage, objects, containerCardLayout);

		containerCardLayout.show(this, "jPanel0");
	}

	public void createPagination(int numberOfPage, ArrayList<?> objects, CardLayout containerPanelCardLayout) {

		// create detail panel
		detailJPanel = new JPanel();
		detailJPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
		detailJPanel.setLayout(new BorderLayout());
		JTextArea detailJTextArea = new JTextArea();
		setStyleJTextArea(detailJTextArea);

		detailJPanel.add(detailJTextArea, BorderLayout.CENTER);

		JScrollPane detailJScrollPane = new JScrollPane(detailJPanel);
		detailJScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		detailJScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		this.add(detailJScrollPane, "detailJPanel");

		relatedObjectJPanel = new JPanel();
		relatedObjectJPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
		relatedObjectJPanel.setLayout(new BorderLayout());
		JTextArea relatedObjecJTextArea = new JTextArea();
		setStyleJTextArea(relatedObjecJTextArea);
		relatedObjectJPanel.add(relatedObjecJTextArea, BorderLayout.CENTER);

		JScrollPane relatedObjectJScrollPane = new JScrollPane(relatedObjectJPanel);
		relatedObjectJScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		relatedObjectJScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		this.add(relatedObjectJScrollPane, "relatedObjectJPanel");

		int objectsPerPage = (int) Math.ceil((double) objects.size() / numberOfPage);

		for (int i = 0; i < numberOfPage; i++) {
			JPanel cardJPanel = new JPanel();
			cardJPanel.setLayout(new BorderLayout());
			cardJPanel.add(createObjectPerPage(objectsPerPage, objects, this, containerPanelCardLayout, detailJTextArea,
					detailJPanel, relatedObjecJTextArea, relatedObjectJPanel), BorderLayout.CENTER);
			this.add(cardJPanel, "jPanel" + i);
		}
	}

	public JScrollPane createObjectPerPage(int numberOfObject, ArrayList<?> objects, JPanel containerPanel,
			CardLayout containerPanelCardLayout, JTextArea detailJTextArea, JPanel detailJPanel,
			JTextArea relatedObjectJTextArea, JPanel relatedObectJPanel) {
		JPanel pageContentJPanel = new JPanel();
		pageContentJPanel.setLayout(new GridLayout(numberOfObject, 1));

		JScrollPane jScrollPane = new JScrollPane(pageContentJPanel);
		jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		for (int j = 0; j < numberOfObject; j++) {
			JPanel gridJPanel = new JPanel();
			gridJPanel.setBackground(new Color(255, 255, 255));
			gridJPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
			gridJPanel.setLayout(new BorderLayout());

			if (index < objects.size()) {
				JLabel objectJLabel = new JLabel(((IObject) objects.get(index)).getName());
				setStyleJLabel(objectJLabel);
				objectJLabel.addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
						setDetail(objectJLabel.getText(), objects, containerPanel, containerPanelCardLayout,
								detailJTextArea, detailJPanel, relatedObjectJTextArea, relatedObectJPanel);

						containerPanelCardLayout.show(containerPanel, "detailJPanel");
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

				gridJPanel.add(objectJLabel, BorderLayout.NORTH);

				JTextArea gridJTextArea = new JTextArea();
				setStyleJTextArea(gridJTextArea);
				gridJTextArea.setText(((Object) objects.get(index)).toString().replaceAll("null", "Không rõ"));

				gridJPanel.add(gridJTextArea, BorderLayout.CENTER);

				pageContentJPanel.add(gridJPanel);
				index++;
			}
		}

		return jScrollPane;
	}

	public void setDetail(String objectName, ArrayList<?> objects, JPanel containerPanel,
			CardLayout containerPanelCardLayout, JTextArea detailJTextArea, JPanel detailJPanel,
			JTextArea relatedObjectJTextArea, JPanel relatedObjectJPanel) {

		if (findObject(objectName, objects) instanceof Figure) {
			detailJTextArea.setText(findObject(objectName, objects).toString().replaceAll("null", "Không rõ") + "\n"
					+ ((IObject) findObject(objectName, objects)).getDesc());
		} else if (findObject(objectName, objects) instanceof Dynasty
				|| findObject(objectName, objects) instanceof Festival
				|| findObject(objectName, objects) instanceof Relic
				|| findObject(objectName, objects) instanceof Event) {
			addNorthComponent(objectName, objects, containerPanel, containerPanelCardLayout, detailJPanel,
					detailJTextArea, relatedObjectJTextArea);
		}
	}

	public <T> void addNorthComponent(String objectName, ArrayList<T> objects, JPanel containerPanel,
			CardLayout containerPanelCardLayout, JPanel detailJPanel, JTextArea detailJTextArea,
			JTextArea relatedObjectJTextArea) {
		T object = (T) findObject(objectName, objects);

		if (object instanceof Dynasty || object instanceof Relic || object instanceof Festival) {
			// remove detailPanel's north component
			BorderLayout detaiLayout = (BorderLayout) detailJPanel.getLayout();
			if (detaiLayout.getLayoutComponent(BorderLayout.NORTH) != null) {
				detailJPanel.remove(detaiLayout.getLayoutComponent(BorderLayout.NORTH));
			}

			// add new detailPanel's north component
			JPanel detailNorthJPanel = new JPanel();
			detailNorthJPanel.setLayout(new BorderLayout());
			JTextArea headerJTextArea = new JTextArea();
			setStyleJTextArea(headerJTextArea);

			if (((IObject) object).getFigures().size() != 0) {
				headerJTextArea.setText(((IObject) object).getName() + "\nNhân vật liên quan:");

				JPanel relatedFigureJPanel = new JPanel();
				relatedFigureJPanel.setLayout(new GridLayout(((IObject) object).getFigures().size() / 5, 5));
				relatedFigureJPanel.setBackground(new Color(255, 255, 255));
				detailNorthJPanel.add(relatedFigureJPanel, BorderLayout.CENTER);

				for (Figure relatedFigure : ((IObject) object).getFigures()) {
					JLabel relatedFigureJLabel = new JLabel(relatedFigure.getName());

					relatedFigureJLabel.addMouseListener(new MouseListener() {

						@Override
						public void mouseReleased(MouseEvent e) {
							// TODO Auto-generated method stub

							relatedObjectJTextArea.setText(relatedFigure.toString() + "\n" + relatedFigure.getDesc());

							containerPanelCardLayout.show(containerPanel, "relatedObjectJPanel");
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

					relatedFigureJPanel.add(relatedFigureJLabel);
				}
			} else {
				headerJTextArea.setText(((IObject) object).getName());
			}

			detailNorthJPanel.add(headerJTextArea, BorderLayout.NORTH);

			detailJPanel.add(detailNorthJPanel, BorderLayout.NORTH);

			detailJTextArea.setText(
					((IObject) object).toString().replaceAll("null", "Không rõ") + "\n" + ((IObject) object).getDesc());
		} else if (object instanceof Event) {

			// remove detailPanel's north component
			BorderLayout detaiLayout = (BorderLayout) detailJPanel.getLayout();
			if (detaiLayout.getLayoutComponent(BorderLayout.NORTH) != null) {
				detailJPanel.remove(detaiLayout.getLayoutComponent(BorderLayout.NORTH));
			}

			// add new detailPanel's north component
			JPanel detailNorthJPanel = new JPanel();
			detailNorthJPanel.setLayout(new BorderLayout());
			JTextArea relatedFigureJTextArea = new JTextArea();
			detailNorthJPanel.add(relatedFigureJTextArea, BorderLayout.NORTH);

			if (((IObject) object).getFigures().size() != 0) {
				relatedFigureJTextArea.setText(((IObject) object).getName() + "\nNhân vật liên quan:");
				setStyleJTextArea(relatedFigureJTextArea);

				JPanel relatedFigureJPanel = new JPanel();
				relatedFigureJPanel.setLayout(new GridLayout(((IObject) object).getFigures().size() / 5, 5));
				relatedFigureJPanel.setBackground(new Color(255, 255, 255));
				detailNorthJPanel.add(relatedFigureJPanel, BorderLayout.CENTER);

				for (Figure relatedFigure : ((IObject) object).getFigures()) {
					JLabel relatedFigureJLabel = new JLabel(relatedFigure.getName());

					relatedFigureJLabel.addMouseListener(new MouseListener() {

						@Override
						public void mouseReleased(MouseEvent e) {
							// TODO Auto-generated method stub

							relatedObjectJTextArea.setText(relatedFigure.toString().replaceAll("null", "Không rõ")
									+ "\n" + relatedFigure.getDesc());

							containerPanelCardLayout.show(containerPanel, "relatedObjectJPanel");
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

					relatedFigureJPanel.add(relatedFigureJLabel);
				}
			} else {
				relatedFigureJTextArea.setText(((IObject) object).getName());
			}

			// add south component to detailNorthJPanel, relating dynasties
			JPanel outerSouthComponent = new JPanel();
			outerSouthComponent.setLayout(new GridLayout(2, 1));
			outerSouthComponent.setBackground(new Color(255, 255, 255));
			detailNorthJPanel.add(outerSouthComponent, BorderLayout.SOUTH);

			JTextArea relatedDynastyJTextArea = new JTextArea();
			setStyleJTextArea(relatedDynastyJTextArea);
			outerSouthComponent.add(relatedDynastyJTextArea);

			if (((Event) object).getDynastyDetails() != null) {
				relatedDynastyJTextArea.setText("Triều đại liên quan:");
				setStyleJTextArea(relatedDynastyJTextArea);

				Dynasty relatedDynasty = ((Event) object).getDynastyDetails();
				JLabel relatedDynastyJLabel = new JLabel(relatedDynasty.getName());

				relatedDynastyJLabel.addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub

						relatedObjectJTextArea.setText(relatedDynasty.toString().replaceAll("null", "Không rõ") + "\n"
								+ relatedDynasty.getDesc());

						containerPanelCardLayout.show(containerPanel, "relatedObjectJPanel");
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
				outerSouthComponent.add(relatedDynastyJLabel);
			} else {
				relatedDynastyJTextArea.setText("Triều đại liên quan: Không có");
			}
			detailJPanel.add(detailNorthJPanel, BorderLayout.NORTH);

			detailJTextArea.setText(
					((IObject) object).toString().replaceAll("null", "Không rõ") + "\n" + ((IObject) object).getDesc());
		}
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

	public <T> T findObject(String inputName, ArrayList<T> objects) {
		for (T object : objects) {
			if (object instanceof Figure && inputName.equals(((Figure) object).getName())) {
				return object;
			} else if (object instanceof Dynasty && inputName.equals(((Dynasty) object).getName())) {
				return object;
			} else if (object instanceof Festival && inputName.equals(((Festival) object).getName())) {
				return object;
			} else if (object instanceof Relic && inputName.equals(((Relic) object).getName())) {
				return object;
			} else if (object instanceof Event && inputName.equals(((Event) object).getName())) {
				return object;
			}
		}

		return null;
	}

}
