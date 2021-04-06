package com.example.insuranceapi.writer;

import com.example.insuranceapi.model.InputOutputData;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Writer {

    public void writeToFile(String pathToFile, WriterConf writerConf, Set<? extends InputOutputData> outputData) throws IllegalArgumentException, IOException {
        File csvOutputFile = new File(pathToFile);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            outputData.stream()
                    .map(l -> convertToFileFormat(l, writerConf))
                    .forEach(pw::println);
        }
    }

    public String convertToFileFormat(
            InputOutputData outputData,
            WriterConf writerConf) {
        final String[] line = outputData.getDataForOutput();
        return Stream.of(line)
                .map(l -> formatLine(l, writerConf.customQuote))
                .collect(Collectors.joining(String.valueOf(writerConf.separator)));

    }

    protected abstract String formatLine(final String field, final char quote);
}
