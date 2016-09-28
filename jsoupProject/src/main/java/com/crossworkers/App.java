package com.crossworkers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {
	public static Document doc;
	static JSONObject obj = new JSONObject();

	public static void main(String[] args) throws IOException, ParseException {
		//get connection to  German Super 6 web site
		getConnectionToURL();
		//fetch last Draw Date 
		getLastDrawDate();
		//fetch Last Draw Winning Numbers
		getLastDrawWinningNumbers();
		//Print Result as a json format
		printJson();
	}

	public static void getConnectionToURL() throws IOException {
		doc = Jsoup.connect("https://www.lotterypost.com/game/327").userAgent("Mozilla").get();
	}

	public static void getLastDrawDate() throws ParseException {
		Element lastDrawDate = doc.select(".resultsDrawDate").first();
		
		//Format date to YYYY-MM-DD
		String formattedLastDrawDate = formateDate(lastDrawDate.text());
		
		obj.put("lastDrawDate", formattedLastDrawDate);
	}

	public static void getLastDrawWinningNumbers() {
		Elements lastDrawWinningNumbers = doc.select(".resultsRow").select(".sprite-results");
		JSONArray list = new JSONArray();
		for (Iterator iterator = lastDrawWinningNumbers.iterator(); iterator.hasNext();) {
			Element element = (Element) iterator.next();
			list.add(element.text());
		}
		obj.put("lastDrawWinningNumbers", list);
	}

	public static void printJson() {
		System.out.println(obj.toJSONString());
	}
	public static String formateDate(String dateStr) throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat("EEEE, MMM dd, yyyy");
		String formatteddate = null;
		Date date = null;
		date = df.parse(dateStr);
		formatteddate = df.format(date);
		df.applyPattern("YYYY-MM-dd");
		String newDateString = df.format(date);
		return newDateString;
	}
}
