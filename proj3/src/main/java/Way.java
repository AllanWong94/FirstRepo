import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Allan Wong on 2017/5/23.
 */
public class Way {

    private Long id;
    private ArrayList<Node> nodes;
    private HashMap<String, String> attributes;

    public Way(Long ID,ArrayList<Node> Nodes,HashMap<String, String> attb){
        id=ID;
        nodes=Nodes;
        attributes=attb;
    }

    public void addNode(Node n){
        nodes.add(n);
    }

    public Long getId() {
        return id;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public HashMap<String, String> getAttributes() {
        return attributes;
    }




}
