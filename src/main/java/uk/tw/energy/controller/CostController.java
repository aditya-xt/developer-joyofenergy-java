package uk.tw.energy.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.service.MeterReadingService;
import uk.tw.energy.service.PricePlanService;

@RestController
@RequestMapping("/getCost")
public class CostController {

    private MeterReadingService meterReadingService;

    private PricePlanService pricePlanService;

    public CostController(MeterReadingService meterReadingService, PricePlanService pricePlanService) {
        this.meterReadingService = meterReadingService;
        this.pricePlanService = pricePlanService;
    }

    @GetMapping("/getCostOfLastWeek/{smartMeterID}")
    public ResponseEntity<String> getCostOfLastWeek(String smartMeterID) {
        Optional<List<ElectricityReading>> readingsOptional = meterReadingService.getReadings(smartMeterID);
        ArrayList<ElectricityReading> electricityReadings = new ArrayList<>();
        readingsOptional.ifPresent(electricityReadings::addAll);
        BigDecimal bigDecimal = !electricityReadings.isEmpty()
                ? electricityReadings.stream()
                        .map(ElectricityReading::reading)
                        .reduce(BigDecimal::add)
                        .get()
                : BigDecimal.ZERO;
        return new ResponseEntity<>(readingsOptional.toString(), HttpStatus.OK);
    }
}
