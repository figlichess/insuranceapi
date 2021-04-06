package com.example.insuranceapi.parser;

import com.example.insuranceapi.model.CascoDataSet;
import com.example.insuranceapi.model.DataType;
import com.example.insuranceapi.model.Vehicle;
import com.example.insuranceapi.validator.DataValidator;
import com.example.insuranceapi.validator.ValidationResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public abstract class ParserByLine implements Parser {
    /**
     * parses single line of file
     *
     * @param row
     * @return
     */
    protected abstract List<String> parseLine(String row, char separators, char customQuote);

    /**
     * Parses the entire file by each line using previously defined parseLine method
     *
     * @param pathToFile
     * @param parsingConf
     * @return
     * @throws IllegalArgumentException
     * @throws FileNotFoundException
     */
    public void parseFile(String pathToFile, ParsingConf parsingConf, CascoDataSet cascoDataSet) throws IllegalArgumentException, FileNotFoundException {
        Scanner scanner = new Scanner(new File(pathToFile));
        DataValidator dataValidator = new DataValidator();
        for (int i = 0; scanner.hasNext(); i++) {
            String scanLine = scanner.nextLine();
            if (i >= parsingConf.startLine) {
                List<String> line = parseLine(scanLine, parsingConf.separators, parsingConf.customQuote);
                ValidationResult validationResult = dataValidator.validateTextLine(line, parsingConf.dataType);
                if (validationResult.validatedResult) {
                    convertCharsToData(validationResult.validatedLine, parsingConf.dataType, cascoDataSet);
                }
            }
        }
        scanner.close();
    }

    /**
     * Used to defined in which type of data should be file parsed
     *
     * @param parsedLine
     * @param dataType
     * @param CascoDataSet
     * @throws IllegalArgumentException
     */
    protected void convertCharsToData(List<String> parsedLine, DataType dataType, CascoDataSet CascoDataSet) throws IllegalArgumentException {

        switch (dataType) {
            case VEHICLE:
                Vehicle vehicle = new Vehicle();
                vehicle.setPlateNumber(parsedLine.get(1));
                vehicle.setFirstRegistration(Integer.parseInt(parsedLine.get(2)));
                vehicle.setPurchasePrice(Long.parseLong(parsedLine.get(3)));
                vehicle.setProducer(parsedLine.get(4));
                vehicle.setMileage(Integer.parseInt(parsedLine.get(5)));
                vehicle.setPreviousIndemnity(Double.parseDouble(parsedLine.get(6)));
                CascoDataSet.vehicles.add(vehicle);
                break;
            default:
                throw new IllegalArgumentException("Data type is not valid");

        }
    }
}
