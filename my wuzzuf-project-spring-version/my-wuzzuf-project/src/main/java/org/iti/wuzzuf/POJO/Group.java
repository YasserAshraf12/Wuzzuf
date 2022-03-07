package org.iti.wuzzuf.POJO;

public class Group {
    String company;
    long frequency;

    @Override
    public String toString() {
        return "Group{" +
                "company='" + company + '\'' +
                ", frequency='" + frequency + '\'' +
                '}';
    }

    public Group(String company, long frequency) {
        this.company = company;
        this.frequency = frequency;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public long getFrequency() {
        return frequency;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }
}
