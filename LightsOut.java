import java.util.Random;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LightsOut extends Application {
	public int SIZE;
	GridPane gridpane= new GridPane();  //grid for puzzle
	ToggleButton[][] buttongrid;            //button array
	Boolean[][] buttonstate;				//button pressed state


	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {  
		VBox verticalbox= new VBox(5);   //radio button with space 5 pixels vertically
		Button btn = new Button("Create Puzzle");
		Label label= new Label("Please select a size:");
		verticalbox.setAlignment(Pos.CENTER);
		verticalbox.getChildren().add(label);       
		ToggleGroup radios = new ToggleGroup();   //Toggle group for radio buttons

		for (int i=0; i<7 ;i++) {
			RadioButton radio= new RadioButton(""+(i+3));
			if(i+3==5) {
				radio.setSelected(true);  //5 selected
			}

			radios.getToggles().add(radio);				
			verticalbox.getChildren().add(radio);
		}
		SIZE=5;  //default size
		buttongrid=  new ToggleButton[5][5];  //default arrays with default size
		buttonstate= new  Boolean[5][5];

		radios.selectedToggleProperty().addListener(new ChangeListener<Toggle>()  
		{ 
			public void changed(ObservableValue<? extends Toggle> ob,  
					Toggle o, Toggle n) 
			{ 
				RadioButton rb = (RadioButton)radios.getSelectedToggle(); 
				if (rb != null) { 
					String s = rb.getText(); 

					// change the SIZE
					try {
						SIZE = Integer.parseInt(s);  //if different size, new size
					}
					catch(NumberFormatException e){
					}
				}
				buttongrid=  new ToggleButton[SIZE][SIZE];   //hence new array
				buttonstate= new  Boolean[SIZE][SIZE];
			} 
		}); 

		verticalbox.getChildren().add(btn);

		btn.setOnAction(e->{   //create puzzle button action
			if(SIZE>2 && SIZE<10)
				startgame(); 
			primaryStage.close();
		});

		Pane rootPane = new Pane();
		rootPane.getChildren().add(verticalbox);
		primaryStage.setTitle("Lights Out Size");
		primaryStage.setScene(new Scene(rootPane));
		primaryStage.show();
	}


	public void startgame() {  

		gridpane.setAlignment(Pos.CENTER);
		BorderPane border = new BorderPane();	  //pane for puzzle layout
		border.setBackground(new Background(new BackgroundFill(Color.rgb(55, 55, 55), CornerRadii.EMPTY, Insets.EMPTY)));
		border.setPrefHeight(SIZE*70);
		border.setPrefWidth(SIZE*70);
		
		getPuzzle();   //gets the puzzle

		HBox horizontalbox=  new HBox(20);  // horizontal  panel in the pane
		horizontalbox.setAlignment(Pos.CENTER);
		horizontalbox.setBackground(null);
		
		Button btn1= new Button("Randomize");

		btn1.setOnAction(e->{
			getPuzzle();
		});

		Button btn2= new Button("Chase Lights");
		horizontalbox.getChildren().addAll(btn1, btn2);
		horizontalbox.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));

		btn2.setOnAction(e->{
			for(int i=0; i<SIZE-1; i++) {
				for(int j=0; j<SIZE; j++) {
					if(buttonstate[i][j]) {
						gridbuttonpressed(buttongrid[i+1][j]);
					}
				}
			}
		});

		border.setCenter(gridpane);
		border.setBottom(horizontalbox);
		Stage stage= new Stage();
		stage.setResizable(false);
		stage.setMinWidth(250);
		stage.setTitle("Lights Out");
		Scene myscene=  new Scene(border);
		stage.setScene(myscene);
		stage.show();
	}


	public void getPuzzle() {  //first create a grid of no light and press button

		boolean state= false;
		Random random = new Random();
		
		for (int i=0; i<SIZE; i++) {  //laying out solved state
			for(int j=0; j< SIZE; j++) {
				ToggleButton button = new ToggleButton("");
				button.setPrefSize(50, 50);
				state= false;
				button.setStyle("-fx-background-color:Black;"+"-fx-background-radius: 10 ;" + "-fx-background-insets: 1;");
				gridpane.add(button, j, i);
				buttongrid[i][j]=button;
				buttonstate[i][j]= state;

				button.setOnAction(e->{  //setting action of each button
					gridbuttonpressed(e.getSource());
					button.setSelected(false);
				});
			}			
		}
		for (int i=0; i<SIZE; i++) {     //randomizing the solved state by pressing or not pressing each button
			for(int j=0; j< SIZE; j++) {
				if(random.nextInt(2)==0) {
					gridbuttonpressed(buttongrid[i][j]);
				}
			}
		}
	}


	public void gridbuttonpressed(Object object) {
		gridpane.getChildren().clear();
		for (int i=0; i<SIZE; i++) {
			for(int j=0; j< SIZE; j++) {
				if(object.equals(buttongrid[i][j])) {

					ToggleButton pressedbutton= (ToggleButton) object;	
					
					//switching the neighboring buttons

					if(i<SIZE-1)  //if last row  then this is does not run otherwise it throws arrayindexoutofbound exception
						switchbutton(i+1,j);
					if(j<SIZE-1)    //if last column  then this is does not run otherwise it throws arrayindexoutofbound exception
						switchbutton(i,j+1);
					if(i>0)      //if first row  then this is does not run otherwise it throws arrayindexoutofbound exception
						switchbutton(i-1,j);
					if(j>0)  //if first column  then this is does not run otherwise it throws arrayindexoutofbound exception
						switchbutton(i,j-1);  

					//setting color of button
					if(buttonstate[i][j]) {
						pressedbutton.setStyle("-fx-background-color:Black;"+"-fx-background-radius: 10 ;" + "-fx-background-insets: 1;");
						buttonstate[i][j]=false;
					}
					else {
						pressedbutton.setStyle("-fx-background-color:Yellow;"+"-fx-background-radius: 10 ;" + "-fx-background-insets: 1;");
						buttonstate[i][j]=true;
					}
					gridpane.add(pressedbutton, j, i);
				}
				else {
					gridpane.add(buttongrid[i][j], j, i);
				}
			}
		}
	}

	private void switchbutton(int i, int j) {

		if(buttonstate[i][j]) {
			buttongrid[i][j].setStyle("-fx-background-color:Black;"+"-fx-background-radius: 10 ;" + "-fx-background-insets: 1;");
			buttonstate[i][j]=false;
		}
		else {
			buttongrid[i][j].setStyle("-fx-background-color:Yellow;"+"-fx-background-radius: 10 ;" + "-fx-background-insets: 1;");
			buttonstate[i][j]=true;
		}
	}
}