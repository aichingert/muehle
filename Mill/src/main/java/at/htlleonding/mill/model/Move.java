package at.htlleonding.mill.model;

public class Move {
    private Long id;
    private final double fx;
    private final double fy;
    private final double tx;
    private final double ty;

    public Move(double fx, double fy, double tx, double ty) {
        this.fx = fx;
        this.fy = fy;
        this.tx = tx;
        this.ty = ty;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public double getFx() {
        return fx;
    }
    public double getFy() {
        return fy;
    }
    public double getTx() {
        return tx;
    }
    public double getTy() {
        return ty;
    }

    @Override
    public String toString() {
        return "Move{" +
                "id=" + id +
                ", fx=" + fx +
                ", fy=" + fy +
                ", tx=" + tx +
                ", ty=" + ty +
                '}';
    }
}
