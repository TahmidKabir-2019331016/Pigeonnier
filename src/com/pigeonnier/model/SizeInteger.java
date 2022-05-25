package com.pigeonnier.model;

public class SizeInteger implements Comparable<SizeInteger> {

    private int size;

    public SizeInteger(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        String s;
        if (size < 1024) {
            s = size + " B";
        } else if(size < 1048576) {
            s = (size / 1024) + " KB";
        } else {
            s = (size / 1048576) + " MB";
        }
        return s;
    }

    @Override
    public int compareTo(SizeInteger o) {
        if(this.size < o.size) return -1;
        return 1;
    }
}
