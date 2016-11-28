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
    private String [][]board;
    private static String currFile;
    private Backtracker obj = new Backtracker();
    private String [][] previousBoard = new String[BOARD_SIZE][BOARD_SIZE];


    public SoltrChessModel(String fileName){
        board = makeBoard(fileName);
    }
    public int getNumChar(String [][] b){
        int chr = 0;
        for(int i = 0; i<BOARD_SIZE; i++){
            for(int j = 0; j<BOARD_SIZE; j++){
              if(!(b[i][j].equals("-"))){chr++;}
            }
        }
        return chr;
    }

    public SoltrChessModel(SoltrChessModel copy){
        board = new String [BOARD_SIZE][BOARD_SIZE];
        this.currFile = copy.currFile;
        for(int row = 0; row<BOARD_SIZE; row++){
            System.arraycopy(copy.board[row],0, this.board[row], 0, BOARD_SIZE);
            System.arraycopy(copy.board[row],0, previousBoard[row],0,BOARD_SIZE);
        }
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
        System.out.println(toString());
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
        else if(choice.equals("hint"))//uses backtracking
        {
            List<Configuration> a = obj.solveWithPath(this);
            if(a != null){
                a.remove(0);
                System.out.println("Hint:");
                System.out.println(a.get(0).toString());
                String [][] b = ((SoltrChessModel)a.get(0)).board;
                for(int i = 0; i<BOARD_SIZE; i++){
                    System.arraycopy(b[i],0,board[i],0, BOARD_SIZE);
                }
                if(getNumChar(board) == 1){
                    System.out.println("You've won, Congratulations");
                }
            }
            else{
                System.out.println("There is no hint");
            }
        }
        else if(choice.equals("solve"))//uses backtracking
        {
            List<Configuration> a = obj.solveWithPath(this);
            if(a != null){
                for(int i =0; i<a.size();i++){
                    System.out.println("Step " + (i+1) + ": ");
                    System.out.println(a.get(i).toString());
                }
                System.out.println("You won, Congratulations!");

            }
            else{
                System.out.println("There is no solution");
            }
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
                if(!(board[i][j].equals("-"))){
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
            else if(piecesOnBoard.get(c).equals("K")){
                ArrayList<Configuration> possibilities = getPossibleConfigsKing(c.getX(),c.getY());
                for(int i = 0; i<possibilities.size(); i++){possibleConfigs.add(possibilities.get(i));}
            }
            else if(piecesOnBoard.get(c).equals("Q")){
                ArrayList<Configuration> possibilities = getPossibleConfigsQueen(c.getX(),c.getY());
                for(int i = 0; i<possibilities.size(); i++){possibleConfigs.add(possibilities.get(i));}
            }
            else{//Rook
                    ArrayList<Configuration> possibilities = getPossibleConfigsRook(c.getX(),c.getY());
                    for(int i = 0; i<possibilities.size(); i++){possibleConfigs.add(possibilities.get(i));}
            }






        }

        return possibleConfigs;
    }



    public ArrayList<Configuration> getPossibleConfigsKing(int x, int y){
        ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
        ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
        ArrayList<Configuration> possibleConfigs = new ArrayList<Configuration>();
        allMovePossibilities.add(new Coordinates(x+1,y));
        allMovePossibilities.add(new Coordinates(x-1,y));
        allMovePossibilities.add(new Coordinates(x,y+1));
        allMovePossibilities.add(new Coordinates(x,y-1));
        allMovePossibilities.add(new Coordinates(x+1,y-1));
        allMovePossibilities.add(new Coordinates(x-1,y-1));
        allMovePossibilities.add(new Coordinates(x+1,y+1));
        allMovePossibilities.add(new Coordinates(x-1,y+1));

        for(int i = 0; i < allMovePossibilities.size(); i++){
            Coordinates currObject = allMovePossibilities.get(i);
            if(!((currObject.getX() < 0)||(currObject.getY() < 0)||(currObject.getX() >= BOARD_SIZE)||(currObject.getY() >= BOARD_SIZE))){
                validPossibilities.add(currObject);
            }
        }

        for(int i = 0; i<validPossibilities.size(); i++){//calls copy constructor, gets that coordinate and moves the piece, adds the configuration
            SoltrChessModel curr = new SoltrChessModel(this);
            Coordinates thisCoor = validPossibilities.get(i);
            curr.board[x][y]= "-";
            curr.board[thisCoor.getX()][thisCoor.getY()] = "K";
            possibleConfigs.add(curr);
        }
        return possibleConfigs;
    }

    public ArrayList<Configuration> getPossibleConfigsQueen(int x, int y){
        ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
        ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
        ArrayList<Configuration> possibleConfigs = new ArrayList<Configuration>();
        for(int i = 1; i<BOARD_SIZE; i++){
            allMovePossibilities.add(new Coordinates(x,y+i));
            allMovePossibilities.add(new Coordinates(x,y-i));
            allMovePossibilities.add(new Coordinates(x+i,y));
            allMovePossibilities.add(new Coordinates(x-i,y));
            allMovePossibilities.add(new Coordinates(x+i,y+i));
            allMovePossibilities.add(new Coordinates(x+i,y-i));
            allMovePossibilities.add(new Coordinates(x-i,y+i));
            allMovePossibilities.add(new Coordinates(x-i,y-i));
        }
        for(int i=0; i<allMovePossibilities.size(); i++){
            Coordinates currObject = allMovePossibilities.get(i);
            if(((currObject.getX() >= 0) && currObject.getX()<BOARD_SIZE) && (currObject.getY()>=0 && currObject.getY()<BOARD_SIZE)){
                validPossibilities.add(currObject);
            }
        }
        for(int i = 0; i<validPossibilities.size(); i++){//calls copy constructor, gets that coordinate and moves the piece, adds the configuration
            SoltrChessModel curr = new SoltrChessModel(this);
            Coordinates thisCoor = validPossibilities.get(i);
            curr.board[x][y]= "-";
            curr.board[thisCoor.getX()][thisCoor.getY()] = "Q";
            possibleConfigs.add(curr);
        }
        return possibleConfigs;
    }

    public ArrayList<Configuration> getPossibleConfigsRook(int x, int y){
        ArrayList<Coordinates> allMovePossibilities = new ArrayList<Coordinates>();
        ArrayList<Coordinates> validPossibilities = new ArrayList<Coordinates>();
        ArrayList<Configuration> possibleConfigs = new ArrayList<Configuration>();
        for(int i = 1; i<BOARD_SIZE; i++){
            allMovePossibilities.add(new Coordinates(x, y+i));
            allMovePossibilities.add(new Coordinates(x, y-i));
            allMovePossibilities.add(new Coordinates(x+i, y));
            allMovePossibilities.add(new Coordinates(x-i, y));
        }
        for(int i=0; i<allMovePossibilities.size(); i++){
            Coordinates currObject = allMovePossibilities.get(i);
            if(((currObject.getX() >= 0) && currObject.getX()<BOARD_SIZE) && (currObject.getY()>=0 && currObject.getY()<BOARD_SIZE)){
                validPossibilities.add(currObject);
            }
        }
        for(int i = 0; i<validPossibilities.size(); i++){//calls copy constructor, gets that coordinate and moves the piece, adds the configuration
            SoltrChessModel curr = new SoltrChessModel(this);
            Coordinates thisCoor = validPossibilities.get(i);
            curr.board[x][y]= "-";
            curr.board[thisCoor.getX()][thisCoor.getY()] = "R";
            possibleConfigs.add(curr);
        }
        return possibleConfigs;
    }










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
            if((!((currObject.getX() < 0)||(currObject.getY() < 0)||(currObject.getX() >= BOARD_SIZE)||(currObject.getY() >= BOARD_SIZE)))){
                validPossibilities.add(currObject);
            }
        }
        for(int i = 0; i<validPossibilities.size(); i++){//calls copy constructor, gets that coordinate and moves the piece, adds the configuration
            SoltrChessModel curr = new SoltrChessModel(this);
            Coordinates thisCoor = validPossibilities.get(i);
            curr.board[x][y]= "-";
            curr.board[thisCoor.getX()][thisCoor.getY()] = "B";
            possibleConfigs.add(curr);
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
            if(((currObject.getX() >= 0) && currObject.getX()<BOARD_SIZE) && (currObject.getY()>=0 && currObject.getY()<BOARD_SIZE)){
                validPossibilities.add(currObject);
            }
        }

        for(int i = 0; i<validPossibilities.size(); i++){//calls copy constructor, gets that coordinate and moves the piece, adds the configuration
            SoltrChessModel curr = new SoltrChessModel(this);
            Coordinates thisCoor = validPossibilities.get(i);
            curr.board[x][y] = "-";
            curr.board[thisCoor.getX()][thisCoor.getY()] = "P";
            possibleConfigs.add(curr);
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
            if(((currObject.getX() >= 0) && currObject.getX()<BOARD_SIZE) && (currObject.getY()>=0 && currObject.getY()<BOARD_SIZE)){
                validPossibilities.add(currObject);
            }

        }
        for(int i = 0; i<validPossibilities.size(); i++){//calls copy constructor, gets that coordinate and moves the piece, adds the configuration
            SoltrChessModel curr = new SoltrChessModel(this);
            Coordinates thisCoor = validPossibilities.get(i);
            curr.board[x][y] = "-";
            curr.board[thisCoor.getX()][thisCoor.getY()] = "N";
            possibleConfigs.add(curr);
        }
        return possibleConfigs;
    }


    @Override
    public boolean isValid() {
        if(getNumChar(previousBoard) - getNumChar(board) == 1){
            return true;
        }
        return false;
    }

    @Override
    public boolean isGoal() {
        int numberOfElements = getNumChar(this.board);
        if(numberOfElements == 1){return true;}
        else{return false;}
    }

    public String toString(){
        String output = "";
        for(int i =0; i<board.length;i++){
            for(int j = 0; j<board.length; j++){
                output += board[i][j];
            }
            output += "\n";
        }
        return output;
    }
}
