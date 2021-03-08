package balltask;

public class Statistics {

    private int totalBalls;
    private int insideBH;
    private int waitingBalls;

    public void setBall() {
        this.totalBalls += 1;

    }

    public void setInsideBH() {
        this.insideBH += 1;

    }

    public void setWaitingBalls() {
        this.waitingBalls += 1;
    }

    public int getTotalBalls() {

        return this.totalBalls;
    }

    public int getWaitingBalls() {
        return waitingBalls;
    }

    public int getInsideBH() {

        return this.insideBH;
    }

    public void removeBall() {
        this.totalBalls -= 1;
    }

    public void removeFromBlackHole() {
        this.insideBH -= 1;
    }

    public void removeWaitingBalls() {
        this.waitingBalls -= 1;
    }

    public void resetStatistics() {
        this.totalBalls = 0;
        this.waitingBalls = 0;
        this.insideBH = 0;
    }

}