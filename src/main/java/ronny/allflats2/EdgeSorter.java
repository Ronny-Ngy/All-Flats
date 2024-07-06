package ronny.allflats2;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EdgeSorter {
    /**
     * Sortiert eine Liste an Kanten lexographisch
     * @param edges Liste an Kanten
     */
    public static void sort(List<Edge> edges) {
        Collections.sort(edges, new Comparator<Edge>() {
            @Override
            public int compare(Edge e1, Edge e2) {
                if (e1.start != e2.start) {
                    return e2.start - e1.start;
                } else {
                    return e2.end - e1.end;
                }
            }
        });
    }
}
