package ru.stereohorse.rotundr.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import ru.stereohorse.rotundr.game.GameState;
import ru.stereohorse.rotundr.model.Block;
import ru.stereohorse.rotundr.model.Field;

public class GameScreen extends AbstractScreen {
    private GameState gameState;
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

        if ( gameState == null ) {
            gameState = new GameState();
        }

        if ( fieldActor == null ) {
            fieldActor = new FieldActor();
            stage.addActor( fieldActor );
        }

        if ( shapeRenderer == null ) {
            shapeRenderer = new ShapeRenderer();
        }
    }

    @Override
    public void render( float delta ) {
        super.render( delta );
        gameState.update( delta );
    }

    private class FieldActor extends Actor {
        @Override
        public void draw( SpriteBatch batch, float parentAlpha ) {
            int offsetX = (int) (( Gdx.graphics.getWidth() - cellSize * Field.WIDTH ) / 2);
            int offsetY = (int) (( Gdx.graphics.getHeight() - cellSize * Field.HEIGHT ) / 2);

            drawBackground( offsetX, offsetY );
            drawBlocks( batch, offsetX, offsetY );
        }

        private void drawBlocks( SpriteBatch batch, int offsetX, int offsetY ) {
            shapeRenderer.begin( ShapeRenderer.ShapeType.FilledRectangle );
            shapeRenderer.setColor( Color.GREEN );

            // draw field blocks
            for ( Block block : gameState.getField().getBlocks() ) {
                shapeRenderer.filledRect( offsetX + block.getX() * cellSize, offsetY + block.getY() * cellSize, cellSize, cellSize );
            }

            // draw shape blocks
            if ( gameState.getCurrentShape() != null ) {
                for ( Block block : gameState.getCurrentShape().getBlocks() ) {
                    shapeRenderer.filledRect(
                            offsetX + ( gameState.getCurrentShape().getX() + block.getX() ) * cellSize,
                            offsetY + ( gameState.getCurrentShape().getY() + block.getY() ) * cellSize,
                            cellSize, cellSize
                    );
                }
            }

            shapeRenderer.end();
        }

        private void drawBackground( float offsetX, float offsetY ) {
            shapeRenderer.begin( ShapeRenderer.ShapeType.Rectangle );
            shapeRenderer.setColor( Color.RED );
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
