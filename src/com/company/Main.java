package com.company;
import java.util.Scanner;

// TODO: force - for the moment da
// TODO: go - I guess?

// TODO: setup cu fen, in features!!!!!!!!

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException
    {

    	Scanner in = new Scanner(System.in);

        XBoardProtocol xBoardProtocol = new XBoardProtocol();

        //xBoardProtocol.parseInput("debug");
        while (true) {
            String buffer = in.nextLine();
            if(xBoardProtocol.parseInput(buffer) != 0) {
            	break;
            }
        }
    }
}
