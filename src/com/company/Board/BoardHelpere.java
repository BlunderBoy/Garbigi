package com.company.Board;

import com.company.Database;

public class BoardHelpere {
	//rank = linie, file = coloana
	//intoarce index pentru tabla de 120
	public static int conversieRFla120(int rank, int file)
	{
		return 21 + file + rank * 10;
	}
	//initializeaza cele 2 array-uri ca sa pot sa fac usor
	//conversia intre cele 2 reprezentari.
	public static int conversie120la64(int array120[], int index) {
		return array120[index];
	}
	

	static int array64[];
	static int array120[];

	public static void initializareArray() {
		array64 = new int[64];
		array120 = new int[120];
		for (int i = 0; i < 120; i++) {
			array120[i] = 65;
		}
		for (int i = 0; i < 64; i++) {
			array64[i] = 120;
		}
		
		//merge de la 0 la 63 si imi seteaza indecsi
		int index = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				int numarSecventa = conversieRFla120(i,j);
				array64[index] = numarSecventa;
				array120[numarSecventa] = index;
				index++;
			}
		}
	}

	public static void initializareValoarePiese()
	{
		BoardState board = BoardState.getInstance();
		board.WhitePawns.valoare = 100;
		board.WhiteKnights.valoare = 325;
		board.WhiteBishops.valoare = 325;
		board.WhiteRooks.valoare = 550;
		board.WhiteQueens.valoare = 1000;
		board.WhiteKing.valoare = 50000;

		board.BlackPawns.valoare = 100;
		board.BlackKnights.valoare = 325;
		board.BlackBishops.valoare = 325;
		board.BlackRooks.valoare = 550;
		board.BlackQueens.valoare = 1000;
		board.BlackKing.valoare = 50000;
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
	
	private static void cineMuta(Database constante, String[] tokens)
	{
		if(tokens[1].contains("w"))
		{
			constante.turn = constante.WHITE;
		}
		else
		{
			constante.turn = constante.BLACK;
		}
	}

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



		int occupied = 0;

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
