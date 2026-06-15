package pt.pa.model;

import com.brunomnsilva.smartgraph.graphview.SmartLabelSource;

/**
 * Representa uma paragem (Stop) no sistema integrado de transportes.
 *
 * <p>Uma paragem é um ponto no grafo do sistema de transportes, que possui um código único,
 * um nome descritivo, e informações de localização geográfica. Também inclui uma posição
 * discreta em um sistema de coordenadas 2D, útil para representação gráfica.</p>
 *
 * <p>A classe contém métodos para acessar e modificar seus atributos principais e
 * um método sobrescrito {@code toString()} que retorna o nome da paragem.</p>
 *
 * <ul>
 *     <li><b>stopCode</b>: Identificador único da paragem.</li>
 *     <li><b>stopName</b>: Nome descritivo da paragem.</li>
 *     <li><b>latitude</b>: Latitude da localização geográfica da paragem.</li>
 *     <li><b>longitude</b>: Longitude da localização geográfica da paragem.</li>
 *     <li><b>posX</b>: Coordenada X em um sistema de representação discreta (2D).</li>
 *     <li><b>posY</b>: Coordenada Y em um sistema de representação discreta (2D).</li>
 * </ul>
 *
 * @author Francisco Paz, Gonçalo Barracha, Rodrigo Cardoso
 */
public class Stop {

    /** Identificador único da paragem. */
    private String stopCode;

    /** Nome descritivo da paragem. */
    private String stopName;

    /** Latitude da localização geográfica da paragem. */
    private Double latitude;

    /** Longitude da localização geográfica da paragem. */
    private Double longitude;

    /** Coordenada X em um sistema discreto de representação (2D). */
    private Integer posX;

    /** Coordenada Y em um sistema discreto de representação (2D). */
    private Integer posY;

    /**
     * Construtor padrão da classe Stop.
     * Inicializa uma paragem com valores não definidos.
     */
    public Stop() {
    }

    /**
     * Obtém o identificador único da paragem.
     *
     * @return O código da paragem.
     */
    public String getStopCode() {
        return stopCode;
    }

    /**
     * Define o identificador único da paragem.
     *
     * @param stopCode O código da paragem.
     */
    public void setStopCode(String stopCode) {
        this.stopCode = stopCode;
    }

    /**
     * Obtém o nome descritivo da paragem.
     *
     * @return O nome da paragem.
     */
    @SmartLabelSource
    public String getStopName() {
        return stopName;
    }


    /**
     * Obtém a latitude da localização geográfica da paragem.
     *
     * @return A latitude da paragem.
     */
    public Double getLatitude() {
        return latitude;
    }


    /**
     * Obtém a longitude da localização geográfica da paragem.
     *
     * @return A longitude da paragem.
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Obtém a coordenada X da paragem no sistema discreto de representação.
     *
     * @return A coordenada X da paragem.
     */
    public Integer getPosX() {
        return posX;
    }

    /**
     * Define a coordenada X da paragem no sistema discreto de representação.
     *
     * @param posX A coordenada X da paragem.
     */
    public void setPosX(Integer posX) {
        this.posX = posX;
    }

    /**
     * Obtém a coordenada Y da paragem no sistema discreto de representação.
     *
     * @return A coordenada Y da paragem.
     */
    public Integer getPosY() {
        return posY;
    }

    /**
     * Define a coordenada Y da paragem no sistema discreto de representação.
     *
     * @param posY A coordenada Y da paragem.
     */
    public void setPosY(Integer posY) {
        this.posY = posY;
    }

    @Override
    public String toString() {
        return "Stop{" +
                "stopCode='" + stopCode + '\'' +
                ", stopName='" + stopName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", posX=" + posX +
                ", posY=" + posY +
                '}';
    }
}

