package at.htlleonding.mill.model;

public class Move {
    private Long id;
    private double fx;
    private double fy;
    private double tx;
    private double ty;

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

    public void setFx(double fx) {
        this.fx = fx;
    }

    public double getFy() {
        return fy;
    }

    public void setFy(double fy) {
        this.fy = fy;
    }

    public double getTx() {
        return tx;
    }

    public void setTx(double tx) {
        this.tx = tx;
    }

    public double getTy() {
        return ty;
    }

    public void setTy(int ty) {
        this.ty = ty;
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
