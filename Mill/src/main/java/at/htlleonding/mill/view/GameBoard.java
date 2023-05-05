package at.htlleonding.mill.view;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class GameBoard extends Pane {
    public GameBoard() {
        drawBoard();
    }

    private void drawBoard() {
        this.setWidth(400);
        this.setHeight(400);
        double start = 50;
        double boardSize = Math.min(getWidth(), getHeight()) - 2 * start;
        double aSixth = boardSize / 6;
        System.out.println(boardSize);

        // Draw outer square
        drawSquare(start, start, boardSize);

        // Draw middle square
        drawSquare(start + aSixth, start + aSixth, 2 * boardSize / 3);

        // Draw inner square
        drawSquare(start + 2 * aSixth, start + 2 * aSixth, 2 * aSixth);

        // Draw lines connecting squares
        drawConnectingLines(start, aSixth);

        // Draw intersections as circles
        for (int dimension = 0; dimension < 3; dimension++) {
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (row == 1 && col == 1) continue;

                    double x = start + col * ((3 - dimension) * aSixth) + dimension * aSixth;
                    double y = start + row * ((3 - dimension) * aSixth) + dimension * aSixth;
                    drawIntersection(x, y, Color.BLACK);
                }
            }
        }

        /*Circle intersection = new Circle(50, 50, 3);
        intersection.setFill(Color.RED);
        getChildren().add(intersection);*/
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

    public void drawIntersection(double x, double y, Color color) {
        Circle intersection = new Circle(x, y, 3);
        intersection.setFill(color);
        getChildren().add(intersection);
    }

}