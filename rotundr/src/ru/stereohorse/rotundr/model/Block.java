package ru.stereohorse.rotundr.model;

public class Block {
    private int colorIndex;
    private int x;
    private int y;

    public static class BlockFactory {
        private int colorIndex;

        public BlockFactory( int colorIndex ) {
            this.colorIndex = colorIndex;
        }

        public Block produce( int x, int y ) {
            Block block = new Block();
            block.setX( x );
            block.setY( y );
            block.setColorIndex( colorIndex );

            return block;
        }
    }

    public int getColorIndex() {
        return colorIndex;
    }

    public void setColorIndex( int colorIndex ) {
        this.colorIndex = colorIndex;
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