package pt.pa.strategy;

import com.brunomnsilva.smartgraph.graph.Edge;
import pt.pa.model.Route;

public class DurationStrategy implements CostCalculationStrategy {
    @Override
    public double calculateCost(Edge<Route, ?> edge, String transportType) {
        Route route = edge.element();
        return switch (transportType) {
            case "Comboio" -> route.getDurationTrain() != null ? route.getDurationTrain() : Double.MAX_VALUE;
            case "Autocarro" -> route.getDurationBus() != null ? route.getDurationBus() : Double.MAX_VALUE;
            case "Barco" -> route.getDurationBoat() != null ? route.getDurationBoat() : Double.MAX_VALUE;
            case "Caminhada" -> route.getDurationWalk() != null ? route.getDurationWalk() : Double.MAX_VALUE;
            case "Bicicleta" -> route.getDurationBicycle() != null ? route.getDurationBicycle() : Double.MAX_VALUE;
            default -> Double.MAX_VALUE;
        };
    }
}

