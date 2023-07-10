package backend.data.service.crawl.dynasty;

import backend.data.model.IScraper;
import backend.data.service.crawl.dynasty.helpers.*;

public class DynastyScraper implements IScraper{

	@Override
	public void merge() {
		MergerDynasty.mergeDynasty();
		
	}

	@Override
	public void start() {
		NKSDynastyScraper.crawl();
		NKSDynastyScraperExtra.crawl();
		VSDynastyScraper.crawl();
	}

}
