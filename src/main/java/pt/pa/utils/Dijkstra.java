package pt.pa.utils;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartStylableNode;
import pt.pa.graph.TransportMap;
import pt.pa.model.Route;
import pt.pa.model.Stop;
import pt.pa.strategy.CostCalculationStrategy;

import java.util.*;

public class Dijkstra {

    private double lastCost;

    private Vertex<Stop> minDistance(Map<Vertex<Stop>, Double> dist, Set<Vertex<Stop>> sptSet) {
        double min = Double.MAX_VALUE;
        Vertex<Stop> minVertex = null;

        for (Map.Entry<Vertex<Stop>, Double> entry : dist.entrySet()) {
            Vertex<Stop> vertex = entry.getKey();
            double distance = entry.getValue();
            if (!sptSet.contains(vertex) && distance <= min) {
                min = distance;
                minVertex = vertex;
            }
        }

        if (minVertex == null) {
            System.err.println("Erro: nenhum vértice encontrado com menor distância.");
        }

        return minVertex;
    }

    public Map<Vertex<Stop>, Vertex<Stop>> calculateShortestPath(
            TransportMap map,
            Vertex<Stop> src,
            Vertex<Stop> last,
            CostCalculationStrategy strategy,
            List<String> transportes) {

        Map<Vertex<Stop>, Double> dist = new HashMap<>();
        Map<Vertex<Stop>, Vertex<Stop>> predecessors = new HashMap<>();
        Set<Vertex<Stop>> sptSet = new HashSet<>();

        for (Vertex<Stop> vertex : map.vertices()) {
            dist.put(vertex, Double.MAX_VALUE);
        }
        dist.put(src, 0.0);

        while (!sptSet.contains(last)) {
            Vertex<Stop> u = minDistance(dist, sptSet);
            if (u == null || dist.get(u) == Double.MAX_VALUE) {
                break;
            }
            sptSet.add(u);

            for (Vertex<Stop> neighbor : map.getNeighbours(u)) {
                double weight = map.getDistance(u, neighbor, strategy, transportes);
                if (!sptSet.contains(neighbor) && dist.get(u) + weight < dist.get(neighbor)) {
                    dist.put(neighbor, dist.get(u) + weight);
                    predecessors.put(neighbor, u);
                }
            }
        }
        return predecessors;
    }


    public List<Stop> reconstructPathWithVisualization(
            Map<Vertex<Stop>, Vertex<Stop>> predecessors,
            Vertex<Stop> src,
            Vertex<Stop> last,
            TransportMap map,
            CostCalculationStrategy strategy,
            List<String> transportes,
            SmartGraphPanel<Stop, Route> graphView) {

        for (Edge<Route, Stop> edge : map.edges()) {
            SmartStylableNode stylableEdge = graphView.getStylableEdge(edge);
            if (stylableEdge != null) {
                stylableEdge.setStyleClass("edge");
            }
        }

        List<Stop> path = new LinkedList<>();
        double totalCost = 0.0;
        Vertex<Stop> current = last;

        while (current != null && predecessors.containsKey(current)) {
            path.add(0, current.element());
            Vertex<Stop> previous = predecessors.get(current);

            if (previous != null) {
                totalCost += map.getDistance(previous, current, strategy, transportes);

                Edge<Route, Stop> selectedEdge = null;
                double minCost = Double.MAX_VALUE;
                String selectedTransport = null;

                for (Edge<Route, Stop> edge : map.incidentEdges(previous)) {
                    Vertex<Stop>[] vertices = edge.vertices();
                    if ((vertices[0].equals(previous) && vertices[1].equals(current)) ||
                            (vertices[1].equals(previous) && vertices[0].equals(current))) {

                        List<String> availableTransport = getAvailableTransportForEdge(transportes, edge);

                        if (!availableTransport.isEmpty()) {
                            double edgeCost = map.getDistance(previous, current, strategy, availableTransport);
                            if (edgeCost < minCost) {
                                minCost = edgeCost;
                                selectedEdge = edge;
                                selectedTransport = availableTransport.get(0);
                            }
                        }
                    }
                }

                if (selectedEdge != null) {
                    String edgeStyleClass = getEdgeStyleClassForEdge(selectedTransport, selectedEdge);
                    SmartStylableNode stylableEdge = graphView.getStylableEdge(selectedEdge);
                    if (stylableEdge != null) {
                        stylableEdge.setStyleClass(edgeStyleClass);
                    }
                }
            }
            current = previous;
        }
        path.add(0, src.element());
        setLastCost(totalCost);

        if (strategy.equals("Duração")) {
            double minutos = totalCost % 60;
            double horas = (totalCost - minutos) / 60;
            if (horas == 0) {
                System.out.println("Custo total do percurso: " + Math.round(minutos) + "m");
            } else {
                System.out.println("Custo total do percurso: " + Math.round(horas) + "h" + Math.round(minutos) + "m");
            }
        }

        graphView.update();

        return path;
    }

    public List<String> getAvailableTransportForEdge(List<String> transportes, Edge<Route, Stop> edge) {
        List<String> availableTransport = new ArrayList<>();
        for (String transporte : transportes) {
            if (isTransportAvailableForEdge(transporte, edge)) {
                availableTransport.add(transporte);
            }
        }

        return availableTransport;
    }

    public boolean isTransportAvailableForEdge(String transporte, Edge<Route, Stop> edge) {
        if(transporte.equals("Barco") && edge.element().getCostBoat() != null){
            return true;
        }
        if(transporte.equals("Caminhada") && edge.element().getCostWalk() != null){
            return true;
        }
        if(transporte.equals("Autocarro") && edge.element().getCostBus() != null){
            return true;
        }
        if(transporte.equals("Comboio") && edge.element().getCostTrain() != null){
            return true;
        }
        if(transporte.equals("Bicicleta") && edge.element().getCostBicycle() != null){
            return true;
        }
        return false;
    }

    public String getEdgeStyleClassForEdge(String selectedTransport, Edge<Route, Stop> edge) {
        return switch (selectedTransport.toLowerCase()) {
            case "comboio" -> "edge-train";
            case "autocarro" -> "edge-bus";
            case "barco" -> "edge-boat";
            case "caminhada" -> "edge-walk";
            case "bicicleta" -> "edge-bicycle";
            default -> "edge";
        };
    }



    public double getLastCost() {
        return lastCost;
    }

    public void setLastCost(double lastCost) {
        this.lastCost = lastCost;
    }
}