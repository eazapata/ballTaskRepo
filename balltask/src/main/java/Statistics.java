public class Statistics {

    private int totalBalls;
    private int pausedBalls;
    private int insideBH;
    private int outsideBH;

    public void setBall() {
        this.totalBalls += 1;

    }

    public int getTotalBalls() {

        return this.totalBalls;
    }

    public void setPausedBalls() {
        this.pausedBalls++;

    }

    public int getPausedBalls() {

        return this.pausedBalls;
    }

    public void setInsideBH() {
        this.insideBH += 1;

    }

    public int getInsideBH() {

        return this.insideBH;
    }

    public void removeBall(){
        this.insideBH -=1;
    }

}
