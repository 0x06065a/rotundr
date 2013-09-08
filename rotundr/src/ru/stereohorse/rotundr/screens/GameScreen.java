package ru.stereohorse.rotundr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ru.stereohorse.rotundr.game.GameState;
import ru.stereohorse.rotundr.model.Block;
import ru.stereohorse.rotundr.model.Field;
import ru.stereohorse.rotundr.model.gui.BlockVisual;

import java.util.Random;

public class GameScreen extends AbstractScreen {
    private static final Color[] BLOCK_COLORS = new Color[]{
            new Color( 0.66f, 0f, 0f, 0f ),     // maroon
            Color.WHITE,
            new Color( 0.66f, 0f, 0.66f, 0f ),  // magenta
            new Color( 0f, 0f, 0.66f, 0f ),     // dark blue
            new Color( 0f, 0.66f, 0f, 0f ),     // green
            new Color( 0.66f, 0.33f, 0f, 0f ),  // brown
            new Color( 0f, 0.66f, 0.66f, 0f )   // cyan
    };

    private GameState gameState;

    private float cellSize;

    private ShapeRenderer shapeRenderer;

    private Random random = new Random();

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
            gameState = new GameState() {
                @Override
                protected BlockVisual createBlockVisual() {
                    BlockVisual blockVisual = new BlockVisual();
                    blockVisual.setColor( BLOCK_COLORS[random.nextInt( BLOCK_COLORS.length )] );
                    return blockVisual;
                }
            };
        }

        if ( shapeRenderer == null ) {
            shapeRenderer = new ShapeRenderer();
        }

        Gdx.input.setInputProcessor( new InputAdapter() {
            @Override
            public boolean keyDown( int keycode ) {
                if ( keycode == Input.Keys.RIGHT ) {
                    gameState.moveCurrentShape( 1, 0 );
                } else if ( keycode == Input.Keys.LEFT ) {
                    gameState.moveCurrentShape( -1, 0 );
                } else if ( keycode == Input.Keys.UP ) {
                    gameState.rotateCurrentShape();
                }

                return true;
            }
        });
    }

    @Override
    public void render( float delta ) {
        super.render( delta );

        processInput();

        gameState.update( delta );

        int offsetX = (int) ( ( Gdx.graphics.getWidth() - cellSize * Field.WIDTH ) / 2 );
        int offsetY = (int) ( ( Gdx.graphics.getHeight() - cellSize * Field.HEIGHT ) / 2 );

        drawBackground( offsetX, offsetY );
        drawBlocks( offsetX, offsetY );
    }

    private void processInput() {
        if ( Gdx.input.isKeyPressed( Input.Keys.DOWN ) ) {
            gameState.moveCurrentShape( 0, -1 );
        }
    }

    private void drawBlocks( int offsetX, int offsetY ) {
        shapeRenderer.begin( ShapeRenderer.ShapeType.FilledRectangle );

        // draw field blocks
        for ( Block block : gameState.getField().getBlocks() ) {
            shapeRenderer.setColor( block.getBlockVisual().getColor() );
            shapeRenderer.filledRect(
                    offsetX + block.getX() * cellSize,
                    offsetY + block.getY() * cellSize,
                    cellSize, cellSize
            );
        }

        // draw shape blocks
        if ( gameState.getCurrentShape() != null ) {
            for ( Block block : gameState.getCurrentShape().getBlocks() ) {
                shapeRenderer.setColor( block.getBlockVisual().getColor() );
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

    @Override
    public void dispose() {
        super.dispose();

        if ( shapeRenderer != null ) {
            shapeRenderer.dispose();
        }
    }
}
