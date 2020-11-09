import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.input.*;
import javafx.geometry.*;
import java.util.*;

public class Pente extends Application {

	private Pane root;
	private Checkerboard board;
	private double height;
	private double width;

	private Label message;
	private Button newGameButton;
	private Button resignButton;
	private Button instructionsButton;

	private boolean gameInProgress;
	private boolean blackTurn;

	private int winR1;
	private int winC1;
	private int winR2;
	private int winC2;

	private int [][] checkBoard;

	private Circle circle;

	private static class Checkerboard extends Canvas {

        public Checkerboard () {
            
            super (329,329);
        }

        public void draw () {
            
            int row;
            int col;
            GraphicsContext g = getGraphicsContext2D();

            g.setStroke (Color.GOLD);
            g.setLineWidth (2);
            g.strokeRect ( 1, 1, 327, 327);


            for (row = 0; row < 13; row ++) {
                

                for (col = 0; col < 13; col ++) {
                    
                    g.setStroke (Color.BLACK);
                    g.setFill (Color.LIGHTGRAY);
                    g.fillRect (2 + col * 25, 2 + row * 25, 25, 25);
                    g.strokeRect (2 + col * 25, 2 + row * 25, 25, 25);
                }
            }  
        }  
    }

	public class Circle extends Checkerboard {

		public Circle (int row, int col) {

            GraphicsContext g = board.getGraphicsContext2D();
     

            if (blackTurn == true) {

            	g.setFill (Color.BLACK);
            	g.fillOval ((row * 25) + 5, (col * 25) + 5, 20, 20);
            }

            else {

            	g.setFill (Color.WHITE);
            	g.fillOval ((row * 25) + 5, (col * 25) + 5, 20, 20);
            }
		}
	}

	public static void main (String [] args) {

		launch ();
	}

	public void start (Stage stage) {

		Scene scene;

		root = new Pane ();
		root.setStyle ("-fx-border-color: gold; -fx-border-width: 3px 3px 0px 3px");

		board = new Checkerboard ();
		board.draw ();

		newGameButton = new Button ("New Game");
		resignButton = new Button ("Resign");
		instructionsButton = new Button ("Instructions");

		resignButton.setOnAction (evt -> resign ());
		newGameButton.setOnAction (evt -> newGame ());
		instructionsButton.setOnAction (evt -> instructions ());
		board.setOnMousePressed (evt -> generateMove (evt));

		blackTurn = true;

		message = new Label ("Click \"New Game\" to begin.");
		message.setTextFill (Color.GOLD);

		board.relocate (20, 20);
		newGameButton.relocate (370, 120);
		resignButton.relocate (370, 200);
		instructionsButton.relocate (370, 280);
		message.relocate (20, 370);

		resignButton.setManaged (false);
		resignButton.resize (100, 30);
		newGameButton.setManaged (false);
		newGameButton.resize (100, 30);
		instructionsButton.setManaged (false);
		instructionsButton.resize (100, 30);
		board.setDisable (true);

		root.setPrefWidth (500);
		root.setPrefHeight (500);

		root.getChildren ().addAll (board, newGameButton, resignButton, instructionsButton, message);
		root.setStyle("-fx-background-color: blue; -fx-border-color: gold; -fx-border-width:3");

		scene = new Scene (root);
		stage.setScene (scene);
		stage.setTitle ("Pente");
		stage.show ();
	}

	private void resign () {

		if (gameInProgress == false) {

			resignButton.setDisable (true);
			message.setText ("You must start a new game before you can resign.");

			return;
		}
	
		if (blackTurn == true) {

			gameInProgress = false;
			resignButton.setDisable (true);
			newGameButton.setDisable (false);
			instructionsButton.setDisable (false);
			board.setDisable (true);
			message.setText ("Black resigns. White is the winner. Press \"New Game\" to start a new game.");
		}
	
		else {

			gameInProgress = false;
			resignButton.setDisable (true);
			newGameButton.setDisable (false);
			instructionsButton.setDisable (false);
			board.setDisable (true);
			message.setText ("White resigns. Black is the winner. Press \"New Game\" to start a new game.");
		}
	}

	private void newGame () {

		checkBoard = new int [13][13];
		gameInProgress = true;
		resignButton.setDisable (false);
		newGameButton.setDisable (true);
		instructionsButton.setDisable (false);
		board.setDisable (false);
		board.draw ();
		message.setText ("Black goes first. Make your move.");
		blackTurn = true;
	}

	private void instructions () {

		message.setText ("This game is similar to Tic Tac Toe. The objective is to get 5 pieces in a row. Good luck!");
	}

	private void generateMove (MouseEvent evt) {

		double x;
		double y;
		int row;
		int col;

		x = evt.getX ();
		y = evt.getY ();

		row = (int)(x / 25);
		col = (int)(y / 25);

		
		if (checkBoard [row][col] == 1 || checkBoard [row][col] == 2) {

			return;
		}

		circle = new Circle (row, col);


		if (blackTurn == true) {

			checkBoard [row][col] = 1;
			message.setText ("White's turn.");
		}

		else {

			checkBoard [row][col] = 2;
			message.setText ("Black's turn.");
		}
		
		generateCheck (row, col);
		blackTurn = ! blackTurn;
	}

	private void generateCheck (int row, int col) {

		int tick;

		tick = 0;


		if (generateWin (checkBoard [row][col], row, col, 1, 0) >= 5) {

			announceWin ();
		}
	
		if (generateWin (checkBoard [row][col], row, col, 0, 1) >= 5) {

			announceWin ();
		}
	
		if (generateWin (checkBoard [row][col], row, col, -1, 0) >= 5) {

			announceWin ();
		}

		if (generateWin (checkBoard [row][col], row, col, 0, -1) >= 5) {

			announceWin ();
		}

		if (generateWin (checkBoard [row][col], row, col, 1, -1) >= 5) {

			announceWin ();
		}
	
		if (generateWin (checkBoard [row][col], row, col, 1, 1) >= 5) {

			announceWin ();
		}
	
		if (generateWin (checkBoard [row][col], row, col, -1, 1) >= 5) {

			announceWin ();
		}
	
		if (generateWin (checkBoard [row][col], row, col, -1, -1) >= 5) {

			announceWin ();
		}
	
		for (int i = 0; i < 13; i ++) {

			
			for (int j = 0; j < 13; j ++) {


				if (checkBoard [i][j] == 1 || checkBoard [i][j] == 2) {

					tick ++;
				}
			
				if (tick >= 169) {

					announceTie ();
				}
			}
		}
	}

	private int generateWin (int player, int row, int col, int dirX, int dirY) {

		int count;
		int r;
		int c;

		count = 1;
		
		r = row + dirX;
		c = col + dirY;


		while (r >= 0 && r < 13 && c >= 0 && c < 13 && checkBoard [r][c] == player) {

			count ++;
			r += dirX;
			c += dirY;
		}
		
		winR1 = r - dirX;
		winC1 = c - dirY;

		r = row - dirX;
		c = col - dirY;

		
		while (r >= 0 && r < 13 && c >= 0 && c < 13 && checkBoard [r][c] == player) {

			count ++;
			r -= dirX;
			c -= dirY;
		}

		winR2 = r + dirX;
		winC2 = c + dirY;

		return count;
	}

	private void announceWin () {

		if (blackTurn == true) {

			drawWinLine ();
			message.setText ("Black wins! Click \"New Game\" to play again.");
			board.setDisable (true);
			resignButton.setDisable (true);
			newGameButton.setDisable (false);
		}
	
		else {

			drawWinLine ();
			message.setText ("White wins! Click \"New Game\" to play again.");
			board.setDisable (true);
			resignButton.setDisable (true);
			newGameButton.setDisable (false);
		}
	}

	private void announceTie () {

		message.setText ("Tie game! Click \"New Game\" to play again.");
		board.setDisable (true);
		resignButton.setDisable (true);
		newGameButton.setDisable (false);
	}

	private void drawWinLine () {

		GraphicsContext g = board.getGraphicsContext2D ();
		g.setStroke (Color.GOLD);
		g.setLineWidth (4);
		g.strokeLine (13 + 25 * winR1, 13 + 25 * winC1, 13 + 25 * winR2, 13 + 25 * winC2);
	}
}