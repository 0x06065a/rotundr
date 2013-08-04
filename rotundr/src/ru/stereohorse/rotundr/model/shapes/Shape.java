package ru.stereohorse.rotundr.model.shapes;

import ru.stereohorse.rotundr.model.Block;
import ru.stereohorse.rotundr.model.Field;
import ru.stereohorse.rotundr.model.gui.BlockVisual;

public class Shape {
    protected Block[][] shapeStates;

    // coordinates of left bottom corner
    private int x = Field.WIDTH / 2;
    private int y = Field.HEIGHT;

    private int currentState;

    public Block[] getRotatedBlocks() {
        return shapeStates[ nextState() ];
    }

    public Block[] getBlocks() {
        return shapeStates[ currentState ];
    }

    public void rotate() {
        currentState = nextState();
    }

    private int nextState() {
        return shapeStates.length == currentState + 1 ? 0 : currentState + 1;
    }

    public static abstract class ShapeFactory {
        public Shape produce( BlockVisual blockVisual ) {
            Shape shape = new Shape();
            Block.BlockFactory blockFactory = new Block.BlockFactory( blockVisual );
            shape.shapeStates = createShapeStates( blockFactory );
            return shape;
        }

        protected abstract Block[][] createShapeStates( Block.BlockFactory blockFactory );
    }

    public int getX() {
        return x;
    }

    public void setX( int x ) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY( int y ) {
        this.y = y;
    }
}
