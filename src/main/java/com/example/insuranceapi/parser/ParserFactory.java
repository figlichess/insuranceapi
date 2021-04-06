package com.example.insuranceapi.parser;

public class ParserFactory {
    public static Parser getParser(String type) {
        if ("CSV".equalsIgnoreCase(type)) {
            return new CSVParser();
        } else if ("JSON".equalsIgnoreCase(type)) {
            return new JSONParser();
        }
        return null;
    }
}
