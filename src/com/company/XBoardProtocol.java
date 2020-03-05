package com.company;

public class XBoardProtocol {
	int k = 0; //MARKED FOR DELETION, HARDCODE PENTRU usermove
	void printOptiuniInitiale() {
		System.out.println("feature ping=0 usermove=1 time=0 myname=\"mnee pula\" sigterm=0 sigint=0");
	}
	int parseInput(String buffer) {
		DatabaseComenziSiConstante longAsShitVariable = DatabaseComenziSiConstante.getInstance();

		if(longAsShitVariable.DEBUG) {
			System.out.println("# " + buffer);
		}
  
		if (buffer.contains("xboard")) { return 0; }

		if (buffer.contains("protover")) {
			new XBoardProtocol().printOptiuniInitiale();
			return 0;
		}

		if (buffer.contains("white")) {
			longAsShitVariable.turn = longAsShitVariable.WHITE;
            longAsShitVariable.engineColor = longAsShitVariable.BLACK;
            longAsShitVariable.opponentColor = longAsShitVariable.WHITE;
			return 0;
		}

		if (buffer.contains("black")) {
			longAsShitVariable.turn = longAsShitVariable.BLACK;
            longAsShitVariable.engineColor = longAsShitVariable.WHITE;
            longAsShitVariable.opponentColor = longAsShitVariable.BLACK;
			return 0;
		}

		if (buffer.contains("new")) {
			longAsShitVariable.engineColor = longAsShitVariable.BLACK;
			// TODO resetTable();
			return 0;
		}

		if (buffer.contains("accepted")) { return 0;}

		if (buffer.contains("random")) { return 0; }

		if (buffer.contains("go")) {
			longAsShitVariable.engineColor = longAsShitVariable.turn;
			longAsShitVariable.opponentColor = !longAsShitVariable.engineColor;
			return 0;
		}

		// trebuie vazut unde il mai folosim, da atm e doar
		// un fel de flag
		if (buffer.equals("force")) {
			longAsShitVariable.forceMode = true;
			return 0;
		}

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
			k++;
			return 0;
		}
  
		if ("quit".equals(buffer)) {
			System.exit(1);
		}
		System.out.println("#!!!!!!!!!!!!!!!!!!!!!" + buffer);

		System.out.println("#!!!!!!!!!!!!!!!!!!!!! COMANDA INVALIDA SAU NETRATATA");
		return -1;
	}
}
