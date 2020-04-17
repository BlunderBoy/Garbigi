package com.company.Board;

import com.company.Database;

/**
 * Clasa asta contine comenzi legate de starea board-ului, de exemplu initializarea unui board nou,
 * crearea unui board dintr-un fen, actualizarea board-ului cand primesti/faci o mutare (soon)
 */

public class BoardCommands {
	public static void initGame() {
		Database.initializareArray();
		BoardCommands.createBoardstateFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		Database.getInstance().engineColor = Database.getInstance().BLACK;
		Database.getInstance().opponentColor = Database.getInstance().WHITE;
		Database.getInstance().turn = Database.getInstance().WHITE;
		Database.getInstance().forceMode = false;
	}
	
	public static void initGame(String fen) {
		Database.initializareArray();
		BoardCommands.createBoardstateFromFEN(fen);
		Database.getInstance().engineColor = Database.getInstance().BLACK;
		Database.getInstance().opponentColor = Database.getInstance().WHITE;
		Database.getInstance().turn = Database.getInstance().WHITE;
		Database.getInstance().forceMode = false;
	}

	public static void createBoardstateFromFEN(String fen) {
		BoardState.getInstance().resetBoard();
		char currentChar = 0;
		int index = 63;
		BoardState board = BoardState.getInstance();
		Database database = Database.getInstance();

		String[] tokens = fen.split(" ");
		//tokens 0 = fenul
		//token 1 = cine muta w/b
		//token 2 = castiling permisions
		//token 3 = en passant square
		//token 4 = numar de miscari de la ultima capturare (irelevant mostly)
		//token 5 = numar de miscari in total

		cineMuta(database, tokens);
		populareBiboards(index, board, tokens[0]);
		castlingPermissions(tokens);
		//TODO en passant ???
		database.halfMoves = Integer.parseInt(tokens[4]);
		database.fullMoves = Integer.parseInt(tokens[5]);
	}

	//WHITE = true, BLACK = false
	// verifica daca piesele de pe SIDE ataca square-ul dat
	public static boolean isSquareAttacked(int rank, int file, boolean side) {
		System.out.print("e pozitia " + rank + " " + file + " atacata de ");

		if(side) {
			System.out.print("alb? ");
		} else {
			System.out.print("negru? ");
		}

		rank--;
		file--;
		BoardState board = BoardState.getInstance();
		Database database = Database.getInstance();
		int pozitie = Database.conversieRFla120(rank, file);
		System.out.print("adica numarul " + Database.conversie120la64(pozitie) + " ");
		int directie;
		
		int count = 0;
		
		int pionAtacDreaptaNegru = Database.conversie120la64(pozitie - 11);
		int pionAtacStangaNegru = Database.conversie120la64(pozitie - 9);
		int pionAtacDreaptaAlb = Database.conversie120la64(pozitie + 11);
		int pionAtacStangaAlb = Database.conversie120la64(pozitie + 9);

		if (side == database.WHITE) {
			if (board.WhitePawns.isBitSet(pionAtacDreaptaNegru) ||
					board.WhitePawns.isBitSet(pionAtacStangaNegru)) {
				return true;
			}
		} else {
			if (board.BlackPawns.isBitSet(pionAtacDreaptaAlb) ||
					board.BlackPawns.isBitSet(pionAtacStangaAlb)) {
				return true;
			}
		}
		return false;
	}

	private static void castlingPermissions(String[] tokens) {
		if(tokens[2].contains("K"))
		{
			BoardState.getInstance().castlePermision[0] = 1;
		}
		if(tokens[2].contains("Q"))
		{
			BoardState.getInstance().castlePermision[1] = 1;
		}
		if(tokens[2].contains("k"))
		{
			BoardState.getInstance().castlePermision[2] = 1;
		}
		if(tokens[2].contains("q"))
		{
			BoardState.getInstance().castlePermision[3] = 1;
		}
	}

	private static void populareBiboards(int index, BoardState board, String token) {
		char currentChar;
		for (int i = 0; i < token.length(); i++) {
			currentChar = token.charAt(i);

			switch (currentChar) {
				case 'N':
					board.WhiteKnights.setBit(index);
					index--;
					break;
				case 'R':
					board.WhiteRooks.setBit(index);
					index--;
					break;
				case 'B':
					board.WhiteBishops.setBit(index);
					index--;
					break;
				case 'P':
					board.WhitePawns.setBit(index);
					index--;
					break;
				case 'Q':
					board.WhiteQueens.setBit(index);
					index--;
					break;
				case 'K':
					board.WhiteKing.setBit(index);
					index--;
					break;

				case 'n':
					board.BlackKnights.setBit(index);
					index--;
					break;
				case 'r':
					board.BlackRooks.setBit(index);
					index--;
					break;
				case 'b':
					board.BlackBishops.setBit(index);
					index--;
					break;
				case 'p':
					board.BlackPawns.setBit(index);
					index--;
					break;
				case 'q':
					board.BlackQueens.setBit(index);
					index--;
					break;
				case 'k':
					board.BlackKing.setBit(index);
					index--;
					break;
				case '/':
					break;
				default:
					index -= (currentChar - '0');
					break;
			}
		}
		board.AllWhitePieces.reprezentare = board.WhiteKing.reprezentare |
				               board.WhiteBishops.reprezentare |
							   board.WhiteKnights.reprezentare |
				               board.WhiteQueens.reprezentare |
						       board.WhiteRooks.reprezentare |
							   board.WhitePawns.reprezentare;
		
		board.AllBlackPieces.reprezentare = board.BlackKing.reprezentare |
				               board.BlackBishops.reprezentare |
							   board.BlackKnights.reprezentare |
				               board.BlackQueens.reprezentare |
						       board.BlackRooks.reprezentare |
							   board.BlackPawns.reprezentare;
		
		board.AllPieces.reprezentare = board.AllWhitePieces.reprezentare |
							           board.AllBlackPieces.reprezentare;
		
		for(Bitboard b : board.allBitboards) {
			b.numarPiese = Long.bitCount(b.reprezentare);
		}
	}
	
	private static void cineMuta(Database constante, String[] tokens) {
		if(tokens[1].contains("w")) {
			constante.turn = constante.WHITE;
		} else {
			constante.turn = constante.BLACK;
		}
	}

	// TODO mare todo ce e aici smr
	// TODO maybe ar trebui mutat in XBoardProtocol??
	public static int parseOpponentMove(String command) {
		Database data =  Database.getInstance();
		BoardState board = BoardState.getInstance();

		String[] tokens = command.split(" ");
		String move = tokens[1];

		// TODO castling
		int sourceIndex = 0;
		int destIndex = 0;

		int return_code = 0;

		MoveToIndexes moveToIndexes = new MoveToIndexes(move, sourceIndex, destIndex).invoke();
		sourceIndex = moveToIndexes.getSourceIndex();
		destIndex = moveToIndexes.getDestIndex();

		if (sourceIndex < 0 || sourceIndex > 63 || destIndex < 0 || destIndex > 63) {
			System.out.println("Illegal move, wrong index you dumbass");
			return -69;
		}

		// astea-s comentarii inutile pt mine btw, ignora-le
		// verifica daca piesa de la source este a celui care trb sa mute
		// verifica daca exista spatiu unde sa o mute sau daca captureaza piesa
		// actualizeaza bitboards

		// shelved for the moment
		// aparent e mai "comod" sa verifici de FIECARE data FIECARE piesa...
		// daca avem probleme de viteza... AICI trebuie optimizat
		// + toate chestiile care au... 12 blocuri de cod duplicat
		// poate sa insistam pe ideea mea de common arrays?

		/*int indexToCheck = 0;
		if (Database.getInstance().turn == Database.getInstance().BLACK) {
			indexToCheck = 1;
		}*/

		char pieceType = getPieceType(sourceIndex);

		if (pieceType == 0) {
			System.out.println("nu avem piesa in source");
			return -2;
		}

		switch (pieceType) {
			case 'P':
				if (data.DEBUG)
					System.out.println("White pawn.");
				if (isPawnMoveLegal(sourceIndex, destIndex, data.WHITE)) {
					updateBitboard(sourceIndex, destIndex, board.WhitePawns);
				} else {
					System.out.println("nu e legal");
					return_code = -1;
				}
				break;
			case 'p':
				if (data.DEBUG)
					System.out.println("Black pawn.");
				if (isPawnMoveLegal(sourceIndex, destIndex, data.BLACK)) {
					updateBitboard(sourceIndex, destIndex, board.BlackPawns);
				} else {
					return_code = -1;
				}
				break;
			case 'R':
				if (data.DEBUG)
					System.out.println("White rook.");
				if (isRookMoveLegal(sourceIndex, destIndex, data.WHITE)) {
					updateBitboard(sourceIndex, destIndex, board.WhiteRooks);
				} else {
					return_code = -1;
				}
				break;
			case 'r':
				if (data.DEBUG)
					System.out.println("Black rook.");
				if (isRookMoveLegal(sourceIndex, destIndex, data.BLACK)) {
					updateBitboard(sourceIndex, destIndex, board.BlackRooks);
				} else {
					return_code = -1;
				}
				break;
			case 'N':
				if (data.DEBUG)
					System.out.println("White knight.");
				break;
			case 'n':
				if (data.DEBUG)
					System.out.println("Black knight.");
				break;
			case 'B':
				if (data.DEBUG)
					System.out.println("White bishop.");
				break;
			case 'b':
				if (data.DEBUG)
					System.out.println("Black bishop.");
				break;
			case 'Q':
				if (data.DEBUG)
					System.out.println("White queen.");
				break;
			case 'q':
				if (data.DEBUG)
					System.out.println("Black queen.");
				break;
			case 'K':
				if (data.DEBUG)
					System.out.println("White king.");
				break;
			case 'k':
				if (data.DEBUG)
					System.out.println("Black king.");
				break;
			default:
				if (data.DEBUG)
					System.out.println("you did smth wrong");
		}

		if (Database.getInstance().DEBUG) {
				System.out.println("move: " + command + " source index: " + sourceIndex + ", dest index: " + destIndex);
		}

		return return_code; // illegal move
	}

	// cu capturari
	public static void updateBitboard(int sourceIndex, int destIndex, Bitboard source) {
		// verificam LA INCEPUT daca avem capturare
		Bitboard dest = getBitboardFromType(getPieceType(destIndex));

		if (dest != null) {
			if (Database.getInstance().DEBUG)
				System.out.println("avem capturare");
			dest.clearBit(destIndex);
		} else {
			if (Database.getInstance().DEBUG)
				System.out.println("nu avem capturare");
		}

		source.clearBit(sourceIndex);
		source.setBit(destIndex);

		BoardState.getInstance().updateBitboards();
	}

	private static Bitboard getBitboardFromType (char type) {
		BoardState board = BoardState.getInstance();
		switch (type) {
			case 'P':
				return board.WhitePawns;
			case 'p':
				return board.BlackPawns;
			case 'R':
				return board.WhiteRooks;
			case 'r':
				return board.BlackRooks;
			case 'N':
				return board.WhiteKnights;
			case 'n':
				return board.BlackKnights;
			case 'B':
				return board.WhiteBishops;
			case 'b':
				return board.BlackBishops;
			case 'Q':
				return board.WhiteQueens;
			case 'q':
				return board.BlackQueens;
			case 'K':
				return board.WhiteKing;
			case 'k':
				return board.BlackKing;
			default:
				return null;
		}
	}

	private static char getPieceType (int index) {
		if (BoardState.getInstance().WhitePawns.isBitSet(index)) {
			return 'P';
		}
		if (BoardState.getInstance().BlackPawns.isBitSet(index)) {
			return 'p';
		}
		if (BoardState.getInstance().Rooks[0].isBitSet(index)) {
			return 'R';
		}
		if (BoardState.getInstance().Rooks[1].isBitSet(index)) {
			return 'r';
		}
		if (BoardState.getInstance().Bishops[0].isBitSet(index)) {
			return 'B';
		}
		if (BoardState.getInstance().Bishops[1].isBitSet(index)) {
			return 'b';
		}
		if (BoardState.getInstance().Knights[0].isBitSet(index)) {
			return 'N';
		}
		if (BoardState.getInstance().Knights[1].isBitSet(index)) {
			return 'n';
		}
		if (BoardState.getInstance().Queens[0].isBitSet(index)) {
			return 'Q';
		}
		if (BoardState.getInstance().Queens[1].isBitSet(index)) {
			return 'q';
		}
		if (BoardState.getInstance().Kings[0].isBitSet(index)) {
			return 'K';
		}
		if (BoardState.getInstance().Kings[1].isBitSet(index)) {
			return 'k';
		}
		return 0;
	}

	public static boolean isPawnMoveLegal (int source, int dest, boolean side) {
		Database data = Database.getInstance();
		BoardState board = BoardState.getInstance();

		int indexToCheck = 0;
		int doubleMoveSize = 16;
		int doubleMoveUpperBoundary = 16;
		int doubleMoveLowerBoundary = 7;
		int normalMoveSize = 8;
		int captureMoveL = 9;
		int captureMoveR = 7;
		int modifier = 1; // unele indexe daca sunt side black, doar se fac cu -,
					      // deci asta o sa fie -1 sau +1
		Bitboard enemyPieces = board.AllBlackPieces;
		if (side == data.BLACK) {
			indexToCheck = 1;
			doubleMoveUpperBoundary = 56;
			doubleMoveLowerBoundary = 47;
			modifier = -1;
			enemyPieces = board.AllWhitePieces;
		}
		// verificarea asta e cam redundanta, ca parseOpponentMove deja o verifica
		if (!BoardState.getInstance().Pawns[indexToCheck].isBitSet(source)) {
			System.out.println("# Illegal move, no source");
			return false;
		}

		// daca avem double move
		if (source < doubleMoveUpperBoundary && source > doubleMoveLowerBoundary
				&& dest == (source + modifier * doubleMoveSize)) {
			if (Database.getInstance().DEBUG)
				System.out.println("legal 2 square move");
			// TODO en passant
			return true;
		}
		// daca avem miscare normala in fata
		if (dest == (source + modifier * normalMoveSize)) {
			if (getBitboardFromType(getPieceType(dest)) == null) {
				if (Database.getInstance().DEBUG)
					System.out.println("legal 1 sq move");
				return true;
			} else {
				if (Database.getInstance().DEBUG)
					System.out.println("avem piesa in fata noastra");
			}
		}
		// daca avem capturare
		if ((dest == (source + captureMoveL) || dest == (source + captureMoveR))
				&& enemyPieces.isBitSet(dest)) {
			// miscare legala de capturare
			if (Database.getInstance().DEBUG)
				System.out.println("capturare");
			return true;
		}

		if (Database.getInstance().DEBUG)
			System.out.println("this shouldn't happen yet");

		return false;
	}

	private static boolean isRookMoveLegal (int source, int dest, boolean side) {
		Database data = Database.getInstance();
		BoardState board = BoardState.getInstance();

		// verificare pt sus/jos -> (source % 8) == (dest % 8)
		// source / 8 == dest / 8
		if ((source % 8) != (dest % 8) && (source / 8) != (dest / 8)) {
			return false;
		}

		//int bitboardIndex = 0;
		if (side == data.BLACK) {
			if (board.AllWhitePieces.isBitSet(dest)) {
				if (data.DEBUG)
					System.out.println("avem capturare de la black pt white");
			}
			if (board.AllBlackPieces.isBitSet(dest)) {
				if (data.DEBUG) {
					System.out.println("ce-ai boss, iti iei singur piesa?");
				}
				return false;
			}
			//bitboardIndex = 1;
		} else {
			if (board.AllBlackPieces.isBitSet(dest)) {
				if (data.DEBUG)
					System.out.println("avem capturare de la white pt black");
			}
			if (board.AllWhitePieces.isBitSet(dest)) {
				if (data.DEBUG) {
					System.out.println("ce-ai boss, iti iei singur piesa?");
				}
				return false;
			}
		}
		int direction;

		if ((source % 8) == (dest % 8)) { // daca mergem sus sau jos
			if (source > dest) { // mergem in jos
				direction = -8;
			} else { // mergem in sus
				direction = 8;
			}
		} else { // dreapta sau stg
			if (source > dest) { // dreapta
				direction = -1;
			} else { // stanga
				direction = 1;
			}
		}
		// mergem acu pe directia aia
		int currentIndex = source + direction;
		while (currentIndex != dest) { // mergem patrat cu patrat si vedem daca e ceva in cale
			if (board.AllPieces.isBitSet(currentIndex)) {
				if (data.DEBUG) {
					System.out.println("e ceva in drum boss");
				}
				return false;
			}
			currentIndex += direction;
		}

		return true;
	}

	private static boolean isKnightMoveLegal (int source, int dest, boolean side) {
		return true;
	}

	private static boolean isBishopMoveLegal (int source, int dest, boolean side) {
		Database data = Database.getInstance();
		BoardState board = BoardState.getInstance();
		
		return true;
	}

	private static boolean isQueenMoveLegal (int source, int dest, boolean side) {
		return true;
	}

	private static boolean isKingMoveLegal (int source, int dest, boolean side) {
		return true;
	}

	/**
	 * Face conversia de la "usermove plm" la indexuri (pt board-ul reprezentat pe 64)
	 */
	public static class MoveToIndexes {
		private String move;
		private int sourceIndex;
		private int destIndex;

		public MoveToIndexes(String move, int sourceIndex, int destIndex) {
			this.move = move;
			this.sourceIndex = sourceIndex;
			this.destIndex = destIndex;
		}

		public int getSourceIndex() {
			return sourceIndex;
		}

		public int getDestIndex() {
			return destIndex;
		}

		public MoveToIndexes invoke() {
			if (move.length() == 4) { // miscare normala
				// magic, don't touch.
				sourceIndex = (8 * (move.charAt(1) - '0') - 1) - (move.charAt(0) - 'a');
				destIndex = (8 * (move.charAt(3) - '0') - 1) - (move.charAt(2) - 'a');
			}
			return this;
		}
	}
}
