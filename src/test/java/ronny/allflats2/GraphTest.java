package ronny.allflats2;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
class GraphTest {

    @Test
    void test() throws IOException {

        Graph graph = RandomGraph.randomConnectedSetSize(10);
        graph.printGraphAsEdgeset();
    }

}