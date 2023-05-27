package at.htlleonding.mill.view;

import at.htlleonding.mill.model.helper.Position;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class GameBoard extends Pane {
    public static final double OFFSET = 10.0;
    private final int WIDTH = 400;
    private final int HEIGHT = 400;
    private final double START = 50.0;
    private final double BOARD_SIZE = Math.min(WIDTH, HEIGHT) - 2.0 * START;
    private final double A_SIXTH = BOARD_SIZE / 6.0;

    private final List<Pair<Double, Double>> circleCoordinates = new ArrayList<>();
    public GameBoard() {
        drawBoard();
    }

    private void drawBoard() {
        this.setWidth(WIDTH);
        this.setHeight(HEIGHT);
        System.out.println(BOARD_SIZE);

        // Draw outer square
        drawSquare(START, START, BOARD_SIZE);

        // Draw middle square
        drawSquare(START + A_SIXTH, START + A_SIXTH, 2.0 * BOARD_SIZE / 3.0);

        // Draw inner square
        drawSquare(START + 2.0 * A_SIXTH, START + 2.0 * A_SIXTH, 2.0 * A_SIXTH);

        // Draw lines connecting squares
        drawConnectingLines(START, A_SIXTH);

        // Draw intersections as circles
        for (int dimension = 0; dimension < 3; dimension++) {
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (row == 1 && col == 1) continue;

                    double x = START + col * ((3 - dimension) * A_SIXTH) + dimension * A_SIXTH;
                    double y = START + row * ((3 - dimension) * A_SIXTH) + dimension * A_SIXTH;
                    circleCoordinates.add(new Pair<>(x,y));
                    drawIntersection(x, y, Color.BLACK, 3);
                }
            }
        }
    }

    private void drawSquare(double x, double y, double size) {
        Line topLine = new Line(x, y, x + size, y);
        Line bottomLine = new Line(x, y + size, x + size, y + size);
        Line leftLine = new Line(x, y, x, y + size);
        Line rightLine = new Line(x + size, y, x + size, y + size);

        getChildren().addAll(topLine, bottomLine, leftLine, rightLine);
    }

    private void drawConnectingLines(double padding, double aSixth) {
        Line leftToMiddleRight = new Line(padding, padding + 3 * aSixth, padding + 2 * aSixth, padding + 3 * aSixth);
        Line topToMiddleBottom = new Line(padding + 3 * aSixth, padding, padding + 3 * aSixth, padding + 2 * aSixth);

        Line rightToMiddleLeft = new Line(padding + 6 * aSixth, padding + 3 * aSixth, padding + 4 * aSixth, padding + 3 * aSixth);
        Line bottomToMiddleTop = new Line(padding + 3 * aSixth, padding + 6 * aSixth, padding + 3 * aSixth, padding + 4 * aSixth);

        getChildren().addAll(leftToMiddleRight, topToMiddleBottom, rightToMiddleLeft, bottomToMiddleTop);
    }

    public void drawIntersection(double x, double y, Color color, double radius) {
        Circle intersection = new Circle(x, y, radius);
        intersection.setFill(color);
        intersection.setStroke(Color.BLACK);
        getChildren().add(intersection);
    }

    public boolean containsCoordinate(double x, double y) {
        for(Pair<Double,Double> cord : circleCoordinates) {
            if (cord.getKey() - OFFSET < x && cord.getKey() + OFFSET > x &&
            cord.getValue() - OFFSET < y && y < cord.getValue() + OFFSET) {
                return true;
            }
        }

        return false;
    }

    public Position convertCoordinateToPosition(double x, double y) {
        int posX = -1;
        int posY = -1;
        int posZ;

        x -= START;
        y -= START;

        if (x > 2 * A_SIXTH - OFFSET && x < 4 * A_SIXTH + OFFSET &&
            y > 2 * A_SIXTH - OFFSET && y < 4 * A_SIXTH + OFFSET) {
            posZ = 2;
        }
        else if (x > A_SIXTH - OFFSET && x < 5 * A_SIXTH + OFFSET &&
                y > A_SIXTH - OFFSET && y < 5 * A_SIXTH + OFFSET) {
            posZ = 1;
        }
        else {
            posZ = 0;
        }

        int helper = (int)(Math.round(x / A_SIXTH));
        switch (helper) {
            case 0, 1, 2 -> posX = 0;
            case 3 -> posX = 1;
            case 4, 5, 6 -> posX = 2;
        }

        helper = (int)(Math.round(y / A_SIXTH));
        switch (helper) {
            case 0, 1, 2 -> posY = 0;
            case 3 -> posY = 1;
            case 4, 5, 6 -> posY = 2;
        }

        return new Position(posX, posY, posZ);
    }
}