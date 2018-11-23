/* Starter code for enumerating topological orders of a DAG
 * @author
 */

package sxm180018;

import rbk.Graph;
import sxm180018.Enumerate;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Timer;
import rbk.Graph.Vertex;
import rbk.Graph.Edge;
import rbk.Graph.Factory;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class EnumerateTopological extends GraphAlgorithm<EnumerateTopological.EnumVertex> {
	boolean print; // Set to true to print array in visit
	long count; // Number of permutations or combinations visited
	Selector sel;
	private static List<Graph.Vertex> finishList;
	EnumerateTopological indegree;

	public EnumerateTopological(Graph g) {
		super(g, new EnumVertex());
		print = false;
		count = 0;
		sel = new Selector();

	}

	static class EnumVertex implements Factory {
		int indegree;

		EnumVertex() {	}

		public EnumVertex make(Vertex u) {
			return new EnumVertex();
		}
	}

	class Selector extends Enumerate.Approver<Vertex> {
		

		@Override
		public boolean select(Vertex u) {

			if (get(u).indegree == 0) {
				for (Edge e : g.incident(u)) {
					Vertex v = e.otherEnd(u);
					get(v).indegree--; 
				}
				return true;
			}
			return false;
		}

		@Override
		public void unselect(Vertex u) {
			for (Edge e : g.incident(u)) {
				Vertex v = e.otherEnd(u);
				get(v).indegree++; 
			}
		}

		@Override
		public void visit(Vertex[] arr, int k) {
			count++;
			if (print) {
				for (Vertex u : arr) {
					System.out.print(u + " ");
				}
				System.out.println();
			}
		}
	}

	private void init() {

		for (Vertex u : g ) {
			get(u).indegree = u.inDegree();
		}

	}

	// To do: LP4; return the number of topological orders of g
	public long enumerateTopological(boolean flag) {
		//EnumerateTopological 
		print = flag;
		Vertex[] arr = g.getVertexArray();
		Enumerate e = new Enumerate (arr, sel);
		init();
		e.permute(arr.length);
		return count;
	}

	// -------------------static methods----------------------

	public static long countTopologicalOrders(Graph g) {
		EnumerateTopological et = new EnumerateTopological(g);
		return et.enumerateTopological(false);
	}

	public static long enumerateTopologicalOrders(Graph g) {
		EnumerateTopological et = new EnumerateTopological(g);
		return et.enumerateTopological(true);
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner in;
		int VERBOSE = 0;
		/*
		if (args.length > 0) {
			VERBOSE = Integer.parseInt(args[0]);
		}*/
		
		File file = new File("/Users/Shivani/eclipse-workspace/LP4/sxm180018/input/permute-dag-50-800.txt");
        in = new Scanner(file);
		Graph g = Graph.readDirectedGraph(in);
		Graph.Timer t = new Graph.Timer();
		long result;
		if (VERBOSE > 0) {
			result = enumerateTopologicalOrders(g);
		} else {
			result = countTopologicalOrders(g);
		}
		System.out.println("\n" + result + "\n" + t.end());
	}

}
