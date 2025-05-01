package uk.tw.energy.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.service.MeterReadingService;
import uk.tw.energy.service.PricePlanService;

@RestController
@RequestMapping("/getCost")
public class CostController {

    @Autowired
    private MeterReadingService meterReadingService;

    @Autowired
    private PricePlanService pricePlanService;

    @GetMapping("/getCostOfLastWeek/{smartMeterID}")
    public ResponseEntity<String> getCostOfLastWeek(@PathVariable String smartMeterID) {
        Optional<List<ElectricityReading>> readingsOptional = meterReadingService.getReadings(smartMeterID);
        ArrayList<ElectricityReading> electricityReadings = new ArrayList<>();
        readingsOptional.ifPresent(electricityReadings::addAll);
        BigDecimal bigDecimal = !electricityReadings.isEmpty()
                ? electricityReadings.stream()
                        .map(ElectricityReading::reading)
                        .reduce(BigDecimal::add)
                        .get()
                : BigDecimal.ZERO;
        return new ResponseEntity<>(bigDecimal.toString(), HttpStatus.OK);
    }
}
