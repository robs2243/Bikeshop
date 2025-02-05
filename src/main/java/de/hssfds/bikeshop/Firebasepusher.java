package de.hssfds.bikeshop;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Firebasepusher {

    public static void pushToFirebase(String key, String value) {

        // Die URL zum gewünschten Pfad in der Datenbank (muss mit .json enden)
        String url = "https://bikeshop-b09d8-default-rtdb.firebaseio.com/" + key + ".json";

        // JSON-Daten: Ein JSON-String, daher werden die doppelten Anführungszeichen benötigt.
        String jsonData = "\"" + value + "\"";
        //String jsonData = value;

        // Erstelle einen HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Erstelle die HttpRequest zum Schreiben (PUT überschreibt die Daten an diesem Pfad)
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonData))
                .build();

        try {
            // Sende die Anfrage synchron und erhalte die Antwort
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] getFromFirebase(String key) {

        // Die URL zum gewünschten Pfad in der Datenbank (muss mit .json enden)
        String url = "https://bikeshop-b09d8-default-rtdb.firebaseio.com/" + key + ".json";

        // Erstelle einen HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Erstelle die HttpRequest zum Lesen (GET liest die Daten an diesem Pfad)
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            // Sende die Anfrage synchron und erhalte die Antwort
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
            return new String[]{String.valueOf(response.statusCode()), response.body()};
        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{"500", "Internal Server Error"};
        }
    }

}
