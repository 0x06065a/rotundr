package ru.stereohorse.rotundr;

import com.badlogic.gdx.Game;
import ru.stereohorse.rotundr.screens.GameScreen;

public class Rotundr extends Game {
    private GameScreen gameScreen;

    @Override
    public void create() {
        if ( gameScreen == null ) {
            gameScreen = new GameScreen();
        }
        setScreen( gameScreen );
    }

    @Override
    public void dispose() {
        super.dispose();
        Util.dispose( gameScreen );
    }
}
