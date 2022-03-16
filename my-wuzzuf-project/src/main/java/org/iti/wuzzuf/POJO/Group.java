package org.iti.wuzzuf.POJO;

public class Group {

    String alias;
    long frequency;


    @Override
    public String toString() {
        return "Group{" +
                "alias='" + alias + '\'' +
                ", frequency='" + frequency + '\'' +
                '}';
    }

    public Group(String alias, long frequency) {
        this.alias = alias;
        this.frequency = frequency;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public long getFrequency() {
        return frequency;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }
}
