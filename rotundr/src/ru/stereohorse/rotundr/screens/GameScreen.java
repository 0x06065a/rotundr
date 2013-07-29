package ru.stereohorse.rotundr.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import ru.stereohorse.rotundr.model.Field;

public class GameScreen extends AbstractScreen {
    private Field field;
    private Actor fieldActor;

    private float cellSize;

    private ShapeRenderer shapeRenderer;

    public GameScreen( Game game ) {
        super( game );
    }

    @Override
    public void resize( int width, int height ) {
        float cellWidth = width / (float) Field.WIDTH;
        float cellHeight = height / (float) Field.HEIGHT;
        cellSize = cellHeight > cellWidth ? cellWidth : cellHeight;
    }

    @Override
    public void show() {
        super.show();

        if ( field == null ) {
            field = new Field();
        }

        if ( fieldActor == null ) {
            fieldActor = new FieldActor();
            stage.addActor( fieldActor );
        }

        if ( shapeRenderer == null ) {
            shapeRenderer = new ShapeRenderer();
        }
    }

    private class FieldActor extends Actor {

        @Override
        public void draw( SpriteBatch batch, float parentAlpha ) {
            drawBackground();
            drawBlocks( batch );
        }

        private void drawBlocks( SpriteBatch batch ) {

        }

        private void drawBackground() {
            int offsetX = (int) (( Gdx.graphics.getWidth() - cellSize * Field.WIDTH ) / 2);
            int offsetY = (int) (( Gdx.graphics.getHeight() - cellSize * Field.HEIGHT ) / 2);

            shapeRenderer.begin( ShapeRenderer.ShapeType.Rectangle );
            shapeRenderer.rect( offsetX, offsetY, cellSize * Field.WIDTH, cellSize * Field.HEIGHT );
            shapeRenderer.end();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        if ( shapeRenderer != null ) {
            shapeRenderer.dispose();
        }
    }
}
