package com.ohunag.xposed_main.smallwindow;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public class FloatingMagnetView extends FrameLayout {

    public static final int MARGIN_EDGE = 0;
    private float mOriginalRawX;
    private float mOriginalRawY;
    private float mOriginalX;
    private float mOriginalY;
    private MagnetViewListener mMagnetViewListener;
    private static final int TOUCH_TIME_THRESHOLD = 150;
    private long mLastTouchDownTime;
    protected MoveAnimator mMoveAnimator;
    protected int mScreenWidth;
    private int mScreenHeight;
    private float mStatusBarHeight;
    private boolean isNearestLeft = true;
    private float mPortraitY;
    private boolean dragEnable = true;
    private boolean autoMoveToEdge = false;

    private float originX;
    private float originY;

    private float moveX;
    private float moveY;

    public void setMagnetViewListener(MagnetViewListener magnetViewListener) {
        this.mMagnetViewListener = magnetViewListener;
    }

    public FloatingMagnetView(Context context) {
        this(context, null);
    }

    public FloatingMagnetView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingMagnetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mMoveAnimator = new MoveAnimator();
        mStatusBarHeight = getStatusBarHeight(getContext());
        setClickable(true);
//        updateSize();
    }

    public float getStatusBarHeight(Context context) {
        float height = 0;
        int resourceId = context.getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getApplicationContext().getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    /**
     * @param dragEnable 是否可拖动
     */
    public void updateDragState(boolean dragEnable) {
        this.dragEnable = dragEnable;
    }

    /**
     * @param autoMoveToEdge 是否自动到边缘
     */
    public void setAutoMoveToEdge(boolean autoMoveToEdge) {
        this.autoMoveToEdge = autoMoveToEdge;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //记录下当前按下点的坐标相对父容器的位置
                originY = event.getY();
                originX = event.getX();

                dealDownEvent();
                break;
            case MotionEvent.ACTION_MOVE:
                updateViewPosition(event);
                break;
            case MotionEvent.ACTION_UP:
                clearPortraitY();
                if (autoMoveToEdge) {
                    moveToEdge();
                }
                if (isOnClickEvent()) {
                    dealClickEvent();
                } else {
                    dealUpEvent();
                }
                break;
        }
        return true;
    }

    protected void dealUpEvent() {
        if (mMagnetViewListener != null) {
            mMagnetViewListener.onUp(this);
        }
    }

    protected void dealClickEvent() {
        if (mMagnetViewListener != null) {
            mMagnetViewListener.onClick(this);
        }
    }

    protected void dealDownEvent() {
        if (mMagnetViewListener != null) {
            mMagnetViewListener.onDown(this);
        }
    }

    protected boolean isOnClickEvent() {
        return System.currentTimeMillis() - mLastTouchDownTime < TOUCH_TIME_THRESHOLD;
    }

    private void updateViewPosition(MotionEvent event) {
        //dragEnable
        if (!dragEnable) return;

//    *********************移动ViewGroup方法一 :layout *****************/
//        float y = event.getY();
//        float x = event.getX();
//
//        moveX = x - originX;
//        moveY = y - originY;
//
//        //view移动前的上下左右位置
//        float left = getLeft() + moveX;
//        float top = getTop() + moveY;
//        float right = getRight() + moveX;
//        float bottom = getBottom() + moveY;
//
//        if ((left) < 0) {
//            left = 0;
//            right = getWidth();
//        }
//
//        if (top < mStatusBarHeight) {
//            top = mStatusBarHeight;
//            bottom = getHeight()+mStatusBarHeight;
//        }
//        layout((int) (left), (int) (top), (int) (right), (int) (bottom));


//        **********************移动ViewGroup方法二：setX,setY ******************//
        //限制不可超出屏幕宽度
        float desX = mOriginalX + event.getRawX() - mOriginalRawX;
        if (desX < 0) {
            desX = MARGIN_EDGE;
        }
        if (desX > mScreenWidth) {
            desX = mScreenWidth - MARGIN_EDGE;
        }
        setX(desX);

        // 限制不可超出屏幕高度
        float desY = mOriginalY + event.getRawY() - mOriginalRawY;
        if (desY < mStatusBarHeight) {
            desY = mStatusBarHeight;
        }
        if (desY > mScreenHeight - getHeight()) {
            desY = mScreenHeight - getHeight();
        }
        setY(desY);
    }


    private void changeOriginalTouchParams(MotionEvent event) {
        mOriginalX = getX();
        mOriginalY = getY();
        mOriginalRawX = event.getRawX();
        mOriginalRawY = event.getRawY();
        mLastTouchDownTime = System.currentTimeMillis();
    }

    protected void updateSize() {
        ViewGroup viewGroup = (ViewGroup) getParent();
        if (viewGroup != null) {
            mScreenWidth = viewGroup.getWidth() - getWidth();
            mScreenHeight = viewGroup.getHeight();
        }
//        mScreenWidth = (SystemUtils.getScreenWidth(getContext()) - this.getWidth());
//        mScreenHeight = SystemUtils.getScreenHeight(getContext());
    }

    public void moveToEdge() {
        //dragEnable
        if (!dragEnable) return;
        moveToEdge(isNearestLeft(), false);
    }

    public void moveToEdge(boolean isLeft, boolean isLandscape) {
        float moveDistance = isLeft ? MARGIN_EDGE : mScreenWidth - MARGIN_EDGE;
        float y = getY();
        if (!isLandscape && mPortraitY != 0) {
            y = mPortraitY;
            clearPortraitY();
        }
        mMoveAnimator.start(moveDistance, Math.min(Math.max(0, y), mScreenHeight - getHeight()));
    }

    private void clearPortraitY() {
        mPortraitY = 0;
    }

    protected boolean isNearestLeft() {
        int middle = mScreenWidth / 2;
        isNearestLeft = getX() < middle;
        return isNearestLeft;
    }

    public void onRemove() {
        if (mMagnetViewListener != null) {
            mMagnetViewListener.onRemove(this);
        }
    }

    //最赞的，用最基础的代码实现了动画。handler+setX()
    protected class MoveAnimator implements Runnable {

        private Handler handler = new Handler(Looper.getMainLooper());
        private float destinationX;
        private float destinationY;
        private long startingTime;

        void start(float x, float y) {
            this.destinationX = x;
            this.destinationY = y;
            startingTime = System.currentTimeMillis();
            handler.post(this);
        }

        @Override
        public void run() {
            if (getRootView() == null || getRootView().getParent() == null) {
                return;
            }
            //400ms
            float progress = Math.min(1, (System.currentTimeMillis() - startingTime) / 400f);
            float deltaX = (destinationX - getX()) * progress;
            float deltaY = (destinationY - getY()) * progress;
            move(deltaX, deltaY);
            if (progress < 1) {
                handler.post(this);
            }
        }

        private void stop() {
            handler.removeCallbacks(this);
        }
    }

    //最基础的代码实现的动画，通过handler 不断的 setX，setY来移动位置
    private void move(float deltaX, float deltaY) {
        setX(getX() + deltaX);
        setY(getY() + deltaY);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getParent() != null) {
            final boolean isLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
            markPortraitY(isLandscape);
            ((ViewGroup) getParent()).post(new Runnable() {
                @Override
                public void run() {
                    updateSize();
                    moveToEdge(isNearestLeft, isLandscape);
                }
            });
        }
    }

    private void markPortraitY(boolean isLandscape) {
        if (isLandscape) {
            mPortraitY = getY();
        }
    }

    private float touchDownX;

    private void initTouchDown(MotionEvent ev) {
        changeOriginalTouchParams(ev);
        updateSize();
        mMoveAnimator.stop();
    }

    //判断是否拦截父容器
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                touchDownX = ev.getX();
                initTouchDown(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                intercepted = Math.abs(touchDownX - ev.getX()) >= ViewConfiguration.get(getContext()).getScaledTouchSlop();
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
        }
        return intercepted;
    }

    public interface MagnetViewListener {
        void onUp(FloatingMagnetView floatingMagnetView);

        void onClick(FloatingMagnetView floatingMagnetView);

        void onDown(FloatingMagnetView floatingMagnetView);

        void onRemove(FloatingMagnetView floatingMagnetView);
    }

}