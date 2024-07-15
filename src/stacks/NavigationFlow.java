
/**********************************************************************
* Class Name: NavigationFlow
* Author/s name: HNG_CML_A1_G1
* Release/Creation date: 14/10/2022
* Class version: 1st
* Class description: The aim is to simulate a navigation flow from a set of web pages 
*
***********************************************************************/

import java.io.*;
import java.util.StringTokenizer;
import java.util.Scanner;
import java.util.Stack;

public class NavigationFlow {
	 
	public static void main (String[]args) {
		Stack <String> URLs = new Stack <String>();
		Stack <Double> times = new Stack <Double>();
		double total=readF(URLs, times, "navegacion.txt");
		System.out.printf("The average time is %.3f seconds\n",total);
		
	}
	
	
	/**********************************************************************
	* Method name: readF
	*
	* Description of the Method: The method reads the content of the file and separates the URL from the time of access. Finally, it returns the average
	*
	* Calling arguments: Stack s, it is the stack where all web pages will be stored; 
	* 					 Stack t, it is the stack where all times will be stored; 
	* 					 String file, it is the address where the file is located in the pc
	*
	* Return value: double, this method returns the average time of all web pages
	*
	* Required Files: The method expects a file to be read
	*
	* FileNotFoundException: In case the file does not exist or it is not in that location
	* 
	*********************************************************************/
	
	public static double readF(Stack <String> s, Stack <Double> time, String file) {
		File file_name = new File(file);
		Scanner file_sc;
		StringTokenizer tokenizer;
		int counter = 0; 			//count: counts total pages accessed
		double average_time_on_page = 0, final_average;
		
		try {
			file_sc = new Scanner(file_name);
			while(file_sc.hasNext()) {
				String next_line=file_sc.next();
				if(next_line.equals("<=")) {
					s.pop();
					showURL(s);
					average_time_on_page+=average_time_update(time);
					counter++;
				}else {
					tokenizer = new StringTokenizer(next_line,",");
					s.push(tokenizer.nextToken().toLowerCase()); //URL
					showURL(s);
					time.push(Double.valueOf(tokenizer.nextToken())); //seconds
					average_time_on_page += time.peek();
					counter++;
				}
			}
			
			file_sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error. The file wasn't found");
			
		}
		
		final_average=calculate_average(average_time_on_page, counter);
		return final_average;
	}
	
	/**********************************************************************
	* Method name: calculate_average
	*
	* Description of the Method: The method calculates the average, which is the total time divided by the number of pages accessed
	*
	* Calling arguments: double average, the total time; double total, the number of pages accessed at the end 
	*
	* Return value: double, the average time
	* 
	*********************************************************************/
	
	public static double calculate_average(double average, int total) {
		return average/total;
	}
	
	/**********************************************************************
	* Method name: average_time_update
	*
	* Description of the Method: The method updates the average time after a pop, adding the previous one
	*
	* Calling arguments: Stack <Double> t: stack that contains each time of the pages accesed
	*
	* Return value: double, this method returns the average time updated
	* 
	*********************************************************************/
	
	public static double average_time_update(Stack <Double> t) {
		t.pop();
		double new_time=t.peek();
		return new_time;
	}
	
	/**********************************************************************
	* Method name: showURL
	*
	* Description of the Method: The method shows in the terminal the last visited web page
	*
	* Calling arguments: Stack <Double> s: stack that contains all URLs accesed
	*
	* Return value: void
	* 
	*********************************************************************/
	
	public static void showURL(Stack <String> s) {
		System.out.println(s.peek());
	}
}
