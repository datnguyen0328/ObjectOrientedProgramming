package backend.data.model.event;

import backend.data.model.IObject;
import backend.data.model.dynasty.*;
import backend.data.model.figure.*;

import java.util.ArrayList;

public class Event extends EventInit{
    private ArrayList<Figure> relatedFigsDetails;
    private Dynasty dynastyDetails;
	
    public Event(){
        super();
        this.relatedFigsDetails = new ArrayList<>();
    }
    public Event(ArrayList<Figure> relatedFigsDetails){
        this.relatedFigsDetails = relatedFigsDetails;
    }
    public Dynasty getDynastyDetails() {
        return dynastyDetails;
    }

    public void setDynastyDetails(Dynasty dynasty) {
        this.dynastyDetails = dynasty;
    }

    public ArrayList<Figure> getRelatedFigsDetails() {
        return relatedFigsDetails;
    }

    public void setRelatedFigsDetails(ArrayList<Figure> relatedFigsDetails) {
        this.relatedFigsDetails = relatedFigsDetails;
    }
    
    @Override
    public ArrayList<Figure> getFigures(){
    	return this.getRelatedFigsDetails();
    }
    
	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return super.getDescription().toString();
	}
}
