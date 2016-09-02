package fridge.com.videotest;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by Fridge on 16/9/2.
 */
public class VideoContainerLayout extends LinearLayout {

    ListView listView;
    VideoLayout videoLayout;
    private int videoState = VIDEO_LAYOUT_FULL_SIZE;
    public static final int VIDEO_LAYOUT_FULL_SIZE = 0;
    public static final int VIDEO_LAYOUT_HALF_SIZE = 1;
    public static final int VIDEO_LAYOUT_ZERO_SIZE = 3;

    private int fullSizeHeight;
    private int fullSizeWidth;

    private int mLastMotionY;
    private int mLastMotionX;

    private int deltaY;

    private boolean isAnimationPlaying = false;

    public VideoContainerLayout(Context context) {
        super(context);
    }

    public VideoContainerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoContainerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isAnimationPlaying) {
            return true;//动画播放期间禁止操作
        }
        int y = (int) ev.getRawY();
        int x = (int) ev.getRawX();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 发生down事件时,记录y坐标
                mLastMotionY = y;
                mLastMotionX = x;
                break;

            case MotionEvent.ACTION_MOVE:
                deltaY = y - mLastMotionY;
                Log.i("shitshit", "======================================== ");
                Log.i("shitshit", "[VideoContainerLayout] deltaY: " + deltaY);
                Log.i("shitshit", "[VideoContainerLayout] state : " + videoState);
                Log.i("shitshit", "[VideoContainerLayout] lT    : " + isListViewTopping());
                if (Math.abs(deltaY) < 20) {
                    break;
                }
                if (videoLayout.isPlaying && isListViewTopping()) {
                    if (deltaY < 0 && videoState == VIDEO_LAYOUT_FULL_SIZE) {
                        zoomInVideoLayout(VIDEO_LAYOUT_HALF_SIZE);
                        return true;
                    } else if (deltaY > 0) {
                        zoomInVideoLayout(VIDEO_LAYOUT_FULL_SIZE);
                        return true;
                    }
                }

                if (!videoLayout.isPlaying) {
                    if (deltaY < 0) {
                        zoomInVideoLayout(VIDEO_LAYOUT_ZERO_SIZE);
                    }
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setListView(ListView listView) {
        this.listView = listView;
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    if (deltaY < 0) {
                        return;//如果是向上，无视
                    }
                    if (videoState == VIDEO_LAYOUT_ZERO_SIZE) {
                        zoomInVideoLayout(VIDEO_LAYOUT_FULL_SIZE);
                    }
                }
            }
        });
    }

    public void setVideoLayout(final VideoLayout videoLayout) {
        this.videoLayout = videoLayout;
        videoLayout.post(new Runnable() {
            @Override
            public void run() {
                fullSizeHeight = videoLayout.getHeight();
                fullSizeWidth = videoLayout.getWidth();
                Log.i("shitshit", "[VideoContainerLayout] fullSizeHeight :" + fullSizeHeight);
                Log.i("shitshit", "[VideoContainerLayout] fullSizeWidth :" + fullSizeWidth);

            }
        });
    }

    public boolean isListViewTopping() {
        if (listView.getFirstVisiblePosition() == 0 && listView.getChildAt(0).getTop() == 0) {
            return true;
        }
        return false;
    }

    private void zoomInVideoLayout(final int targetState) {
        float scaleSize = 1f;
        switch (targetState) {
            case VIDEO_LAYOUT_FULL_SIZE:
                scaleSize = 1f;
                break;

            case VIDEO_LAYOUT_HALF_SIZE:
                scaleSize = 0.5f;
                break;

            case VIDEO_LAYOUT_ZERO_SIZE:
                scaleSize = 0f;

        }
        ValueAnimator animationHeight = ValueAnimator.ofInt(videoLayout.getHeight(), (int) (fullSizeHeight * scaleSize));
        animationHeight.setInterpolator(new AccelerateInterpolator());
        animationHeight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                videoLayout.getLayoutParams().height = (int) animation.getAnimatedValue();
                videoLayout.setLayoutParams(videoLayout.getLayoutParams());
//                    videoLayout.requestLayout();
            }
        });

//        ValueAnimator animationWidth = ValueAnimator.ofInt(videoLayout.getWidth(), (int) (fullSizeWidth * scaleSize));
//        animationWidth.setInterpolator(new AnticipateInterpolator());
//        animationWidth.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                videoLayout.getLayoutParams().width = (int) animation.getAnimatedValue();
//                videoLayout.requestLayout();
//            }
//        });


        animationHeight.setDuration(200);
//        animationWidth.setDuration(200);

        animationHeight.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimationPlaying = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //在播放完成之后才能
                videoState = targetState;
                isAnimationPlaying = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animationHeight.start();
//        animationWidth.start();


    }
}
