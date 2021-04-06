package com.example.insuranceapi.validator;

import com.example.insuranceapi.model.TextDataType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.example.insuranceapi.model.DataType.VEHICLE;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DataValidatorTest {
    @InjectMocks
    private DataValidator dataValidator;

    @Test
    void fixParsedLine() {
        List<String> fixablePrice = new ArrayList<String>();
        fixablePrice.add("1");
        fixablePrice.add("174RW2");
        fixablePrice.add("1999");
        fixablePrice.add("82285");
        fixablePrice.add("BMW");
        fixablePrice.add("55O669");
        fixablePrice.add("4564.12");
        List<String> fixablePricefixed = dataValidator.fixParsedLine(fixablePrice, VEHICLE);
        assertEquals("550669", fixablePricefixed.get(5));
    }

    @Test
    void fixTypo() {
        String typoNumber = "55O669";
        assertEquals("550669", dataValidator.fixTypo(typoNumber, TextDataType.NUMERIC));
    }

    @Test
    void isYear() {
        assertTrue(dataValidator.isYear("1999"));
        assertFalse(dataValidator.isYear("aaaa"));
    }

    @Test
    void isPositiveNumeric() {
        assertTrue(dataValidator.isPositiveNumeric("1000"));
        assertFalse(dataValidator.isPositiveNumeric("-12"));
    }

    @Test
    void isPlateNumber() {
        assertTrue(dataValidator.isPlateNumber("123abc"));
        assertFalse(dataValidator.isPlateNumber("123er1"));
    }

    @Test
    void validateTextLineInvalidYear() {
        List<String> invalidYearLine = new ArrayList<String>();
        invalidYearLine.add("1");
        invalidYearLine.add("546TLJ");
        invalidYearLine.add("311AZK");
        invalidYearLine.add("69271");
        invalidYearLine.add("AUDI");
        invalidYearLine.add("567879");
        invalidYearLine.add("6503.37");
        ValidationResult invalidYearLineResult = dataValidator.validateTextLine(invalidYearLine, VEHICLE);
        assertFalse(invalidYearLineResult.validatedResult);
    }

    @Test
    void validateTextLineWrongPlateNumber() {
        List<String> wrongPlateNumber = new ArrayList<String>();
        wrongPlateNumber.add("1");
        wrongPlateNumber.add("174RW2");
        wrongPlateNumber.add("1999");
        wrongPlateNumber.add("82285");
        wrongPlateNumber.add("BMW");
        wrongPlateNumber.add("550669");
        wrongPlateNumber.add("4564.12");
        ValidationResult wrongPlateNumberResult = dataValidator.validateTextLine(wrongPlateNumber, VEHICLE);
        assertFalse(wrongPlateNumberResult.validatedResult);
    }

    @Test
    void validateTextLineFixablePrice() {
        List<String> fixablePrice = new ArrayList<String>();
        fixablePrice.add("1");
        fixablePrice.add("174RWL");
        fixablePrice.add("1999");
        fixablePrice.add("82285");
        fixablePrice.add("BMW");
        fixablePrice.add("55O669");
        fixablePrice.add("4564.12");
        ValidationResult fixablePriceResult = dataValidator.validateTextLine(fixablePrice, VEHICLE);
        assertTrue(fixablePriceResult.validatedResult);
        assertEquals("550669", fixablePriceResult.validatedLine.get(5));

    }
}
