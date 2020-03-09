package com.company.Board;

import com.company.Printer;

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
	
	public static void initializareArray(int array64[], int array120[]) {
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

	public static void createBitboardFromFEN (String fen) {
		// TODO reset bitboards
		BoardState.getInstance().resetBoard();

		char currentChar = 0;
		int index = 63;
		BoardState board = BoardState.getInstance();
		for (int i = 0; i < fen.length(); i++) {
			currentChar = fen.charAt(i);

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
		
	}

}
