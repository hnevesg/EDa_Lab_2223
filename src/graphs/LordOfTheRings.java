/**********************************************************************
* Class Name: LordOfTheRings
* Author/s name: HNG_CML_A1_G1
* Release/Creation date: 25/11/2022
* Class version: 1st
* Class description: The aim is to create an interactive guide for consulting information about the network of relationships between
*					 characters from The Lord of the Rings, corresponding to the interactions they have had throughout the 3 books
*					 that make up the original saga
* 
***********************************************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;
import graphsDSESIUCLM.*;

public class LordOfTheRings {
	final static Scanner keyboard = new Scanner(System.in);
	
	public static void main(String[]args) throws IOException {
		Graph gr = new TreeMapGraph<>();
		
		readVertices("lotr-pers.csv",gr);
		readEdges("networks-id-3books.csv",gr);
		
		menu(gr);
	}	  
	
	/**********************************************************************
	* Method name: readVertices
	*
	* Description of the Method: The method reads the content of the file with all characters and separates 
	* 							 the id, type, subtype, name, gender, and number of references,
	* 							 allowing to store these values for objects' attributes
	*
	* Calling arguments: String file, it is the address where the file is located in the pc;
	* 					 Graph gr, it is the graph where the vertices are going to be created
	*
	* Return value: void
	*
	* Required Files: The method expects a file to be read
	*
	* FileNotFoundException: In case the file does not exist or it is not in that location
	* IOException: In case there is an error during an input-output operation (when opening the file)
	* 
	*********************************************************************/
	
	public static void readVertices(String file, Graph gr) throws IOException {
			File f = new File(file);
			BufferedReader br;
			String str;
			List<Character> l = new ArrayList<Character>();
			int ncharacters = 0; // total number of characters
			try {
				
				 br = new BufferedReader(new FileReader(f));
				 str = br.readLine();
				//Keeps repeating until there is no more information left in the file to read
				while((str=br.readLine())!=null) {
					//For saving all attributes:
					String [] array = str.split(";",6);
					String id = array[0].toLowerCase();
					String type = array[1].toLowerCase();
					String subtype = array[2].toLowerCase();
					String name = array[3].toLowerCase();
					String gender = array[4].toLowerCase();
					int nreferences = Integer.valueOf(array[5]);
					l.add(new Character(id,type,subtype,name,gender,nreferences));
					ncharacters++;
				}
				br.close();
				
			} catch (FileNotFoundException e) {
				System.out.println("Error. The file wasn't found");
			}
			for(int i=0; i<ncharacters; i++) {
				gr.insertVertex(new DecoratedElement<Character> (l.get(i).getId(), l.get(i)));
			}
	}
	
	/**********************************************************************
	* Method name: readEdges
	*
	* Description of the Method: The method reads the content of the file with all characters' relationships   
	* 							 and separates the source vertex, end vertex, and weight,
	* 							 allowing to store these values for objects' attributes
	*
	* Calling arguments: String file, it is the address where the file is located in the pc;
	* 					 Graph gr, it is the graph where the edges are going to be created
	*
	* Return value: void
	*
	* Required Files: The method expects a file to be read
	*
	* FileNotFoundException: In case the file does not exist or it is not in that location
	* IOException: In case there is an error during an input-output operation (when opening the file)
	* 
	*********************************************************************/
	
	public static void readEdges(String file, Graph gr) throws IOException {
		File f = new File(file);
		BufferedReader br;
		int nedges=0; //counter used to increase the Id of the edges
		String str;
		try {
			 br = new BufferedReader(new FileReader(f));
			StringTokenizer tokenizer; 
			 br.readLine();
			
			while((str = br.readLine()) != null) {
				tokenizer = new StringTokenizer(str,";");
				String v = tokenizer.nextToken().toLowerCase();
				String u = tokenizer.nextToken().toLowerCase();
				int weight = Integer.valueOf(tokenizer.nextToken());
				gr.insertEdge(gr.getVertex(v),gr.getVertex(u),new DecoratedElement<Integer>(String.valueOf(nedges), weight));
					nedges++;
			}
				br.close();
				
		} catch (FileNotFoundException e) {
			System.out.println("Error. The file wasn't found");
		}
	}

	/**********************************************************************
	* Method name: showInfo
	*
	* Description of the Method: The method shows by screen basic information about the characters
	* 							 of the Lord of the Rings and their relationships.
	* 							 It is executed if the user selects option "1"
	*
	* Calling arguments: Graph gr, it is the created graph
	*
	* Return value: void
	* 
	*********************************************************************/

	public static void showInfo(Graph gr) {
		System.out.println("The number of characters is " + gr.getN() + ".");
		System.out.println("The number of relationships between characters is " + gr.getM() + ".");
		System.out.print("The character with a relation with more characters is "); calc_incidentEdges(gr);
		System.out.print("The pair of characters with highest level of interaction is "); calc_weightEdges(gr);
	}

	/**********************************************************************
	* Method name: calc_incidentEdges
	*
	* Description of the Method: The method calculates the incident edges of every vertex 
	* 							 and returns the higher one, with the corresponding 
	* 							 vertex name and its number of incident edges
	*
	* Calling arguments: Graph gr, it is the created graph
	*
	* Return value: void
	* 
	*********************************************************************/
	
	public static void calc_incidentEdges(Graph gr) {
		int incident_edges, max=0;
		String character_name="";
		Vertex<DecoratedElement<Character>> character; 
		Iterator vertices=gr.getVertices();
		
		while(vertices.hasNext()) {
			incident_edges=0;
			character = (Vertex<DecoratedElement<Character>>) vertices.next();
			Iterator edges = gr.incidentEdges(character);
			
			while(edges.hasNext()){
				edges.next();
				incident_edges++;
			}

			if(incident_edges>max) {
				max = incident_edges;
				character_name = character.getElement().getElement().getName();
			}
		}
		System.out.println(character_name + " with " + max + " relationships.");
	}

	/**********************************************************************
	* Method name: calc_weightEdges
	*
	* Description of the Method: The method calculates the weight of every edge
	* 							 and obtains the higher one, with the corresponding 
	* 							 names of the source and end vertices 
	*
	* Calling arguments: Graph gr, it is the created graph
	*
	* Return value: void
	* 
	*********************************************************************/
	
	public static void calc_weightEdges(Graph gr) {
		int max=0, index=0, weight=0;
		Vertex<DecoratedElement<Character>> endVert[];
		Edge<DecoratedElement<Integer>> e; 
		String char1="", char2="";
		
		for(int i=0; i<gr.getM(); i++) {
			e = gr.getEdge(String.valueOf(i));
			weight = e.getElement().getElement();

			if(weight>max){ 
				max = weight;
				index = i;
			}	
		}
		
		endVert = gr.endVertices(gr.getEdge(String.valueOf(index)));
		char1 = endVert[0].getElement().getElement().getName();
		char2 = endVert[1].getElement().getElement().getName();
		
		System.out.println(char1 + " and " + char2 + " with " + max + " interactions.");
	}
	
	/**********************************************************************
	* Method name: witchKing
	*
	* Description of the Method: The method asks for the two leaders and shows by screen
	* 							 the command created (if possible) to defeat the Witch King.
	* 							 It is executed if the user selects option "2"
	*
	* Calling arguments: Graph gr, it is the created graph
	*
	* Return value: void
	* 
	*********************************************************************/
	
	public static void witchKing(Graph gr) {
		Vertex<DecoratedElement<Character>> leader1=askCharacter("first leader",gr);
		Vertex<DecoratedElement<Character>> leader2=askCharacter("second leader",gr);
		
		if(leader1.getElement().getElement().getType().equals("per") && leader2.getElement().getElement().getType().equals("per")){
			pathDFS(gr,leader1,leader2);
		}else {
			System.out.println("Error. At least one of the leaders is not a person");
		}
	}

	/**********************************************************************
	* Method name: pathDFS
	*
	* Description of the Method: The method calculates the path using a DFS algorithm.
	*
	* Calling arguments: Graph g, it is the created graph;
	* 					 Vertex<DecoratedElement<Character>> v, it is the initial vertex;
	* 					 Vertex<DecoratedElement<Character>> z, it is the end vertex;
	*
	* Return value: void
	* 
	*********************************************************************/
	
	public static void pathDFS(Graph g, Vertex<DecoratedElement<Character>> v,Vertex<DecoratedElement<Character>> z) {
		boolean noEnd = !v.getID().equals(z.getID()); 					// to check that the initial and end vertices are not the same
		Edge<DecoratedElement> e;
		Vertex<DecoratedElement<Character>> w, current;
		Iterator<Edge<DecoratedElement>> it;
		Stack<Vertex<DecoratedElement<Character>>> s = new Stack<Vertex<DecoratedElement<Character>>>();
		
		//DFS
		s.push(v);
		while(!s.isEmpty()) {
			current=s.pop();											// the above line avoids the EmptyStackException
			if (!current.getElement().getVisited()) {
				if(!noEnd) {                            
					current.getElement().setVisited(true);
					System.out.print(current.getID() + " "); 
					break; //final vertex found -> end
				}else {
					System.out.print(current.getID() + " ");
					current.getElement().setVisited(true);
					it = g.incidentEdges(current);
					while (it.hasNext() && noEnd) {	 
						e = it.next();
						w = g.opposite(current, e);
						if (!w.getElement().getVisited()) {
							if(checkVertexDFS(g,w)) {
								s.push(w);
								noEnd = !(w.getID().equals(z.getID())); // if the child of v is equal to the end node -> stop adding nodes to the stack
							}
						}
					}
				}
			}
		}
		if(noEnd) { 													// noEnd==true -> the end vertex has not been reached
			System.out.println("...No possible command to defeat the Witch King");
		}
		setAllFalse(g); 												// to have all vertices not visited for next iteration
	}

	/**********************************************************************
	* Method name: checkVertexDFS
	*
	* Description of the Method: The method checks if the conditions
	* 							 are accomplished by the vertex
	*
	* Calling arguments: Graph gr, it is the created graph;
	* 					 Vertex<DecoratedElement<Character>> w, it is the vertex to check
	*
	* Return value: boolean, whether the vertex accomplishes or not all conditions
	* 
	*********************************************************************/
	
	public static boolean checkVertexDFS(Graph gr, Vertex<DecoratedElement<Character>> w) {
		boolean b=false;
	
		if(w.getElement().getElement().getType().equals("per")) {
			if(!(w.getElement().getElement().getGender().equals("male") && w.getElement().getElement().getSubtype().equals("men"))) {	
				if(w.getElement().getElement().getNreferences()>=80) {
					b=true;
				}
			}
		}
		return b;
	}
	
	/**********************************************************************
	* Method name: communicationNetwork
	*
	* Description of the Method: The method asks for the sender and recipient of the message
	* 							 and shows by screen the path (if possible) to send it.
	* 							 It is executed if the user selects option "3"
	*
	* Calling arguments: Graph gr, it is the created graph;ç
	*
	* Return value: void
	* 
	*********************************************************************/
	
	public static void communicationNetwork(Graph gr) {
		Vertex<DecoratedElement<Character>> sender=askCharacter("sender character",gr);
		Vertex<DecoratedElement<Character>> recipient=askCharacter("recipient character",gr);
		
		if(sender.getElement().getElement().getType().equals("per") && recipient.getElement().getElement().getType().equals("per")){ 
			shortestPathBFS(gr,sender,recipient);
		}else {
			System.out.println("Error. At least one of the characters is not a person");
		}
	}
	
	/**********************************************************************
	* Method name: shortestPathBFS
	*
	* Description of the Method: The method calculates the path using a BFS algorithm.
	*
	* Calling arguments: Graph g, it is the created graph;
	* 					 Vertex<DecoratedElement<Character>> s, it is the initial vertex (sender);
	* 					 Vertex<DecoratedElement<Character>> r, it is the end vertex (recipient)
	*
	* Return value: void
	* 
	*********************************************************************/
	
	public static void shortestPathBFS(Graph g, Vertex<DecoratedElement<Character>> s, Vertex<DecoratedElement<Character>> r) {
		boolean noEnd = !s.getID().equals(r.getID()); 						// to check that the initial and end vertices are not the same
		Vertex<DecoratedElement<Character>> u, v = null;
		Edge e;
		Iterator<Edge> it;
		Queue<Vertex<DecoratedElement<Character>>> q = new LinkedList<Vertex<DecoratedElement<Character>>>();
		
		//BFS
		s.getElement().setVisited(true);
		q.offer(s);
		while (!q.isEmpty()) {
			u = q.poll(); 												// the above line avoids the removing operation in an empty queue (but it would return null)
			System.out.print(u.getID() + " ");
			it = g.incidentEdges(u);
			while (it.hasNext() && noEnd) {
				e = it.next();
				v = g.opposite(u, e);
				if (!(v.getElement()).getVisited()) {
					if(checkVertexBFS(g,u,v)) {
						v.getElement().setVisited(true);
						q.offer(v);			 							// if it can't be added to the queue, returns false
						noEnd = !(v.getID().equals(r.getID()));			// if the child of v is equal to the end node -> stop adding nodes to the queue
					}
				}
			}
		}
		if(noEnd) { // noEnd==true -> the end vertex has not been reached
			System.out.println("...No possible secure communication network to send the secret message");
		}
		setAllFalse(g);
	}

	/**********************************************************************
	* Method name: checkVertexBFS
	*
	* Description of the Method: The method checks if the conditions
	* 							 are accomplished by the vertex
	*
	* Calling arguments: Graph g, it is the created graph;
 	* 					 Vertex<DecoratedElement<Character>> u, it is the incident vertex to w;
	* 					 Vertex<DecoratedElement<Character>> w, it is the vertex to check
	*
	* Return value: boolean, whether the vertex accomplishes or not the conditions
	* 
	*********************************************************************/

	public static boolean checkVertexBFS(Graph g, Vertex<DecoratedElement<Character>> u, Vertex<DecoratedElement<Character>> w) {
		boolean b=false;
		Iterator it=g.getEdges();
		Edge<DecoratedElement<Integer>> e;
		if(w.getElement().getElement().getType().equals("per")) {
			while(it.hasNext()) {
				e=(Edge<DecoratedElement<Integer>>) it.next();
				if(w==g.opposite(u, e)) {
					if(e.getElement().getElement()>=10) {
						b=true;
					}
				}
			}
		
		}
		return b;
	}
	
	/**********************************************************************
	* Method name: askCharacter
	*
	* Description of the Method: The method asks to the user for a character
	*
	* Calling arguments: String s, it is the type of character (leader, sender, or recipient);
	* 					 Graph gr, it is the created graph
	* 					
	* Return value: Vertex<DecoratedElement<Character>>, the vertex corresponding to the answer of the user
	* 
	*********************************************************************/
	
	public static Vertex<DecoratedElement<Character>> askCharacter(String s, Graph gr) {
		boolean b=false;
		
		System.out.println("Please introduce the " + s);
		String str = keyboard.next().toLowerCase(); //toLowerCase(), to avoid being case sensitive
		//Keeps asking until the answer is one existing character Id 
		while(!b) {
			if(gr.getVertex(str)!=null) {
				b = true;
			}else {
				System.out.println("Error. Try again.");
				str = keyboard.next().toLowerCase();
			}
		}
		return gr.getVertex(str);
	}
	
	/**********************************************************************
	* Method name: setAllFalse
	*
	* Description of the Method: The method sets to false the visited attribute of all vertices
	*
	* Calling arguments: Graph gr, it is the created graph
	*
	* Return value: void
	* 
	*********************************************************************/
	
	public static void setAllFalse( Graph gr) {
		Vertex<DecoratedElement<Character>> v; 
		Iterator vertices=gr.getVertices();
		
		while(vertices.hasNext()) {
			v=(Vertex<DecoratedElement<Character>>) vertices.next();
			v.getElement().setVisited(false);
		}
	}
	
	/**********************************************************************
	* Method name: showOptions
	*
	* Description of the Method: The method shows by screen the possible options that the user can choose
	*
	* Calling arguments: none
	* 					
	* Return value: void
	* 
	*********************************************************************/
	
	public static void showOptions() {
		System.out.println("Select one of the following options. If you want to exit, write 4.");
		System.out.println("1.Consult lord of the rings basic information");
		System.out.println("2.Select two leaders to knock down the Witch King");
		System.out.println("3.Send a secret message");
		System.out.println("4.Exit");
	}
	
	/**********************************************************************
	* Method name: menu
	*
	* Description of the Method: The method shows the menu to the user, and according to the answer it executes one method or another
	*
	* Calling arguments: Graph gr, the created graph
	* 					
	* Return value: void
	* 
	*********************************************************************/
	
	public static void menu(Graph gr) {
		int option=0;
		do {
		showOptions();
		option=checkKeyboard();
		System.out.println();			
		// Each option has a method in charge of executing to get the expected results
		switch (option) {
		case 1: 
			showInfo(gr);
			break;
		case 2:
			witchKing(gr);
			break;
		case 3: 
			communicationNetwork(gr);
			break;
		case 4:
			System.out.print("End of the program.");
			break;
		}
		System.out.println(" ");
		}while(option > 0 && option < 4);
	}
	
	/**********************************************************************
	* Method name: checkKeyboard
	*
	* Description of the Method: The method checks if the value introduced by
	* 							 keyboard is valid according to the possible options
	*
	* Calling arguments: none
	* 					
	* Return value: int, the valid number introduced by the user
	* 
	*********************************************************************/
	
	public static int checkKeyboard() {
		boolean result=false;
		int option=0;
		do {
		try {
			//We ask for a number in range [0,4], which is the number of options in the menu
				option=keyboard.nextInt();
				if(option<=0 || option>=5) throw new IndexOutOfBoundsException();
				result=false;
			}
			catch(InputMismatchException y){ 
				System.out.println("Error, letters and symbols are not allowed. Try again:");
				keyboard.next();
				result=true;
			}
			catch(IndexOutOfBoundsException z) {
				System.out.println("Error, the number must be in the range [1,4]. Try again:");
				result=true;
			}
		}while(result);
		return option;
	}
	
	
}
