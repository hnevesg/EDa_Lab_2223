
/**********************************************************************
* Class Name: HospitalTriage
* Author/s name: HNG_CML_A1_G1
* Release/Creation date: 21/10/2022
* Class version: 1st
* Class description: The aim is to simulate a hospital triage 
*
***********************************************************************/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class HospitalTriage implements Comparable<Patient>{
  static int attendTime = 10; // represents the necessary time to attend a patient: 10 minutes
  static double entryTime=10; // represents the entry time of patients, every 10 seconds
  enum severity {mild,severe,vital};
  
  public static void main (String[]args) {
	  Queue <Patient> generic = new LinkedBlockingQueue <Patient> ();
	  Queue <Patient> neurology = new ArrayBlockingQueue <Patient> (6);
	  Queue <Patient> traumatology = new LinkedBlockingQueue <Patient> ();
	  Queue  <Patient> cardiology = new PriorityQueue <Patient> ();
	  
	  readF("Patients.txt", generic);
	  classifyPatients(generic,traumatology,neurology,cardiology);
	  orderCardiologyPatients(cardiology);
	  
	  System.out.println("Neurology department\n" + neurology.toString() + "\n");
	  System.out.println("Traumatology department\n" + traumatology.toString() + "\n");
	  System.out.println("Cardiology department\n" + cardiology.toString() + "\n");
	  
  }
  
  /**********************************************************************
	* Method name: classifyPatients
	*
	* Description of the Method: The method classifies the patients according to the specialty
	*
	* Calling arguments: Queue<Patient> generic, the unordered queue containing all patients; 
	* 					 Queue<Patient> t, empty queue, it will contain all traumatology patients; 
	* 					 Queue<Patient> n, empty queue, it will contain all neurology patients; 
	* 					 Queue<Patient> c, empty queue, it will contain all cardiology patients
	*
	* Return value: void
	* 
	*********************************************************************/
  
  	public static void classifyPatients(Queue <Patient> generic, Queue <Patient> t, Queue <Patient> n, Queue <Patient> c) {
  		int count=0;
  			//We are going to dequeue the main queue to divide all patients in their sub-queues
	  		while(!generic.isEmpty()) {
	  		//get the specialty, and add the patient to the corresponding sub-queue
  			String specialty=generic.peek().getSpecialty();
  			switch (specialty) {
  			case "cardiology":
  				c.add(generic.peek());   
  				generic.poll();	
  			break;
  			case "neurology":
  				//If the total time spend by the cardiology department is smaller than an hour
  				if(count<60) {
  					n.add(generic.peek()); 
  					count+=attendTime;
  				}
  				generic.poll();
  			break;
  			case "traumatology":
  				t.add(generic.peek());
  				generic.poll();
  			break;
  		}
  	  }
  	}  
  	
  	/**********************************************************************
	* Method name: orderCardiologyPatients
	*
	* Description of the Method: The method orders the cardiology patients based on the severity
	*
	* Calling arguments: Queue<Patient> total, the full and unordered cardiology queue
	* 					
	* Return value: void
	* 
	*********************************************************************/
  	
  	public static void orderCardiologyPatients(Queue <Patient> total) {
  		Queue <Patient> mild_cases = new LinkedBlockingQueue <Patient> ();
  		Queue <Patient> severe_cases= new LinkedBlockingQueue <Patient> ();
  		Queue <Patient> vital_cases= new LinkedBlockingQueue <Patient> ();
  		
  	// The idea is to divide the main cardiology queue into 3 
  	// auxiliary queues to subsequently add them to the final queue
  		
  		int value_of_severity;
  		
  		while(!total.isEmpty()) {
  			value_of_severity=severity.severe.toString().compareTo(total.peek().getSeverity());
  			
  			if(value_of_severity>0){ 
  				mild_cases.add(total.poll());
  			}else if(value_of_severity==0) { 
  				severe_cases.add(total.poll());
  			}else { 
  				vital_cases.add(total.poll());
  			}
  		}
  		
  		addOrderedPatients(total, vital_cases,severe_cases,mild_cases);
  	}
  	
  	/**********************************************************************
	* Method name: addOrderedPatients
	*
	* Description of the Method: The method orders the cardiology patients from the auxiliary queues based on the severity
	*
	* Calling arguments: Queue<Patient> total, the empty cardiology queue; 
	* 					 Queue<Patient> vital, it contains all cardiology patients with vital severity; 
	* 					 Queue<Patient> severe, it contains all cardiology patients with severe severity; 
	* 					 Queue<Patient> mild, it contains all cardiology patients with mild severity
	*
	* Return value: void
	* 
	*********************************************************************/
  	
  	public static void addOrderedPatients(Queue<Patient> total,Queue<Patient> vital,Queue<Patient> severe,Queue<Patient> mild) {
  		while(!vital.isEmpty()) 
  			total.add(vital.poll());
  		while(!severe.isEmpty())
  			total.add(severe.poll());
  		while(!mild.isEmpty())
  			total.add(mild.poll());
  	}  		

	/**********************************************************************
	* Method name: readF
	*
	* Description of the Method: The method reads the content of the file and separates the DNI, the specialty and the severity
	*
	* Calling arguments: Queue <Patient> patients, it is the queue where all patients will be stored;
	* 					 String file, it is the address where the file is located in the pc
	*
	* Return value: Queue <Patient>, this method returns the initial queue with all unclassified patients
	*
	* Required Files: The method expects a file to be read
	*
	* FileNotFoundException: In case the file does not exist or it is not in that location
	* 
	*********************************************************************/
  	
  	public static Queue<Patient> readF(String file, Queue <Patient> patients) {
		File f = new File(file);
		Scanner sc;
		int entry_time=0;
		try {
			sc = new Scanner(f);
			StringTokenizer tokenizer;
			while(sc.hasNext()) {
				String str=sc.next();
				tokenizer = new StringTokenizer(str,";");
				String DNI=tokenizer.nextToken().toUpperCase();
				String specialty=tokenizer.nextToken().toLowerCase();
				String severity=tokenizer.nextToken().toLowerCase();
				patients.add(new Patient(DNI, specialty, severity,increaseEntrytime(entry_time)));
				entry_time++;
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error. The file wasn't found");
		}
		return patients;
		 
		}
  	
	/**********************************************************************
	* Method name: increaseEntrytime
	*
	* Description of the Method: The method increases by 10 the entry time according to patient's time entrance
	*
	* Calling arguments: int i, it is the index
	*
	* Return value: double, the updated entry time
	* 
	*********************************************************************/
	
	public static double increaseEntrytime(int i) {
		return entryTime*i+10;
	}
	
	public int compareTo(Patient o) {
		return 0;
	}
}
