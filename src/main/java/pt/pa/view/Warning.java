package pt.pa.view;

import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import javafx.scene.Scene;

/**
 * Esta classe permite lanÃ§ar uma Warning.
 *
 * @author GonÃ§alo Barracha, Rodrigo Cardoso
 * @version 28/06/2024
 */
public class Warning extends Stage {
    /**
     * Construtor da classe Warning, recebe com parÃ¢metro um titulo e uma mensagem a ser exibida
     *
     * @param title - Titulo do Aviso
     * @param message - Mensagem a ser lanÃ§ada
     */
    public Warning(String title, String message) {

        VBox mainPanel = new VBox(10);
        mainPanel.setPadding(new Insets(10));
        mainPanel.setAlignment(Pos.CENTER);

        Label shownMessage = new Label(message);
        shownMessage.setStyle("-fx-font-weight: bold;");

        final Button okButton = new Button("OK");
        okButton.setOnAction(e -> ((Stage) okButton.getScene().getWindow()).close());

        mainPanel.getChildren().addAll(shownMessage, okButton);
        setResizable(false);
        initStyle(StageStyle.UTILITY);
        initModality(Modality.APPLICATION_MODAL);
        setIconified(false);
        centerOnScreen();
        setTitle(title);
        setScene(new Scene(mainPanel));
        showAndWait();
    }
}
