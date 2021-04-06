package com.example.insuranceapi.writer;

import com.example.insuranceapi.model.CascoInsurance;
import com.example.insuranceapi.model.InputOutputData;
import com.example.insuranceapi.model.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(MockitoExtension.class)
class CSVWriterTest {

    @InjectMocks
    private CSVWriter csvWriter;

    private WriterConf writerConf;

    private Set<InputOutputData> outputData;

    private Vehicle vehicle;

    private CascoInsurance cascoInsurance;

    @BeforeEach
    public void init() {
        cascoInsurance = new CascoInsurance();
        vehicle = new Vehicle("789abc", 2013, 64533, "VOLVO", 192719, 0.0);
        vehicle.setCascoInsurance(cascoInsurance);
        writerConf = new WriterConf(',', '\u0000');
        outputData = new HashSet<InputOutputData>();
        outputData.add(vehicle);
    }

    @Test
    void writeToFile() {
        File tempFile = null;
        String scanLine = "";
        try {
            csvWriter.writeToFile("src/test/resources/testCSVWriter.csv", writerConf, outputData);
            tempFile = new File("src/test/resources/testCSVWriter.csv");
            Scanner scanner = new Scanner(tempFile);
            scanLine = scanner.nextLine();
            scanner.close();
        } catch (IllegalArgumentException | IOException exception) {
            fail("Caught an FileNotFoundException/IllegalArgumentException" + exception);
        } finally {
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
        assertEquals("789abc,VOLVO,0.0", scanLine);
    }
}
