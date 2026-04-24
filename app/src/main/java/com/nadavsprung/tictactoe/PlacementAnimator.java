package com.nadavsprung.tictactoe;

import android.os.Handler;
import android.view.View;

public class PlacementAnimator {

    public static void animate(final View v) {
        animate(v, 250);
    }

    public static void animate(final View v, final long duration) {
        final long start = System.currentTimeMillis();
        v.setAlpha(0f);
        v.setScaleX(0f);
        v.setScaleY(0f);
        final Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = System.currentTimeMillis() - start;
                float t = Math.min(1f, (float) elapsed / duration);
                v.setAlpha(t);
                v.setScaleX(t);
                v.setScaleY(t);
                if (t < 1f) {
                    h.postDelayed(this, 16);
                }
            }
        });
    }
}
