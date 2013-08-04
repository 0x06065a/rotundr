package ru.stereohorse.rotundr.model;

import java.util.HashMap;
import java.util.Map;

public class Field {
    public static final int HEIGHT = 20;
    public static final int WIDTH = 10;

    private Map<Integer, Block> blocks = new HashMap<Integer, Block>( WIDTH * HEIGHT );

    public Block getBlock( int x, int y ) {
        assert x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;

        return blocks.get( y * WIDTH + x );
    }

    public void setBlock( Block block ) {
        int x = block.getX();
        int y = block.getY();

        assert x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;

        blocks.put( y * WIDTH + x, block );
    }

    public Iterable<Block> getBlocks() {
        return blocks.values();
    }
}
