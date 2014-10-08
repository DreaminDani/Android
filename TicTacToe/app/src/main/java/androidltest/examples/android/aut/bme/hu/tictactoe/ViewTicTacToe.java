package androidltest.examples.android.aut.bme.hu.tictactoe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Peter on 2014.09.18..
 */
public class ViewTicTacToe extends View {

    private Paint paintBg;
    private Paint paintLine;
    private int x = 10;
    private int y = 10;

    private boolean drawWithRed = false;

    public ViewTicTacToe(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintBg = new Paint();
        paintBg.setStyle(Paint.Style.FILL);
        paintBg.setColor(Color.BLACK);

        paintLine = new Paint();
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setColor(Color.WHITE);
        paintLine.setStrokeWidth(5);
    }

    public void setDrawingColor (boolean toRed) {
        drawWithRed = toRed;
        if (drawWithRed) {
            paintLine.setColor(Color.RED);
        } else {
            paintLine.setColor(Color.WHITE);
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0,0,getWidth(),getHeight(),paintBg);

        drawGameArea(canvas);

        drawPlayer(canvas);
    }

    private void drawGameArea(Canvas canvas) {
        // border
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintLine);
        // two horizontal lines
        canvas.drawLine(0, getHeight() / 3, getWidth(), getHeight() / 3,
                paintLine);
        canvas.drawLine(0, 2 * getHeight() / 3, getWidth(),
                2 * getHeight() / 3, paintLine);
        // two vertical lines
        canvas.drawLine(getWidth() / 3, 0, getWidth() / 3, getHeight(),
                paintLine);
        canvas.drawLine(2 * getWidth() / 3, 0, 2 * getWidth() / 3, getHeight(),
                paintLine);
    }

    private void drawPlayer(Canvas canvas) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (TicTacToeModel.getInstance().getFieldContent(i,j) == TicTacToeModel.CIRCLE) {
                    // draw a circle at the center of the field
                    // X coordinate: left side of the square + half width of the square
                    float centerX = i * getWidth() / 3 + getWidth() / 6;
                    float centerY = j * getHeight() / 3 + getHeight() / 6;
                    int radius = getHeight() / 6 - 2;
                    canvas.drawCircle(centerX, centerY, radius, paintLine);
                } else if (TicTacToeModel.getInstance().getFieldContent(i,j) == TicTacToeModel.CROSS) {
                    canvas.drawLine(i * getWidth() / 3, j * getHeight() / 3,
                            (i + 1) * getWidth() / 3,
                            (j + 1) * getHeight() / 3, paintLine);
                    canvas.drawLine((i + 1) * getWidth() / 3, j * getHeight() / 3,
                            i * getWidth() / 3, (j + 1) * getHeight() / 3, paintLine);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int tX = ((int) event.getX()) / (getWidth() / 3);
            int tY = ((int) event.getY()) / (getHeight() / 3);
            if (tX < 3 && tY < 3 && TicTacToeModel.getInstance().getFieldContent(tX,tY) == TicTacToeModel.EMPTY) {
                TicTacToeModel.getInstance().setFieldContent(tX,tY,TicTacToeModel.getInstance().getNextPlayer());
                TicTacToeModel.getInstance().changeNextPlayer();

                if (TicTacToeModel.getInstance().getWinner() == TicTacToeModel.CROSS
                    || TicTacToeModel.getInstance().getWinner() == TicTacToeModel.CIRCLE) {

                    TicTacToeModel.getInstance().resetModel();
                    Toast.makeText(getContext(),"WINNER!",Toast.LENGTH_LONG).show();
                }

                invalidate();
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
    }
}
