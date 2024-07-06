package ronny.allflats2;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomGraph {
    /**
     * RandomGraph findet im Programm keinen Nutzen
     * Wurde für Tests verwendet
     */
    public RandomGraph() {
    }

    /**
     * Zufällige Kante
     */
    public static Edge randomEdge(int amtVertices) {
        Random rand = new Random();
        int start = rand.nextInt(amtVertices);
        int end = rand.nextInt(amtVertices);
        Edge edge = new Edge(start, end);
        return edge;
    }

    /**
     * Zufällige Kantenmenge
     */
    public static Set<Edge> randomEdgeSet(int amtVertices) {
        Set<Edge> edgeSet = new HashSet();
        Random rand = new Random();
        int setSize = rand.nextInt(amtVertices * (amtVertices + 1) / 2);

        for(int i = 0; i < setSize; ++i) {
            Edge edge = randomEdge(amtVertices);
            edgeSet.add(edge);
        }

        while(edgeSet.size() < amtVertices - 1) {
            Edge edge = randomEdge(amtVertices);
            edgeSet.add(edge);
        }

        return edgeSet;
    }

    /**
     * Zufälliger Graph mit max. Größe maxSize
     */
    public static Graph random(int maxSize) {
        Random rand = new Random();
        int size = rand.nextInt(maxSize) + 1;
        Graph graph = new Graph(size, randomEdgeSet(size));
        return graph;
    }

    /**
     * erstellt einen zufälligen Graphen mit max. Größe maxSize und "würfelt" solange neu,
     * bis dieser verbunden ist
     */
    public static Graph randomConnected(int maxSize) {
        Random rand = new Random();
        int size = rand.nextInt(maxSize) + 1;

        Graph graph;
        for(graph = new Graph(size, randomEdgeSet(size)); !graph.isConnected(); graph = new Graph(size, randomEdgeSet(size))) {
        }

        return graph;
    }

    /**
     * Zufälliger verbundener Graph mit einer festen Knotenanzahl
     */
    public static Graph randomConnectedSetSize(int size) {
        Graph graph;
        for(graph = new Graph(size, randomEdgeSet(size)); !graph.isConnected(); graph = new Graph(size, randomEdgeSet(size))) {
        }

        return graph;
    }
}
