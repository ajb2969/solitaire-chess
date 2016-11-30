package ptui;
import model.*;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * Part of SoltrChess project.
 * Created 11 2015
 *
 * @author James Heliotis
 */
public class SoltrChessPTUI implements Observer {
    private SoltrChessModel model;

    public SoltrChessPTUI(String fileName) {
        this.model = new SoltrChessModel(fileName);
        initializeView();
    }


    // CONTROLLER
    public void run() {
        Scanner input = new Scanner(System.in);
        for(; ;){
            System.out.print("[move,new,restart,hint,solve,quit]> ");
            String choice = input.nextLine(); //if input is incorrect, re ask again
            model.evaluate(choice);
        }

    }

    public void initializeView() {
        this.model.addObserver(this);
        update(this.model, null);
    }

    @Override
    //
    public void update(Observable o, Object arg) {
        model.printBoard();
        if(model.isGoal()){
            System.out.println("You won, Congrats!!");
            System.exit(0);
        }
    }
}
