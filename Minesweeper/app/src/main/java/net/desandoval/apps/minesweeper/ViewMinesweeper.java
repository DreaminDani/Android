package net.desandoval.apps.minesweeper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Daniel on 29/09/2014.
 */
public class ViewMinesweeper extends View {
    private Paint paintBg;
    private Paint paintLine;

    private static boolean isFlag = false;
    private int gridViewSize = MinesweeperModel.getInstance().gridViewSize;
    Context ctx;

    public ViewMinesweeper (Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;

        paintBg = new Paint();
        paintBg.setStyle(Paint.Style.STROKE);
        paintBg.setColor(Color.GRAY);

        paintLine = new Paint();
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setColor(Color.DKGRAY);
        paintLine.setStrokeWidth(2);
    }

    /*
    Sets the isFlag boolean to determine which type of click is being sent
     */
    public static void toggleFlag (boolean flag) {
        isFlag = flag;
    }

    /*
    Overrides onDraw to provide the logic needed to draw the game board in its current state
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0,0,getWidth(),getHeight(),paintBg);

        drawGameArea(canvas);

        drawPlayer(canvas);
    }

    /*
    Draws the game board, using the gridViewSize as a guide to the height/width
     */
    private void drawGameArea(Canvas canvas) {
        // border
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintLine);
        // four horizontal lines
        for (int i=1; i < gridViewSize; i++) {
            canvas.drawLine(0, i * getHeight() / gridViewSize, getWidth(),
                    i * getHeight() / gridViewSize, paintLine);
        }
        // four vertical lines
        for (int i=1; i < gridViewSize; i++) {
            canvas.drawLine(i * getWidth() / gridViewSize, 0, i * getWidth() / gridViewSize, getHeight(),
                    paintLine);
        }
    }

    /*
    Draws game board according to what the user has already clicked or flagged.
    Circles show where user has clicked and number of bombs surrounding
    X's show where the user has flagged.
     */
    private void drawPlayer(Canvas canvas) {
        for (int i = 0; i < gridViewSize; i++) {
            for (int j = 0; j < gridViewSize; j++) {
                if (MinesweeperModel.getInstance().getBoardContent(i, j) == MinesweeperModel.CLICKED) {
                    float centerX = i * getWidth() / gridViewSize + getWidth() / (2 * gridViewSize);
                    float centerY = j * getHeight() / gridViewSize + getHeight() / (2 * gridViewSize);
                    int radius = getHeight() / (2 * gridViewSize) - 2;
                    canvas.drawCircle(centerX, centerY, radius, paintLine);
                    canvas.drawText(String.valueOf(MinesweeperModel.getInstance().getBombsNear(i, j)),centerX, centerY, paintLine); // draw number of bombs near
                } else if (MinesweeperModel.getInstance().getBoardContent(i, j) == MinesweeperModel.FLAGGED) {
                    canvas.drawLine(i * getWidth() / gridViewSize, j * getHeight() / gridViewSize,
                            (i + 1) * getWidth() / gridViewSize,
                            (j + 1) * getHeight() / gridViewSize, paintLine);
                    canvas.drawLine((i + 1) * getWidth() / gridViewSize, j * getHeight() / gridViewSize,
                            i * getWidth() / gridViewSize, (j + 1) * getHeight() / gridViewSize, paintLine);
                }
            }
        }
    }

    /*
    Takes user input to control the game logic
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int tX = ((int) event.getX()) / (getWidth() / gridViewSize);
            int tY = ((int) event.getY()) / (getHeight() / gridViewSize);
            if (tX < gridViewSize && tY < gridViewSize && MinesweeperModel.getInstance().getBoardContent(tX, tY) == MinesweeperModel.EMPTY) {
                if (isFlag) {
                    MinesweeperModel.getInstance().setBoardContent(tX,tY,MinesweeperModel.getInstance().FLAGGED);
                } else if (MinesweeperModel.getInstance().getBombContent(tX,tY) == MinesweeperModel.getInstance().BOMB) {
                    Toast.makeText(getContext(), "You clicked a bomb! You lose!", Toast.LENGTH_LONG).show();
                    invalidate();

                    Intent myIntent = new Intent(ctx, Splash.class);
                    ctx.startActivity(myIntent);
                }else {
                    MinesweeperModel.getInstance().setBoardContent(tX, tY, MinesweeperModel.getInstance().CLICKED);
                }

                if (MinesweeperModel.getInstance().getWinner() == 1) {
                    Toast.makeText(getContext(), "You survived! Click \"Start\" to play again!", Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(ctx, Splash.class);
                    ctx.startActivity(myIntent);
                }

                invalidate();
            } else if (tX < gridViewSize && tY < gridViewSize && MinesweeperModel.getInstance().getBoardContent(tX,tY) == MinesweeperModel.FLAGGED){
                MinesweeperModel.getInstance().setBoardContent(tX,tY,MinesweeperModel.getInstance().EMPTY);
                invalidate();
            }
        }
        return super.onTouchEvent(event);
    }

    /*
    Override onMeasure to fit the height and width into desired height and width
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
    }
}
