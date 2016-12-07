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
    /*
    * The model for the view and the controller.
    */
    private SoltrChessModel model;
    /*
    * Construct the PTUI
    */

    public SoltrChessPTUI(String fileName) {
        this.model = new SoltrChessModel(fileName);
        initializeView();
    }
    // CONTROLLER
    public void run() {
        /*
         * Read a command and executes the loop.
         */
        Scanner input = new Scanner(System.in);
        for(; ;){
            System.out.print("[move,new,restart,hint,solve,quit]> ");
            String choice = input.nextLine(); //if input is incorrect, re ask again
            model.evaluate(choice);
        }
    }
    public void initializeView() {
        /*
         * Initializes the view for the observer.
         */
        this.model.addObserver(this);
        update(this.model, null);
    }
    @Override
    //
    public void update(Observable o, Object arg) {
        model.printBoard();
        //Displays "You won, Congrats!!" when goal is true.
        if(model.isGoal()){
            System.out.println("You won, Congrats!!");
            //System.exit(0);
        }
    }
}
