package pt.pa.strategy;

import com.brunomnsilva.smartgraph.graph.Edge;
import pt.pa.model.Route;

public class DistanceStrategy implements CostCalculationStrategy {
    @Override
    public double calculateCost(Edge<Route, ?> edge, String transportType) {
        Route route = edge.element();
        return switch (transportType) {
            case "Comboio" -> route.getDistanceTrain() != null ? route.getDistanceTrain() : Double.MAX_VALUE;
            case "Autocarro" -> route.getDistanceBus() != null ? route.getDistanceBus() : Double.MAX_VALUE;
            case "Barco" -> route.getDistanceBoat() != null ? route.getDistanceBoat() : Double.MAX_VALUE;
            case "Caminhada" -> route.getDistanceWalk() != null ? route.getDistanceWalk() : Double.MAX_VALUE;
            case "Bicicleta" -> route.getDistanceBicycle() != null ? route.getDistanceBicycle() : Double.MAX_VALUE;
            default -> Double.MAX_VALUE;
        };
    }
}

