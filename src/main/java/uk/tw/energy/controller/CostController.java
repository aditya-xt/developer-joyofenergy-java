package uk.tw.energy.controller;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

import javax.xml.transform.Result;

@RestController
@RequestMapping("/getCost")
public class CostController {

    @Autowired
    private MeterReadingService meterReadingService;

    @Autowired
    private PricePlanService pricePlanService;

    @GetMapping("/getCostOfLastWeek/{smartMeterID}")
    public ResponseEntity<String> getCostOfLastWeek(@PathVariable String smartMeterID) {
//        Double avg = getAverage(smartMeterID);
        String avg = getAverageString(smartMeterID);
        return new ResponseEntity<>(avg, HttpStatus.OK);
    }

    private Double getAverage(String smartMeterID) {
        Optional<List<ElectricityReading>> readingsOptional = meterReadingService.getReadings(smartMeterID);
        ArrayList<ElectricityReading> electricityReadings = new ArrayList<>();
        readingsOptional.ifPresent(electricityReadings::addAll);
        Double avg = null;
        if (!electricityReadings.isEmpty()) {
            BigDecimal bigDecimal = electricityReadings.stream()
                    .map(ElectricityReading::reading)
                    .reduce(BigDecimal::add)
                    .get();
            avg = Double.parseDouble(bigDecimal.toString()) / electricityReadings.size();
        }
        avg = avg != null ? avg : 0 ;
        return avg;
    }

    private String getAverageString(String smartMeterID) {
        Optional<List<ElectricityReading>> readingsOptional = meterReadingService.getReadings(smartMeterID);
        ArrayList<ElectricityReading> electricityReadings = new ArrayList<>();
        readingsOptional.ifPresent(electricityReadings::addAll);
        Double avg = null;
        String result = "";
        Instant instant = Instant.now();
        Instant instant1 = Instant.now().minus(5, ChronoUnit.DAYS);
        if (!electricityReadings.isEmpty()) {
            BigDecimal bigDecimal = electricityReadings.stream()
                    .map(ElectricityReading::reading)
                    .reduce(BigDecimal::add)
                    .get();
            avg = Double.parseDouble(bigDecimal.toString()) / electricityReadings.size();

            result = "{" +
                    "readingsOptional : "+readingsOptional+"\n"+
                    "bigDecimal : "+bigDecimal+"\n"+
                    "avg : "+avg+"\n"+
                    "electricityReadings : "+electricityReadings+"\n"+
                    "instant now : "+instant+"\n"+
                    "instant1 7 days ago date : "+instant1+"\n"+
                    "}";
        }
        avg = avg != null ? avg : 0 ;
        return result;
    }
}
