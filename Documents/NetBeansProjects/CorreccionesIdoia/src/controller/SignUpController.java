package controller;

import classes.User;
import com.sun.javafx.scene.control.behavior.TextAreaBehavior;
import exceptions.*;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import static logic.UiLogicFactory.getUiImplem;

/**
 * El controlador de la ventana de SignUp la cual controla las excepciones y las
 * acciones de los botones y los textos.
 *
 * @author Idoia Ormaetxea y Alain Cosgaya
 */
public class SignUpController {

    private static final Logger LOGGER = Logger.getLogger(SignUpController.class.getName());
    @FXML
    private Label lblCaract;

    @FXML
    private PasswordField txtPasswd;

    @FXML
    private TextField txtEmail;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblNum;

    @FXML
    private PasswordField txtPassw2;

    @FXML
    private Label lblPasswd2;

    @FXML
    private TextField txtUser;

    @FXML
    private TextField txtName;

    @FXML
    private Button btnSignUp;

    @FXML
    private Button btnAtras;

    @FXML
    private BorderPane paneSignUp;

    private final int max = 50;

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
     * El metodo que instancia la ventana. Se añaden tanto el icono como el
     * titulo y los listener de los metodos.
     *
     * @param root
     */
    public void initStage(Parent root) {
        Stage anotherStage = new Stage();
        anotherStage.setResizable(false);
        anotherStage.getIcons().add(new Image(
                "/photos/descargas-removebg-preview.png")
        );
        anotherStage.setTitle("SignUp");
        anotherStage.setScene(new Scene(root));
        LOGGER.info("Llamada a metodos y restricciones del controlador");
        // Listener de los campos
        txtEmail.textProperty().addListener(this::emailTextValidation);
        txtPasswd.textProperty().addListener(this::passwdTextCaractValidation);
        txtPasswd.textProperty().addListener(this::passwdTextNumValidation);
        txtPassw2.textProperty().addListener(this::repeatpasswd);
        // Metodo de vinculacion de campos
        reportedFields();
        anotherStage.setOnCloseRequest(this::confirmClose);
        anotherStage.show();

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
        if (evt.equals(txtUser) || evt.equals(txtPasswd) || evt.equals(txtPassw2)
                || evt.equals(txtEmail)) {
            if (event.getCharacter().equals(" ")) {
                event.consume();
            }
        }
        // Comprobacion y borrado de caracteres que superen a longitud maxima
        // en el campo txtName
        if (evt.equals(txtName)) {
            if (txtName.getText().length() >= max) {
                event.consume();
            }
        }
        // Comprobacion y borrado de caracteres que superen a longitud maxima
        // en el campo txtUser
        if (evt.equals(txtUser)) {
            if (txtUser.getText().length() >= max) {
                event.consume();
            }
        }
        // Comprobacion y borrado de caracteres que superen a longitud maxima
        // en el campo txtPasswd
        if (evt.equals(txtPasswd)) {
            if (txtPasswd.getText().length() >= max) {
                event.consume();
            }
        }
        // Comprobacion y borrado de caracteres que superen a longitud maxima
        // en el campo txtPassw2
        if (evt.equals(txtPassw2)) {
            if (txtPassw2.getText().length() >= max) {
                event.consume();
            }
        }
        // Comprobacion y borrado de caracteres que superen a longitud maxima
        // en el campo txtEmail
        if (evt.equals(txtEmail)) {
            if (txtEmail.getText().length() >= max) {
                event.consume();
            }
        }

    }

    /**
     * El metodo que indica las acciones del botón y crea la ventana a la que se
     * dirige, llevandole los datos. Se hace uso de la implementacion mandando
     * la peticion de registro, esperando respuesta de esta. En el caso de que
     * no haya ningun tipo de error, se cargara el FXML de Session, y se
     * mostrara llevandole los datos del usuario que ha sido registrado.
     *
     * @param event El evento de pulsacion de SignUp
     */
    @FXML
    private void buttonEvent(ActionEvent event) {

        try {
            LOGGER.info("Inicializacion de la variable user");
            User user = new User();
            user.setUsername(txtUser.getText());
            user.setFullName(txtName.getText());
            user.setEmail(txtEmail.getText());
            user.setPassword(txtPasswd.getText());
            LOGGER.info("Ejecucion del metodo signUp de la implementacion");
            user = getUiImplem().signUp(user);
            LOGGER.info("Registro de usuario exitoso");
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Usuario registrado correctamente");

            alert.showAndWait();

            LOGGER.info("Carga del FXML de Session");
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/view/Session.fxml")
            );
            // Se carga el FXML de Session
            Parent root = (Parent) loader.load();
            LOGGER.info("Llamada al controlador del FXML");
            // Se recoge el controlador del FXML
            SessionController controller = ((SessionController) loader.getController());
            controller.setStage(stage);
            controller.initData(user);
            controller.initStage(root);

            paneSignUp.getScene().getWindow().hide();

        } catch (IOException e) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        } catch (ConnectException | SignUpException | UpdateException | ServerFullException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.show();
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    /**
     * El metodo que indica las acciones del botón y crea la ventana a la que se
     * dirige. Se cargara el FXML de SignIn y se mostrara la ventana de SignIn.
     *
     * @param event
     */
    @FXML
    private void buttonEventBack(ActionEvent event) {

        try {
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

            paneSignUp.getScene().getWindow().hide();

        } catch (IOException e) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

    }

    /**
     * El metodo que valida si el formato de correo es correcto. La comprobacion
     * se hace a traves de la llamada al metodo validarPattern.
     *
     * @param ov valor observable
     * @param oldV valor antiguo
     * @param newV valor nuevo
     */
    private void emailTextValidation(ObservableValue ov, String oldV,
            String newV) {
        // Se comprueba si el textField tiene algun tipo de texto para poder hacer una validacion.
        if (!txtEmail.getText().equals("")) {
            try {
                // Guarda el texto del textField en un string para llevarlo al metodo de validacion. 
                String email = txtEmail.getText();
                validarEmailPattern(email);
                lblEmail.setVisible(false);
            } catch (EmailPatternException e) {
                Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, e.getMessage());
                lblEmail.setVisible(true);
            }
        } else {
            lblEmail.setVisible(false);
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
        if (!txtPasswd.getText().equals("")) {

            try {
                // Guarda el texto del textField en un string para llevarlo al metodo de validacion. 
                String passwd = txtPasswd.getText();
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
        if (!txtPasswd.getText().equals("")) {
            try {
                // Guarda el texto del textField en un string para llevarlo al metodo de validacion. 
                String passwd = txtPasswd.getText();
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
     * El metodo que valida si las contraseñas coinciden entre ellas. La
     * validacion se hace a traves de la llamada al metodo validarEqualPasswd.
     *
     * @param ov valor observable
     * @param oldV valor antiguo
     * @param newV valor nuevo
     */
    private void repeatpasswd(ObservableValue ov, String oldV, String newV) {
        // Se comprueba si el textField tiene algun tipo de texto para poder
        // hacer una validacion.
        if (!txtPassw2.getText().equals("")) {
            try {
                // Llamada al metodo para la comprobacion de si las contraseñas
                // son iguales.
                validarEqualPasswd();
                lblPasswd2.setVisible(false);
            } catch (SamePasswordException e) {
                Logger.getLogger(SignInController.class.getName()).log(Level.SEVERE, e.getMessage());
                lblPasswd2.setVisible(true);
            }
        } else {
            lblPasswd2.setVisible(false);
        }
    }

    /**
     * El metodo que controla si los campos están informados. Vinculando las
     * propiedades de todos los campos.
     */
    private void reportedFields() {
        btnSignUp.disableProperty().bind(
                txtPasswd.textProperty().isEmpty().or(
                        txtEmail.textProperty().isEmpty().or(
                                txtPassw2.textProperty().isEmpty().or(
                                        txtUser.textProperty().isEmpty().or(
                                                txtName.textProperty().isEmpty().or(
                                                        lblCaract.visibleProperty().or(
                                                                lblEmail.visibleProperty().or(
                                                                        lblNum.visibleProperty().or(
                                                                                lblPasswd2.visibleProperty()
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }

    /**
     * El metodo que valida que el patron del correo es correcto. La
     * comprobacion se hace a traves de una comparacion con un patron.
     *
     * @param email recoge el valor del correo.
     * @throws EmailPatternException
     */
    public void validarEmailPattern(String email) throws EmailPatternException {
        // Patron para la comprobacion.
        String regex = "^(.+)@(.+)[.](.+)$";
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            throw new EmailPatternException(lblEmail.getText());
        }

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
     * El metodo que compara que las dos contraseñas son iguales. La
     * comprobacion se hace comparando los dos campos de contraseña.
     *
     * @throws SamePasswordException
     */
    public void validarEqualPasswd() throws SamePasswordException {
        if (!txtPasswd.getText().equals(txtPassw2.getText())) {
            throw new SamePasswordException(lblPasswd2.getText());
        }
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
        if (!ButtonType.OK.equals(close.get())) {
            event.consume();
        }

    }
}
