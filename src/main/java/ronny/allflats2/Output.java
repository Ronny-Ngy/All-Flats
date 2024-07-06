package ronny.allflats2;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Output {
    /**
     * Output Methode, die Brute-Force alle Teilmengen eines Graphen berechnet hat,
     * Untergraphen mit Kantenmengengröße n-1 entgegennahm und jeden geprüft hat,
     * ob es sich um einen spannenden Baum handelt
     * boolean mode, ob die Ausgabe nach dem Format
     * Graph
     * Spannender Baum1
     * Untergraph1
     * Flat1
     * Untergraph2
     * Flat2
     * ...
     * Spannender Baum2
     * erfolgen sollte oder nur die Flats
     */
    public static void allFlatsBruteForce(Graph graph, boolean mode) throws IOException {
        if(mode) {
            FileWriter file = new FileWriter("outputBruteForceCheck.txt");
            BufferedWriter output = new BufferedWriter(file);
            String string ="\nGraph: \n";
            Set<Graph> allSTs = mathST.allST(graph);
            string = string +"\nAmount of spanning trees: " + allSTs.size() + "\n";
            for (Graph ST : allSTs) {
                string = string +"\n\n\nSpanning Tree: \n" + ST.GraphAsStringAsEdgeset() + "\n";
                //output.write("[");
                Set<Graph> allSubgraphs = mathFlats.allSubgraphs(ST);
                for (Graph subgraph : allSubgraphs) {
                    string = string + "\n\nSubgraph: \n"+ subgraph.GraphAsStringAsEdgeset()+ "\n\nGenerated Flat: \n" +"[" + Edge.edgeSetToString(mathFlats.generatedFlat(subgraph, graph)) + "]";
                }
                //output.write("],");
            }
            string = string.replace("],]", "]]");
            char c = 0;
            output.write(string);
            output.close();
        }
        if(!mode) {
            FileWriter file = new FileWriter("outputBruteForceCorrect.txt");
            BufferedWriter output = new BufferedWriter(file);
            String string = "\nGraph: \n" + graph.GraphAsStringAsEdgeset();
            Set<Graph> allSTs = mathST.allST(graph);
            int size = allSTs.size();
            String string1 = "\n\nAmount of spanning trees: " + size + "\n\n";
            string = string + string1;
            for (Graph ST : allSTs) {
                string = string + "[" + mathFlats.allGeneratedFlats(ST, graph) + "],";
            }
            string = string.replace("],]", "]]");
            string = string.replace("][","],[");
            char c = 0;
            string = replaceCharUsingCharArray(string, c, string.length() - 1);
            output.write(string);
            output.close();
        }
    }

    /**
     * Verwendet um in einem String an seindem Index index den char mit char ch zu ersetzen
     */
    public static String replaceCharUsingCharArray(String str, char ch, int index) {
        char[] chars = str.toCharArray();
        chars[index] = ch;
        return String.valueOf(chars);
    }

    /**
     * Output methode, die die Originalmethoden verwendete, also
     * alle STs und zu jedem ST alle Flats zwischengespeichert hat
     * Ohne Sortierung der Flats
     * boolean mode, wie im Brute-Force
     */
    public static void allFlatsSorensen(Graph graph, boolean mode) throws IOException {
        if(mode) {
            FileWriter file = new FileWriter("outputSorensenCheck.txt");
            BufferedWriter output = new BufferedWriter(file);
            String string ="\nGraph: \n";
            Set<Graph> allSTs = mathST.allSTSorensen(graph);
            string = string +"\nAmount of spanning trees: " + allSTs.size() + "\n";
            for (Graph ST : allSTs) {
                string = string +"\n\n\nSpanning Tree: \n" + ST.GraphAsStringAsEdgeset() + "\n";
                //output.write("[");
                Set<Graph> allSubgraphs = mathFlats.allSubgraphs(ST);
                for (Graph subgraph : allSubgraphs) {
                    string = string + "\n\nSubgraph: \n"+ subgraph.GraphAsStringAsEdgeset()+ "\n\nGenerated Flat: \n" +"[" + Edge.edgeSetToString(mathFlats.generatedFlat(subgraph, graph)) + "]";
                }
                //output.write("],");
            }
            string = string.replace("],]", "]]");
            string = string.replace("][","],[");
            char c = 0;
            output.write(string);

            output.close();
        }
        if(!mode) {
            FileWriter file = new FileWriter("outputSorensenCorrect.txt");
            BufferedWriter output = new BufferedWriter(file);
            String string = "\nGraph: \n" + graph.GraphAsStringAsEdgeset();
            Set<Graph> allSTs = mathST.allSTSorensen(graph);
            int size = allSTs.size();
            String string1 = "\n\nAmount of spanning trees: " + size + "\n\n";
            string = string + string1;
            for (Graph ST : allSTs) {
                string = string + "[" + mathFlats.allGeneratedFlats(ST, graph) + "]";
            }
            string = string.replace("],]", "]]");
            string = string.replace("][","],[");
            output.write(string);
            output.close();
        }
    }

    /**
     * Output Methode, die nur alle Flats zwischenspeichern soll
     */
    public static void allFlatsZwischen(Graph graph, boolean mode) throws IOException {
        Set<Graph> allSTs = new HashSet<>();
        Set<Edge> included = new HashSet<>();
        Set<Edge> excluded = new HashSet<>();
        Partition partition = new Partition(included,excluded);
        Graph ST = mathST.oneST(graph,partition);
        List<Integer> empty = new ArrayList<>();
        if(mode) {
            FileWriter file = new FileWriter("outputFinalCheck.txt");
            BufferedWriter output = new BufferedWriter(file);
            output.write("\nGraph: \n"+graph.GraphAsStringAsEdgeset()+"\n");
            SorensenHelper(ST,graph,partition,output,empty);
            output.write("\nAmount of Spanning Trees: "+empty.size());
            output.close();
        }
        if(!mode) {
            FileWriter file = new FileWriter("outputFinalCorrect.txt");
            BufferedWriter output = new BufferedWriter(file);
            output.write("\nGraph: \n"+graph.GraphAsStringAsEdgeset()+"\n\n\n");
            SorensenHelperCorrect(ST,graph,partition,output);
            output.close();
        }
    }

    /**
     * wird von allFlatsZwischen aufgerufen
     * Berechnet nach Sörensen einen ST, speichert alle Untergraphen an und berechnet
     * zu jedem Untergraphen den Flat und schreibt diesen in die Ausgabe
     */
    public static void SorensenHelper(Graph spanningTree,Graph graph,Partition p,BufferedWriter output, List<Integer> counter) throws IOException {
        if(spanningTree==null){
            return;
        }
        extracted(spanningTree, output);
        Integer i = 0;
        counter.add(i);
        Set<Graph> allSubgraphs = mathFlats.allSubgraphs(spanningTree);
        for (Graph subgraph : allSubgraphs) {
            String string = "[" + Edge.edgeSetToString(mathFlats.generatedFlat(subgraph, graph)) + "]";
            string = string.replace("],]", "]]");
            string = string.replace("][","],[");
            output.write("\n\nSubgraph: \n"+ subgraph.GraphAsStringAsEdgeset()+ "\n\nGenerated Flat: \n" +string);
        }
        List<Edge> STList = spanningTree.verticesToList();
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
            Graph ST = mathST.oneST(graph,partition);
            if(ST!=null){
                SorensenHelper(ST,graph,partition,output,counter);
            }
        }
    }

    /**
     * Schreibt einen spannenden Baum in den Output
     */
    private static void extracted(Graph spanningTree, BufferedWriter output) throws IOException {
        output.write("\nSpanning Tree: \n"+ spanningTree.GraphAsStringAsEdgeset()+"\n");
    }

    /**
     * Äquivalent zu Sörensenhelper mit anderem Format der Ausgabe
     */
    public static void SorensenHelperCorrect(Graph spanningTree,Graph graph,Partition p,BufferedWriter output) throws IOException {
        if(spanningTree==null){
            return;
        }
        String allFlats = "[";
        Set<Graph> allSubgraphs = mathFlats.allSubgraphs(spanningTree);
        for (Graph subgraph : allSubgraphs) {
            allFlats = allFlats +"[" + Edge.edgeSetToString(mathFlats.generatedFlat(subgraph, graph)) + "]";
            allFlats = allFlats.replace("],]", "]]");
            allFlats = allFlats.replace("][","],[");
        }
        allFlats = allFlats+"]";
        output.write(allFlats);
        List<Edge> STList = spanningTree.verticesToList();
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
            Graph ST = mathST.oneST(graph,partition);
            if(ST!=null){
                SorensenHelperCorrect(ST,graph,partition,output);
            }
        }
    }

    /**
     * Main Output Methoden
     * fastOutput wird aus dem Programm noch entfernt und es wird nur simpleOutput verwendet,
     * da fastOutput kaum schneller ist und erst am Kantenmengen der Größe 32 und größer
     * den Speichervorteil zum Nutzen beiträgt
     */
    public static void simpleOutput(Graph graph, String name) throws IOException {
        FileWriter file = new FileWriter(name);
        BufferedWriter output = new BufferedWriter(file);
        mathST.outputHelper1(graph,output);
    }
    public static void fastOutput(Graph graph, String name) throws IOException {
        FileWriter file = new FileWriter(name);
        BufferedWriter output = new BufferedWriter(file);
        mathST.outputHelperFast(graph, output);
    }

}
