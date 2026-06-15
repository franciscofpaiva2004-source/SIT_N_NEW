package pt.pa.strategy;

import com.brunomnsilva.smartgraph.graph.Edge;
import pt.pa.model.Route;

public interface CostCalculationStrategy {
    double calculateCost(Edge<Route, ?> edge, String transportType);
}
