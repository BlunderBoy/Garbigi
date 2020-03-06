package com.company.Board;

import java.util.ArrayList;

public class BoardState
{
	/*
	 piese { EMPTY, wP, wN, wB, wR, wQ, wK, bP, bN, bB, bR, bQ, bK };
	 File { FILE_A, FILE_B, FILE_C, FILE_D, FILE_E, FILE_F, FILE_G, FILE_NONE };
	 Rank { RANK_1, RANK_2, RANK_3, RANK_4, RANK_5, RANK_6, RANK_7, RANK_8, RANK_NONE };
	*/
	
	int[] pieseFormat120 = new int[120];
	int[] pieseFormat64 = new int[64];
	long[] pioni = new long[3];
	int[] rege = new int[3];
	int enPassant = 0;
	
	/*
	sunt 13 posibilitati, 12 sunt piese, una e gol
	numaru de piese pentru alb, negru si ambii
	*/
	int numarDePiese[] = new int[13];
	
	/*
	pisele mari sunt tot ce nu e pion
	numaru de piese mari pentru alb, negru si ambii
	*/
	int pieseMari[] = new int[3]; //[0] pentru alb, [1] pentru negru, [2] pentru ambii
	
	/*
	rege, regina, tura
	numaru de piese majore pentru alb, negru si ambii
	*/
	int pieseMajore[] = new int[3]; //[0] pentru alb, [1] pentru negru, [2] pentru ambii
	
	/*
	tura, nebun, cal
	numaru de piese mici pentru alb, negru si ambii
	*/
	int pieseMici[] = new int[3]; //[0] pentru alb, [1] pentru negru, [2] pentru ambii
	
	/*
	 0 0 0 0
	albRege, albRegina, negruRege, negruRegina
	se vede ca un numar de 4 biti
	daca bitul e setat inseamna ca se poate
	*/
	char castlePermision[] = {0,0,0,0};
	
	//istoric de miscari, tin sho minte 10 mutari plm
	MoveHistory istoric[] = new MoveHistory[10];
	
	
	
}
