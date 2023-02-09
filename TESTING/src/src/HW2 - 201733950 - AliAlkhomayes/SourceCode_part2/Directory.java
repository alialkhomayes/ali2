package fapp;

import java.util.ArrayList;
import java.util.List;

public class Directory implements Details {
    private String name;
    private long size;


    public Directory(String name, long size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }


    @Override
    public String showDetails() {
        return getName() + " (" + getSize() + " KB)\n";
    }
}
