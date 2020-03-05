package com.company;


public class XBoardProtocol
{
	int k = 0; //MARKED FOR DELETION, HARDCODE PENTRU usermove
	void printOptiuniInitiale()
	{
		System.out.println("feature ping=0 usermove=1 time=0 myname=\"mnee pula\" sigterm=0 sigint=0");
	}
	int parseInput(String buffer)
	{
		if(Constante.getInstace().DEBUG)
		{
			System.out.println("# " + buffer);
		}
  
		if (buffer.contains("xboard")) { return 0; }

		if (buffer.contains("protover 2")) { return 0; }

		if (buffer.contains("new")) { return 0; }

		if (buffer.contains("random")) { return 0; }

		if (buffer.contains("level")) { return 0; }

		if (buffer.contains("post")) { return 0; }

		if (buffer.contains("hard")) { return 0; }

		if (buffer.contains("usermove")) {
			if (k == 0) {
				System.out.println("move c7c6");
			} else if (k == 1) {
				System.out.println("move b8a6");
			} else if (k == 2) {
				System.out.println("move d8c7");
			} else {
				System.out.println("resign");
			}
			return 0;
		}
  
		if ("quit".equals(buffer)) {
			System.exit(1);
		}
		System.out.println("#!!!!!!!!!!!!!!!!!!!!! COMANDA INVALIDA SAU NETRATATA");
		return -1;
	}
}
