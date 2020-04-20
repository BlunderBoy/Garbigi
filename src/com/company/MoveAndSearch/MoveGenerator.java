package com.company.MoveAndSearch;

import com.company.*;
import com.company.Board.Bitboard;
import com.company.Board.BoardCommands;
import com.company.Board.BoardState;

import java.util.ArrayList;
import java.util.HashMap;

import static com.company.Board.Bitboard.popLSB;

public class MoveGenerator {
	public ArrayList<Move> mutariGenerate;

    //helpere
    HashMap<Integer, Integer> getrank;




    public MoveGenerator() {
        //in hashpmap o sa am [0,1,2,3,4,5,6,7] cu cheia 1
        //[8,9,10,11,12,13,14,15] cu cheia 2 etc
        //ca sa pot lua usor rank-ul dupa pozitie
        mutariGenerate = new ArrayList<>();
        getrank = new HashMap<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                getrank.put((i) * 8 + j, i + 1);
            }
        }
    }

    //helper
    public int getRank(int pozitiePiesaIn64) {
        return getrank.get(pozitiePiesaIn64);
    }

    //printer
    void moveListPrint() {
        for (Move m : mutariGenerate) {
            m.printMove();
        }
    }

    //adaugare de mutari
    //quiet move fara capturare
    void addMove(int mutare) {
        Move move = new Move(mutare, 0);
        mutariGenerate.add(move);
    }

    void addCaptureMove(int mutare) {
        Move move = new Move(mutare, 0);
        mutariGenerate.add(move);
    }

    void addEnPassantMove(int mutare) {
        Move move = new Move(mutare, 0);
        mutariGenerate.add(move);
    }

    //creator de mutari
    int createMove(int sursa, int destinatie, int promotie, int flag) {
        int move = 0;
        move = move |
                sursa |
                (destinatie << 6) |
                (promotie << 12) |
                (flag << 14);
        return move;
    }

    //generatoare de mutari
    //primeste white sau black pawns bitboard
    public void generatePawnMoves(boolean side) throws CloneNotSupportedException {
        BoardState board = BoardState.getInstance();
        Bitboard bitBoard;
        if (side) {
            bitBoard = (Bitboard) board.whitePawns.clone();
        } else {
            bitBoard = (Bitboard) board.blackPawns.clone();
        }
        while (bitBoard.reprezentare != 0) {
            //extrag locul unde se afla piesa
            int pozitie = popLSB(bitBoard.reprezentare);
            bitBoard.clearBit(pozitie);
            int move = 0;
            int temp = Database.conversie120la64(pozitie);
            int negruDreapta = temp - 11;
            int negruStanga = temp - 9;
            int albStanga = temp + 11;
            int albDreapta = temp + 9;

            //TODO : daca facand pop regele ar fi in sah inseamna ca misacarea nu e valida, ca sa nu tot verific

            //daca suntem pe alb
            if (side) {
                if (getRank(pozitie) == 7) //daca suntem pe penultimul rank inseamna ca pot sa si fac promotie
                {
                    //setez flag de promotie
                    if (!board.allPieces.isBitSet(pozitie + 8)) {
                        //adaug miscare pion alb in fata daca regele nu e in sah
                        for (int i = 3; i >= 0; i--) {
                            move = createMove(pozitie, pozitie + 8, i, 1);
                            addMove(move);
                        }
                    }

                    if (Database.conversie120la64(albDreapta) != 65 && !board.allWhitePieces.isBitSet(Database.conversie120la64(albDreapta))) {
                        //adaug miscare pion alb in dreapta daca regele nu e in sah
                        if (board.allBlackPieces.isBitSet(Database.conversie120la64(albDreapta))) {
                            for (int i = 3; i >= 0; i--) {
                                move = createMove(pozitie, pozitie + 7, i, 1);
                                addCaptureMove(move);
                            }
                        }
                    }
                    if (Database.conversie120la64(albStanga) != 65 && !board.allWhitePieces.isBitSet(Database.conversie120la64(albStanga))) {
                        //adaug miscare pion alb in dreapta daca regele nu e in sah
                        if (board.allBlackPieces.isBitSet(Database.conversie120la64(albStanga))) {
                            for (int i = 3; i >= 0; i--) {
                                move = createMove(pozitie, pozitie + 9, i, 1);
                                addCaptureMove(move);
                            }
                        }
                    }
                } else {
                    if (getRank(pozitie) == 5) {
                        if (Database.conversie120la64(albDreapta) != 65 && (board.enPassant == Database.conversie120la64(albDreapta))) {
                            move = createMove(pozitie, pozitie + 7, 0, 2);
                            addEnPassantMove(move);
                        }
                        if (Database.conversie120la64(albStanga) != 65 && (board.enPassant == Database.conversie120la64(albStanga))) {
                            move = createMove(pozitie, pozitie + 9, 0, 2);
                            addEnPassantMove(move);
                        }
                    }
                    if(getRank(pozitie) == 2)
                    {
                    	if (!board.allPieces.isBitSet(pozitie + 16) && !board.allPieces.isBitSet(pozitie + 8)) {
                                move = createMove(pozitie, pozitie + 16, 0, 0);
                                addMove(move);
                    	}
                    }
                    if (!board.allPieces.isBitSet(pozitie + 8)) {
                        move = createMove(pozitie, pozitie + 8, 0, 0);
                        addMove(move);
                    }
                    if (Database.conversie120la64(albDreapta) != 65 && !board.allWhitePieces.isBitSet(Database.conversie120la64(albDreapta))) {
                        if (board.allBlackPieces.isBitSet(Database.conversie120la64(albDreapta))) {
                            move = createMove(pozitie, pozitie + 7, 0, 0);
                            addCaptureMove(move);
                        }
                    }
                    if (Database.conversie120la64(albStanga) != 65 && !board.allWhitePieces.isBitSet(Database.conversie120la64(albStanga))) {
                        if (board.allBlackPieces.isBitSet(Database.conversie120la64(albStanga))) {
                            move = createMove(pozitie, pozitie + 9, 0, 0);
                            addCaptureMove(move);
                        }
                    }
                }
            } else { //negru ghici
                if (getRank(pozitie) == 4) { //daca suntem pe penultimul rank inseamna ca pot sa si fac promotie
                    //setez flag de promotie
                    if (!board.allPieces.isBitSet(pozitie - 8)) {
                        //adaug miscare pion negru in fata daca regele nu e in sah
                        for (int i = 3; i >= 0; i--) {
                            move = createMove(pozitie, pozitie - 8, i, 1);
                            addMove(move);
                        }
                    }
                    if (Database.conversie120la64(negruDreapta) != 65 && !board.allBlackPieces.isBitSet(Database.conversie120la64(negruDreapta))) {
                        //adaug miscare pion negru in dreapta daca regele nu e in sah
                        if (board.allWhitePieces.isBitSet(Database.conversie120la64(negruDreapta))) {
                            for (int i = 3; i >= 0; i--) {
                                move = createMove(pozitie, pozitie - 9, i, 1);
                                addCaptureMove(move);
                            }
                        }
                    }
                    if (Database.conversie120la64(negruStanga) != 65 && !board.allBlackPieces.isBitSet(Database.conversie120la64(negruStanga))) {
                        //adaug miscare pion negru in dreapta daca regele nu e in sah
                        if (board.allWhitePieces.isBitSet(Database.conversie120la64(negruStanga))) {
                            for (int i = 3; i >= 0; i--) {
                                move = createMove(pozitie, pozitie - 7, i, 1);
                                addCaptureMove(move);
                            }
                        }
                    }
                } else {
                    if (getRank(pozitie) == 3) {
                        if (Database.conversie120la64(negruDreapta) != 65 && (board.enPassant == Database.conversie120la64(negruDreapta))) {
                            move = createMove(pozitie, pozitie - 9, 0, 2);
                            addEnPassantMove(move);
                        }
                        if (Database.conversie120la64(negruStanga) != 65 && (board.enPassant == Database.conversie120la64((negruStanga)))) {
                            move = createMove(pozitie, pozitie - 7, 0, 2);
                            addEnPassantMove(move);
                        }
                    }
                    if(getRank(pozitie) == 7)
                    {
                    	if (!board.allPieces.isBitSet(pozitie - 16) && !board.allPieces.isBitSet(pozitie - 8)) {
                                move = createMove(pozitie, pozitie - 16, 0, 0);
                                addMove(move);
                    	}
                    }
                    if (!board.allPieces.isBitSet(pozitie - 8)) {
                        move = createMove(pozitie, pozitie - 8, 0, 0);
                        addMove(move);
                    }
                    if (Database.conversie120la64(negruDreapta) != 65 && !board.allBlackPieces.isBitSet(Database.conversie120la64(negruDreapta))) {
                        if (board.allWhitePieces.isBitSet(Database.conversie120la64(negruDreapta))) {
                            move = createMove(pozitie, pozitie - 9, 0, 0);
                            addCaptureMove(move);
                        }
                    }
                    if (Database.conversie120la64(negruStanga) != 65 && !board.allBlackPieces.isBitSet(Database.conversie120la64(negruStanga))) {
                        if (board.allWhitePieces.isBitSet(Database.conversie120la64(negruStanga))) {
                            move = createMove(pozitie, pozitie - 7, 0, 0);
                            addCaptureMove(move);
                        }
                    }
                }
            }
        }
    }

    public void generateKnightMoves(boolean side) throws CloneNotSupportedException {
        BoardState board = BoardState.getInstance();
        Bitboard bitBoard;
        if (side) {
            bitBoard = (Bitboard) board.whiteKnights.clone();
        } else {
            bitBoard = (Bitboard) board.blackKnights.clone();
        }
        while (bitBoard.reprezentare != 0) {
            int pozitie = popLSB(bitBoard.reprezentare);
            bitBoard.clearBit(pozitie);
            int temp = Database.conversie64la120(pozitie);
            final int[] directiiCal = new int[]{-8, -19, -21, -12, 8, 19, 21, 12};
            for (int i = 0; i < 8; i++) {
                int checker = Database.conversie120la64(temp + directiiCal[i]);
                int move = 0;
                if (checker != 65) {
                    if (side) {
                        if (!board.allWhitePieces.isBitSet(checker)) {
                            if (board.allBlackPieces.isBitSet(checker)) {
                                move = createMove(pozitie, checker, 0, 0);
                                addCaptureMove(move);
                            } else {
                                move = createMove(pozitie, checker, 0, 0);
                                addMove(move);
                            }
                        }
                    } else {
                        if (!board.allBlackPieces.isBitSet(checker)) {
                            if (board.allWhitePieces.isBitSet(checker)) {
                                move = createMove(pozitie, checker, 0, 0);
                                addCaptureMove(move);
                            } else {
                                move = createMove(pozitie, checker, 0, 0);
                                addMove(move);
                            }
                        }
                    }
                }
            }
        }
    }

    //sa nu generez miscari ilegale
    public void generateKingMoves(boolean side) throws CloneNotSupportedException {
        BoardState board = BoardState.getInstance();
        Bitboard bitBoard;
        if (side) {
            bitBoard = (Bitboard) board.whiteKing.clone();
        } else {
            bitBoard = (Bitboard) board.blackKing.clone();
        }
        while (bitBoard.reprezentare != 0) {
            int pozitie = popLSB(bitBoard.reprezentare);
            bitBoard.clearBit(pozitie);
            int temp = Database.conversie64la120(pozitie);
            final int[] directiiRege = new int[]{-1, -10, 1, 10, -9, -11, 11, 9};
            for (int i = 0; i < 8; i++) {
                int checker = Database.conversie120la64(temp + directiiRege[i]);
                int move = 0;
                if (checker != 65) {
                    if (side) {
                        if (!board.allWhitePieces.isBitSet(checker)) {
                            if (board.allBlackPieces.isBitSet(checker)) {
                                if (!BoardCommands.isSquareAttacked(checker, false)) {
                                    move = createMove(pozitie, checker, 0, 0);
                                    addCaptureMove(move);
                                }
                            } else {
                                if (!BoardCommands.isSquareAttacked(checker, false)) {
                                    move = createMove(pozitie, checker, 0, 0);
                                    addMove(move);
                                }
                            }
                        }
                    } else {
                        if (!board.allBlackPieces.isBitSet(checker)) {
                            if (board.allWhitePieces.isBitSet(checker)) {
                                if (!BoardCommands.isSquareAttacked(checker, true)) {
                                    move = createMove(pozitie, checker, 0, 0);
                                    addCaptureMove(move);
                                }
                            } else {
                                if (!BoardCommands.isSquareAttacked(checker, true)) {
                                    move = createMove(pozitie, checker, 0, 0);
                                    addMove(move);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
	
	public long getBishopAttacks(int pozitie, long blockers, boolean side)
	{
		blockers &= SlidingPieceGenerator.bishopMasks[pozitie];
		long rezultat = SlidingPieceGenerator.magicBishopTable[pozitie][(int) ((blockers * SlidingPieceGenerator.bishopMagics[pozitie])
				>>> (64 - SlidingPieceGenerator.bishopIndexBits[pozitie]))];
		if(side)
		{
			rezultat &= ~BoardState.getInstance().allWhitePieces.reprezentare;
		}
		else
		{
			rezultat &= ~BoardState.getInstance().allBlackPieces.reprezentare;
		}
		return rezultat;
	}
	
	public long getRookAttacks(int pozitie, long blockers, boolean side)
	{
		blockers &= SlidingPieceGenerator.rookMasks[pozitie];
		int key = (int) ((blockers * SlidingPieceGenerator.rookMagics[pozitie])
				>>> (64 - SlidingPieceGenerator.rookIndexBits[pozitie]));
		System.out.println("--" + key);
		long rezultat = SlidingPieceGenerator.magicRookTable[pozitie][key];
		if(side)
		{
			rezultat &= ~BoardState.getInstance().allWhitePieces.reprezentare;
		}
		else
		{
			rezultat &= ~BoardState.getInstance().allBlackPieces.reprezentare;
		}
		return rezultat;
	}
	
	public long getQueenAtacks(int pozitie, long blockers, boolean side)
	{
		long rezultat = getRookAttacks(pozitie, blockers, side) | getBishopAttacks(pozitie, blockers, side);
		rezultat &= ~BoardState.getInstance().allWhitePieces.reprezentare;
		if(side)
		{
			rezultat &= ~BoardState.getInstance().allWhitePieces.reprezentare;
		}
		else
		{
			rezultat &= ~BoardState.getInstance().allBlackPieces.reprezentare;
		}
		return rezultat;
	}
	
	public void generateRookMoves(boolean side) throws CloneNotSupportedException
	{
		BoardState board = BoardState.getInstance();
        Bitboard bitBoard;
        if (side) {
            bitBoard = (Bitboard) board.whiteRooks.clone();
        } else {
            bitBoard = (Bitboard) board.blackRooks.clone();
        }
         while (bitBoard.reprezentare != 0)
         {
	         int pozitie = popLSB(bitBoard.reprezentare);
	         bitBoard.clearBit(pozitie);
	         long atackBoard = getRookAttacks(pozitie, board.allPieces.reprezentare, side);
	         while(atackBoard != 0)
	         {
	         	int move = 0;
	         	int checker = popLSB(atackBoard);
	         	atackBoard = Bitboard.clearBit(checker, atackBoard);
	         	if(side)
	            {
	            	if(board.allBlackPieces.isBitSet(checker))
		            {
		                move = createMove(pozitie, checker, 0, 0);
		                addCaptureMove(move);
		            }
	            	else
		            {
		            	move = createMove(pozitie, checker, 0, 0);
		                addMove(move);
		            }
	            }
	         	else
	            {
	                if(board.allWhitePieces.isBitSet(checker))
		            {
		                move = createMove(pozitie, checker, 0, 0);
		                addCaptureMove(move);
		            }
	            	else
		            {
		            	move = createMove(pozitie, checker, 0, 0);
		                addMove(move);
		            }
	            }
	         }
         }
	}
	
	public void generateBishopMoves(boolean side) throws CloneNotSupportedException
	{
		BoardState board = BoardState.getInstance();
        Bitboard bitBoard;
        if (side) {
            bitBoard = (Bitboard) board.blackBishops.clone();
        } else {
            bitBoard = (Bitboard) board.blackBishops.clone();
        }
         while (bitBoard.reprezentare != 0)
         {
	         int pozitie = popLSB(bitBoard.reprezentare);
	         bitBoard.clearBit(pozitie);
	         long atackBoard = getBishopAttacks(pozitie, board.allPieces.reprezentare, side);
	         while(atackBoard != 0)
	         {
	         	int move = 0;
	         	int checker = popLSB(atackBoard);
	         	atackBoard = Bitboard.clearBit(checker, atackBoard);
	         	if(side)
	            {
	            	if(board.allBlackPieces.isBitSet(checker))
		            {
		                move = createMove(pozitie, checker, 0, 0);
		                addCaptureMove(move);
		            }
	            	else
		            {
		            	move = createMove(pozitie, checker, 0, 0);
		                addMove(move);
		            }
	            }
	         	else
	            {
	                if(board.allWhitePieces.isBitSet(checker))
		            {
		                move = createMove(pozitie, checker, 0, 0);
		                addCaptureMove(move);
		            }
	            	else
		            {
		            	move = createMove(pozitie, checker, 0, 0);
		                addMove(move);
		            }
	            }
	         }
         }
	}
	public void generateQueenMoves(boolean side) throws CloneNotSupportedException
	{
		BoardState board = BoardState.getInstance();
        Bitboard bitBoard;
        if (side) {
            bitBoard = (Bitboard) board.blackQueens.clone();
        } else {
            bitBoard = (Bitboard) board.blackQueens.clone();
        }
         while (bitBoard.reprezentare != 0)
         {
	         int pozitie = popLSB(bitBoard.reprezentare);
	         bitBoard.clearBit(pozitie);
	         long atackBoard = getQueenAtacks(pozitie, board.allPieces.reprezentare, side);
	         while(atackBoard != 0)
	         {
	         	int move = 0;
	         	int checker = popLSB(atackBoard);
	         	atackBoard = Bitboard.clearBit(checker, atackBoard);
	         	if(side)
	            {
	            	if(board.allBlackPieces.isBitSet(checker))
		            {
		                move = createMove(pozitie, checker, 0, 0);
		                addCaptureMove(move);
		            }
	            	else
		            {
		            	move = createMove(pozitie, checker, 0, 0);
		                addMove(move);
		            }
	            }
	         	else
	            {
	                if(board.allWhitePieces.isBitSet(checker))
		            {
		                move = createMove(pozitie, checker, 0, 0);
		                addCaptureMove(move);
		            }
	            	else
		            {
		            	move = createMove(pozitie, checker, 0, 0);
		                addMove(move);
		            }
	            }
	         }
         }
	}
}
