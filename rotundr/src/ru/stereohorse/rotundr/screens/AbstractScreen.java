package ru.stereohorse.rotundr.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import ru.stereohorse.rotundr.Util;

public class AbstractScreen implements Screen, Disposable {
    protected final Game game;
    protected Stage stage;

    protected AbstractScreen( Game game ) {
        this.game = game;
    }

    @Override
    public void render( float delta ) {
        Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );

        stage.act( delta );
        stage.draw();
    }

    @Override
    public void resize( int width, int height ) {
        stage.setViewport( width, height, true );
    }

    @Override
    public void show() {
        if ( stage == null ) {
            stage = new Stage( 0, 0, true );
            Gdx.input.setInputProcessor( stage );
        }
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        Util.dispose( stage );
    }
}