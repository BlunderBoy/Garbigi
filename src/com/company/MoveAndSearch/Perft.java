package com.company.MoveAndSearch;

import com.company.Board.BoardState;
import com.company.Printer;

import java.text.NumberFormat;

public class Perft
{
	
	long numarNoduri;
	public void test(int depth, BoardState board, boolean side) throws CloneNotSupportedException
	{
		if(depth == 0)
			return;
			
		MoveGenerator generator = new MoveGenerator(board, side);
		generator.generateAllMoves(side);
		while (!generator.mutariGenerate.isEmpty())
		{
			Move move = generator.mutariGenerate.poll();
			numarNoduri++;
			Negamax.applyMove(board, move, side);
			test(depth-1, board, !side);
			Negamax.undoMove(board, move, side);
		}
	}
	public void timeTest(int depth, BoardState board) throws CloneNotSupportedException
	{
		long time = System.nanoTime();
		NumberFormat format = NumberFormat.getInstance();
		test(depth, board, true);
		time = System.nanoTime() - time;
		double trecut = (double)time / 1_000_000_000;
		System.out.println("am vizitat " + format.format(numarNoduri) + " noduri.");
		System.out.println("mi-a luat " + format.format(time) + " nano (" + trecut + " sec)");
		System.out.println("noduri pe sec: " + format.format((int)((numarNoduri)/trecut)));
	}
}
