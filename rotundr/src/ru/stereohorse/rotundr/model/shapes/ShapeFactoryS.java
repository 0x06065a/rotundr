package ru.stereohorse.rotundr.model.shapes;

import ru.stereohorse.rotundr.model.Block;

public class ShapeFactoryS extends Shape.ShapeFactory {
    @Override
    protected Block[][] createShapeStates( Block.BlockFactory blockFactory ) {
        return new Block[][]{
                new Block[]{
                        blockFactory.produce( 1, 0 ),
                        blockFactory.produce( 0, 1 ),
                        blockFactory.produce( 1, 1 ),
                        blockFactory.produce( 0, 2 )
                },
                new Block[]{
                        blockFactory.produce( 0, 0 ),
                        blockFactory.produce( 1, 0 ),
                        blockFactory.produce( 1, 1 ),
                        blockFactory.produce( 2, 1 )
                }
        };
    }
}
