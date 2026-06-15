package pt.pa.Factory;

import com.brunomnsilva.smartgraph.graph.Edge;
import pt.pa.model.Route;
import pt.pa.strategy.CostCalculationStrategy;

public interface StrategyFactory {
    CostCalculationStrategy createStrategy(String strategyType);
}