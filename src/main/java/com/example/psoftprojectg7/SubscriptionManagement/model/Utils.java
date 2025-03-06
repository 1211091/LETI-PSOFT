package com.example.psoftprojectg7.SubscriptionManagement.model;

import java.io.*;
import java.net.*;


public class Utils {
    private static final String url_joke = "https://jokes-by-api-ninjas.p.rapidapi.com/v1/jokes";
    private static final String key_joke = "90dae9c70emsh86dbf55186e6723p12fa34jsn3aff2c580ec2";
    private static final String host_joke = "jokes-by-api-ninjas.p.rapidapi.com";

    /*private static final String url_weather = "https://python-api-weather.herokuapp.com";
    private static final String key_weather = "90dae9c70emsh86dbf55186e6723p12fa34jsn3aff2c580ec2";
    private static final String host_weather = "forecast9.p.rapidapi.com";*/
    public static String sendGETJoke() throws IOException {
        URL obj = new URL(url_joke);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("X-RapidAPI-Key", key_joke);
        con.setRequestProperty("X-RapidAPI-Host", host_joke);
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            return response.toString();
        } else {
            System.out.println("GET request did not work.");
        }

        return null;
    }

    /*public static String sendGETWeather() throws IOException {
        URL obj = new URL(url_weather);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            return response.toString();
        } else {
            System.out.println("GET request did not work.");
        }

        return null;
    }*/
}
