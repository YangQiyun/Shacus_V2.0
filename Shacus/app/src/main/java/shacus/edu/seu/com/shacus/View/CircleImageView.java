package shacus.edu.seu.com.shacus.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by zbyue on 2017/9/5.
 */

public class CircleImageView extends ImageView {


    private Bitmap bitmap;
    private BitmapShader bitmapShader;
    private Paint paint;

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 2;

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(bitmap == null||bitmapShader == null)
            return;
        if(bitmap.getHeight() == 0||bitmap.getWidth() == 0)
            return;

        updateBitmapShader();
        paint.setShader(bitmapShader);
        canvas.drawCircle(getWidth()/2.0f, getHeight() / 2.0f, Math.min(getWidth() / 2.0f, getHeight() / 2.0f), paint);
    }

    private void init(){
        if(bitmap == null)
            return;
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        bitmap = bm;
        init();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        bitmap = getBitmapFromDrawable(drawable);
        init();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        bitmap = getBitmapFromDrawable(getDrawable());
        init();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        bitmap = uri != null ? getBitmapFromDrawable(getDrawable()) : null;
        init();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable){
        if(drawable == null)
            return null;
        if(drawable instanceof BitmapDrawable)
            return ((BitmapDrawable)drawable).getBitmap();

        try{
            Bitmap bitmap1;
            if(drawable instanceof ColorDrawable){
                bitmap1 = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            }else{
                bitmap1 = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),BITMAP_CONFIG);
            }
            Canvas canvas = new Canvas(bitmap1);
            drawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
            drawable.draw(canvas);
            return bitmap1;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    private void updateBitmapShader(){
        if(bitmap == null)
            return;
        int canvasSize = Math.min(getWidth(),getHeight());
        if(canvasSize == 0)
            return;
        //缩放
        if( canvasSize!= bitmap.getWidth() || canvasSize !=bitmap.getHeight()) {
            Matrix matrix = new Matrix();
            float scale = (float) canvasSize / (float) bitmap.getWidth();
            matrix.setScale(scale, scale);
            bitmapShader.setLocalMatrix(matrix);
        }
    }
}
