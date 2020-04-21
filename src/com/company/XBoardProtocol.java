package com.company;

import com.company.Board.*;
import com.company.MoveAndSearch.*;

/**
 * Clasa asta tine chestii legate de comunicarea cu xboardu. Practic e un adapter pt engine.
 * Metode care ar trebui sa fie aici: command parser, move parser, move printer etc.
 */

public class XBoardProtocol {
	private static final int NEXT_INSTRUCTION = 0;
	private static final int ERROR = -1;
	private static final String numeEngine = "Garbigi";
	StringBuilder move = new StringBuilder("c7c6"); // default pt negru

	void printOptiuniInitiale() {
		System.out.println("feature ping=0 usermove=1 time=0 myname=\"" + numeEngine +"\" sigterm=0 sigint=0 reuse=0");
		//System.out.println("feature done=1");
	}

	int parseInput(String buffer) throws CloneNotSupportedException
	{
		Database database = Database.getInstance();

		if (buffer.contains("debug")) {
			/////////
			Bitboard.initMasti();
			database.numarDeMiscariFacute = 0;
			BoardCommands.initGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b - - 0 1");
			//database.engineColor = database.WHITE;
			///database.opponentColor = database.BLACK;
			//database.turn = database.BLACK;
			//BoardCommands.initGame();
			/////////

			//BoardCommands.parseOpponentMove("usermove f2f1r");

			System.out.println("Eval for the fen: " + Eval.eval(BoardState.getInstance(), 0, database.BLACK));
			Printer.print();
			//System.out.println(Eval.eval(BoardState.getInstance(),1));

			//MoveGenerator movegen = new MoveGenerator(BoardState.getInstance());

			//MoveGenerator movegen = new MoveGenerator(BoardState.getInstance());
			//movegen.generateAllMovesAndStats(true);
			//new Perft().timeTest(7,BoardState.getInstance());
			//Negamax.iterativeDebug(BoardState.getInstance(), 5);
			new Negamax(10000000).negamax(3,Integer.MIN_VALUE,Integer.MAX_VALUE,false,BoardState.getInstance()).printMove();
			System.exit(1);
			//DEBUG pentru consola
			//System.out.println(buffer);
			//pune aici functia pe care vrei sa o testezi
			
			return NEXT_INSTRUCTION;
		}

		if (buffer.contains("new")) {
			database.numarDeMiscariFacute = 0;
			BoardCommands.initGame();
			return NEXT_INSTRUCTION;
		}
		if (database.DEBUG) {
			System.out.println("# " + buffer);
		}
		
		if (buffer.contains("xboard")) { return 0; }

		if (buffer.contains("protover")) {
			new XBoardProtocol().printOptiuniInitiale();
			Bitboard.initMasti();
			return NEXT_INSTRUCTION;
		}

		if (buffer.contains("white")) {
			System.out.println("# am primit white, joc pe alb");
			database.turn = database.WHITE;
			database.engineColor = database.WHITE;
			database.opponentColor = database.BLACK;
			move = new StringBuilder("d2d3");
			return NEXT_INSTRUCTION;
		}

		if (buffer.contains("black")) {
			System.out.println("# am primit black, joc pe negru");
			database.turn = database.BLACK;
			database.engineColor = database.BLACK;
			database.opponentColor = database.WHITE;
			move = new StringBuilder("c7c6");
			return NEXT_INSTRUCTION;
		}


		if (buffer.contains("accepted")) { return NEXT_INSTRUCTION;}

		if (buffer.contains("random")) { return NEXT_INSTRUCTION; }

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

		if (buffer.contains("go")) {
			database.forceMode = false;
			database.engineColor = database.turn;

			if (database.engineColor == database.WHITE) {
				move = new StringBuilder("d2d3");
			}

			database.opponentColor = !database.engineColor;
			int sursa = 0;
			int dest = 0;
			BoardCommands.MoveToIndexes verificare = new BoardCommands.MoveToIndexes(move.toString(), sursa, dest).invoke();
			sursa = verificare.getSourceIndex();
			dest = verificare.getDestIndex();
			//makeHardcodedMove(sursa, dest);
			Negamax search = new Negamax(20000);
			System.out.println("#1 plm " + database.engineColor);
			Move move = search.negamax(6, Integer.MIN_VALUE, Integer.MAX_VALUE, database.engineColor, BoardState.getInstance());
			Negamax.applyMove(BoardState.getInstance(), move, database.engineColor);
			System.out.println("move " + move.getMove());
			System.out.println("# scorul mutarii: " + move.getScor() + " si priot: " + move.getPrioritate());
			//System.out.println("move " + move);
		}

		if (buffer.contains("usermove")) {
			int err = BoardCommands.parseOpponentMove(buffer);
			if (err < 0) {
				if (database.DEBUG)
					System.out.println("Illegal move boss");
				return NEXT_INSTRUCTION;
			}
			database.turn = database.engineColor;

			if (database.DEBUG) {
				Printer.print();
			}

			if (!database.forceMode) {
				int sursa = 0;
				int dest = 0;
				BoardCommands.MoveToIndexes verificare = new BoardCommands.MoveToIndexes(move.toString(), sursa, dest).invoke();
				sursa = verificare.getSourceIndex();
				dest = verificare.getDestIndex();

				//makeHardcodedMove(sursa, dest);
				Negamax search = new Negamax(20000);
				System.out.println("#2 plm " + database.engineColor);
				Move move = search.negamax(6, Integer.MIN_VALUE, Integer.MAX_VALUE, database.engineColor, BoardState.getInstance());
				Negamax.applyMove(BoardState.getInstance(), move, database.engineColor);
				System.out.println("move " + move.getMove());
				System.out.println("# scorul mutarii: " + move.getScor() + " si priot: " + move.getPrioritate());
			}

			database.turn = database.opponentColor;

			//if (database.DEBUG) {
				Printer.print();
			//}

			//Printer.print();
			return NEXT_INSTRUCTION;
		}

		if ("quit".equals(buffer)) {
			System.exit(1);
		}

		System.out.println("#!!!!!!!!!!!!!!!!!!!!! COMANDA INVALIDA SAU NETRATATA");
		return NEXT_INSTRUCTION; //aia e
	}

	private void makeHardcodedMove(int sursa, int dest) {
		Database database = Database.getInstance();

		if (database.engineColor == database.WHITE) {
			if (BoardCommands.isPawnMoveLegal(sursa, dest, database.engineColor)) {
				System.out.println("move " + move);
				System.out.println("# I did the move: " + move);
				move.setCharAt(1, (char) (move.charAt(1) + 1));
				move.setCharAt(3, (char) (move.charAt(3) + 1));
				database.numarDeMiscariFacute++;
				System.out.println("# new move: " + move);

				BoardCommands.updateBitboard(sursa, dest, BoardState.getInstance().whitePawns);
			} else {
				System.out.println("# not legal????? resigning");
				System.out.println("resign");
			}
		} else if (database.engineColor == database.BLACK) {
			if (BoardCommands.isPawnMoveLegal(sursa, dest, database.engineColor)) {
				System.out.println("move " + move);
				System.out.println("# I did the move: " + move);
				move.setCharAt(1, (char) (move.charAt(1) - 1));
				move.setCharAt(3, (char) (move.charAt(3) - 1));
				System.out.println("# new move: " + move);
				database.numarDeMiscariFacute++;

				BoardCommands.updateBitboard(sursa, dest, BoardState.getInstance().blackPawns);

			} else {
				System.out.println("# not legal????? resigning");
				System.out.println("resign");
			}
		} else {
			System.out.println("# force mode??");
		}
	}
}
