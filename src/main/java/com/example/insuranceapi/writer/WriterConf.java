package com.example.insuranceapi.writer;

public class WriterConf {
    char separator;
    char customQuote;

    public WriterConf(char separator, char customQuote) {
        this.separator = separator;
        this.customQuote = customQuote;
    }
}
