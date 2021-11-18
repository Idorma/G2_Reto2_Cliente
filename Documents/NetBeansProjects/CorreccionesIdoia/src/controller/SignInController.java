package controller;

import classes.User;
import exceptions.*;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import static logic.UiLogicFactory.getUiImplem;

/**
 * El controlador de la ventana de SignIn la cual controla las excepciones y las
 * acciones de los botones y los textos.
 *
 * @author Idoia Ormaetxea y Alain Cosgaya
 */
public class SignInController {

    private static final Logger LOGGER = Logger.getLogger(SignInController.class.getName());

    private Stage stage;

    @FXML
    private TextField textUser;

    @FXML
    private PasswordField textPasswd;

    @FXML
    private Button btnLogin;

    @FXML
    private Hyperlink linkSignIn;

    @FXML
    private Label lblCaract;

    @FXML
    private Label lblNum;

    @FXML
    private Pane paneVentana;

    private final int max = 50;

    /**
     * El metodo que indica el stage.
     *
     * @param stage1
     */
    public void setStage(Stage stage1) {
        stage = stage1;
    }

    /**
     * El metodo que instancia la ventana. Se añaden tanto el icono como el
     * titulo y los listener de los metodos.
     *
     * @param root
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/photos/descargas-removebg-preview.png"));
        stage.setTitle("SignIn");
        stage.setScene(scene);
        LOGGER.info("Llamada a los metodos y restricciones del controlador");
        // Listener de los campos
        textPasswd.textProperty().addListener(this::passwdTextCaractValidation);
        textPasswd.textProperty().addListener(this::passwdTextNumValidation);
        // Metodo de vinculacion de campos
        reportedFields();
        btnLogin.setOnAction(this::buttonEventSignIn);
        linkSignIn.setOnAction(this::buttonEvent);
        textPasswd.setOnKeyTyped(this::eventKey);
        textUser.setOnKeyTyped(this::eventKey);
        textPasswd.setOnKeyTyped(this::eventKey);
        stage.setOnCloseRequest(this::confirmClose);
        stage.show();

    }

    /**
     * El metodo que controla que no se introduzcan espacios en los campos de
     * texto y que no llegue al limite de caracteres.
     *
     * @param event
     */
    @FXML
    private void eventKey(KeyEvent event) {
        Object evt = event.getSource();
        // Comprobacion y borrado de espacios en blanco para cada campo
        if (evt.equals(textUser) || evt.equals(textPasswd)) {
            if (event.getCharacter().equals(" ")) {
                event.consume();
            }
        }
        // Comprobacion y borrado de caracteres que superen a longitud maxima
        // en el campo textUser
        if (evt.equals(textUser)) {
            if (textUser.getText().length() >= max) {
                event.consume();
            }
        }
        // Comprobacion y borrado de caracteres que superen a longitud maxima
        // en el campo textPasswd
        if (evt.equals(textPasswd)) {
            if (textPasswd.getText().length() >= max) {
                event.consume();
            }
        }
    }

    /**
     * El metodo que indica las acciones del botón y crea la ventana a la que se
     * dirige, llevandole los datos. Se hace uso de la implementacion mandando
     * la peticion de inicio de sesion, esperando respuesta de esta. En el caso
     * de que no haya ningun tipo de error, se cargara el FXML de Session, y se
     * mostrara llevandole los datos del usuario que ha hecho un inicio de
     * sesion.
     *
     * @param event El evento de pulsacion del boton SignIn.
     */
    @FXML
    private void buttonEventSignIn(ActionEvent event) {
        LOGGER.info("Pulsacion de boton de inicio de sesion");
        try {
            LOGGER.info("Inicializacion de la variable user");
            User user = new User();
            user.setUsername(textUser.getText());
            user.setPassword(textPasswd.getText());
            LOGGER.info("Ejecucion del metodo signIn de la implementacion");
            user = getUiImplem().signIn(user);
            LOGGER.info("Registro de usuario exitoso");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Usuario registrado correctamente");
            LOGGER.info("Carga del FXML de Session");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Session.fxml"));
            // Se carga el FXML de Session
            Parent root = (Parent) loader.load();
            LOGGER.info("Llamada al controlador del FXML");
            // Se recoge el controlador del FXML
            SessionController controller = ((SessionController) loader.getController());
            controller.setStage(stage);
            LOGGER.info("Inicio del stage de Session");
            controller.initStage(root);
            controller.initData(user);
            paneVentana.getScene().getWindow().hide();

        } catch (IOException e1) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, e1.getMessage(), e1);
        } catch (ConnectException | SignInException | UpdateException | ServerFullException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.show();
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * El metodo que indica las acciones del botón y crea la ventana a la que se
     * dirige. Se hace una pulsacion de un hyperLink, cargando el FXML de
     * SignUp, y mostrandolo.
     *
     * @param event
     */
    @FXML
    private void buttonEvent(ActionEvent event) {
        LOGGER.info("Pulsacion de hyperLink de registro de usuario");
        try {
            LOGGER.info("Carga del FXML de SignUp");
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/view/Registro.fxml")
            );
            // Se carga el FXML de SignUp
            Parent root = (Parent) loader.load();
            LOGGER.info("Llamada al controlador del FXML");
            // Se recoge el controlador del FXML
            SignUpController controller = ((SignUpController) loader.getController());
            controller.setStage(stage);
            LOGGER.info("Inicio del stage de SignUp");
            controller.initStage(root);

            paneVentana.getScene().getWindow().hide();

        } catch (IOException e) {
            Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * El metodo que valida si se han introducido menos de 8 caracteres. La
     * comprobacion se hace a traves de la llamada al metodo
     * validarMinCaractPasswdPattern.
     *
     * @param ov valor observable
     * @param oldV valor antiguo
     * @param newV valor nuevo
     */
    private void passwdTextCaractValidation(ObservableValue ov, String oldV,
            String newV) {
        // Se comprueba si el textField tiene algun tipo de texto para poder hacer una validacion.
        if (!textPasswd.getText().equals("")) {
            try {
                // Guarda el texto del textField en un string para llevarlo al metodo de validacion. 
                String passwd = textPasswd.getText();
                validarMinCaractPasswdPattern(passwd);
                lblCaract.setVisible(false);
            } catch (PasswordLengthException e) {
                Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, e.getMessage());
                lblCaract.setVisible(true);
            }
        } else {
            lblCaract.setVisible(false);
        }

    }

    /**
     * El metodo que valida si se han introducido números. La comprobacion se
     * hace a traves de la llamada al metodo validarNumPasswdPattern.
     *
     * @param ov valor observable
     * @param oldV valor antiguo
     * @param newV valor nuevo
     */
    private void passwdTextNumValidation(ObservableValue ov, String oldV,
            String newV) {
        // Se comprueba si el textField tiene algun tipo de texto para poder hacer una validacion.
        if (!textPasswd.getText().equals("")) {
            try {
                // Guarda el texto del textField en un string para llevarlo al metodo de validacion. 
                String passwd = textPasswd.getText();
                validarNumPasswdPattern(passwd);
                lblNum.setVisible(false);
            } catch (PasswordNumException e) {
                Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, e.getMessage());
                lblNum.setVisible(true);
            }
        } else {
            lblNum.setVisible(false);
        }

    }

    /**
     * El metodo que controla si los campos están informados. Se vinculan todas
     * las propiedades de los atributos.
     */
    private void reportedFields() {
        btnLogin.disableProperty().bind(
                textUser.textProperty().isEmpty().or(
                        textPasswd.textProperty().isEmpty()).or(
                        lblCaract.visibleProperty().or(
                                lblNum.visibleProperty()
                        )
                )
        );
    }

    /**
     * El metodo que controla los caracteres mínimos de la contraseña. La
     * comprobacion se hace a traves de una comparacion con un patron.
     *
     * @param passwd recoge el valor de la contraseña.
     * @throws PasswordLengthException
     */
    public void validarMinCaractPasswdPattern(String passwd)
            throws PasswordLengthException {
        // Patron para la comprobacion.
        String regex = "^(.+){8,50}$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(passwd);

        if (!matcher.matches()) {
            throw new PasswordLengthException(lblCaract.getText());
        }

    }

    /**
     * El metodo que controla si la contraseña contiene números. La comprobacion
     * se hace a traves de una comparacion con un patron.
     *
     * @param passwd recoge el valor de la contraseña.
     * @throws PasswordNumException
     */
    public void validarNumPasswdPattern(String passwd)
            throws PasswordNumException {
        // Patron para la comprobacion.
        String regex = ".*\\d.*";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(passwd);
        if (!matcher.matches()) {
            throw new PasswordNumException(lblNum.getText());
        }
    }

    /**
     * Alert de confirmacion de cerrado de programa. Tendras la opcion de elegir
     * si deseas cerrarlo o no.
     *
     * @param event Pulsacion del evento de cerrado.
     */
    public void confirmClose(Event event) {
        LOGGER.info("Creacion de alert de confirmacion");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Estas seguro de que quieres salir del programa?");
        Button btnClose = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        btnClose.setText("Salir");
        // Muestra el alert a la espera de la pulsacion de un boton del alert.
        Optional<ButtonType> close = alert.showAndWait();
        if (!ButtonType.OK.equals(close.get())) {
            event.consume();
        }

    }

}
