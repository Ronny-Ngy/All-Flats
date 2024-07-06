package ronny.allflats2;
import java.util.HashSet;
import java.util.Set;

public class Vertex {
    int vertexID;
    Set<Vertex> neighbors;

    public Vertex(int vertexID) {
        this.vertexID = vertexID;
        this.neighbors = new HashSet<>();
    }

    public void addNeighbor(Vertex toBeAdded){
        this.neighbors.add(toBeAdded);
        toBeAdded.neighbors.add(this);
    }

    public boolean removeNeighbor(Vertex toBeRemoved){
        return this.neighbors.remove(toBeRemoved) && toBeRemoved.neighbors.remove(this);
    }

    public int getVertexID() {
        return vertexID;
    }

    @Override
    public String toString() {
        return ""+this.vertexID;
    }

    public boolean isConnectedTo(Vertex toBeConnected){
        return this.neighbors.contains(toBeConnected);
    }

    public int getAmtNeighbors(){
        return this.neighbors.size();
    }

    @Override
    public boolean equals(Object obj) {
        return this.vertexID == ((Vertex)obj).vertexID;
    }


}
