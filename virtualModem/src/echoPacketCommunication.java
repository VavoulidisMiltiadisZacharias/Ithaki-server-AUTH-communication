import java.io.*;
import ithakimodem.*;

public class echoPacketCommunication 
{
	
//Variables declaration/initialization
	private long startingMoment = 0; //Start of procedure
	private long endingMoment = 0; //End of procedure
	private long sendingMoment = 0; //Moment we sent a packet to the server
	private long receivingMoment = 0; //Moment we receive a packet from server
	private long responseTime = 0 ; //Duration between send and receive (receiveTime - sentingTime)
	private String echoPacket;
	private int minutes;
	
	/* 
	 * savedEchoPacketsResponseTime.txt: Within this ".txt" document (file) i save the responseTime 
	 */
	PrintStream responseTimeFile;  //Global in order to close them
	
//Setters
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
	
	public void setResponseTime(long responseTime)
	{
		this.responseTime = responseTime;
	}
	
	public void setEchoPacket(String echoPacket)
	{
		this.echoPacket = echoPacket;
	}
	
	public void setMinutes(int minutes)
	{
		this.minutes = minutes;
	}
	
//Getters
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
	
	public long getResponseTime()
	{
		return this.responseTime;
	}
	
	public String getEchoPacket()
	{
		return this.echoPacket;
	}
	
	public int getMinutes()
	{
		return this.minutes;
	}
	
	
//Constructor
	/* Will be called to demonstrate the echoPacketComunication
	 * 
	 * Within its body i create a file, 
	 * call all the functions that need to be called
	 * and finally close the file
	 * 
	 * I also print some info messages
	 */
	echoPacketCommunication(Modem modem, String echoPacket) throws IOException
	{
		System.out.println();
		System.out.println("START OF ECHO_PACKET_COMMUNICATION");
		System.out.println();
		
		//Open a file to store the response time of each echocPacket
		responseTimeFile = new PrintStream(new FileOutputStream("savedEchoPacketsResponseTime.txt"));
		
		this.sendEchoPacket(modem, echoPacket);
		
		//Close all the files i opened
		responseTimeFile.close();
		
		System.out.println();
		System.out.println("END OF ECHO_PACKET_COMMUNICATION");
		System.out.println();
		
	}
	
//Methods
	/*
	 * This method is used to send an echo packet (through the communication line)
	 */
	public void sendEchoPacket(Modem modem, String echoPacket) throws IOException
	{
		int read;
		this.setMinutes(5);
		System.out.println("You have to wait for " +this.getMinutes()+ " minutes");
		String helpingEchoPacket = ""; //helping variable (helps within "if(read!=1)"  )
		
		//this.setEchoPacket("NoEchoPacketYet"); //Initialize the echoPacket
		this.setStartingMoment(System.currentTimeMillis()); //Set the StartingMoment
		
		//Read the packet
		for(;;)
		{
			helpingEchoPacket=""; //Reset it
			
			//Need to be set in order to get the bytes in line 156(modem.write(etc...))
			this.setEchoPacket(echoPacket); //Set the echoPacket in order to use it within the following methods(which i call with no arguments) 
			
			this.setSendingMoment(System.currentTimeMillis()); //set the sendingMoment
			modem.write(this.getEchoPacket().getBytes());
			
			for(;;)
			{
				read = modem.read();
					
				//More bytes on their way to be read
				if(read != -1)
				{
					helpingEchoPacket+=(char)read; //Add one more character to the packet -> (cannot type cast within the following setMethod so i use this helping variable)
					this.setEchoPacket(helpingEchoPacket);
					//System.out.println(this.getEchoPacket());
				}
				
				//End of packet
				else
				{
					this.setReceivingMoment(System.currentTimeMillis()); //Set the receivingMoment
					break;
				}
			}
			this.checkEchoPacket(this.getEchoPacket());
			if(this.checkTimeOut())
				break;
		}
	}
	
	//Checks if the given packet is ok (if not, print an error message - just a print statement)
	public void checkEchoPacket(String echoPacket) throws IOException
	{	 
		this.setEchoPacket(echoPacket); //Set it in order to use the get method
		
		/* 35 is the length (total number of characters) of a correct echo packet
		 * 
		 * if length is not 35 print an error message
		 * else (if length is 35 indeed) check if the string contains "PSTART' and "PSTOP"
		 * and if "true" save the echoPacket else print error message
		 */
		if(this.getEchoPacket().length() != 35) //False packet
		{
			System.out.println("Checking procedure has spotted a false packet due to length!!!");
		}
		else
		{
			if((this.getEchoPacket().substring(0, 6).equals("PSTART")) && (this.getEchoPacket().substring(30, 35).equals("PSTOP")))
			{
				//Need to subtract 2000 (modem timeOut)
				this.setResponseTime((this.getReceivingMoment() - this.getSendingMoment())-2000); //Setting the responseTime
				
				responseTimeFile.println(Long.toString(this.getResponseTime()));
	            System.out.println("Echo packet received: "+this.getEchoPacket()+ " responseTime: " +this.getResponseTime());
			}
			else
			{
				System.out.println("Checking procedure of echoPackets has spotted a false packet due to delimiteres!!!");
			}
		}
		
		//Prepare for the next echoPacket(No need...just for debug)
		this.setEchoPacket("Ready for the next packet"); //Status = ready
		this.setEndingMoment(System.currentTimeMillis()); //Set the endingMoment
	}
	
	/*
	 * Checks if we timed out
	 * Returns true if we timed out else false
	 */
	public boolean checkTimeOut() //-> NEED "BREAK" 
	{
		if(this.getEndingMoment() - this.getStartingMoment() >= 60000*this.getMinutes())
		{
			System.out.println();
			System.out.println("TIMEOUT");
			return true;
		}
		else 
			return false;
	}
}
