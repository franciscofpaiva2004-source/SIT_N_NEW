package pt.pa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.pa.graph.TransportMap;
import pt.pa.model.Stop;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the TransportMap class, ensuring the correct functionality
 * of graph operations and data handling.
 */
class TransportMapTest {
    private TransportMap transportMap;

    /**
     * Sets up a new instance of TransportMap and initializes its model
     * before each test.
     */
    @BeforeEach
    void setUp() {
        transportMap = new TransportMap();
        transportMap.initialModel();
    }

    /**
     * Tests if the graph is initialized with the correct number of vertices
     * based on the dataset.
     */
    @Test
    void initialModel_VerifyVerticesCount() {
        assertEquals(31, transportMap.numVertices());
    }

    /**
     * Ensures that edges are added to the graph during initialization,
     * indicating the presence of routes between stops.
     */
    @Test
    void initialModel_VerifyEdgesExistence() {
        assertTrue(transportMap.numEdges() > 0);
    }

    /**
     * Verifies that a specific stop exists in the graph after initialization.
     * Uses the stop code to identify the stop.
     */
    @Test
    void findStopByCode_ExistingStop() {
        Stop expectedStop = new Stop();
        expectedStop.setStopCode("SIN001");

        boolean stopExists = transportMap.vertices().stream()
                .anyMatch(vertex -> vertex.element().getStopCode().equals(expectedStop.getStopCode()));

        assertTrue(stopExists);
    }

    /**
     * Tests the removal of a valid stop from the graph and ensures
     * that it no longer exists in the graph after removal.
     */
    @Test
    void removeStop_ValidStop() {
        Stop stop = new Stop();
        stop.setStopCode("SIN001");

        transportMap.removeVertex(transportMap.vertices().stream()
                .filter(v -> v.element().getStopCode().equals(stop.getStopCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Stop not found.")));

        assertFalse(transportMap.vertices().stream()
                .anyMatch(vertex -> vertex.element().getStopCode().equals("SIN001")));
    }

    /**
     * Verifies that valid routes exist between two connected stops
     * in the graph.
     */
    @Test
    void findRoutesBetweenStops_ValidRouteExists() {
        Stop stopStart = new Stop();
        stopStart.setStopCode("MEM002");

        Stop stopEnd = new Stop();
        stopEnd.setStopCode("SIN001");

        long routesCount = transportMap.edges().stream()
                .filter(edge -> edge.vertices()[0].element().getStopCode().equals(stopStart.getStopCode()) &&
                        edge.vertices()[1].element().getStopCode().equals(stopEnd.getStopCode()))
                .count();

        assertTrue(routesCount > 0);
    }

    /**
     * Ensures that no routes exist between two unconnected stops
     * in the graph.
     */
    @Test
    void findRoutesBetweenStops_NoRouteExists() {
        Stop stopStart = new Stop();
        stopStart.setStopCode("MEM002");

        Stop stopEnd = new Stop();
        stopEnd.setStopCode("S999");

        long routesCount = transportMap.edges().stream()
                .filter(edge -> edge.vertices()[0].element().getStopCode().equals(stopStart.getStopCode()) &&
                        edge.vertices()[1].element().getStopCode().equals(stopEnd.getStopCode()))
                .count();

        assertEquals(0, routesCount);
    }


}
