package pt.pa.app;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;

import javafx.application.Application;
import javafx.util.StringConverter;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.pa.Factory.CreateStrategyFactory;
import pt.pa.Factory.StrategyFactory;
import pt.pa.graph.TransportMap;
import pt.pa.model.Route;
import pt.pa.model.Stop;
import pt.pa.strategy.CostCalculationStrategy;
import pt.pa.utils.Dijkstra;
import pt.pa.utils.Logger;
import pt.pa.utils.CareTaker;
import pt.pa.view.MapView;
import pt.pa.view.Warning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;




public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {

        TransportMap graph = new TransportMap();
        ComboBox<String> stopsStart = new ComboBox<>();
        for (Vertex<Stop> vertex : graph.vertices()) {
            stopsStart.getItems().add(vertex.element().getStopName());
        }

        TextField distanceField = new TextField();
        distanceField.setPromptText("Número Máximo de Rotas");

        MapView mapView = new MapView(graph);
        graph.addObserver(mapView);
        CareTaker caretaker = new CareTaker(mapView);


        VBox sidebar = new VBox(10);
        Button btnTop5 = new Button("Top 5 Paragens Mais Centrais");
        Button btnTotalStops = new Button("Número de Paragens");
        Button btnTotalRoutes = new Button("Número de Rotas");
        Button btnCentrality = new Button("Centralidade das Paragens");
        Button btnReachableStops = new Button("Paragens Acessíveis");
        Button btnCalculatePaths = new Button("Calcular Caminhos");
        Button btnDeactivateRoute = new Button("Desativar Rota ");
        Button btnDeactivateTransport = new Button("Desativar Transporte");
        Button btnChangeTime = new Button("Alterar tempo");
        Button btnUndo = new Button("Undo");

        Button btnHide = new Button("Esconder");


        TextArea infoArea = new TextArea();
        infoArea.setEditable(false);
        infoArea.setWrapText(true);


        StackPane contentArea = new StackPane();

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Paragens");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Centralidade");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Top 5 Paragens Mais Centrais");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Centralidade");

        for (Map.Entry<Stop, Integer> entry : graph.getTop5CentralStops()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().getStopName(), entry.getValue()));
        }
        barChart.getData().add(series);

        btnHide.setOnAction(event -> {
            Logger.getInstance().log("Utilizador clicou em 'Esconder'.");
            contentArea.getChildren().clear();
        });
        btnTop5.setOnAction(e -> {
            Logger.getInstance().log("Utilizador clicou em 'Top 5 Paragens Mais Centrais'.");
            barChart.setPadding(new Insets(10, 10, 50, 10));
            barChart.setPrefSize(200, 300);
            contentArea.getChildren().clear();
            contentArea.getChildren().add(barChart);
        });

        btnTotalStops.setOnAction(e -> {
            Logger.getInstance().log("Utilizador clicou em 'Número de Paragens'.");
            infoArea.setText("Total de Paragens: " + graph.getTotalStops() + " Pragens Isoladas: " + graph.getIsolatedStops() + " Paragens não Isoladas: " + (graph.getTotalStops() - graph.getIsolatedStops()));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(infoArea);
        });

        btnTotalRoutes.setOnAction(e -> {
                    Logger.getInstance().log("Utilizador clicou em 'Número de Rotas'.");
                    infoArea.setText("Total de Rotas: " + graph.getTotalRoutes());
                    contentArea.getChildren().clear();
                    contentArea.getChildren().add(infoArea);
                }
        );


        btnCentrality.setOnAction(e -> {
                    Logger.getInstance().log("Utilizador clicou em 'Centralidade das Paragens'.");

                    infoArea.setText("Centralidade das Paragens:\n");
                    for (Map.Entry<Stop, Integer> entry : graph.getCentralityList()) {
                        infoArea.appendText(entry.getKey().getStopName() + ": " + entry.getValue() + "\n");
                    }
                    contentArea.getChildren().clear();
                    contentArea.getChildren().add(infoArea);
                }

        );
        btnCalculatePaths.setOnAction(e -> {
            ComboBox<String> origemComboBox = new ComboBox<>();
            ComboBox<String> destinoComboBox = new ComboBox<>();

            Collection<Vertex<Stop>> vertices = graph.vertices();
            for (Vertex<Stop> vertex : vertices) {
                origemComboBox.getItems().add(vertex.element().getStopName());
                destinoComboBox.getItems().add(vertex.element().getStopName());
            }

            ComboBox<String> tipoCustoComboBox = new ComboBox<>();
            tipoCustoComboBox.getItems().addAll("Distância", "Duração", "Sustentabilidade");

            CheckBox comboioCheckBox = new CheckBox("Comboio");
            CheckBox autocarroCheckBox = new CheckBox("Autocarro");
            CheckBox barcoCheckBox = new CheckBox("Barco");
            CheckBox caminhadaCheckBox = new CheckBox("Caminhada");
            CheckBox bicicletaCheckBox = new CheckBox("Bicicleta");

            VBox routeSelectionLayout = new VBox(10);
            routeSelectionLayout.setPrefSize(200, 200);
            routeSelectionLayout.setPadding(new Insets(10, 10, 10, 10));
            routeSelectionLayout.setPadding(new Insets(10, 10, 10, 10));
            routeSelectionLayout.getChildren().addAll(
                    new Label("Selecione a Paragem de Origem:"),
                    origemComboBox,
                    new Label("Selecione a Paragem de Destino:"),
                    destinoComboBox,
                    new Label("Selecione o Tipo de Custo:"),
                    tipoCustoComboBox,
                    new Label("Selecione o Meio de Transporte:"),
                    autocarroCheckBox, barcoCheckBox, comboioCheckBox, caminhadaCheckBox, bicicletaCheckBox
            );


            Button confirmButton = new Button("Confirmar Seleção");
            confirmButton.setOnAction(ev -> {
                Vertex<Stop> origem = graph.findVertexByName(origemComboBox.getValue());
                Vertex<Stop> destino = graph.findVertexByName(destinoComboBox.getValue());
                String tipoCusto = tipoCustoComboBox.getValue();
                List<String> meiosTransporte = new ArrayList<>();

                if (comboioCheckBox.isSelected()) {
                    meiosTransporte.add(comboioCheckBox.getText());
                }
                if (autocarroCheckBox.isSelected()) {
                    meiosTransporte.add(autocarroCheckBox.getText());
                }
                if (barcoCheckBox.isSelected()) {
                    meiosTransporte.add(barcoCheckBox.getText());
                }
                if (caminhadaCheckBox.isSelected()) {
                    meiosTransporte.add(caminhadaCheckBox.getText());
                }
                if (bicicletaCheckBox.isSelected()) {
                    meiosTransporte.add(bicicletaCheckBox.getText());
                }

                if (origem.equals(destino)) {
                    new Warning("Mesmo destino e origem", "A paragem de origem é a mesma do destino!");
                    contentArea.getChildren().clear();
                } else if (origem != null && destino != null && tipoCusto != null && meiosTransporte != null) {

                    StrategyFactory strategyFactory = new CreateStrategyFactory();
                    CostCalculationStrategy strategy = strategyFactory.createStrategy(tipoCusto);

                    Dijkstra dijkstra = new Dijkstra();
                    Map<Vertex<Stop>, Vertex<Stop>> predecessors = dijkstra.calculateShortestPath(graph, origem, destino, strategy, meiosTransporte);

                    if (predecessors.isEmpty()) {
                        infoArea.setText("Não foi possível alcançar o destino com as opções fornecidas.\n");
                        contentArea.getChildren().clear();
                        contentArea.getChildren().add(infoArea);
                    } else {
                        infoArea.setText("Rota selecionada:\n");
                        infoArea.appendText("Origem: " + origem.element().getStopName() + "\n");
                        infoArea.appendText("Destino: " + destino.element().getStopName() + "\n");
                        infoArea.appendText("Tipo de Custo: " + tipoCusto + "\n");
                        infoArea.appendText("Meio de Transporte: " + meiosTransporte + "\n");


                        List<Stop> caminhoPercorrido = dijkstra.reconstructPathWithVisualization(predecessors, origem, destino, graph, strategy, meiosTransporte, mapView.graphView);
                        infoArea.appendText("\nCaminho Percorrido:\n");
                        for (Stop stop : caminhoPercorrido) {
                            infoArea.appendText(stop.getStopName() + " -> ");
                        }
                        infoArea.appendText("Fim\n");

                        double custoTotal = dijkstra.getLastCost();
                        switch (tipoCusto) {
                            case "Duração" -> {
                                double minutos = custoTotal % 60;
                                double horas = (custoTotal - minutos) / 60;
                                if (horas == 0) {
                                    infoArea.appendText("Custo total do percurso: " + Math.round(minutos) + "m\n");
                                } else {
                                    infoArea.appendText("Custo total do percurso: " + Math.round(horas) + "h" + Math.round(minutos) + "m\n");
                                }
                            }
                            case "Distância" ->
                                    infoArea.appendText("Custo total do percurso: " + Math.round(custoTotal) + "km");
                            case "Sustentabilidade" ->
                                    infoArea.appendText("Custo total do percurso : pegada ecológica " + custoTotal);
                        }

                        contentArea.getChildren().clear();
                        contentArea.getChildren().add(infoArea);
                        Logger.getInstance().log(String.format(
                                "Utilizador calculou caminho: Origem=%s, Destino=%s, Custo=%s, Transporte=%s",
                                origem.element().getStopName(), destino.element().getStopName(), tipoCusto, meiosTransporte
                        ));

                    }
                } else {
                    Logger.getInstance().log("Tentativa de cálculo de caminho falhou: opções incompletas.");
                    infoArea.setText("Por favor, selecione todas as opções.\n");
                    contentArea.getChildren().clear();
                    contentArea.getChildren().add(infoArea);
                }


            });


            routeSelectionLayout.getChildren().add(confirmButton);

            Stage routeSelectionStage = new Stage();
            routeSelectionStage.setTitle("Escolher Paragens e Custo");
            routeSelectionStage.setScene(new Scene(routeSelectionLayout, 200, 450));
            routeSelectionStage.show();

        });

        btnChangeTime.setOnAction(e -> {
            ComboBox<Edge<Route, Stop>> rotaComboBox = new ComboBox<>();

            for (Edge<Route, Stop> edge : graph.edges()) {
                if (edge.element().getDurationBicycle() != null) {
                    rotaComboBox.getItems().add(edge);
                }
            }
            convertEdgeString(rotaComboBox);

            TextField custo = new TextField();

            VBox edgeSelectionLayout = new VBox(10);
            edgeSelectionLayout.setPadding(new Insets(10, 10, 10, 10));
            edgeSelectionLayout.setPadding(new Insets(10, 10, 10, 10));
            edgeSelectionLayout.getChildren().addAll(
                    new Label("Selecione a Rota que pretende ajustar:"), rotaComboBox,custo

            );

            Button confirmButton1 = new Button("Confirmar Seleção");

            edgeSelectionLayout.getChildren().add(confirmButton1);
            Stage edgeSelectionStage = new Stage();
            edgeSelectionStage.setTitle("Ajustar Rota");
            edgeSelectionStage.setScene(new Scene(edgeSelectionLayout, 300, 150));
            edgeSelectionStage.show();
            confirmButton1.setOnAction(ev -> {
                caretaker.saveState();
                Edge<Route, Stop> edgeToChange = rotaComboBox.getSelectionModel().getSelectedItem();
                try {
                    edgeToChange.element().setDurationBicycle(Double.valueOf(custo.getText()));
                    mapView.getGraphView().update();
                    edgeSelectionStage.close();

                } catch (NumberFormatException e1) {
                    new Warning("Tem de ser um número", "O valor não pode ser em texto!");
                }
            });
        });

       //Desativar meio de transporte.
       btnDeactivateTransport.setOnAction(e->{
           ComboBox<Edge<Route,Stop>> rotaComboBox = new ComboBox<>();
           for (Edge<Route, Stop> edge : graph.edges()) {
               rotaComboBox.getItems().add(edge);
           }
           convertEdgeString(rotaComboBox);


           CheckBox comboioCheckBox = new CheckBox("Comboio");
           CheckBox autocarroCheckBox = new CheckBox("Autocarro");
           CheckBox barcoCheckBox = new CheckBox("Barco");
           CheckBox caminhadaCheckBox = new CheckBox("Caminhada");
           CheckBox bicicletaCheckBox = new CheckBox("Bicicleta");

           VBox edgeSelectionLayout = new VBox(10);
           edgeSelectionLayout.setPadding(new Insets(10,10,10,10));
           edgeSelectionLayout.setPadding(new Insets(10, 10, 10, 10));
           edgeSelectionLayout.getChildren().addAll(
                   new Label("Rota:"), rotaComboBox,
                   new Label("Selecione o meio de transporte que pretende desativar "),
                   caminhadaCheckBox,
                   comboioCheckBox,
                   autocarroCheckBox,
                   barcoCheckBox,
                   bicicletaCheckBox);

           Button confirmButton1 = new Button("Confirmar Seleção");

           edgeSelectionLayout.getChildren().addAll(confirmButton1);
           Stage edgeSelectionStage = new Stage();
           edgeSelectionStage.setTitle("Escolher Rota");
           edgeSelectionStage.setScene(new Scene(edgeSelectionLayout, 300, 400));
           edgeSelectionStage.show();
           confirmButton1.setOnAction(ev -> {

               Edge <Route,Stop> edge = rotaComboBox.getSelectionModel().getSelectedItem();
               List<String> transportsToRemove = new ArrayList<>();

               if(comboioCheckBox.isSelected()){ transportsToRemove.add("Comboio");}
               if(autocarroCheckBox.isSelected()){ transportsToRemove.add("Autocarro");}
               if(barcoCheckBox.isSelected()){ transportsToRemove.add("Barco");}
               if(caminhadaCheckBox.isSelected()){ transportsToRemove.add("Caminhada");}
               if(bicicletaCheckBox.isSelected()){ transportsToRemove.add("Bicicleta");}

               caretaker.saveState();
               edge.element().desactivateRoute(transportsToRemove.toArray(new String[0]));
               edgeSelectionStage.close();
           });
       });
        btnUndo.setOnAction(e -> {
            caretaker.restoreState(
            );
            mapView.getGraphView().update();
        });



        btnReachableStops.setOnAction(e -> {

            int maxDistance;
            if (distanceField.getText().isEmpty() && stopsStart.getSelectionModel().isEmpty()) {
                Logger.getInstance().log("Tentativa de calcular paragens acessíveis falhou: campos vazios.");
                new Warning("Valor Maximo e Paragem","O valor maximo  e a paragem têm de estar preenchidos");
            }
            else if (distanceField.getText().isEmpty() ) {
                Logger.getInstance().log("Tentativa de calcular paragens acessíveis falhou: campos vazios.");
                new Warning("Valor Maximo","O valor maximo de paragens tem de estar preenchido");
            }
            else if(stopsStart.getSelectionModel().getSelectedItem() == null){
                Logger.getInstance().log("Tentativa de calcular paragens acessíveis falhou: campos vazios.");
                new Warning("Seleção do Stop","Deve selecionar uma paragem!");
            }else{
                try {
                    maxDistance = Integer.parseInt(distanceField.getText());
                }catch (NumberFormatException ex) {
                    new Warning("Valor","O valor deve ser um inteiro");
                    Logger.getInstance().log("Tentativa de calcular paragens acessíveis falhou: campos incorretos.");
                    return;
                }
                try {

                    String startStop = stopsStart.getSelectionModel().getSelectedItem();

                    contentArea.getChildren().clear();
                    contentArea.getChildren().add(infoArea);
                    List<Stop> stops = graph.getAccessibleStops(startStop, maxDistance);
                    infoArea.setText("");
                    infoArea.setText("Paragens acessíveis a partir de " + startStop + ":\n");
                    for (Stop stop : stops) {
                        infoArea.appendText(stop.getStopName() + " (" + stop.getStopCode() + ")\n");
                    }
                    Logger.getInstance().log(String.format(
                            "Utilizador calculou paragens acessíveis a partir de '%s' com distância máxima de %d paragens.",
                            startStop, maxDistance
                    ));
                } catch (IllegalArgumentException ex) {
                    infoArea.setText(ex.getMessage());
                }
            }
        });


        VBox stopContent = new VBox(new Label("Paragem Inicial (e):"), stopsStart);
        VBox distanceContent = new VBox(new Label("Distância Máxima (n):"), distanceField);
        sidebar.getChildren().addAll(stopContent,distanceContent,btnReachableStops,btnTop5, btnTotalStops, btnTotalRoutes, btnCentrality, btnCalculatePaths,btnDeactivateRoute,btnDeactivateTransport,btnChangeTime,btnUndo,btnHide);
        VBox.setMargin(btnReachableStops, new Insets(0, 0, 20, 0));
        VBox.setMargin(btnHide,new Insets(15,0,0,0));


        BorderPane root = new BorderPane();
        root.setCenter(mapView);
        root.setRight(sidebar);
        root.setBottom(contentArea);

        sidebar.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(root, 1200, 700);
        Stage stage = new Stage();
        stage.setTitle("Projeto PA 2024/25 - Maps");
        stage.setScene(scene);


        stage.show();
        mapView.getGraphView().init();
    }

    private void convertEdgeString(ComboBox<Edge<Route,Stop>> comboBox) {
        comboBox .setConverter(new StringConverter<Edge<Route,Stop>>() {
            @Override
            public String toString(Edge<Route, Stop> object) {
                if(object != null){
                StringBuilder sb = new StringBuilder();
                sb.append(object.element().getStopCodeStart());
                sb.append(" - ");
                sb.append(object.element().getStopCodeEnd());
                return sb.toString();
                }
                return null;
            }

            @Override
            public Edge<Route, Stop> fromString(String string) {
                return null;
            }
        });
    }

}


