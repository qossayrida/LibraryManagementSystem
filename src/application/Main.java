package application;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		SceneManager.setPrimaryStage(primaryStage);
		SceneManager.setLoginScene();

	    primaryStage.setTitle("Library");
	    //primaryStage.getIcons().add(new Image("file:icon.png"));
	    primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
	    System.out.println(getClass().getResource("/icon.png"));
	    primaryStage.setMaximized(true);
	    primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}



