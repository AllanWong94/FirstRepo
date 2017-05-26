import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by Allan Wong on 2017/5/23.
 */
public class Route {
    private GraphDB g;
    private double start_lon;
    private double start_lat;
    private double end_lon;
    private double end_lat;
    private Node startNode;
    private Node endNode;
    private MinPQ<Node> fringe;
    private NodeComparators nc;
    private LinkedList<Long> route;
    private LinkedList<Node> nodes;
    private double routeLength;
    private MapDBHandler mh;

    public Route(GraphDB G,MapDBHandler MH){
        g=G;
        mh=MH;
        nc=new NodeComparators();
        fringe=new MinPQ<>(nc);
        route=new LinkedList<>();
        nodes=new LinkedList<>();
        routeLength=0;
    }

    private class NodeComparators implements Comparator<Node>{
        @Override
        public int compare(Node node1,Node node2) {
            if((node1.priority-node2.priority)<0){
                return -1;
            }else if((node1.priority-node2.priority)>0){
                return 1;
            }
            return 0;
        }

    }

    public void setStartEnd(double Start_lon,double Start_lat,double End_lon,double End_lat){
        start_lon=Start_lon;
        start_lat=Start_lat;
        end_lon=End_lon;
        end_lat=End_lat;
        startNode=g.findClosestNode(start_lon, start_lat);
        endNode=g.findClosestNode(end_lon, end_lat);
        startNode.priority=0;
        System.out.println(mh.uf.connected(mh.num.get(startNode.getId()),mh.num.get(endNode.getId())));
    }

    public void setStartEnd(Long id1,Long id2){
        startNode=g.connectedNodes.get(id1);
        endNode=g.connectedNodes.get(id2);
        startNode.priority=0;
        System.out.println(mh.uf.connected(mh.num.get(id1),mh.num.get(id2)));
    }


    public void findCrossroads(){
        Set<Long> id=g.adjacencyList.keySet();
        for (Long l:id){
            ArrayList<Long> al=g.adjacencyList.get(l);
            if(al.size()>2){
                System.out.println("Crossroad of "+al.size()+" nodes!");
            }
        }
    }

    public LinkedList<Long> getRoute() {
        return route;
    }

    public LinkedList<Long> route(){
        route=new LinkedList<>();
        routeLength=0;
        double p;
        fringe.insert(startNode);
        ArrayList<Long> neighbors;
        Node temp=startNode;
        Node temp2;
        while(!temp.equals(endNode)) {
            if (fringe.size()==1){
               // System.out.println("Warning! fringe about to be emptied!");
            }
            temp = fringe.delMin();
            temp.marked=true;
            neighbors = g.adjacencyList.get(temp.getId());
            if(neighbors.size()>2){
             //   System.out.println("Crossroad!");
            }
            for (Long id : neighbors) {
                Node x = g.nodes.get(id);
                if (!x.equals(endNode)) {
                    if (x.marked == false) {
                        p=temp.priority+g.calDist(temp,x)+g.calDist(x,endNode);
                        x.setPrev(temp,p);
                        fringe.insert(x);
                    }

                }else{
                    p=temp.priority+g.calDist(temp,x)+g.calDist(x,endNode);
                    x.setPrev(temp,p);
                    temp=x;
                    break;
                }
            }
        }
        while(temp.prev!=null){
            route.addFirst(temp.getId());
            nodes.addFirst(temp);
            temp=temp.prev;
        }
        return route;
    }


    public LinkedList<Node> getNodes() {
        return nodes;
    }


    public void displayRoute(){
        for(int i=route.size()-1;i>=0;i--){
            System.out.println(route.get(i));
        }
    }
}
