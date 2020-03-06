package com.company;

public class XBoardProtocol {
	int k = 0; //MARKED FOR DELETION, HARDCODE PENTRU usermove
	private static final int NEXT_INSTRUCTION = 0;
	private static final int ERROR = -1;
	
	void printOptiuniInitiale() {
		System.out.println("feature ping=0 usermove=1 time=0 myname=\"mnee pula\" sigterm=0 sigint=0");
	}
	int parseInput(String buffer) {
		DatabaseComenziSiConstante database = DatabaseComenziSiConstante.getInstance();

		if(database.DEBUG) {
			System.out.println("# " + buffer);
		}
  
		if (buffer.contains("xboard")) { return 0; }

		if (buffer.contains("protover")) {
			new XBoardProtocol().printOptiuniInitiale();
			return NEXT_INSTRUCTION;
		}

		if (buffer.contains("white")) {
			database.turn = database.WHITE;
            database.engineColor = database.BLACK;
            database.opponentColor = database.WHITE;
			return NEXT_INSTRUCTION;
		}

		if (buffer.contains("black")) {
			database.turn = database.BLACK;
            database.engineColor = database.WHITE;
            database.opponentColor = database.BLACK;
			return NEXT_INSTRUCTION;
		}

		if (buffer.contains("new")) {
			database.engineColor = database.BLACK;
			// TODO resetTable();
			return NEXT_INSTRUCTION;
		}

		if (buffer.contains("accepted")) { return NEXT_INSTRUCTION;}

		if (buffer.contains("random")) { return NEXT_INSTRUCTION; }

		if (buffer.contains("go")) {
			database.engineColor = database.turn;
			database.opponentColor = !database.engineColor;
			return NEXT_INSTRUCTION;
		}

		// trebuie vazut unde il mai folosim, da atm e doar
		// un fel de flag
		if (buffer.equals("force")) {
			database.forceMode = true;
			return NEXT_INSTRUCTION;
		}

		if (buffer.contains("level")) { return NEXT_INSTRUCTION; }

		if (buffer.contains("post")) { return NEXT_INSTRUCTION; }

		if (buffer.contains("hard")) { return NEXT_INSTRUCTION; }

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
			return NEXT_INSTRUCTION;
		}
  
		if ("quit".equals(buffer)) {
			System.exit(1);
		}

		System.out.println("#!!!!!!!!!!!!!!!!!!!!! COMANDA INVALIDA SAU NETRATATA");
		return ERROR;
	}
}
