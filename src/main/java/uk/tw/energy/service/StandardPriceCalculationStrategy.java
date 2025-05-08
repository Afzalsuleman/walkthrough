package uk.tw.energy.service;

import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.PricePlan;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;

public class StandardPriceCalculationStrategy implements PriceCalculationStrategy {
    @Override
    public BigDecimal calculateCost(List<ElectricityReading> readings, PricePlan pricePlan) {
        if (readings == null || readings.size() < 2) {
            return BigDecimal.ZERO;
        }
        BigDecimal averageReading = readings.stream()
            .map(ElectricityReading::reading)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(readings.size()), RoundingMode.HALF_UP);

        ElectricityReading first = readings.stream()
            .min(Comparator.comparing(ElectricityReading::time))
            .get();
        ElectricityReading last = readings.stream()
            .max(Comparator.comparing(ElectricityReading::time))
            .get();

        BigDecimal hours = BigDecimal.valueOf(Duration.between(first.time(), last.time()).getSeconds() / 3600.0);
        if (hours.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal energyConsumed = averageReading.multiply(hours);
        return energyConsumed.multiply(pricePlan.getUnitRate());
    }
}