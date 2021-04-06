package com.example.insuranceapi.parser;

import com.example.insuranceapi.model.CascoDataSet;
import com.example.insuranceapi.model.DataType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
class JSONParserTest {

    @InjectMocks
    private JSONParser jsonParser;

    private ParsingConf parsingConf;

    @BeforeEach
    public void init() {
        parsingConf = new ParsingConf('\0', '\0', 0, "data", DataType.COEFFICIENT);
    }

    @Test
    void parseFile() {
        CascoDataSet parsedDatset = new CascoDataSet();
        try {
            jsonParser.parseFile("src/test/resources/data.json", parsingConf, parsedDatset);
        } catch (IOException | IllegalArgumentException exception) {
            fail("Caught an IOException/IllegalArgumentException" + exception);
        }
        assertEquals(3, parsedDatset.riskparameters.size());
        assertEquals(17, parsedDatset.additionalRiskparameters.size());
    }
}
