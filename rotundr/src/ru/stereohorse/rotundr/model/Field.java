package ru.stereohorse.rotundr.model;

import java.util.HashMap;
import java.util.Map;

public class Field {
    public static final int HEIGHT = 20;
    public static final int WIDTH = 10;

    private Map<Integer, Block> blocks = new HashMap<Integer, Block>( WIDTH * HEIGHT );

    public int getCellColorIndex( int x, int y ) {
        Block block = blocks.get( y * WIDTH + x );
        return block == null ? -1 : block.getColorIndex();
    }

    public void setCellColorIndex( int x, int y, int colorIndex ) {
        int key = y * WIDTH + x;
        Block block = blocks.get( key );

        if ( block == null && x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT ) {
            block = new Block();
            block.setX( x );
            block.setY( y );
            blocks.put( key, block );
        }

        if ( block != null ) {
            block.setColorIndex( colorIndex );
        }
    }

    public Iterable<Block> getBlocks() {
        return blocks.values();
    }
}
