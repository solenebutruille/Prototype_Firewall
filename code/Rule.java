package Main;

public class Rule {
	// Attributes of rule
	private int nb;
	private String srcIpAddr;
	private String destIpAddr;
	private int sourcePortMin;
	private int sourcePortMax;
	private int destPortMin;
	private int destPortMax;
	private String protocol;
	private String data; 
	private boolean valid = true;
	
	
	public Rule(String content) {
		// Gets informations from text
		String[] infos = content.split("\n"); 
		String sb = (String) infos[1].subSequence(5, infos[1].length()); 
		nb = Integer.parseInt(sb); 
		if (nb <= 0) valid = false;  
		srcIpAddr = (String) infos[2].subSequence(13, infos[2].length());
		destIpAddr = (String) infos[3].subSequence(14, infos[3].length());
		isIpValid(srcIpAddr);
		isIpValid(destIpAddr);
		sb = (String) infos[4].subSequence(10, infos[4].length());
		String[] split = sb.split("-");
		sourcePortMin = Integer.parseInt(split[0]);
		sourcePortMax = Integer.parseInt(split[1]);
		if(sourcePortMin < 0 || sourcePortMin >  65535 || sourcePortMax < 0 || sourcePortMax > 65535 || sourcePortMax < sourcePortMin) {
			valid = false; 
		}
		if((sourcePortMin == 0 && sourcePortMax != 0) || (sourcePortMax == 0 && sourcePortMin != 0) ) {
			valid = false; 
		}
		sb = (String) infos[5].subSequence(11, infos[5].length());
		split = sb.split("-");
		destPortMin = Integer.parseInt(split[0]);
		destPortMax = Integer.parseInt(split[1]);
		if(destPortMin < 0 || destPortMin >  65535 || destPortMax < 0 || destPortMax > 65535 || destPortMax < destPortMin) {
			valid = false;
		}
		if((destPortMin == 0 && destPortMax != 0) || (destPortMax == 0 && destPortMin != 0) ) { 
			valid = false;
		}
		protocol = (String) infos[6].subSequence(10, infos[6].length());
		data = (String) infos[7].subSequence(6, infos[7].length());  
	}
	
	//Check if the ip is valid
	public void isIpValid(String ip) {
		String[] partsMyIP = ip.split("\\.");
		int digit1 = Integer.parseInt(partsMyIP[0]);
		int digit2 = Integer.parseInt(partsMyIP[1]);
		int digit3 = Integer.parseInt(partsMyIP[2]);
		partsMyIP = partsMyIP[3].split("/");
		int digit4 = Integer.parseInt(partsMyIP[0]);
		int sous_reseau = Integer.parseInt(partsMyIP[1]);
		if (digit1 < 0 || digit1 > 255 || digit2 < 0 || digit2 > 255 || digit3 < 0 || digit3 > 255 || digit4 < 0 || digit4 > 255) {
			valid = false; 
		}
		if((sous_reseau < 8 || sous_reseau > 32) && sous_reseau != 0) { 
			valid = false;
		}
	}

	//Check if the packet matches the rule
	public boolean matches(Packet p) { 
		if(!acceptIp(srcIpAddr, p.getSrcIpAddr()) || !acceptIp(destIpAddr, p.getDestIpAddr())) {
			return false;
		}
		if(!acceptPort(sourcePortMin, sourcePortMax, p.getSourcePort()) || !acceptPort(destPortMin, destPortMax, p.getDestPort())){
			return false;
		}
		if(!p.getProtocol().contains(protocol)) return false; 
		if(!p.getData().contains(data) && !data.equals("*")) return false; 
		return true;
	}
	
	//Check if the ip of packet corresponds to the ip of th rule
	private boolean acceptIp(String ipMine, String ip) {
		if (ipMine.equals("0.0.0.0/0")) {
			return true;
		} 
		//en faite c'est pas ça, il faut prendre le masque /x, le convertir en 255.x1.x2.x3
		//Après on calucle d = 255 - x1
		//Après on fait sur notre adresse a.b.c.d --> a.b+d.255.255
		//Et du coup toutes les adresses bonnes sont entre a.b.c.d et a.b+d.255.255
		String[] partsMyIP = ipMine.split("\\.");
		int digit1 = Integer.parseInt(partsMyIP[0]);
		int digit2 = Integer.parseInt(partsMyIP[1]);
		int digit3 = Integer.parseInt(partsMyIP[2]);
		partsMyIP = partsMyIP[3].split("/");
		int digit4 = Integer.parseInt(partsMyIP[0]);
		int sous_reseau = Integer.parseInt(partsMyIP[1]); 
		float nbBit = 32 - sous_reseau;
		int infoPositionMasque = -1;
		int infoMasque = 0;
		int maxdigit = -1; 
		if(sous_reseau < 16) infoPositionMasque = 2; 
		else if(sous_reseau < 24) infoPositionMasque = 3; 
		else infoPositionMasque = 4; 
		
		float bit_avai = (5 - infoPositionMasque)*8;
		bit_avai = bit_avai - nbBit;
		for (int i = 7; bit_avai > 0; i--) { 
			infoMasque += Math.pow(2, i); 
			bit_avai --; 
		}  
		if(sous_reseau < 16) maxdigit = digit2 + 255 - infoMasque;
		else if(sous_reseau < 24) maxdigit = digit3 + 255 - infoMasque;
		else maxdigit = digit4 + 255 - infoMasque;
		
		
		String[] partsIP = ip.split("\\."); 
		if (digit1 != Integer.parseInt(partsIP[0])) return false;
		if (infoPositionMasque == 2 && (Integer.parseInt(partsIP[1]) < digit2 || Integer.parseInt(partsIP[1]) > maxdigit)) return false;
		if (infoPositionMasque == 3 && digit2 == Integer.parseInt(partsIP[1]) && (Integer.parseInt(partsIP[2]) < digit3 || Integer.parseInt(partsIP[2]) > maxdigit)) return false;
		if (infoPositionMasque == 4 && digit2 == Integer.parseInt(partsIP[1]) && digit3 == Integer.parseInt(partsIP[2]) && (Integer.parseInt(partsIP[3]) < digit4 || Integer.parseInt(partsIP[3]) > maxdigit)) return false;
		return true;
	}
	
	//Check if the port of the packet is corresponding to the ports of the rule 
	private boolean acceptPort(int myMinPort, int myMaxPort, int portTested) {  
		if(myMinPort == 0 && myMaxPort == 0) return true;
		else if(myMinPort <= portTested && portTested <= myMaxPort) return true;
		else return false;
	}
	
	//Getters
	public int getNb() { return nb;} 
	public boolean isValid() {return valid;}

}
