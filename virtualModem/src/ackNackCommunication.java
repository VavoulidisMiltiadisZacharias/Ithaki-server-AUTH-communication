import java.io.*;
import java.util.ArrayList;
import ithakimodem.Modem;

public class ackNackCommunication 
{
	//Variables declaration/Initialization
	private int minutes;
	private long startingMoment;
	private long endingMoment;
	private long sendingMoment;
	private long receivingMoment;
	private long responseMoment;
	private long totalNumberOfPacketsReceived;
	private long totalNumberOfTimesNackCodeSent;
	private String ackNackPacket;
	private String ackCode;
	private String nackCode;
	private boolean correctPacket;
	private long frameCheckSequence;
	private long XOR;
	
	PrintStream ackNackPacketsResponse;
	
//Setters
	public void setMinutes(int minutes)
	{
		this.minutes = minutes;
	}
	
	public void setStartingMoment(long startingMoment)
	{
		this.startingMoment = startingMoment;
	}
	
	public void setEndingMoment(long endingMoment)
	{
		this.endingMoment = endingMoment;
	}
	
	public void setSendingMoment(long sendingMoment)
	{
		this.sendingMoment = sendingMoment;
	}
	
	public void setReceivingMoment(long receivingMoment)
	{
		this.receivingMoment = receivingMoment;
	}
	
	public void setResponseMoment(long responseMoment)
	{
		this.responseMoment = responseMoment;
	}
	
	public void setTotalNumberOfPacketsReceived(long totalNumberOfPacketsReceived)
	{
		this.totalNumberOfPacketsReceived = totalNumberOfPacketsReceived;
	}
	
	public void setTotalNumberOfTimesNackCodeSent(long totalNumberOfTimesNackCodeSent)
	{
		this.totalNumberOfTimesNackCodeSent = totalNumberOfTimesNackCodeSent;
	}
	
	public void setAckNackPacket(String ackNackPacket)
	{
		this.ackNackPacket = ackNackPacket;
	}
	
	public void setAckCode(String ackCode)
	{
		this.ackCode = ackCode;
	}
	
	public void setNackCode(String nackCode)
	{
		this.nackCode = nackCode;
	}
	
	public void setCorrectPacket(boolean correctPacket)
	{
		this.correctPacket = correctPacket;
	}
	
	public void setFrameCheckSequence(long frameCheckSequence)
	{
		this.frameCheckSequence = frameCheckSequence;
	}
	
	public void setXOR(long XOR)
	{
		this.XOR = XOR;
	}
	
//Getters
	public int getMinutes()
	{
		return this.minutes;
	}
	
	public long getStartingMoment()
	{
		return this.startingMoment;
	}
	
	public long getEndingMoment()
	{
		return this.endingMoment;
	}
	
	public long getSendingMoment()
	{
		return this.sendingMoment;
	}
	
	public long getReceivingMoment()
	{
		return this.receivingMoment;
	}
	
	public long getResponseMoment()
	{
		return this.responseMoment;
	}
	
	public long getTotalNumberOfPacketsReceived()
	{
		return this.totalNumberOfPacketsReceived;
	}
	
	public long getTotalNumberOfTimesNackCodeSent()
	{
		return this.totalNumberOfTimesNackCodeSent;
	}
	
	public String getAckNackPacket()
	{
		return this.ackNackPacket;
	}
	
	public String getAckCode()
	{
		return this.ackCode;
	}
	
	public String getNackCode()
	{
		return this.nackCode;
	}
	
	public boolean getCorrectPacket()
	{
		return this.correctPacket;
	}
	
	public long getFrameCheckSequence()
	{
		return this.frameCheckSequence;
	}
	
	public long getXOR()
	{
		return this.XOR;
	}
	
	
//Constructor
	/* Will be called to demonstrate the AckNackCommunication
	 * 
	 * Within its body i create a file, 
	 * call all the functions that need to be called
	 * and finally close the file
	 * 
	 * I also print some info messages
	 */
	ackNackCommunication(Modem modem, String ack, String nack) throws FileNotFoundException
	{
		System.out.println();
		System.out.println("START OF ACK_NACK_COMMUNICATION");
		System.out.println();
		
		//Open a file to store some data
		ackNackPacketsResponse = new PrintStream(new FileOutputStream("savedAckNackPacketsResponseTime.txt"));
		
		this.sendAckNackPacket(modem, ack, nack);
		
		//Close the file i opened
		ackNackPacketsResponse.close();
		
		System.out.println();
		System.out.println("END OF ACK_NACK_COMMUNICATION");
		System.out.println();
	}
	
	
	
//Methods	

	/*
	 * This method is used to send an ackNackPacket (through the communication line)
	 */
	public void sendAckNackPacket(Modem modem, String ack, String nack)
	{
		int read;
		this.setAckCode(ack);
		this.setNackCode(nack);
		this.setMinutes(5);
		System.out.println("You have to wait for " +this.getMinutes()+ " minutes");
		
		String helpingAckNackPacket; //helping variable (helps within the "try-catch" block of code)
		
		this.setCorrectPacket(true);//Just to allow the algorithm start the procedure
		this.setTotalNumberOfTimesNackCodeSent(0); //How many times we sent an nack code for each packet
		this.setStartingMoment(System.currentTimeMillis());
		this.setSendingMoment(0);
		helpingAckNackPacket = "" ;
		this.setAckNackPacket(helpingAckNackPacket); //Initialize (set default) the AckNackPacket
		this.setTotalNumberOfPacketsReceived(0); //Set the total (including incorrect) number of packets we received
		
		for(;;)
		{
			this.setAckNackPacket(""); //Reset it
			helpingAckNackPacket = ""; //Reset it
			if(this.getCorrectPacket() == true) //We received a correct packet -> send ack code
			{
				//this.setTotalNumberOfTimesNackCodeSent(0);
				this.setSendingMoment(System.currentTimeMillis()); //Set the sendignMoment of the Ack code
				modem.write(this.getAckCode().getBytes());
			}
			else //We received a wrong packet -> send nack code
			{
				this.setTotalNumberOfTimesNackCodeSent(this.getTotalNumberOfTimesNackCodeSent()+1); //Increase the number representing the times we've sent a nack code  
				modem.write(this.getNackCode().getBytes());
			}
			
			//Read the packet
			for(;;)
			{
					read = modem.read();
					
					if(read != -1) //More stuff to read
					{
						helpingAckNackPacket +=(char)read ; //cannot type cast within the following setMethod so i use this helping variable
						this.setAckNackPacket(helpingAckNackPacket);
					}
					else //Packet's reading procedure is over
						break;
			}
			
			//Call the function to check the packet
			this.checkAckNackPacket();
			this.setTotalNumberOfPacketsReceived(this.getTotalNumberOfPacketsReceived()+1);//Increase the number of total received packets
			
			//Get ready for the next packet
		    this.setAckNackPacket("ReadyForTheNextPacket");
		    this.setEndingMoment(System.currentTimeMillis());
		    
		    //Check for timeout
		    if(this.checkTimeOut())
		    	break;
		}
	}
	
	//Checks if the ackNackPacket is ok (if not, print an error message - just a print statement)
	public void checkAckNackPacket()
	{
		System.out.println(this.getAckNackPacket().length());
		
		//At first, check the length (must be equal to 58)
		if(this.getAckNackPacket().length() == 58)
		{
			if((this.getAckNackPacket().substring(0,6).equals("PSTART"))&&(this.getAckNackPacket().substring(53,58).equals("PSTOP")))
			{
				//System.out.println("YES");
				//Calculate the XOR of the bytes sequence
				byte[] encryptedSequence = new byte[16]; //Allocate memory for 16bytes
				encryptedSequence = this.getAckNackPacket().substring(31,47).getBytes(); //get the<XXXXX> sequence
				
				this.setXOR(encryptedSequence[0]^encryptedSequence[1]);
				for(int i = 2 ; i < encryptedSequence.length; i++)
				{
					this.setXOR(((byte)this.getXOR())^(encryptedSequence[i]));
				}
				//Set the frameCheckSequence
				this.setFrameCheckSequence(Integer.parseInt(this.getAckNackPacket().substring(49, 52)));
				
				/*Compare the encrypted sequence with the FCS number
				 * 
				 * If same, send ackCode
				 * 
				 * else send nackCode
				 */
				if(this.getXOR() == this.getFrameCheckSequence()) //If packet is correct
				{
					System.out.println("YES1");
					this.setReceivingMoment(System.currentTimeMillis()); //Set the receivingMoment
					this.setCorrectPacket(true);
					System.out.println(this.getAckNackPacket());
					this.setResponseMoment(this.getReceivingMoment()-this.getSendingMoment());
					System.out.println(Long.toString(this.getResponseMoment()));
					System.out.println(String.valueOf(this.getTotalNumberOfTimesNackCodeSent()));
					ackNackPacketsResponse.println(Long.toString(this.getResponseMoment()));
				}
				else //Packet is not correct
				{
					//this.setSendingMoment(System.currentTimeMillis());
					this.setCorrectPacket(false);
				}	
			}
			else //packet's substrings are not correct...send nack code
			{
				this.setCorrectPacket(false);
			}
		}
		else //packet's length is not correct..send nack code
		{
			this.setCorrectPacket(false);
		}
	}
	
	
	/*
	 * Checks if we've timed out
	 * Returns true if we've timed out else false
	 */
	public boolean checkTimeOut() //-> NEED "BREAK" 
	{
		if(this.getEndingMoment() - this.getStartingMoment() >= 60000*this.getMinutes())
		{
			System.out.println();
			System.out.println("TIMEOUT");
			System.out.println();
			return true;
		}
		else 
			return false;
	}
}
