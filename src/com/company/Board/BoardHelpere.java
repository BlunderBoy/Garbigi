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
	
}
