package uk.tw.energy.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.PricePlan;

@Service
public class PricePlanService {

    private final List<PricePlan> pricePlans;
    private final MeterReadingService meterReadingService;
    private final PriceCalculationStrategy priceCalculationStrategy;

    public PricePlanService(List<PricePlan> pricePlans, MeterReadingService meterReadingService, PriceCalculationStrategy priceCalculationStrategy) {
        this.pricePlans = pricePlans;
        this.meterReadingService = meterReadingService;
        this.priceCalculationStrategy = priceCalculationStrategy;
    }

    public Optional<Map<String, BigDecimal>> getConsumptionCostOfElectricityReadingsForEachPricePlan(
            String smartMeterId) {
        Optional<List<ElectricityReading>> electricityReadings = meterReadingService.getReadings(smartMeterId);

        if (!electricityReadings.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(pricePlans.stream()
                .collect(Collectors.toMap(
                    PricePlan::getPlanName,
                    t -> priceCalculationStrategy.calculateCost(electricityReadings.get(), t)
                )));
    }
}
