package com.company.Board;

public class BoardHelpere
{
	//rank = linie, file = coloana
	//intoarce index pentru tabla de 120
	public static int conversieRFla120(int rank, int file)
	{
		return 21 + file + rank * 10;
	}
	//initializeaza cele 2 array-uri ca sa pot sa fac usor
	//conversia intre cele 2 reprezentari.
	public static int conversie120la64(int array120[], int index)
	{
		return array120[index];
	}
	
	public static void initializareArray(int array64[], int array120[])
	{
		for (int i = 0; i < 120; i++)
		{
			array120[i] = 65;
		}
		for (int i = 0; i < 64; i++)
		{
			array64[i] = 120;
		}
		
		//merge de la 0 la 63 si imi seteaza indecsi
		int index = 0;
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				int numarSecventa = conversieRFla120(i,j);
				array64[index] = numarSecventa;
				array120[numarSecventa] = index;
				index++;
			}
		}
	}
	
	//tabelul acesta si popBit scoate lsb-ul unui numar de 64 de biti si returneaza
	//indexul intr-un tabel de 64 pozitiei
	static int[] BitTable = { 63, 30, 3, 32, 25, 41, 22, 33, 15, 50, 42, 13, 11, 53, 19, 34, 61, 29, 2,
    51, 21, 43, 45, 10, 18, 47, 1, 54, 9, 57, 0, 35, 62, 31, 40, 4, 49, 5, 52,
    26, 60, 6, 23, 44, 46, 27, 56, 16, 7, 39, 48, 24, 59, 14, 12, 55, 38, 28,
    58, 20, 37, 17, 36, 8
	};

	public static int popBit(Bitboard bitboard)
	{
        long b = bitboard.reprezentare ^ (bitboard.reprezentare - 1);
        long fold = ((b & 0xffffffff) ^ (b >> 32));
        bitboard.reprezentare &= (bitboard.reprezentare - 1);
        return BitTable[(int)(fold * 0x783a9b23) >> 26];
	}

}
