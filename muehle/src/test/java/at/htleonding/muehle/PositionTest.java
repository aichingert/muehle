package at.htleonding.muehle;
import at.htleonding.muehle.model.Position;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PositionTest {
    @Test
    void positionTest() {
        // arrange

        // act
        Position position = new Position(0, 2, 1);

        // assert
        assertThat(position.getX()).isEqualTo(0);
        assertThat(position.getY()).isEqualTo(2);
        assertThat(position.getZ()).isEqualTo(1);
    }
}
