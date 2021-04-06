package com.example.insuranceapi.parser;

import com.example.insuranceapi.model.CascoDataSet;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class CSVParser extends ParserByLine {
    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';

    /**
     * Parses CSV file row
     *
     * @param row
     * @param separators
     * @param customQuote
     * @return
     */
    public List<String> parseLine(String row, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (row == null && row.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = row.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }

    public void parseFile(String pathToFile, ParsingConf parsingConf, CascoDataSet cascoDataSet) throws IllegalArgumentException, FileNotFoundException {
        super.parseFile(pathToFile, parsingConf, cascoDataSet);
    }
}
