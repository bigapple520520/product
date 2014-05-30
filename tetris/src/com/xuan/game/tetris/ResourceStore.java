package com.xuan.game.tetris;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 资源类
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-5-30 下午4:46:26 $
 */
public class ResourceStore {
    private static Bitmap background = null;
    private static Bitmap[] blocks = null;
    private static Bitmap menuBackground = null;
    private static Bitmap menu = null;
    private static Bitmap speed = null;
    private static Bitmap line = null;
    private static Bitmap score = null;
    private static Bitmap gameover = null;

    private Context context = null;

    public ResourceStore(Context context) {
        this.context = context;
        Resources resources = context.getResources();
        if (null == background) {
            background = createImage(resources.getDrawable(R.drawable.courtbg), Court.COURT_WIDTH * Court.BLOCK_WIDTH,
                    TetrisView.SCREEN_HEIGHT);
        }

        if (null == menuBackground) {
            menuBackground = createImage(resources.getDrawable(R.drawable.menubg), TetrisView.SCREEN_WIDTH,
                    TetrisView.SCREEN_HEIGHT);
        }

        if (null == menu) {
            menu = createImage(resources.getDrawable(R.drawable.menu), 200, 100);
        }

        if (null == speed) {
            speed = createImage(resources.getDrawable(R.drawable.speed), 200, 100);
        }

        if (null == line) {
            line = createImage(resources.getDrawable(R.drawable.line), 200, 100);
        }

        if (null == score) {
            score = createImage(resources.getDrawable(R.drawable.score), 200, 100);
        }

        if (null == gameover) {
            gameover = createImage(resources.getDrawable(R.drawable.gameover), 200, 100);
        }

        if (null == blocks) {
            blocks = new Bitmap[8];
            for (int i = 0; i < 8; i++) {
                blocks[i] = createImage(resources.getDrawable(R.drawable.block0 + i), Court.BLOCK_WIDTH,
                        Court.BLOCK_WIDTH);
            }
        }
    }

    public Bitmap getCourtBackground() {
        return background;
    }

    public Bitmap getMenuBackground() {
        return menuBackground;
    }

    public Bitmap getMenu() {
        return menu;
    }

    public Bitmap getBlock(int index) {
        return blocks[index];
    }

    public Bitmap getGameover() {
        return gameover;
    }

    public void loadImage(int index, Bitmap bitmap) {
        bitmap = ((BitmapDrawable) context.getResources().getDrawable(index)).getBitmap();
    }

    /**
     * 从资源中获取图片
     * 
     * @param drawable
     * @param w
     * @param h
     * @return
     */
    public static Bitmap createImage(Drawable drawable, int w, int h) {
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

}
