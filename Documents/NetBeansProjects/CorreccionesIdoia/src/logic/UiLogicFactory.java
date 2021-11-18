package logic;

import classes.Signable;


/**
 * La factoria del lado cliente.
 * @author Alain Cosgaya
 */
public class UiLogicFactory {
    /**
     * Metodo que devuelve la implementacion del lado cliente.
     * @return Implementacion
     */
    public static Signable getUiImplem(){
        Signable uiImplem = new UiLogicImplementation();
        return uiImplem;
    }
}
