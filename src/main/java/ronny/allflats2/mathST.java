package ronny.allflats2;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class mathST {

    /**
     * Kruskals Algorithmus
     * Fügt Kanten hinzu, die keinen Zyklus bilden, bis der Graph verbunden ist
     */
//    public static Graph oneST(Graph graph){
//        Vertex[] verticesST = new Vertex[graph.amtVertices];
//        for(int i = 0; i < graph.amtVertices; i++){
//            verticesST[i] = new Vertex(i);
//        }
//        Graph ST = new Graph(graph.amtVertices,verticesST);
//        Set<Edge> edgeset = graph.verticesToSet();
//        while(!ST.isConnected()){
//            for(Edge edge : edgeset){
//                ST.addEdge(edge);
//                if(ST.isCyclic()){
//                    ST.removeEdge(edge);
//                }
//            }
//        }
//        return ST;
//    }

    /**
     * Kruskals Algorithmus, hat aber eine Partition,
     * also Liste an inkludierten und exkludierten Kanten, d.h.
     * Kanten, die in den berechneten ST rein müssen und Kanten,
     * die nicht in den berechneten ST dürfen
     */
    public static Graph oneST(Graph graph, Partition partition){
        Graph ST = new Graph(graph.amtVertices);
        Set<Edge> edgeset = graph.verticesToSet();
        edgeset.removeAll(partition.excluded);
        Graph test = new Graph(graph.amtVertices,partition.included);
        test.addEdgeSet(edgeset);
        if(!test.isConnected()){
            return null;
        }
        ST.addEdgeSet(partition.included);
        if(ST.isCyclic()){
            return null;
        }
        while(!ST.isConnected()){
            for(Edge edge : edgeset){
                ST.addEdge(edge);
                if(ST.isCyclic()){
                    ST.removeEdge(edge);
                }
            }
        }
        return ST;
    }

    /**
     * Erstellt einen Set aus allen spannenden Bäumen anhand Sörensens Algorithmus
     */
    public static Set<Graph> allSTSorensen(Graph graph){
        Set<Graph> allSTs = new HashSet<>();
        Set<Edge> included = new HashSet<>();
        Set<Edge> excluded = new HashSet<>();
        Partition partition = new Partition(included,excluded);
        Graph ST = oneST(graph,partition);
        SorensenHelper(ST, graph, allSTs,partition);
        return allSTs;
    }

    /**
     * Hier findet die Partitionierung im Algorithmus von Sörensen statt
     */
    public static void SorensenHelper(Graph spanningTree,Graph graph, Set<Graph> allSTs,Partition p){
        if(spanningTree==null){
            return;
        }
        allSTs.add(spanningTree);
        List<Edge>STList = spanningTree.verticesToList();
        STList.removeAll(p.included);
        List<Partition> partitions = new ArrayList<>();
        Set<Edge> newIncluded = new HashSet<>(p.included);
        for(Edge edge : STList){
            Set<Edge> newExcluded = new HashSet<>(p.excluded);
            newExcluded.add(edge);
            Set<Edge> includedClone = new HashSet<>(newIncluded);
            Partition partition = new Partition(includedClone,newExcluded);
            partitions.add(partition);
            newIncluded.add(edge);
        }
        for(Partition partition : partitions){
            Graph ST = oneST(graph,partition);
            if(ST!=null){
                allSTs.add(ST);
                SorensenHelper(ST,graph,allSTs,partition);
            }
        }
    }

    /**
     * Schreibt den Originalgraphen in den Output und ruft outputHelper2 auf,
     * welcher Sörensens Algorithmus darstellt ohne speichern aller STs, sondern
     * direkt zu einem ST alle Flats ausrechnet die dann in mathFlats.outputHelper3
     * sortiert in den Output geschrieben werden
     */
    public static void outputHelper1(Graph graph, BufferedWriter output) throws IOException {
        String graphString = "Knotenanzahl: "+graph.amtVertices+"\n"+graph.GraphAsStringAsEdgeset()+"\n\n";
        output.write(graphString);
        Set<Edge> included = new HashSet<>();
        Set<Edge> excluded = new HashSet<>();
        Partition partition = new Partition(included,excluded);
        Graph ST = oneST(graph,partition);
        outputHelper2(ST,graph,partition,output);
        output.close();
    }

    /**
     * Verwendet Sörensens Algorithmus für die Berechnung aller spannenden Bäume
     * Sobald ein ST gefunden wird, wird mathFlats.outputHelper3 darauf angewandt,
     * damit die STs nicht zwischengespeichert werden müssen
     */
    public static void outputHelper2(Graph spanningTree, Graph graph, Partition p,BufferedWriter output) throws IOException {
        if(spanningTree==null){
            return;
        }
        output.write("[");
        mathFlats.outputHelper3(spanningTree,graph,output);
        output.write("],");
        List<Edge>STList = spanningTree.verticesToList();
        STList.removeAll(p.included);
        List<Partition> partitions = new ArrayList<>();
        Set<Edge> newIncluded = new HashSet<>(p.included);
        for(Edge edge : STList){
            Set<Edge> newExcluded = new HashSet<>(p.excluded);
            newExcluded.add(edge);
            Set<Edge> includedClone = new HashSet<>(newIncluded);
            Partition partition = new Partition(includedClone,newExcluded);
            partitions.add(partition);
            newIncluded.add(edge);
        }
        for(Partition partition : partitions){
            Graph ST = oneST(graph,partition);
            if(ST!=null){
                outputHelper2(ST,graph,partition,output);
            }
        }
    }

    /**
     * outputHelperFast und outputHelperFast2 sind identisch zu ihren normalen Varianten,
     * aber outputHelper2 ruft die nicht sortierte Variant des Outputs der Flats auf
     */

    public static void outputHelperFast(Graph graph, BufferedWriter output) throws IOException {
        String graphString = "Knotenanzahl: " + graph.amtVertices + "\n" + graph.GraphAsStringAsEdgeset() + "\n\n";
        output.write(graphString);
        Set<Edge> included = new HashSet<>();
        Set<Edge> excluded = new HashSet<>();
        Partition partition = new Partition(included, excluded);
        Graph ST = oneST(graph, partition);
        outputHelperFast2(ST, graph, partition, output);
        output.close();
    }

    public static void outputHelperFast2(Graph spanningTree, Graph graph, Partition p, BufferedWriter output) throws IOException {
        if (spanningTree == null)
            return;
        output.write("[");
        mathFlats.outputHelperFast(spanningTree, graph, output);
        output.write("]");
        List<Edge> STList = spanningTree.verticesToList();
        STList.removeAll(p.included);
        List<Partition> partitions = new ArrayList<>();
        Set<Edge> newIncluded = new HashSet<>(p.included);
        for (Edge edge : STList) {
            Set<Edge> newExcluded = new HashSet<>(p.excluded);
            newExcluded.add(edge);
            Set<Edge> includedClone = new HashSet<>(newIncluded);
            Partition partition = new Partition(includedClone, newExcluded);
            partitions.add(partition);
            newIncluded.add(edge);
        }
        for (Partition partition : partitions) {
            Graph ST = oneST(graph, partition);
            if (ST != null)
                outputHelper2(ST, graph, partition, output);
        }
    }


/**
 * WIP
 */
//    public static Set<Graph> allST(Graph graph){
//        graph.graphCleaner();
//        Set<Graph> allST = new HashSet<>();
//        Graph spanningTree0 = oneST(graph);
//        int size = spanningTree0.amtVertices;
//        allST.add(spanningTree0);
//        Set<Edge> replacementSet = graph.verticesToSet();
//        replacementSet.removeAll(spanningTree0.verticesToSet());
//        findChildren(allST,replacementSet, size);
//        return allST;
//    }
//
//    public static void findChildren(Set<Graph> allST,Set<Edge> replacementSet, int size){
//        int index = size-2;
//        while(index > -1){
//            ArrayList<Graph> toBeAdded = new ArrayList<>();
//            for (Graph child : allST) {
//                for (Edge edge : replacementSet) {
//                    Graph newChild = child.copy();
//                    newChild.removeEdge(index);
//                    newChild.addEdge(edge);
//                    if (newChild.isConnected()) {
//                        toBeAdded.add(newChild);
//                    }
//                }
//            }
//            allST.addAll(toBeAdded);
//            index--;
//        }
//    }

    /**
     * this implementation brute-forces by considering all subgraphs of a given graph and
     * looks at the subgraphs with an edge set of size amtVertices-1 and adds those, that connect all vertices
     * @param graph
     * @return
     */
    public static Set<Graph> allST(Graph graph){
        Set<Graph> allST = new HashSet<>();
        Set<Set<Edge>> allSubsets = allSubsetsOfSize(graph.verticesToList(),graph.amtVertices-1);
        for(Set<Edge> subset : allSubsets){
            if(subset.size()==graph.amtVertices-1){
                Graph subgraph = new Graph(graph.amtVertices,subset);
                if (subgraph.isConnected()) {
                    allST.add(subgraph);
                }
            }
        }
        return allST;
    }

    public static Set<Set<Edge>> allSubsetsOfSize(List<Edge> edgeList, int size) {
        Set<Set<Edge>> allSubsets = new HashSet<>();
        int max = 1 << edgeList.size(); //Äquivalent zu edgeList.size()^2
        for (int i = 1; i < max-1; i++) { //Starte bei 1, damit leere Menge nicht berechnet wird
            //Ended vor max, damit triviale Teilmenge nicht berechnet wird
            Set<Edge> subset = new HashSet<>(); //Berechnung der Untermenge
            if(Integer.bitCount(i)==size) {
                for (int j = 0; j < edgeList.size(); j++) {
                    if (((i >> j) & 1) == 1) { //Generierung einer Teilmenge nach einem Binärprinzip
                        subset.add(edgeList.get(j));
                    }
                }
                allSubsets.add(subset);
            }
        }
        return allSubsets;
    }
}

