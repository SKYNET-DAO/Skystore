

package com.android.wallet.widgets;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.android.wallet.constants.Constants;



public final class CurrencySymbolDrawable extends Drawable {
    private final Paint paint = new Paint();
    private final String symbol;
    private final float y;

    public CurrencySymbolDrawable(final String symbol, final float textSize, final int color, final float y) {
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);

        this.symbol = symbol + Constants.CHAR_HAIR_SPACE;
        this.y = y;
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.drawText(symbol, 0, y, paint);
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) paint.measureText(symbol);
    }

    @Override
    public int getOpacity() {
        return 0;
    }

    @Override
    public void setAlpha(final int alpha) {
    }

    @Override
    public void setColorFilter(final ColorFilter cf) {
    }
}
