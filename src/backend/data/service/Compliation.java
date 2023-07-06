package backend.data.service;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.*;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

//import helper_package.HelperFunctions;
import backend.data.model.*;
//import objects.King;
//import objects.Poinsettia;
//import testing.Car;
import backend.data.model.figure.Figure;
import backend.data.model.figure.King;
import backend.data.model.figure.Poinsettia;
import backend.data.service.decode.HelperFunctions;

public class Compliation {
	public static void main(String[] args) {


		ArrayList<Figure> objectList = new ArrayList<>();
		String filePath1 = "after_kings.json";
		String filePath2 = "after_nonduplicate_figures";
		String filePath3 = "after_poinsettias.json";

		// Read JSON data from each file and convert it to objects
		String jsonData = HelperFunctions.readFile(filePath2);
		Gson gson = new Gson();
		Type listType = new TypeToken<List<Figure>>() {
		}.getType();
		ArrayList<Figure> fileObjects = gson.fromJson(jsonData, listType);

		objectList.addAll(fileObjects);

		String jsonData1 = HelperFunctions.readFile(filePath1);
		Gson gson2 = new Gson();
		Type listType2 = new TypeToken<List<King>>() {
		}.getType();
		ArrayList<King> kingList = gson2.fromJson(jsonData1, listType2);

		String jsonData2 = HelperFunctions.readFile(filePath3);
		Gson gson3 = new Gson();
		Type listType3 = new TypeToken<List<Poinsettia>>() {
		}.getType();
		ArrayList<Poinsettia> poinsettiaList = gson3.fromJson(jsonData2, listType3);

//	Car car = new Car(4, "four", 2);
		// objectList.add(car);
		objectList.addAll(kingList);
		objectList.addAll(poinsettiaList);
		// Print the list of objects
		for (Figure object : objectList) {
			System.out.println(object);
		}

		HelperFunctions.encodeListToJson(objectList, "after_append.json");
	}

}
