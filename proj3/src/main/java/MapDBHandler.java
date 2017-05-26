import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

/**
 *  Parses OSM XML files using an XML SAX parser. Used to construct the graph of roads for
 *  pathfinding, under some constraints.
 *  See OSM documentation on
 *  <a href="http://wiki.openstreetmap.org/wiki/Key:highway">the highway tag</a>,
 *  <a href="http://wiki.openstreetmap.org/wiki/Way">the way XML element</a>,
 *  <a href="http://wiki.openstreetmap.org/wiki/Node">the node XML element</a>,
 *  and the java
 *  <a href="https://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html">SAX parser tutorial</a>.
 *  @author Alan Yao
 */
public class MapDBHandler extends DefaultHandler {
    HashMap<String,String> attb;
    long ID;
    double lon;
    double lat;
    Node node;
    ArrayList<Node> nodes;//a list of all nodes
    public int nodeCount;
    public int wayCount;
    public HashMap<Long, Integer> num;
    public WeightedQuickUnionUF uf;
    /**
     * Only allow for non-service roads; this prevents going on pedestrian streets as much as
     * possible. Note that in Berkeley, many of the campus roads are tagged as motor vehicle
     * roads, but in practice we walk all over them with such impunity that we forget cars can
     * actually drive on them.
     */
    private static final Set<String> ALLOWED_HIGHWAY_TYPES = new HashSet<>(Arrays.asList
            ("motorway", "trunk", "primary", "secondary", "tertiary", "unclassified",
                    "residential", "living_street", "motorway_link", "trunk_link", "primary_link",
                    "secondary_link", "tertiary_link"));
    private String activeState = "";
    private final GraphDB g;

    public MapDBHandler(GraphDB g) {
        attb=new HashMap<>();
        ID=0;
        lon=0;
        lat=0;
        this.g = g;
        nodes=new ArrayList<>();
        nodeCount=0;
        wayCount=0;
        num=new HashMap<>();
        uf=new WeightedQuickUnionUF(190000);
    }

    /**
     * Called at the beginning of an element. Typically, you will want to handle each element in
     * here, and you may want to track the parent element.
     * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
     *            if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if qualified names are
     *              not available. This tells us which element we're looking at.
     * @param attributes The attributes attached to the element. If there are no attributes, it
     *                   shall be an empty Attributes object.
     * @throws SAXException Any SAX exception, possibly wrapping another exception.
     * @see Attributes
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        if (qName.equals("node")) {
            activeState = "node";
            ID=Long.parseLong(attributes.getValue("id"));
            lon=Double.valueOf(attributes.getValue("lon"));
            lat=Double.valueOf(attributes.getValue("lat"));
            attb.clear();
        } else if (qName.equals("way")) {
            activeState = "way";
            ID=Long.parseLong(attributes.getValue("id"));
            attb.clear();
            nodes.clear();;
            //          System.out.println("Beginning a way...");
        } else if (activeState.equals("way") && qName.equals("nd")) {
            node=g.nodes.get(Long.parseLong(attributes.getValue("ref")));
            nodes.add(node);
            //           System.out.println("Tag with k=" + k + ", v=" + v + ".");
        }else if (activeState.equals("way") && qName.equals("tag")) {
            String k = attributes.getValue("k");
            String v = attributes.getValue("v");
            attb.put(k,v);
            //           System.out.println("Tag with k=" + k + ", v=" + v + ".");
        } else if (activeState.equals("node") && qName.equals("tag")) {
            attb.put(attributes.getValue("k"),attributes.getValue("v"));
        }
    }

    /**
     * Receive notification of the end of an element. You may want to take specific terminating
     * actions here, like finalizing vertices or edges found.
     * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
     *            if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if qualified names are
     *              not available.
     * @throws SAXException  Any SAX exception, possibly wrapping another exception.
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("way")) {
            wayCount++;
            /*if(ID==397741699L){
                System.out.println("Ways done.");
                System.out.println("WayCount="+wayCount);
            }*/
            Way way=new Way(ID, nodes, attb);
            g.putWay(way);
        }
        if(qName.equals("node")){
            Node n=new Node(ID,lon,lat,attb);
            g.putNode(n);
            nodeCount++;
            num.put(ID,nodeCount);
            /*if(n.getId()==4005231815L){
                System.out.println("Nodes done.");
                System.out.println("NodeCount="+nodeCount);
            }*/
        }
    }

}
