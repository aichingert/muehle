package at.htleonding.muehle;
import at.htleonding.muehle.model.Logic;
import at.htleonding.muehle.model.Muehle;
import at.htleonding.muehle.model.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LogicTest {
    @Test
    void switchDimensionsTest() {
        // arrange
        Position okPosition = new Position(1, 0, 2);
        Position notOkPosition = new Position(1, 1, 0);
        Position topLeftCorner = new Position(0, 0, 1);
        Position bottomRightCorner = new Position(2, 2, 0);

        // act

        // assert
        assertThat(Logic.isAbleToSwitchDimensions(okPosition)).isTrue();
        assertThat(Logic.isAbleToSwitchDimensions(notOkPosition)).isFalse();
        assertThat(Logic.isAbleToSwitchDimensions(topLeftCorner)).isFalse();
        assertThat(Logic.isAbleToSwitchDimensions(bottomRightCorner)).isFalse();
    }

    @Test
    void possibleDirections() {
        // arrange
        Muehle muehle = new Muehle(null, null);
        Position currPosition = new Position(1, 2, 0);

        Position firstPos = new Position(0, 2, 0);
        Position secPos = new Position(2, 2, 0);
        Position thirdPos = new Position(1, 2, 1);

        // act
        List<Position> possiblePositions = Logic.getMoves(muehle, currPosition);
        System.out.printf(possiblePositions.toString());

        // assert
        assertThat(possiblePositions).contains(firstPos);
        assertThat(possiblePositions).contains(secPos);
        assertThat(possiblePositions).contains(thirdPos);
    }
}
