package application;

import controller.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * Es la clase principal del programa.
 *
 * @author Idoia Ormaetxea y Alejandro Gomez
 */
public class App extends Application {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    /**
     * El metodo que carga el FXML de la ventana de SignIn.
     *
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        LOGGER.info("Carga del FXML de SignIn");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignIn.fxml"));
        Parent root;
        try {
            root = (Parent) loader.load();
            LOGGER.info("Llamada al controlador del FXML");
            SignInController controller = ((SignInController) loader.getController());
            controller.setStage(stage);
            controller.initStage(root);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    /**
     * Metodo de inicio del programa.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
