package model;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.*;
import java.util.Observable;
import backtracking.*;


/**
 * Created by alexbrown on 11/11/16.
 */
public class SoltrChessModel extends Observable implements Configuration{
    private static final int BOARD_SIZE = 4;
    private int numOfChars;
    private static final int NUM_CARDS = BOARD_SIZE * BOARD_SIZE;
    private static String [][]board;
    private String[][] previousBoard;
    private static String currFile;
    private Backtracker obj = new Backtracker();
    private boolean valid;

    public SoltrChessModel(String fileName){
        currFile = fileName;
        board = makeBoard(fileName);
        obj = new Backtracker();
        numOfChars = getNumOfChar(board);
        previousBoard = makeBoard(fileName);
        valid = true;
    }
    public String[][] getBoard(){
        return board;
    }

    public String[][] getPreviousBoard(){
        return previousBoard;
    }


    public SoltrChessModel(SoltrChessModel copy){
        this.numOfChars = copy.numOfChars;
        this.currFile = copy.currFile;
        obj = new Backtracker();
        previousBoard = new String [BOARD_SIZE][BOARD_SIZE];
        for(int i = 0; i<BOARD_SIZE; i++){
            for(int j = 0; j < BOARD_SIZE; j++){
                this.board[i][j] = copy.board[i][j];
                this.previousBoard[i][j] = copy.previousBoard[i][j];
            }
        }
        valid = true;
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
        String [][] b = this.getBoard();
        for(int i = 0; i<BOARD_SIZE; i++){
            for(int j = 0; j<BOARD_SIZE; j++){
                System.out.print(b[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void evaluateMove(int sR,int sC,int dR,int dC){
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
            for(int i = 0; i<BOARD_SIZE; i++){
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
            if (sR == 0 ){
                movePossibilities.add(new Coordinates(sR - 1,sC + 1));
                movePossibilities.add(new Coordinates(sR - 1,sC - 1));
            }
            else{
                movePossibilities.add(new Coordinates(sR + 1,sC + 1));
                movePossibilities.add(new Coordinates(sR + 1,sC - 1));
            }


            for(Coordinates c: movePossibilities){
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
            for(int i = 0; i<BOARD_SIZE; i++){
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
            for(int i = 0; i<BOARD_SIZE; i++){
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
    public void evaluate(String choice){ // still have to complete hint and solve
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
            System.out.println("New Game " + newFileName);
            makeBoard(newFileName);
            announce(null);
        }
        else if(choice.equals("restart")){
            makeBoard(currFile);
            announce(null);
        }






        else if(choice.equals("hint")){ // need to write
            Stack<Configuration> solution;
            Configuration currConfig = new SoltrChessModel(this);
            solution = obj.solveWithPath(currConfig);
            Configuration nextMove = solution.pop();
            String [][] newBoard = ((SoltrChessModel)nextMove).getBoard();
            String [][] oldBoard = getBoard();
            for(int i = 0; i<BOARD_SIZE; i++){
                for(int j =0; j<BOARD_SIZE; j++){
                    oldBoard[i][j] = newBoard[i][j];
                }
            }
            ((SoltrChessModel)nextMove).printBoard();
            announce(null);

        }
        else if(choice.equals("solve")){//need to write
            Stack<Configuration> solution;
            int i = 1;
            Configuration currConfig = new SoltrChessModel(this);
            solution = obj.solve(currConfig);
            if(solution != null){
                while(!(solution.empty())){
                    Configuration step = solution.pop();
                    System.out.println("Step " + i);
                    ((SoltrChessModel)step).printBoard();
                }
            }
            else{
                System.out.println("There is no solution");
                System.exit(1);
            }
        }
        else if(choice.equals("quit")){
            System.out.println("The game has been quit");
            System.exit(0);
        }
        else{
            System.out.println("Invalid Input value");
            evaluate("move");

        }

    }


    private void announce(String arg) {
        setChanged();
        notifyObservers(arg);
    }


    @Override
    public Collection<Configuration> getSuccessors() {// need to write
        ArrayList<Configuration> allSuccessors = new ArrayList<Configuration>();
        HashMap<Coordinates,String> charPositions = new HashMap<Coordinates, String>();
        String [][] b = getBoard();
        for(int i = 0; i <BOARD_SIZE; i++){
            for(int j = 0; j<BOARD_SIZE; j++){
                if(!(b[i][j].equals("-"))){
                    charPositions.put(new Coordinates(i,j), board[i][j]);
                }
            }
        }
        allSuccessors = getValidConfigurations(charPositions);
        return allSuccessors;
    }

    private int getNumOfChar(String [][] b){
        int numChar = 0;
        for(int i = 0; i<b.length; i++){
            for(int j = 0; j<b.length; j++){
                if(!(b[i][j].equals("-"))){
                    numChar++;
                }
            }
        }
        return numChar;
    }
    @Override
    public boolean isValid() {
        if(!valid)return false;

        int previousBoardChar = getNumOfChar(this.getPreviousBoard());
        String [][] currBoard = ((SoltrChessModel)this).getBoard();
        int currBoardChar = getNumOfChar(currBoard);
        if(previousBoardChar - currBoardChar != 1){
            valid = false;
            return false;
        }
        else{
            return true;
        }
    }
    @Override
    public boolean isGoal() {
        if(!isValid())return false;

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



































































































    public ArrayList<Configuration> getValidConfigurations(HashMap<Coordinates,String> charPositions){
        ArrayList<Configuration> configs = new ArrayList<Configuration>();
        Configuration mConfig = new SoltrChessModel(this);
        Configuration currConfig = mConfig;
        for(Coordinates c: charPositions.keySet()){
            String thisChar = charPositions.get(c);

            if(thisChar.equals("B")){ //Bishops
                for(int i =1; i<BOARD_SIZE; i++){
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x+i>=0 && x+i<BOARD_SIZE) && (y+i>=0 && y+i<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x+i][y+i] = thisChar;
                        configs.add(currConfig);
                        thisBoard[x][y] = thisChar;
                        thisBoard[x+i][y+i] = "-";
                    }
                }

                for(int i =1; i<BOARD_SIZE; i++){
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x+i>=0 && x+i<BOARD_SIZE) && (y+i>=0 && y+i<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x+i][y-i] = thisChar;
                        configs.add(currConfig);
                        thisBoard[x][y] = thisChar;
                        thisBoard[x+i][y-i] = "-";
                    }
                }
                for(int i =1; i<BOARD_SIZE; i++){
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x-i>=0 && x<BOARD_SIZE) && (y+i>=0 && y+i<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x-i][y+i] = thisChar;
                        configs.add(currConfig);
                        thisBoard[x][y] = thisChar;
                        thisBoard[x-i][y+i] = "-";


                    }
                }
                for(int i =1; i<BOARD_SIZE; i++){
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x+i>=0 && x+i<BOARD_SIZE) && (y+i>=0 && y+i<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x-i][y-i] = thisChar;
                        configs.add(currConfig);
                        thisBoard[x][y] = thisChar;
                        thisBoard[x-i][y-i] = "-";
                    }
                }

            }





            else if(thisChar.equals("K")){
                for(int i =1; i<2; i++){
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x+i<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x+i][y] = thisChar;
                        configs.add(currConfig);
                        thisBoard[x][y] = thisChar;
                        thisBoard[x+i][y] = "-";

                    }
                }

                for(int i =1; i<2; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x-i>=0 )){
                        thisBoard[x][y] = "-";
                        thisBoard[x-i][y] = thisChar;
                        configs.add(currConfig);
                    }
                }

                for(int i =1; i<2; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((y+i<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x][y+i] = thisChar;
                        configs.add(currConfig);
                    }
                }


                for(int i =1; i<2; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((y-i>=0 )){
                        thisBoard[x][y] = "-";
                        thisBoard[x][y-i] = thisChar;
                        configs.add(currConfig);
                    }
                }

                for(int i =1; i<2; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x+i<BOARD_SIZE) && (y-i>=0 )){
                        thisBoard[x][y] = "-";
                        thisBoard[x+i][y-i] = thisChar;
                        configs.add(currConfig);
                    }
                }


                for(int i =1; i<2; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x-i>=0 ) && (y-i>=0 )){
                        thisBoard[x][y] = "-";
                        thisBoard[x-i][y-i] = thisChar;
                        configs.add(currConfig);
                    }
                }

                for(int i =1; i<2; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x+i<BOARD_SIZE) && ( y+i<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x+i][y+i] = thisChar;
                        configs.add(currConfig);
                    }
                }

                for(int i =1; i<2; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x-i>=0 ) && (y+i<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x-i][y+i] = thisChar;
                        configs.add(currConfig);
                    }
                }
            }
            else if(thisChar.equals("Q")){
                for(int i =1; i<BOARD_SIZE; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((y+i<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x][y+i] = thisChar;
                        configs.add(currConfig);
                    }
                }

                for(int i =1; i<BOARD_SIZE; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((y-i>=0)){
                        thisBoard[x][y] = "-";
                        thisBoard[x][y-i] = thisChar;
                        configs.add(currConfig);
                    }
                }

                for(int i =1; i<BOARD_SIZE; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if(( x+i<BOARD_SIZE) ){
                        thisBoard[x][y] = "-";
                        thisBoard[x+i][y] = thisChar;
                        configs.add(currConfig);
                    }
                }


                for(int i =1; i<BOARD_SIZE; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x-i>=0)){
                        thisBoard[x][y] = "-";
                        thisBoard[x-i][y] = thisChar;
                        configs.add(currConfig);
                    }
                }

                for(int i =1; i<BOARD_SIZE; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x+i<BOARD_SIZE) && (y+i<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x+i][y+i] = thisChar;
                        configs.add(currConfig);
                    }
                }


                for(int i =1; i<BOARD_SIZE; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x+i<BOARD_SIZE) && (y-i>=0 )){
                        thisBoard[x][y] = "-";
                        thisBoard[x+i][y-i] = thisChar;
                        configs.add(currConfig);
                    }
                }

                for(int i =1; i<BOARD_SIZE; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x-i>=0 ) && (y+i<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x-i][y+i] = thisChar;
                        configs.add(currConfig);
                    }
                }


                for(int i =1; i<BOARD_SIZE; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x-i>=0 ) && (y-i>=0 )){
                        thisBoard[x][y] = "-";
                        thisBoard[x-i][y-i] = thisChar;
                        configs.add(currConfig);
                    }
                }


            }
            else if(thisChar.equals("R")){
                for(int i =1; i<BOARD_SIZE; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if( (y+i<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x][y+i] = thisChar;
                        configs.add(currConfig);
                    }
                }

                for(int i =1; i<BOARD_SIZE; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if( y-i>=0 ){
                        thisBoard[x][y] = "-";
                        thisBoard[x][y-i] = thisChar;
                        configs.add(currConfig);
                    }
                }

                for(int i =1; i<BOARD_SIZE; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x+i<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x+i][y] = thisChar;
                        configs.add(currConfig);
                    }
                }

                for(int i =1; i<BOARD_SIZE; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if(x-i>=0 ){
                        thisBoard[x][y] = "-";
                        thisBoard[x-i][y] = thisChar;
                        configs.add(currConfig);
                    }
                }
            }
            else if(thisChar.equals("N")){
                for(int i =0; i<1; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x>=0 && x+2<BOARD_SIZE) && (y-1>0 && y+i<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x+2][y-1] = thisChar;
                        configs.add(currConfig);
                    }
                }

                for(int i =0; i<1; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x>=0 && x+2<BOARD_SIZE) && (y>=0 && y+1<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x+2][y+1] = thisChar;
                        configs.add(currConfig);
                    }
                }

                for(int i =0; i<1; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x>=0 && x+1<BOARD_SIZE) && (y-2>=0 && y<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x+1][y-2] = thisChar;
                        configs.add(currConfig);
                    }
                }


                for(int i =0; i<1; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x>=0 && x+1<BOARD_SIZE) && (y>=0 && y+2<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x+1][y+2] = thisChar;
                        configs.add(currConfig);
                    }
                }


                for(int i =0; i<1; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x-2>=0 && x<BOARD_SIZE) && (y>=0 && y+1<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x-2][y+1] = thisChar;
                        configs.add(currConfig);
                    }
                }


                for(int i =0; i<1; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x-2>=0 && x<BOARD_SIZE) && (y-1>=0 && y<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x-2][y-1] = thisChar;
                        configs.add(currConfig);
                    }
                }


                for(int i =0; i<1; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x-1>=0 && x<BOARD_SIZE) && (y>=0 && y+2<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x-1][y+2] = thisChar;
                        configs.add(currConfig);
                    }
                }


                for(int i =0; i<1; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x-1>=0 && x<BOARD_SIZE) && (y-2>=0 && y<BOARD_SIZE)){
                        thisBoard[x][y] = "-";
                        thisBoard[x-1][y-2] = thisChar;
                        configs.add(currConfig);
                    }
                }
            }
            else{//Pawn

                for(int i =0; i<1; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x+i>=0 && x+i<BOARD_SIZE) && (y+i>=0 && y+i<BOARD_SIZE)){
                        if(x == 0){
                            thisBoard[x][y] = "-";
                            thisBoard[x-1][y-1] = thisChar;
                            configs.add(currConfig);
                        }
                    }
                }

                for(int i =0; i<1; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x+i>=0 && x+i<BOARD_SIZE) && (y+i>=0 && y+i<BOARD_SIZE)){
                        if(x == 0){
                            thisBoard[x][y] = "-";
                            thisBoard[x-1][y+1] = thisChar;
                            configs.add(currConfig);
                        }
                    }
                }


                for(int i =0; i<1; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x+i>=0 && x+i<BOARD_SIZE) && (y+i>=0 && y+i<BOARD_SIZE)){
                        if (x != 0) {
                            thisBoard[x][y] = "-";
                            thisBoard[x+1][y+1] = thisChar;
                            configs.add(currConfig);
                        }
                    }
                }

                for(int i =0; i<1; i++){
                    Configuration currConfig = mConfig;
                    String [][] thisBoard = ((SoltrChessModel)currConfig).getBoard();
                    int x = c.getX();int y = c.getY();
                    if((x+i>=0 && x+i<BOARD_SIZE) && (y+i>=0 && y+i<BOARD_SIZE)){
                        if(x != 0){
                            thisBoard[x][y] = "-";
                            thisBoard[x+1][y-1] = thisChar;
                            configs.add(currConfig);
                        }
                    }
                }
            }

        }
        return configs;
    }
}
