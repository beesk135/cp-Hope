package Controller;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ScoreBoard extends StackPane {
	
	private static int score;
	private static Text scoretxt;
	
	public ScoreBoard(){
		super();
		score = 0;
		
		scoretxt = new Text("Score: "+Integer.toString(score));
		scoretxt.setFill(Color.WHITE);
		scoretxt.setFont(Font.font("Cornerstone", FontWeight.BOLD, 18));
		
		this.setAlignment(Pos.CENTER);
		this.setLayoutX(20);
		this.setLayoutY(20);
		this.getChildren().addAll(scoretxt);
	}
	
	public static int getScore() {
		return score;
	}
	public static void setScore(int newScore)
	{ 
		score = newScore;
	}
	public static void addScore(int addScore)
	{
		score += addScore;
	}
	
	public static void update() {
		scoretxt.setText("Score: "+Integer.toString(score));
	}
}