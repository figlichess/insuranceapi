package com.example.insuranceapi.parser;

import com.example.insuranceapi.model.CascoDataSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.util.List;

import static com.example.insuranceapi.model.DataType.VEHICLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
class CSVParserTest {

    @InjectMocks
    private CSVParser csvParser;

    private ParsingConf parsingConf;
    private ParsingConf parsingConfBroken;

    @BeforeEach
    public void init() {
        parsingConf = new ParsingConf(',', '"', 1,
                null, VEHICLE);
        parsingConfBroken = new ParsingConf(',', '"', 1,
                null, VEHICLE);

    }

    @Test
    void parseLine() {
        String row = "1338,174RWL,1999,82285,BMW,550669,4564.12";
        List<String> parsedLine = csvParser.parseLine(row, ',', '\0');
        assertEquals(7, parsedLine.size());
        assertEquals("174RWL", parsedLine.get(1));
        assertEquals("4564.12", parsedLine.get(6));
    }

    @Test
    void parseFile() {
        CascoDataSet parsedDatset = new CascoDataSet();
        try {
            csvParser.parseFile("src/test/resources/vehicles.csv", parsingConf, parsedDatset);
        } catch (FileNotFoundException | IllegalArgumentException exception) {
            fail("Caught an FileNotFoundException/IllegalArgumentException" + exception);
        }
        assertEquals(64320, parsedDatset.vehicles.size());
    }

    @Test
    void parseInvalidFile() {
        CascoDataSet parsedDatset = new CascoDataSet();
        try {
            csvParser.parseFile("src/test/resources/brokenvehicles.csv", parsingConfBroken, parsedDatset);
        } catch (FileNotFoundException | IllegalArgumentException exception) {
            fail("Caught an FileNotFoundException/IllegalArgumentException" + exception);
        }
        assertEquals(1, parsedDatset.vehicles.size());
    }
}
