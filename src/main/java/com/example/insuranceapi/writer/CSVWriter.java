package com.example.insuranceapi.writer;

public class CSVWriter extends Writer {
    public String formatLine(final String field, final char quote) {
        String result = field;
        // should all fields enclosed in double quotes
        if (quote != '\u0000') {
            result = quote + result + quote;
        }

        return result;

    }
}
