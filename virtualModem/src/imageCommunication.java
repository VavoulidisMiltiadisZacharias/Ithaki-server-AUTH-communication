import java.io.*;
import java.util.ArrayList;

import ithakimodem.Modem;
public class imageCommunication 
{
	
//Variables declaration/initialization
	ArrayList<Integer> bytesOfImage = new ArrayList<Integer>(); //Store the bytes' sequence (integer form)
	FileOutputStream imageNoError; //image with no errors
	
//Constructor
	
	/* Will be called to demonstrate the echoPacketComunication
	 * 
	 * Within its body i create a file, 
	 * call all the functions that need to be called
	 * and finally close the file
	 * 
	 * I also print some info messages
	 */
	imageCommunication(Modem modem, String imageWithoutErrors) throws IOException
	{
		System.out.println();
		System.out.println("START OF IMAGE_WITHOUT_ERRORS_COMMUNICATION");
		System.out.println();
		
		this.tryToCommunicate(modem, imageWithoutErrors);
		System.out.println(bytesOfImage.size());
		
		//Close all the files i opened
		imageNoError.close();
		
		System.out.println();
		System.out.println("END OF IMAGE_WITHOUT_ERRORS_COMMUNICATION");
		System.out.println();
	}
	
//Methods
	
	/* Communicate with the server...send request, read the incoming packets,
	 */
	public void tryToCommunicate(Modem modem, String imageWithoutErrors) throws FileNotFoundException
	{
		int read;
		
		//Clear the ArrayList at first
		bytesOfImage.clear();
		
		//Read the image (similar process to echo packets')
		 for(;;)
	     {
	  	     modem.write(imageWithoutErrors.getBytes());
	  	     
	  	     //Try to read the packet
	  	     for (;;) 
	  	     {
	  	    		 read = modem.read();
	  	    		 if((read != -1))
	  	    		 { 		 
	  	    			 bytesOfImage.add(read);
	  	    		 }
	  	    		 else
	  	    			 break;       
	  	     }
	  	     this.checkImageDelimiter();
	  	     break;
	     }
		
	}
	
	
	/* Checks the first two and the last two bytes (called delimiters)
	 * 
	 * If the delimiters of the file are correct then this file is an image
	 * 
	 * Create a ".jpg" file to save the image
	 * 
	 * Return "true" if the delimiters are correct else "false"
	 */
	public void checkImageDelimiter() throws FileNotFoundException
	{
		//Correct delimiters
		if((bytesOfImage.get(0) == 255)
		&&(bytesOfImage.get(1) == 216)
		&&(bytesOfImage.get(bytesOfImage.size()-2) == 255)
		&&(bytesOfImage.get(bytesOfImage.size()-1) == 217))
		{
			imageNoError = new FileOutputStream("imageNoError.jpg");
					
			for(int i = 0 ; i < bytesOfImage.size() ; i++)
		    {
		 	    try 
		 	    {
					imageNoError.write(bytesOfImage.get(i));
					
				} catch (IOException e) 
		 	    {
					System.out.println("AN IO Exception has been spotted");
					e.printStackTrace();
				}
		 	}
		}
		
		//False delimiters
		else
		{
			System.out.println("WARNING: False delimiters");
		}
	}
}
