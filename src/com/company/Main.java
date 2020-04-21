package com.company;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

// TODO: force - for the moment da
// TODO: go - I guess?

// TODO: setup cu fen, in features!!!!!!!!

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException, IOException
    {

    	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        XBoardProtocol xBoardProtocol = new XBoardProtocol();

        //xBoardProtocol.parseInput("debug");
        while (true) {
            String buffer = in.readLine();
            if(xBoardProtocol.parseInput(buffer) != 0) {
            	break;
            }
        }
    }
}
