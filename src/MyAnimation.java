
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.util.RandomGenerator;
import com.shpp.cs.a.graphics.WindowProgram;

import java.awt.*;


/**
 * File: MyAnimation.java
 *
 * My animation
 *
 * More details in Task #3 - Problem-solving in Java, Part 6 - Five seconds of glory
 *
 */
public class MyAnimation extends WindowProgram {

    /**
     * The default width and height of the window. These constants will tell Java to
     * create a window whose size is *approximately* given by these dimensions.
     */
    public static final int APPLICATION_WIDTH = 800;
    public static final int APPLICATION_HEIGHT = 600;

    /** The size of the ball. */
    private static final double BALL_SIZE = 80;

    /** The amount of time to pause between frames (50 fps). */
    private static final double PAUSE_TIME = 1000.0 / 50;

    /** The initial horizontal velocity of the ball. */
    private static final double HORIZONTAL_VELOCITY = 7.0;

    /** The initial vertical velocity of the ball. */
    private static final double VERTICAL_VELOCITY = -7.0;

    /** Gravitational acceleration. */
    private static final double GRAVITY = 0.22;

    /** Time of the beginning of the program */
    private static double startMoment = 0;

    /** Size of little balls than lay in main balls*/
    private double sizeOfLittleOnes;

    /** This method plays the animation and print the time of animation*/
    public void run() {
        startMoment = System.nanoTime();
        theAnimation();
        println(System.nanoTime() - startMoment + " seconds");
    }

    /**
     * This method plays my own animation.
     * Animation lasts for 5 sec.
     */
    private void theAnimation() {
        GOval leftBall = createCircle(0, getHeight() - BALL_SIZE, BALL_SIZE);
        leftBall.setFillColor(Color.BLACK);
        GOval rightBall = createCircle(getWidth() - BALL_SIZE, getHeight() - BALL_SIZE, BALL_SIZE);
        rightBall.setFillColor(Color.RED);
        animationWithMainBalls(leftBall, rightBall);
    }

    /**
     * In this animation method, 2 balls from different angles fly according to a given trajectory until they collide.
     * When the balls collide, call the method animationWithLittleBalls \\each ball is divided into 3 small balls that lie inside the main layer.
     * Small balls fly in opposite directions and increasing their size.
     * When ball touches the edge of the frame, is there until the end of animation.
     * Animation lasts for 5 sec.
     */
    private void animationWithMainBalls(GOval ball1, GOval ball2) {
        /* Downward velocity */
        double deltaY = VERTICAL_VELOCITY * 1.25;

        /* The cycle is performed until 2 balls had not collide */
        while (ball1.getX() + BALL_SIZE < ball2.getX()) {

            ball1.move(HORIZONTAL_VELOCITY, deltaY);
            ball2.move(-HORIZONTAL_VELOCITY, deltaY);
            deltaY += GRAVITY;
            pause(PAUSE_TIME);
            if (System.nanoTime() / 1000000000.0 - startMoment / 1000000000.0 >= 5) break;

        }
        animationWithLittleBalls(ball1, ball2);

    }

    /**
     * Each ball is divided into 3 small balls that lie inside the main layer.
     * Small balls fly in opposite directions and increasing their size.
     * When ball touches the edge of the frame, it stay there until the end of animation.
     * Animation lasts for 5 sec.
     */
    private void animationWithLittleBalls(GOval ball1, GOval ball2) {
        /* Size of little balls than lay in main balls*/
        sizeOfLittleOnes = BALL_SIZE / 2;

        /*             Adding little balls that laying inside left main ball                                */
        GOval littleBall11 = createCircle(ball1.getX(), ball1.getY(), sizeOfLittleOnes);
        GOval littleBall12 = createCircle(ball1.getX() + sizeOfLittleOnes, ball1.getY() + sizeOfLittleOnes / 4.0, sizeOfLittleOnes);
        GOval littleBall13 = createCircle(ball1.getX() + sizeOfLittleOnes / 4.0, ball1.getY() + sizeOfLittleOnes, sizeOfLittleOnes);

        /*             Adding little balls that laying inside right main ball                                */
        GOval littleBall21 = createCircle(ball2.getX(), ball2.getY(), sizeOfLittleOnes);
        GOval littleBall22 = createCircle(ball2.getX() + sizeOfLittleOnes, ball1.getY() + sizeOfLittleOnes / 4.0, sizeOfLittleOnes);
        GOval littleBall23 = createCircle(ball2.getX() + sizeOfLittleOnes / 4.0, ball2.getY() + sizeOfLittleOnes, sizeOfLittleOnes);

        remove(ball1);
        remove(ball2);

        /* variable that increasing the size with each iteration */
        int i = 1;

        do {
            i++;
            /* Animation for littleBall11 (moving left and up)*/
            moveLittleBallChangingSizeAndColor(littleBall11, i, -HORIZONTAL_VELOCITY, VERTICAL_VELOCITY);

            /* Animation for littleBall11 (moving left)*/
            moveLittleBallChangingSizeAndColor(littleBall12, i, -HORIZONTAL_VELOCITY, 0);

            /* Animation for littleBall11 (moving left and down) */
            moveLittleBallChangingSizeAndColor(littleBall13, i, -HORIZONTAL_VELOCITY, -VERTICAL_VELOCITY);

            /* Animation for littleBall11 (moving right and up)*/
            moveLittleBallChangingSizeAndColor(littleBall21, i, HORIZONTAL_VELOCITY, -VERTICAL_VELOCITY);

            /* Animation for littleBall11 (moving right)*/
            moveLittleBallChangingSizeAndColor(littleBall22, i, HORIZONTAL_VELOCITY, 0);

            /* Animation for littleBall11 (moving right and down)*/
            moveLittleBallChangingSizeAndColor(littleBall23, i, HORIZONTAL_VELOCITY, VERTICAL_VELOCITY);


            pause(PAUSE_TIME);
        } while (!(System.nanoTime() / 1000000000.0 - startMoment / 1000000000.0 >= 5));
    }

    /**
     * Ball fly in opposite direction and increasing its size.
     * When ball touches the edge of the frame, it stay there until the end of animation and changing color randomly.
     *
     * @param ball Object on which the transformation is performed
     * @param i    Changing size of param ball
     * @param x    The x coordinate of the upper-left corner of the circle.
     * @param y    The y coordinate of the upper-left corner of the circle.
     */
    private void moveLittleBallChangingSizeAndColor(GOval ball, int i, double x, double y) {
        if (isInsideFrame(ball)) {
            ball.move(x, y);
            ball.setSize(sizeOfLittleOnes + 2 * i, sizeOfLittleOnes + 2 * i);
        } else {
            ball.setFillColor(RandomGenerator.getInstance().nextColor());
        }
    }

    /**
     * @return true if the object inside frame
     */
    private boolean isInsideFrame(GObject gObject) {
        return gObject.getX() > 0 && gObject.getY() > 0 &&
                gObject.getX() < getWidth() - gObject.getWidth() && gObject.getY() < getHeight() - gObject.getHeight();
    }

    /**
     * Create the circle with the specified bounds
     *
     * @param x        The x coordinate of the upper-left corner of the circle.
     * @param y        The y coordinate of the upper-left corner of the circle.
     * @param diameter The diameter of the circle.
     * @return circle figure
     */
    private GOval createCircle(double x, double y, double diameter) {
        GOval circle = new GOval(x, y, diameter, diameter);
        circle.setFilled(true);
        circle.setFillColor(RandomGenerator.getInstance().nextColor());
        add(circle);
        return circle;
    }
}
