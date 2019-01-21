/*
Long Project 4 PERT
Team: LP 120 :
Sivagurunathan Velayutham sxv176330
Sai Spandan Gogineni sxg175130
Shivani Mankotia sxm180018
Maitreyee Mhasakar mam171630
 */

package sxm180018;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import sxm180018.Graph.Factory;
import sxm180018.Graph.GraphAlgorithm;
import sxm180018.Graph.Vertex;


public class DFS extends GraphAlgorithm<DFS.DFSVertex>
{

    LinkedList<Vertex> finishlist=new LinkedList<>();
    boolean isCyclic;

    public static class DFSVertex implements Factory {
        int cno;
        boolean seen;
        boolean stackStatus;
        Vertex parent;

        public DFSVertex(Vertex u)
        {
            seen = false;
            this.stackStatus = false;
            parent = null;
        }
        public DFSVertex make(Vertex u) { return new DFSVertex(u); }
    }

    public DFS(Graph g) {
        super(g, new DFSVertex(null));
    }
    public void init(){
        this.isCyclic = false;
        this.finishlist = new LinkedList<Vertex>();

        for(Vertex u : g){
            get(u).cno = 0;
            get(u).seen = false;
            get(u).stackStatus = false;
            get(u).parent = null;
        }
    }
    // function to check if node is visited
    public void dfs(Iterable<Vertex> itr){
        this.init();
        for(Vertex u : itr){
            if(!get(u).seen){
//                this.cno = this.cno+1;
                get(u).stackStatus = true;
                dfsVisitHelper(u);
            }
        }
    }


    public void dfsVisitHelper(Vertex u){
        get(u).seen = true;
//        System.out.println(this.cno);
//        get(u).cno = this.cno;

        for(Graph.Edge e : g.incident(u)){
            Vertex toVertex = e.otherEnd(u);
            /**
             * If the vertex is already in the recursion stack then it indicates that there is cycle
             * in the directed graph and hence we return and Topological Order cannot be determined.
             */

            if(get(toVertex).stackStatus && !this.isCyclic){
                this.isCyclic = true;
            }
            if(!get(toVertex).seen){
                get(u).stackStatus = true;
                get(toVertex).parent = u;
                dfsVisitHelper(toVertex);
            }
        }
        get(u).stackStatus=false;
        this.finishlist.addFirst(u);
    }

    public static DFS depthFirstSearch(Graph g) {
        DFS d = new DFS(g);
        d.dfs(g);
        return d;
    }


    /**
     * Remove vertices with no incoming edges, one at a
     * time, along with their incident edges, and add them to a list.
     *
     */
    // Member function to find topological order
    public List<Vertex> topologicalOrder1() {
        if(!g.directed){
            return null;
        }
        this.dfs(g);
        if(this.isCyclic){
            return null;
        }else{
            return this.finishlist;
        }
    }

    // Find the number of connected components of the graph g by running dfs.
    // Enter the component number of each vertex u in u.cno.
    // Note that the graph g is available as a class field via GraphAlgorithm.
    public int connectedComponents() {
        return 0;
    }

    // After running the connected components algorithm, the component no of each vertex can be queried.
    public int cno(Vertex u) {
        return get(u).cno;
    }

    // Find topological oder of a DAG using DFS. Returns null if g is not a DAG.
    public static List<Vertex> topologicalOrder1(Graph g) {
        DFS d = new DFS(g);
        return d.topologicalOrder1();
    }

    // Find topological oder of a DAG using the second algorithm. Returns null if g is not a DAG.
    public static List<Vertex> topologicalOrder2(Graph g) {
        return null;
    }

    public static void main(String[] args) throws Exception {

        String string = "10 12  1 3 1  3 2 1  2 4 1  4 7 1  1 8 1  8 5 1  8 2 1  5 4 1  6 8 1  6 10 1  5 10 1  10 9 1 0";

        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);

        // Read graph from input
        // Graph g = Graph.readGraph(in,true);
        Graph g = Graph.readDirectedGraph(in);

        g.printGraph(false);

        DFS d = new DFS(g);
        d.topologicalOrder1();
        System.out.println(d.finishlist);
    }
}
