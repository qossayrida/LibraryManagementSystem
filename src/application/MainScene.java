package application;

import javafx.stage.Screen;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import java.util.*;

public class MainScene extends Scene{

	BorderPane root;
	
    public MainScene(){
    	super(new BorderPane(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
    	root = (BorderPane) getRoot();
    	root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    	
   
        ArrayList<String> strings = new ArrayList<String>();
        ArrayList<String> icons = new ArrayList<String>();
        ArrayList<Button> buttons = new ArrayList<Button>();
        strings.add("Home");
        strings.add("New Borrow");
        strings.add("Return Book");
        strings.add("Book");
        strings.add("Borrowing");
        strings.add("Patron");
        if (SceneManager.getCuurent_user()==1) 
        	strings.add("Librarian");
        strings.add("Dewey Classification");
        strings.add("Change Password");
        strings.add("log out");
        
        
        icons.add("/homeicon.png");
        icons.add("/borrowicon.png");
        icons.add("/returnicon.png");
        icons.add("/bookicon.png");
        icons.add("/borrowingicon.png");
        icons.add("/patronicon.png");
        if (SceneManager.getCuurent_user()==1) 
        	icons.add("/librarianicon.png");
        icons.add("/deweyicon.png");
        icons.add("/passwordicon.png");
        icons.add("/logouticon.png");
        
        settingUpButton(strings,icons,buttons);
        VBox sidebar = new VBox(15);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.getChildren().add(new ImageView(new Image(getClass().getResourceAsStream("/libraryicon.png"))));
        sidebar.getChildren().addAll(buttons);
        sidebar.getStyleClass().add("sidebar");
        
        root.setLeft(sidebar);
        
    }
    
    public void settingUpButton(ArrayList<String> strings,ArrayList<String> icons, ArrayList<Button> buttons) {
    	for (int i = 0; i < strings.size(); i++) {
        	Button button = new Button(strings.get(i));
            buttons.add(button);
            button.getStyleClass().add("custom-button");
            button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(icons.get(i)))));
            button.setFont(Font.font("Helvetica"));
            button.setMinWidth(150);
        }
    	
    	root.setCenter(new HomeScene());

    	buttons.get(0).setOnAction(e -> {
         	root.setCenter(new HomeScene());
        });
    	 
    	buttons.get(1).setOnAction(e -> {
    		root.setCenter(new NewBorrowManagement());
        });
    	
    	buttons.get(2).setOnAction(e -> {
    		root.setCenter(new ReturnBookManagement());
        });
    	
    	buttons.get(3).setOnAction(e -> {
    		root.setCenter(new BookManagement());
        });
    	 
    	
    	buttons.get(4).setOnAction(e -> {
   		 	root.setCenter(new BorrowingTransactionManagement());
    	});
    	
    	buttons.get(5).setOnAction(e -> {
       	 	root.setCenter(new PatronManagement());
        });
    	
    	
    	
    	if  (SceneManager.getCuurent_user()==1) {	
    		buttons.get(6).setOnAction(e -> {
       		 	root.setCenter(new LibrarianManagement());
        	});
    		
    		buttons.get(7).setOnAction(e -> {
           	 	root.setCenter(new DeweyClassificationManagement());
            });
    		
        	buttons.get(8).setOnAction(e -> {
        		root.setCenter(new PasswordManagement());
        	});
        	
        	buttons.get(9).setOnAction(e -> {
        		SceneManager.setLoginScene();
        	});
    	}
    	else {
    		buttons.get(6).setOnAction(e -> {
           	 	root.setCenter(new DeweyClassificationManagement());
            });
    		
        	buttons.get(7).setOnAction(e -> {
        		root.setCenter(new PasswordManagement());
        	});
        	
        	buttons.get(8).setOnAction(e -> {
        		SceneManager.setLoginScene();
        	});
		}
    }
}