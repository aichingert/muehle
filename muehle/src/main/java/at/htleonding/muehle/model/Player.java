package at.htleonding.muehle.model;

public class Player {
    private Long id;
    private String name;
    private int pieces;
    private int color;

    public Player(String name) {
        this.name = name;
        this.pieces = 9;
    }

    public boolean setPiece(int x, int y) {
        if (pieces == 0) {
            return false;
        }



        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPieces() {
        return pieces;
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
