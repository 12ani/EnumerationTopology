/*EnumerateTopological class contains Selectpr class with extends
*approver class from Enumerate.java
*it overrides select, unselect and visit method from approver class.
*Long Project 4 PERT
*Team: LP 120 :
*Sivagurunathan Velayutham sxv176330
*Sai Spandan Gogineni sxg175130
*Shivani Mankotia sxm180018
*Maitreyee Mhasakar mam171630
 */

package sxm180018;

import sxm180018.Enumerate;
import sxm180018.Graph.Edge;
import sxm180018.Graph.Factory;
import sxm180018.Graph.GraphAlgorithm;
import sxm180018.Graph.Timer;
import sxm180018.Graph.Vertex;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class EnumerateTopological extends GraphAlgorithm<EnumerateTopological.EnumVertex> {
	boolean print; // Set to true to print array in visit
	long count; // Number of permutations or combinations visited
	Selector sel; //object of class Selector

	public EnumerateTopological(Graph g) {
		super(g, new EnumVertex());
		print = false;
		count = 0;
		sel = new Selector();

	}
	/*
	*@param indegree stores the count of incoming edges of a vertex
	*/
	static class EnumVertex implements Factory {
		int indegree; // increment and decrement inDegree for vertices

		EnumVertex() {
		}

		public EnumVertex make(Vertex u) {
			return new EnumVertex();
		}
	}

	class Selector extends Enumerate.Approver<Vertex> {
		// methods to check whether to select a vertex or not
		// select and unselect methods are called in permute method
		@Override
		public boolean select(Vertex u) {

			if (get(u).indegree == 0) {
				// If vertex selected, decrement inDegree of adjacent vertices by 1
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
			// when vertex unselected, increment inDegree of adjacent vertices by 1
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

	/*
	* init() is helper method to retrieve the indegree of each vertex.
	*/

	private void init() {
		for (Vertex u : g) {
			get(u).indegree = u.inDegree();
		}

	}

	//returns the number of topological orders of g
	//getVertexArray is a function of graph class
	public long enumerateTopological(boolean flag) {
		print = flag;
		Vertex[] arr = g.getVertexArray();
		Enumerate e = new Enumerate(arr, sel);
		init();
		//permute fucntion to make all possible permutations
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
		int VERBOSE = 0;
		Scanner in;

		if (args.length > 0) {
			VERBOSE = Integer.parseInt(args[0]);
		}

		Graph g = Graph.readDirectedGraph(new Scanner(System.in));

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
