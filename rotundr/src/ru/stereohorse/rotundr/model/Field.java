package ru.stereohorse.rotundr.model;

import java.util.HashMap;
import java.util.Map;

public class Field {
    public static final int HEIGHT = 20;
    public static final int WIDTH = 10;

    private Map<Integer,Block> blocks = new HashMap<Integer, Block>( WIDTH * HEIGHT );

    public int getCellColorIndex( int x, int y ) {
        Block block = blocks.get( y * WIDTH + x );
        return block == null ? -1 : block.getColorIndex();
    }

    private class Block {
        private int colorIndex;

        private int getColorIndex() {
            return colorIndex;
        }

        private void setColorIndex( int colorIndex ) {
            this.colorIndex = colorIndex;
        }
    }
}
