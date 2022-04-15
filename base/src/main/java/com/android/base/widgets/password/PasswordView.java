package com.android.base.widgets.password;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import com.android.base.R;

import java.util.Timer;
import java.util.TimerTask;



public class PasswordView extends View {

    private Mode mode; 
    private int passwordLength;
    private long cursorFlashTime;
    private int passwordPadding;
    private int passwordSize = dp2px(40);
    private int borderColor;
    private int borderWidth;
    private int cursorPosition;
    private int cursorWidth;
    private int cursorHeight;
    private int cursorColor;
    private boolean isCursorShowing;
    private boolean isCursorEnable;
    private boolean isInputComplete;
    private int cipherTextSize;
    private boolean cipherEnable;
    private static String CIPHER_TEXT = "●"; 
    private String[] password;
    private InputMethodManager inputManager;
    private PasswordListener passwordListener;
    private Paint paint;
    private Timer timer;
    private TimerTask timerTask;
    public PasswordView(Context context) {
        super(context);
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        postInvalidate();
    }

    public enum Mode {

        UNDERLINE(0),


        RECT(1);
        private int mode;

        Mode(int mode) {
            this.mode = mode;
        }

        public int getMode() {
            return this.mode;
        }

        static Mode formMode(int mode) {
            for (Mode m : values()) {
                if (mode == m.mode) {
                    return m;
                }
            }
            throw new IllegalArgumentException();
        }
    }


    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttribute(attrs);
    }

    private void readAttribute(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PasswordView);
            mode = Mode.formMode(typedArray.getInteger(R.styleable.PasswordView_mode, Mode.UNDERLINE.getMode()));
            passwordLength = typedArray.getInteger(R.styleable.PasswordView_passwordLength, 4);
            cursorFlashTime = typedArray.getInteger(R.styleable.PasswordView_cursorFlashTime, 500);
            borderWidth = typedArray.getDimensionPixelSize(R.styleable.PasswordView_borderWidth, dp2px(3));
            borderColor = typedArray.getColor(R.styleable.PasswordView_borderColor, Color.BLACK);
            cursorColor = typedArray.getColor(R.styleable.PasswordView_cursorColor, Color.GRAY);
            isCursorEnable = typedArray.getBoolean(R.styleable.PasswordView_isCursorEnable, true);
            
            if (mode == Mode.UNDERLINE) {
                passwordPadding = typedArray.getDimensionPixelSize(R.styleable.PasswordView_passwordPadding, dp2px(15));
            } else {
                passwordPadding = typedArray.getDimensionPixelSize(R.styleable.PasswordView_passwordPadding, 0);
            }
            cipherEnable = typedArray.getBoolean(R.styleable.PasswordView_cipherEnable, true);
            typedArray.recycle();
        }
        password = new String[passwordLength];
        init();
    }

    private void init() {
        setFocusableInTouchMode(true);
        MyKeyListener MyKeyListener = new MyKeyListener();
        setOnKeyListener(MyKeyListener);
        inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        paint = new Paint();
        paint.setAntiAlias(true);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                isCursorShowing = !isCursorShowing;
                postInvalidate();
            }
        };
        timer = new Timer();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = 0;
        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                
                width = passwordSize * passwordLength + passwordPadding * (passwordLength - 1);
                break;
            case MeasureSpec.EXACTLY:
                
                width = MeasureSpec.getSize(widthMeasureSpec);
                
                passwordSize = (width - (passwordPadding * (passwordLength - 1))) / passwordLength;
                break;
        }
        setMeasuredDimension(width, passwordSize);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        
        cipherTextSize = passwordSize / 3;
        
        cursorWidth = dp2px(2);
        
        cursorHeight = passwordSize / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mode == Mode.UNDERLINE) {
            
            drawUnderLine(canvas, paint);
        } else {
            
            drawRect(canvas, paint);
        }
        
        drawCursor(canvas, paint);
        
        drawCipherText(canvas, paint,CIPHER_TEXT);
    }

    class MyKeyListener implements OnKeyListener {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            int action = event.getAction();
            if (action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    if (TextUtils.isEmpty(password[0])) {
                        return true;
                    }
                    String deleteText = delete();
                    if (passwordListener != null && !TextUtils.isEmpty(deleteText)) {
                        passwordListener.passwordChange(deleteText);
                    }
                    postInvalidate();
                    return true;
                }
                if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {

                    if (isInputComplete) {
                        return true;
                    }
                    String addText = add((keyCode - 7) + "");
                    if (passwordListener != null && !TextUtils.isEmpty(addText)) {
                        passwordListener.passwordChange(addText);
                    }
                    postInvalidate();
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_ENTER) {

                    if (passwordListener != null) {
                        passwordListener.keyEnterPress(getPassword(), isInputComplete);
                    }
                    return true;
                }
            }
            return false;
        }
    }


    private String delete() {
        String deleteText = null;
        if (cursorPosition > 0) {
            deleteText = password[cursorPosition - 1];
            password[cursorPosition - 1] = null;
            cursorPosition--;
        } else if (cursorPosition == 0) {
            deleteText = password[cursorPosition];
            password[cursorPosition] = null;
        }
        isInputComplete = false;
        return deleteText;
    }


    private String add(String c) {
        String addText = null;
        if (cursorPosition < passwordLength) {
            addText = c;
            password[cursorPosition] = c;
            cursorPosition++;
            if (cursorPosition == passwordLength) {
                isInputComplete = true;
                if (passwordListener != null) {
                    passwordListener.passwordComplete();
                }
            }
        }
        return addText;
    }


    private String getPassword() {
        StringBuffer stringBuffer = new StringBuffer();
        for (String c : password) {
            if (TextUtils.isEmpty(c)) {
                continue;
            }
            stringBuffer.append(c);
        }
        return stringBuffer.toString();
    }


    private void drawCipherText(Canvas canvas, Paint paint, String text) {

       
        paint.setColor(Color.BLACK);
        paint.setTextSize(cipherTextSize);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.FILL);
        
        Rect r = new Rect();
        canvas.getClipBounds(r);
        int cHeight = r.height();
        paint.getTextBounds(text, 0, CIPHER_TEXT.length(), r);
        float y = cHeight / 2f + r.height() / 2f - r.bottom;

        
        for (int i = 0; i < password.length; i++) {
            if (!TextUtils.isEmpty(password[i])) {
                
                if (cipherEnable) {

                    
                    canvas.drawText(CIPHER_TEXT,
                            (getPaddingLeft() + passwordSize / 2) + (passwordSize + passwordPadding) * i,
                            getPaddingTop() + y, paint);
                } else {
                    
                    canvas.drawText(password[i],
                            (getPaddingLeft() + passwordSize / 2) + (passwordSize + passwordPadding) * i,
                            getPaddingTop() + y, paint);
                }
            }


        }
    }


    private void drawCursor(Canvas canvas, Paint paint) {
        
        paint.setColor(cursorColor);
        paint.setStrokeWidth(cursorWidth);
        paint.setStyle(Paint.Style.FILL);
        
        if (!isCursorShowing && isCursorEnable && !isInputComplete && hasFocus()) {
            
            canvas.drawLine((getPaddingLeft() + passwordSize / 2) + (passwordSize + passwordPadding) * cursorPosition,
                    getPaddingTop() + (passwordSize - cursorHeight) / 2,
                    (getPaddingLeft() + passwordSize / 2) + (passwordSize + passwordPadding) * cursorPosition,
                    getPaddingTop() + (passwordSize + cursorHeight) / 2,
                    paint);
        }
    }


    private void drawUnderLine(Canvas canvas, Paint paint) {
        
        paint.setColor(borderColor);
        paint.setStrokeWidth(borderWidth);
        paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < passwordLength; i++) {
           
            canvas.drawLine(getPaddingLeft() + (passwordSize + passwordPadding) * i, getPaddingTop() + passwordSize,
                    getPaddingLeft() + (passwordSize + passwordPadding) * i + passwordSize, getPaddingTop() + passwordSize,
                    paint);
        }
    }

    private void drawRect(Canvas canvas, Paint paint) {
        paint.setColor(borderColor);
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.STROKE);
        Rect rect;
        for (int i = 0; i < passwordLength; i++) {
            int startX = getPaddingLeft() + (passwordSize + passwordPadding) * i;
            int startY = getPaddingTop();
            int stopX = getPaddingLeft() + (passwordSize + passwordPadding) * i + passwordSize;
            int stopY = getPaddingTop() + passwordSize;
            rect = new Rect(startX, startY, stopX, stopY);
            canvas.drawRect(rect, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            requestFocus();
            inputManager.showSoftInput(this, InputMethodManager.SHOW_FORCED);
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            inputManager.hideSoftInputFromWindow(this.getWindowToken(), 0);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        
        timer.scheduleAtFixedRate(timerTask, 0, cursorFlashTime);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        timer.cancel();
    }

    private int dp2px(float dp) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = InputType.TYPE_CLASS_NUMBER; 
        return super.onCreateInputConnection(outAttrs);
    }

    public void setPasswordListener(PasswordListener passwordListener) {
        this.passwordListener = passwordListener;
    }

    public void setPasswordSize(int passwordSize) {
        this.passwordSize = passwordSize;
        postInvalidate();
    }

    public void setPasswordLength(int passwordLength) {
        this.passwordLength = passwordLength;
        postInvalidate();
    }

    public void setCursorColor(int cursorColor) {
        this.cursorColor = cursorColor;
        postInvalidate();
    }

    public void setCursorEnable(boolean cursorEnable) {
        isCursorEnable = cursorEnable;
        postInvalidate();
    }

    public void setCipherEnable(boolean cipherEnable) {
        this.cipherEnable = cipherEnable;
        postInvalidate();
    }


    public interface PasswordListener {

        void passwordChange(String changeText);


        void passwordComplete();


        void keyEnterPress(String password, boolean isComplete);

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putStringArray("password", password);
        bundle.putInt("cursorPosition", cursorPosition);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            password = bundle.getStringArray("password");
            cursorPosition = bundle.getInt("cursorPosition");
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }


    public void clearText(){
        for(int i=0;i<passwordLength;i++ ){
            String deleteText = delete();
            if (passwordListener != null && !TextUtils.isEmpty(deleteText)) {
                passwordListener.passwordChange(deleteText);
            }
            postInvalidate();

        }



    }



}
