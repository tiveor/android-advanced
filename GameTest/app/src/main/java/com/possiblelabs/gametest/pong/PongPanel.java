package com.possiblelabs.gametest.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * Based on a Tutorial
 * Created by possiblelabs on 9/8/15.
 */
public class PongPanel extends View {

    private final float paddleWidth = 100f;
    private final float paddleHeight = 30f;
    private final float space = 10f;
    private final float TOLERANCE = 20f;
    private final String COLOR_BACK = "#CC000000";
    private final String COLOR_BALL = "#FFFFFF";
    private final String COLOR_PAD1 = "#2096f3";
    private final String COLOR_PAD2 = "#22c9ef";
    private final float VEOLOCITY = 1.01f;


    private float ballX = 250f;
    private float ballY = 500f;
    private float ballXVelocity = 5f;
    private float ballYVelocity = 5f;
    private float ballRadius = 10f;

    private RectF ball = new RectF(ballX - ballRadius, ballY - ballRadius, ballX + ballRadius, ballY + ballRadius);
    private float pad1X = 0f;
    private float pad2X = 0f;
    private RectF pad1Graphic = new RectF(pad1X - paddleWidth, getHeight() - paddleHeight - space, pad1X + paddleWidth, getHeight() - space);
    private RectF pad2Graphic = new RectF(pad2X - paddleWidth, space, pad2X + paddleWidth, paddleHeight + space);
    private Paint painter = new Paint();

    public PongPanel(Context context) {
        super(context);
        setBackgroundColor(Color.parseColor(COLOR_BACK));
    }

    protected void onDraw(Canvas canvas) {
        physics();
        painter.setColor(Color.parseColor(COLOR_BALL));
        canvas.drawOval(ball, painter);
        painter.setColor(Color.parseColor(COLOR_PAD1));
        canvas.drawRect(pad1Graphic, painter);
        painter.setColor(Color.parseColor(COLOR_PAD2));
        canvas.drawRect(pad2Graphic, painter);
        invalidate();
    }

    private void physics() {
        ballX += ballXVelocity;
        ballY += ballYVelocity;

        if ((ball.left >= pad1Graphic.left) && (ball.right <= pad1Graphic.right) &&
                (ballY >= pad1Graphic.top) && (ballY > pad1Graphic.bottom))
            ballY = pad1Graphic.top - ballRadius;

        setBall();
        setPad1();
        setPad2();
        if (ballXVelocity > -TOLERANCE && ballXVelocity < TOLERANCE) {
            ballXVelocity *= VEOLOCITY;
            ballYVelocity *= VEOLOCITY;
        }
        getBallVelocity();
    }

    private void setBall() {
        ball.set(ballX - ballRadius, ballY - ballRadius, ballX + ballRadius, ballY + ballRadius);
    }

    private void setPad1() {
        if (ball.left + paddleWidth >= getWidth())
            pad1Graphic.set(getWidth() - paddleWidth, getHeight() - paddleHeight - space, getWidth(), getHeight() - space);
        else
            pad1Graphic.set(ball.left, getHeight() - paddleHeight - space, ball.left + paddleWidth, getHeight() - space);
    }

    private void setPad2() {
        if (ball.left + paddleWidth >= getWidth())
            pad2Graphic.set(getWidth() - paddleWidth, space, getWidth(), paddleHeight + space);
        else
            pad2Graphic.set(ball.left, space, ball.left + paddleWidth, paddleHeight + space);
    }

    private void getBallVelocity() {
        if ((ball.left >= pad1Graphic.left) && (ball.right <= pad1Graphic.right) &&
                (ball.bottom >= pad1Graphic.top) && (ball.bottom <= pad1Graphic.top + ballYVelocity)) {
            ballXVelocity = (Math.random() > 0.5) ? ballXVelocity : -ballXVelocity;
            ballYVelocity = -ballYVelocity;
        }
        if (((ball.left >= pad2Graphic.left) && (ball.right <= pad2Graphic.right)) && (ball.top <= pad2Graphic.bottom)) {
            ballXVelocity = (Math.random() > 0.5) ? ballXVelocity : -ballXVelocity;
            ballYVelocity = Math.abs(ballYVelocity);
        }
        if (ball.right >= getWidth())
            ballXVelocity = -ballXVelocity;
        if (ball.left <= 0f)
            ballXVelocity = -ballXVelocity;
        if (ball.top >= getHeight())
            ballYVelocity = -ballYVelocity;
        if (ball.bottom <= 0f)
            ballYVelocity = -ballYVelocity;
    }
}
