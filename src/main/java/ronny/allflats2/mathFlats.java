package ronny.allflats2;
import java.io.BufferedWriter;

import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
public class mathFlats {
    /**
     * Generiert alle Teilmengen einer Menge anhand eines Binärsystems
     * und gibt eine Menge aller Teilmengen aus (außer triviale Mengen)
     */
    public static Set<Set<Edge>> allSubsets(List<Edge> edgeList) {
        Set<Set<Edge>> allSubsets = new HashSet<>();
        int max = 1 << edgeList.size(); //Äquivalent zu 2^edgeList.size()
        for (int i = 1; i < max-1; i++) { //Starte bei 1, damit leere Menge nicht berechnet wird
            //Ended vor max, damit triviale Teilmenge nicht berechnet wird
            Set<Edge> subset = new HashSet<>(); //Berechnung der Untermenge
            for (int j = 0; j < edgeList.size(); j++) {
                if (((i >> j) & 1) == 1) { //Generierung einer Teilmenge nach einem Binärprinzip
                    subset.add(edgeList.get(j));
                }
            }
            allSubsets.add(subset);
        }
        return allSubsets;
    }

    /**
     * Generiert alle Untergraphen eines Graphen (hier spannenden Baumes) anhand der Potenzmenge
     * der Kantenmenge und gibt diese aus
     */
    public static Set<Graph> allSubgraphs(Graph spanningTree){
        Set<Graph> allSubgraphs = new HashSet<>();
        Set<Set<Edge>> allSubsets = allSubsets(spanningTree.verticesToList());
        for(Set<Edge> edgeSet : allSubsets){
            Graph subgraph = new Graph(spanningTree.amtVertices,edgeSet);
            allSubgraphs.add(subgraph);
        }
        return allSubgraphs;
    }

    /**
     * Gibt alle Flats eines spannenden Baumes als String aus, d.h.
     * Flat = [Kante1,Kante2,...]
     * allFlats = [Flat1,Flat2,...]
     * Tut dies ohne alle Teilmengen des spannenden Baumes zu speichern, sondern
     * erstellt den String einer Flat einer Teilmengen, "addiert" den zum großen Output String
     * und fährt so fort für alle anderen Teilmengen
     */
    public static String allGeneratedFlats(Graph spanningTree, Graph originalGraph){
        String allFlats = "";
        List<Edge> edgeList = spanningTree.verticesToList();
        int max = 1 << edgeList.size(); //Äquivalent zu edgeList.size()^2
        for (int i = 1; i < max-1; i++) { //Starte bei 1, damit leere Menge nicht berechnet wird
            //Ended vor max, damit triviale Teilmenge nicht berechnet wird
            Set<Edge> subset = new HashSet<>(); //Berechnung der Untermenge
            for (int j = 0; j < edgeList.size(); j++) {
                if (((i >> j) & 1) == 1) { //Generierung einer Teilmenge nach einem Binärprinzip
                    subset.add(edgeList.get(j));
                }
            }
            Graph subgraph = new Graph(originalGraph.amtVertices,subset);
            allFlats = allFlats+"["+Edge.edgeSetToString(generatedFlat(subgraph,originalGraph))+"]";
        }
        return allFlats;
    }

    /**
     * Flat eines Untergraphen eines spannenden Baumes wird ausgerechnet und ausgegeben als Set
     */
    public static Set<Edge> generatedFlat(Graph subgraph, Graph originalGraph){
        Set<Edge> subsetFlat = subgraph.verticesToSet();
        Set<Edge> availableEdges = originalGraph.verticesToSet();
        for(Edge edge : availableEdges){
            if(edge.start!=edge.end) {
                Graph flatGraph = subgraph.copy();
                flatGraph.addEdge(edge);
                if (flatGraph.isCyclic()) {
                    subsetFlat.add(edge);
                }
            }
        }
        return subsetFlat;
    }
    /**
     * Flat eines Untergraphen eines spannenden Baumes wird ausgerechnet und ausgegeben als Liste
     */
    public static List<Edge> generatedFlatAsList(Graph subgraph, Graph originalGraph){
        List<Edge> subsetFlat = subgraph.verticesToList();
        Set<Edge> availableEdges = originalGraph.verticesToSet();
        for(Edge edge : availableEdges){
            if(edge.start!=edge.end) {
                Graph flatGraph = subgraph.copy();
                flatGraph.addEdge(edge);
                if (flatGraph.isCyclic()) {
                    subsetFlat.add(edge);
                }
            }
        }
        return subsetFlat;
    }

    /**
     * Für Tests, Print Out einer Kantenmenge(Überflüssig, Klasse Edge besitzt eine äquivalente Methode)
     */
    public static void printEdgeSet(Set<Edge> flat){
        for(Edge edge : flat){
            edge.printEdge();
        }
    }

    /**
     * Nimmt einen spannenden Baum, berechnet und zwischenspeichert alle Flats
     * sortiert alle Flats und Kanten innerhalb eines Flats lexographisch
     * und erstellt dann erst einen großen String, der alle Flats eines spannenden Baumes ausgibt
     * String wird in den Output geschrieben
     */

    public static void outputHelper3(Graph spanningTree, Graph originalGraph, BufferedWriter output) throws IOException {
        List<List<Edge>> allFlatsToOneST = new ArrayList<>();
        List<Edge> edgeList = spanningTree.verticesToList();
        int max = 1 << edgeList.size();
        /**
         * Berechnet alle Flats und fügt die zu einer Liste allFlatsToOneST hinzu
         */
        for (int i = 1; i < max-1; i++) {
            Set<Edge> subset = new HashSet<>();
            for (int j = 0; j < edgeList.size(); j++) {
                if (((i >> j) & 1) == 1) {
                    subset.add(edgeList.get(j));
                }
            }
            Graph subgraph = new Graph(originalGraph.amtVertices,subset);
            List<Edge> flat = generatedFlatAsList(subgraph,originalGraph);
            EdgeSorter.sort(flat);
            /** Hier werden die Kanten innerhalb eines Flats sortiert
             */
            allFlatsToOneST.add(flat);
        }
        /**
         * Hier wird die Liste der Flats sortiert
         */
        FlatSorter.sort(allFlatsToOneST);
        /**
         * Erstellung des großten Strings
         */
        String bigString = "";
        for(List<Edge> flat : allFlatsToOneST){
            String string = "["+Edge.edgeListToString(flat)+"]";
            bigString= bigString+string;
        }
        bigString = bigString.replace("],]", "]]");
        bigString = bigString.replace("][","],[");
        output.write(bigString);
    }

    /**
     * Äquivalent zu oberen Methode outputHelper3, bloß
     * werden die Flats für die Sortierung nicht zwischengespeichert
     * sondern ein Flat wird direkt zum String konvertiert und in die Ausgabe geschrieben
     */
    public static void outputHelperFast(Graph spanningTree, Graph originalGraph, BufferedWriter output) throws IOException {
        List<Edge> edgeList = spanningTree.verticesToList();
        int max = 1 << edgeList.size();
        String bigString = "";
        for (int i = 1; i < max - 1; i++) {
            Set<Edge> subset = new HashSet<>();
            for (int j = 0; j < edgeList.size(); j++) {
                if ((i >> j & 0x1) == 1)
                    subset.add(edgeList.get(j));
            }
            List<Edge> flat = generatedFlatAsList(new Graph(originalGraph.amtVertices, subset), originalGraph);
            bigString = bigString + "[" + Edge.edgeListToString(flat) + "]";
        }
        bigString = bigString.replace("],]", "]]");
        bigString = bigString.replace("][", "],[");
        output.write(bigString);
    }
}
