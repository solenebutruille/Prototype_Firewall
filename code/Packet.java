package Main;

public class Packet {
	// Attributes of packet
	private int nb;
	private String srcIpAddr;
	private String destIpAddr;
	private int sourcePort;
	private int destPort;
	private String protocol;
	private String data; 
	private boolean valid = true;
	
	public Packet(String content) {
		// Getting all informations from the text.  
		String[] infos = content.split("\n"); 
		String sb = (String) infos[1].subSequence(5, infos[1].length()); 
		nb = Integer.parseInt(sb); 
		//Checking that number is >= 0
		if (nb <= 0) valid = false;  
		srcIpAddr = (String) infos[2].subSequence(13, infos[2].length());
		destIpAddr = (String) infos[3].subSequence(14, infos[3].length());
		//Checking that ip addresses are valid
		isIpValid(srcIpAddr);
		isIpValid(destIpAddr);
		sb = (String) infos[4].subSequence(10, infos[4].length()); 
		sourcePort = Integer.parseInt(sb); 
		sb = (String) infos[5].subSequence(11, infos[5].length()); 
		destPort = Integer.parseInt(sb); 
		//Checking that port are between 0 and 65535
		if(sourcePort <= 0 || sourcePort >  65535 || destPort <= 0 || destPort > 65535 ) {
			 valid = false; 
		}
		protocol = (String) infos[6].subSequence(10, infos[6].length());
		data = (String) infos[7].subSequence(6, infos[7].length()); 
	}
	
	//Check if the component of the ip address are between 0 and 255
	public void isIpValid(String ip) {
		String[] partsMyIP = ip.split("\\.");
		int digit1 = Integer.parseInt(partsMyIP[0]);
		int digit2 = Integer.parseInt(partsMyIP[1]);
		int digit3 = Integer.parseInt(partsMyIP[2]); 
		int digit4 = Integer.parseInt(partsMyIP[3]); 
		if (digit1 < 0 || digit1 > 255 || digit2 < 0 || digit2 > 255 || digit3 < 0 || digit3 > 255 || digit4 < 0 || digit4 > 255) {
			valid = false;  
		}
	}
	
	//Getters
	public int getNb() {return nb;} 
	public int getSourcePort() {return sourcePort;} 
	public int getDestPort() {return destPort;} 
	public boolean isValid() {return valid;}
	public String getSrcIpAddr() {return srcIpAddr;} 
	public String getDestIpAddr() {return destIpAddr;}  
	public String getProtocol() {return protocol;} 
	public String getData() {return data;} 
}
