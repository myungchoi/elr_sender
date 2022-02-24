package edu.gatech.i3l.hl7.v2.elr_sender;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.datatype.XPN;
import ca.uhn.hl7v2.model.v251.message.ORU_R01;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Hl7InputStreamMessageIterator;

/**
 * Hello world!
 *
 */
public class ELRSender 
{
    public static void main( String[] args ) throws Exception
    {
		HapiContext context = new DefaultHapiContext();
    	boolean useTls = false;

   		int port = 8087;
		String envPort = System.getenv("PORT");
		if (envPort != null && !envPort.isEmpty()) {
			port = Integer.valueOf(envPort);
		}

		String envHost = System.getenv("ELR_RECEIVER_HOST");

		String host = "localhost";
		if (envHost != null && !envHost.isEmpty()) {
			host = envHost;
		}

		String filename = "hl7v2msg_fhirpatient.txt";
		String envFilename = System.getenv("V2_FILE_TO_SEND");
		if (envFilename != null && !envFilename.isEmpty()) {
			filename = envFilename;
		}

		Connection connection = context.newClient(host, port, useTls);
		File file = new File(filename);

//		File file = new File("hl7v2msg23.txt");
		// int port = 8087;
//		int port = 8888;
//		Connection connection = context.newClient("apps.hdap.gatech.edu/elrreceiver", port, useTls);
		// Connection connection = context.newClient("musctest.hdap.gatech.edu", port, useTls);
//		File file = new File("hl7v2msg_fhirmusc.txt");
		// File file = new File("hl7v2msg251.txt");
    	
//		Connection connection = context.newClient("ec2-54-91-0-90.compute-1.amazonaws.com", port, useTls);
//		Connection connection = context.newClient("cdcsti.hdap.gatech.edu", port, useTls);
		Initiator initiator = connection.getInitiator();
		Parser p = context.getPipeParser();
    	
//		File file = new File("hl7v2msg.txt");
		InputStream is = new FileInputStream(file);
		is = new BufferedInputStream(is);
		Hl7InputStreamMessageIterator iter = new Hl7InputStreamMessageIterator(is);
		
		while (iter.hasNext()) {
			Message msg = iter.next();
			Message response = initiator.sendAndReceive(msg);
			
			String responseString = p.encode(response);
			System.out.println("Received response:\n" + responseString);
			System.out.println("End of response message");
//				ORU_R01 oruMsg = (ORU_R01) next;
//				MSH msh = oruMsg.getMSH();
//				
//				String msgType = msh.getMessageType().getComponents()[0].toString();
//				String msgTrigger = msh.getMessageType().getTriggerEvent().getValue();
//				
//				System.out.println(msgType + " " + msgTrigger);
//				
//				XPN[] patientName = null;
//				if (oruMsg.getPATIENT_RESULT() != null) {
//					patientName = oruMsg.getPATIENT_RESULT().getPATIENT().getPID().getPatientName();
//				}
//				
//				String familyName = "None";
//				if (patientName != null) 
//					familyName = patientName[0].getFamilyName().getComponents()[0].toString();
//				
//				System.out.println(familyName);
		}
	}
}
