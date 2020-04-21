package com.company.Board;

public class PSqTable
{
	public static final int pawnValues[] = {
			0, 0, 0, 0, 0, 0, 0, 0,
			10, 10, 0, -10, -10, 0, 10, 10,
			5, 0, 0, 10, 10, 0, 0, 5,
			0, 0, 10, 25, 25, 10, 0, 0,
			5, 5, 5, 10, 10, 5, 5, 5,
			10, 10, 10, 20, 20, 10, 10, 10,
			20, 20, 20, 30, 30, 20, 20, 20,
			0, 0, 0, 0, 0, 0, 0, 0
	};
	
	public static final int knightValues[] = {
			0,-10, 0, 0, 0, 0,-10, 0,
			0, 0, 0, 5, 5, 0, 0, 0,
			-5, 0, 10, 10, 10, 10, 0, -5,
			0, 0, 10, 20, 20, 10, 5, 0,
			5, 10, 15, 20, 20, 15, 10, 5,
			5, 10, 10, 20, 20, 10, 10, 5,
			0, 0, 5, 10, 10, 5, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0
	};
	
	public static final int bishopValues[] = {
			0, 0, -10, 0, 0, -10, 0, 0,
			0, 0, 0, 10, 10, 0, 0, 0,
			0, 0, 10, 15, 15, 10, 0, 0,
			0, 10, 15, 20, 20, 15, 10, 0,
			0, 10, 15, 20, 20, 15, 10, 0,
			0, 0, 10, 15, 15, 10, 0, 0,
			0, 0, 0, 10, 10, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0
	};
	
	public static final int rookValues[] = {
			0, 0, 5, 10, 10, 5, 0, 0,
			0, 0, 5, 10, 10, 5, 0, 0,
			0, 0, 5, 10, 10, 5, 0, 0,
			0, 0, 5, 10, 10, 5, 0, 0,
			0, 0, 5, 10, 10, 5, 0, 0,
			0, 0, 5, 10, 10, 5, 0, 0,
			25, 25, 25, 25, 25, 25, 25, 25,
			0, 0, 5, 10, 10, 5, 0, 0
	};
	
	public static final int index[] = {
			63, 62, 61, 60, 59, 58, 57, 56,
			55, 54, 53, 52, 51, 50, 49, 48,
			47, 46, 45, 44, 43, 42, 41, 40,
			39, 38, 37, 36, 35, 34, 33, 32,
			31, 30, 29, 28, 27, 26, 25, 24,
			23, 22, 21, 20, 19, 18, 17, 16,
			15, 14, 13, 12, 11, 10, 9, 8, 7,
			6,  5,  4,  3,  2,  1,  0,};
	
	public static final int queenValues[] = {
			-20, -10, -10, -5, -5, -10, -10, -20,
			-10, 0, 5, 0, 0, 0, 0, -10,
			-10, 0, 5, 5, 5, 5, 0, -10,
			-5, 0, 5, 5, 5, 5, 0, -5,
			0, 0, 5, 5, 5, 5, 0, -5,
			-10, 5, 5, 5, 5, 5, 0, -10,
			-10, 0, 5, 0, 0, 0, 0, -10,
			-20, -10, -10, -5, -5, -10, -10, -20
	};
	
	public static final int kingValues[] = {
			20, 30, 10, 0, 0, 10, 30, 20,
			20, 20, 0, 0, 0, 0, 20, 20,
			-10, -20, -20, -20, -20, -20, -20, -10,
			-20, -30, -30, -40, -40, -30, -30, -20,
			-30, -40, -40, -50, -50, -40, -40, -30,
			-30, -40, -40, -50, -50, -40, -40, -30,
			-30, -40, -40, -50, -50, -40, -40, -30,
			-30, -40, -40, -50, -50, -40, -40, -30,
	};
	
	
	public static final int[][] pieceSquareTables = {pawnValues, knightValues, bishopValues,
			rookValues, queenValues, kingValues};
	
}
