package com.chrislee.tetris;

import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * 具体每个方块的抽象
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-1-13 下午8:03:29 $
 */
public class TileView {
    int[][] tile = new int[4][4];
    Random rand = null;
    int color = 1;
    int shape = 0;
    int offsetX = (Court.COURT_WIDTH - 4) / 2 + 1;
    int offsetY = 0;

    private ResourceStore resourceStore = null;

    public TileView(Context context) {
        resourceStore = new ResourceStore(context);
        init();
    }

    private void init() {
        rand = new Random();
        shape = Math.abs(rand.nextInt() % 28);
        color = Math.abs(rand.nextInt() % 8) + 1;

        if (null == tile) {
            return;
        }

        int i, j;
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                tile[i][j] = TileStore.store[shape][i][j];
            }
        }
    }

    public boolean rotateOnCourt(Court court) {
        int tempX = 0, tempY = 0;
        int tempShape;
        int[][] tempTile = new int[4][4];

        tempShape = shape;
        if (tempShape % 4 > 0) {
            tempShape--;
        }
        else {
            tempShape += 3;
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tempTile[i][j] = TileStore.store[tempShape][i][j];
            }
        }

        tempX = offsetX;
        tempY = offsetY;
        boolean canTurn = false;

        if (court.availableForTile(tempTile, tempX, tempY)) {
            canTurn = true;
        }
        else if (court.availableForTile(tempTile, tempX - 1, tempY)) {
            canTurn = true;
            tempX--;
        }
        else if (court.availableForTile(tempTile, tempX - 2, tempY)) {
            canTurn = true;
            tempX -= 2;
        }
        else if (court.availableForTile(tempTile, tempX + 1, tempY)) {
            canTurn = true;
            tempX++;
        }
        else if (court.availableForTile(tempTile, tempX + 2, tempY)) {
            canTurn = true;
            tempX += 2;
        }

        if (canTurn) {
            shape = tempShape;
            offsetX = tempX;
            offsetY = tempY;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    tile[i][j] = tempTile[i][j];
                }
            }
            return true;
        }
        return false;
    }

    public boolean moveRightOnCourt(Court court) {
        Log.i("tetris", "here is moveRightOnCourt");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tile[i][j] != 0) {
                    if (!court.isSpace(offsetX + i + 1, offsetY + j)) {
                        return false;
                    }
                }
            }
        }
        ++offsetX;
        return true;
    }

    public boolean moveLeftOnCourt(Court court) {
        int i, j;
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                if (tile[i][j] != 0) {
                    if (!court.isSpace(offsetX + i - 1, offsetY + j)) {
                        return false;
                    }
                }
            }
        }
        offsetX--;
        return true;
    }

    public boolean moveDownOnCourt(Court court) {
        int i, j;
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                if (tile[i][j] != 0) {
                    if (!court.isSpace(offsetX + i, offsetY + j + 1) || isUnderBaseline(offsetY + j + 1)) {
                        return false;
                    }
                }
            }
        }
        offsetY++;
        return true;
    }

    public boolean fastDropOnCourt(Court court) {
        int i, j, k;
        int step = Court.COURT_HEIGHT;
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                if (tile[i][j] != 0) {
                    for (k = offsetY + j; k < Court.COURT_HEIGHT; k++) {
                        if (!court.isSpace(offsetX + i, k + 1) || isUnderBaseline(k + 1)) {
                            if (step > k - offsetY - j) {
                                step = k - offsetY - j;
                            }
                        }
                    }
                }
            }
        }
        offsetY += step;
        if (step > 0) {
            return true;
        }
        return false;
    }

    private boolean isUnderBaseline(int posY) {
        if (posY >= Court.COURT_HEIGHT) {
            return true;
        }
        return false;
    }

    public void paintTile(Canvas canvas) {
        Paint paint = new Paint();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tile[i][j] != 0) {
                    canvas.drawBitmap(resourceStore.getBlock(color - 1), Court.BEGIN_DRAW_X + (i + offsetX)
                            * Court.BLOCK_WIDTH, Court.BEGIN_DRAW_Y + (j + offsetY) * Court.BLOCK_WIDTH, paint);
                }
            }
        }
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getColor() {
        return color;
    }

    public int[][] getMatrix() {
        return tile;
    }

    public int getShape() {
        return shape;
    }

    public void setColor(int color) {
        this.color = color;

    }

    public void setShape(int shape) {
        this.shape = shape;
    }

}
