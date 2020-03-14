package com.company;

import com.company.Board.*;

import java.util.ArrayList;
import java.util.Arrays;

public class XBoardProtocol {
	private static final int NEXT_INSTRUCTION = 0;
	private static final int ERROR = -1;
	private static final String numeEngine = "Neintitulat";

	void printOptiuniInitiale() {
		System.out.println("feature ping=0 usermove=1 time=0 myname=\"" + numeEngine +"\" sigterm=0 sigint=0 reuse=0");
		System.out.println("feature done=1");
	}
	int parseInput(String buffer) {
		DatabaseComenziSiConstante database = DatabaseComenziSiConstante.getInstance();

		if(database.DEBUG) {
			System.out.println("# " + buffer);
		}

		if (buffer.contains("xboard")) { return 0; }

		if (buffer.contains("protover")) {
			new XBoardProtocol().printOptiuniInitiale();
			Bitboard.initMasti();
			return NEXT_INSTRUCTION;
		}

		if (buffer.contains("white")) {
			System.out.println("# JOC PE NEGRU");
			database.turn = database.WHITE;
            database.engineColor = database.WHITE;
            database.opponentColor = database.BLACK;
			return NEXT_INSTRUCTION;
		}

		if (buffer.contains("black")) {
			System.out.println("# JOC PE ALB");
			database.turn = database.BLACK;
            database.engineColor = database.BLACK;
            database.opponentColor = database.WHITE;
			return NEXT_INSTRUCTION;
		}

		if (buffer.contains("debug")) {
			//DEBUG pentru consola
			System.out.println(buffer);
			//pune aici functia pe care vrei sa o testezi
			return NEXT_INSTRUCTION;
		}

		if (buffer.contains("new")) {
			database.numarDeMiscariFacute = 0;
			DatabaseComenziSiConstante.getInstance().initGame();
			return NEXT_INSTRUCTION;
		}

		if (buffer.contains("accepted")) { return NEXT_INSTRUCTION;}

		if (buffer.contains("random")) { return NEXT_INSTRUCTION; }

		/*if (buffer.contains("go")) {
			database.engineColor = database.turn;
			database.opponentColor = !database.engineColor;
			return NEXT_INSTRUCTION;
		}*/

		// trebuie vazut unde il mai folosim, da atm e doar
		// un fel de flag
		if (buffer.contains("force")) {
			database.forceMode = true;
			return NEXT_INSTRUCTION;
		}

		if (buffer.contains("level")) { return NEXT_INSTRUCTION; }

		if (buffer.contains("post")) { return NEXT_INSTRUCTION; }

		if (buffer.contains("hard")) { return NEXT_INSTRUCTION; }

		if (buffer.contains("easy")) { return NEXT_INSTRUCTION; }

		if (buffer.contains("result")) { return NEXT_INSTRUCTION; }
		
		if (buffer.contains("computer")) { return NEXT_INSTRUCTION; }
		
		if (buffer.contains("usermove") || buffer.contains("go")) {
			if(database.forceMode)
			{
				BoardHelpere.parseOpponentMove(buffer);
			}
			else
			{
				if (database.engineColor == database.WHITE)
				{
					if (database.numarDeMiscariFacute == 0) {
						System.out.println("move h2h4");
					} else if (database.numarDeMiscariFacute == 1) {
						System.out.println("move h4h5");
					} else if (database.numarDeMiscariFacute == 2) {
						System.out.println("move h5h6");
					} else {
						System.out.println("resign");
					}
				} else
				{
					if (database.numarDeMiscariFacute == 0) {
						System.out.println("move c7c6");
					} else if (database.numarDeMiscariFacute == 1) {
						System.out.println("move b8a6");
					} else if (database.numarDeMiscariFacute == 2) {
						System.out.println("move d8c7");
					} else {
						System.out.println("resign");
					}
				}
				database.numarDeMiscariFacute++;
			return NEXT_INSTRUCTION;
			}
		}

		if ("quit".equals(buffer)) {
			System.exit(1);
		}

		System.out.println("#!!!!!!!!!!!!!!!!!!!!! COMANDA INVALIDA SAU NETRATATA");
		return NEXT_INSTRUCTION; //aia e
	}
}
