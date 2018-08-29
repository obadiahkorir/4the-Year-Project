package com.example.obadiahkorir.projects;

public class DistanceTest {

	public static void main(String args[])
	{
		final int R=6371;
		double lat1=Double.parseDouble("12.444000");
		double lon1=Double.parseDouble("16.999204");
		double lat2=Double.parseDouble("110.657439");
		double lon2=Double.parseDouble("43.905678");
		
		double latDistance=Math.toRadians(lat2-lat1);
		double lonDistance=Math.toRadians(lon2-lon1);
		
		double a=Math.sin(latDistance/2)*Math.sin(latDistance/2)+Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))*Math.sin(lonDistance/2)*Math.sin(lonDistance/2);
		
		double c=2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
		
		double distance=R*c;
		
		System.out.println("The distance between two lat and long is: " + distance);
	}
	
	
}
