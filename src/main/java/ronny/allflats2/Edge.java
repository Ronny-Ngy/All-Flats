package ronny.allflats2;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Edge {
    /**
     * start und end sind die zwei Knotenelemente einer Kante
     */
    int start;
    int end;

    /**
     * Constructor sortiert automatisch, sodass am Ende start < end
     */
    public Edge(int start, int end) {
        if(start > end){
            this.start = end;
            this.end = start;
        }
        else{
            this.start = start;
            this.end = end;
        }
    }

    /**
     * Kanten sollen identisch sein, wenn sie die selben Knoten haben
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return ((start == edge.start && end == edge.end)
//                || (end == edge.start && start == edge.end));
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(start+end);
    }

    /**
     * Benutzt in Tests, ob es sich um die richtige Kante handelt
     */
    public void printEdge(){
        System.out.print("["+this.start+","+this.end+"]");
    }

    /**
     * printEdge, aber als String und nicht als PrintOut
     */
    public String edgeToString(){
        return "["+this.start+","+this.end+"]";
    }

    /**
     * @param set Kantenmenge, die als String gegeben wird
     */
    public static String edgeSetToString(Set<Edge> set){
        String string = "";
        for(Edge edge: set){
            string = edge.edgeToString()+","+string;
        }
        return string;
    }
    /**
     * @param list Kantenliste, die als String gegeben wird
     */
    public static String edgeListToString(List<Edge> list){
        String string = "";
        for(Edge edge: list){
            string = edge.edgeToString()+","+string;
        }
        return string;
    }
}