package src;


import java.util.Date;

public class Stage {

    private String objectValue, documentNumber, time;
    private int oldValue, newValue;
    private Date date;

    public Stage(String objectValue, String documentNumber, int oldValue,
            int newValue, Date date, String time) {
        this.objectValue = objectValue;
        this.documentNumber = documentNumber;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.date = date;
        this.time = time;
    }

    public Stage() {
    }

    public String getObjectValue() {
        return objectValue;
    }

    public void setObjectValue(String objectValue) {
        this.objectValue = objectValue;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public int getOldValue() {
        return oldValue;
    }

    public void setOldValue(int oldValue) {
        this.oldValue = oldValue;
    }

    public int getNewValue() {
        return newValue;
    }

    public void setNewValue(int newValue) {
        this.newValue = newValue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
