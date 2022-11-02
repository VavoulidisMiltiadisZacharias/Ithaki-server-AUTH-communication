import java.io.*;
import ithakimodem.*;

public class virtualModem 
{
		 public static void main(String[] param) throws IOException 
		 {	 
			 (new virtualModem()).demo();
		 }
		 
		 public void demo() throws IOException
		 {
			 //Session1 codes
			 
			 //String echoPacket = "E0131\r";
			 //String imageWithoutErrors = "M0776\r";
			 //String imageWithErrors = "G7157\r" ;
			 //String gps = "P4183\r";
			 //String ackResult = "Q6896\r";
			 //String nackResult = "R8299\r";
			 
			 
			 
			 //Session2 codes
			 
			 //String echoPacket = "E6997\r";
			 //String imageWithoutErrors = "M6341\r";
			 //String imageWithErrors = "G7133\r" ;
			 //String gps = "P4250\r";
			 //String ackResult = "Q8989\r";
			 //String nackResult = "R7362\r";
			 
			 
			 //Declare one object for each one of my classes
			 echoPacketCommunication a;
			 imageCommunication b;
			 gpsCommunication e;
			 errorImageCommunication c;
			 ackNackCommunication d;
			 SteliosGps t;
			 
			 int k;
			 Modem modem;
			 modem=new Modem();
			 modem.setSpeed(80000);
			 modem.setTimeout(2000);
			 modem.open("ithaki");
			 for (;;)
			 { 
				 try 
				 {
					 k=modem.read();
					 if (k==-1)
						 break;
					 System.out.print((char)k);
				 } 
				 catch (Exception x)
				 {
					 break;
				 }
			 }
			 // NOTE : Break endless loop by catching sequence "\r\n\n\n".
			 // NOTE : Stop program execution when "NO CARRIER" is detected.
			 // NOTE : A time-out option will enhance program behavior.
			 // NOTE : Continue with further Java code.
			 // NOTE : Enjoy :)
			 
			 
			 //Execute one of the following at a time (Remove "//" on the left hand side to run corresponding line)
			 //a = new echoPacketCommunication(modem, echoPacket);
			 //b = new imageCommunication(modem, imageWithoutErrors);
			 //e = new gpsCommunication(modem, gps);
			 //c = new errorImageCommunication(modem, imageWithErrors);
			// d = new ackNackCommunication(modem, ackResult, nackResult);
			 modem.close();
		 	}
	}
