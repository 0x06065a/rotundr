package ru.stereohorse.rotundr.game;

import ru.stereohorse.rotundr.model.Field;
import ru.stereohorse.rotundr.model.shapes.Shape;
import ru.stereohorse.rotundr.model.shapes.ShapeFactoryI;
import ru.stereohorse.rotundr.model.shapes.ShapeFactoryS;

import java.util.Random;

public class GameState {
    private static final float INITIAL_PERIOD = 1f;
    private static final float PERIOD_DEGRADATION = 0.01f;

    private Field field = new Field();

    private Shape currentShape;
    private static final Shape.ShapeFactory[] shapeFactories = new Shape.ShapeFactory[]{
            new ShapeFactoryI(),
            new ShapeFactoryS()
    };

    private float period = INITIAL_PERIOD;
    private float periodLeft = period;

    private Random random = new Random();

    public void update( float delta ) {
        if ( !tick( delta ) ) {
            return;
        }

        if ( currentShape == null ) {
            currentShape = shapeFactories[random.nextInt( shapeFactories.length )].produce( 1 );
        }

        currentShape.setY( currentShape.getY() - 1 );
    }

    private boolean tick( float delta ) {
        periodLeft -= delta;
        if ( periodLeft > 0f ) {
            return false;
        }

        period -= PERIOD_DEGRADATION;
        periodLeft = period;

        return true;
    }

    public Shape getCurrentShape() {
        return currentShape;
    }

    public Field getField() {
        return field;
    }
}
