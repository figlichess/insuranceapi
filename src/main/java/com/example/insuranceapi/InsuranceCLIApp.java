package com.example.insuranceapi;

import com.example.insuranceapi.casco.CascoCalculator;
import com.example.insuranceapi.model.CascoDataSet;
import com.example.insuranceapi.model.RiskParameter;
import com.example.insuranceapi.parser.Parser;
import com.example.insuranceapi.parser.ParserFactory;
import com.example.insuranceapi.parser.ParsingConf;
import com.example.insuranceapi.service.AdditionalRiskParameterService;
import com.example.insuranceapi.service.RiskParameterService;
import com.example.insuranceapi.writer.Writer;
import com.example.insuranceapi.writer.WriterConf;
import com.example.insuranceapi.writer.WriterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.insuranceapi.model.DataType.COEFFICIENT;
import static com.example.insuranceapi.model.DataType.VEHICLE;

@EnableCaching
@Component
public class InsuranceCLIApp implements CommandLineRunner {
    private static Logger LOG = LoggerFactory
            .getLogger(InsuranceCLIApp.class);
    @Autowired
    private CascoCalculator cascoCalculator;
    @Autowired
    private RiskParameterService riskParameterService;
    @Autowired
    private AdditionalRiskParameterService addRiskParameterService;

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(InsuranceCLIApp.class, args);
        LOG.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) {
        LOG.info("EXECUTING : command line runner");

        if (args == null || args.length != 3) {
            System.out.println("Insert 3 file path parameters to parse vehicles,coefficients " +
                    "and to write file results to run application");
            return;
        }
        try {
            ParserFactory parserFactory = new ParserFactory();
            WriterFactory writerFactory = new WriterFactory();
            Parser csvParser = parserFactory.getParser("CSV");
            Parser jsonParser = parserFactory.getParser("JSON");
            Writer CSVWriter = writerFactory.getWriter("CSV");

            ParsingConf CSVparsingConf = new ParsingConf(',', '"', 1, null, VEHICLE);
            ParsingConf JSONparsingConf = new ParsingConf(',', '"', 0, "data", COEFFICIENT);

            //parsing data
            CascoDataSet cascoDataSet = new CascoDataSet();
            csvParser.parseFile(args[0], CSVparsingConf, cascoDataSet);
            jsonParser.parseFile(args[1], JSONparsingConf, cascoDataSet);

            //filtering risk parameters
            Set<String> calcRisks = new HashSet<>();
            calcRisks.add("vehicle_value");
            calcRisks.add("previous_indemnity");
            Set<RiskParameter> riskparams = cascoDataSet.riskparameters.stream()
                    .filter(s -> calcRisks.contains(s.getType()))
                    .collect(Collectors.toSet());
            RiskParameter currentValue = new RiskParameter("current_value", "purchase_price", 1, true);
            riskparams.add(currentValue);
            //saving risks
            riskParameterService.batchRisks(riskparams);
            addRiskParameterService.batchAddRisks(cascoDataSet.additionalRiskparameters);
            //calculating casco insurance
            cascoCalculator.calculateAnnualCascoForVehiclesSet(cascoDataSet.vehicles, riskparams, cascoDataSet.additionalRiskparameters);

            //writing results to file
            WriterConf writerConf = new WriterConf(',', '\u0000');
            CSVWriter.writeToFile(args[2], writerConf, cascoDataSet.vehicles);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        LOG.info("EXECUTING: runner finished");
    }
}
