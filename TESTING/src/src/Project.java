package src;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.stream.Collectors;

public class Project {

    private String nodeID, createdOn;
    private int stage;
    private String cusID, changedOn;
    private Date startDate;
    private Date endDate;
    private ArrayList<Stage> Stages;
    private final String monthsArray[] = {"Jan", "Feb", "Mar", "Apr",
        "May", "June", "July", "Aug", "Sep",
        "Oct", "Nov", "Dec"};

    private final ArrayList<Date> datesList;

    public Project(String nodeID, int stage, String cusID, Date startDate,
            String createdOn, Date endDate, String changedOn) {
        this.nodeID = nodeID;
        this.cusID = cusID;
        this.startDate = startDate;
        this.createdOn = createdOn;
        this.endDate = endDate;
        this.changedOn = changedOn;
        this.stage = stage;
        Stages = new ArrayList<>();
        datesList = new ArrayList<>();
    }

    public Project() {
        Stages = new ArrayList<>();
        datesList = new ArrayList<>();
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getCustomerID() {
        return cusID;
    }

    public void setProID(String cusID) {
        this.cusID = cusID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getChangedOn() {
        return changedOn;
    }

    public void setChangedOn(String changedOn) {
        this.changedOn = changedOn;
    }

    public void addStage(Stage s) {
        Stages.add(s);
    }

    public int getStagesSize() {
        return Stages.size();
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public String getCusID() {
        return cusID;
    }

    public void setCusID(String cusID) {
        this.cusID = cusID;
    }

    public ArrayList<Date> getDatesList() {

        ArrayList<Stage> stageArrayList = getStages();
        for (int i = 0; i < stageArrayList.size(); i++) {
            Date date = stageArrayList.get(i).getDate();

            datesList.add(date);

        }
        datesList.sort((o1, o2) -> o1.compareTo(o2));
        return (ArrayList<Date>) datesList.stream().distinct().collect(Collectors.toList());

    }

    public ArrayList<String> getMonthsList(ArrayList<Date> list) {
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(list.get(i));
            String month = monthsArray[calendar.get(Calendar.MONTH)];
            int year = calendar.get(Calendar.YEAR);
            if (!temp.contains(month + " " + year)) {
                temp.add(month + " " + year);

            }
        }
        return temp;
    }

    public ArrayList<Stage> getStages() {
        return Stages;
    }

    @Override
    public String toString() {
        return "Project [nodeID=" + nodeID + ", createdOn=" + createdOn
                + ", cusID=" + cusID + ", changedOn="
                + changedOn + ", startDate=" + startDate + ", endDate="
                + endDate + ", Stages=" + Stages + "]";
    }

    public long getProjectDuration() {
        ArrayList<Date> datesListTemp = getDatesList();
        Date d1 = datesListTemp.get(0);
        Date d2 = datesListTemp.get(datesListTemp.size() - 1);
        long difference_In_Time
                = d2.getTime() - d1.getTime();

        long projectDuration
                = (difference_In_Time
                / (1000 * 60 * 60 * 24))
                % 365;

        return projectDuration;
    }

    public void setStages(ArrayList<Stage> stages) {
        Stages = stages;
    }
}
