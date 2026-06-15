package pt.pa.graph;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Vertex;
import pt.pa.model.Route;
import pt.pa.model.Stop;
import pt.pa.strategy.CostCalculationStrategy;
import pt.pa.utils.DataSetUtil;
import pt.pa.utils.Observer;
import pt.pa.utils.Subject;

import java.util.*;

/**
 * Representa um sistema integrado de transportes modelado como um grafo.
 *
 * <p>A classe utiliza a estrutura de dados de grafo estendida de {@code GraphEdgeList},
 * onde:
 * <ul>
 *     <li><b>Vértices:</b> Representados por objetos da classe {@code Stop}, que indicam as paragens.</li>
 *     <li><b>Arestas:</b> Representadas por objetos da classe {@code Route}, que indicam as ligações entre paragens.</li>
 * </ul>
 *
 * <p>A classe importa dados de ficheiros CSV para configurar o grafo, atribuindo as coordenadas
 * (x, y) das paragens e conectando-as conforme as rotas disponíveis.</p>
 *
 * <h3>Estrutura do Sistema:</h3>
 * <ul>
 *     <li>{@code stopList}: Lista de paragens carregadas a partir do ficheiro CSV.</li>
 *     <li>{@code routeList}: Lista de rotas carregadas a partir do ficheiro CSV.</li>
 *     <li>{@code xyList}: Lista de coordenadas (x, y) associadas às paragens.</li>
 * </ul>
 *
 * <h3>Funcionamento:</h3>
 * <ol>
 *     <li>Importa os dados de ficheiros CSV para listas de paragens, rotas e coordenadas.</li>
 *     <li>Atribui as coordenadas (x, y) às paragens com base na correspondência dos códigos.</li>
 *     <li>Insere as paragens como vértices no grafo.</li>
 *     <li>Cria arestas entre as paragens conectadas por rotas.</li>
 * </ol>
 *
 * <p><b>Exemplo de Utilização:</b></p>
 * <pre>{@code
 * TransportMap transportMap = new TransportMap();
 * transportMap.initialModel();
 * }</pre>
 *
 * @see Stop
 * @see Route
 * @see GraphEdgeList
 */
public class TransportMap extends GraphEdgeList<Stop, Route> implements Subject {

    /**
     * Lista de rotas importadas do ficheiro CSV.
     */
    private  List<Route> routeList;

    /**
     * Lista de paragens importadas do ficheiro CSV.
     */
    private final List<Stop> stopList;

    /**
     * Lista de coordenadas (x, y) associadas às paragens.
     */
    private final List<Stop> xyList;


    private final List<Observer> observers = new ArrayList<>();

    /**
     * Construtor padrão da classe {@code TransportMap}.
     *
     * <p>Importa os dados das paragens, rotas e coordenadas (x, y) a partir de ficheiros CSV.</p>
     */
    public TransportMap() {
        this.routeList = DataSetUtil.importCSV("src/main/resources/dataset/routes.csv", Route.class);
        this.stopList = DataSetUtil.importCSV("src/main/resources/dataset/stops.csv", Stop.class);
        this.xyList = DataSetUtil.importCSV("src/main/resources/dataset/xy.csv", Stop.class);
        initialModel();
    }

    /**
     * Inicializa o modelo de grafo a partir dos dados importados.
     *
     * <p>O metodo realiza as seguintes etapas:
     * <ol>
     *     <li>Atualiza as coordenadas (x, y) das paragens com base na lista {@code xyList}.</li>
     *     <li>Adiciona todas as paragens como vértices no grafo.</li>
     *     <li>Adiciona arestas ao grafo, conectando as paragens conforme as rotas na lista {@code routeList}.</li>
     * </ol>
     * </p>
     */

    public void initialModel() {
        for (Stop stop : stopList) {
            for (Stop xy : xyList) {
                if (xy.getStopCode().equals(stop.getStopCode())) {
                    stop.setPosX(xy.getPosX());
                    stop.setPosY(xy.getPosY());
                }
            }
        }
        for (Stop stop : stopList) {
            insertVertex(stop);
        }
        for (Route route : routeList) {
            Stop start = findStopByCode(route.getStopCodeStart());
            Stop end = findStopByCode(route.getStopCodeEnd());
            if (start != null && end != null) {
                insertEdge(start, end, route);
            }
        }
    }

    private Stop findStopByCode(String code) {
        for (Vertex<Stop> vertex : vertices()) {
            if (vertex.element().getStopCode().equals(code)) {
                return vertex.element();
            }
        }
        return null;
    }

    public int getTotalStops() {
        return vertices().size();
    }


    public int getTotalRoutes() {
        return edges().size();
    }

    public int getIsolatedStops() {
        return (int) vertices().stream().filter(v -> incidentEdges(v).isEmpty()).count();
    }


    public List<Map.Entry<Stop, Integer>> getCentralityList() {
        List<Map.Entry<Stop, Integer>> centralityList = new ArrayList<>();
        Map<Stop, Integer> centralityMap = new HashMap<>();

        for (Vertex<Stop> vertex : vertices()) {
            Stop stop = vertex.element();
            int adjacentCount = incidentEdges(vertex).size();
            centralityMap.put(stop, adjacentCount);
        }

        centralityList.addAll(centralityMap.entrySet());
        centralityList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        return centralityList;
    }

    public List<Map.Entry<Stop, Integer>> getTop5CentralStops() {
        List<Map.Entry<Stop, Integer>> centralityList = getCentralityList();
        return centralityList.stream().limit(5).toList();
    }


    public List<Stop> getAccessibleStops(String stopName, int distance) {
        List<Stop> accessibleStops = new ArrayList<>();
        Map<String, Integer> distances = new HashMap<>();
        Queue<Vertex<Stop>> queue = new LinkedList<>();
        Vertex<Stop> startVertex = findVertexByName(stopName);
        queue.add(startVertex);
        distances.put(stopName, 0);

        while (!queue.isEmpty()) {
            Vertex<Stop> currentVertex = queue.poll();
            int currentDistance = distances.get(currentVertex.element().getStopName());
            if (currentDistance >= distance) {
                continue;
            }
            for (Edge<Route, Stop> edge : incidentEdges(currentVertex)) {
                Vertex<Stop>[] connectedVertices = edge.vertices();
                Vertex<Stop> neighborVertex = (connectedVertices[0] == currentVertex) ? connectedVertices[1] : connectedVertices[0];
                if (!distances.containsKey(neighborVertex.element().getStopName())) {
                    distances.put(neighborVertex.element().getStopName(), currentDistance + 1);
                    queue.add(neighborVertex);
                    accessibleStops.add(neighborVertex.element());
                }
            }
        }
        return accessibleStops;
    }

    public double getDistance(Vertex<Stop> v1, Vertex<Stop> v2, CostCalculationStrategy strategy, List<String> transportes) {
        double minCost = Double.MAX_VALUE;

        for (Edge<Route, Stop> edge : incidentEdges(v1)) {
            for (Edge<Route, Stop> edge2 : incidentEdges(v2)) {
                if (edge.equals(edge2)) {
                    for (String transporte : transportes) {
                        minCost = Math.min(minCost, strategy.calculateCost(edge, transporte));
                    }
                }
            }
        }

        return minCost == Double.MAX_VALUE ? Double.MAX_VALUE : minCost;
    }


    public List <Route> getRouteList(){
        return routeList;
    }



    public List<Vertex<Stop>> getNeighbours(Vertex<Stop> v1){
        List<Vertex<Stop>> neighbours = new ArrayList<>();
        for(Vertex<Stop> v : vertices()) {
            for(Edge<Route, Stop> edge : incidentEdges(v)) {
                for(Edge<Route, Stop> edge1 : incidentEdges(v1)) {
                    if(edge1.equals(edge) && v!= v1) {
                        neighbours.add(v);
                    }
                }
            }
        }
        return neighbours;
    }

    public Vertex<Stop> findVertexByName(String stopName) {
        for (Vertex<Stop> vertex : vertices()) {
            if (vertex.element().getStopName().equals(stopName)) {
                return vertex;
            }
        }
        return null;
    }
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
    @Override
    public Edge<Route, Stop> insertEdge(Stop start, Stop end, Route route) {
        super.insertEdge(start, end, route);
        notifyObservers();
        return null;
    }
    @Override
    public Route removeEdge(Edge<Route, Stop> edge) {
        super.removeEdge(edge);
        notifyObservers();
        return null;
    }


}


