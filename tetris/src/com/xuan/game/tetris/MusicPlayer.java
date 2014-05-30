package com.xuan.game.tetris;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * 声音播放器
 * 
 * @author xuan
 * @version $Revision: 1.0 $, $Date: 2014-1-13 下午7:22:26 $
 */
public class MusicPlayer {
    private MediaPlayer moveVoice = null;
    private MediaPlayer bombVoice = null;
    private boolean isMute = false;// 是否静音

    public MusicPlayer(Context context) {
        moveVoice = MediaPlayer.create(context, R.raw.move);
        bombVoice = MediaPlayer.create(context, R.raw.bomb);
    }

    public void playMoveVoice() {
        if (isMute) {
            return;
        }
        moveVoice.start();
    }

    public void playBombVoice() {
        if (isMute) {
            return;
        }
        bombVoice.start();
    }

    public boolean isMute() {
        return isMute;
    }

    public void setMute(boolean isMute) {
        this.isMute = isMute;
    }

    public void free() {
        moveVoice.release();
        bombVoice.release();
    }

}
