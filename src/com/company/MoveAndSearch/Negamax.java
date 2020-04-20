//package com.company.MoveAndSearch;
//
//import com.company.Board.Bitboard;
//import com.company.Board.BoardCommands;
//import com.company.Board.BoardState;
//import com.company.Board.PSqTable;
//import com.company.Database;
//import com.company.Printer;
//
//import java.util.ArrayList;
//
//import static com.company.Board.Bitboard.clearBit;
//
//public class Negamax {
//    public final int MIDGAME = 0;
//    public final int ENDGAME = 1;
//
//    long startTime;
//    long stopTime;
//    // cand apelam negamax prima oara, o sa construim un obiect Negamax si ii dam
//    // new Negamax(System.nanoTime(), *5 sec*)
//    // alea *5 sec* trebuie fine tuned by us
//    // TODO daca o miscare nu e "stabila", sa continue search-ul, eventual sa mai bagam un time limit field
//    // ceva de genu soft limit si hard limit
//    public Negamax (long startTime, long stopTime) {
//        this.startTime = startTime;
//        this.stopTime = stopTime;
//    }
//
//    public static int eval(BoardState board, int gamePhase) throws CloneNotSupportedException {
//        int score = getScoreWhite(board.whitePawns.reprezentare, board.whitePawns.valoare, PSqTable.pawnValues[gamePhase]);
//        System.out.println("score for white pawns: " + score);
//        int temp = getScoreWhite(board.whiteKnights.reprezentare, board.whiteKnights.valoare, PSqTable.knightValues[gamePhase]);
//        System.out.println("score for white knights: " + temp);
//        score += temp;
//        temp = getScoreWhite(board.whiteBishops.reprezentare, board.whiteBishops.valoare, PSqTable.bishopValues[gamePhase]);
//        System.out.println("score for white bishops: " + temp);
//        score += temp;
//        temp = getScoreWhite(board.whiteRooks.reprezentare, board.whiteRooks.valoare, PSqTable.rookValues[gamePhase]);
//        System.out.println("score for white rooks: " + temp);
//        score += temp;
//        temp = getScoreWhite(board.whiteQueens.reprezentare, board.whiteQueens.valoare, PSqTable.queenValues[gamePhase]);
//        System.out.println("score for white queens: " + temp);
//        score += temp;
//        temp = getScoreWhite(board.whiteKing.reprezentare, board.whiteKing.valoare, PSqTable.kingValues[gamePhase]);
//        System.out.println("score for white king: " + temp);
//        score += temp;
//
//        temp = getScoreBlack(board.blackPawns.reprezentare, board.blackPawns.valoare, PSqTable.pawnValues[gamePhase]);
//        System.out.println("score for black pawns: " + temp);
//        score -= temp;
//        temp = getScoreBlack(board.blackKnights.reprezentare, board.blackKnights.valoare, PSqTable.knightValues[gamePhase]);
//        System.out.println("score for black knights: " + temp);
//        score -= temp;
//        temp = getScoreBlack(board.blackBishops.reprezentare, board.blackBishops.valoare, PSqTable.bishopValues[gamePhase]);
//        System.out.println("score for black bishops: " + temp);
//        score -= temp;
//        temp = getScoreBlack(board.blackRooks.reprezentare, board.blackRooks.valoare, PSqTable.rookValues[gamePhase]);
//        System.out.println("score for black rooks: " + temp);
//        score -= temp;
//        temp = getScoreBlack(board.blackQueens.reprezentare, board.blackQueens.valoare, PSqTable.queenValues[gamePhase]);
//        System.out.println("score for black queens: " + temp);
//        score -= temp;
//        temp = getScoreBlack(board.blackKing.reprezentare, board.blackKing.valoare, PSqTable.kingValues[gamePhase]);
//        System.out.println("score for black king: " + temp);
//        score -= temp;
//
//        return score;
//    }
//
//    private static int getScoreWhite (long bitboard, int valoare, int[] value) throws CloneNotSupportedException {
//        int score = 0;
//
//        while (bitboard != 0) {
//            int index = Bitboard.popLSB(bitboard);
//            bitboard = clearBit(index, bitboard);
//            score += valoare + value[63-index];
//        }
//
//        return score;
//    }
//
//    private static int getScoreBlack (long bitboard, int valoare, int[] value) throws CloneNotSupportedException {
//        int score = 0;
//
//        while (bitboard != 0) {
//            int index = Bitboard.popLSB(bitboard);
//            bitboard = clearBit(index, bitboard);
//            score += valoare + value[index];
//        }
//
//        return score;
//    }
//
//    // alt TODO
//    boolean gameOver() {
//        return false;
//    }
//
//    public static void applyMove(BoardState board, Move move, boolean side) {
//        int source = move.getSursa();
//        int dest = move.getDestinatie();
//        int flag = move.getFlag();
//
//        // promotion
//        if (flag == 1) { // desetam piesa veche si setam piesa noua
//                         // nu avem nev de dest
//            if (side == Database.getInstance().WHITE) {
//                board.whiteBitboards[move.piesa].clearBit(source);
//                board.whiteBitboards[move.getPromotie()].setBit(source);
//            } else {
//                board.blackBitboards[move.piesa].clearBit(source);
//                board.blackBitboards[move.getPromotie()].setBit(source);
//            }
//            return;
//        }
//
//        if (flag == 2) {
//            // en passant set
//            int enPassant = move.getDestinatie();
//            if (side == Database.getInstance().WHITE) {
//                enPassant = enPassant >> 8;
//            } else {
//                enPassant = enPassant << 8;
//            }
//            board.enPassant = enPassant;
//        }
//
//        if (flag == 3) {
//            // castling todo?
//	        //muti regele la locul lui
//	        //muti tura la locul ei
//	        //desetei castelul
//        }
//
//        if (side == Database.getInstance().WHITE) {
//            BoardCommands.updateBitboard(source, dest, board.whiteBitboards[move.piesa], board);
//        } else {
//            BoardCommands.updateBitboard(source, dest, board.blackBitboards[move.piesa], board);
//        }
//    }
//
//    Move negamax(int depth, int alfa, int beta, boolean side, BoardState currentState) throws CloneNotSupportedException {
//        long elapsedTime = System.nanoTime() - startTime;
//
//        if (gameOver() || depth == 0 || (elapsedTime > stopTime)) {
//            // TODO identifica midgame si endgame, ca degeaba avem psqtable pt fiecare daca nu le folosim
//            return new Move(0, eval(currentState, MIDGAME));
//        }
//
//        int max = Integer.MIN_VALUE;
//        Move bestLocalMove = new Move(0,0);
//
//        MoveGenerator plm = new MoveGenerator(BoardState.getInstance());
//        ArrayList<Move> moves = plm.mutariGenerate;
//        // TODO chiar fa clona board stateului si fa miscarile etc
//        // TODO abeta pruning
//        // TODO isKingAttacked !!!!!!!!!!!
//        for (Move move : moves) {
//            // apply move??
//            BoardState nextState = currentState.clone();
//            applyMove(nextState, move, side);
//
//            bestLocalMove = negamax(depth - 1, -beta, -alfa, !side, nextState);
//            bestLocalMove.scor = -bestLocalMove.scor;
//            bestLocalMove.mutare = move.mutare;
//
//            if (bestLocalMove.scor > max) {
//                max = bestLocalMove.scor;
//                // remember move
//            }
//
//            if (max > alfa) {
//                alfa = max;
//            }
//
//            if (alfa >= beta) {
//                break;
//            }
//
//            // undo move lmao
//            // mai trb??
//        }
//
//        bestLocalMove.scor = alfa;
//
//        return bestLocalMove;
//    }
//}
