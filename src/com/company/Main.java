package com.company;
import java.util.Scanner;

// TODO: xboard
// TODO: new
// TODO: force - for the moment da
// TODO: go - I guess?
// TODO: white - I guess?
// TODO: black - I guess?
// TODO: quit - atm da
// TODO: resign
// TODO: move

public class Main {
    public static void main(String[] args)  {

    	Scanner in = new Scanner(System.in);
        XBoardProtocol xBoardProtocol = new XBoardProtocol();

        while (true) {
            String buffer = in.nextLine();
            if(xBoardProtocol.parseInput(buffer) != 0)
            {
            	break;
            }
        }
    }
}
