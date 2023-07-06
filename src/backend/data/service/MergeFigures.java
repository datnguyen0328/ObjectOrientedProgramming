package backend.data.service;

import java.util.ArrayList;

import backend.data.model.*;
import backend.data.model.figure.Figure;
import backend.data.service.decode.HelperFunctions;

public class MergeFigures {
	
	//merge dynasty
	static public ArrayList<Figure> mergeDynasty(ArrayList<Figure> list1, ArrayList<Figure> list2) {
		ArrayList<Figure> resList = new ArrayList<>(list1);

		for (Figure fig2 : list2) {
				addToList(resList, fig2);
		}

		return resList;
	}

	static public void addToList(ArrayList<Figure> resList, Figure targetFig) {
		
		//check if exist or not
		for (Figure resFig : resList) {
			String resName = HelperFunctions.normalizeString(resFig.getName()).toLowerCase().replaceAll(" ", "");
			String resOtherName = HelperFunctions.normalizeString(resFig.getOtherName()).toLowerCase().replaceAll(" ", "");
			String targetName = HelperFunctions.normalizeString(targetFig.getName()).toLowerCase().replaceAll(" ", "");
			String targetOtherName = HelperFunctions.normalizeString(targetFig.getOtherName()).toLowerCase().replaceAll(" ", "");
			if (resFig.getId().contains(targetFig.getId())
					|| targetFig.getId().contains(resFig.getId())
					|| resName.equals(targetName) || resName.equals(targetOtherName)
					|| resOtherName.equals(targetName) || resName.equals(targetOtherName)) {
				
				if(resFig.getBornYear() == 0 && targetFig.getBornYear() != 0) resFig.setBornYear(targetFig.getBornYear());
				if(resFig.getDeathYear() == 0 && targetFig.getDeathYear() != 0) resFig.setDeathYear(targetFig.getDeathYear());
				if(resFig.getHome().equals("Không rõ") && !targetFig.getHome().equals("Không rõ")) resFig.setHome(targetFig.getHome());
				for(String targetDynasty: targetFig.getDynasties()) {
					boolean alreadyFound = false;
					for(String resDynasty: resFig.getDynasties()) {
						if(targetDynasty.equals(resDynasty)) {
							alreadyFound = true;
							break;
						}
					}
					if(!alreadyFound) resFig.getDynasties().add(targetDynasty);
				}
				return;
			}
		}
		resList.add(targetFig);
	}

//	//test main function
	public static void main(String[] args) {
		ArrayList<Figure> figures = HelperFunctions.decodeFromJson("after_nks_figures.json");
		ArrayList<Figure> figures2 = HelperFunctions.decodeFromJson("after_vsfigures.json");
		System.out.println(figures.size());
		System.out.println(figures2.size());
		
		ArrayList<Figure> resList = mergeDynasty(figures, figures2);
		System.out.println(resList.size());
		HelperFunctions.encodeListToJson(resList, "after_figures.json");
		
	}
}