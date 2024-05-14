/**
 * This game's aim is hitting targets. Obstacles exist for getting harder to hit targets. The game is very enhanced it allows to player plays again.
 * First import allows change font of text and second one responds to the user pressing the keys
 * @author Beratcan Dogan, Student id: 2021400132
 * @since 21.03.2024
 */

import java.awt.*;
import java.awt.event.KeyEvent;

public class AngryBulletGame {
    /**
     * @param args main input arguments are not used
     */
    public static void main(String[] args) {
        // Game Parameters
        int width = 1600; //screen width
        int height = 800; // screen height
        double gravity = 9.80665; // gravity
        double x0 = 120; // x and y coordinates of the bullet’s starting position on the platform
        double y0 = 120;
        double bulletVelocity = 180; // initial velocity
        double bulletAngle = 45.0; // initial angle
        int radius = 3; //radius of ball
        double xCurrent = x0;
        double yCurrent = y0;
        double time ;
        StdDraw.setCanvasSize(width, height); // set the size of the drawing canvas
        StdDraw.setXscale(0, width); // set the scale of the coordinate system
        StdDraw.setYscale(0, height);
        StdDraw.setFont(new Font("Helvatica",Font.PLAIN,15));
        StdDraw.enableDoubleBuffering();

// Box coordinates for obstacles and targets
// Each row stores a box containing the following information:
// x and y coordinates of the lower left rectangle corner, width, and height
        double[][] obstacleArray = {
                {1200, 0, 60, 220},
                {1000, 0, 60, 160},
                {600, 0, 60, 80},
                {600, 180, 60, 160},
                {220, 0, 120, 180}
        };
        double[][] targetArray = {
                {1160, 0, 30, 30},
                {730, 0, 30, 30},
                {150, 0, 20, 20},
                {1480, 0, 60, 60},
                {340, 80, 60, 30},
                {1500, 600, 60, 60}
        };
        while(true){
            double startingTime = System.currentTimeMillis()/200.0;
            StdDraw.clear();
            //drawing obstacles and targets
            StdDraw.setPenColor(StdDraw.DARK_GRAY);
            for (double[] obs : obstacleArray) {
                StdDraw.filledRectangle(obs[0]+(obs[2]/2), obs[1]+(obs[3]/2), obs[2]/2, obs[3]/2);
            }
            StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
            for (double[] tar : targetArray) {
                StdDraw.filledRectangle(tar[0]+(tar[2]/2), tar[1]+(tar[3]/2), tar[2]/2, tar[3]/2);
            }

            //drawing shooting platform
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledRectangle(60,60,60,60);
            //setting speed and angle with arrow keys
            if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT))
                bulletVelocity++;
            if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT))
                bulletVelocity--;
            if (StdDraw.isKeyPressed(KeyEvent.VK_UP)&& 180>bulletAngle)
                bulletAngle++;
            if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN)&& bulletAngle>-90)
                bulletAngle--;
            //writing a and v variables value on shooting platform
            StdDraw.setPenColor(StdDraw.WHITE);
            String ang = "a: " + bulletAngle;
            StdDraw.text(60,70, ang);
            String vel = "v: " + bulletVelocity;
            StdDraw.text(60, 50, vel);
            //drawing initial line to predict where ball goes
            StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(StdDraw.BLACK);
            double xi = x0 + bulletVelocity * Math.cos(Math.toRadians(bulletAngle)) * 0.3;
            double yi = y0 + bulletVelocity * Math.sin(Math.toRadians(bulletAngle)) * 0.3;
            StdDraw.line(x0, y0, xi, yi);
            StdDraw.setPenRadius(0.001);
            boolean spaceCheck = StdDraw.isKeyPressed(KeyEvent.VK_SPACE);
            //after pressed space drawing ball's path
            int i = 0;
            if (spaceCheck){
            while(spaceCheck) {
                i++;
                double currentTime = System.currentTimeMillis() / 200.0;
                time = currentTime - startingTime;
                //to update coordinates I give initial x and y values.
                double x_before = xCurrent;
                double y_before = yCurrent;
                //because of initial velocity was very meaningless I scaled it again:)
                double scaledVelocity = bulletVelocity * 0.5787;
                //That variables show at the instantaneous position of the ball.
                xCurrent = x0 + scaledVelocity * time * Math.cos(Math.toRadians(bulletAngle));
                yCurrent = y0 + scaledVelocity * time * Math.sin(Math.toRadians(bulletAngle)) - gravity * time * time * 0.5;
                //this if block draws lines between two balls. In order to draw lines with two balls. Variable i must be more than 1.
                if(i>1)
                    StdDraw.line(x_before,y_before,xCurrent,yCurrent);
                StdDraw.filledCircle(xCurrent, yCurrent, radius);
                //checks whether the ball reached max x value.
                if (xCurrent > width) {
                    StdDraw.setPenColor(StdDraw.BLACK);
                    String text = "Max X reached. Press 'r' to shoot again.";
                    StdDraw.text(200, 750, text);
                    spaceCheck = false;
                }
                //checks whether the ball hit the ground.
                if (yCurrent < 0) {
                    StdDraw.setPenColor(StdDraw.BLACK);
                    String text = "Hit the ground. Press 'r' to shoot again.";
                    StdDraw.text(200, 750, text);
                    spaceCheck = false;
                }
                //checks whether the ball hit an obstacle.
                for (double[] obstacle : obstacleArray)
                    if (obstacle[0] <= xCurrent && xCurrent <= obstacle[0] + obstacle[2] && obstacle[1] <= yCurrent && yCurrent <= obstacle[1] + obstacle[3]) {
                        StdDraw.setPenColor(StdDraw.BLACK);
                        String text = "Hit an obstacle. Press 'r' to shoot again.";
                        StdDraw.text(200, 750, text);
                        spaceCheck = false;
                        break;
                    }
                //checks whether the ball hit a target.
                for (double[] target : targetArray)
                    if (target[0] <= xCurrent && xCurrent <= target[0] + target[2] && target[1] <= yCurrent && yCurrent <= target[1] + target[3]) {
                        StdDraw.setPenColor(StdDraw.BLACK);
                        String text = "Congratulations. You hit the target.";
                        StdDraw.text(200, 750, text);
                        spaceCheck = false;
                        break;
                    }
                StdDraw.show();
                StdDraw.pause(25);


            }
            //program waits player to press r to restart.
                while(true){
                    if(StdDraw.isKeyPressed(KeyEvent.VK_R)) {
                        bulletVelocity = 180; // initial velocity
                        bulletAngle = 45.0; //initial angle
                        break;
                    }
                }

            }
            StdDraw.show();
            StdDraw.pause(25);
        }
    }
}
