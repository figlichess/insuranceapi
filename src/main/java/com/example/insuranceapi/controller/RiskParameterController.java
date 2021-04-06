package com.example.insuranceapi.controller;

import com.example.insuranceapi.converter.DTOConverter;
import com.example.insuranceapi.exception.ResourceNotFoundException;
import com.example.insuranceapi.model.RiskParameter;
import com.example.insuranceapi.service.RiskParameterService;
import dto.RiskParamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/api/v1/riskparams")
public class RiskParameterController {

    @Autowired
    private RiskParameterService riskParameterService;

    @Autowired
    private DTOConverter dtoConverter;

    @GetMapping("/list")
    public Page<RiskParamDTO> list(@RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "id");
        Page<RiskParameter> pageResult = riskParameterService.getRiskParameterPages(pageRequest);
        List<RiskParamDTO> riskParamDTOs = pageResult
                .stream()
                .map(l -> dtoConverter.convertToRiskParamDTO(l))
                .collect(toList());

        return new PageImpl<>(riskParamDTOs, pageRequest, pageResult.getTotalElements());
    }

    @GetMapping("/allriskparams")
    public List<RiskParamDTO> getAllRiskParameter() {
        List<RiskParameter> riskParameters = riskParameterService.getRiskParameters();
        List<RiskParamDTO> riskParamDTOs = riskParameters
                .stream()
                .map(l -> dtoConverter.convertToRiskParamDTO(l))
                .collect(toList());
        return riskParamDTOs;
    }

    @GetMapping("/{id}")
    public ResponseEntity<RiskParamDTO> getRiskParameterByID(@PathVariable(value = "id") Integer riskParameterId) throws ResourceNotFoundException {
        RiskParameter riskParameter = riskParameterService.getRiskParameter(riskParameterId);
        return ResponseEntity.ok().body(dtoConverter.convertToRiskParamDTO(riskParameter));
    }

    @PostMapping
    public RiskParameter createRiskParameter(@RequestBody RiskParamDTO riskParamDTO) {
        return riskParameterService.saveRiskParameter(dtoConverter.convertToRiskParameter(riskParamDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RiskParamDTO> updateRiskParameter(@Valid @RequestBody RiskParamDTO riskParamDTO, @PathVariable(value = "id") Integer riskParameterId) throws ResourceNotFoundException {
        RiskParameter riskParameter = dtoConverter.convertToRiskParameter(riskParamDTO);
        riskParameter.setId(riskParameterId);
        RiskParameter updatedRiskParameter = riskParameterService.updateRiskParameter(riskParameterId, riskParameter);
        return ResponseEntity.ok(dtoConverter.convertToRiskParamDTO(updatedRiskParameter));
    }

    @DeleteMapping("/{riskparamId}")
    public Map<String, Boolean> deleteRiskParameter(@PathVariable("riskparamId") Integer riskParameterId) throws Exception {
        riskParameterService.deleteRiskParameter(riskParameterId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
