public class Statistics {

    private int totalBalls;
    private int pausedBalls;
    private int insideBH;
    private int outsideBH;

    public void setBall() {
        this.totalBalls += 1;

    }

    public void setInsideBH() {
        this.insideBH += 1;

    }



    public void setPausedBalls() {
        this.pausedBalls++;

    }

    public int getTotalBalls() {

        return this.totalBalls;
    }

    public int getPausedBalls() {

        return this.pausedBalls;
    }

    public int getInsideBH() {

        return this.insideBH;
    }

    public void removeBall(){
        this.totalBalls -=1;
    }

    public void removeFromBlackHole(){
        this.insideBH -= 1;
    }

}
