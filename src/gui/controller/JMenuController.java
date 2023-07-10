package gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import backend.data.constant.Constant;
import gui.view.MainView;

public class JMenuController implements ActionListener{
	private MainView mainView;
	
	public JMenuController(MainView mainView) {
		this.mainView = mainView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String actionString = e.getActionCommand();
		
		switch(actionString) {
		case "Nhân vật":
			this.mainView.changeSeclectedJMenu(mainView.getHumanMenu());
			this.mainView.getContentCardLayout().show(mainView.getContentPanel(), Constant.HUMAN_PANEL_NAME);
			this.mainView.getContentPanel().setCurrentPanel(Constant.HUMAN_PANEL_NAME);
			break;
		case "Vua":
			this.mainView.changeSeclectedJMenu(mainView.getHumanMenu());
			this.mainView.getContentCardLayout().show(mainView.getContentPanel(), Constant.KING_PANEL_NAME);
			this.mainView.getContentPanel().setCurrentPanel(Constant.KING_PANEL_NAME);
			break;
		case "Trạng nguyên":
			this.mainView.changeSeclectedJMenu(mainView.getHumanMenu());
			this.mainView.getContentCardLayout().show(mainView.getContentPanel(), Constant.POINSETTIA_PANEL_NAME);
			this.mainView.getContentPanel().setCurrentPanel(Constant.POINSETTIA_PANEL_NAME);
			break;
		case "Sự kiện":
			this.mainView.changeSeclectedJMenu(mainView.getEventMenu());
			this.mainView.getContentCardLayout().show(mainView.getContentPanel(), Constant.EVENT_PANEL_NAME);
			this.mainView.getContentPanel().setCurrentPanel(Constant.EVENT_PANEL_NAME);
			break;
		case "Triều đại":
			this.mainView.changeSeclectedJMenu(mainView.getDynastyMenu());
			this.mainView.getContentCardLayout().show(mainView.getContentPanel(), Constant.DYNASTY_PANEL_NAME);
			this.mainView.getContentPanel().setCurrentPanel(Constant.DYNASTY_PANEL_NAME);
			break;
		case "Di tích":
			this.mainView.changeSeclectedJMenu(mainView.getSiteMenu());
			this.mainView.getContentCardLayout().show(mainView.getContentPanel(), Constant.RELIC_PANEL_NAME);
			this.mainView.getContentPanel().setCurrentPanel(Constant.RELIC_PANEL_NAME);
			break;
		case "Lễ hội":
			this.mainView.changeSeclectedJMenu(mainView.getFestivalMenu());
			this.mainView.getContentCardLayout().show(mainView.getContentPanel(), Constant.FESTIVAL_PANEL_NAME);
			this.mainView.getContentPanel().setCurrentPanel(Constant.FESTIVAL_PANEL_NAME);
			break;
		}
	}

}
