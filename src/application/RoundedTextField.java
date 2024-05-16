package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;


public class RoundedTextField extends HBox {

    private Label label;
    private TextField textField;

    public RoundedTextField(String labelText, String promptText, String iconPath) {
        this.setSpacing(10);

        label = new Label(labelText);
        label.getStyleClass().add("rounded-label");

        textField = new TextField();
        textField.setPromptText(promptText);
        textField.getStyleClass().add("rounded-textfield");

        Image iconImage = new Image(getClass().getResourceAsStream(iconPath));
        ImageView imageView = new ImageView(iconImage);
        imageView.setFitHeight(20); // Set the desired height for your icon
        imageView.setPreserveRatio(true);

        StackPane iconPane = new StackPane(imageView);
        iconPane.getStyleClass().add("rounded-icon");

        getChildren().addAll(iconPane, label, textField);

        setPadding(new Insets(10));
        setAlignment(Pos.CENTER);
        getStyleClass().add("rounded-textfield-container");
    }

    // Add getters for label and textField if needed

    // Add other methods as needed
}