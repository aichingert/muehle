package at.htleonding.muehle;
import at.htleonding.muehle.model.Muehle;
import at.htleonding.muehle.model.Position;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MuehleTest {
    @Test
    void setPiecesTest() {
        // arrange
        Muehle muehle = new Muehle();

        // act
        muehle.setPiece(1, new Position(0, 0, 0));
        muehle.setPiece( 2, new Position(1, 0, 0));
        muehle.setPiece( 2, new Position(0, 0, 0));

        // assert
        assertThat(muehle.getValueAt(new Position(0, 0, 0))).isEqualTo(1);
        assertThat(muehle.getValueAt(new Position(1, 0, 0))).isEqualTo(2);
    }

    @Test
    void illegalMovesTest() {
        // arrange
        Muehle muehle = new Muehle();

        // act
        boolean isMoveOk = muehle.setPiece(1, new Position(1, 1, 0));

        // assert
        assertThat(isMoveOk).isEqualTo(false);
    }

    @Test
    void upMovesTest() {
        // arrange
        Muehle muehle = new Muehle();

        // act
        //todo

        // assert
        assertThat(1+1).isEqualTo(2);
    }

    @Test
    void downMovesTest() {
        // arrange
        Muehle muehle = new Muehle();

        // act
        //todo

        // assert
        assertThat(1+1).isEqualTo(2);
    }

    @Test
    void leftMovesTest() {
        // arrange
        Muehle muehle = new Muehle();

        // act
        //todo

        // assert
        assertThat(1+1).isEqualTo(2);
    }

    @Test
    void rightMovesTest() {
        // arrange
        Muehle muehle = new Muehle();

        // act
        //todo

        // assert
        assertThat(1+1).isEqualTo(2);
    }
}
