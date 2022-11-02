import java.io.*;
import java.util.ArrayList;
import ithakimodem.*;

public class gpsCommunication 
{
	
//Constructor
	gpsCommunication(Modem modem, String gps) throws FileNotFoundException
	{
		this.GpsCommunication(modem, gps);
	}
	
//Methods
	/* Sends a gps request code
	 * 
	 * Receives a gps packet and a google maps' image with 5 positions
	 */
	public void GpsCommunication(Modem modem, String gpsCode) throws FileNotFoundException
	{
		System.out.println("START GPS");
		int read;
		String codeToSend="";
		FileOutputStream imageGps = new FileOutputStream("imageGps.jpeg"); 
		
		String[] latitudeAndLongitudeTogether = new String[5]; 
		
		ArrayList<String> onlyGpggaList = new ArrayList<>(); //Store all the GPGGA sequences 
		ArrayList<String> finalFiveGpggaList = new ArrayList<>(); //Store 5 GPGGA sequences and use them finally
		
		//Form the request code
		String delimiter = gpsCode.substring(0, 5);
		String R = "R=1000199\r";
		
		modem.write((delimiter+R).getBytes());
		String test = "";
		
		
		//Read the packets from ithaki
		for (;;)
		 {
			 try 
			 {
				 read = modem.read();
				 if (read != -1)
				 {
					 test+=(char)read;
				 }
				 else
					 break;
			 } 
			 catch (Exception e)
			 {
				 break;
			 }
		 }
		
		
		//Find and store all the GPGGA packets
		System.out.println();
		System.out.println("ENTERED FOR LOOP");
		System.out.println();
		for(int i = 0 ; i < test.length(); i++)
		{
			if(String.valueOf(test.charAt(i)).equals("$"))
			{
				if(String.valueOf(test.substring(i+1, i+6)).equals("GPGGA"))
				{
					onlyGpggaList.add(test.substring(i, i+76)); //Add every GPGGA sequence within a list
					
				}
			}
		}
		
		//Fill the final list with 5 GPGGA sequences (time between them > 4 seconds)
		finalFiveGpggaList.add(onlyGpggaList.get(10));
		finalFiveGpggaList.add(onlyGpggaList.get(20));
		finalFiveGpggaList.add(onlyGpggaList.get(30));
		finalFiveGpggaList.add(onlyGpggaList.get(40));
		finalFiveGpggaList.add(onlyGpggaList.get(50));
		
		//Store the latitude and longitude 
		for(int i = 0 ; i < 5 ; i++)
		{
			latitudeAndLongitudeTogether[i] = finalFiveGpggaList.get(i).substring(31,35)
	  		  	  	   +String.valueOf((int)((new Float(finalFiveGpggaList.get(i).substring(35,40)).floatValue())*60))
	  		  	  	   +finalFiveGpggaList.get(i).substring(18,22)
	  		  	  	   +String.valueOf((int)((new Float(finalFiveGpggaList.get(i).substring(22,27)).floatValue())*60)); 
		}
		
		//Create the code that will send to ithaki to get 5 points
		codeToSend = gpsCode.substring(0, 5) + "T=" + latitudeAndLongitudeTogether[0] + "T=" 
		+latitudeAndLongitudeTogether[1] + "T=" + latitudeAndLongitudeTogether[2] + "T="
		+latitudeAndLongitudeTogether[3]
		+"T="+latitudeAndLongitudeTogether[4]+"\r";
		
		
		
		//Will use this in the following lines
		int k;
		ArrayList<Integer> IMAGE = new ArrayList<Integer>();
		modem.write(codeToSend.getBytes());
		
	  	     //Try to read the packet
	  	     for (;;) 
	  	     {
	  	    		 k = modem.read();
	  	    		 if((k != -1))
	  	    		 { 		 
	  	    			 IMAGE.add(k);
	  	    		 }
	  	    		 else
	  	    			 break;       
	  	     }
	  	     
	  	   
	  	   for(int i = 0 ; i < IMAGE.size() ; i++)
		    {
		 	    try 
		 	    {
					imageGps.write(IMAGE.get(i));
					
				} catch (IOException e) 
		 	    {
					System.out.println("AN IO Exception has been spotted");
					e.printStackTrace();
				}
		 	}
		
		
		//Just for debugging reasons
		System.out.println(finalFiveGpggaList.get(0));
		System.out.println(finalFiveGpggaList.get(1));
		System.out.println(finalFiveGpggaList.get(2));
		System.out.println(finalFiveGpggaList.get(3));
		System.out.println(finalFiveGpggaList.get(4));
		
		
		System.out.println("END GPS");
	}
	
}

