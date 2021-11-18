package controller;

import classes.User;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * El controlador de la ventana de Session la cual controla las excepciones y
 * las acciones de los botones y los textos.
 *
 * @author Idoia Ormaetxea y Alain Cosgaya
 */
public class SessionController {

    private static final Logger LOGGER = Logger.getLogger(SessionController.class.getName());

    @FXML
    private BorderPane bPane;

    @FXML
    private Label lblNomUsu;

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblEmail;

    private Stage stage;

    /**
     * El metodo que indica el stage.
     *
     * @param stage1
     */
    public void setStage(Stage stage1) {
        stage = stage1;
    }

    /**
     * El metodo que rellena los datos de los campos de texto. En el caso de que
     * los caracteres del parametro Fullname sean mayores a 12, se disminuira el
     * tamaño de la letra.
     *
     * @param user Los datos del usuario
     */
    public void initData(User user) {
        String txtName;
        txtName = user.getFullName();
        LOGGER.info("Comprobacion de si hay espacios en el atributo FullName");
        lblNomUsu.setFont(new Font(30));
        lblNomUsu.setText(txtName);
        if (lblNomUsu.getText().length() > 12) {
            LOGGER.info("Longitud mayor a 12, se reajustara la fuente del label");
            lblNomUsu.setFont(new Font(20));
        }

        lblUsername.setText(user.getUsername());
        lblEmail.setText(user.getEmail());
    }

    /**
     * El metodo que instancia la ventana. Se añaden tanto el icono como el
     * titulo y los listener de los metodos.
     *
     * @param root
     */
    public void initStage(Parent root) {
        Stage anotherStage = new Stage();
        anotherStage.setResizable(false);
        anotherStage.getIcons().add(
                new Image("/photos/descargas-removebg-preview.png")
        );
        anotherStage.setTitle("Session");
        anotherStage.setScene(new Scene(root));
        anotherStage.show();
        anotherStage.setOnCloseRequest(this::confirmClose);
    }

    /**
     * El metodo que indica las acciones del botón y crea la ventana a la que se
     * dirige. Se cargara el FXML de SignIn y se mostrara la ventana de SignIn.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void buttonEventBack(ActionEvent event) throws IOException {

        try {
            LOGGER.info("Cierre de sesion del usuario");
            LOGGER.info("Carga del FXML de SignIn");
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/view/SignIn.fxml")
            );
            // Se carga el FXML de SignIn
            Parent root = (Parent) loader.load();
            LOGGER.info("Llamada al controlador del FXML");
            // Se recoge el controlador del FXML
            SignInController controller = ((SignInController) loader.getController());
            controller.setStage(stage);
            LOGGER.info("Inicio del stage de SignIn");
            controller.initStage(root);

            bPane.getScene().getWindow().hide();

        } catch (IOException e) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

    }

    /**
     * El metodo que indica la acción al botón y sale de la aplicación. Se hará
     * a traves de una llamada al metodo confirmClose.
     *
     * @param event
     */
    @FXML
    private void buttonEventExit(ActionEvent event) {
        LOGGER.info("Llamada a metodo de confirmacion de salida del programa");
        confirmClose(event);

    }
    /**
     * Alert de confirmacion de cerrado de programa. Tendras la opcion de elegir
     * si deseas cerrarlo o no.
     *
     * @param event Pulsacion del evento de cerrado.
     */
    private void confirmClose(Event event) {
        LOGGER.info("Creacion de alert de confirmacion");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Estas seguro de que quieres salir del programa?");
        Button btnClose = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        btnClose.setText("Salir");
        // Muestra el alert a la espera de la pulsacion de un boton del alert.
        Optional<ButtonType> close = alert.showAndWait();
        LOGGER.info("Comprobacion de pulsacion de salir");
        if (!ButtonType.OK.equals(close.get())) {
            event.consume();
        } else {
            LOGGER.info("Cierre del programa");
            Platform.exit();
        }

    }

}
