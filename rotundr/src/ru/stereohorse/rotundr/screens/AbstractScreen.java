package ru.stereohorse.rotundr.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.utils.Disposable;

public class AbstractScreen implements Screen, Disposable {
    protected final Game game;

    protected AbstractScreen( Game game ) {
        this.game = game;
    }

    @Override
    public void render( float delta ) {
        Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
    }

    @Override
    public void resize( int width, int height ) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}