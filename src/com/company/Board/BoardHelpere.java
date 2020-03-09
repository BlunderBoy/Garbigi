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
		BoardState.getInstance().resetBoard();

		char currentChar = 0;
		int index = 63;
		for (int i = 0; i < fen.length(); i++) {
			currentChar = fen.charAt(i);

			switch (currentChar) {
				case 'N':
					BoardState.getInstance().WhiteKnights.setBit(index);
					BoardState.getInstance().AllWhitePieces.setBit(index);
					BoardState.getInstance().AllPieces.setBit(index);
					index--;
					break;
				case 'R':
					BoardState.getInstance().WhiteRooks.setBit(index);
					BoardState.getInstance().AllWhitePieces.setBit(index);
					BoardState.getInstance().AllPieces.setBit(index);
					index--;
					break;
				case 'B':
					BoardState.getInstance().WhiteBishops.setBit(index);
					BoardState.getInstance().AllWhitePieces.setBit(index);
					BoardState.getInstance().AllPieces.setBit(index);
					index--;
					break;
				case 'P':
					BoardState.getInstance().WhitePawns.setBit(index);
					BoardState.getInstance().AllWhitePieces.setBit(index);
					BoardState.getInstance().AllPieces.setBit(index);
					index--;
					break;
				case 'Q':
					BoardState.getInstance().WhiteQueens.setBit(index);
					BoardState.getInstance().AllWhitePieces.setBit(index);
					BoardState.getInstance().AllPieces.setBit(index);
					index--;
					break;
				case 'K':
					BoardState.getInstance().WhiteKing.setBit(index);
					BoardState.getInstance().AllWhitePieces.setBit(index);
					BoardState.getInstance().AllPieces.setBit(index);
					index--;
					break;

				case 'n':
					BoardState.getInstance().BlackKnights.setBit(index);
					BoardState.getInstance().AllBlackPieces.setBit(index);
					BoardState.getInstance().AllPieces.setBit(index);
					index--;
					break;
				case 'r':
					BoardState.getInstance().BlackRooks.setBit(index);
					BoardState.getInstance().AllBlackPieces.setBit(index);
					BoardState.getInstance().AllPieces.setBit(index);
					index--;
					break;
				case 'b':
					BoardState.getInstance().BlackBishops.setBit(index);
					BoardState.getInstance().AllBlackPieces.setBit(index);
					BoardState.getInstance().AllPieces.setBit(index);
					index--;
					break;
				case 'p':
					BoardState.getInstance().BlackPawns.setBit(index);
					BoardState.getInstance().AllBlackPieces.setBit(index);
					BoardState.getInstance().AllPieces.setBit(index);
					index--;
					break;
				case 'q':
					BoardState.getInstance().BlackQueens.setBit(index);
					BoardState.getInstance().AllBlackPieces.setBit(index);
					BoardState.getInstance().AllPieces.setBit(index);
					index--;
					break;
				case 'k':
					BoardState.getInstance().BlackKing.setBit(index);
					BoardState.getInstance().AllBlackPieces.setBit(index);
					BoardState.getInstance().AllPieces.setBit(index);
					index--;
					break;
				case '/':
					break;
				default:
					index -= (currentChar - '0');
					break;
			}
		}
	}

}
