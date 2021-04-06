package com.example.insuranceapi.writer;

public class WriterFactory {
    public static Writer getWriter(String type) {
        if ("CSV".equalsIgnoreCase(type)) {
            return new CSVWriter();
        }
        return null;
    }
}
