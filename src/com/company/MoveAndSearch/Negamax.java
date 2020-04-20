package com.company.MoveAndSearch;

import com.company.Board.Bitboard;
import com.company.Board.BoardCommands;
import com.company.Board.BoardState;
import com.company.Board.PSqTable;
import com.company.Database;

import java.util.ArrayList;

public class Negamax {
    public final int MIDGAME = 0;
    public final int ENDGAME = 1;

    long startTime;
    long stopTime;
    // cand apelam negamax prima oara, o sa construim un obiect Negamax si ii dam
    // new Negamax(System.nanoTime(), *5 sec*)
    // alea *5 sec* trebuie fine tuned by us
    // TODO daca o miscare nu e "stabila", sa continue search-ul, eventual sa mai bagam un time limit field
    // ceva de genu soft limit si hard limit
    public Negamax (long startTime, long stopTime) {
        this.startTime = startTime;
        this.stopTime = stopTime;
    }
    
    int eval(BoardState board, int gamePhase) throws CloneNotSupportedException {
        int score = getScore(board.whitePawns.clone(), PSqTable.pawnValues[gamePhase]);
        score += getScore(board.whiteKnights.clone(), PSqTable.knightValues[gamePhase]);
        score += getScore(board.whiteBishops.clone(), PSqTable.bishopValues[gamePhase]);
        score += getScore(board.whiteRooks.clone(), PSqTable.rookValues[gamePhase]);
        score += getScore(board.whiteQueens.clone(), PSqTable.queenValues[gamePhase]);
        score += getScore(board.whiteKing.clone(), PSqTable.kingValues[gamePhase]);

        score -= getScore(board.blackPawns.clone(), PSqTable.pawnValues[gamePhase]);
        score -= getScore(board.blackKnights.clone(), PSqTable.knightValues[gamePhase]);
        score -= getScore(board.blackBishops.clone(), PSqTable.bishopValues[gamePhase]);
        score -= getScore(board.blackRooks.clone(), PSqTable.rookValues[gamePhase]);
        score -= getScore(board.blackQueens.clone(), PSqTable.queenValues[gamePhase]);
        score -= getScore(board.blackKing.clone(), PSqTable.kingValues[gamePhase]);

        return score;
    }

    private int getScore (Bitboard bitboard, int[] value) throws CloneNotSupportedException {
        int score = 0;

        while (bitboard.reprezentare != 0) {
            int index = Bitboard.popLSB(bitboard.reprezentare);
            bitboard.clearBit(index);
            score += bitboard.valoare + value[index];
        }

        return score;
    }

    // alt TODO
    boolean gameOver() {
        return false;
    }

    void applyMove(BoardState board, Move move, boolean side) {
        int source = move.getSursa();
        int dest = move.getDestinatie();
        int flag = move.getFlag();

        // promotion
        if (flag == 1) { // desetam piesa veche si setam piesa noua
                         // nu avem nev de dest
            if (side == Database.getInstance().WHITE) {
                board.whiteBitboards[move.piesa].clearBit(source);
                board.whiteBitboards[move.getPromotie()].setBit(source);
            } else {
                board.blackBitboards[move.piesa].clearBit(source);
                board.blackBitboards[move.getPromotie()].setBit(source);
            }
            return;
        }

        if (flag == 2) {
            // en passant set
            int enPassant = move.getDestinatie();
            if (side == Database.getInstance().WHITE) {
                enPassant = enPassant >> 8;
            } else {
                enPassant = enPassant << 8;
            }
            board.enPassant = enPassant;
        }

        if (flag == 3) {
            // castling todo?
        }

        if (side == Database.getInstance().WHITE) {
            BoardCommands.updateBitboard(source, dest, board.whiteBitboards[move.piesa], board);
        } else {
            BoardCommands.updateBitboard(source, dest, board.blackBitboards[move.piesa], board);
        }

    }

    Move negamax(int depth, int alfa, int beta, boolean side, BoardState currentState) throws CloneNotSupportedException {
        long elapsedTime = System.nanoTime() - startTime;

        if (gameOver() || depth == 0 || (elapsedTime > stopTime)) {
            // TODO identifica midgame si endgame, ca degeaba avem psqtable pt fiecare daca nu le folosim
            return new Move(0, eval(currentState, MIDGAME));
        }

        int max = Integer.MIN_VALUE;
        Move bestLocalMove = new Move(0,0);

        MoveGenerator plm = new MoveGenerator();
        ArrayList<Move> moves = plm.mutariGenerate;
        // TODO chiar fa clona board stateului si fa miscarile etc
        // TODO abeta pruning
        // TODO isKingAttacked !!!!!!!!!!!
        for (Move move : moves) {
            // apply move??
            BoardState nextState = currentState.clone();
            applyMove(nextState, move, side);

            bestLocalMove = negamax(depth - 1, -beta, -alfa, !side, nextState);
            bestLocalMove.scor = -bestLocalMove.scor;
            bestLocalMove.mutare = move.mutare;

            if (bestLocalMove.scor > max) {
                max = bestLocalMove.scor;
                // remember move
            }

            if (max > alfa) {
                alfa = max;
            }

            if (alfa >= beta) {
                break;
            }

            // undo move lmao
            // mai trb??
        }

        bestLocalMove.scor = alfa;

        return bestLocalMove;
    }
}
