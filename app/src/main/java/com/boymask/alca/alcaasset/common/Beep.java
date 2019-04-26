package com.boymask.alca.alcaasset.common;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Handler;
import android.os.Looper;

public class Beep {
    private static final int DURATION = 400;

    public static void play() {
        final ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 200);
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_PIP, DURATION);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (toneGenerator != null) {
                    //  Log.d(TAG, "ToneGenerator released");
                    toneGenerator.release();
                    //  toneGenerator = null;
                }
            }

        }, DURATION);
    }
}
