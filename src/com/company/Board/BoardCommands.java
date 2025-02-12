package com.company.Board;

import com.company.Database;
import com.company.MoveAndSearch.MoveGenerator;
import com.company.MoveAndSearch.SlidingPieceGenerator;
import com.company.Printer;

/**
 * Clasa asta contine comenzi legate de starea board-ului, de exemplu initializarea unui board nou,
 * crearea unui board dintr-un fen, actualizarea board-ului cand primesti/faci o mutare (soon)
 */

public class BoardCommands {
	public static void initGame() {
		Database.initializareArray();
		// initial config
		BoardCommands.createBoardstateFromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		// debugging config
		//BoardCommands.createBoardstateFromFEN("rnbqk2r/ppp2ppp/3pp3/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1");
		SlidingPieceGenerator.initSlidingPieceGenerator();
		MoveGenerator.initMoveGenerator();
		Database.getInstance().engineColor = Database.getInstance().BLACK;
		Database.getInstance().opponentColor = Database.getInstance().WHITE;
		Database.getInstance().turn = Database.getInstance().WHITE;
		Database.getInstance().forceMode = false;
	}
	
	public static void initGame(String fen) {
		Database.initializareArray();
		BoardCommands.createBoardstateFromFEN(fen);
		SlidingPieceGenerator.initSlidingPieceGenerator();
		MoveGenerator.initMoveGenerator();
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
		enPassant(tokens, board);
		database.halfMoves = Integer.parseInt(tokens[4]);
		database.fullMoves = Integer.parseInt(tokens[5]);
	}
	
	public static void enPassant(String[] tokens, BoardState board)
	{
		if(!tokens[3].equals("-"))
			board.enPassant = ((8 * (tokens[3].charAt(1) - '0') - 1) - (tokens[3].charAt(0) - 'a')) - 1;
	}

	//WHITE = true, BLACK = false
	// verifica daca piesele de pe SIDE ataca square-ul dat
	public static boolean isSquareAttacked(int pozitie, boolean side) {
		BoardState board = BoardState.getInstance();
		Database database = Database.getInstance();
		pozitie = Database.conversie64la120(pozitie);
//		System.out.print("adica numarul " + Database.conversie120la64(pozitie) + " ");
		int directie;
		
		
		//pioni
		int pionAtacDreaptaNegru = Database.conversie120la64(pozitie - 11);
		int pionAtacStangaNegru = Database.conversie120la64(pozitie - 9);
		int pionAtacDreaptaAlb = Database.conversie120la64(pozitie + 11);
		int pionAtacStangaAlb = Database.conversie120la64(pozitie + 9);

		if (side == database.WHITE) {
			if ((pionAtacDreaptaNegru != 65) && board.whitePawns.isBitSet(pionAtacDreaptaNegru) ||
				(pionAtacStangaNegru != 65) && board.whitePawns.isBitSet(pionAtacStangaNegru)) {
				return true;
			}
		} else {
			if ((pionAtacDreaptaAlb != 65) && board.blackPawns.isBitSet(pionAtacDreaptaAlb)||
					(pionAtacStangaAlb != 65) && board.blackPawns.isBitSet(pionAtacStangaAlb)) {
				return true;
			}
		}
		
		//cal
		final int[] directiiCal = new int[]{-8, -19, -21, -12, 8, 19, 21, 12};
		for (int i = 0; i < 8; i++) {
			int checker = Database.conversie120la64(pozitie + directiiCal[i]);
			if(checker != 65) {
				if (side == database.WHITE)
					if (board.whiteKnights.isBitSet(checker)) {
						return true;
					}
				if (side == database.BLACK)
					if (board.blackKnights.isBitSet(checker)) {
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
					if (board.whiteKing.isBitSet(checker)) {
						return true;
					}
				if (side == database.BLACK)
					if (board.blackKing.isBitSet(checker)) {
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
				if (board.allPieces.isBitSet(Database.conversie120la64(checker))) {
					if (side == database.WHITE) {
						if (board.whiteRooks.isBitSet(Database.conversie120la64(checker)) ||
							board.whiteQueens.isBitSet(Database.conversie120la64(checker))) {
							return true;
						}
					}
					if (side == database.BLACK) {
						if (board.blackRooks.isBitSet(Database.conversie120la64(checker)) ||
							board.blackQueens.isBitSet(Database.conversie120la64(checker))) {
							return true;
						}
					}
					break;
				}
				checker += directie;
			}
		}
		//nebun si regina
		final int[] directiiNebun = new int[]{-9, -11, 11, 9};
		for (int i = 0; i < 4; i++) {
			directie = directiiNebun[i];
			int checker = pozitie + directie;
			while (Database.conversie120la64(checker) != 65) {
				if (board.allPieces.isBitSet(Database.conversie120la64(checker))) {
					if (side == database.WHITE) {
						if (board.whiteBishops.isBitSet(Database.conversie120la64(checker)) ||
							board.whiteQueens.isBitSet(Database.conversie120la64(checker))) {
							return true;
						}
					}
					if (side == database.BLACK) {
						if (board.blackBishops.isBitSet(Database.conversie120la64(checker)) ||
							board.blackQueens.isBitSet(Database.conversie120la64(checker))) {
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
		if(tokens.equals("-"))
			return;
		if(tokens[2].contains("K"))
		{
			BoardState.getInstance().castlePermission[0] = 1;
		}
		if(tokens[2].contains("Q"))
		{
			BoardState.getInstance().castlePermission[1] = 1;
		}
		if(tokens[2].contains("k"))
		{
			BoardState.getInstance().castlePermission[2] = 1;
		}
		if(tokens[2].contains("q"))
		{
			BoardState.getInstance().castlePermission[3] = 1;
		}
	}

	private static void populareBiboards(int index, BoardState board, String token) {
		char currentChar;
		for (int i = 0; i < token.length(); i++) {
			currentChar = token.charAt(i);

			switch (currentChar) {
				case 'N':
					board.whiteKnights.setBit(index);
					index--;
					break;
				case 'R':
					board.whiteRooks.setBit(index);
					index--;
					break;
				case 'B':
					board.whiteBishops.setBit(index);
					index--;
					break;
				case 'P':
					board.whitePawns.setBit(index);
					index--;
					break;
				case 'Q':
					board.whiteQueens.setBit(index);
					index--;
					break;
				case 'K':
					board.whiteKing.setBit(index);
					index--;
					break;

				case 'n':
					board.blackKnights.setBit(index);
					index--;
					break;
				case 'r':
					board.blackRooks.setBit(index);
					index--;
					break;
				case 'b':
					board.blackBishops.setBit(index);
					index--;
					break;
				case 'p':
					board.blackPawns.setBit(index);
					index--;
					break;
				case 'q':
					board.blackQueens.setBit(index);
					index--;
					break;
				case 'k':
					board.blackKing.setBit(index);
					index--;
					break;
				case '/':
					break;
				default:
					index -= (currentChar - '0');
					break;
			}
		}
		board.allWhitePieces.reprezentare = board.whiteKing.reprezentare |
				               board.whiteBishops.reprezentare |
							   board.whiteKnights.reprezentare |
				               board.whiteQueens.reprezentare |
						       board.whiteRooks.reprezentare |
							   board.whitePawns.reprezentare;
		
		board.allBlackPieces.reprezentare = board.blackKing.reprezentare |
				               board.blackBishops.reprezentare |
							   board.blackKnights.reprezentare |
				               board.blackQueens.reprezentare |
						       board.blackRooks.reprezentare |
							   board.blackPawns.reprezentare;
		
		board.allPieces.reprezentare = board.allWhitePieces.reprezentare |
							           board.allBlackPieces.reprezentare;
		
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

		// promotion
		if (move.length() == 5) {
			char pieceType = moveToIndexes.promotedPiece;
			if (data.opponentColor == data.WHITE) {
				pieceType -= 32; // to uppercase
			}
			Bitboard promoted = getBitboardFromType(pieceType);
			System.out.println("# pawn promoted to " + pieceType);
			promoted.setBit(destIndex);

			Bitboard source = getBitboardFromType(getPieceType(sourceIndex));
			source.clearBit(sourceIndex);
			// sper ca compileaza, mi s-a bulit proiectul si nu mai pot pusha din intellij si pe interfata web nu am linter..
			board.updateBitboards();
			// test
			return 1;
		}

		if (sourceIndex < 0 || sourceIndex > 63 || destIndex < 0 || destIndex > 63) {
			System.out.println("Illegal move, wrong index you dumbass");
			return -69;
		}

		// CASTLING
		if (sourceIndex == 3 && destIndex == 1) { // white king side
			updateBitboard(0, 2, board.whiteRooks);
		}
		if (sourceIndex == 3 && destIndex == 5) { // white queen side
			updateBitboard(7, 4, board.whiteRooks);
		}
		if (sourceIndex == 59 && destIndex == 57) { // black king side
			updateBitboard(56, 58, board.blackRooks);
		}
		if (sourceIndex == 59 && destIndex == 61) { // black king side
			updateBitboard(63, 60, board.blackRooks);
		}
		
		BoardState.getInstance().updateBitboards();
		//Printer.print();
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
					updateBitboard(sourceIndex, destIndex, board.whitePawns);
				break;
			case 'p':
				if (data.DEBUG)
					System.out.println("Black pawn.");
					updateBitboard(sourceIndex, destIndex, board.blackPawns);
				break;
			case 'R':
				if (data.DEBUG)
					System.out.println("White rook.");
					updateBitboard(sourceIndex, destIndex, board.whiteRooks);
				break;
			case 'r':
				if (data.DEBUG)
					System.out.println("Black rook.");
					updateBitboard(sourceIndex, destIndex, board.blackRooks);
				break;
			case 'N':
				if (data.DEBUG)
					System.out.println("White knight.");
					updateBitboard(sourceIndex, destIndex, board.whiteKnights);
				break;
			case 'n':
				if (data.DEBUG)
					System.out.println("Black knight.");
					updateBitboard(sourceIndex, destIndex, board.blackKnights);
				break;
			case 'B':
				if (data.DEBUG)
					System.out.println("White bishop.");
					updateBitboard(sourceIndex, destIndex, board.whiteBishops);
				break;
			case 'b':
				if (data.DEBUG)
					System.out.println("Black bishop.");
					updateBitboard(sourceIndex, destIndex, board.blackBishops);
				break;
			case 'Q':
				if (data.DEBUG)
					System.out.println("White queen.");
					updateBitboard(sourceIndex, destIndex, board.whiteQueens);
				break;
			case 'q':
				if (data.DEBUG)
					System.out.println("Black queen.");
					updateBitboard(sourceIndex, destIndex, board.blackQueens);
				break;
			case 'K':
				if (data.DEBUG)
					System.out.println("White king.");
				updateBitboard(sourceIndex, destIndex, board.whiteKing);
				break;
			case 'k':
				if (data.DEBUG)
					System.out.println("Black king.");
				updateBitboard(sourceIndex, destIndex, board.blackKing);
				break;
			default:
				if (data.DEBUG)
					System.out.println("you did smth wrong");
		}

	

		return returnCode; // illegal move
	}

	// cu capturari
	public static void updateBitboard(int sourceIndex, int destIndex, Bitboard source) {
		// verificam LA INCEPUT daca avem capturare
		Bitboard dest = getBitboardFromType(getPieceType(destIndex));

		if (dest != null) {
			dest.clearBit(destIndex);
		}

		source.clearBit(sourceIndex);
		source.setBit(destIndex);

		BoardState.getInstance().updateBitboards();
	}
	

	private static Bitboard getBitboardFromType (char type) {
		BoardState board = BoardState.getInstance();
		switch (type) {
			case 'P':
				return board.whitePawns;
			case 'p':
				return board.blackPawns;
			case 'R':
				return board.whiteRooks;
			case 'r':
				return board.blackRooks;
			case 'N':
				return board.whiteKnights;
			case 'n':
				return board.blackKnights;
			case 'B':
				return board.whiteBishops;
			case 'b':
				return board.blackBishops;
			case 'Q':
				return board.whiteQueens;
			case 'q':
				return board.blackQueens;
			case 'K':
				return board.whiteKing;
			case 'k':
				return board.blackKing;
			default:
				return null;
		}
	}

	private static char getPieceType (int index) {
		if (BoardState.getInstance().whitePawns.isBitSet(index)) {
			return 'P';
		}
		if (BoardState.getInstance().blackPawns.isBitSet(index)) {
			return 'p';
		}
		if (BoardState.getInstance().whiteRooks.isBitSet(index)) {
			return 'R';
		}
		if (BoardState.getInstance().blackRooks.isBitSet(index)) {
			return 'r';
		}
		if (BoardState.getInstance().whiteBishops.isBitSet(index)) {
			return 'B';
		}
		if (BoardState.getInstance().blackBishops.isBitSet(index)) {
			return 'b';
		}
		if (BoardState.getInstance().whiteKnights.isBitSet(index)) {
			return 'N';
		}
		if (BoardState.getInstance().blackKnights.isBitSet(index)) {
			return 'n';
		}
		if (BoardState.getInstance().whiteQueens.isBitSet(index)) {
			return 'Q';
		}
		if (BoardState.getInstance().blackQueens.isBitSet(index)) {
			return 'q';
		}
		if (BoardState.getInstance().whiteKing.isBitSet(index)) {
			return 'K';
		}
		if (BoardState.getInstance().blackKing.isBitSet(index)) {
			return 'k';
		}
		return 0;
	}
	
	public static int getPieceType(BoardState board, int index, boolean side) {
		if (side)
		{
			return getPieceTypeWhite(board, index);
		}
		else
		{
			return getPieceTypeBlack(board, index);
		}
	}
	
	static int getPieceTypeWhite(BoardState board, int index)
	{
		if (board.whitePawns.isBitSet(index)) {
			return 0;
		}
		if (board.whiteKnights.isBitSet(index)) {
			return 1;
		}
		if (board.whiteBishops.isBitSet(index)) {
			return 2;
		}
		if (board.whiteRooks.isBitSet(index)) {
			return 3;
		}
		if (board.whiteQueens.isBitSet(index)) {
			return 4;
		}
		if (board.whiteKing.isBitSet(index)) {
			return 5;
		}
		return -1;
	}
	
	static int getPieceTypeBlack(BoardState board, int index)
	{
		if (board.blackPawns.isBitSet(index)) {
			return 0;
		}
		if (board.blackKnights.isBitSet(index)) {
			return 1;
		}
		if (board.blackBishops.isBitSet(index)) {
			return 2;
		}
		if (board.blackRooks.isBitSet(index)) {
			return 3;
		}
		if (board.blackQueens.isBitSet(index)) {
			return 4;
		}
		if (board.blackKing.isBitSet(index)) {
			return 5;
		}
		return -1;
	}

	// TODO
	// nu ar trebui ca pawn move sa fie legal daca lasa regele descoperit dupa mutare
	// fac o fct isKingAttacked care verifica asta
	// TODO TREBUIE PUSA LA FIECARE FUNCTIE
	// TODO isKingMoveLegal

	// bounds checking pt tabla, am gasit un corner case pt pion black side
	// "8/8/8/n6P/8/8/8/8 w - - 0 1", usermove h5a5

	// nu mai trb facut pentru ca doar miscarile primite de la xboard, adica legale,
	// trec prin functia asta. ar trebui sa rezolvam bug-ul just for corectness...
	public static boolean isPawnMoveLegal (int source, int dest, boolean side) {
		Database data = Database.getInstance();
		BoardState board = BoardState.getInstance();

		int doubleMoveUpperBoundary = 16;
		int doubleMoveLowerBoundary = 7;
		int normalMoveSize = 8;
		int captureMoveL = 9;
		int captureMoveR = 7;

		Bitboard enemyPieces = board.allBlackPieces;
		if (side == data.BLACK) {
			doubleMoveUpperBoundary = 56;
			doubleMoveLowerBoundary = 47;
			normalMoveSize = -8;
			captureMoveL = -9;
			captureMoveR = -7;
			enemyPieces = board.allWhitePieces;
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
			if (board.allPieces.isBitSet(currentIndex)) {
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
			if (board.allWhitePieces.isBitSet(dest) && data.DEBUG)
				System.out.println("avem capturare de la black pt white");

			return board.allBlackPieces.isBitSet(dest);
		} else {
			if (board.allBlackPieces.isBitSet(dest) && data.DEBUG)
				System.out.println("avem capturare de la white pt black");
			return board.allWhitePieces.isBitSet(dest);
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
		private char promotedPiece;

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

		public char getPromotedPiece () {
			return promotedPiece;
		}

		public MoveToIndexes invoke() {
			if (move.length() == 5) { // promotion
				promotedPiece = move.charAt(4);
			}
			// magic, don't touch.
			sourceIndex = (8 * (move.charAt(1) - '0') - 1) - (move.charAt(0) - 'a');
			destIndex = (8 * (move.charAt(3) - '0') - 1) - (move.charAt(2) - 'a');

			return this;
		}
	}
}
