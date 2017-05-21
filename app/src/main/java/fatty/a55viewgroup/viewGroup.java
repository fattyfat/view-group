package fatty.a55viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by Fatty on 2017-02-15.
 */

public class viewGroup extends ViewGroup {

    private Scroller mScroller;
    private int mScreenHeight;
    private int mLastY = 0 ,mStart = 0 ,mEnd = 0;

    public viewGroup(Context context) {

        super(context);
        mScroller = new Scroller(context);
    }

    public viewGroup(Context context, AttributeSet attrs){

        super(context,attrs);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);

        int childCount = getChildCount();
        for(int i=0 ; i<childCount ; i++){
            View childView = getChildAt(i);
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
        }
        mScreenHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed,int l ,int t ,int r ,int b){

        int childCount = getChildCount();

        MarginLayoutParams mlp = (MarginLayoutParams)getLayoutParams();

        mlp.height = mScreenHeight;
        setLayoutParams(mlp);

        for(int i=0 ; i<childCount ; i++){
            View child = getChildAt(i);
            if(child.getVisibility() != View.GONE){
                child.layout(l ,i*mScreenHeight ,r ,(i+1)*mScreenHeight);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        int y = (int)event.getY();
        int childCount = getChildCount();

        switch(event.getAction()){

            case MotionEvent.ACTION_DOWN:

                mLastY = y;
                mStart = getScrollY();

                break;

            case MotionEvent.ACTION_MOVE:

                if(!mScroller.isFinished()) //上次還沒完成就取消動畫
                    mScroller.abortAnimation();

                int dy = mLastY - y;
                if (getScrollY()+dy < 0)
                    dy = 0;

                if (getScrollY()+dy > mScreenHeight*(childCount-1))
                    dy = 0;

                scrollBy(0, dy);

                mLastY = y;

                break;

            case MotionEvent.ACTION_UP:

                mEnd = getScrollY();
                int dScrollY = mEnd - mStart;
                if (dScrollY > 0){
                    if(dScrollY < mScreenHeight/3){
                        mScroller.startScroll(0 ,getScrollY() ,0 ,-dScrollY ,1000);
                    }else {
                        mScroller.startScroll(0 ,getScrollY() ,0 ,mScreenHeight - dScrollY ,1000);
                    }
                }else{
                    if(-dScrollY < mScreenHeight/3){
                        mScroller.startScroll(0 ,getScrollY() ,0 ,-dScrollY ,1000);
                    }else {
                        mScroller.startScroll(0 ,getScrollY() ,0 ,-mScreenHeight - dScrollY ,1000);
                    }
                }
                break;
        }
        postInvalidate();
        return true;
    }

    @Override
    public void computeScroll(){

        super.computeScroll();

        if(mScroller.computeScrollOffset()){
            scrollTo(0 ,mScroller.getCurrY());
            postInvalidate();
        }
    }

}
