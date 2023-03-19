package at.htleonding.muehle;
import at.htleonding.muehle.model.Logic;
import at.htleonding.muehle.model.Position;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LogicTest {
    @Test
    void switchDimensionsTest() {
        // arrange

        // act
        Position okPosition = new Position(1, 0, 2);
        Position notOkPosition = new Position(1, 1, 0);

        // assert
        assertThat(Logic.isAbleToSwitchDimensions(okPosition)).isEqualTo(true);
        assertThat(Logic.isAbleToSwitchDimensions(notOkPosition)).isEqualTo(false);
    }
}
