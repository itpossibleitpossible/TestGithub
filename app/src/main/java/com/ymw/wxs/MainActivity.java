package com.ymw.wxs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener  {
   int[] images = new int[]{
           R.drawable.a1111
   };

   int x1,x2,x;

   int current = images.length * 100;

    private ImageView img_test;

    // 縮放控制

    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();

    // 不同状态的表示：

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;

    // 定义第一个按下的点，两只接触点的重点，以及出事的两指按下的距离：
    private PointF startPoint = new PointF();
    private PointF midPoint = new PointF();
    private float oriDis = 1f;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        img_test = (ImageView) this.findViewById(R.id.main_imgZooming);
        img_test.setImageResource(images[current % images.length]);
        img_test.setOnTouchListener(this);

    }

    // 计算两个触摸点之间的距离

    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return Float.valueOf(String.valueOf(Math.sqrt(x * x + y * y))) ;

    }

    // 计算两个触摸点的中点

    private PointF middle(MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        return new PointF(x / 2, y / 2);
    }



    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;



        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // 单指
            case MotionEvent.ACTION_DOWN:
                 x1 = (int) event.getX();
//


//                matrix.set(view.getImageMatrix());
//                savedMatrix.set(matrix);
//                startPoint.set(event.getX(), event.getY());
                mode = DRAG;
                break;
            // 双指
            case MotionEvent.ACTION_POINTER_DOWN:
                oriDis = distance(event);
                if (oriDis > 10f) {
                    savedMatrix.set(matrix);
                    midPoint = middle(event);
                    mode = ZOOM;
                }
                break;
            // 手指放开
            case MotionEvent.ACTION_UP:
                x2 = (int) event.getX();
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                break;
            // 单指滑动事件
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                     x = x1 - x2;
                    img_test.setImageResource(images[(current + x) % images.length]);
//
                    // 是一个手指拖动
//                    matrix.set(savedMatrix);
//                    matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y);
                } else if (mode == ZOOM) {
                    // 两个手指滑动
                    float newDist = distance(event);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oriDis;
                        matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                    }
                }
                break;
        }
        // 设置ImageView的Matrix
        view.setImageMatrix(matrix);
        return true;


//        return detector.onTouchEvent(event);
    }


}
