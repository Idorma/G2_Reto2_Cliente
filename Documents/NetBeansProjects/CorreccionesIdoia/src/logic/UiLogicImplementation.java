/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import classes.MessageType;
import classes.Signable;
import classes.User;
import classes.UserInfo;
import exceptions.ConnectException;
import exceptions.ServerFullException;
import exceptions.SignInException;
import exceptions.SignUpException;
import exceptions.UpdateException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Locale;
import java.util.ResourceBundle;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementacion con los tipos de peticiones del cliente y conexion al
 * servidor.
 *
 * @author Jonathan Camacho y Alain Cosgaya
 */
public class UiLogicImplementation implements Signable {

    private static final Logger LOGGER = Logger.getLogger(UiLogicImplementation.class.getName());

    /**
     * Metodo de SignIn que establece una conexion con el servidor mandandole la
     * peticion del cliente y el usuario. Al recibir la respuesta del servidor,
     * se hace una comprobacion de si ha saltado algun tipo de excepcion en el
     * servidor.
     *
     * @param message Usuario enviado desde el controlador de la ventana
     * @return El usuario con los datos recuperados del servidor
     * @throws ConnectException
     * @throws SignInException
     * @throws UpdateException
     * @throws ServerFullException
     */
    @Override
    public User signIn(User message) throws ConnectException, SignInException, UpdateException, ServerFullException {
        ResourceBundle configFile = ResourceBundle.getBundle("archives.config");
        Socket socket = null;
        ObjectOutputStream out;
        ObjectInputStream in;
        UserInfo userInfo = new UserInfo(message, MessageType.SIGNIN_REQUEST);

        try {
            LOGGER.info("Prepara socket para su conexion con el servidor");
            // Inicializa un socket con la IP y el puerto asignado
            socket = new Socket(configFile.getString("serverHost"), Integer.valueOf(configFile.getString("port")));
            LOGGER.info("Cliente iniciado");
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            LOGGER.info("Manda la clase de encapsulacion con la peticion y el usuario");
            // Envia el objeto de tipo UserInfo al lado servidor
            out.writeObject(userInfo);
            LOGGER.info("Recibe la respuesta del servidor");
            // Recibe la respuesta del servidor
            userInfo = (UserInfo) in.readObject();
            // Comprobacion del tipo de mensaje recibido para lanzar
            // una excepcion
            switch (userInfo.getMessage()) {
                case SIGNIN_EXCEPTION:
                    throw new SignInException("Los parametros introducidos no corresponden a ningún cliente");
                case UPDATE_EXCEPTION:
                    throw new UpdateException("Error al intentar registrar la conexión en la base de datos");
                case CONNECT_EXCEPTION:
                    throw new ConnectException("Error al intentar abrir/cerrar la conexion al servidor, intentelo mas tarde");
                case SERVER_FULL_EXCEPTION:
                    throw new ServerFullException("El servidor esta lleno, intente conectarse mas tarde");
            }

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(UiLogicImplementation.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new ConnectException("Error al intentar conectarse al servidor, intentelo mas tarde");
        }

        return userInfo.getUser();
    }

    /**
     * Metodo de SignUp que establece una conexion con el servidor mandandole la
     * peticion del cliente y el usuario. Al recibir la respuesta del servidor,
     * se hace una comprobacion de si ha saltado algun tipo de excepcion en el
     * servidor.
     *
     * @param message Usuario enviado desde el controlador de la ventana
     * @return El usuario con sus datos.
     * @throws ConnectException
     * @throws SignUpException
     * @throws UpdateException
     * @throws ServerFullException
     */
    @Override
    public User signUp(User message) throws ConnectException, SignUpException, UpdateException, ServerFullException {
        ResourceBundle configFile = ResourceBundle.getBundle("archives.config");
        Socket socket = null;
        ObjectOutputStream out;
        ObjectInputStream in;
        UserInfo userInfo = new UserInfo(message, MessageType.SIGNUP_REQUEST);
        try {
            LOGGER.info("Prepara socket para su conexion con el servidor");
            // Inicializa un socket con la IP y el puerto asignado
            socket = new Socket(configFile.getString("serverHost"), Integer.valueOf(configFile.getString("port")));
            LOGGER.info("Cliente iniciado");
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            LOGGER.info("Manda la clase de encapsulacion con la peticion y el usuario");
            // Envia el objeto de tipo UserInfo al lado servidor
            out.writeObject(userInfo);
            LOGGER.info("Recibe la respuesta del servidor");
            // Recibe la respuesta del servidor
            userInfo = (UserInfo) in.readObject();
            // Comprobacion del tipo de mensaje recibido para lanzar
            // una excepcion
            switch (userInfo.getMessage()) {
                case SIGNUP_EXCEPTION:
                    throw new SignUpException("El email y/o el nombre de usuario introducidos ya corresponden a un cliente");
                case UPDATE_EXCEPTION:
                    throw new UpdateException("Error al intentar registrar la conexión en la base de datos");
                case CONNECT_EXCEPTION:
                    throw new ConnectException("Error al intentar abrir/cerrar la conexion al servidor, intentelo mas tarde");
                case SERVER_FULL_EXCEPTION:
                    throw new ServerFullException("El servidor esta lleno, intente conectarse mas tarde");
            }

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(UiLogicImplementation.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new ConnectException("Error al intentar conectarse al servidor, intentelo mas tarde");
        }
        return userInfo.getUser();
    }

}
