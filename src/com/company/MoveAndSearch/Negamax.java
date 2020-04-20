package com.company.MoveAndSearch;

import com.company.Board.BoardState;
import com.company.Database;
import com.company.Printer;

import java.util.PriorityQueue;

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
    public Negamax (long stopTime) {
        this.startTime = System.nanoTime();
        this.stopTime = stopTime;
    }

    public Negamax()
    {

    }

    // alt TODO
    boolean gameOver() {
        return false;
    }

    public static void undoMove(BoardState board, Move move, boolean side) {
        if (move.flag == 1) {
            if (side == Database.getInstance().WHITE) {
                board.whiteBitboards[move.promotie].clearBit(move.sursa);
                board.whiteBitboards[move.piesa].setBit(move.sursa);
            } else {
                board.blackBitboards[move.promotie].clearBit(move.sursa);
                board.blackBitboards[move.piesa].setBit(move.sursa);
            }
            return;
        }

        if (move.flag == 2) {
            board.enPassant = -1;
        }

        if (move.flag == 3) {
            // castling todo?
        }

        if (side == Database.getInstance().WHITE) {
            if (move.piesaDestinatie != -1) {
                board.blackBitboards[move.piesaDestinatie].setBit(move.destinatie);
            }
            board.whiteBitboards[move.piesa].clearBit(move.destinatie);
            board.whiteBitboards[move.piesa].setBit(move.sursa);
        } else {
            if (move.piesaDestinatie != -1) {
                board.whiteBitboards[move.piesaDestinatie].setBit(move.destinatie);
            }
            board.blackBitboards[move.piesa].clearBit(move.destinatie);
            board.blackBitboards[move.piesa].setBit(move.sursa);
        }

        board.updateBitboards();
    }

    public static void applyMove(BoardState board, Move move, boolean side) {
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

        // en passant set
        if (flag == 2) {
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
	        //muti regele la locul lui
	        //muti tura la locul ei
	        //desetei castelul
        }

        if (side == Database.getInstance().WHITE) {
            if (move.piesaDestinatie != -1) {
                board.blackBitboards[move.piesaDestinatie].clearBit(dest);
            }
            board.whiteBitboards[move.piesa].clearBit(source);
            board.whiteBitboards[move.piesa].setBit(dest);
        } else {
            if (move.piesaDestinatie != -1) {
                board.whiteBitboards[move.piesaDestinatie].clearBit(dest);
            }
            board.blackBitboards[move.piesa].clearBit(source);
            board.blackBitboards[move.piesa].setBit(dest);
        }

        board.updateBitboards();
    }

    public Move negamax(int depth, double alfa, double beta, boolean side, BoardState currentState) throws CloneNotSupportedException {
        long elapsedTime = System.nanoTime() - startTime;
        if (gameOver() || depth == 0/* || elapsedTime > stopTime*/) {
            // TODO identifica midgame si endgame, ca degeaba avem psqtable pt fiecare daca nu le folosim
            Move dummy = new Move();
            dummy.scor = Eval.eval(currentState, MIDGAME);
            return dummy;
        }

        double max = Integer.MIN_VALUE;
        Move bestLocalMove = new Move();

        MoveGenerator plm = new MoveGenerator(BoardState.getInstance());
        plm.generateAllMoves(side);
        PriorityQueue<Move> moves = plm.mutariGenerate;
        // TODO isKingAttacked !!!!!!!!!!!
        while (!moves.isEmpty()) {
            Move move = moves.poll();
            // apply move?
            applyMove(currentState, move, side);
            Move result;
            result = negamax(depth - 1, -beta, -alfa, !side, currentState);
            result.scor = -result.scor;
            result.sursa = move.sursa;
            result.destinatie = move.destinatie;
            result.promotie = move.promotie;
            result.flag = move.flag;
            result.piesa = move.piesa;
            result.piesaDestinatie = move.piesaDestinatie;

            Printer.print();

            System.out.println("comparing " + result.scor + " with " + max);
            if (result.scor > max) {
                max = result.scor;
                bestLocalMove = result;
                System.out.println("returned score for best move " + max);
                result.printMove();
                System.out.println();
            }

            if (max > alfa) {
                alfa = max;
            }

            if (alfa >= beta) {
                break;
            }

            undoMove(currentState, move, side);
        }

        bestLocalMove.scor = alfa;

        return bestLocalMove;
    }
}
