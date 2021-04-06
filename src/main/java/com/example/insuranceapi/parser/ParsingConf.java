package com.example.insuranceapi.parser;

import com.example.insuranceapi.model.DataType;

public class ParsingConf {
    char separators;
    char customQuote;
    int startLine;
    String jsonRootNodeName;
    DataType dataType;

    public ParsingConf(char separators, char customQuote, int startLine, String jsonRootNodeName, DataType dataType) {
        this.separators = separators;
        this.customQuote = customQuote;
        this.startLine = startLine;
        this.jsonRootNodeName = jsonRootNodeName;
        this.dataType = dataType;
    }
}
