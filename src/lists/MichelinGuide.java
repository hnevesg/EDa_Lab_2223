
/**********************************************************************
* Class Name: MichelinGuide
* Author/s name: HNG_CML_A1_G1
* Release/Creation date: 04/11/2022
* Class version: 1st
* Class description: The aim is to create an interactive guide of all restaurants, bars and cafes in CLM
*
* Program description: The purpose of this program is to give the user the capability of getting to know the best restaurants
* 					   throughout the autonomous community of Castilla La Mancha. The user is able to choose a "custom" * search in order 
* 					   to get the TOP TEN of the region desired as well as the TOP TEN most visited sites in Ciudad Real or cafes in CLM.
* 					   There is a Menu to select which information to consult.
***********************************************************************/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Vector;

public class MichelinGuide {
	static final Scanner keyboard = new Scanner (System.in);
	static final int TOP=10; // represents the number of establishments shown in each top
	
	public static void main (String [] args) {
		List <Establishment> BestCafes = new ArrayList <Establishment> ();
		List <Establishment> Top10_CR = new LinkedList <Establishment> ();
		List <Establishment> PersonalTop = new Vector <Establishment> ();
		List <Establishment> establishments = new Vector <Establishment> ();
		readF("Restaurants.txt", establishments);
		menu(establishments,Top10_CR,BestCafes, PersonalTop);
	}
	
	/**********************************************************************
	* Method name: personalTop
	*
	* Description of the Method: The method creates the top according to the province and type of establishment selected
	* 							 and displays the selection.
	*
	* Calling arguments: List <Establishment> establishments, the list containing all establishments;
	* 					 List <Establishment> personal, the empty list that will contain the establishments only selected by the user
	* 					
	* Return value: void
	* 
	*********************************************************************/
	
	public static void personalTop(List <Establishment> establishments, List <Establishment> personal) {
		String province=askProvince();
		String type=askType();
		int size=establishments.size(); //we save the total number of the list to iterate afterwards
		for(int i=0;i<size;i++) {
			//if the province and the type of establishment selected exist, it will get all those establishments into the new list
			if(establishments.get(i).getProvince().equals(province) && establishments.get(i).getType().equals(type)) {
				//Add it to the list
				personal.add(establishments.get(i));				
			}
			
		}
		compare(personal);
		System.out.println(personal.toString());
	}
	
	/**********************************************************************
	* Method name: TopCR
	*
	* Description of the Method: The method orders all Ciudad Real establishments based on the number of total visits, showing the 10 most visited
	*
	* Calling arguments: List <Establishment> establishments, the list containing all establishments;
	* 					 List <Establishment> CR, the empty list that will contain the Ciudad Real establishments ordered
	* 					
	* Return value: void
	* 
	*********************************************************************/
	
	public static void TopCR(List <Establishment> establishments, List <Establishment> CR) {
		int size=establishments.size();
		//We need to iterate in order to find those establishments whose province attribute value equals "ciudad real"
		for(int i=0;i<size;i++) {
			if(establishments.get(i).getProvince().equals("ciudad real")) {
				//Add it to the list
				CR.add(establishments.get(i));
			}
		}
		//Sort it from most visited to least
		compare(CR);
		//Display it
		showTop(CR);
	}
	
	/**********************************************************************
	* Method name: TopCafes
	*
	* Description of the Method: The method orders, along with TopCR(), orders all cafes based on the number of total visits, showing the first 10
	*
	* Calling arguments: List <Establishment> establishments, the list containing all establishments;
	* 					 List <Establishment> cafes, the empty list that will contain the cafes establishments ordered
	* 					
	* Return value: void
	* 
	*********************************************************************/
	
	public static void TopCafes(List <Establishment> establishments, List <Establishment> cafes) {
		int size=establishments.size();
		//We need to iterate to search within all the establishments in order to find those establishments whose type attribute value equals "cafeterías"
		for(int i=0;i<size;i++) {
			if(establishments.get(i).getType().equals("cafeterías")) {
				//Add it to the list
				cafes.add(establishments.get(i));
			}
		}
		//Sort it from most visited to least
		compare(cafes);
		//Display it
		showTop(cafes);
	}
	
	/**********************************************************************
	* Method name: askProvince
	*
	* Description of the Method: The method, as it names already infers, asks to the user to choose a province within the region
	*
	* Calling arguments: none
	* 					
	* Return value: String, the answer of the user
	* 
	*********************************************************************/
	
	public static String askProvince() {
		boolean b=false;
		keyboard.nextLine(); //We are using this here because if not, the buffer would contain a "\n" character, making it fail during execution
		System.out.println("Please introduce the province");
		String s = keyboard.nextLine().toLowerCase(); //toLowerCase(), to avoid being case sensitive
		//Keeps asking until the answer is one from the expected ones 
		while(!b) {
			if(s.contains("ciudad real") || s.contains("albacete") ||s.contains("cuenca") || s.contains("toledo") || s.contains("guadalajara")) {
			b = true;
			}else {
				System.out.println("Error. Try again.");
				s = keyboard.nextLine().toLowerCase();
			}
		}
		//Once saved, we return the value to the method for posterior usage
		return s;
	}
	
	/**********************************************************************
	* Method name: askType
	*
	* Description of the Method: The method asks to the user to choose the establishment type
	*
	* Calling arguments: none
	* 					
	* Return value: String, the answer of the user
	* 
	*********************************************************************/
	
	public static String askType() {
		boolean b=false;
		String s;
		System.out.println("Please introduce the type of establishment: bares, restaurantes o cafeterías");
		//The loop is again for asking until the answer is one from the expected ones 
		do {
			s=keyboard.next().toLowerCase();
			switch (s) {
			case "bares":
				b=true;
				break;
			case "restaurantes":
				b=true;
				break;
			case "cafeterías":
				b=true;
				break;
			case "cafeterias": // We use cafeterias/cafeterías to prevent from system failure
				s="cafeterías";
				b=true;
				break;

			default:
				System.out.println("Error. Please try again");
			}
		} while(!b);
		//Once saved, we return the value to the method for posterior usage
		return s;		
	}
	
	/**********************************************************************
	* Method name: readF
	*
	* Description of the Method: The method reads the content of the file and separates 
	* 							 the establishment type, name, postal code, town, province, and total visits,
	* 							 allowing to store these values for objects' attributes
	*
	* Calling arguments: String file, it is the address where the file is located in the pc;
	* 					 List <Establishment> establishments, it is the empty list where all establishments will be stored
	*
	* Return value: List <Establishment>, this method returns the list with all the establishments in CLM
	*
	* Required Files: The method expects a file to be read
	*
	* FileNotFoundException: In case the file does not exist or it is not in that location
	* 
	*********************************************************************/
	
	public static List <Establishment> readF(String file, List <Establishment> establishments) {
		File f = new File(file);
		Scanner sc;
		try {
			sc = new Scanner(f);
			StringTokenizer tokenizer;
			//Keeps repeating until there is no more information left in the file to read
			while(sc.hasNext()) {
				String str=sc.nextLine();
				//For saving all atributes:
				tokenizer = new StringTokenizer(str,";");
				String type=tokenizer.nextToken().toLowerCase();
				String name=tokenizer.nextToken().toLowerCase();
				int postal_code=Integer.valueOf(tokenizer.nextToken()); 
				String town=tokenizer.nextToken().toLowerCase();
				String province=tokenizer.nextToken().toLowerCase();
				int total_visits=Integer.valueOf(tokenizer.nextToken());
				
				//an object of the type establishment is created every time it reads a line
				switch (type) {
				case "bares":
					establishments.add(new Establishment(type, name, postal_code,town,province,total_visits));
					break;
				case "cafeterías":
					establishments.add(new Establishment(type, name, postal_code,town,province,total_visits));
					break;
				case "restaurantes":
					establishments.add(new Establishment(type, name, postal_code,town,province,total_visits));				
					break;
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error. The file wasn't found");
		}
		
		return establishments;
		 
	}
	
	/**********************************************************************
	* Method name: showTop
	*
	* Description of the Method: The method shows by screen the first 10 elements of any list
	*
	* Calling arguments: List <Establishment> l, a list
	* 					
	* Return value: void
	* 
	*********************************************************************/
  	
	public static void showTop(List <Establishment> l) {
		for(int i=0;i<TOP;i++)
		System.out.println(l.get(i));
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
		System.out.println("1.Create a personal top");
		System.out.println("2.See 10 most visited sites in Ciudad Real");
		System.out.println("3.See 10 most visited cafes in Castilla-La Mancha");
		System.out.println("4.Exit");
	}
	
	/**********************************************************************
	* Method name: menu
	*
	* Description of the Method: The method shows the menu to the user, and according to the answer it executes one method or another
	*
	* Calling arguments: List <Establishment> establishments, the list containing all establishments;
	* 					 List <Establishment> cr, the list that contains only the Ciudad Real establishments
	* 					 List <Establishment> cafes, the list that contains only the cafes establishments
	* 					 List <Establishment> personal, the list that contains only the establishments selected by the user
	* 					
	* Return value: void
	* 
	*********************************************************************/
	
	public static void menu(List <Establishment> establishments, List <Establishment> cr, List <Establishment> cafe, List <Establishment> personal) {
		int option=0;
		do {
		showOptions();
		boolean result=false;
		do {
		try {
			//We ask from a number in range [0,4], which is the number of options in the menu
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
		 			
		// Each option has a method in charge of executing to get the expected results
		switch (option) {
		case 1: 
			personalTop(establishments, personal);
			break;
		case 2:
			TopCR(establishments, cr);
			break;
		case 3: 
			TopCafes(establishments, cafe);
			break;
		case 4:
			System.out.print("End of the program.");
			break;
		}
		System.out.println(" ");
		}while(option > 0 && option < 4);
	}
	
	/**********************************************************************
	* Method name: compare
	*
	* Description of the Method: The method compares every element of the list with each other and orders them, placing the one with most number of visits to the first position
	*
	* Calling arguments: List <Establishment> l, a list
	* 					
	* Return value: void
	* 
	*********************************************************************/
	
	public static void compare(List <Establishment> l) {
		Collections.sort(l, new Comparator<Establishment>() {
			public int compare(Establishment o1, Establishment o2) {
				return Integer.compare(o1.getTotal_visits(),o2.getTotal_visits());
			}
		}.reversed());
	}
}
