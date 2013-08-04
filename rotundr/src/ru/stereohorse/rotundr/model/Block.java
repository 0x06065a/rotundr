package ru.stereohorse.rotundr.model;

import ru.stereohorse.rotundr.model.gui.BlockVisual;

public class Block {
    private BlockVisual blockVisual;
    private int x;
    private int y;

    public static class BlockFactory {
        private BlockVisual blockVisual;

        public BlockFactory( BlockVisual blockVisual ) {
            this.blockVisual = blockVisual;
        }

        public Block produce( int x, int y ) {
            Block block = new Block();
            block.x = x;
            block.y = y;
            block.blockVisual = blockVisual;

            return block;
        }
    }

    public BlockVisual getBlockVisual() {
        return blockVisual;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX( int x ) {
        this.x = x;
    }

    public void setY( int y ) {
        this.y = y;
    }
}