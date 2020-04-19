package com.company.MoveAndSearch;

import com.company.Board.BoardCommands;
import com.company.Board.BoardState;
import com.company.Database;

import java.util.ArrayList;

class Pair<F, S> {
    public F first;
    public S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }
}

public class Negamax {
    // TODO
    int eval(BoardState board) {
        return 0;
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
        if (gameOver() || depth == 0) {
            return new Move(0, eval(currentState));
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
