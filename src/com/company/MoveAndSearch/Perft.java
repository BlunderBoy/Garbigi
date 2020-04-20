package com.company.MoveAndSearch;

import com.company.Board.BoardState;
import com.company.Printer;

public class Perft
{
	boolean side = true;
	int numarNoduri = 0;
	public void test(int depth, BoardState board) throws CloneNotSupportedException
	{
		if(depth == 0)
			return;
		
		MoveGenerator generator = new MoveGenerator(board);
		generator.generateAllMoves(side);
		while (!generator.mutariGenerate.isEmpty())
		{
			Move move = generator.mutariGenerate.poll();
			numarNoduri++;
			Negamax.applyMove(board, move, side);
			Printer.print();
			side = !side;
			test(depth-1, board);
			Negamax.undoMove(board, move, side);
			Printer.print();
		}
	}
	public void timeTest(int depth, BoardState board) throws CloneNotSupportedException
	{
		long time = System.nanoTime();
		test(depth, board);
		time = System.nanoTime() - time;
		System.out.println("am vizitat " + numarNoduri + " noduri.");
		System.out.println("mi-a luat " + time + " nano (" + (double) time / 1_000_000_000 + " sec)");
	}
}
