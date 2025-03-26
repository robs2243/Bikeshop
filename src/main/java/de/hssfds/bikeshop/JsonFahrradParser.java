package de.hssfds.bikeshop;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to manually parse JSON strings into Fahrrad objects.
 */
public class JsonFahrradParser {

    /**
     * Parses a JSON string into a map of Fahrrad objects.
     * 
     * @param json The JSON string to parse
     * @return A map of Fahrrad objects, keyed by their IDs
     */
    public static Map<Integer, Fahrrad> parse(String json) {
        Map<Integer, Fahrrad> fahrradMap = new HashMap<>();
        
        // Remove outer braces and trim whitespace
        json = json.trim();
        if (json.startsWith("{")) {
            json = json.substring(1);
        }
        if (json.endsWith("}")) {
            json = json.substring(0, json.length() - 1);
        }
        
        // Process each bike entry
        int i = 0;
        while (i < json.length()) {
            // Find ID
            i = json.indexOf("\"", i);
            if (i == -1) break;
            
            int idStart = i + 1;
            i = json.indexOf("\"", idStart);
            if (i == -1) break;
            
            int idEnd = i;
            String idStr = json.substring(idStart, idEnd);
            
            try {
                int id = Integer.parseInt(idStr);
                
                // Find bike object
                i = json.indexOf("{", i);
                if (i == -1) break;
                
                int objectStart = i;
                int braceCount = 1;
                i++;
                
                while (braceCount > 0 && i < json.length()) {
                    char c = json.charAt(i);
                    if (c == '{') {
                        braceCount++;
                    } else if (c == '}') {
                        braceCount--;
                    }
                    i++;
                }
                
                if (braceCount != 0) {
                    throw new IllegalArgumentException("Malformed JSON: no matching closing brace");
                }
                
                int objectEnd = i - 1;
                String bikeJson = json.substring(objectStart + 1, objectEnd);
                
                // Parse the bike properties
                Fahrrad fahrrad = parseFahrrad(bikeJson, id);
                fahrradMap.put(id, fahrrad);
                
            } catch (NumberFormatException e) {
                // Not a valid ID, skip to next quote
                i++;
            }
        }
        
        return fahrradMap;
    }
    
    /**
     * Parses a JSON string representing a single Fahrrad object.
     * 
     * @param bikeJson The JSON string for a single bike
     * @param id The ID of the bike
     * @return A Fahrrad object
     */
    private static Fahrrad parseFahrrad(String bikeJson, int id) {
        double preis = 0.0;
        double akku = 0.0;
        double drehmoment = 0.0;
        String produktname = "";
        int zustand = 0;
        
        // Process each property
        int propIdx = 0;
        while (propIdx < bikeJson.length()) {
            // Find property name
            propIdx = bikeJson.indexOf("\"", propIdx);
            if (propIdx == -1) break;
            
            int propNameStart = propIdx + 1;
            propIdx = bikeJson.indexOf("\"", propNameStart);
            if (propIdx == -1) break;
            
            String propName = bikeJson.substring(propNameStart, propIdx);
            
            // Find property value
            propIdx = bikeJson.indexOf(":", propIdx);
            if (propIdx == -1) break;
            propIdx++;
            
            // Skip whitespace
            while (propIdx < bikeJson.length() && Character.isWhitespace(bikeJson.charAt(propIdx))) {
                propIdx++;
            }
            
            // Extract value based on type
            if (propIdx < bikeJson.length()) {
                if (bikeJson.charAt(propIdx) == '"') {
                    // String value
                    propIdx++;
                    int valueStart = propIdx;
                    propIdx = bikeJson.indexOf("\"", propIdx);
                    if (propIdx == -1) break;
                    
                    String value = bikeJson.substring(valueStart, propIdx);
                    
                    if (propName.equals("produktname")) {
                        produktname = value;
                    }
                    
                    propIdx++;
                } else {
                    // Number value
                    int valueStart = propIdx;
                    int valueEnd = bikeJson.indexOf(",", propIdx);
                    if (valueEnd == -1) {
                        valueEnd = bikeJson.length();
                    }
                    
                    String value = bikeJson.substring(valueStart, valueEnd).trim();
                    
                    switch (propName) {
                        case "preis":
                            preis = Double.parseDouble(value);
                            break;
                        case "akku":
                            akku = Double.parseDouble(value);
                            break;
                        case "drehmoment":
                            drehmoment = Double.parseDouble(value);
                            break;
                        case "zustand":
                            zustand = Integer.parseInt(value);
                            break;
                        case "id": 
                            // ID is already parsed, nothing to do
                            break;
                    }
                    
                    propIdx = valueEnd;
                }
            }
        }
        
        return new Fahrrad(preis, akku, drehmoment, produktname, zustand, id);
    }
}
