package com.company.MoveAndSearch;

import com.company.*;
import com.company.Board.Bitboard;
import com.company.Board.BoardState;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;

public class MoveGenerator {
    public ArrayList<Move> mutariGenerate;

    //helpere
    HashMap<Integer, Integer> getrank;
    //using LITERAL FUCKING MAGIC dam pop la primul bit si ii aflam pozitia in O(FUCKING 1)
    //nu chiar magic, folosim un magic number dupa ideea lui De Brujin de pop lsb
    final int[] BitTable = {
            63, 30, 3, 32, 25, 41, 22, 33, 15, 50, 42, 13, 11, 53, 19, 34, 61, 29, 2,
            51, 21, 43, 45, 10, 18, 47, 1, 54, 9, 57, 0, 35, 62, 31, 40, 4, 49, 5, 52,
            26, 60, 6, 23, 44, 46, 27, 56, 16, 7, 39, 48, 24, 59, 14, 12, 55, 38, 28,
            58, 20, 37, 17, 36, 8};

    int popLSB(long bitboard) {
        long b = bitboard ^ (bitboard - 1);
        int fold = (int) ((b) ^ (b >>> 32));
        return BitTable[(int) ((fold * 0x783a9b23) >>> 26)];
    }

    public MoveGenerator() {
        //in hashpmap o sa am [0,1,2,3,4,5,6,7] cu cheia 1
        //[8,9,10,11,12,13,14,15] cu cheia 2 etc
        //ca sa pot lua usor rank-ul dupa pozitie
        mutariGenerate = new ArrayList<>();
        getrank = new HashMap<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                getrank.put(j * (i + 1), i + 1);
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
    public void generatePawnMoves(boolean side) {
        BoardState board = BoardState.getInstance();
        Bitboard bitBoard;
        if (side == true) {
            bitBoard = board.WhitePawns;
        } else {
            bitBoard = board.BlackPawns;
        }

        while (bitBoard.reprezentare != 0) {
            //extrag locul unde se afla piesa
            int pozitie = popLSB(bitBoard.reprezentare);
            bitBoard.clearBit(pozitie);
            int move = 0;
            int negruDreapta = Database.conversie64la120(pozitie - 9);
            int negruStanga = Database.conversie64la120(pozitie - 7);
            int albStanga = Database.conversie64la120(pozitie + 9);
            int albDreapta = Database.conversie64la120(pozitie + 7);

            //TODO : daca facand pop regele ar fi in sah inseamna ca misacarea nu e valida, ca sa nu tot verific

            //daca suntem pe alb
            if (side == true) {
                if (getRank(pozitie) == 7) { //daca suntem pe penultimul rank inseamna ca pot sa si fac promotie
                    //setez flag de promotie
                    if (!board.AllPieces.isBitSet(pozitie + 8)) {
                        //adaug miscare pion alb in fata daca regele nu e in sah
                        for (int i = 0; i < 4; i++) {
                            move = createMove(pozitie, pozitie + 8, i, 1);
                            addMove(move);
                        }
                    }

                    if (albDreapta != 65 && !board.AllWhitePieces.isBitSet(Database.conversie120la64(albDreapta))) {
                        //adaug miscare pion alb in dreapta daca regele nu e in sah
                        if (board.AllBlackPieces.isBitSet(Database.conversie120la64(albDreapta))) {
                            for (int i = 0; i < 4; i++) {
                                move = createMove(pozitie, pozitie + 7, i, 1);
                                addCaptureMove(move);
                            }
                        }
                    }
                    if (albStanga != 65 && !board.AllWhitePieces.isBitSet(Database.conversie120la64(albStanga))) {
                        //adaug miscare pion alb in dreapta daca regele nu e in sah
                        if (board.AllBlackPieces.isBitSet(Database.conversie120la64(albStanga))) {
                            for (int i = 0; i < 4; i++) {
                                move = createMove(pozitie, pozitie + 9, i, 1);
                                addCaptureMove(move);
                            }
                        }
                    }
                } else {
                    if (!board.AllPieces.isBitSet(pozitie + 8)) {
                        move = createMove(pozitie, pozitie + 8, 0, 0);
                        addMove(move);
                    }
                    if (albDreapta != 65 && !board.AllWhitePieces.isBitSet(Database.conversie120la64(albDreapta))) {
                        if (board.AllBlackPieces.isBitSet(Database.conversie120la64(albDreapta))) {
                            move = createMove(pozitie, pozitie + 7, 0, 0);
                            addCaptureMove(move);
                        }
                    }
                    if (albStanga != 65 && !board.AllWhitePieces.isBitSet(Database.conversie120la64(albStanga))) {
                        if (board.AllBlackPieces.isBitSet(Database.conversie120la64(albStanga))) {
                            move = createMove(pozitie, pozitie + 9, 0, 0);
                            addCaptureMove(move);
                        }
                    }
                }
            } else { //negru ghici
                if (getRank(pozitie) == 2) { //daca suntem pe penultimul rank inseamna ca pot sa si fac promotie
                    //setez flag de promotie
                    if (!board.AllPieces.isBitSet(pozitie - 8)) {
                        //adaug miscare pion negru in fata daca regele nu e in sah
                        for (int i = 0; i < 4; i++) {
                            move = createMove(pozitie, pozitie - 8, i, 1);
                            addMove(move);
                        }
                    }
                    if (negruDreapta != 65 && !board.AllBlackPieces.isBitSet(Database.conversie120la64(negruDreapta))) {
                        //adaug miscare pion negru in dreapta daca regele nu e in sah
                        if (board.AllWhitePieces.isBitSet(Database.conversie120la64(negruDreapta))) {
                            for (int i = 0; i < 4; i++) {
                                move = createMove(pozitie, pozitie - 9, i, 1);
                                addCaptureMove(move);
                            }
                        }
                    }
                    if (negruStanga != 65 && !board.AllBlackPieces.isBitSet(Database.conversie120la64(negruStanga))) {
                        //adaug miscare pion negru in dreapta daca regele nu e in sah
                        if (board.AllWhitePieces.isBitSet(Database.conversie120la64(negruStanga))) {
                            for (int i = 0; i < 4; i++) {
                                move = createMove(pozitie, pozitie - 7, i, 1);
                                addCaptureMove(move);
                            }
                        }
                    }
                } else {
                    if (!board.AllPieces.isBitSet(pozitie - 8)) {
                        move = createMove(pozitie, pozitie - 8, 0, 0);
                        addMove(move);
                    }
                    if (negruDreapta != 65 && !board.AllBlackPieces.isBitSet(Database.conversie120la64(negruDreapta))) {
                        if (board.AllWhitePieces.isBitSet(Database.conversie120la64(negruDreapta))) {
                            move = createMove(pozitie, pozitie - 9, 0, 0);
                            addCaptureMove(move);
                        }
                    }
                    if (negruStanga != 65 && !board.AllBlackPieces.isBitSet(Database.conversie120la64(negruStanga))) {
                        if (board.AllWhitePieces.isBitSet(Database.conversie120la64(negruStanga))) {
                            move = createMove(pozitie, pozitie - 7, 0, 0);
                            addCaptureMove(move);
                        }
                    }
                }
            }
        }
    }
}
