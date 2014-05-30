package com.xuan.game.tetris;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

/**
 * 游戏画布
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-5-30 下午5:52:00 $
 */
public class TetrisView extends View implements Runnable {
    public static final String TAG = "TetrisView";

    public final static int SCREEN_WIDTH = 320;
    public final static int SCREEN_HEIGHT = 455;

    public final int STATE_MENU = 0;
    public final int STATE_PLAY = 1;
    public final int STATE_PAUSE = 2;
    public final int STATE_OVER = 3;
    private int gamestate = STATE_PLAY;// 游戏的运行状态

    public static final int MAX_LEVEL = 6;

    public static final String DATAFILE = "save.dt";

    int score = 0;
    int speed = 1;
    int deLine = 0;

    private boolean isCombo = false;// 判断是否落地
    boolean isPaused = false;
    boolean isVoice = true;

    private long moveDelay = 600;// 每移动的时间
    private long lastMove = 0;

    private final Paint paint = new Paint();

    RefreshHandler refreshHandler = null;

    TileView currentTile = null;
    TileView nextTile = null;
    Court court = null;
    ResourceStore resourceStore = null;

    MusicPlayer musicPlayer = null;

    public TetrisView(Context context) {
        super(context);
        init();
    }

    // 游戏初始化
    private void init() {
        currentTile = new TileView(getContext());
        nextTile = new TileView(getContext());

        court = new Court(getContext());
        refreshHandler = new RefreshHandler(this);
        resourceStore = new ResourceStore(getContext());
        musicPlayer = new MusicPlayer(getContext());

        setLevel(1);

        paint.setAntiAlias(true);
        paint.setColor(Color.RED);

        setFocusable(true);
        new Thread(this).start();
    }

    /**
     * 每次操作
     */
    public void logic() {
        switch (gamestate) {
        case STATE_MENU:
            gamestate = STATE_PLAY;
            break;
        case STATE_PLAY:
            playGame();
            break;
        case STATE_PAUSE:
            break;
        case STATE_OVER:
            break;
        }
    }

    /**
     * 初始化关卡，启动游戏
     */
    public void startGame() {
        gamestate = STATE_PLAY;
        court.clearCourt();
        currentTile = new TileView(getContext());
        nextTile = new TileView(getContext());

        setLevel(1);
        score = 0;
        deLine = 0;
        isPaused = false;
        isCombo = false;

        playGame();
    }

    /**
     * 每次游戏步骤
     */
    public void playGame() {
        long now = System.currentTimeMillis();
        if (now - lastMove > moveDelay) {
            if (isPaused) {
                return;
            }

            if (isCombo) {
                court.placeTile(currentTile);
                musicPlayer.playMoveVoice();

                if (court.isGameOver()) {
                    gamestate = STATE_OVER;
                    return;
                }
                int line = court.removeLines();
                if (line > 0) {
                    musicPlayer.playBombVoice();
                }
                deLine += line;
                countScore(line);

                currentTile = nextTile;
                nextTile = new TileView(getContext());

                isCombo = false;
            }

            moveDown();
            lastMove = now;
        }
    }

    private void countScore(int line) {
        switch (line) {
        case 1:
            score += 100;
            break;
        case 2:
            score += 300;
            break;
        case 3:
            score += 600;
            break;
        case 4:
            score += 1000;
            break;
        default:
            ;
        }
        if (score >= 2000 && score < 4000) {
            setLevel(2);
        }
        else if (score >= 4000 && score < 6000) {
            setLevel(3);
        }
        else if (score >= 6000 && score < 8000) {
            setLevel(4);
        }
        else if (score >= 8000 && score < 10000) {
            setLevel(5);
        }
        else if (score >= 10000) {
            setLevel(6);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (gamestate) {
        case STATE_MENU:
            paintMenu(canvas);
            break;
        case STATE_PLAY:
            paintGame(canvas);
            break;
        case STATE_PAUSE:
            paintPause(canvas);
            break;
        case STATE_OVER:
            paintOver(canvas);
            break;
        default:
            ;
        }
    }

    public boolean isGameOver() {
        return court.isGameOver();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_DPAD_UP:
            if (gamestate == STATE_PLAY) {
                if (!isPaused) {
                    rotate();
                    musicPlayer.playMoveVoice();
                }
            }
            else if (gamestate == STATE_PAUSE) {
            }
            else if (gamestate == STATE_MENU) {

            }
            break;
        case KeyEvent.KEYCODE_DPAD_DOWN:
            if (gamestate == STATE_PLAY) {
                if (!isPaused) {
                    moveDown();
                    musicPlayer.playMoveVoice();
                }
            }
            else if (gamestate == STATE_PAUSE) {
            }
            else if (gamestate == STATE_MENU) {

            }
            break;
        case KeyEvent.KEYCODE_DPAD_LEFT:
            if (gamestate == STATE_PLAY) {
                if (!isPaused) {
                    moveLeft();
                    musicPlayer.playMoveVoice();
                }
            }
            else if (gamestate == STATE_PAUSE) {
            }
            else if (gamestate == STATE_MENU) {

            }
            break;
        case KeyEvent.KEYCODE_DPAD_RIGHT:
            if (gamestate == STATE_PLAY) {
                if (!isPaused) {
                    moveRight();
                    musicPlayer.playMoveVoice();
                }
            }
            else if (gamestate == STATE_PAUSE) {
            }
            else if (gamestate == STATE_MENU) {

            }
            break;
        case KeyEvent.KEYCODE_ENTER:
            ;
        case KeyEvent.KEYCODE_DPAD_CENTER:
            if (gamestate == STATE_PLAY) {
                if (!isPaused) {
                    fastDrop();
                    musicPlayer.playMoveVoice();
                }
            }
            else if (gamestate == STATE_PAUSE) {
            }
            else if (gamestate == STATE_MENU) {
            }
            break;
        //
        case KeyEvent.KEYCODE_S:
            if (gamestate == STATE_PLAY) {
                isPaused = true;
            }
            else if (gamestate == STATE_PAUSE) {
                isPaused = false;
            }
            else if (gamestate == STATE_MENU) {

            }
            break;
        case KeyEvent.KEYCODE_SPACE:
            isPaused = !isPaused;
            if (isPaused) {
                refreshHandler.pause();
            }
            else {
                refreshHandler.resume();
            }
            break;

        default:
            ;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void rotate() {
        if (!isCombo) {
            currentTile.rotateOnCourt(court);
        }
    }

    private void moveDown() {
        if (!isCombo) {
            if (!currentTile.moveDownOnCourt(court)) {
                isCombo = true;
            }
        }
    }

    private void moveLeft() {
        if (!isCombo) {
            currentTile.moveLeftOnCourt(court);

        }
    }

    private void moveRight() {
        if (!isCombo) {
            currentTile.moveRightOnCourt(court);

        }

    }

    private void fastDrop() {
        if (!isCombo) {
            currentTile.fastDropOnCourt(court);
            isCombo = true;
        }
    }

    private void paintMenu(Canvas canvas) {
        DrawTool.paintImage(canvas, resourceStore.getMenuBackground(), 0, 0);
        DrawTool.paintImage(canvas, resourceStore.getMenu(), 0, SCREEN_HEIGHT / 2 - resourceStore.getMenu().getHeight()
                / 2);

    }

    private void paintGame(Canvas canvas) {
        court.paintCourt(canvas);
        currentTile.paintTile(canvas);
        // nextTile.paintTile(canvas);

        paint.setTextSize(20);
        paintNextTile(canvas);
        paintSpeed(canvas);
        paintScore(canvas);
        paintDeLine(canvas);
    }

    private void paintNextTile(Canvas canvas) {
        int i, j;
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                if (nextTile.tile[i][j] != 0) {
                    DrawTool.paintImage(
                            canvas,
                            resourceStore.getBlock(nextTile.getColor() - 1),
                            (int) (Court.BEGIN_DRAW_X + getBlockDistance(Court.COURT_WIDTH) + getBlockDistance((float) (i + 0.5))),
                            (int) (getBlockDistance((float) (j + 0.5))));
                }
            }
        }
    }

    private void paintSpeed(Canvas canvas) {
        paint.setColor(Color.BLUE);
        canvas.drawText("�ȼ�:", getBlockDistance(Court.COURT_WIDTH) + getRightMarginToCourt(), getBlockDistance(9),
                paint);
        paint.setColor(Color.RED);
        canvas.drawText(String.valueOf(speed), getBlockDistance(Court.COURT_WIDTH) + 2 * getRightMarginToCourt(),
                getBlockDistance(11), paint);
    }

    private void paintScore(Canvas canvas) {
        paint.setColor(Color.BLUE);
        canvas.drawText("�÷�:", getBlockDistance(Court.COURT_WIDTH) + getRightMarginToCourt(), getBlockDistance(13),
                paint);
        paint.setColor(Color.RED);
        canvas.drawText(String.valueOf(score), getBlockDistance(Court.COURT_WIDTH) + 2 * getRightMarginToCourt(),
                getBlockDistance(15), paint);
    }

    private void paintDeLine(Canvas canvas) {
        paint.setColor(Color.BLUE);
        canvas.drawText("��ȥ����:", getBlockDistance(Court.COURT_WIDTH) + getRightMarginToCourt(),
                getBlockDistance(17), paint);
        paint.setColor(Color.RED);
        canvas.drawText(String.valueOf(deLine), getBlockDistance(Court.COURT_WIDTH) + 2 * getRightMarginToCourt(),
                getBlockDistance(19), paint);
    }

    private float getBlockDistance(float blockNum) {
        return blockNum * Court.BLOCK_WIDTH;
    }

    private float getRightMarginToCourt() {
        return (float) 10.0;
    }

    private void paintPause(Canvas canvas) {

    }

    private void paintOver(Canvas canvas) {
        paintGame(canvas);
        Paint paint = new Paint();
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        paint.setARGB(0xe0, 0xff, 0x00, 0x00);
        canvas.drawText("Game Over", getBlockDistance(1), getBlockDistance(Court.COURT_HEIGHT / 2 - 2), paint);
        // DrawTool.paintImage(canvas,resourceStore.getGameover(),0,SCREEN_HEIGHT/2 -
        // resourceStore.getGameover().getHeight()/2 );
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Message message = new Message();
            message.what = RefreshHandler.MESSAGE_REFRESH;
            refreshHandler.sendMessage(message);

            try {
                Thread.sleep(moveDelay);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void setLevel(int level) {
        speed = level;
        moveDelay = (long) (600 * (1.0 - speed / 7.0));
    }

    public void setVoice(boolean isVoice) {
        this.isVoice = isVoice;
        musicPlayer.setMute(!isVoice);
    }

    public void restoreGame() {
        Properties pro = new Properties();
        try {
            FileInputStream in = getContext().openFileInput(DATAFILE);
            pro.load(in);
            in.close();
        }
        catch (IOException e) {
            Log.i(TAG, "file open failed in restoreGame()");
            return;
        }

        gamestate = Integer.valueOf(pro.get("gamestate").toString());
        speed = Integer.valueOf(pro.get("speed").toString());
        setLevel(speed);
        score = Integer.valueOf(pro.get("score").toString());
        deLine = Integer.valueOf(pro.get("deLine").toString());
        isVoice = Boolean.valueOf(pro.get("isVoice").toString());
        isCombo = Boolean.valueOf(pro.get("isCombo").toString());
        isPaused = Boolean.valueOf(pro.get("isPaused").toString());

        restoreCourt(pro);
        restoreTile(pro, currentTile);
        restoreTile(pro, nextTile);
    }

    private void restoreCourt(Properties pro) {
        int[][] matrix = court.getMatrix();
        int i, j;
        for (i = 0; i < Court.COURT_WIDTH; i++) {
            for (j = 0; j < Court.COURT_HEIGHT; j++) {
                matrix[i][j] = Integer.valueOf(pro.get("courtMatrix" + i + j).toString());
            }
        }
    }

    private void restoreTile(Properties pro, TileView tile) {
        int[][] matrix = tile.getMatrix();
        int i, j;
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                matrix[i][j] = Integer.valueOf(pro.get("tileMatrix" + i + j).toString());
            }
        }
        tile.setColor(Integer.valueOf(pro.get("tileColor").toString()));
        tile.setShape(Integer.valueOf(pro.get("tileShape").toString()));
        tile.setOffsetX(Integer.valueOf(pro.get("tileOffsetX").toString()));
        tile.setOffsetY(Integer.valueOf(pro.get("tileOffsetY").toString()));
    }

    public void saveGame() {
        Properties pro = new Properties();

        pro.put("gamestate", String.valueOf(gamestate));
        pro.put("speed", String.valueOf(speed));
        pro.put("score", String.valueOf(score));
        pro.put("deLine", String.valueOf(deLine));
        Boolean b = new Boolean(isVoice);
        pro.put("isVoice", b.toString());
        b = new Boolean(isCombo);
        pro.put("isCombo", b.toString());
        b = new Boolean(isPaused);
        pro.put("isPaused", b.toString());

        saveCourt(pro);
        saveTile(pro, currentTile);
        saveTile(pro, nextTile);

        try {
            FileOutputStream stream = getContext().openFileOutput(DATAFILE, Context.MODE_WORLD_WRITEABLE);
            pro.store(stream, "");
            stream.close();
        }
        catch (IOException e) {
            Log.i(TAG, "ioexeption in saveGame()");
            return;
        }
    }

    private void saveCourt(Properties pro) {
        int[][] ct = court.getMatrix();
        int i, j;
        for (i = 0; i < Court.COURT_WIDTH; i++) {
            for (j = 0; j < Court.COURT_HEIGHT; j++) {
                pro.put("courtMatrix" + i + j, String.valueOf(ct[i][j]));
            }
        }
    }

    private void saveTile(Properties pro, TileView tile) {
        int[][] matrix = tile.getMatrix();
        int i, j;
        for (i = 0; i < 4; i++) {
            for (j = 0; j < 4; j++) {
                pro.put("tileMatrix" + i + j, String.valueOf(matrix[i][j]));
            }
        }
        pro.put("tileColor", String.valueOf(tile.getColor()));
        pro.put("tileShape", String.valueOf(tile.getShape()));
        pro.put("tileOffsetX", String.valueOf(tile.getOffsetX()));
        pro.put("tileOffsetY", String.valueOf(tile.getOffsetY()));
    }

    public void onPause() {
        refreshHandler.pause();
        isPaused = true;

    }

    public void onResume() {
        refreshHandler.resume();
        isPaused = false;
    }

    public void freeResources() {
        musicPlayer.free();
    }

}
