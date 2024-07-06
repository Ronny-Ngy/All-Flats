package ronny.allflats2;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import javafx.fxml.FXML;
//import javafx.scene.control.Label;


public class HelloController {
    public TextArea edgeSet;
    public TextField amtVertices;
    public TextField fileName;
    @FXML
    private Label welcomeText;
    @FXML
    /**
     * Funktion des Calculate Buttons
     */
    protected void calculate() throws IOException, InterruptedException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        /**
         * Name der Output Datei
         */
        String name = "";
        name = name+fileName.getText();
        if(name.equals("")){
            welcomeText.setText("Please enter a file name");
            return;
        }
        /**
         * Konvertierung der Eingabe in einen Graphenund Erkennung von invaliden Eingaben
         */
            Integer amount = Integer.parseInt(amtVertices.getText());
            String edges = edgeSet.getText();
            edges = edges.substring(1, edges.length() - 1);
            edges = edges.replace("],[", "][");
            Pattern pattern = Pattern.compile("(\\[(\\d+),(\\d+)])+");
            Matcher matcher = pattern.matcher(edges);
            if (!matcher.find()) {
                welcomeText.setText("Wrong format");
                return;
            }
            welcomeText.setText("Calculating ...");
            Set<Edge> edgeset = new HashSet<>();
            boolean edgesFound = false;
            String number = "";
            int start = -1;
            int end = -1;
        /**
         * Erstellung der Kantenmenge anhand der Eingabe
         */
        for (char c : edges.toCharArray()) {
                if (c == '[') {
                    edgesFound = true;
                }
                if (edgesFound) {
                    if (47 < c && c < 58) {
                        number = number + c;
                    }
                    if (c == ',') {
                        start = Integer.parseInt(number);
                        if(amount<=start){
                            welcomeText.setText("Invalid edge");
                            return;
                        }
                        number = "";
                    }
                    if (c == ']') {
                        end = Integer.parseInt(number);
                        if(amount<=end){
                            welcomeText.setText("Invalid edge");
                            return;
                        }
                        number = "";
                        edgeset.add(new Edge(start,end));
                        edgesFound = false;
                    }
                }
            }
            Graph graph = new Graph(amount, edgeset);
        /**
         * Wenn der Graph nicht verbunden ist, gibt es auch keine spannenden Bäume
         */
            if (!graph.isConnected()) {
                welcomeText.setText("Graph not connected");
                return;
            }
                TimeUnit.SECONDS.sleep(1);
                Output.simpleOutput(graph,name);
                welcomeText.setText("Finished");
    }
}