package backend.data.model;

import java.util.ArrayList;

import backend.data.model.figure.Figure;

public interface IObject {
	public String getId();
	public String getName();
	public String getDesc();
	public String toString();
	public ArrayList<Figure> getFigures();
}
