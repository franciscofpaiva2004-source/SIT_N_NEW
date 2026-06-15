package pt.pa.Factory;

import com.brunomnsilva.smartgraph.graph.Edge;
import pt.pa.model.Route;
import pt.pa.strategy.CostCalculationStrategy;
import pt.pa.strategy.DistanceStrategy;
import pt.pa.strategy.DurationStrategy;
import pt.pa.strategy.SustainabilityStrategy;

public class CreateStrategyFactory implements StrategyFactory {

    @Override
    public CostCalculationStrategy createStrategy(String strategyType) {
        return switch (strategyType) {
            case "Distância" -> new DistanceStrategy();
            case "Duração" -> new DurationStrategy();
            case "Sustentabilidade" -> new SustainabilityStrategy();
            default -> throw new IllegalArgumentException("Tipo de estratégia inválido: " + strategyType);
        };
    }
}