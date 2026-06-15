
package pt.pa.view;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import pt.pa.graph.TransportMap;
import pt.pa.model.Route;
import pt.pa.model.Stop;
import pt.pa.utils.Memento;
import pt.pa.utils.Observer;
import pt.pa.utils.Originator;
import pt.pa.utils.PropertiesUtil;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MapView extends BorderPane implements Originator, Observer {

    public final SmartGraphPanel<Stop, Route> graphView;

    public MapView(Graph<Stop, Route> graph) {
        this.graphView = initializeGraphPanel(graph);
        doLayout();
        setupClickEvents();
    }
    @Override
    public void update() {
        graphView.update();
    }

    private SmartGraphPanel<Stop, Route> initializeGraphPanel(Graph<Stop, Route> graph) {
        try {
            InputStream smartgraphProperties = getClass().getClassLoader().getResourceAsStream("smartgraph.properties");
            URL css = MapView.class.getClassLoader().getResource("styles/smartgraph.css");

            if (css == null || smartgraphProperties == null) {
                throw new RuntimeException("Configuração de estilos ou propriedades não encontrada.");
            }

            SmartGraphPanel<Stop, Route> panel = new SmartGraphPanel<>(
                    graph,
                    new SmartGraphProperties(smartgraphProperties),
                    new SmartPlacementStrategy() {
                        @Override
                        public <V, E> void place(double v, double v1, SmartGraphPanel<V, E> smartGraphPanel) {
                            if (smartGraphPanel != null) {
                                SmartGraphPanel<Stop, Route> panelWithStop = (SmartGraphPanel<Stop, Route>) smartGraphPanel;

                                for (Vertex<Stop> v2 : graph.vertices()) {
                                    panelWithStop.setVertexPosition(v2, v2.element().getPosX(), v2.element().getPosY());
                                }
                            }
                        }
                    },
                    css.toURI()
            );


            panel.setMaxHeight(Integer.parseInt(PropertiesUtil.getInstance().getProperty("map.height")));
            panel.setMaxWidth(Integer.parseInt(PropertiesUtil.getInstance().getProperty("map.width")));

            return panel;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inicializar o painel do grafo.", e);
        }

    }

    private void doLayout() {

        ScrollPane scrollPane = new ScrollPane(this.graphView);
        scrollPane.setPannable(false);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);

        setCenter(scrollPane);
    }

    private void setupClickEvents() {
        graphView.setVertexDoubleClickAction(vertex ->
                showStopInfo(vertex.getUnderlyingVertex().element()));
        graphView.setEdgeDoubleClickAction(edge ->
                showRouteInfo(edge.getUnderlyingEdge().element()));
    }

    private void showStopInfo(Stop stop) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informações da Paragem");
        alert.setHeaderText("Detalhes da Paragem");
        alert.setContentText(String.format(
                "Código: %s\nNome: %s\nLatitude: %.4f\nLongitude: %.4f\nCoordenadas: (%d, %d)",
                stop.getStopCode(),
                stop.getStopName(),
                stop.getLatitude(),
                stop.getLongitude(),
                stop.getPosX(),
                stop.getPosY()
        ));
        alert.showAndWait();
    }

    private void showRouteInfo(Route route) {
        TableView<Object[]> table = new TableView<>();

        TableColumn<Object[], String> costTypeColumn = new TableColumn<>("Tipo de Custo");
        costTypeColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper((String) data.getValue()[0]));

        TableColumn<Object[], Double> trainColumn = new TableColumn<>("Comboio");
        trainColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>((Double) data.getValue()[1]));

        TableColumn<Object[], Double> busColumn = new TableColumn<>("Autocarro");
        busColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>((Double) data.getValue()[2]));

        TableColumn<Object[], Double> boatColumn = new TableColumn<>("Barco");
        boatColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>((Double) data.getValue()[3]));

        TableColumn<Object[], Double> walkColumn = new TableColumn<>("Caminhada");
        walkColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>((Double) data.getValue()[4]));

        TableColumn<Object[], Double> bicycleColumn = new TableColumn<>("Bicicleta");
        bicycleColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>((Double) data.getValue()[5]));

        table.getColumns().addAll(costTypeColumn, trainColumn, busColumn, boatColumn, walkColumn, bicycleColumn);

        table.getItems().addAll(
                new Object[]{"Custo (CO2)", route.getCostTrain(), route.getCostBus(), route.getCostBoat(), route.getCostWalk(), route.getCostBicycle()},
                new Object[]{"Distância (km)", route.getDistanceTrain(), route.getDistanceBus(), route.getDistanceBoat(), route.getDistanceWalk(), route.getDistanceBicycle()},
                new Object[]{"Duração (min)", route.getDurationTrain(), route.getDurationBus(), route.getDurationBoat(), route.getDurationWalk(), route.getDurationBicycle()}
        );

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Informações da Rota");
        dialog.getDialogPane().setContent(new VBox(new Label("Custos da Rota"), table));
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    public SmartGraphPanel<Stop, Route> getGraphView() {
        return graphView;
    }

    public Memento createMemento() {
        List<Route> clonedRoutes = new ArrayList<>();
        for (Edge<Route, Stop> edge : graphView.getModel().edges()) {
            clonedRoutes.add(Route.cloneRoute(edge.element()));
        }
        return new MapViewMemento(this.graphView.getModel().edges(),clonedRoutes);
    }

    @Override
    public void setMemento(Memento savedState) {
        if (savedState instanceof MapViewMemento) {
            Graph<Stop, Route> model = this.graphView.getModel();

            for (Edge<Route, Stop> edge : new ArrayList<>(model.edges())) {
                model.removeEdge(edge);
            }

            Collection<Route> clonedRoutes = ((MapViewMemento) savedState).routeList;
            Collection<Edge<Route, Stop>> restoredEdges = ((MapViewMemento) savedState).edgeList;

            //Substituir as rotas salvas nas edges que foram copiadas
            for (Edge<Route, Stop> edge : restoredEdges) {
                for (Route restoredRoute : clonedRoutes) {
                    //Se forem iguais
                    if (restoredRoute.getStopCodeEnd().equals(edge.element().getStopCodeEnd()) &&
                            restoredRoute.getStopCodeStart().equals(edge.element().getStopCodeStart())) {
                        model.insertEdge(
                                findStopByCode(edge.element().getStopCodeStart()),
                                findStopByCode(edge.element().getStopCodeEnd()),
                                restoredRoute
                        );
                    }
                }
            }
        }
    }


    private Stop findStopByCode(String code) {
        for (Vertex<Stop> vertex : graphView.getModel().vertices()) {
            if (vertex.element().getStopCode().equals(code)) {
                return vertex.element();
            }
        }
        return null;
    }
    public class MapViewMemento implements Memento {
        private Collection<Edge<Route, Stop>> edgeList;
        private Collection<Route> routeList;


        public MapViewMemento(Collection<Edge<Route, Stop>> edgeList,Collection<Route> routeList) {
            this.edgeList = new ArrayList<>(edgeList);
            this.routeList = new ArrayList<>(routeList);
            }
    @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (Edge<Route, Stop> edge : edgeList) {
                builder.append(edge.element().toString());
                builder.append("\n");
            }
           return builder.toString();
        }
    }
}
