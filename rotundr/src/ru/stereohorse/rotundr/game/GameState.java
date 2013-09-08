package ru.stereohorse.rotundr.game;

import ru.stereohorse.rotundr.model.Block;
import ru.stereohorse.rotundr.model.Field;
import ru.stereohorse.rotundr.model.gui.BlockVisual;
import ru.stereohorse.rotundr.model.shapes.*;

import java.util.Random;

public abstract class GameState {
    private static final float INITIAL_PERIOD = 0.3f;
    private static final float PERIOD_DEGRADATION = 0.0001f;

    private Field field = new Field();

    private Shape currentShape;
    private static final Shape.ShapeFactory[] shapeFactories = new Shape.ShapeFactory[]{
            new ShapeFactoryI(),
            new ShapeFactoryS(),
            new ShapeFactoryZ(),
            new ShapeFactoryO(),
            new ShapeFactoryJ(),
            new ShapeFactoryL(),
            new ShapeFactoryT()
    };

    private float period = INITIAL_PERIOD;
    private float periodLeft = period;

    private Random random = new Random();

    public void update( float delta ) {
        if ( waitForGameTick( delta ) ) {
            return;
        }

        if ( currentShape == null ) {
            currentShape = shapeFactories[random.nextInt( shapeFactories.length )].produce( createBlockVisual() );
        }

        currentShape.setY( currentShape.getY() - 1 );
        if ( hasCollisions() ) {
            currentShape.setY( currentShape.getY() + 1 );

            for ( Block block : currentShape.getBlocks() ) {
                block.setX( currentShape.getX() + block.getX() );
                block.setY( currentShape.getY() + block.getY() );
                field.setBlock( block );
            }

            currentShape = null;

            removeFilledLines();
        }
    }

    private void removeFilledLines() {
        for ( int y = 0; y < Field.HEIGHT; y++ ) {
            boolean filledLine = true;
            for ( int x = 0; x < Field.WIDTH; x++ ) {
                if ( field.getBlock( x, y ) == null ) {
                    filledLine = false;
                    break;
                }
            }

            if ( filledLine ) {
                for ( int y2 = y; y2 < Field.HEIGHT; y2++ ) {
                    for ( int x2 = 0; x2 < Field.WIDTH; x2++ ) {
                        Block block = field.getBlock( x2, y2 + 1 );

                        if ( block == null ) {
                            field.removeBlock( x2, y2 );
                        } else {
                            field.removeBlock( x2, y2 + 1 );

                            block.setY( y2 );
                            field.setBlock( block );
                        }
                    }
                }

                y--;
            }
        }
    }

    public void rotateCurrentShape() {
        if ( currentShape == null ) {
            return;
        }

        currentShape.rotateRight();
        if ( hasCollisions() ) {
            for ( int offsetX = 1; offsetX <= currentShape.getBlocks()[0].getX(); offsetX++ ) {
                if ( moveCurrentShape( -offsetX, 0 ) ) {
                    return;
                }
            }

            currentShape.rotateLeft();
        }
    }

    public boolean moveCurrentShape( int offsetX, int offsetY ) {
        if ( currentShape == null ) {
            return false;
        }

        currentShape.setX( currentShape.getX() + offsetX );
        currentShape.setY( currentShape.getY() + offsetY );

        if ( hasCollisions() ) {
            currentShape.setX( currentShape.getX() - offsetX );
            currentShape.setY( currentShape.getY() - offsetY );

            return false;
        }

        return true;
    }

    protected abstract BlockVisual createBlockVisual();

    private boolean hasCollisions() {
        for ( Block block : currentShape.getBlocks() ) {
            int x = block.getX() + currentShape.getX();
            int y = block.getY() + currentShape.getY();

            if ( x < 0 || x > Field.WIDTH - 1 ) {
                return true;
            }

            if ( y < 0 ) {
                return true;
            }

            if ( field.getBlock( x, y ) != null ) {
                return true;
            }
        }

        return false;
    }

    private boolean waitForGameTick( float delta ) {
        periodLeft -= delta;
        if ( periodLeft > 0f ) {
            return true;
        }

        period -= PERIOD_DEGRADATION;
        periodLeft = period;

        return false;
    }

    public Shape getCurrentShape() {
        return currentShape;
    }

    public Field getField() {
        return field;
    }
}
