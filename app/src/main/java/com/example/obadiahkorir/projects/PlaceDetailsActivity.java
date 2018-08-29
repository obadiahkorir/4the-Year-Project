package com.example.obadiahkorir.projects;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class PlaceDetailsActivity extends Activity {
	WebView mWvPlaceDetails;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_place_details);
		
		// Getting reference to WebView ( wv_place_details ) of the layout activity_place_details
		mWvPlaceDetails = (WebView) findViewById(R.id.wv_place_details);
		
		mWvPlaceDetails.getSettings().setUseWideViewPort(false);
		
		// Getting place reference from the map	
		String reference = getIntent().getStringExtra("reference");
		
		
		StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
		sb.append("reference="+reference);
		sb.append("&sensor=true");
		sb.append("&key=AIzaSyBqKHbG2wAlDhNsONbxyZlHKnLgfpyLrEk");
		
		
		// Creating a new non-ui thread task to download Google place details 
        PlacesTask placesTask = new PlacesTask();		        			        
        
		// Invokes the "doInBackground()" method of the class PlaceTask
        placesTask.execute(sb.toString());	
		
	};
	
	
	/** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);                
                

                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();                

                // Connecting to url 
                urlConnection.connect();                

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }

                data = sb.toString();
                br.close();

        }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }

        return data;
    }         

	
	/** A class, to download Google Place Details */
	private class PlacesTask extends AsyncTask<String, Integer, String>{

		String data = null;
		
		// Invoked by execute() method of this object
		@Override
		protected String doInBackground(String... url) {
			try{
				data = downloadUrl(url[0]);
			}catch(Exception e){
				 Log.d("Background Task",e.toString());
			}
			return data;
		}
		
		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(String result){			
			ParserTask parserTask = new ParserTask();
			
			// Start parsing the Google place details in JSON format
			// Invokes the "doInBackground()" method of the class ParseTask
			parserTask.execute(result);
		}
	}
	
	
	/** A class to parse the Google Place Details in JSON format */
	private class ParserTask extends AsyncTask<String, Integer, HashMap<String,String>>{

		JSONObject jObject;
		
		// Invoked by execute() method of this object
		@Override
		protected HashMap<String,String> doInBackground(String... jsonData) {
		
			HashMap<String, String> hPlaceDetails = null;
			PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();
        
	        try{
	        	jObject = new JSONObject(jsonData[0]);
	        	
	            // Start parsing Google place details in JSON format
	            hPlaceDetails = placeDetailsJsonParser.parse(jObject);
	            
	        }catch(Exception e){
	                Log.d("Exception",e.toString());
	        }
	        return hPlaceDetails;
		}
		
		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(HashMap<String,String> hPlaceDetails){			
			
			
			String name = hPlaceDetails.get("name");
			String icon = hPlaceDetails.get("icon");
			String vicinity = hPlaceDetails.get("vicinity");
			String lat = hPlaceDetails.get("lat");
			String lng = hPlaceDetails.get("lng");
			String formatted_address = hPlaceDetails.get("formatted_address");
			String formatted_phone = hPlaceDetails.get("formatted_phone");
			String website = hPlaceDetails.get("website");
			String rating = hPlaceDetails.get("rating");
			String international_phone_number = hPlaceDetails.get("international_phone_number");
			String url = hPlaceDetails.get("url");
			
			
			String mimeType = "text/html";
			String encoding = "utf-8";
			
			String data = 	"<html>"+							
							"<body><img style='float:left' src="+icon+" /><h1><center>"+name+"</center></h1>" +
							"<br style='clear:both' />" +
							"<hr  />"+
							"<p>Vicinity : " + vicinity + "</p>" +
							"<p>Location : " + lat + "," + lng + "</p>" +
							"<p>Address : " + formatted_address + "</p>" +
							"<p>Phone : " + formatted_phone + "</p>" +
							"<p>Website : " + website + "</p>" +
							"<p>Rating : " + rating + "</p>" +
							"<p>International Phone  : " + international_phone_number + "</p>" +
							"<p>URL  : <a href='" + url + "'>" + url + "</p>" +			
							"</body></html>";
			
			// Setting the data in WebView
			mWvPlaceDetails.loadDataWithBaseURL("", data, mimeType, encoding, "");			
		}
	}
}