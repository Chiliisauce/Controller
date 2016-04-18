package com.example.dabai.controller.model;

/**
 * Represents the motion of the aerial vehicles
 * Created by cfc3434 on 2015/12/31.
 */
public class Motion {

    /**
     * Each attribute has a value from -1000 to 1000
     * Following the right hand rule
     */
    protected final int max = 1500;
    protected final int min = -1000;
    protected int throttle;
    protected int row;
    protected int yaw;
    protected int pitch;

    public Motion(){
        this.throttle = 0;
        this.row = 0;
        this.yaw = 0;
        this.pitch = 0;
    }

    public int getPitch() {
        return pitch;
    }

    public int getPitch(int min, int range) {
        return mapValueOut(min, range, pitch);
    }

    public int getRow() {
        return row;
    }

    public int getRow(int min, int range) {
        return mapValueOut(min, range, row);
    }

    public int getThrottle() {
        return throttle;
    }

    public int getThrottle(int min, int range) {
        return mapValueOut(min, range, throttle);
    }

    public int getYaw() {
        return yaw;
    }

    public int getYaw(int min, int range) {
        return mapValueOut(min, range, yaw);
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
        motionChanged();
    }

    public void setPitch(int min, int range,int pitch) {
        this.pitch = mapValueIn(min, range, pitch);
        motionChanged();
    }

    public void setRow(int row) {
        this.row = row;
        motionChanged();
    }

    public void setRow(int min, int range,int row) {
        this.row = mapValueIn(min, range, row);
        motionChanged();
    }

    public void setThrottle(int throttle) {
        this.throttle = throttle;
        motionChanged();
    }

    public void setThrottle(int min, int range,int throttle) {
        this.throttle = mapValueIn(min, range, throttle);
        motionChanged();
    }

    public void setYaw(int yaw) {
        this.yaw = yaw;
        motionChanged();
    }

    public void setYaw(int min, int range,int yaw) {
        this.yaw = mapValueIn(min, range, yaw);
        motionChanged();
    }

    public int getMaxValue(){
        return max;
    }

    public int getMinValue(){
        return min;
    }

    private void motionChanged(){
//        System.out.print("Row:" + this.row + "  ");
//        System.out.print("Yaw:" + this.yaw +"  ");
//        System.out.print("Pitch:" + this.pitch+"  ");
//        System.out.println("Throttle:"+ this.throttle+"  ");
    }

    // Map value from [min,(range+min)] to [this.min,this,max]
    private int mapValueIn(int min, int range, int value){
        return ((value-min)*(this.max-this.min))/range+this.min;
    }

    // Map value from [this.min,this,max] to [min,(range+min)]
    private int mapValueOut (int min, int range, int value){
        return ((value-this.min)*range)/(this.max-this.min)+min;
    }
}
