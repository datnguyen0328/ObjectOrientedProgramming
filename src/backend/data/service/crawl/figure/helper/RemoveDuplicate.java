package backend.data.service.crawl.figure.helper;

import java.util.*;

import backend.data.model.figure.*;
import backend.data.service.decode.HelperFunctions;

public class RemoveDuplicate {
	static ArrayList<Figure> figures = HelperFunctions.decodeFromJson("merged_figures.json");
	static ArrayList<Figure> kings = HelperFunctions.decodeFromJson("kings.json");
	static ArrayList<Figure> poinsettias = HelperFunctions.decodeFromJson("poinsettias.json");
	static ArrayList<String> delIDs = new ArrayList<>();
	static ArrayList<Integer> delIndexs = new ArrayList<>();
	static int count = 0;

	public static void remove() {
		Iterator<Figure> iterator = figures.iterator();

		while (iterator.hasNext()) {
			Figure fig = iterator.next();
			String nameId = fig.getName();
			String otherNameId = fig.getOtherName();

			boolean found = false;

			for (Figure king : kings) {
				String checkKing = king.getName() + " " + king.getOtherName();
				if (checkKing != null) {
					if (checkKing.contains(nameId) || checkKing.contains(otherNameId)) {
						count++;
						System.out.println(count + "." + king.getId());
						found = true;
						break;
					}
				}
				if (found) {
					break;
				}
			}

			if (!found) {
				for (Figure poinsettia : poinsettias) {
					String checkPoin = poinsettia.getName() + poinsettia.getOtherName();

					if (checkPoin.contains(nameId) || checkPoin.contains(otherNameId)) {
						System.out.println(checkPoin);
						count++;
						System.out.println(count + "." + poinsettia.getId());
						found = true;
						break;
					}
				}
			}

			if (found) {
				iterator.remove();
			}
		}
		
		HelperFunctions.encodeListToJson(figures, "nonduplicate_figures.json");
		ArrayList<Figure> objectList = new ArrayList<>();
		
		objectList.addAll(figures);
		objectList.addAll(kings);
		objectList.addAll(poinsettias);
		HelperFunctions.encodeListToJson(objectList, "appended_figures.json");
	}
}