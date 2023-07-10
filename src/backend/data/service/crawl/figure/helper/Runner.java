package backend.data.service.crawl.figure.helper;
import backend.data.service.crawl.dynasty.*;
import backend.data.service.crawl.figure.*;

public class Runner {
	public static void main(String[] args) {
		
		DynastyScraper ds = new DynastyScraper();
		ds.start();
		ds.merge();
		
		FigureScraper fs = new FigureScraper();
		System.out.println("RUN");
		fs.start();
		fs.merge();
		
		ArrangeFunctions.arrangeFiguresToDynasties();
		
		
	}
}
