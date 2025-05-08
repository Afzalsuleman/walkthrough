package uk.tw.energy.service;

import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.PricePlan;
import java.math.BigDecimal;
import java.util.List;

public interface PriceCalculationStrategy {
    BigDecimal calculateCost(List<ElectricityReading> readings, PricePlan pricePlan);
}