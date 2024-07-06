package ronny.allflats2;
import java.util.Set;

public class Partition {

    Set<Edge> included;
    Set<Edge> excluded;

    public Partition(Set<Edge> included, Set<Edge> excluded) {
        this.included = included;
        this.excluded = excluded;
    }

}