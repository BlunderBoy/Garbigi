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
	public static boolean isSquareAttacked(int file, int rank, boolean side)
	{
		BoardState board = BoardState.getInstance();
		Database database = Database.getInstance();
		int pozitie = Database.conversieRFla120(rank, file);
		pozitie = Database.conversie120la64(pozitie);
		System.out.println("verific pentru pozitia " + pozitie);
		int directie;

		if (side == database.WHITE) {
			if (board.WhitePawns.isBitSet(pozitie-11) || board.WhitePawns.isBitSet(pozitie-9)) {
				return true;
			}
		} else {
			if (board.BlackPawns.isBitSet(pozitie+11) || board.BlackPawns.isBitSet(pozitie+9)) {
				return true;
			}
		}
		return false;
	}

	private static void castlingPermissions(String[] tokens)
	{
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

	private static void populareBiboards(int index, BoardState board, String token)
	{
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
		String[] tokens = command.split(" ");
		String move = tokens[1];
		// TODO maybe check if move is legal??
		// TODO castling
		int sourceIndex = 0;
		int destIndex = 0;

		MoveToIndexes moveToIndexes = new MoveToIndexes(move, sourceIndex, destIndex).invoke();
		sourceIndex = moveToIndexes.getSourceIndex();
		destIndex = moveToIndexes.getDestIndex();

		// verifica daca piesa de la source este a celui care trb sa mute
		// verifica daca exista spatiu unde sa o mute sau daca captureaza piesa
		// actualizeaza bitboards

		int indexToCheck = 0;
		if (Database.getInstance().turn == Database.getInstance().BLACK) {
			indexToCheck = 1;
		}

		if (Database.getInstance().DEBUG) {
				System.out.println("move: " + command + "source index: " + sourceIndex + ", dest index: " + destIndex);
		}

		return 0;
	}

	private static void updateBitboard(int sourceIndex, int destIndex, Bitboard bitboard) {
		// TODO capturari
		bitboard.clearBit(sourceIndex);
		bitboard.setBit(destIndex);
	}

	private static char getPieceType (int index) {
		if (BoardState.getInstance().Pawns[0].isBitSet(index)) {
			return 'P';
		}
		if (BoardState.getInstance().Pawns[1].isBitSet(index)) {
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
	private static boolean isPawnMoveLegal (int source, int dest) {
		return true;
	}

	private static boolean isRookMoveLegal (int source, int dest) {
		return true;
	}

	private static boolean isKnightMoveLegal (int source, int dest) {
		return true;
	}

	private static boolean isBishopMoveLegal (int source, int dest) {
		return true;
	}

	private static boolean isQueenMoveLegal (int source, int dest) {
		return true;
	}

	private static boolean isKingMoveLegal (int source, int dest) {
		return true;
	}

	/**
	 * Face conversia de la "usermove plm" la indexuri (pt board-ul reprezentat pe 64)
	 */
	private static class MoveToIndexes {
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
