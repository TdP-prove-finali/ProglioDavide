package it.polito.tdp.tesi;

import javafx.application.Application;
import static javafx.application.Application.launch;
import it.polito.tdp.tesi.FXMLController;
import it.polito.tdp.tesi.model.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class EntryPoint extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scene.fxml"));
    	Parent root = loader.load();
        Scene scene = new Scene(root);
        
        Model model = new Model();
        FXMLController controller = loader.getController();
        controller.setModel(model);
        
        scene.getStylesheets().add("/styles/Styles.css");
        scene.getRoot().setStyle("-fx-font-family: 'Verdana'");
        stage.setTitle("Software per la schedulazione e la simulazione produttiva");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
