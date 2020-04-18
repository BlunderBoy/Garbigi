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
			System.out.print("negru?");
		}

		rank--;
		file--;
		BoardState board = BoardState.getInstance();
		Database database = Database.getInstance();
		int pozitie = Database.conversieRFla120(rank, file);
		System.out.print("adica numarul " + Database.conversie120la64(pozitie) + " ");
		int directie;
		
		
		//pioni
		int pionAtacDreaptaNegru = Database.conversie120la64(pozitie - 11);
		int pionAtacStangaNegru = Database.conversie120la64(pozitie - 9);
		int pionAtacDreaptaAlb = Database.conversie120la64(pozitie + 11);
		int pionAtacStangaAlb = Database.conversie120la64(pozitie + 9);

		if (side == database.WHITE) {
			if ((pionAtacDreaptaNegru != 65) && board.WhitePawns.isBitSet(pionAtacDreaptaNegru) ||
				(pionAtacStangaNegru != 65) && board.WhitePawns.isBitSet(pionAtacStangaNegru)) {
				return true;
			}
		} else {
			if ((pionAtacDreaptaAlb != 65) && board.BlackPawns.isBitSet(pionAtacDreaptaAlb)||
					(pionAtacStangaAlb != 65) && board.BlackPawns.isBitSet(pionAtacStangaAlb)) {
				return true;
			}
		}
		
		//cal
		final int[] directiiCal = new int[]{-8, -19, -21, -12, 8, 19, 21, 12};
		for (int i = 0; i < 8; i++) {
			int checker = Database.conversie120la64(pozitie + directiiCal[i]);
			if(checker != 65) {
				if (side == database.WHITE)
					if (board.WhiteKnights.isBitSet(checker)) {
						return true;
					}
				if (side == database.BLACK)
					if (board.BlackKnights.isBitSet(checker)) {
						return true;
					}
			}
		}
		//rege
		final int[] directiiRege = new int[]{-1, -10, 1, 10, -9, -11, 11, 9};
		for (int i = 0; i < 8; i++) {
			int checker = Database.conversie120la64(pozitie + directiiRege[i]);
			if(checker != 65) {
				if (side == database.WHITE)
					if (board.WhiteKing.isBitSet(checker)) {
						return true;
					}
				if (side == database.BLACK)
					if (board.BlackKing.isBitSet(checker)) {
						return true;
					}
			}
		}
		//tura si regina
		final int[] directiiTura = new int[]{-1, -10, 1, 10};
		for (int i = 0; i < 4; i++) {
			directie = directiiTura[i];
			int checker = pozitie + directie;
			while (Database.conversie120la64(checker) != 65) {
				if (board.AllPieces.isBitSet(Database.conversie120la64(checker))) {
					if (side == database.WHITE) {
						if (board.WhiteRooks.isBitSet(Database.conversie120la64(checker)) ||
							board.WhiteQueens.isBitSet(Database.conversie120la64(checker))) {
							return true;
						}
					}
					if (side == database.BLACK) {
						if (board.BlackRooks.isBitSet(Database.conversie120la64(checker)) ||
							board.BlackQueens.isBitSet(Database.conversie120la64(checker))) {
							return true;
						}
					}
					break;
				}
				checker += directie;
			}
		}
		//nebun si regina
		final int[] directiiNebun = new int[]{-1, -11, 11, 9};
		for (int i = 0; i < 4; i++) {
			directie = directiiNebun[i];
			int checker = pozitie + directie;
			while (Database.conversie120la64(checker) != 65) {
				if (board.AllPieces.isBitSet(Database.conversie120la64(checker))) {
					if (side == database.WHITE) {
						if (board.WhiteBishops.isBitSet(Database.conversie120la64(checker)) ||
							board.WhiteQueens.isBitSet(Database.conversie120la64(checker))) {
							return true;
						}
					}
					if (side == database.BLACK) {
						if (board.BlackBishops.isBitSet(Database.conversie120la64(checker)) ||
							board.BlackQueens.isBitSet(Database.conversie120la64(checker))) {
							return true;
						}
					}
					break;
				}
				checker += directie;
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

		// TODO castling & en passant
		int sourceIndex = 0;
		int destIndex = 0;

		int returnCode = 0;

		MoveToIndexes moveToIndexes = new MoveToIndexes(move, sourceIndex, destIndex).invoke();
		sourceIndex = moveToIndexes.getSourceIndex();
		destIndex = moveToIndexes.getDestIndex();

		if (sourceIndex < 0 || sourceIndex > 63 || destIndex < 0 || destIndex > 63) {
			System.out.println("Illegal move, wrong index you dumbass");
			return -69;
		}

		// astea-s comentarii inutile pt mine btw, ignora-le
		// TODO (maybe) verifica daca piesa de la source este a celui care trb sa mute

		// shelved for the moment
		// aparent e mai "comod" sa verifici de FIECARE data FIECARE piesa...
		// daca avem probleme de viteza... AICI trebuie optimizat
		// + toate chestiile care au... 12 blocuri de cod duplicat
		// poate sa insistam pe ideea mea de common arrays?

		char pieceType = getPieceType(sourceIndex);

		if (pieceType == 0) {
			System.out.println("nu avem piesa in source");
			return -2;
		}

		// isKingAttacked

		switch (pieceType) {
			case 'P':
				if (data.DEBUG)
					System.out.println("White pawn.");
				if (isPawnMoveLegal(sourceIndex, destIndex, data.WHITE)) {
					updateBitboard(sourceIndex, destIndex, board.WhitePawns);
				} else {
					System.out.println("nu e legal");
					returnCode = -1;
				}
				break;
			case 'p':
				if (data.DEBUG)
					System.out.println("Black pawn.");
				if (isPawnMoveLegal(sourceIndex, destIndex, data.BLACK)) {
					updateBitboard(sourceIndex, destIndex, board.BlackPawns);
				} else {
					returnCode = -1;
				}
				break;
			case 'R':
				if (data.DEBUG)
					System.out.println("White rook.");
				if (isRookMoveLegal(sourceIndex, destIndex, data.WHITE)) {
					updateBitboard(sourceIndex, destIndex, board.WhiteRooks);
				} else {
					returnCode = -1;
				}
				break;
			case 'r':
				if (data.DEBUG)
					System.out.println("Black rook.");
				if (isRookMoveLegal(sourceIndex, destIndex, data.BLACK)) {
					updateBitboard(sourceIndex, destIndex, board.BlackRooks);
				} else {
					returnCode = -1;
				}
				break;
			case 'N':
				if (data.DEBUG)
					System.out.println("White knight.");
				if (isKnightMoveLegal(sourceIndex, destIndex, data.WHITE)) {
					updateBitboard(sourceIndex, destIndex, board.WhiteKnights);
				} else {
					returnCode = -1;
				}
				break;
			case 'n':
				if (data.DEBUG)
					System.out.println("Black knight.");
				if (isKnightMoveLegal(sourceIndex, destIndex, data.BLACK)) {
					updateBitboard(sourceIndex, destIndex, board.BlackKnights);
				} else {
					returnCode = -1;
				}
				break;
			case 'B':
				if (data.DEBUG)
					System.out.println("White bishop.");
				if (isBishopMoveLegal(sourceIndex, destIndex, data.WHITE)) {
					updateBitboard(sourceIndex, destIndex, board.WhiteBishops);
				} else {
					returnCode = -1;
				}
				break;
			case 'b':
				if (data.DEBUG)
					System.out.println("Black bishop.");
				if (isBishopMoveLegal(sourceIndex, destIndex, data.BLACK)) {
					updateBitboard(sourceIndex, destIndex, board.BlackBishops);
				} else {
					returnCode = -1;
				}
				break;
			case 'Q':
				if (data.DEBUG)
					System.out.println("White queen.");
				if (isQueenMoveLegal(sourceIndex, destIndex, data.WHITE)) {
					updateBitboard(sourceIndex, destIndex, board.WhiteQueens);
				} else {
					returnCode = -1;
				}
				break;
			case 'q':
				if (data.DEBUG)
					System.out.println("Black queen.");
				if (isQueenMoveLegal(sourceIndex, destIndex, data.BLACK)) {
					updateBitboard(sourceIndex, destIndex, board.BlackQueens);
				} else {
					returnCode = -1;
				}
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

		return returnCode; // illegal move
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

	// TODO
	// nu ar trebui ca pawn move sa fie legal daca lasa regele descoperit dupa mutare
	// fac o fct isKingAttacked care verifica asta
	// TODO TREBUIE PUSA LA FIECARE FUNCTIE
	// TODO isKingMoveLegal

	// TODO bounds checking pt tabla, am gasit un corner case pt pion black side
	// "8/8/8/n6P/8/8/8/8 w - - 0 1", usermove h5a5
	public static boolean isPawnMoveLegal (int source, int dest, boolean side) {
		Database data = Database.getInstance();
		BoardState board = BoardState.getInstance();

		int doubleMoveUpperBoundary = 16;
		int doubleMoveLowerBoundary = 7;
		int normalMoveSize = 8;
		int captureMoveL = 9;
		int captureMoveR = 7;

		Bitboard enemyPieces = board.AllBlackPieces;
		if (side == data.BLACK) {
			doubleMoveUpperBoundary = 56;
			doubleMoveLowerBoundary = 47;
			normalMoveSize = -8;
			captureMoveL = -9;
			captureMoveR = -7;
			enemyPieces = board.AllWhitePieces;
		}
		// daca avem double move
		if (source < doubleMoveUpperBoundary && source > doubleMoveLowerBoundary
				&& dest == (source + normalMoveSize * 2)) {
			if (Database.getInstance().DEBUG)
				System.out.println("legal 2 square move");
			// TODO en passant
			return true;
		}
		// daca avem miscare normala in fata
		if (dest == (source + normalMoveSize)) {
			if (getBitboardFromType(getPieceType(dest)) == null) {
				return true;
			} else {
				if (Database.getInstance().DEBUG)
					System.out.println("avem piesa in fata noastra");
			}
		}
		// daca avem capturare
		// miscare legala de capturare
		System.out.println("src: " + source + " capture L "  + captureMoveL + " capture R " + captureMoveR);
		if (dest == (source + captureMoveL) || dest == (source + captureMoveR)) {
			if (enemyPieces.isBitSet(dest)) {
				return true;
			}
		}

		return false;
	}

	private static boolean isRookMoveLegal (int source, int dest, boolean side) {
		// verificare pt sus/jos -> (source % 8) == (dest % 8)
		// source / 8 == dest / 8
		if ((source % 8) != (dest % 8) && (source / 8) != (dest / 8)) { // quick check
			return false;
		}

		if (checkDestIndex(dest, side)) return false;
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
		return isObstacleInTheWay(source, dest, direction);
	}

	private static boolean isObstacleInTheWay(int source, int dest, int direction) {
		Database data = Database.getInstance();
		BoardState board = BoardState.getInstance();

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

	public static boolean isKnightMoveLegal (int source, int dest, boolean side) {
		if (checkDestIndex(dest, side)) return false;

		boolean returnCode = false;

		//
		int[] offset = new int[]{15, 6, 17, 10, -15, -6, -17, -10};
		for (int i = 0; i < 8; i++) {
			if (dest == source + offset[i]) {
				returnCode = true;
				break;
			}
		}

		return returnCode;
	}

	public static boolean isBishopMoveLegal (int source, int dest, boolean side) {
		Database data = Database.getInstance();

		int direction = 0;

		if ((dest % 8) > (source % 8)) { // stanga
			if (data.DEBUG) {
				System.out.println("te duci in stg");
			}
			direction += 1;
		} else { // dreapta
			if (data.DEBUG) {
				System.out.println("te duci in dr");
			}
			direction += -1;
		}

		if ((dest / 8) > (source / 8)) { // sus
			if (data.DEBUG) {
				System.out.println("te duci in sus");
			}
			direction += 8;
		} else { // jos
			if (data.DEBUG) {
				System.out.println("te duci in jos");
			}
			direction += -8;
		}

		if (checkDestIndex(dest, side)) return false;

		return isObstacleInTheWay(source, dest, direction);
	}
	// verifica daca dest e piesa inamica, adica avem capturare, sau daca
	// dest e piesa ta, adica esti prost
	private static boolean checkDestIndex(int dest, boolean side) {
		Database data = Database.getInstance();
		BoardState board = BoardState.getInstance();

		if (side == data.BLACK) {
			if (board.AllWhitePieces.isBitSet(dest) && data.DEBUG)
				System.out.println("avem capturare de la black pt white");

			return board.AllBlackPieces.isBitSet(dest);
		} else {
			if (board.AllBlackPieces.isBitSet(dest) && data.DEBUG)
				System.out.println("avem capturare de la white pt black");
			return board.AllWhitePieces.isBitSet(dest);
		}
	}

	public static boolean isQueenMoveLegal (int source, int dest, boolean side) {
		return isBishopMoveLegal(source, dest, side) || isRookMoveLegal(source, dest, side);
	}

	public static boolean isKingMoveLegal (int source, int dest, boolean side) {
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
