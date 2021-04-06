package com.example.insuranceapi.parser;

import com.example.insuranceapi.model.AdditionalRiskParameter;
import com.example.insuranceapi.model.CascoDataSet;
import com.example.insuranceapi.model.RiskParameter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class JSONParser implements Parser {
    /**
     * Parses File to different data types sets
     *
     * @param pathToFile
     * @param parsingConf
     * @param cascoDataSet
     * @throws IOException
     * @throws IllegalArgumentException
     */
    public void parseFile(String pathToFile, ParsingConf parsingConf, CascoDataSet cascoDataSet) throws IOException, IllegalArgumentException {

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode root = objectMapper.readTree(new File(pathToFile));
        JsonNode nodeRoot = root.path(parsingConf.jsonRootNodeName);
        Iterator<Map.Entry<String, JsonNode>> fields = nodeRoot.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String fieldName = field.getKey();
            JsonNode fieldValue = field.getValue();
            switch (parsingConf.dataType) {
                case COEFFICIENT:
                    parseNode(fieldName, fieldValue, cascoDataSet);
                    continue;
                default:
                    throw new IllegalArgumentException("Data type is not valid");
            }
        }
    }

    /**
     * Parses node according to their titles into different datasets
     *
     * @param nodeName
     * @param node
     * @param cascoDataSet
     * @throws IllegalArgumentException
     */
    public void parseNode(String nodeName, JsonNode node, CascoDataSet cascoDataSet) throws IllegalArgumentException {
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String fieldName = field.getKey();
            JsonNode fieldValue = field.getValue();
            switch (nodeName) {
                case "coefficients":
                    RiskParameter riskParameter =
                            new RiskParameter(fieldName, fieldName, fieldValue.asDouble(), false);
                    cascoDataSet.riskparameters.add(riskParameter);
                    continue;
                case "make_coefficients", "avg_purchase_price":
                    AdditionalRiskParameter addRiskParam =
                            new AdditionalRiskParameter(fieldName, nodeName, fieldValue.asDouble());
                    cascoDataSet.additionalRiskparameters.add(addRiskParam);
                    continue;
                default:
                    throw new IllegalArgumentException("Coefficient type is not valid");
            }
        }
    }
}
