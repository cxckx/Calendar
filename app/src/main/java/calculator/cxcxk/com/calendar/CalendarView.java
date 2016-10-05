package calculator.cxcxk.com.calendar;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.cxcxk.android_my_library.utils.AndroidUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by cxcxk on 2016/9/27.
 */
public class CalendarView extends View {


    private Paint textPaint,linePaint,numPaint,imagePaint,datePaint,rectPaint;
    private String dateText = "你我几女发生的";
    private int [] month_day = {31,28,31,30,31,30,31,31,30,31,30,31};
    private String []date = {"一","二","三","四","五","六","日"};
    private Calendar calendar;
    private SimpleDateFormat format;
    private int currentYear,currentMonth,currentDay,currentWeek;
    private AnimationSet enterSet,exitSet;
    public CalendarView(Context context) {
        super(context);
        initPaint(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);
    }

    private void initPaint(Context context) {

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(AndroidUtils.dip2px(context, 20f));

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
        linePaint.setStyle(Paint.Style.STROKE);
        //linePaint.setTextSize(AndroidUtils.dip2px(context,24f));

        numPaint = new Paint();
        numPaint.setAntiAlias(true);
        numPaint.setDither(true);
        numPaint.setTextSize(AndroidUtils.dip2px(context, 14f));
        numPaint.setColor(Color.BLACK);

        imagePaint = new Paint();
        imagePaint.setAntiAlias(true);
        imagePaint.setDither(true);
        imagePaint.setColor(Color.BLACK);
        imagePaint.setStyle(Paint.Style.FILL);
        //imagePaint.setTextSize(AndroidUtils.dip2px(context,24f));
        datePaint = new Paint();
        datePaint.setAntiAlias(true);
        datePaint.setDither(true);
        datePaint.setColor(Color.BLACK);
        datePaint.setTextSize(AndroidUtils.dip2px(context, 14));
        datePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setDither(true);
        rectPaint.setColor(getResources().getColor(R.color.colorAccent));
       // rectPaint.setTextSize(AndroidUtils.dip2px(context, 14));
        rectPaint.setStyle(Paint.Style.FILL);

        format = new SimpleDateFormat("yyyy-MM-dd");
        calendar = Calendar.getInstance();
        dateText = format.format(calendar.getTime());

        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;

        pathWidth = AndroidUtils.dip2px(context,16);

        if(isLeapYear(currentYear)){
            month_day[1] = 29;
        }else {
            month_day[1] = 28;
        }

        enterSet = (AnimationSet) AnimationUtils.loadAnimation(context,R.anim.anim_enter);
        exitSet = (AnimationSet) AnimationUtils.loadAnimation(context,R.anim.anim_exit);


    }

    private boolean isLeapYear(int year){
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);


        drawTopTip(canvas);

        //画星期
        drawLine(canvas);
        drawSelectUnit(canvas);
        //画日历内容
        drawCalendar(canvas);
        drawRoundRectBoder(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtils.dip2px(getContext(), 300), MeasureSpec.EXACTLY));
    }

    private boolean isDateEqual(int date){

        date = date+1;

        int datee  = calendar.get(Calendar.DAY_OF_WEEK);

        if(datee  == 1) datee = 8;
        return datee <= date || date == 1;
    }
    private float upX,upY,nextX,nextY,pathWidth,pathHeight;
    private void drawTopTip(Canvas canvas){
        Rect textRect = new Rect();

        //返回包围整个字符串的最小的一个Rect区域
        textPaint.getTextBounds(dateText, 0, dateText.length(), textRect);

        float height = textRect.height();
        float width = textRect.width();
        //画文本
        canvas.drawText(dateText, (canvas.getWidth() - width) / 2, height, textPaint);

        //花三角按钮
        Path path1 = new Path();
        upX = (canvas.getWidth() - width) / 2 - AndroidUtils.dip2px(getContext(), 32);
        upY = AndroidUtils.dip2px(getContext(), 4);
        pathHeight = height - upY;
        path1.moveTo((canvas.getWidth() - width) / 2 - AndroidUtils.dip2px(getContext(), 32), height / 2 + AndroidUtils.dip2px(getContext(), 2));
        path1.lineTo((canvas.getWidth() - width) / 2 - AndroidUtils.dip2px(getContext(), 16), AndroidUtils.dip2px(getContext(), 4));
        path1.lineTo((canvas.getWidth() - width) / 2 - AndroidUtils.dip2px(getContext(), 16), height);

        path1.close();

        canvas.drawPath(path1, imagePaint);

        Path path2 = new Path();
        nextX = canvas.getWidth() / 2 + AndroidUtils.dip2px(getContext(), 16) + width / 2;
        nextY = AndroidUtils.dip2px(getContext(), 4);
        path2.moveTo(canvas.getWidth() / 2 + AndroidUtils.dip2px(getContext(), 32) + width / 2, height / 2 + AndroidUtils.dip2px(getContext(), 2));
        path2.lineTo(canvas.getWidth() / 2 + AndroidUtils.dip2px(getContext(), 16) + width / 2, AndroidUtils.dip2px(getContext(), 4));
        path2.lineTo(canvas.getWidth() / 2 + AndroidUtils.dip2px(getContext(), 16) + width / 2, height);

        path2.close();

        canvas.drawPath(path2, imagePaint);
    }

    public void drawRoundRectBoder(Canvas canvas){
        Rect textRect = new Rect();

        //返回包围整个字符串的最小的一个Rect区域
        textPaint.getTextBounds(dateText, 0, dateText.length(), textRect);

        float height = textRect.height();


        //画网格
        RectF rectf= new RectF(0,height+AndroidUtils.dip2px(getContext(),16),canvas.getWidth(),canvas.getHeight());

        canvas.drawRoundRect(rectf,AndroidUtils.dip2px(getContext(),8),AndroidUtils.dip2px(getContext(),8),linePaint);
    }

    private void drawCalendar(Canvas canvas){
        Rect dateRect = new Rect();
        Rect textRect = new Rect();

        //返回包围整个字符串的最小的一个Rect区域
        textPaint.getTextBounds(dateText, 0, dateText.length(), textRect);

        float height = textRect.height();

        int month = calendar.get(Calendar.MONTH);
        int days = month_day[month];

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int tmp = calendar.get(Calendar.DAY_OF_WEEK) - 1 == 0?  7 : calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int spacingX = 7-(tmp-1) ;
        days -= spacingX;

        float hangLineHeight = days /7 +(days % 7 > 0 ? 1 :0)+ 2;
        float heightUnit = (canvas.getHeight()-height-AndroidUtils.dip2px(getContext(),16))/hangLineHeight;
        float hangLineWidth = canvas.getWidth()/7;
        int num = 1;
        for(int i = 1;i<hangLineHeight;i++){
            if(num > month_day[month]){
                break;
            }
            for(int j = 0;j<7;j++){
                numPaint.getTextBounds(num + "", 0, (num + "").length(), dateRect);
                if(i == 1 && isDateEqual((j+1) % 7)){
                    canvas.drawText(num+"",j * hangLineWidth + (hangLineWidth-dateRect.width())/2,i * heightUnit+height+AndroidUtils.dip2px(getContext(),16)+(heightUnit+dateRect.height())/2,numPaint);
                    num++;
                }else if(i != 1){
                    canvas.drawText(num+"", j * hangLineWidth + (hangLineWidth-dateRect.width())/2,i * heightUnit+height+AndroidUtils.dip2px(getContext(),16)+(heightUnit+dateRect.height())/2,numPaint);
                    num++;
                }
                if(num > month_day[month]){
                    break;
                }
            }

        }
    }
    float heightUnit, hangLineWidth,topSpacing,hangLineHeight;
    public void drawLine(Canvas canvas){
        Rect dateRect = new Rect();
        Rect textRect = new Rect();

        //返回包围整个字符串的最小的一个Rect区域
        textPaint.getTextBounds(dateText, 0, dateText.length(), textRect);

        float height = textRect.height();

        int month = calendar.get(Calendar.MONTH);
        int days = month_day[month];

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int tmp = calendar.get(Calendar.DAY_OF_WEEK) - 1 == 0?  7 : calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int spacingX = 7-(tmp-1) ;
        days -= spacingX;

        hangLineHeight = days /7 +(days % 7 > 0 ? 1 :0)+ 2;
        heightUnit = (canvas.getHeight()-height-AndroidUtils.dip2px(getContext(),16))/hangLineHeight;
        hangLineWidth = canvas.getWidth()/7;
        topSpacing = height + AndroidUtils.dip2px(getContext(), 16);
        for (int i = 0;i<hangLineHeight-1;i++){
            canvas.drawLine(0, topSpacing + heightUnit * (i + 1), canvas.getWidth(),topSpacing + heightUnit * (i + 1), datePaint);
            if(i == 0){
                for(int j = 0;j<=6;j++){
                    datePaint.getTextBounds(date[j], 0, 1, dateRect);
                    canvas.drawText(date[j],hangLineWidth * j + (hangLineWidth-dateRect.width())/2,(heightUnit+dateRect.height())/2+height+AndroidUtils.dip2px(getContext(),16),datePaint);
                }
            }
        }
        for (int i = 0;i< 6;i++){
            canvas.drawLine(0+hangLineWidth * (i+1),height+AndroidUtils.dip2px(getContext(),16),hangLineWidth * (i+1),canvas.getHeight(),datePaint);
        }
    }
  boolean isClick = false;
    private void drawSelectUnit(Canvas canvas){
        if(!isClick){
            //int first_week = calendar.get(Calendar.DAY_OF_WEEK)-1;

             int indexX = currentWeek == 0 ? 7: currentWeek;
             int indexY = currentDay / 7 + 1;
             UnitX = (indexX -1) * hangLineWidth;
             UnitY = (indexY - 1) * heightUnit + topSpacing+heightUnit;
        }
        canvas.drawCircle(UnitX + hangLineWidth / 2, UnitY + heightUnit / 2, heightUnit / 2, rectPaint);
    }


    long downTime = 0;
    float downX = 0,downY = 0;
    float UnitX = -1,UnitY = -1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        if(event.getAction() == MotionEvent.ACTION_DOWN){
            downTime = System.currentTimeMillis();
            downX = event.getX();
            downY = event.getY();
        }else if(event.getAction() == MotionEvent.ACTION_UP){
            if(System.currentTimeMillis() - downTime < 2000){
                float x = event.getX();
                float y = event.getY();

                if(x == downX && y == downY){
                    if(downY >= topSpacing+heightUnit){



                        int indexX = (int)(downX / hangLineWidth + (downX % hangLineWidth > 0 ? 1:0));
                        int indexY = (int)((downY-topSpacing-heightUnit) / heightUnit + ((downY-topSpacing-heightUnit) % heightUnit > 0 ? 1:0));


                        int first_week = calendar.get(Calendar.DAY_OF_WEEK);

                        if(indexY == 1 && indexX < first_week-1){
                            invalidate();
                            return true;
                        }else if(indexY == hangLineHeight-1){
                            int num = indexY * 7 - first_week+1 - month_day[calendar.get(Calendar.MONTH)];
                            if(indexX >= 7-num){

                                invalidate();
                                return true;
                            }
                        }
                        isClick = true;
                        UnitX = (indexX -1) * hangLineWidth;
                        UnitY = (indexY-1) * heightUnit + topSpacing+heightUnit;
                        invalidate();
                    }else {
                        if(x >= upX && x <= upX+pathWidth){
                            if(y >= upY && y<=pathHeight + upY ){
                                this.startAnimation(exitSet);
                                calendar.add(Calendar.MONTH, -1);
                                dateText = format.format(calendar.getTime());
                                isClick = false;
                                currentWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
                                currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                            }



                        }else if(x >= nextX && x <= nextX + pathWidth){
                            if(y >= nextY && y <= nextY + pathHeight){
                                this.startAnimation(enterSet);
                                //this.setTranslationX(-getWidth());
                                calendar.add(Calendar.MONTH,1);
                                dateText = format.format(calendar.getTime());
                                isClick = false;
                                currentWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
                                currentDay = calendar.get(Calendar.DAY_OF_MONTH);


                            }


                        }

                        if(isLeapYear(calendar.get(Calendar.YEAR))){
                            month_day[1] = 29;
                        }else {
                            month_day[1] = 28;
                        }


                    }

                }
            }
        }

        return true;
    }
}
