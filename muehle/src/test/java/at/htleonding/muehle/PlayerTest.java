package at.htleonding.muehle;
import at.htleonding.muehle.model.Player;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PlayerTest {
    @Test
    void declarePlayer() {
        // arrange
        Player player = new Player("Tobias Aichinger");

        // act

        // assert
        assertThat(player.getName()).isEqualTo("Tobias Aichinger");
    }
}
