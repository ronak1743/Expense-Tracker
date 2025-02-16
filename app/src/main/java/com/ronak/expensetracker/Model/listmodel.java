package com.ronak.expensetracker.Model;

public class listmodel {
    String Date;
    String Time;
    String notes;
    String amount;
    String type;

    public listmodel() {
    }

    public listmodel(String string, String time, String notes, String amount, String type) {

        this.Date = string;
        Time = time;
        this.notes = notes;
        this.amount = amount;
        this.type = type;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String string) {
        this.Date = string;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
