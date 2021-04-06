package com.example.insuranceapi.validator;

import com.example.insuranceapi.model.DataType;
import com.example.insuranceapi.model.TextDataType;

import java.util.List;

import static com.example.insuranceapi.model.TextDataType.NUMERIC;

public class DataValidator {
    public List<String> fixParsedLine(List<String> parsedLine, DataType dataType) {
        return switch (dataType) {
            case VEHICLE -> {
                if (parsedLine.size() == 7) {
                    parsedLine.set(2, fixTypo(parsedLine.get(2), NUMERIC));
                    parsedLine.set(3, fixTypo(parsedLine.get(3), NUMERIC));
                    parsedLine.set(5, fixTypo(parsedLine.get(5), NUMERIC));
                    parsedLine.set(6, fixTypo(parsedLine.get(6), NUMERIC));
                }
                yield parsedLine;
            }
            default -> parsedLine;
        };
    }

    public String fixTypo(String textToRepair, TextDataType type) {
        return switch (type) {
            case NUMERIC -> textToRepair.replaceAll("[Oo]", "0");
            default -> textToRepair;
        };
    }

    public boolean isYear(String textVar) {
        if (textVar == null) {
            return false;
        }
        return textVar.matches("^[0-9]{4}$");
    }

    public boolean isPositiveNumeric(String textVar) {
        if (textVar == null) {
            return false;
        }
        return textVar.matches("\\d+(\\.\\d+)?");
    }

    public boolean isPlateNumber(String textVar) {
        if (textVar == null) {
            return false;
        }
        return textVar.matches("^\\d{3}[a-zA-Z]{3}$");
    }

    /**
     * Used to validate and fix different textline according to certain DataType rule
     *
     * @param parsedLine
     * @param dataType
     * @return
     */
    public ValidationResult validateTextLine(List<String> parsedLine, DataType dataType) {
        ValidationResult validationResult = new ValidationResult();
        if (parsedLine == null || parsedLine.isEmpty() || dataType == null) {
            validationResult.validatedResult = false;
            validationResult.validatedLine = parsedLine;
            return validationResult;
        }
        validationResult.validatedLine = fixParsedLine(parsedLine, dataType);
        switch (dataType) {
            case VEHICLE -> {
                if (parsedLine.size() == 7 && isPlateNumber(parsedLine.get(1)) && isYear(parsedLine.get(2))
                        && isPositiveNumeric(parsedLine.get(3)) && isPositiveNumeric(parsedLine.get(5))
                        && isPositiveNumeric(parsedLine.get(6))) {
                    validationResult.validatedResult = true;
                }
            }
            default -> validationResult.validatedResult = false;
        }
        return validationResult;
    }
}
