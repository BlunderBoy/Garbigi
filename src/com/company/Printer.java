package com.company;

import com.company.Board.*;

/**
 * Diferite metode pt a printa starea boatrd-ului (mostly debugging shit)
 */

public class Printer {
    public static void print(String fenNotation) {
        // pracurg stringu
        for (int i = 0; i < fenNotation.length(); i++) {
            char currentChar = fenNotation.charAt(i);
            // daca trb sa newline
            if (currentChar == '/') {
                System.out.println();
                continue;
            }
            // daca e cifra
            if (currentChar <= '9') {
                // printam de atatea ori punct
                for (int j = currentChar - 48; j > 0; j--) {
                    System.out.print(". ");
                }
                continue;
            }
            // daca nu e niciuna, inseamna ca e litera deci nu-i facem nik
            System.out.print(currentChar + " ");
        }
    }
	
	/**
	 * print boardstate, all info
	 */
	public static void print() {
    	BoardState board = BoardState.getInstance();
    	boolean print;
		System.out.println("# boardstate usor de citit: ");
        for (int i = 63; i >= 0; i--) {
            print = false;
            if ((i+1) % 8 == 0)
                System.out.print("#");

            if (board.whiteBishops.isBitSet(i)) {
                System.out.print("B ");
                print = true;
            }

            if (board.whiteKnights.isBitSet(i)) {
                System.out.print("N ");
                print = true;
            }

            if (board.whitePawns.isBitSet(i)) {
                System.out.print("P ");
                print = true;
            }

            if (board.whiteKing.isBitSet(i)) {
                System.out.print("K ");
                print = true;
            }

            if (board.whiteQueens.isBitSet(i)) {
                System.out.print("Q ");
                print = true;
            }

            if (board.whiteRooks.isBitSet(i)) {
                System.out.print("R ");
                print = true;
            }

            if (board.blackBishops.isBitSet(i)) {
                System.out.print("b ");
                print = true;
            }

            if (board.blackKnights.isBitSet(i)) {
                System.out.print("n ");
                print = true;
            }

            if (board.blackPawns.isBitSet(i)) {
                System.out.print("p ");
                print = true;
            }

            if (board.blackKing.isBitSet(i)) {
                System.out.print("k ");
                print = true;
            }

            if (board.blackQueens.isBitSet(i)) {
                System.out.print("q ");
                print = true;
            }

            if (board.blackRooks.isBitSet(i)) {
                System.out.print("r ");
                print = true;
            }

            if (!print) {
                System.out.print(". ");
            }

            if (i % 8 == 0) {
                System.out.println();
            }
        }
//        System.out.println("# White pawns: ");
//        print(board.whitePawns.reprezentare);
//        System.out.println("# White knights: ");
//        print(board.whiteKnights.reprezentare);
//        System.out.println("# White bishops: ");
//        print(board.whiteBishops.reprezentare);
//        System.out.println("# White rooks: ");
//        print(board.whiteRooks.reprezentare);
//        System.out.println("# White queen: ");
//        print(board.whiteQueens.reprezentare);
//        System.out.println("# White king: ");
//        print(board.whiteKing.reprezentare);
//
//        System.out.println("# Black pawns: ");
//        print(board.blackPawns.reprezentare);
//        System.out.println("# Black knights: ");
//        print(board.blackKnights.reprezentare);
//        System.out.println("# Black bishops: ");
//        print(board.blackBishops.reprezentare);
//        System.out.println("# Black rooks: ");
//        print(board.blackRooks.reprezentare);
//        System.out.println("# Black queen: ");
//        print(board.blackQueens.reprezentare);
//        System.out.println("# Black king: ");
//        print(board.blackKing.reprezentare);
		/*System.out.println("Negru: ");
        print(BoardState.getInstance().AllBlackPieces);
		System.out.println("Alb: ");
        print(BoardState.getInstance().AllWhitePieces);
		System.out.println("Castleing:");
		for (int i = 0; i < BoardState.getInstance().castlePermision.length; i++)
		{
			if(i == 0)
			{
				System.out.print("albRege " + BoardState.getInstance().castlePermision[i] + " ");
			}
			if(i == 1)
			{
				System.out.print("albRegina " + BoardState.getInstance().castlePermision[i] + " ");
			}
			if(i == 2)
			{
				System.out.print("negruRege " + BoardState.getInstance().castlePermision[i] + " ");
			}
			if(i == 3)
			{
				System.out.print("negruRegina " + BoardState.getInstance().castlePermision[i] + " ");
			}
		}*/
    }

    public static void print(BoardState board) {
        boolean print;
        System.out.println("# boardstate usor de citit: ");
        for (int i = 63; i >= 0; i--) {
            print = false;
            if ((i + 1) % 8 == 0)
                System.out.print("#");

            if (board.whiteBishops.isBitSet(i)) {
                System.out.print("B ");
                print = true;
            }

            if (board.whiteKnights.isBitSet(i)) {
                System.out.print("N ");
                print = true;
            }

            if (board.whitePawns.isBitSet(i)) {
                System.out.print("P ");
                print = true;
            }

            if (board.whiteKing.isBitSet(i)) {
                System.out.print("K ");
                print = true;
            }

            if (board.whiteQueens.isBitSet(i)) {
                System.out.print("Q ");
                print = true;
            }

            if (board.whiteRooks.isBitSet(i)) {
                System.out.print("R ");
                print = true;
            }

            if (board.blackBishops.isBitSet(i)) {
                System.out.print("b ");
                print = true;
            }

            if (board.blackKnights.isBitSet(i)) {
                System.out.print("n ");
                print = true;
            }

            if (board.blackPawns.isBitSet(i)) {
                System.out.print("p ");
                print = true;
            }

            if (board.blackKing.isBitSet(i)) {
                System.out.print("k ");
                print = true;
            }

            if (board.blackQueens.isBitSet(i)) {
                System.out.print("q ");
                print = true;
            }

            if (board.blackRooks.isBitSet(i)) {
                System.out.print("r ");
                print = true;
            }

            if (!print) {
                System.out.print(". ");
            }

            if (i % 8 == 0) {
                System.out.println();
            }
        }

    }

    public static void print(Bitboard b) {
    	long shifter = 1;
    	shifter <<= 63;

	    for (int i = 0; i < 64; i++) {
		    if ((b.reprezentare & shifter) != 0) {
			    System.out.print("# ");
		    } else {
			    System.out.print(". ");
		    }

		    if ((i+1) % 8 == 0 && i != 0) {
			    System.out.println();
		    }
		    shifter >>>= 1;
	    }
	    System.out.println();
    }
    
    public static void print(long b) {
    	long shifter = 1;
    	shifter <<= 63;

	    for (int i = 0; i < 64; i++) {
		    if ((b & shifter) != 0) {
			    System.out.print("# ");
		    } else {
			    System.out.print(". ");
		    }

		    if ((i+1) % 8 == 0 && i != 0) {
			    System.out.println();
		    }
		    shifter >>>= 1;
	    }
	    System.out.println();
    }
}
