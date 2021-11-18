package controller;

import application.App;
import java.util.concurrent.TimeoutException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

/**
 *
 * @author Idoia Ormaetxea y Alejandro Gomez
 */
@FixMethodOrder (MethodSorters.NAME_ASCENDING)
public class SessionControllerIT extends ApplicationTest{
    
    @BeforeClass
    public static void setUp() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(App.class);
    }
    
    /**
     * Test de logeo correcto.
     */
    @Test
    public void testA_CorrectLogin() {
        clickOn("#textUser");
        write("idoia");
        clickOn("#textPasswd");
        write("abcd*1234");
        verifyThat("#btnLogin", isEnabled());
        clickOn("#btnLogin");
        verifyThat("#bPane", isVisible());
    }
    /**
     * Test de pulsacion de boton de logOut.
     */
    @Test
    @Ignore
    public void testB_LogOut() {
        clickOn("#window");
        clickOn("#cerrarSesion");
        verifyThat("#paneVentana", isVisible());
    }
    /**
     * Test de pulsacion de logOut.
     */
    @Test
    @Ignore
    public void testC_Salir() {
        clickOn("#window");
        clickOn("#cerrarVentana");
    }
    /**
     * Test de cerrar ventana no exitoso.
     */
    @Test
    public void testD_SalirVentana() {
        clickOn("#window");
        clickOn("#cerrarVentana");
        clickOn("Cancelar");
        verifyThat("#bPane", isVisible());
    }
}
