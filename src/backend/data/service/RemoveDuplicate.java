package backend.data.service;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//import objects.Figure;
import backend.data.model.*;
import backend.data.model.figure.Figure;
import backend.data.service.decode.HelperFunctions;

public class RemoveDuplicate {
	static ArrayList<Figure> figures = HelperFunctions.decodeFromJson("after_figures.json");
	static ArrayList<Figure> kings = HelperFunctions.decodeFromJson("after_kings.json");	
	static ArrayList<Figure> poinsettias = HelperFunctions.decodeFromJson("after_poinsettias.json");
	static ArrayList<String> delIDs = new ArrayList<>();	
	static ArrayList<Integer> delIndexs = new ArrayList<>();
	static int count =0;
	static void removeDuplicate2() {
	    Iterator<Figure> iterator = figures.iterator();
	    
//	   for(int i=0;i<figures.size();i++) {
	    while (iterator.hasNext()) {
	        Figure fig = iterator.next();
//		    Figure fig=figures.get(i);
	        String nameId = fig.getName();
	        String otherNameId = fig.getOtherName();
	        
	        boolean found = false;
	        
	        for (Figure king : kings) {
	        	String checkKing = king.getName()+" "+king.getOtherName();
	            if (checkKing != null) {
//	                String[] kingIds = king.getId()	;
//	                for (String kingId : kingIds) {
	                    if (checkKing.contains(nameId) || checkKing.contains(otherNameId)) {
//	                    	delIndexs.add(i);
//	                    	kings.remove(king);
	                    	count++;
	                    	System.out.println(count+ "."+king.getId());
	                        found = true;
	                        break;
	                    }
//	                }
	            }
	            if (found) {
	                break;
	            }
	        }
	        
	        if (!found) {
	            for (Figure poinsettia : poinsettias) {
	            	String checkPoin = poinsettia.getName()+ poinsettia.getOtherName();
	            	
	                if (checkPoin.contains(nameId) || checkPoin.contains(otherNameId)) {
	                	System.out.println(checkPoin);
	                	//delIndexs.add(i);
//	                	poinsettias.remove(poinsettia);
	                	count++;
	                	System.out.println(count+ "."+poinsettia.getId());
	                    found = true;
	                    break;
	                }
	            }
	        }
	        
	        if (found) {
	            iterator.remove();
	        }
	    }
	}

	
	
	
	//test main function
	public static void main(String[] args) {
		
		System.out.println(figures.size());
		removeDuplicate2();
		for (int delIndex :delIndexs) {
			System.out.println(delIndex);
			//Figure delFigure = figures.get(1);
			//figures.remove(delFigure);
			
		}
		System.out.println(figures.size());
//		for(Figure fig: figures) System.out.println(fig.getId());
		HelperFunctions.encodeListToJson(figures,"after_nonduplicate_figures.json");
	}

}