package com.company.Board;

public class MoveHistory {
	char castlePermision[] = {1,1,1,1};
	int enPassant = 0;
	long posKey = 0;
	
	/*
	o sa fie un integer care reprezinta in bitboards cum arata inainte
	de ultimul move boardul
	daca vreau sa nu ma repet trebuie sa verific nu ultima
	ci penultima mutare facuta
	*/
	long OldBoard[];
}
