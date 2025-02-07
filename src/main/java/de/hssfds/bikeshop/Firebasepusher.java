package de.hssfds.bikeshop;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class Firebasepusher {

    public static void pushToFirebase(String key, String value, String token) {

        // Die URL zum gewünschten Pfad in der Datenbank (muss mit .json enden)
        //String url = "https://bikeshop-b09d8-default-rtdb.firebaseio.com/" + key + ".json";
        String url = "https://bikeshop-b09d8-default-rtdb.firebaseio.com/" + key + ".json?auth=" + token;

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

    public static void pushJSONtoDB(String key, String jsonData, String token) {
        String url = "https://bikeshop-b09d8-default-rtdb.firebaseio.com/" + key + ".json?auth=" + token;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonData))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("pushJSONtoDB - Status Code: " + response.statusCode());
            System.out.println("pushJSONtoDB - Response Body: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getJSONfromDB(String key, String token) {
        String url = "https://bikeshop-b09d8-default-rtdb.firebaseio.com/" + key + ".json?auth=" + token;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("getJSONfromDB - Status Code: " + response.statusCode());
            System.out.println("getJSONfromDB - Response Body: " + response.body());
            String responseBody = response.body();
            if (responseBody == null || responseBody.equals("null") || responseBody.isEmpty()) {
                return new JSONObject(); // Leeres JSON-Objekt zurückgeben, falls keine Daten vorhanden sind.
            }
            return new JSONObject(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] getFromFirebase(String key, String token) {

        // Die URL zum gewünschten Pfad in der Datenbank (muss mit .json enden)
        //String url = "https://bikeshop-b09d8-default-rtdb.firebaseio.com/" + key + ".json";
        String url = "https://bikeshop-b09d8-default-rtdb.firebaseio.com/" + key + ".json?auth=" + token;
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

    public static String generateToken(String email, String password) {
        String appId = "AIzaSyBCPNSDjRLjAqJNgTHupg7mA8N5KbtluNk";

        // JSON-Daten für den Request – hier werden die übergebenen Parameter genutzt
        String jsonPayload = "{\n" +
                "  \"email\": \"" + email + "\",\n" +
                "  \"password\": \"" + password + "\",\n" +
                "  \"returnSecureToken\": true\n" +
                "}";

        // URL mit dem API-Key
        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + appId;

        // HttpClient erstellen
        HttpClient client = HttpClient.newHttpClient();

        // HttpRequest erstellen (POST-Request mit JSON-Payload)
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        try {
            // Sende die Anfrage und erhalte die Antwort synchron
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Verarbeite die Antwort als JSON und extrahiere das idToken
                JSONObject jsonResponse = new JSONObject(response.body());
                String idToken = jsonResponse.getString("idToken");
                return idToken;
            } else {
                System.err.println("Fehler: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
