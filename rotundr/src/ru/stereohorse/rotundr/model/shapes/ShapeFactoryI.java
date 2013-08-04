package ru.stereohorse.rotundr.model.shapes;

import ru.stereohorse.rotundr.model.Block;

public class ShapeFactoryI extends Shape.ShapeFactory {
    @Override
    protected Block[][] createShapeStates( Block.BlockFactory blockFactory ) {
        return new Block[][]{
                new Block[]{
                        blockFactory.produce( 0, 0 ),
                        blockFactory.produce( 0, 1 ),
                        blockFactory.produce( 0, 2 ),
                        blockFactory.produce( 0, 3 )
                },
                new Block[]{
                        blockFactory.produce( 0, 0 ),
                        blockFactory.produce( 1, 0 ),
                        blockFactory.produce( 2, 0 ),
                        blockFactory.produce( 3, 0 )
                }
        };
    }
}

