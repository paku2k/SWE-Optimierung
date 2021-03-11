package swe_mo.reply;

public class Haus {
    short x;
    short y;
    byte latencyWeight;
    byte connectionSpeedWeight;

    public Haus(short x, short y, byte lat, byte conn){
        this.x = x;
        this.y = y;
        this.latencyWeight = lat;
        this.connectionSpeedWeight = conn;
    }
}
