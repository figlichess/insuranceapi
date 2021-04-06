package com.example.insuranceapi.parser;

import com.example.insuranceapi.model.CascoDataSet;

import java.io.IOException;

public interface Parser {
    void parseFile(String pathToFile, ParsingConf parsingConf, CascoDataSet cascoDataSet) throws IllegalArgumentException, IOException;
}
