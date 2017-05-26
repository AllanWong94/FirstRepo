import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Wraps the parsing functionality of the MapDBHandler as an example.
 * You may choose to add to the functionality of this class if you wish.
 * @author Alan Yao
 */
public class GraphDB {
    //Key:The ID of the nodes. Value:An arraylist holding all the ID
    // of the nodes that are connected to this node
    public Map<Long, ArrayList<Long>> adjacencyList;
    public HashMap<Long, Node> nodes;
    public HashMap<Long, Node> connectedNodes;
    public HashMap<Long, Way> ways;
    public WeightedQuickUnionUF uf;
    public MapDBHandler maphandler;
    /**
     * Example constructor shows how to create and start an XML parser.
     * @param db_path Path to the XML file to be parsed.
     */
    public GraphDB(String db_path) {
        adjacencyList=new HashMap<>();
        nodes=new HashMap<>();
        ways=new HashMap<>();
        uf=new WeightedQuickUnionUF(190000);
        connectedNodes=new HashMap<>();
        try {
            File inputFile = new File(db_path);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            maphandler = new MapDBHandler(this);
            saxParser.parse(inputFile, maphandler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    public void putNode(Node n){
        long id=n.getId();
        nodes.put(id,n);
    }

    public void putWay(Way w){
        Long id=w.getId();
        ways.put(id,w);
        ArrayList<Node> n=w.getNodes();
        ArrayList<Long> adjacencyPrev;
        ArrayList<Long> adjacencyNext;
        Long nodeIDPrev;
        Long nodeIDNext;
        for(int i=0;i<n.size()-1;i++){
            nodeIDPrev=n.get(i).getId();
            nodeIDNext=n.get(i+1).getId();
            connectedNodes.put(nodeIDNext,nodes.get(nodeIDNext));
            connectedNodes.put(nodeIDPrev,nodes.get(nodeIDPrev));
            maphandler.uf.union(maphandler.num.get(nodeIDNext),maphandler.num.get(nodeIDPrev));
            if (!adjacencyList.containsKey(nodeIDPrev)){
                adjacencyPrev=new ArrayList<>();
                adjacencyPrev.add(nodeIDNext);
                adjacencyList.put(nodeIDPrev,adjacencyPrev);
            }else{
                adjacencyPrev=adjacencyList.get(nodeIDPrev);
                adjacencyPrev.add(nodeIDNext);
                adjacencyList.put(nodeIDPrev,adjacencyPrev);
            }
            if (!adjacencyList.containsKey(nodeIDNext)){
                adjacencyNext=new ArrayList<>();
                adjacencyNext.add(nodeIDPrev);
                adjacencyList.put(nodeIDNext,adjacencyNext);
            }else{
                adjacencyNext=adjacencyList.get(nodeIDNext);
                adjacencyNext.add(nodeIDPrev);
                adjacencyList.put(nodeIDNext,adjacencyNext);
            }
        }
    }

    public Node findClosestNode(double lon,double lat){
        updateConnectedNodes();
        Set<Long> nodeNo=connectedNodes.keySet();
        double minDist=1000;
        double dist=0;
        Long index=0L;
        for(Long id:nodeNo){
            dist=calDist(lon,lat,id);
            if(dist<minDist){
                minDist=dist;
                index=id;
            }
        }
        return nodes.get(index);
    }

    public double calDist(double lon,double lat,Long id){
        Node node=nodes.get(id);
        double lon2=node.getLon();
        double lat2=node.getLat();
        return calDist(lon,lat,lon2,lat2);
    }

    public double calDist(Node node1, Node node2){
        double lon1=node1.getLon();
        double lat1=node1.getLat();
        double lon2=node2.getLon();
        double lat2=node2.getLat();
        return calDist(lon1,lat1,lon2,lat2);
    }


    public double calDist(Long id1,Long id2){
        Node node1=nodes.get(id1);
        Node node2=nodes.get(id2);
        return calDist(node1,node2);
    }

    public double calDist(double lon1,double lat1,double lon2,double lat2){
        return Math.sqrt((lon1-lon2)*(lon1-lon2)+(lat1-lat2)*(lat1-lat2));
    }

    public void updateConnectedNodes(){
        connectedNodes.clear();
        Set<Long> set=adjacencyList.keySet();
        for(Long id:set){
            connectedNodes.put(id,nodes.get(id));
        }
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
    }
}
