package ru.stereohorse.rotundr;

import com.badlogic.gdx.utils.Disposable;

public class Util {
    public static void dispose( Disposable disposable ) {
        if ( disposable != null ) {
            disposable.dispose();
        }
    }
}
