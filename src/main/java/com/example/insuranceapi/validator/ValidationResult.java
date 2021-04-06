package com.example.insuranceapi.validator;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    public List<String> validatedLine;
    public boolean validatedResult;

    public ValidationResult() {
        this.validatedLine = new ArrayList<>();
        this.validatedResult = false;
    }
}
