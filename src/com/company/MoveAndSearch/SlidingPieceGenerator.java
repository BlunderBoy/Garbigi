package com.company.MoveAndSearch;

import com.company.Board.Bitboard;
import com.company.Database;
import com.company.Printer;

import javax.xml.crypto.Data;

/*
	Teoretic daca implementarea functioneaza ar trebui sa vedem impovement
	mai mare de dublu fata de metoda clasica.
	Magic bitboards lets go.
 */
public class SlidingPieceGenerator {
    final static long[] rookMagics = {
            0xa8002c000108020L, 0x6c00049b0002001L, 0x100200010090040L, 0x2480041000800801L, 0x280028004000800L,
            0x900410008040022L, 0x280020001001080L, 0x2880002041000080L, 0xa000800080400034L, 0x4808020004000L,
            0x2290802004801000L, 0x411000d00100020L, 0x402800800040080L, 0xb000401004208L, 0x2409000100040200L,
            0x1002100004082L, 0x22878001e24000L, 0x1090810021004010L, 0x801030040200012L, 0x500808008001000L,
            0xa08018014000880L, 0x8000808004000200L, 0x201008080010200L, 0x801020000441091L, 0x800080204005L,
            0x1040200040100048L, 0x120200402082L, 0xd14880480100080L, 0x12040280080080L, 0x100040080020080L,
            0x9020010080800200L, 0x813241200148449L, 0x491604001800080L, 0x100401000402001L, 0x4820010021001040L,
            0x400402202000812L, 0x209009005000802L, 0x810800601800400L, 0x4301083214000150L, 0x204026458e001401L,
            0x40204000808000L, 0x8001008040010020L, 0x8410820820420010L, 0x1003001000090020L, 0x804040008008080L,
            0x12000810020004L, 0x1000100200040208L, 0x430000a044020001L, 0x280009023410300L, 0xe0100040002240L,
            0x200100401700L, 0x2244100408008080L, 0x8000400801980L, 0x2000810040200L, 0x8010100228810400L,
            0x2000009044210200L, 0x4080008040102101L, 0x40002080411d01L, 0x2005524060000901L, 0x502001008400422L,
            0x489a000810200402L, 0x1004400080a13L, 0x4000011008020084L, 0x26002114058042L
    };

    final static long[] bishopMagics = {
            0x89a1121896040240L, 0x2004844802002010L, 0x2068080051921000L, 0x62880a0220200808L, 0x4042004000000L,
            0x100822020200011L, 0xc00444222012000aL, 0x28808801216001L, 0x400492088408100L, 0x201c401040c0084L,
            0x840800910a0010L, 0x82080240060L, 0x2000840504006000L, 0x30010c4108405004L, 0x1008005410080802L,
            0x8144042209100900L, 0x208081020014400L, 0x4800201208ca00L, 0xf18140408012008L, 0x1004002802102001L,
            0x841000820080811L, 0x40200200a42008L, 0x800054042000L, 0x88010400410c9000L, 0x520040470104290L,
            0x1004040051500081L, 0x2002081833080021L, 0x400c00c010142L, 0x941408200c002000L, 0x658810000806011L,
            0x188071040440a00L, 0x4800404002011c00L, 0x104442040404200L, 0x511080202091021L, 0x4022401120400L,
            0x80c0040400080120L, 0x8040010040820802L, 0x480810700020090L, 0x102008e00040242L, 0x809005202050100L,
            0x8002024220104080L, 0x431008804142000L, 0x19001802081400L, 0x200014208040080L, 0x3308082008200100L,
            0x41010500040c020L, 0x4012020c04210308L, 0x208220a202004080L, 0x111040120082000L, 0x6803040141280a00L,
            0x2101004202410000L, 0x8200000041108022L, 0x21082088000L, 0x2410204010040L, 0x40100400809000L,
            0x822088220820214L, 0x40808090012004L, 0x910224040218c9L, 0x402814422015008L, 0x90014004842410L,
            0x1000042304105L, 0x10008830412a00L, 0x2520081090008908L, 0x40102000a0a60140L
    };

    public static final int[] rookIndexBits = {
            12, 11, 11, 11, 11, 11, 11, 12,
            11, 10, 10, 10, 10, 10, 10, 11,
            11, 10, 10, 10, 10, 10, 10, 11,
            11, 10, 10, 10, 10, 10, 10, 11,
            11, 10, 10, 10, 10, 10, 10, 11,
            11, 10, 10, 10, 10, 10, 10, 11,
            11, 10, 10, 10, 10, 10, 10, 11,
            12, 11, 11, 11, 11, 11, 11, 12};

    public static final int[] bishopIndexBits = {
            6, 5, 5, 5, 5, 5, 5, 6,
            5, 5, 5, 5, 5, 5, 5, 5,
            5, 5, 7, 7, 7, 7, 5, 5,
            5, 5, 7, 9, 9, 7, 5, 5,
            5, 5, 7, 9, 9, 7, 5, 5,
            5, 5, 7, 7, 7, 7, 5, 5,
            5, 5, 5, 5, 5, 5, 5, 5,
            6, 5, 5, 5, 5, 5, 5, 6,
    };

    //date calculate de mana care au durat o viata de om
    static long RANK_1 = 0xffL;
    static long RANK_2 = 0xff00L;
    static long RANK_3 = 0xff0000L;
    static long RANK_4 = 0xff000000L;
    static long RANK_5 = 0xff00000000L;
    static long RANK_6 = 0xff0000000000L;
    static long RANK_7 = 0xff000000000000L;
    static long RANK_8 = 0xff00000000000000L;

    static long FILE_A = 0x8080808080808080L;
    static long FILE_B = 0x4040404040404040L;
    static long FILE_C = 0x2020202020202020L;
    static long FILE_D = 0x1010101010101010L;
    static long FILE_E = 0x808080808080808L;
    static long FILE_F = 0x404040404040404L;
    static long FILE_G = 0x202020202020202L;
    static long FILE_H = 0x101010101010101L;

    static long diag11 = 0x1;
    static long diag12 = 0x102;
    static long diag13 = 0x10204;
    static long diag14 = 0x1020408;
    static long diag15 = 0x102040810L;
    static long diag16 = 0x10204081020L;
    static long diag17 = 0x1020408102040L;
    static long diag18 = 0x102040810204080L;
    static long diag19 = 0x204081020408000L;
    static long diag110 = 0x408102040800000L;
    static long diag111 = 0x810204080000000L;
    static long diag112 = 0x1020408000000000L;
    static long diag113 = 0x2040800000000000L;
    static long diag114 = 0x4080000000000000L;
    static long diag115 = 0x8000000000000000L;

    static long diag21 = 0x80L;
    static long diag22 = 0x8040L;
    static long diag23 = 0x804020L;
    static long diag24 = 0x80402010L;
    static long diag25 = 0x8040201008L;
    static long diag26 = 0x804020100804L;
    static long diag27 = 0x80402010080402L;
    static long diag28 = 0x8040201008040201L;
    static long diag29 = 0x4020100804020100L;
    static long diag210 = 0x2010080402010000L;
    static long diag211 = 0x1008040201000000L;
    static long diag212 = 0x804020100000000L;
    static long diag213 = 0x402010000000000L;
    static long diag214 = 0x201000000000000L;
    static long diag215 = 0x100000000000000L;

    static long[] ranks = {RANK_1, RANK_2, RANK_3, RANK_4, RANK_5, RANK_6, RANK_7, RANK_8};
    static long[] files = {FILE_H, FILE_G, FILE_F, FILE_E, FILE_D, FILE_C, FILE_B, FILE_A};
    static long[] diagonale1 = {diag11, diag12, diag13, diag14,
            diag15, diag16, diag17, diag18,
            diag19, diag110, diag111, diag112,
            diag113, diag114, diag115};
    static long[] diagonale2 = {diag21, diag22, diag23, diag24,
            diag25, diag26, diag27, diag28,
            diag29, diag210, diag211, diag212,
            diag213, diag214, diag215};

    public static long[] rookMasks;
    public static long[] bishopMasks;
    public static long[] raysRook;
    public static long[] raysBishop;

    public SlidingPieceGenerator () {
        initRookRays();
        initBishopRays();
        initRookBishopMask();
        initRookMagicTable();
        initBishopMagicTable();
    }

    public static void initRookRays () {
        int counter = 0;
        raysRook = new long[64];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                raysRook[counter] = ranks[i] | files[j];
                counter++;
            }
        }
    }

    public static void initBishopRays () {
        raysBishop = new long[64];
        int counter = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                raysBishop[counter] = diagonale1[i + j] | diagonale2[7 - j + i];
                counter++;
            }
        }
    }

    void initRookBishopMask () {
        rookMasks = new long[64];
        bishopMasks = new long[64];
        //%8 - 1
        int[] valoriMarginaleStanga = {15, 23, 31, 39, 47, 55};
        //%8
        int[] valoriMarginaleDreapta = {8, 16, 24, 32, 40, 48};
        //jos
        int[] valoriMarginaleJos = {1, 2, 3, 4, 5, 6};
        //sus
        int[] valoriMarginaleSus = {57, 58, 59, 60, 61, 62};

        for (int i = 0; i < 64; i++) {
            rookMasks[i] = raysRook[i];
            rookMasks[i] = Bitboard.clearBit(i, raysRook[i]);
            bishopMasks[i] = raysBishop[i];
            bishopMasks[i] = Bitboard.clearBit(i, raysBishop[i]);
        }

        updateBorders(valoriMarginaleDreapta, RANK_1, RANK_8, FILE_A);
        updateBorders(valoriMarginaleStanga, RANK_1, RANK_8, FILE_H);

        updateBorders(valoriMarginaleJos, RANK_8, FILE_A, FILE_H);
        updateBorders(valoriMarginaleSus, RANK_1, FILE_A, FILE_H);

        updateCorners(rookMasks);
        updateCorners(bishopMasks);

        updateMiddle(rookMasks);
        updateMiddle(bishopMasks);
    }

    private void updateMiddle (long[] pieceMasks) {
        int[] restul = {9, 10, 11, 12, 13, 14, 17, 18, 19, 20, 21, 22, 25, 26, 27, 28, 29, 30, 33,
                34, 35, 36, 37, 38, 41, 42, 43, 44, 45, 46, 49, 50, 51, 52, 53, 54};

        for (int value : restul) {
            pieceMasks[value] &= ~RANK_1;
            pieceMasks[value] &= ~FILE_A;
            pieceMasks[value] &= ~FILE_H;
            pieceMasks[value] &= ~RANK_8;
        }
    }

    // scoate cele 2 patratele pt mastile care incep din colt
    private void updateCorners (long[] pieceMasks) {
        pieceMasks[0] &= ~RANK_8;
        pieceMasks[0] &= ~FILE_A;

        pieceMasks[7] &= ~RANK_8;
        pieceMasks[7] &= ~FILE_H;

        pieceMasks[56] &= ~RANK_1;
        pieceMasks[56] &= ~FILE_A;

        pieceMasks[63] &= ~RANK_1;
        pieceMasks[63] &= ~FILE_H;
    }
    // scoate cele 3 chestii pt mastile care incep pe margini
    private void updateBorders (int[] valoriMarginaleDreapta, long border1, long border2, long border3) {
        for (int i2 : valoriMarginaleDreapta) {
            rookMasks[i2] &= ~border1;
            rookMasks[i2] &= ~border2;
            rookMasks[i2] &= ~border3;

            bishopMasks[i2] &= ~border1;
            bishopMasks[i2] &= ~border2;
            bishopMasks[i2] &= ~border3;
        }
    }

    public static long[][] magicRookTable;
    public static long[][] magicBishopTable;

    public static long getBlockersFromIndex (long index, long mask) {
        long blockers = 0;
        long bits = Long.bitCount(mask);
        for (int i = 0; i < bits; i++) {
            int bitPos = Bitboard.popLSB(mask);
            mask = Bitboard.clearBit(bitPos, mask);
            if ((index & (1L << i)) != 0) {
                blockers |= (1L << bitPos);
            }
        }
        return blockers;
    }

    public void initRookMagicTable () {
        magicRookTable = new long[64][4096];
        for (int square = 0; square < 64; square++) {
            for (int blockerIndex = 0; blockerIndex < (1L << rookIndexBits[square]); blockerIndex++) {
                long blockers = getBlockersFromIndex(blockerIndex, rookMasks[square]);
                int key = (int)((blockers * rookMagics[square]) >>> (64 - rookIndexBits[square]));
                magicRookTable[square][key] = getRookAttacksSlow(square, blockers);
            }
        }
    }

    public void initBishopMagicTable () {
        magicBishopTable = new long[64][1024];
        for (int square = 0; square < 64; square++) {
            for (int blockerIndex = 0; blockerIndex < (1L << bishopIndexBits[square]); blockerIndex++) {
                long blockers = getBlockersFromIndex(blockerIndex, bishopMasks[square]);
                magicBishopTable[square][(int) ((blockers * bishopMagics[square]) >>> (64 - bishopIndexBits[square]))] = getBishopAttacksSlow(square, blockers);
            }
        }
    }

    public long getBishopAttacksSlow (int pozitie, long blockers) {
        //blockers e un bitboard de piese
        //pozitie e patratul pe care sunt
        long atackSet = 0;
        pozitie = Database.conversie64la120(pozitie);

        final int[] directiiNebun = new int[]{-9, -11, 11, 9};
        for (int i = 0; i < 4; i++) {
            int directie = directiiNebun[i];
            int checker = pozitie + directie;
            while (Database.conversie120la64(checker) != 65) {
                if (!Bitboard.isBitSet(Database.conversie120la64(checker), blockers)) {
                    atackSet = Bitboard.setBit(Database.conversie120la64(checker), atackSet);
                    checker += directie;
                } else {
                    atackSet = Bitboard.setBit(Database.conversie120la64(checker), atackSet);
                    break;
                }
            }
        }
        return atackSet;
    }

    private long getRookAttacksSlow (int pozitie, long blockers) {
        //blockers e un bitboard de piese
        //pozitie e patratul pe care sunt

        long atackSet = 0;
        pozitie = Database.conversie64la120(pozitie);

        final int[] directiiTura = new int[]{-1, -10, 1, 10};
        for (int i = 0; i < 4; i++) {
            int directie = directiiTura[i];
            int checker = pozitie + directie;
            while (Database.conversie120la64(checker) != 65) {
                if (!Bitboard.isBitSet(Database.conversie120la64(checker), blockers)) {
                    atackSet = Bitboard.setBit(Database.conversie120la64(checker), atackSet);
                    checker += directie;
                } else {
                    atackSet = Bitboard.setBit(Database.conversie120la64(checker), atackSet);
                    break;
                }
            }
        }
        return atackSet;
    }
}
