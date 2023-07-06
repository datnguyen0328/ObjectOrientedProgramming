package gui.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

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

	public ContainerPanel(CardLayout containerCardLayout, int numberOfPage, ArrayList<?> objects) {
		this.containerCardLayout = containerCardLayout;
		this.numberOfPage = numberOfPage;
		this.objects = objects;

		setLayout(containerCardLayout);

		createPagination(numberOfPage, objects, containerCardLayout);

		containerCardLayout.show(this, "jPanel0");
	}

	public void createPagination(int numberOfPage, ArrayList<?> objects, CardLayout containerPanelCardLayout) {

		// create detail panel
		JPanel detailJPanel = new JPanel();
		detailJPanel.setLayout(new BorderLayout());
		JTextPane detailJTextPane = new JTextPane();
		detailJPanel.add(detailJTextPane, BorderLayout.CENTER);

		this.add(detailJPanel, "detailJPanel");

		JPanel relatedObjectJPanel = new JPanel();
		relatedObjectJPanel.setLayout(new BorderLayout());
		JTextPane relatedObjecJTextPane = new JTextPane();
		relatedObjectJPanel.add(relatedObjecJTextPane, BorderLayout.CENTER);

		this.add(relatedObjectJPanel, "relatedObjectJPanel");

		int objectsPerPage = (int) Math.ceil((double) objects.size() / numberOfPage);

		for (int i = 0; i < numberOfPage; i++) {
			JPanel cardJPanel = new JPanel();
			cardJPanel.setLayout(new BorderLayout());
			cardJPanel.add(createObjectPerPage(objectsPerPage, objects, this, containerPanelCardLayout, detailJTextPane,
					detailJPanel, relatedObjecJTextPane, relatedObjectJPanel), BorderLayout.CENTER);

			this.add(cardJPanel, "jPanel" + i);
		}
	}

	public JScrollPane createObjectPerPage(int numberOfObject, ArrayList<?> objects, JPanel containerPanel,
			CardLayout containerPanelCardLayout, JTextPane detailJTextPane, JPanel detailJPanel,
			JTextPane relatedObjectJTextPane, JPanel relatedObectJPanel) {
		JPanel pageContentJPanel = new JPanel();
		pageContentJPanel.setLayout(new GridLayout(numberOfObject, 1));

		JScrollPane jScrollPane = new JScrollPane(pageContentJPanel);
		jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		for (int j = 0; j < numberOfObject; j++) {
			JPanel gridJPanel = new JPanel();
			gridJPanel.setLayout(new BorderLayout());

			if (index < objects.size()) {
				JLabel objectJLabel = new JLabel(((IObject) objects.get(index)).getName());
				objectJLabel.addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
//						detailJTextPane.setText(findObject(objectJLabel.getText(), objects).toString() + "\n"
//								+ ((IObject) findObject(objectJLabel.getText(), objects)).getDesc());
						setDetail(objectJLabel.getText(), objects, containerPanel, containerPanelCardLayout,
								detailJTextPane, detailJPanel, relatedObjectJTextPane, relatedObectJPanel);

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

				JTextPane humanJTextPane = new JTextPane();
				humanJTextPane.setText(((Object) objects.get(index)).toString());
				gridJPanel.add(humanJTextPane, BorderLayout.CENTER);

				pageContentJPanel.add(gridJPanel);
				index++;
			}
		}

		return jScrollPane;
	}

	public void setDetail(String objectName, ArrayList<?> objects, JPanel containerPanel,
			CardLayout containerPanelCardLayout, JTextPane detailJTextPane, JPanel detailJPanel,
			JTextPane relatedObjectJTextPane, JPanel relatedObjectJPanel) {
		
		if (findObject(objectName, objects) instanceof Figure) {
			detailJTextPane.setText(findObject(objectName, objects).toString() + "\n"
					+ ((IObject) findObject(objectName, objects)).getDesc());
		} else if (findObject(objectName, objects) instanceof Dynasty || findObject(objectName, objects) instanceof Festival || findObject(objectName, objects) instanceof Relic || findObject(objectName, objects) instanceof Event) {
			addNorthComponent(objectName, objects, containerPanel, containerPanelCardLayout, detailJPanel, detailJTextPane, relatedObjectJTextPane);
		}
	}
	
	public <T> void addNorthComponent(String objectName, ArrayList<T> objects, JPanel containerPanel, CardLayout containerPanelCardLayout, JPanel detailJPanel, JTextPane detailJTextPane, JTextPane relatedObjectJTextPane) {
		T object = (T) findObject(objectName, objects);

		//remove detailPanel's north component
		BorderLayout detaiLayout = (BorderLayout) detailJPanel.getLayout();
		if(detaiLayout.getLayoutComponent(BorderLayout.NORTH) != null) {
			detailJPanel.remove(detaiLayout.getLayoutComponent(BorderLayout.NORTH));
		}
		
		//add new detailPanel's north component
		JPanel detailNorthJPanel = new JPanel();
		detailNorthJPanel.setLayout(new BorderLayout());
		JTextPane headerJTextPane = new JTextPane();
		
		if(((IObject) object).getFigures().size() != 0) {
			headerJTextPane.setText(((IObject) object).getName() + "\nNhân vật liên quan:"  );
			
			JPanel relatedFigureJPanel = new JPanel();
			relatedFigureJPanel.setLayout(new GridLayout(((IObject) object).getFigures().size()/5, 5));
			detailNorthJPanel.add(relatedFigureJPanel, BorderLayout.CENTER);
			
			for (Figure relatedFigure : ((IObject) object).getFigures()) {
				JLabel relatedFigureJLabel = new JLabel(relatedFigure.getName());

				relatedFigureJLabel.addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub

						relatedObjectJTextPane.setText(relatedFigure.toString() + "\n" + relatedFigure.getDesc());

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
			headerJTextPane.setText(((IObject) object).getName());
		}
		
		detailNorthJPanel.add(headerJTextPane, BorderLayout.NORTH);
		
		detailJPanel.add(detailNorthJPanel, BorderLayout.NORTH);

		detailJTextPane.setText(object.toString() + "\n" + ((IObject) object).getDesc());
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
