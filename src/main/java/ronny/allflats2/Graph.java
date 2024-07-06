package ronny.allflats2;
import java.util.*;


public class Graph{
    int amtVertices;
    Vertex[] vertices;

    /** Constructor
     * @param amtVertices Anzahl der Knoten
     * @param vertices Array mit den Knoten, vertices[0] ist der Knoten 0
     */
    public Graph(int amtVertices, Vertex[] vertices) {
        this.amtVertices = amtVertices;
        this.vertices = vertices;
    }

    /**
     * Constructor war für Tests nützlich, da int[][] einfach zu initialisieren ist manuell
     * int[][] edgeset = {{1,2},{2,3}}; für die Kanten 1 zu 2 und 2 zu 3
     */
    public Graph(int amtVertices, int[][] edges) {
        this.amtVertices = amtVertices;
        this.vertices = new Vertex[amtVertices];
        for(int i = 0; i < amtVertices; i++){
            this.vertices[i] = new Vertex(i);
        }
        for(int i = 0; i < edges.length; i++){
            this.vertices[edges[i][0]].addNeighbor(this.vertices[edges[i][1]]);
        }
    }

    public Graph(int amtVertices, Set<Edge> edgeSet) {
        this.amtVertices = amtVertices;
        this.vertices = new Vertex[amtVertices];
        for(int i = 0; i < amtVertices; i++){
            this.vertices[i] = new Vertex(i);
        }
        for(Edge edge : edgeSet){
            this.vertices[edge.start].addNeighbor(this.vertices[edge.end]);
            this.vertices[edge.end].addNeighbor(this.vertices[edge.start]);
        }
    }

    /**
     * leerer Graph
     */
    public Graph(int amtVertices) {
        this.amtVertices = amtVertices;
        this.vertices = new Vertex[amtVertices];
        for(int i = 0; i < amtVertices; i++){
            this.vertices[i] = new Vertex(i);
        }
    }
    /**
     * Graph fügt eine Kantenmenge hinzu.
     * Nützlich, wenn ein leerer Graph erstellt wurde und die Kantenmenge eines anderen Graphen annimmt,
     * um eine neue Kopie des Objektes zu erstellen
     */
    public void addEdgeSet(Set<Edge> edges){
        for(Edge edge : edges){
            this.addEdge(edge);
        }
    }

    public Graph copy(){
        Graph graph = new Graph(this.amtVertices);
        graph.addEdgeSet(this.verticesToSet());
        return graph;
    }

    /**
     * Prüft, ob jeder Knoten vom Knoten 0 erreichbar ist mithilfe von DFS
     */
    public boolean isConnected(){
        boolean[] visited = new boolean[this.amtVertices];
        DFS(this.vertices[0], visited);
        boolean connected = true;
        for (int i = 0; i < visited.length; i++) {
            if (!visited[i]) {
                connected = false;
                break;
            }
        }
        return connected;
    }

    public void DFS(Vertex origin, boolean[] visited) {
        visited[origin.getVertexID()] = true;
        for (Vertex v : origin.neighbors) {
            if (!visited[v.getVertexID()]) {
                DFS(v, visited);
            }
        }
    }

    /**
     * Prüft nach Zyklen mithilfe von DFS
     */
    public boolean isCyclic() {
        boolean[] visited = new boolean[this.amtVertices];
        for (Vertex v : this.vertices) {
            if (!visited[v.getVertexID()]) {
                if(cyclicHelper(v, visited, new Vertex(-1))){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * DFS und es nimmt den Vorgänger Knoten mit
     * Wenn auf ein Knoten getroffen wird, der bereits besucht wurde, aber nicht der Vorgänger ist,
     * gibt es einen Kreis
     */
    public boolean cyclicHelper(Vertex current, boolean[] visited, Vertex predecessor) {
        visited[current.getVertexID()] = true;
        for (Vertex v : current.neighbors) {
            if (!visited[v.getVertexID()]) {
                if(cyclicHelper(v, visited, current)){
                    return true;
                }
            } else if (visited[v.getVertexID()] && v.getVertexID() != predecessor.getVertexID()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gibt eine Kantenmenge eines Graphen wieder
     */
    public Set<Edge> verticesToSet(){
        Set<Edge> edges = new HashSet<>();
        for(Vertex v : this.vertices){
            for(Vertex w : v.neighbors){
                edges.add(new Edge(v.getVertexID(), w.getVertexID()));
            }
        }
        return edges;
    }

    /**
     * Gibt Kantenliste wieder. Notwendig, wenn auf Indizes der Kantenmenge zugegriffen werden soll
     * Nützlich für die Implementierung von den Algorithmen von Tamura für alle spannende Bäume
     */
    public List<Edge> verticesToList(){
        return new ArrayList<>(this.verticesToSet());
    }

    /**
     * Fügt Kante hinzu, indem es den Knoten edge.start zu den Nachbarn von dem Knoten edge.end hinzufügt
     * und andersrum
     */
    public void addEdge(Edge edge){
        this.vertices[edge.start].neighbors.add(this.vertices[edge.end]);
        this.vertices[edge.end].neighbors.add(this.vertices[edge.start]);
    }

    /**
     * Entfernt eine Kante mit der selben Logik, wie addEdge
     */
    public void removeEdge(Edge edge){
        if(!this.vertices[edge.start].neighbors.contains(this.vertices[edge.end])){
            return;
        }
        this.vertices[edge.start].neighbors.remove(this.vertices[edge.end]);
        this.vertices[edge.end].neighbors.remove(this.vertices[edge.start]);
    }

    /**
     * PrintOut vom Graphen mit Kantenmenge im Format [Kante1,Kante2, ...]
     * mit Kante1 = [Kante1.start,Kante2.end]
     */
    public void printGraphAsEdgeset(){
        System.out.println("\nKnotenanzahl: "+this.amtVertices);
        System.out.print("[");
        for(Edge edge : this.verticesToSet()){
            edge.printEdge();
        }
        System.out.print("]");
    }

    /**
     * String Variante von printGraphAsEdgeset
     */
    public String GraphAsStringAsEdgeset(){
        Set<Edge> edgeset = this.verticesToSet();
        String string ="["+Edge.edgeSetToString(edgeset)+"]";
        string = string.replace("],]","]]");
        return string ;
    }

    /**
     * Für Tests nützlich, gibt Graphen in seiner Nachbarliste wieder, d.h.
     * Der Graph mit den Knoten 0 und 1 und der Kante [0,0] und [0,1] sieht dann so aus:
     * KnotenID: 0 Knotennachbarn: [0,1]
     * KnotenID: 1 Knotennachbarn: [0]
     */
    public void printGraph() {
        System.out.println("Knoten:" + this.amtVertices);
        Vertex[] var1 = this.vertices;
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Vertex v = var1[var3];
            int var10001 = v.vertexID;
            System.out.println("KnotenID: " + var10001 + " Knotenachbarn: " + v.neighbors.toString());
        }

    }
}

