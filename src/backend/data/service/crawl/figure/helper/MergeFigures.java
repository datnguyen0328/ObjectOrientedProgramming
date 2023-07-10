package backend.data.service.crawl.figure.helper;

import java.util.ArrayList;

import backend.data.model.figure.*;
import backend.data.service.decode.HelperFunctions;

public class MergeFigures {
	
	//merge dynasty
	static public void mergeDynasty() {
		ArrayList<Figure> list1 = HelperFunctions.decodeFromJson("nks_figures.json");
		ArrayList<Figure> list2 = HelperFunctions.decodeFromJson("vs_figures.json");
		ArrayList<Figure> resList = new ArrayList<>(list2);

		for (Figure fig1 : list1)
				addToList(resList, fig1);

		HelperFunctions.encodeListToJson(resList, "merged_figures.json");
	}

	static public void addToList(ArrayList<Figure> resList, Figure targetFig) {
		
		//check if exist or not
		for (Figure resFig : resList) {
			String resName = resFig.getName().toLowerCase().replaceAll(" ", "");
			String resOtherName = resFig.getOtherName().toLowerCase().replaceAll(" ", "");
			String targetName = targetFig.getName().toLowerCase().replaceAll(" ", "");
			String targetOtherName = targetFig.getOtherName().toLowerCase().replaceAll(" ", "");
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
}