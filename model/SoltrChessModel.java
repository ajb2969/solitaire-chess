package model;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.Observable;

/**
 * Created by alexbrown on 11/11/16.
 */
public class SoltrChessModel extends Observable{
    private static final int BOARD_SIZE = 4;
    private static final int NUM_CARDS = BOARD_SIZE * BOARD_SIZE;
    private static String [][]board;
    private static String currFile;

    public SoltrChessModel(String fileName){
        board = makeBoard(fileName);
    }



    public String[][] makeBoard(String fileName){
        board = new String [BOARD_SIZE][BOARD_SIZE];
        try{
            currFile = fileName;
            Scanner input = new Scanner(new File(fileName));
            for(int i = 0; i< BOARD_SIZE; i++){
                String currLine = input.nextLine();
                String[] lineSplit = currLine.split("\\s+");
                for(int j = 0; j < lineSplit.length; j++){
                    if(lineSplit[j].equals("-")){board[i][j] = "-";}
                    else if(lineSplit[j].equals("P")){board[i][j] = "P";}
                    else if(lineSplit[j].equals("B")){board[i][j] = "B";}
                    else if(lineSplit[j].equals("K")){board[i][j] = "K";}
                    else if(lineSplit[j].equals("N")){board[i][j] = "N";}
                    else if(lineSplit[j].equals("R")){board[i][j] = "R";}
                    else if(lineSplit[j].equals("Q")){board[i][j] = "Q";}
                    else{
                        System.out.println("File is Invalid");
                        System.exit(1);
                    }

                }

            }
        }
        catch(FileNotFoundException e){
            System.out.println("File could not be found");
            System.exit(1);
        }

        return board;
    }

    public void printBoard(){
        for(int i = 0; i<BOARD_SIZE; i++){
            for(int j = 0; j<BOARD_SIZE; j++){
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void evaluateMove(int sR,int sC,int dR,int dC){
        if(board[sR][sC].contains("-")){
            System.out.println("Invalid source element");
            System.exit(1);
        }
        if(board[sR][sC].contains("B")){

        }
        else if(board[sR][sC].contains("K")){

        }
        else if(board[sR][sC].contains("N")){

        }
        else if(board[sR][sC].contains("P")){

        }
        else if(board[sR][sC].contains("R")){

        }
        else{ //contains Q

        }

    }
    public void evaluate(String choice){
        Scanner input = new Scanner(System.in);
        if(choice.equals("move")){
            System.out.print("Enter the source row: ");
            int sourceRow = input.nextInt();
            System.out.print("\nEnter the source col: ");
            int sourceCol = input.nextInt();
            System.out.print("\nEnter the dest row: ");
            int destRow = input.nextInt();
            System.out.print("\nEnter the dest col: ");
            int destCol = input.nextInt();
            evaluateMove(sourceRow,sourceCol,destRow,destCol);

        }
        else if(choice.equals("new")){
            System.out.print("Enter file name: ");
            String newFileName = input.nextLine();
            newFileName = "data/"+newFileName;
            makeBoard(newFileName);
            announce(null);
        }
        else if(choice.equals("restart")){
            makeBoard(currFile);
            announce(null);
        }
        else if(choice.equals("hint")){}
        else if(choice.equals("solve")){}
        else if(choice.equals("quit")){System.out.println("The game has been quit"); System.exit(0);}
        else{
            System.out.println("Invalid Input value");
            System.exit(1);
        }

    }

    private void announce(String arg) {
        setChanged();
        notifyObservers(arg);
    }



}
