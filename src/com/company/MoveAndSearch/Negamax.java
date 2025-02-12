package com.company.MoveAndSearch;

import com.company.Board.Bitboard;
import com.company.Board.BoardCommands;
import com.company.Board.BoardState;
import com.company.Database;
import com.company.Printer;

import javax.xml.crypto.Data;
import java.util.PriorityQueue;

public class Negamax {
    public final int MIDGAME = 0;
    public final int ENDGAME = 1;

    long startTime;
    long stopTime;
    long quintesenceStartTime;
    // cand apelam negamax prima oara, o sa construim un obiect Negamax si ii dam
    // new Negamax(System.nanoTime(), *5 sec*)
    // alea *5 sec* trebuie fine tuned by us
    // TODO daca o miscare nu e "stabila", sa continue search-ul, eventual sa mai bagam un time limit field
    // ceva de genu soft limit si hard limit
    public Negamax (long stopTime) {
        this.quintesenceStartTime = System.currentTimeMillis();
        this.startTime = System.currentTimeMillis();
        this.stopTime = stopTime;
    }

    public Negamax()
    {

    }

    // alt TODO
    boolean gameOver() {
        return false;
    }
    // NU TE ATINGE DE CASTLING PERMS!!!!!!!!!!!!!
    public static void undoMove(BoardState board, Move move, boolean side) {
        if (move.flag == 1) { // promotie
            if (side == Database.getInstance().WHITE) {
                board.whiteBitboards[move.promotie].clearBit(move.destinatie);
                board.whiteBitboards[move.piesa].setBit(move.sursa);

                if (move.piesaDestinatie != -1) { // a fost capturare si dupa promotie
                    board.blackBitboards[move.piesaDestinatie].setBit(move.destinatie);
                }
            } else {
                board.blackBitboards[move.promotie].clearBit(move.destinatie);
                board.blackBitboards[move.piesa].setBit(move.sursa);

                if (move.piesaDestinatie != -1) { // a fost capturare si dupa promotie
                    board.whiteBitboards[move.piesaDestinatie].setBit(move.destinatie);
                }
            }
            board.updateBitboards();
            return;
        }

        if (move.flag == 2) {
            board.enPassant = -1;
        }

        /*if (move.piesa == 3) { // daca se muta tura
            if (move.sursa == 7) {
                board.castlePermission[1] = 0;
            }
            if (move.sursa == 63) {
                board.castlePermission[3] = 0;
            }
            if (move.sursa == 0) {
                board.castlePermission[0] = 0;
            }
            if (move.sursa == 56) {
                board.castlePermission[2] = 0;
            }
        }*/
        if (move.flag == 3) {
            if (move.sursa == 3) { // white king castling
                if (move.destinatie == 1) { // white king side
                    // sursa si dest la king se updateaza la final
                    // eu trb sa updatez turele
                    board.whiteRooks.setBit(0);
                    board.whiteRooks.clearBit(2);
                }
                if (move.destinatie == 5) { // queen side
                    board.whiteRooks.setBit(7);
                    board.whiteRooks.clearBit(4);
                }
            }
            if (move.sursa == 59) {
                if (move.destinatie == 57) { // black king side
                    board.blackRooks.setBit(56);
                    board.blackRooks.clearBit(58);
                }
                if (move.destinatie == 61) { // queen side
                    board.blackRooks.setBit(63);
                    board.blackRooks.clearBit(60);
                }
            }
            //board.updateBitboards();
            //return;
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
    // NU TE ATINGE DE CASTLING PERMS!!!!!!!!!!!!!
    public static void applyMove(BoardState board, Move move, boolean side) {
        board = BoardState.getInstance();
        int source = move.getSursa();
        int dest = move.getDestinatie();
        int flag = move.getFlag();

        // promotion
        if (flag == 1) { // desetam piesa veche si setam piesa noua
            if (side == Database.getInstance().WHITE) {
                //board.whiteBitboards[move.piesa].clearBit(source);
                board.whitePawns.clearBit(source);
                board.allWhitePieces.clearBit(source);
                board.whiteBitboards[move.getPromotie()].setBit(dest);

                if (move.piesaDestinatie != -1) { // a fost capturare si dupa promotie
                    board.blackBitboards[move.piesaDestinatie].clearBit(move.destinatie);
                }
            } else {
                //board.blackBitboards[move.piesa].clearBit(source);
                board.blackPawns.clearBit(source);
                board.allBlackPieces.clearBit(source);
                board.blackBitboards[move.getPromotie()].setBit(dest);

                if (move.piesaDestinatie != -1) { // a fost capturare si dupa promotie
                    board.whiteBitboards[move.piesaDestinatie].clearBit(move.destinatie);
                }

            }
            board.updateBitboards();
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
            if (move.sursa == 3) { // white king castling
                if (move.destinatie == 1) { // white king side
                    // sursa si dest la king se updateaza la final
                    // eu trb sa updatez turele
                    //board.castlePermission[0] = 1;
                    board.whiteRooks.clearBit(0);
                    board.whiteRooks.setBit(2);
                }
                if (move.destinatie == 5) { // queen side
                    board.whiteRooks.clearBit(7);
                    board.whiteRooks.setBit(4);
                }
            }
            if (move.sursa == 59) {
                if (move.destinatie == 57) { // black king side
                    board.blackRooks.clearBit(56);
                    board.blackRooks.setBit(58);
                }
                if (move.destinatie == 61) { // queen side
                    board.blackRooks.clearBit(63);
                    board.blackRooks.setBit(60);
                }
            }
            //board.updateBitboards();
            //return;
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

    public static void iterativeDebug (BoardState board, int depth) throws CloneNotSupportedException {
        boolean side = true;
        for (int i = 1; i < depth; i++) {
            Move move = new Negamax().negamax(i,Integer.MIN_VALUE,Integer.MAX_VALUE,side,BoardState.getInstance());
            System.out.println("Best move for depth " + i + " and color " + side);
            side = !side;
            move.printMove();
        }
    }

    public Move quiescence(double alfa, double beta, BoardState currentState, int gameState, boolean side, int depth) throws CloneNotSupportedException {
        double standPat = Eval.eval(currentState, gameState, side);
        Move dummy = new Move();
        //long elapsedTime = System.currentTimeMillis() - startTime;
        if (depth == 0/* || elapsedTime > stopTime*/) {
            dummy.scor = standPat;
            return dummy;
        }

        if (standPat >= beta) {
            dummy.scor = beta;
            return dummy;
        }

        if (alfa < standPat) {
            alfa = standPat;
        }

        MoveGenerator plm = new MoveGenerator(currentState, side);
        plm.generateAllMoves(side);
        while (!plm.mutariGenerate.isEmpty()) {
            //for (Move chosenMove : plm.mutariDebugIGuess) {
            Move chosenMove = plm.mutariGenerate.poll();
            if (chosenMove.piesaDestinatie != -1) {
                int oldCastlePerm[] = new int[4];
                for (int i = 0; i < 4; i++) {
                    oldCastlePerm[i] = currentState.castlePermission[i];
                }
                applyMove(currentState, chosenMove, side);
                Move result = quiescence(-beta, -alfa, currentState, gameState, !side, depth - 1);

                undoMove(currentState, chosenMove, side);
                for (int i = 0; i < 4; i++) {
                    currentState.castlePermission[i] = oldCastlePerm[i];
                }
                result.scor = -result.scor;
                result.sursa = chosenMove.sursa;
                result.destinatie = chosenMove.destinatie;
                result.promotie = chosenMove.promotie;
                result.flag = chosenMove.flag;
                result.piesa = chosenMove.piesa;
                result.piesaDestinatie = chosenMove.piesaDestinatie;
                result.prioritate = chosenMove.prioritate;

                if (result.scor >= beta) {
                    dummy.scor = beta;
                    return dummy;
                }
                if (result.scor > alfa) {
                    alfa = result.scor;
                }
            }
        }
        dummy.scor = alfa;
        return dummy;
    }

    public Move negamax(int depth, double alfa, double beta, boolean side, BoardState currentState) throws CloneNotSupportedException {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (depth == 0 || elapsedTime > stopTime) {
            // TODO identifica midgame si endgame, ca degeaba avem psqtable pt fiecare daca nu le folosim
            Move dummy = new Move();
            dummy.scor = Eval.eval(currentState, MIDGAME, side);
            //System.out.println("reached depth 0, returning " + dummy.scor);
            //Printer.print();
            return dummy;
            //this.quintesenceStartTime = System.currentTimeMillis();
            //return quiescence(alfa, beta, currentState, MIDGAME, side, 2);
        }

        double max = Integer.MIN_VALUE;
        Move bestLocalMove = new Move();

        MoveGenerator plm = new MoveGenerator(currentState, side);
        plm.generateAllMoves(side);
        PriorityQueue<Move> moves = plm.mutariGenerate;


        while (!moves.isEmpty()) {
            Move move = moves.poll();
            if(move.sursa == Database.getInstance().lastLastMove.sursa)
            {
                if(move.destinatie == Database.getInstance().lastLastMove.destinatie)
                {
                    continue;
                }
            }
            
            int oldCastlePerm[] = new int[4];
            for (int i = 0; i < 4; i++) {
                oldCastlePerm[i] = currentState.castlePermission[i];
            }
            applyMove(currentState, move, side);
            Move result;
            //Printer.print();
            //if (counter <= conditie) {
            result = negamax(depth - 1, -beta, -alfa, !side, BoardState.getInstance());
            //System.out.println("this has score " + (-result.scor));
            //Printer.print();
            //} else {
            //result = negamax(depth/2, -beta, -alfa, !side, currentState);
            //}
            undoMove(currentState, move, side);
            for (int i = 0; i < 4; i++) {
                currentState.castlePermission[i] = oldCastlePerm[i];
            }
            //Move result = negamax(depth - 1, -beta, -alfa, !side, currentState);
            result.scor = -result.scor;
            result.sursa = move.sursa;
            result.destinatie = move.destinatie;
            result.promotie = move.promotie;
            result.flag = move.flag;
            result.piesa = move.piesa;
            result.piesaDestinatie = move.piesaDestinatie;
            result.prioritate = move.prioritate;


            //System.out.println("comparing " + result.scor);
            //result.printMove();
            //System.out.println(" with " + max + " for " + side);
            if (result.scor > max) {
                //if (Double.compare(result.scor, max) > 0) {
                max = result.scor;
                bestLocalMove = result;
                //System.out.println("returned score for best move " + max);
                //result.printMove();
                //Printer.print();
                //System.out.println();
            }

            if (max > alfa) {
                alfa = max;
            }


            if (alfa >= beta) {
                //System.out.println("!!!!!!!!!!! CUTOFF !!!!!!!!!!!!!");
                break;
            }
        }

        bestLocalMove.scor = max;

        return bestLocalMove;
    }
}