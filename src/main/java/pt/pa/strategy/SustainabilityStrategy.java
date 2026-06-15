package pt.pa.strategy;

import com.brunomnsilva.smartgraph.graph.Edge;
import pt.pa.model.Route;

public class SustainabilityStrategy implements CostCalculationStrategy {
    @Override
    public double calculateCost(Edge<Route, ?> edge, String transportType) {
        Route route = edge.element();
        return switch (transportType) {
            case "Comboio" -> route.getCostTrain() != null ? route.getCostTrain() : Double.MAX_VALUE;
            case "Autocarro" -> route.getCostBus() != null ? route.getCostBus() : Double.MAX_VALUE;
            case "Barco" -> route.getCostBoat() != null ? route.getCostBoat() : Double.MAX_VALUE;
            case "Caminhada" -> route.getCostWalk() != null ? route.getCostWalk() : Double.MAX_VALUE;
            case "Bicicleta" -> route.getCostBicycle() != null ? route.getCostBicycle() : Double.MAX_VALUE;
            default -> Double.MAX_VALUE;
        };
    }
}
