package model;
import backtracking.Backtracker;
import backtracking.Configuration;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import jdk.nashorn.internal.runtime.regexp.joni.ScanEnvironment;

import java.io.*;
import java.util.*;
import java.util.Observable;

/**
 * Created by alexbrown on 11/11/16.
 */
public class SoltrChessModel extends Observable implements Configuration{
    private static final int BOARD_SIZE = 4;
    private static String [][]board;
    private static String currFile;
    private Backtracker obj = new Backtracker();
    private static ArrayList<Integer> numChar = new ArrayList<Integer>();


    public String[][] getBoard(){return board;}

    public SoltrChessModel(String fileName){
        board = makeBoard(fileName);
        numChar.add(getNumChar(board));
    }
    public int getNumChar(String [][] b){
        int chr = 0;
        for(int i = 0; i<BOARD_SIZE; i++){
            for(int j = 0; j<BOARD_SIZE; j++){
              if(b[i][j]!= "-"){chr++;}
            }
        }
        return chr;
    }

    public SoltrChessModel(SoltrChessModel copy){
        String [][] copyBoard = copy.getBoard();
        String [][] thisBoard = this.getBoard();
        this.currFile = copy.currFile;
        for(int row = 0; row<BOARD_SIZE; row++){System.arraycopy(copyBoard[row],0, thisBoard[row], 0, BOARD_SIZE);}
        this.numChar = new ArrayList<>(copy.numChar);
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

    public void printBoard(String [][] b){
        for(int i = 0; i<BOARD_SIZE; i++){
            for(int j = 0; j<BOARD_SIZE; j++){
                System.out.print(b[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void evaluateMove(int sR,int sC,int dR,int dC) {
        if(board[sR][sC].contains("-")){
            System.out.println("Invalid source element");
            evaluate("move");
        }
        if(board[dR][dC].contains("-")){
            System.out.println("Invalid Destination element");
            evaluate("move");
        }
        if(board[sR][sC].contains("B")){ //Bishop - finished
            ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            for(int i = 1; i<BOARD_SIZE; i++){
                allMovePossibilities.add(new Coordinates(sR+i,sC+i));
                allMovePossibilities.add(new Coordinates(sR+i,sC-i));
                allMovePossibilities.add(new Coordinates(sR-i,sC+i));
                allMovePossibilities.add(new Coordinates(sR-i,sC-i));
            }
            for(int i = 0; i < allMovePossibilities.size(); i++){
                Coordinates currObject = allMovePossibilities.get(i);
                if(!((currObject.getX() < 0)||(currObject.getY() < 0)||(currObject.getX() >= BOARD_SIZE)||(currObject.getY() >= BOARD_SIZE))){
                    validPossibilities.add(currObject);
                }
            }
            for(Coordinates c: validPossibilities){
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))){
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                }
            }
            announce(null);

        }
        else if(board[sR][sC].contains("K")){ //King -- finished
            ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            allMovePossibilities.add(new Coordinates(sR+1,sC));
            allMovePossibilities.add(new Coordinates(sR-1,sC));
            allMovePossibilities.add(new Coordinates(sR,sC+1));
            allMovePossibilities.add(new Coordinates(sR,sC-1));
            allMovePossibilities.add(new Coordinates(sR+1,sC-1));
            allMovePossibilities.add(new Coordinates(sR-1,sC-1));
            allMovePossibilities.add(new Coordinates(sR+1,sC+1));
            allMovePossibilities.add(new Coordinates(sR-1,sC+1));

            for(int i = 0; i < allMovePossibilities.size(); i++){
                Coordinates currObject = allMovePossibilities.get(i);
                if(!((currObject.getX() < 0)||(currObject.getY() < 0)||(currObject.getX() >= BOARD_SIZE)||(currObject.getY() >= BOARD_SIZE))){
                    validPossibilities.add(currObject);
                }
            }
            for(Coordinates c: validPossibilities){
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))){
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                }
            }
            announce(null);
        }
        else if(board[sR][sC].contains("N")){//knight - finished
            ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            allMovePossibilities.add(new Coordinates(sR + 2,sC - 1));
            allMovePossibilities.add(new Coordinates(sR + 2,sC + 1));
            allMovePossibilities.add(new Coordinates(sR + 1,sC - 2));
            allMovePossibilities.add(new Coordinates(sR + 1,sC + 2));
            allMovePossibilities.add(new Coordinates(sR - 2,sC + 1));
            allMovePossibilities.add(new Coordinates(sR - 2,sC - 1));
            allMovePossibilities.add(new Coordinates(sR - 1,sC + 2));
            allMovePossibilities.add(new Coordinates(sR - 1,sC - 2));
            for(int i=0; i<allMovePossibilities.size(); i++){
                Coordinates currObject = allMovePossibilities.get(i);
                if(((currObject.getX() >= 0) && currObject.getX()<BOARD_SIZE) && (currObject.getY()>=0 && currObject.getY()<BOARD_SIZE)){
                    validPossibilities.add(currObject);
                }

            }
            for(Coordinates c: validPossibilities){
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))){
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                }
            }
            announce(null);

        }
        else if(board[sR][sC].contains("P")){//Pawn - finished
            ArrayList<Coordinates> movePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            if (sR == 0 ){
                movePossibilities.add(new Coordinates(sR + 1,sC + 1));
                movePossibilities.add(new Coordinates(sR + 1,sC - 1));
            }
            else{
                movePossibilities.add(new Coordinates(sR - 1,sC + 1));
                movePossibilities.add(new Coordinates(sR - 1,sC - 1));
            }

            for(int i = 0; i<movePossibilities.size(); i++){
                Coordinates currObject = movePossibilities.get(i);
                if(((currObject.getX() >= 0) && currObject.getX()<BOARD_SIZE) && (currObject.getY()>=0 && currObject.getY()<BOARD_SIZE)){
                    validPossibilities.add(currObject);
                }
            }

            for(Coordinates c: validPossibilities){
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))){
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                }
            }

            announce(null);
        }
        else if(board[sR][sC].contains("R")){//Rook -- finished
            ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            for(int i = 1; i<BOARD_SIZE; i++){
                allMovePossibilities.add(new Coordinates(sR, sC+i));
                allMovePossibilities.add(new Coordinates(sR, sC-i));
                allMovePossibilities.add(new Coordinates(sR+i, sC));
                allMovePossibilities.add(new Coordinates(sR-i, sC));
            }
            for(int i=0; i<allMovePossibilities.size(); i++){
                Coordinates currObject = allMovePossibilities.get(i);
                if(((currObject.getX() >= 0) && currObject.getX()<BOARD_SIZE) && (currObject.getY()>=0 && currObject.getY()<BOARD_SIZE)){
                    validPossibilities.add(currObject);
                }

            }
            for(Coordinates c: validPossibilities){
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))){
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                }
            }
            announce(null);

        }
        else{ //contains Queen - finished
            ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
            ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
            for(int i = 1; i<BOARD_SIZE; i++){
                allMovePossibilities.add(new Coordinates(sR,sC+i));
                allMovePossibilities.add(new Coordinates(sR,sC-i));
                allMovePossibilities.add(new Coordinates(sR+i,sC));
                allMovePossibilities.add(new Coordinates(sR-i,sC));
                allMovePossibilities.add(new Coordinates(sR+i,sC+i));
                allMovePossibilities.add(new Coordinates(sR+i,sC-i));
                allMovePossibilities.add(new Coordinates(sR-i,sC+i));
                allMovePossibilities.add(new Coordinates(sR-i,sC-i));
            }
            for(int i=0; i<allMovePossibilities.size(); i++){
                Coordinates currObject = allMovePossibilities.get(i);
                if(((currObject.getX() >= 0) && currObject.getX()<BOARD_SIZE) && (currObject.getY()>=0 && currObject.getY()<BOARD_SIZE)){
                    validPossibilities.add(currObject);
                }

            }
            for(Coordinates c: validPossibilities){
                if ((c.getX() == dR) && (c.getY() == dC) && (!board[dR][dC].equals("-"))){
                    String symbolBeingMoved = board[sR][sC];
                    board[dR][dC] = symbolBeingMoved;
                    board[sR][sC] = "-";
                }
            }
            announce(null);

        }
    }
    public void evaluate(String choice){
        Scanner input = new Scanner(System.in);
        if(choice.equals("move")){
            System.out.print("Enter the source row: ");
            int sourceRow = input.nextInt();
            System.out.print("Enter the source col: ");
            int sourceCol = input.nextInt();
            System.out.print("Enter the dest row: ");
            int destRow = input.nextInt();
            System.out.print("Enter the dest col: ");
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
        else if(choice.equals("hint")){//uses backtracking
             }
        else if(choice.equals("solve")){//uses backtracking
            obj.solve(this);

        }
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























    @Override
    public Collection<Configuration> getSuccessors() {
        HashMap<Coordinates, String > piecesOnBoard = new HashMap<Coordinates, String >();
        ArrayList<Configuration> possibleConfigs = new ArrayList<Configuration>();
        for(int i = 0; i<BOARD_SIZE; i++){
            for(int j = 0; j<BOARD_SIZE; j++){
                if(board[i][j]!= "-"){
                    piecesOnBoard.put(new Coordinates(i,j), board[i][j]);//builds hashmap, puts each piece and it's coordinate in hashmap
                }
            }
        }
        for(Coordinates c: piecesOnBoard.keySet()){
            if(piecesOnBoard.get(c).equals("B")){
                ArrayList<Configuration> possibilities = getPossibleConfigsBishop(c.getX(),c.getY());
                for(int i = 0; i<possibilities.size(); i++){possibleConfigs.add(possibilities.get(i));}

            }
            else if(piecesOnBoard.get(c).equals("P")){
                ArrayList<Configuration> possibilities = getPossibleConfigsPawn(c.getX(),c.getY());
                for(int i = 0; i<possibilities.size(); i++){possibleConfigs.add(possibilities.get(i));}
            }
            else if(piecesOnBoard.get(c).equals("N")){
                ArrayList<Configuration> possibilities = getPossibleConfigsKnight(c.getX(),c.getY());
                for(int i = 0; i<possibilities.size(); i++){possibleConfigs.add(possibilities.get(i));}
            }
        }

        return possibleConfigs;
    }

    public void modifyBoard(int r, int c, String symbol){board[r][c] = symbol;}

    public ArrayList<Configuration> getPossibleConfigsBishop(int x, int y){
        ArrayList<Configuration> possibleConfigs = new ArrayList<Configuration>();
        ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
        ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
        for(int i = 1; i<BOARD_SIZE; i++){//radiates outwards to find all possible moves at currentPosition
            allMovePossibilities.add(new Coordinates(x+i,y+i));
            allMovePossibilities.add(new Coordinates(x+i,y-i));
            allMovePossibilities.add(new Coordinates(x-i,y+i));
            allMovePossibilities.add(new Coordinates(x-i,y-i));
        }
        for(int i = 0; i < allMovePossibilities.size(); i++){//prunes to eliminate all invalid moves
            Coordinates currObject = allMovePossibilities.get(i);
            if((!((currObject.getX() < 0)||(currObject.getY() < 0)||(currObject.getX() >= BOARD_SIZE)||(currObject.getY() >= BOARD_SIZE))) && (!board[currObject.getX()][currObject.getY()].equals("-"))){
                validPossibilities.add(currObject);
            }
        }
        for(int i = 0; i<validPossibilities.size(); i++){//calls copy constructor, gets that coordinate and moves the piece, adds the configuration
            Configuration curr = new SoltrChessModel(this);
            Coordinates thisCoor = validPossibilities.get(i);
            String [][]thisBoard = ((SoltrChessModel)curr).getBoard();
            String taken;
            if(!(thisBoard[thisCoor.getX()][thisCoor.getY()].equals("-"))){
                taken = thisBoard[thisCoor.getX()][thisCoor.getY()];
            }
            else{
                taken = "-";
            }

            ((SoltrChessModel)curr).modifyBoard(x,y,"-");
            ((SoltrChessModel)curr).modifyBoard(thisCoor.getX(), thisCoor.getY(), "B");
            possibleConfigs.add(curr);
            thisBoard = ((SoltrChessModel)curr).getBoard();
            numChar.add(getNumChar(thisBoard));
//            ((SoltrChessModel)curr).modifyBoard(x,y,"B");
//            ((SoltrChessModel)curr).modifyBoard(thisCoor.getX(), thisCoor.getY(), taken);

        }
        return possibleConfigs;
    }













    public ArrayList<Configuration> getPossibleConfigsPawn(int x, int y){
        ArrayList<Coordinates> movePossibilities = new ArrayList<Coordinates>();
        ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
        ArrayList<Configuration> possibleConfigs = new ArrayList<Configuration>();
        if (x == 0 ){
            movePossibilities.add(new Coordinates(x + 1,y + 1));
            movePossibilities.add(new Coordinates(x + 1,y - 1));
        }
        else{
            movePossibilities.add(new Coordinates(x - 1,y + 1));
            movePossibilities.add(new Coordinates(x - 1,y - 1));
        }

        for(int i = 0; i<movePossibilities.size(); i++){
            Coordinates currObject = movePossibilities.get(i);
            if(((currObject.getX() >= 0) && currObject.getX()<BOARD_SIZE) && (currObject.getY()>=0 && currObject.getY()<BOARD_SIZE)&& (!board[currObject.getX()][currObject.getY()].equals("-"))){
                validPossibilities.add(currObject);
            }
        }

        for(int i = 0; i<validPossibilities.size(); i++){//calls copy constructor, gets that coordinate and moves the piece, adds the configuration
            Configuration curr = new SoltrChessModel(this);
            Coordinates thisCoor = validPossibilities.get(i);
            String [][]thisBoard = ((SoltrChessModel)curr).getBoard();
            String taken;
            if(!(thisBoard[thisCoor.getX()][thisCoor.getY()].equals("-"))){
                taken = thisBoard[thisCoor.getX()][thisCoor.getY()];
            }
            else{
                taken = "-";
            }

            ((SoltrChessModel)curr).modifyBoard(x,y,"-");
            ((SoltrChessModel)curr).modifyBoard(thisCoor.getX(), thisCoor.getY(), "P");
            possibleConfigs.add(curr);
            thisBoard = ((SoltrChessModel)curr).getBoard();
            this.numChar.add(getNumChar(thisBoard));
//            ((SoltrChessModel)curr).modifyBoard(x,y,"P");
//            ((SoltrChessModel)curr).modifyBoard(thisCoor.getX(), thisCoor.getY(), taken);
        }
        return possibleConfigs;

    }


















    public ArrayList<Configuration> getPossibleConfigsKnight(int x, int y){
        ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
        ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
        ArrayList<Configuration> possibleConfigs = new ArrayList<Configuration>();
        allMovePossibilities.add(new Coordinates(x + 2,y - 1));
        allMovePossibilities.add(new Coordinates(x + 2,y + 1));
        allMovePossibilities.add(new Coordinates(x + 1,y - 2));
        allMovePossibilities.add(new Coordinates(x + 1,y + 2));
        allMovePossibilities.add(new Coordinates(x - 2,y + 1));
        allMovePossibilities.add(new Coordinates(x - 2,y - 1));
        allMovePossibilities.add(new Coordinates(x - 1,y + 2));
        allMovePossibilities.add(new Coordinates(x - 1,y - 2));
        for(int i=0; i<allMovePossibilities.size(); i++){
            Coordinates currObject = allMovePossibilities.get(i);
            if(((currObject.getX() >= 0) && currObject.getX()<BOARD_SIZE) && (currObject.getY()>=0 && currObject.getY()<BOARD_SIZE) && (!board[currObject.getX()][currObject.getY()].equals("-"))){
                validPossibilities.add(currObject);
            }

        }
        for(int i = 0; i<validPossibilities.size(); i++){//calls copy constructor, gets that coordinate and moves the piece, adds the configuration
            Configuration curr = new SoltrChessModel(this);
            Coordinates thisCoor = validPossibilities.get(i);
            String [][]thisBoard = ((SoltrChessModel)curr).getBoard();
            String taken;
            if(!(thisBoard[thisCoor.getX()][thisCoor.getY()].equals("-"))){
                taken = thisBoard[thisCoor.getX()][thisCoor.getY()];
            }
            else{
                taken = "-";
            }

            ((SoltrChessModel)curr).modifyBoard(x,y,"-");
            ((SoltrChessModel)curr).modifyBoard(thisCoor.getX(), thisCoor.getY(), "N");
            possibleConfigs.add(curr);
            thisBoard = ((SoltrChessModel)curr).getBoard();
            numChar.add(getNumChar(thisBoard));

//            ((SoltrChessModel)curr).modifyBoard(x,y,"N");
//            ((SoltrChessModel)curr).modifyBoard(thisCoor.getX(), thisCoor.getY(), taken);
        }
        return possibleConfigs;
    }



    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isGoal() {
        int numberOfElements = 0;
        for(int i = 0; i< BOARD_SIZE; i++){
            for(int j = 0; j<BOARD_SIZE; j++){
                if(!board[i][j].equals("-")){
                    numberOfElements++;
                }
            }
        }
        if(numberOfElements == 1){return true;}
        else{return false;}
    }
}
