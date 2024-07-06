package ronny.allflats2;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FlatSorter {
    /**
     * Sortiert eine Liste an Flats lexographisch
     * @param listOfEdges Liste an Flats
     */

    public static void sort(List<List<Edge>> listOfEdges) {
        Collections.sort(listOfEdges, new Comparator<List<Edge>>() {
            @Override
            public int compare(List<Edge> o1, List<Edge> o2) {
                if(o1.size() != o2.size()){
                    return o1.size() - o2.size();
                } else {
                    for(int i = 0; i < o1.size(); i++){
                        if(o1.get(i)!=o2.get(i)) {
                            return compareEdge(o1.get(i), o2.get(i));
                        }
                    }
                }
                return 0;
            }

        });
    }
    public static int compareEdge(Edge e1, Edge e2) {
        if (e1.start != e2.start) {
            return Integer.compare(e1.start, e2.start);
        } else {
            return Integer.compare(e1.end, e2.end);
        }
    }
}
