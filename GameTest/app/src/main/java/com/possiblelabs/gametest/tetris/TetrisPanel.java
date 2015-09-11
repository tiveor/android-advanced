package com.possiblelabs.gametest.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.possiblelabs.gametest.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Based on a Tutorial
 * Created by possiblelabs on 9/8/15.
 * Music by http://www.looperman.com/
 * Sounds by https://www.freesound.org
 */
public class TetrisPanel extends View {

    private final int ROWS = 12;
    private final int COLUMNS = 20;
    private final int MAX_COLORS = 5;
    private final long DOWN_SPEED = 300;
    private final int PANEL_RIGHT_COLUMNS = 4;

    private Rect bounds;
    private int score = 0;
    private Timer timer = null;

    private MoveTask strafetask;
    private Figure currentFigure;
    private Figure nextFigure;
    private int newFigure;
    private int[][] pool;
    private boolean action = false;
    private boolean gameOver = false;
    private boolean pause = false;
    private int figureHeight;
    private int figureWidth;
    private Paint paint;
    private Rect figureRect = new Rect();
    private Context context;

    private MediaPlayer back;
    private Effects effects;

    public TetrisPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public void init() {
        pool = new int[ROWS + 8][COLUMNS + 8];
        gameOver = false;
        newFigure = 0;
        score = 0;

        for (int i = 4; i < ROWS + 4; i++)
            for (int j = 4; j < COLUMNS + 4; j++)
                pool[i][j] = 0;

        for (int j = 4; j < COLUMNS + 6; j++) {
            pool[3][j] = 1;
            pool[ROWS + 4][j] = 1;
        }


        for (int i = 0; i < ROWS + 4; i++)
            pool[i][3] = 1;


        nextFigure = new Figure();
        currentFigure = new Figure();
        paint = new Paint();
        bounds = new Rect();
        back = new MediaPlayer();

        startDropping();
    }

    private void startDropping() {
        MoveTask task = new MoveTask(MoveTask.MOVE_DOWN);
        timer = new Timer();
        timer.schedule(task, 500, DOWN_SPEED);
        back = MediaPlayer.create(context, R.raw.beat);
        back.setLooping(true);
        back.start();
        effects = new Effects(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.getClipBounds(bounds);
        figureHeight = bounds.height() / COLUMNS;
        figureWidth = bounds.width() / (ROWS + PANEL_RIGHT_COLUMNS);
        paint.setColor(Color.BLACK);
        canvas.drawColor(Color.rgb(53, 53, 67));

        drawStaticFigures(canvas);
        drawCurrentFigure(canvas);
        drawRightPanel(canvas);
        drawNextFigure(canvas);
        drawScore(canvas);
        drawDataStatus(canvas);
    }

    private void drawStaticFigures(Canvas canvas) {
        for (int i = 4; i < ROWS + 4; i++)
            for (int j = 4; j < COLUMNS + 4; j++)
                if (pool[i][j] != 0) {
                    figureRect.set((i - 4) * figureWidth + 1, bounds.height() - (j - 3) * figureHeight + 1,
                            (i - 3) * figureWidth - 1, bounds.height() - (j - 4) * figureHeight - 1);
                    paint.setColor(Color.RED);
                    canvas.drawRect(figureRect, paint);
                }
    }

    public void drawRightPanel(Canvas canvas) {
        paint.setColor(Color.GRAY);
        canvas.drawRect(figureWidth * ROWS, 0, bounds.width(), bounds.height(), paint);
    }

    public void drawScore(Canvas canvas) {
        paint.setColor(Color.GREEN);
        paint.setTextSize(figureHeight);
        canvas.drawText(Integer.toString(score), ROWS * figureWidth, 7 * figureHeight, paint);
    }

    public void drawDataStatus(Canvas canvas) {
        if (gameOver) canvas.drawText("Game Over", 10, bounds.height() / 2, paint);
        if (pause) canvas.drawText("Pause", 10, bounds.height() / 2, paint);
    }

    public void drawNextFigure(Canvas canvas) {
        paint.setColor(Color.YELLOW);
        for (int i = 0; i < 4; i++) {
            canvas.drawRect((ROWS + nextFigure.data[i][0]) * figureWidth + 1,
                    bounds.height() - (COLUMNS + nextFigure.data[i][1] - 4) * figureHeight + 1,
                    (ROWS + 1 + nextFigure.data[i][0]) * figureWidth - 1,
                    bounds.height() - (COLUMNS - 5 + nextFigure.data[i][1]) * figureHeight - 1, paint);
        }
    }

    public void drawCurrentFigure(Canvas canvas) {
        paint.setColor(Color.BLUE);
        for (int i = 0; i < 4; i++) {
            figureRect.set(
                    (currentFigure.data[i][0] + currentFigure.x - 4) * figureWidth + 1,
                    bounds.height() - (currentFigure.data[i][1] - 3 + currentFigure.y) * figureHeight + 1,
                    (currentFigure.data[i][0] - 3 + currentFigure.x) * figureWidth - 1,
                    bounds.height() - (currentFigure.data[i][1] - 4 + currentFigure.y) * figureHeight - 1);
            canvas.drawRect(figureRect, paint);
        }
    }


    public boolean onTouchEvent(MotionEvent me) {
        if ((!gameOver) && (!pause)) {
            if (me.getAction() == MotionEvent.ACTION_DOWN) {
                action = true;
                float x = me.getX();
                float y = me.getY();
                int height = bounds.height();
                int width = bounds.width();

                if (y < 3 * height / 4) {
                    if (x <= width / 5) {
                        strafetask = new MoveTask(MoveTask.MOVE_LEFT);
                        timer.schedule(strafetask, 0, 200);
                    } else if (x >= 4 * width / 5) {
                        strafetask = new MoveTask(MoveTask.MOVE_RIGHT);
                        timer.schedule(strafetask, 0, 200);
                    } else {
                        strafetask = new MoveTask(MoveTask.MOVE_ROTATE);
                        timer.schedule(strafetask, 0, 2 * DOWN_SPEED);
                    }
                } else {
                    strafetask = new MoveTask(MoveTask.MOVE_DROP);
                    timer.schedule(strafetask, 0);
                }
                action = false;
                invalidate();
            }
            if (me.getAction() == MotionEvent.ACTION_UP) {
                strafetask.cancel();
            }
        }
        return true;
    }

    private class MoveTask extends TimerTask {
        int move;
        static final int MOVE_LEFT = 0;
        static final int MOVE_ROTATE = 1;
        static final int MOVE_RIGHT = 2;
        static final int MOVE_DOWN = 3;
        static final int MOVE_DROP = 4;

        public MoveTask(int c) {
            move = c;
        }

        @Override
        public void run() {
            switch (move) {
                case MOVE_LEFT:
                    currentFigure.moveLeft(pool);
                    effects.playBell();
                    break;
                case MOVE_RIGHT:
                    currentFigure.moveRight(pool);
                    effects.playBell();
                    break;
                case MOVE_ROTATE:
                    currentFigure.rotate(pool);
                    effects.playBell();
                    break;
                case MOVE_DROP:
                    currentFigure.drop(pool);
                    break;
                case MOVE_DOWN: {

                    while (action) ;

                    if (currentFigure.down(pool) == false) {
                        if (newFigure == 0)
                            newFigure = 2;
                        else newFigure--;
                    }
                    if (newFigure == 1) {
                        effects.playHit();
                        currentFigure.print(pool);
                        delete();
                        currentFigure = new Figure(nextFigure);
                        nextFigure = new Figure();
                        newFigure = 0;
                    }
                    postInvalidate();
                }
            }
            postInvalidate();
        }
    }

    private void delete() {
        action = true;
        int x = 0;
        boolean combo;
        for (int j = 4; j < COLUMNS + 4; j++) {
            combo = true;
            for (int i = 4; i < ROWS + 4; i++)
                if (pool[i][j] == 0)
                    combo = false;
            if (combo) {
                for (int k = j; k < COLUMNS + 4; k++)
                    for (int l = 4; l < ROWS + 4; l++)
                        pool[l][k] = pool[l][k + 1];
                x++;
                j--;
            }
        }

        switch (x) {
            case 1:
                effects.playLine();
                score += 100;
                break;
            case 2:
                effects.playLine();
                score += 300;
                break;
            case 3:
                effects.playLine();
                score += 700;
                break;
            case 4:
                effects.playLine();
                score += 1500;
                break;
        }
        action = false;
        postInvalidate();
    }

    public void gameOver() {
        timer.cancel();
        gameOver = true;
        back.stop();
        effects.playGameOver();
        postInvalidate();
    }

    public void pause() {
        if (!pause) {
            timer.cancel();
            pause = true;
            postInvalidate();
        }
    }

    public void restore() {
        if (pause) {
            timer.cancel();
            timer = new Timer();
            MoveTask task = new MoveTask(MoveTask.MOVE_DOWN);
            timer.schedule(task, 0, DOWN_SPEED);
            pause = false;
        }
    }

    public void switchPause() {
        if (!gameOver) {
            if (pause) restore();
            else pause();
        }
    }


    public boolean isGameOver() {
        return gameOver;
    }

    private class Figure {
        private int colors[] = new int[4];
        private int data[][];
        private int x, y;

        public Figure() {
            x = ROWS / 2 + 2;
            y = COLUMNS + 3;
            Random r = new Random();
            int type = r.nextInt(7);
            switch (type) {
                case 0:
                    /*  0 1 2 3
                    * 0   @
                    * 1   @
                    * 2   @
                    * 3   @
                    * */
                    data = new int[][]{{1, 0}, {1, 1}, {1, 2}, {1, 3}};
                    break;
                case 1:
                    /*  0 1 2 3
                    * 0
                    * 1
                    * 2 @ @ @
                    * 3 @
                    * */
                    data = new int[][]{{0, 3}, {0, 2}, {1, 2}, {2, 2}};
                    break;
                case 2:
                    /*  0 1 2 3
                    * 0
                    * 1   @ @ @
                    * 2       @
                    * 3
                    * */
                    data = new int[][]{{1, 1}, {2, 1}, {3, 1}, {3, 2}};
                    break;
                case 3:
                    /*  0 1 2 3
                    * 0
                    * 1   @ @
                    * 2   @ @
                    * 3
                    * */
                    data = new int[][]{{1, 1}, {1, 2}, {2, 1}, {2, 2}};
                    break;
                case 4:
                    /*  0 1 2 3
                    * 0
                    * 1 @ @
                    * 2   @ @
                    * 3
                    * */
                    data = new int[][]{{0, 1}, {1, 1}, {1, 2}, {2, 2}};
                    break;
                case 5:
                    /*  0 1 2 3
                    * 0
                    * 1 @ @ @
                    * 2   @
                    * 3
                    * */
                    data = new int[][]{{0, 1}, {1, 1}, {2, 1}, {1, 2}};
                    break;
                case 6:
                    /*  0 1 2 3
                    * 0
                    * 1   @ @
                    * 2 @ @
                    * 3
                    * */
                    data = new int[][]{{0, 2}, {1, 2}, {1, 1}, {2, 1}};
                    break;
            }
            int x = r.nextInt(MAX_COLORS) + 1;
            for (int i = 0; i < 4; i++) {
                colors[i] = x;
            }
            if (crossing(pool)) {
                gameOver();
            }
        }

        public Figure(Figure copy) {
            data = new int[4][2];
            for (int i = 0; i < 4; i++) {
                data[i][0] = copy.data[i][0];
                data[i][1] = copy.data[i][1];
            }
            x = copy.x;
            y = copy.y;
            for (int i = 0; i < 4; i++)
                colors[i] = copy.colors[i];
        }

        public boolean rotate(int[][] pool) {
            int buf;
            Figure figure = new Figure(this);
            for (int i = 0; i < 4; i++) {
                buf = figure.data[i][0];
                figure.data[i][0] = figure.data[i][1];
                figure.data[i][1] = (int) (3 - buf);
            }
            if (!figure.crossing(pool))
                for (int i = 0; i < 4; i++) {
                    buf = data[i][0];
                    data[i][0] = data[i][1];
                    data[i][1] = (int) (3 - buf);
                }
            else
                return false;
            return true;
        }

        public boolean moveLeft(int[][] pool) {
            x--;

            if (crossing(pool))
                x++;
            else
                return true;
            return false;
        }

        public boolean moveRight(int[][] pool) {
            x++;
            if (crossing(pool))
                x--;
            else
                return true;

            return false;
        }

        public boolean down(int[][] pool) {
            Figure figure = new Figure(this);
            figure.y--;

            if (figure.crossing(pool)) {
                if (!figure.moveLeft(pool)) {
                    if (!figure.moveRight(pool)) {
                        newFigure = 1;
                        return true;
                    }
                }
                return false;
            } else y--;
            return true;
        }

        public void drop(int[][] pool) {
            do {
                y--;
            } while (!crossing(pool));
            y++;
        }

        public boolean crossing(int[][] pool) {
            for (int i = 0; i < 4; i++)
                if (pool[data[i][0] + x][data[i][1] + y] != 0) return true;
            return false;
        }

        public void print(int[][] pool) {
            for (int i = 0; i < 4; i++)
                pool[data[i][0] + x][data[i][1] + y] = colors[i];
        }
    }

    public class Effects {
        private SoundPool sp;
        private int hitId;
        private int gameOverId;
        private int bellId;
        private int lineId;

        public Effects(Context context) {
            sp = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
            hitId = sp.load(context, R.raw.hit, 1);
            gameOverId = sp.load(context, R.raw.game_over, 2);
            bellId = sp.load(context, R.raw.bell, 3);
            lineId = sp.load(context, R.raw.line, 4);
        }

        public void playHit() {
            sp.play(hitId, 1, 1, 1, 0, 1.0f);
        }

        public void playGameOver() {
            sp.play(gameOverId, 1, 1, 1, 0, 1.0f);
        }

        public void playBell() {
            sp.play(bellId, 1, 1, 1, 0, 1.0f);
        }

        public void playLine() {
            sp.play(lineId, 1, 1, 1, 0, 1.0f);
        }

        public void stop() {
            sp.stop(hitId);
            sp.stop(gameOverId);
            sp.stop(bellId);
        }
    }

    public void destroy() {
        back.stop();
        back = null;
        effects.stop();
        effects = null;
    }
}
