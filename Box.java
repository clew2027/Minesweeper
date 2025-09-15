package org.cis1200.mineSweeper;

public class Box {
    private int status; // how many adjacent, 10 if is mine
    private boolean isExposed; // if the number is exposed

    public Box(int status) {
        this.status = status;
        this.isExposed = false;
    }

    public int getStatus() {
        return this.status;
    }

    public boolean isExposed() {
        return isExposed;
    }

    public void setExposed(boolean exposed) {
        isExposed = exposed;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
