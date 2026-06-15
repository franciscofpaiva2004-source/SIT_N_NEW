package pt.pa.model;

import pt.pa.utils.Memento;
import pt.pa.utils.Originator;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa uma rota bidirecional entre duas paragens (stops) do sistema integrado de transportes.
 * Cada rota possui informações detalhadas sobre as distâncias, durações e custos associados
 * a diferentes meios de transporte, incluindo comboio, autocarro, barco, caminhada e bicicleta.
 *
 * <p>Este modelo permite calcular as opções disponíveis para deslocar-se entre duas paragens da rede.
 * A rota é caracterizada pelos seguintes atributos:
 * <ul>
 *     <li><b>stopCodeStart</b>: Código da paragem de início da rota.</li>
 *     <li><b>stopCodeEnd</b>: Código da paragem de destino da rota.</li>
 *     <li><b>distance*</b>: Distâncias (em km) para cada meio de transporte.</li>
 *     <li><b>duration*</b>: Durações (em minutos) para cada meio de transporte.</li>
 *     <li><b>cost*</b>: Custos (em euros) para cada meio de transporte.</li>
 * </ul>
 *
 * <p>Esta classe faz parte do modelo de grafo onde os vértices representam paragens e as arestas
 * representam as rotas que conectam essas paragens.</p>
 *
 * @author Francisco Paz, Gonçalo Barracha, Rodrigo Cardoso
 */
public class Route{

    /** Código da paragem de início da rota. */
    private String stopCodeStart;

    /** Código da paragem de destino da rota. */
    private String stopCodeEnd;

    /** Distância (em km) da rota percorrida de comboio. */
    private Double distanceTrain;

    /** Distância (em km) da rota percorrida de autocarro. */
    private Double distanceBus;

    /** Distância (em km) da rota percorrida de barco. */
    private Double distanceBoat;

    /** Distância (em km) da rota percorrida a pé. */
    private Double distanceWalk;

    /** Distância (em km) da rota percorrida de bicicleta. */
    private Double distanceBicycle;

    /** Duração (em minutos) da rota percorrida de comboio. */
    private Double durationTrain;

    /** Duração (em minutos) da rota percorrida de autocarro. */
    private Double durationBus;

    /** Duração (em minutos) da rota percorrida de barco. */
    private Double durationBoat;

    /** Duração (em minutos) da rota percorrida a pé. */
    private Double durationWalk;

    /** Duração (em minutos) da rota percorrida de bicicleta. */
    private Double durationBicycle;

    /** Custo (em sustentabilidade) da rota percorrida de comboio. */
    private Double costTrain;

    /** Custo (em sustentabilidade) da rota percorrida de autocarro. */
    private Double costBus;

    /** Custo (em sustentabilidade) da rota percorrida de barco. */
    private Double costBoat;

    /** Custo (em sustentabilidade) da rota percorrida a pé. */
    private Double costWalk;

    /** Custo (em sustentabilidade) da rota percorrida de bicicleta. */
    private Double costBicycle;

    /**
     * Construtor padrão da classe Route.
     * Inicializa uma rota vazia sem informações definidas.
     */
    public Route() {
    }
    /**
     * Obtém o código da paragem de início da rota.
     *
     * @return Código da paragem inicial.
     */
    public String getStopCodeStart() {
        return stopCodeStart;
    }

    /**
     * Obtém o código da paragem de destino da rota.
     *
     * @return Código da paragem final.
     */
    public String getStopCodeEnd() {
        return stopCodeEnd;
    }


    // Métodos get e set para distâncias
    /**
     * Obtém a distância da rota percorrida de comboio.
     *
     * @return Distância (em km) de comboio.
     */
    public Double getDistanceTrain() {
        return distanceTrain;
    }

    /**
     * Obtém a distância da rota percorrida de autocarro.
     *
     * @return Distância (em km) de autocarro.
     */
    public Double getDistanceBus() {
        return distanceBus;
    }

    /**
     * Obtém a distância da rota percorrida de barco.
     *
     * @return Distância (em km) de barco.
     */
    public Double getDistanceBoat() {
        return distanceBoat;
    }

    /**
     * Obtém a distância da rota percorrida a pé.
     *
     * @return Distância (em km) a pé.
     */
    public Double getDistanceWalk() {
        return distanceWalk;
    }

    /**
     * Obtém a distância da rota percorrida de bicicleta.
     *
     * @return Distância (em km) de bicicleta.
     */
    public Double getDistanceBicycle() {
        return distanceBicycle;
    }

    /**
     * Obtém a duração da rota percorrida de comboio.
     *
     * @return Duração (em minutos) de comboio.
     */
    public Double getDurationTrain() {
        return durationTrain;
    }

    /**
     * Obtém a duração da rota percorrida de autocarro.
     *
     * @return Duração (em minutos) de autocarro.
     */
    public Double getDurationBus() {
        return durationBus;
    }


    /**
     * Obtém a duração da rota percorrida de barco.
     *
     * @return Duração (em minutos) de barco.
     */
    public Double getDurationBoat() {
        return durationBoat;
    }

    /**
     * Obtém a duração da rota percorrida a pé.
     *
     * @return Duração (em minutos) a pé.
     */
    public Double getDurationWalk() {
        return durationWalk;
    }

    /**
     * Obtém a duração da rota percorrida de bicicleta.
     *
     * @return Duração (em minutos) de bicicleta.
     */
    public Double getDurationBicycle() {
        return durationBicycle;
    }

    /**
     * Obtém o custo da rota percorrida de comboio.
     *
     * @return Custo (em euros) de comboio.
     */
    public Double getCostTrain() {
        return costTrain;
    }

    /**
     * Obtém o custo da rota percorrida de autocarro.
     *
     * @return Custo (em euros) de autocarro.
     */
    public Double getCostBus() {
        return costBus;
    }

    /**
     * Obtém o custo da rota percorrida de barco.
     *
     * @return Custo (em euros) de barco.
     */
    public Double getCostBoat() {
        return costBoat;
    }

    /**
     * Obtém o custo da rota percorrida a pé.
     *
     * @return Custo (em euros) a pé.
     */
    public Double getCostWalk() {
        return costWalk;
    }

    /**
     * Obtém o custo da rota percorrida de bicicleta.
     *
     * @return Custo (em euros) de bicicleta.
     */
    public Double getCostBicycle() {
        return costBicycle;
    }

    public void desactivateRoute(String... transports) {
        for (String transport : transports) {
            switch (transport) {
                case "Comboio" -> {
                    setCostTrain(null);
                    setDurationTrain(null);
                    setDistanceTrain(null);
                }
                case "Autocarro" -> {
                    setCostBus(null);
                    setDurationBus(null);
                    setDistanceBus(null);
                }
                case "Barco" -> {
                    setCostBoat(null);
                    setDurationBoat(null);
                    setDistanceBoat(null);
                }
                case "Caminhada" -> {
                    setCostWalk(null);
                    setDurationWalk(null);
                    setDistanceWalk(null);
                }
                case "Bicicleta" -> {
                    setCostBicycle(null);
                    setDurationBicycle(null);
                    setDistanceBicycle(null);
                }
            }

    }
}



    @Override
    public String toString() {
        return "Route{" +
                "stopCodeStart='" + stopCodeStart + '\'' +
                ", stopCodeEnd='" + stopCodeEnd + '\'' +
                ", distanceTrain=" + distanceTrain +
                ", distanceBus=" + distanceBus +
                ", distanceBoat=" + distanceBoat +
                ", distanceWalk=" + distanceWalk +
                ", distanceBicycle=" + distanceBicycle +
                ", durationTrain=" + durationTrain +
                ", durationBus=" + durationBus +
                ", durationBoat=" + durationBoat +
                ", durationWalk=" + durationWalk +
                ", durationBicycle=" + durationBicycle +
                ", costTrain=" + costTrain +
                ", costBus=" + costBus +
                ", costBoat=" + costBoat +
                ", costWalk=" + costWalk +
                ", costBicycle=" + costBicycle +
                '}';
    }

    public void setStopCodeStart(String stop){
        this.stopCodeStart = stop;
    }
    public void setStopCodeEnd(String stop){
        this.stopCodeEnd = stop;
    }

    public void setDistanceTrain(Double distanceTrain) {
        this.distanceTrain = distanceTrain;
    }

    public void setDistanceBus(Double distanceBus) {
        this.distanceBus = distanceBus;
    }

    public void setDistanceBoat(Double distanceBoat) {
        this.distanceBoat = distanceBoat;
    }

    public void setDistanceWalk(Double distanceWalk) {
        this.distanceWalk = distanceWalk;
    }

    public void setDistanceBicycle(Double distanceBicycle) {
        this.distanceBicycle = distanceBicycle;
    }

    public void setDurationTrain(Double durationTrain) {
        this.durationTrain = durationTrain;
    }

    public void setDurationBus(Double durationBus) {
        this.durationBus = durationBus;
    }

    public void setDurationBoat(Double durationBoat) {
        this.durationBoat = durationBoat;
    }

    public void setDurationWalk(Double durationWalk) {
        this.durationWalk = durationWalk;
    }

    public void setDurationBicycle(Double durationBicycle) {
        this.durationBicycle = durationBicycle;
    }

    public void setCostTrain(Double costTrain) {
        this.costTrain = costTrain;
    }

    public void setCostBus(Double costBus) {
        this.costBus = costBus;
    }

    public void setCostBoat(Double costBoat) {
        this.costBoat = costBoat;
    }

    public void setCostWalk(Double costWalk) {
        this.costWalk = costWalk;
    }

    public void setCostBicycle(Double costBicycle) {
        this.costBicycle = costBicycle;
    }
    public static Route cloneRoute(Route route){
        Route clone = new Route();
        clone.setStopCodeStart(route.getStopCodeStart());
        clone.setStopCodeEnd(route.getStopCodeEnd());
        clone.setDistanceTrain(route.getDistanceTrain());
        clone.setDistanceBus(route.getDistanceBus());
        clone.setDistanceBoat(route.getDistanceBoat());
        clone.setDistanceWalk(route.getDistanceWalk());
        clone.setDistanceBicycle(route.getDistanceBicycle());
        clone.setDurationTrain(route.getDurationTrain());
        clone.setDurationBus(route.getDurationBus());
        clone.setDurationBoat(route.getDurationBoat());
        clone.setDurationWalk(route.getDurationWalk());
        clone.setDurationBicycle(route.getDurationBicycle());
        clone.setCostTrain(route.getCostTrain());
        clone.setCostBus(route.getCostBus());
        clone.setCostBoat(route.getCostBoat());
        clone.setCostWalk(route.getCostWalk());
        clone.setCostBicycle(route.getCostBicycle());
        return clone;
    }

    /*@Override
    public Memento createMemento() {
        return new RouteMemento(this);
    }
    @Override
    public void setMemento(Memento memento) {
        if (memento instanceof RouteMemento) {

            Map<String, Double> state = ((RouteMemento) memento).getState();

            this.setCostBicycle(state.get("costBicycle"));
            this.setCostBus(state.get("costBus"));
            this.setCostWalk(state.get("costWalk"));
            this.setCostBoat(state.get("costBoat"));
            this.setCostTrain(state.get("costTrain"));
            this.setDistanceBicycle(state.get("distanceBicycle"));
            this.setDistanceBus(state.get("distanceBus"));
            this.setDistanceTrain(state.get("distanceTrain"));
            this.setDistanceWalk(state.get("distanceWalk"));
            this.setDistanceBoat(state.get("distanceBoat"));
            this.setDurationBicycle(state.get("durationBicycle"));
            this.setDurationBus(state.get("durationBus"));
            this.setDurationTrain(state.get("durationTrain"));
            this.setDurationWalk(state.get("durationWalk"));
            this.setDurationBoat(state.get("durationBoat"));
        }
    }



    public  class RouteMemento implements Memento {
        private  Map<String, Double> state;

        public RouteMemento(Route route) {
            state = new HashMap<>();
            state.put("costBicycle", route.getCostBicycle());
            state.put("costBus", route.getCostBus());
            state.put("costWalk", route.getCostWalk());
            state.put("costBoat", route.getCostBoat());
            state.put("costTrain", route.getCostTrain());
            state.put("distanceBicycle", route.getDistanceBicycle());
            state.put("distanceBus", route.getDistanceBus());
            state.put("distanceTrain", route.getDistanceTrain());
            state.put("distanceWalk", route.getDistanceWalk());
            state.put("distanceBoat", route.getDistanceBoat());
            state.put("durationBicycle", route.getDurationBicycle());
            state.put("durationBus", route.getDurationBus());
            state.put("durationTrain", route.getDurationTrain());
            state.put("durationWalk", route.getDurationWalk());
            state.put("durationBoat", route.getDurationBoat());
        }

        public Map<String, Double> getState() {
            return state;
        }
    }*/
}
