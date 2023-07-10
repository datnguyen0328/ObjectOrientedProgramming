package backend.data.service.crawl.figure;

import java.util.ArrayList;

import backend.data.model.IScraper;
import backend.data.model.dynasty.*;
import backend.data.service.crawl.figure.helper.*;
import backend.data.service.decode.HelperFunctions;

public class FigureScraper implements IScraper{
	
	static public ArrayList<String> refDynasties = HelperFunctions.decodeDynastyNamesFromJson("final_dynasties.json");
	static public ArrayList<Dynasty> refDynastiesObj = HelperFunctions.decodeDynastiesFromJson("final_dynasties.json");

	
	@Override
	public void merge() {
		RemoveDuplicate.remove();
	}

	
	public void start() {
		NKSFigureScraper.crawl();
		VSFigureScraper.crawl();
		MergeFigures.mergeDynasty();
		
		NKSPoinsettiaScraper.crawl();
		WikiKingScraper.crawl();
	}

}
