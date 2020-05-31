My program can be launch from the Main class :
	java main rulefile.txt pktfile.txt
  
This program is a kind of small firewall.
rulefile.txt must be a list of rules for acceptance of packet with informations : 
	
	1) A Rule number (integer in the range {1, 2, ..., ∞}). It is assumed that each rule number will be in
	strictly increasing order and that all rule numbers are unique.
	
	2) A src ip address : <a.b.c.d/w>. The special value 0.0.0.0/0 indicates that any input IP address in the corresponding field (source or
	destination) will match this field of the rule. We assume that the prefix length (w) is between 8 and 32. 
	
	3) A dest ip address on the same principle

	4) 2 integer given the range for source port. We assume each port should be in the range of 1 to 65535. The special value 0-0 implies that any port number is matching this field of the rule.

	5) A destination port on the same idea.
	
	6) A protocol : tcp | udp | icmp
	
	7) A string of length 10 bytes representing the data
	
	Example:
	BEGIN
	NUM: 123
	SRC IP ADDR: 121.11.240.0/24
	DEST IP ADDR: 0.0.0.0/0
	SRC PORT: 0-0
	DEST PORT: 21-22
	PROTOCOL: tcp
	DATA: FTPServer
	END

And pktfile.tkt mustcontain the packet with informations :
	
	1) A Rule number (integer in the range {1, 2, ..., ∞}). It is assumed that each rule number will be in
	strictly increasing order and that all rule numbers are unique.
	
	2) A src ip address : <a.b.c.d>. 
	
	3) A dest ip address on the same principle

	4) A source port

	5) A destination port.
	
	6) A protocol : tcp or udp or icmp
	
	7) A string of length 100 bytes representing the data
	
	Example:
	BEGIN
	NUM: 346
	SRC IP ADDR: 121.11.240.15
	DEST IP ADDR: 195.20.34.4
	SRC PORT: 45678
	DEST PORT: 21
	PROTOCOL: tcp
	DATA: Hello to FTPServer
	END
