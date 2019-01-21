
/*
Long Project 4 PERT
Team: LP 120 :
Sivagurunathan Velayutham sxv176330
Sai Spandan Gogineni sxg175130
Shivani Mankotia sxm180018
Maitreyee Mhasakar mam171630
 */
package sxm180018;
import sxm180018.DFS;
import sxm180018.Graph;
import sxm180018.Graph.Edge;
import sxm180018.Graph.Factory;
import sxm180018.Graph.GraphAlgorithm;
import sxm180018.Graph.Vertex;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * PERT is used to calculate completion time of a project.
 * A graph represents a project.
 * Graph is accessed by calling object of Graph class.
 * Object of DFS class is used to access topological ordering on graph using methods of DFS class.
 * Every project has tasks that need to be completed.
 * Critical tasks are tasks that are completed on high priority inorder to avoid delay of completion of project.
 * Every node in the graph represents a task for completion.
 * Critical nodes are high priority tasks.
 * Critical path is order in which critical tasks need to be completed.
 * Every task has earliest completion time, latest completion time, slack time allowed to delay the task and duration of time required to complete the task.
 *s, first task  is start node of graph; t the end task is last node in the graph.
 * PERT calculates the EC, LC ,Slack,Critical nodes , critical time duration as path length of the graph.
 *  */



public class PERT extends GraphAlgorithm<PERT.PERTVertex> {
    LinkedList<Vertex> criticalpath = new LinkedList<>();
    LinkedList<Vertex> toporder= new LinkedList<>();
    int maxtime;        // Maximum time taken by the project
    int numcritical=0; // Variable to count number of critical nodes in the graph , critical nodes are critical tasks that need to be completed on priority basis.

    /**
     * PERTVertex class creates nodes in the graph.
     * The graph represents a project and the nodes represent the tasks of the project that need to be completed in stipulated deadline.
     * Every node represents a task to be completed.
     * Task has earliest completion time , latest completion time, duration of time required to complete the task.
     *
     * */

    public static class PERTVertex implements Factory
    {
        int ec=0; // Earliest completion time of task node
        int lc=0; // Latest completion time of task node
        int slack=0; // slack available time to delay task without delaying project of task node
        int duration=0; // time duration taken by task node to complete a task

        public PERTVertex(Vertex u)
        {
            // Initialization
            this.ec=0;
        }
        public PERTVertex make(Vertex u) { return new PERTVertex(u); }
    }

    public PERT(Graph g) {
        super(g, new PERTVertex(null));
    }
    /**
     * Set duration of each task in the project.
     * To set duration time for each node of graph
     * @param u is node for which duration is to be set.
     * @param d is the specified duration of the node.
     * */


    public void setDuration(Vertex u, int d)
    {
        get(u).duration=d;
    }

    /**
     * Checks if graph contains a cycle
     * Calls DFS object to check cycle in graph
     * */


    public boolean pert()
    {

        DFS d=new DFS(g);
        return d.isCyclic;
    }

    /**
     * Setter method for calculation of earliest completion time for tasks in the project.
     * The graph is traversed according to a topological order on the graph.
     * For every node in the topological ordered list, ec is calculated
     * maxtime is maximum time taken by the project for completion.
     * Maxtime is earliest completion time of the last task in the project.
     *
     * */


    // Method to calculate EC of all vertices of the graph
    public void calEC()
    {


        for(Vertex x:toporder)
        {
            ec(x);
        }
        maxtime = get(g.getVertex(g.size())).ec;

    }
    /**
     * Setter method for calculation of latest completion time for tasks in the project.
     * Lc of every task is by default assigned as maxtime required by the project.
     * The graph is traversed in reverse topological order. i.e from last task in the project to first task.
     * For every node in the reversed topological ordered list, lc is calculated
     *
     * */

    // Method to calculate LC of all nodes in the graph
    public void calLC()
    {
        for(Vertex u:g)
        {
            get(u).lc=maxtime;
        }

        //lc of last element t =t.ec;

        // reverse topological order and access vertex from lst to first
        // Reverses topological order in place.
        Collections.reverse(toporder);



        for(Vertex x:toporder)
        {
            lc(x);

        }


    }
    /**
     * Setter method for calculation of slack time for tasks in the project.
     * Every task in the graph, slack time is the maximum delay for completion of task without delaying the project.
     * */


    // Method to calculate slack of all nodes in the graph

    public void calSlack()
    {
        for(Vertex u:g)
        {
            get(u).slack=slack(u);

        }
    }

    /**
     * Getter method for calculation of earliest completion time for tasks in the project.
     * Ec of every task = maximum ec among its preceding depending tasks + duration time of current task.
     * @param u is vertex for which ec is to be set.
     * if current ec of node is less than maximum ec of preceding nodes+duration, ec is set to newly calculated ec.
     * Returns ec of the vertex u.
     * */


    // Method that calculates Earliest completion time of a node task in the graph
    public int ec(Vertex u)
    {

        for (Edge e : g.incident(u))
        {
            Vertex v = e.otherEnd(u);
            if(get(u).ec+get(v).duration>get(v).ec)
            {
                get(v).ec=get(u).ec+get(v).duration;
            }
        }



        return get(u).ec;
    }

    /**
     * Getter method for calculation of latest completion time for tasks in the project.
     * Lc of every task = minimum  lc among its succeeding depending tasks - duration time of succeeding task.
     * @param u is vertex for which lc is to be set.
     * if current lc of node is more than minimum lc of preceding nodes-duration, lc is set to newly calculated lc.
     * Returns lc of the vertex u.
     * */


    // Method that calculates Latest completion time of a node task in the graph
    public int lc(Vertex u)
    {

        for (Edge e : g.incident(u))
        {
            Vertex v = e.otherEnd(u);
            if(get(v).lc-get(v).duration<get(u).lc)
            {
                get(u).lc=get(v).lc-get(v).duration;
            }

        }
        return get(u).lc;


    }

    /**
     * Getter method for calculation of slack  time for tasks in the project.
     * Slack of every task = Latest completion time of task - earliest completion time for the task.
     * Slack time indicates maximum delay in completion without delay in final project completion.
     * @param u is vertex for which slack is to be set.
     *
     * Returns slack of the vertex u.
     * */


    // Returns slack of a vertex
    public int slack(Vertex u)
    {
        //Slack= LC(vertex)-EC(Vertex)
        get(u).slack=get(u).lc - get(u).ec;
        return get(u).slack;
    }
    /**
     * Getter method for calculation of critical task order for tasks in the project.
     * A task is marked critical if its slack time is zero  i.e. it if task is delayed, it results in delay of the project.
     * Critical path is the order of critical tasks that need to be completed in order to avoid delay of project.
     * path length is total time duration of critical tasks to complete the project on time.
     * numcritical counts number of critical tasks in project.
     * critical path stores the critical task numbers.
     * Returns pathlength of the graph.
     * */

    // method that returns Critical path length
    public int criticalPath()
    {
        int pathlength=0;
        // Path length is addition of duration of critical nodes on path which is equal to latest completion time of the last node.
        pathlength= get(toporder.getFirst()).lc;
        // Adds critical node to critical path
        criticalpath.clear();
        for(Vertex u:g)
        {
            if(get(u).slack==0)
            {
                criticalpath.add(u);
                numcritical+=1;

            }

        }

        return pathlength;
    }

    /**
     * Getter method to check if a node is critical task of the project.
     * A node in graph is critical node if its slack time is zero.
     * @param u is vertex for which critical condition is to be checked..
     * Returns true if node is critical else false.
     * */


    // returns true if node is critical
    public boolean critical(Vertex u)
    {
        if(get(u).slack==0)
        {
            return true;
        }
        else{return false; }

    }
    /**
     * Getter method for number of critical nodes in the graph.
     * Returns number of critical nodes in the graph.
     * */


    //@return numCritical returns number of critical nodes in the graph
    public int numCritical()
    {
        return numcritical;

    }

    /**
     * Getter method for topological ordering on the graph.
     *
     * Object of DFS is created that runs topological sorting to return topological ordering on the graph
     * If graph contains cycle or graph is not directed, it returns null else it returns topological order of nodes in the graph.
     * 
     * */


    /// runs topological order on the graph
    public LinkedList<Vertex> topologicalorder()
    {
        DFS d=new DFS(g);
        if(d.isCyclic|| !g.directed)
        {
            // pert returns true on finding non DAG

            return null;

        }
        else
        {
            // run topological ordering on the graph
            //pert();
            d.topologicalOrder1();

            toporder=d.finishlist;
            return toporder;




        }








    }

    /**
     * Static method to run PERT on graph g.
     * @param g is gRaph that represents the project.
     * @param duration is prespecified durations of each task in the project / nodes in the graph.
     * An edge is added from start node(s) to all nodes in graph.
     * An edge is added from all nodes to end node(t) in the graph.
     * Duration time is set for all nodes.
     * Topological order is stored in topoorder.
     * Earliest completion time, latest completion time and slack of ndoes is calculated.
     *
     * */


    // setDuration(u, duration[u.getIndex()]);
    public static PERT pert(Graph g, int[] duration)
    {

        // Connecting start and end vertices to all nodes in the graph
        Vertex s = g.getVertex(1);
        Vertex t = g.getVertex(g.size());
        int m = g.edgeSize();
        for(int i=2; i<g.size(); i++) {
            g.addEdge(s, g.getVertex(i), 1, ++m);
            g.addEdge(g.getVertex(i), t, 1, ++m);
        }


        PERT p= new PERT(g);
        // Set duration of each vertex in the graph
        for(Vertex u:g)
        {
            p.setDuration(u,duration[u.getIndex()]);

        }

        // Run topological order on the graph
        p.topologicalorder();
        // Calculate EC of all vertices
        p.calEC();
        // Calculate LC of all vertices
        p.calLC();
        // Calculate slack of all vertices
        p.calSlack();


        return p;
    }

    public static void main(String[] args) throws Exception
    {
        String graph = "11 31   1 2 1   1 3 1   1 4 1   1 5 1   1 6 1   1 7 1   1 8 1   1 9 1   1 10 1  1 11 1   2 4 1   2 5 1  2 11 1   3 5 1   3 6 1  3 11 1   4 7 1  4 11 1   5 7 1   5 8 1  5 11 1   6 8 1   6 9 1  6 11 1   7 10 1 7 11 1   8 10 1 8 11 1   9 10 1 9 11 1    10 11 1 0 3 2 3 2 1 3 2 4 1 0";
        Scanner in;
        // If there is a command line argument, use it as file from which
        // input is read, otherwise use input from string.
        in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(graph);

        Graph g = Graph.readDirectedGraph(in);
        g.printGraph(false);

        PERT p = new PERT(g);
        for(Vertex u: g) {
            p.setDuration(u, in.nextInt());
        }
        // Run rbk.PERT algorithm.  Returns null if g is not a DAG
        if(p.pert()) {
            System.out.println("Invalid graph: not a DAG");
        } else {
            System.out.println("Number of critical vertices: " + p.numCritical());
            System.out.println("u\tEC\tLC\tSlack\tCritical");
            for(Vertex u: g) {
                System.out.println(u + "\t" + p.ec(u) + "\t" + p.lc(u) + "\t" + p.slack(u) + "\t" + p.critical(u));
            }
        }
        System.out.println(p.criticalpath);
        System.out.println(p.criticalPath());

    }
}
