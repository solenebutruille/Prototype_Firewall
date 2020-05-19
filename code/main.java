package Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class main {

	public static void main(String[] args) {
		// list with all the valid rules
		LinkedList<Rule> lr = new LinkedList<Rule>(); 
	    try {
	    	//Open rule file
	    	BufferedReader reader = new BufferedReader(new FileReader(args[0]));
	    	String line = "";
	    	String content = "";
	    	int compt = 0;
	    	int notValid = 0; 
		    while ((line = reader.readLine()) != null) {
		    	if (line.equals("BEGIN")) compt = 1;
		    	if (line.equals("END")) {
		    		compt = 0;
		    		//Create rule with the text
		    		Rule r = new Rule(content); 
		    		//Check if rule is valid
		    		if(r.isValid()) lr.add(r); 
		    		else {
		    			System.out.println(r.getNb());
		    			notValid +=1;
		    		}
		    		content = "";
		    	}
		    	if (compt != 0) content += line +"\n"; 
		    }
		    reader.close();
	    	System.out.println("A total of " +(lr.size() + notValid)+" rules were read; "+(lr.size()) +" valid rules are stored.\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    try {
	    	//Open file with all the packet
	    	BufferedReader reader = new BufferedReader(new FileReader(args[1]));
	    	String line = "";
	    	String content = "";
	    	Packet p;
	    	int compt = 0; 
	    	int packet_nb = 0;
	    	float mean_time = 0;
	    	LinkedList<Integer> rulesAccepted = new LinkedList<Integer>();
		    while ((line = reader.readLine()) != null) {
		    	if (line.equals("BEGIN")) compt = 1;
		    	if (line.equals("END")) {
		    		compt = 0;
		    		//Create packet
		    		p = new Packet(content);
		    		packet_nb += 1;
		    		//Check if packet is valid
		    		if (!p.isValid()) {
		    			System.out.println("Packet number "+p.getNb()+" is invalid."); 
		    		} else {
			    		rulesAccepted.clear();
			    		//Test every rule to find matching ones
			    		long time = System.currentTimeMillis();
			 	    	for (Rule r :lr) {
			 	    		if(r.matches(p)) {
			 	    			// If rule accepted, add it to the list of accepted rules
			 	    			rulesAccepted.add(r.getNb());
			 	    		}
			 	    	}
			 	    	mean_time = ((packet_nb-1)*(mean_time) + (System.currentTimeMillis() - time)) / packet_nb;
			 	    	System.out.println("Packet number " +p.getNb()+" matches "+rulesAccepted.size()+" rule(s): " +rulesAccepted.toString());
		    		}
		    		content = "";
		    	}
		    	if (compt != 0) content += line +"\n"; 
		    }
		    System.out.println("A total of "+packet_nb+" packet(s) were read from the file and processed. Bye.\r\n" + 
		    		"Average time taken per packet: "+mean_time*1000+" microseconds.");
		    reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	} 
}
