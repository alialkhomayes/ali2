package src;






import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.canvas.GraphicsContext;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class Controller extends Pane {

    private ArrayList<String> tempStageList = new ArrayList<>();
    private TableView<Project> table;
    private TableColumn<Project, String> colIndex;
    private TableColumn<Project, String> colProject_ID;
    private TableColumn<Project, Integer> colStages;

    private TableColumn<Project, Date> colStartDate;
    private TableColumn<Project, Date> colEndDate;
    private static ArrayList<Project> projects;
    private final Text text;

    private final String months[] = {"Jan", "Feb", "Mar", "Apr",
        "May", "June", "July", "Aug", "Sep",
        "Oct", "Nov", "Dec"};

    private Canvas canvas;

    public Controller() throws IOException, ParseException {
        readXLS();
        text = new Text();
        setTable();
        setCanvas();
    }

    private void setCanvas() {
        canvas = new Canvas();
        canvas.setWidth(500);
        canvas.setHeight(300);
        canvas.setLayoutX(240);
        canvas.setLayoutY(120);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(0, 0, 500, 300);
        this.getChildren().add(canvas);
    }

    private void setTable() {
        table = new TableView();

        colIndex = new TableColumn("No");
        colProject_ID = new TableColumn("Project ID");
        colStages = new TableColumn("Stages");
        colStartDate = new TableColumn("Start Date");
        colEndDate = new TableColumn("End Date");

        ObservableList<Project> data = FXCollections.observableArrayList();

        for (int i = 0; i < projects.size(); i++) {
            data.add(projects.get(i));
        }

        colIndex.setCellValueFactory((TableColumn.CellDataFeatures<Project, String> p) -> new ReadOnlyObjectWrapper((table.getItems().indexOf(p.getValue()) + 1) + ""));
        colProject_ID.setCellValueFactory(new PropertyValueFactory<>("CusID"));
        colStages.setCellValueFactory(new PropertyValueFactory("Stage"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("StartDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("EndDate"));

        table.setPrefWidth(200);
        table.setPrefHeight(420);

        colIndex.setSortable(false);

        table.setItems(data);
        table.getColumns().addAll(colIndex, colProject_ID, colStages, colStartDate, colEndDate);
        table.setLayoutX(30);
        table.setLayoutY(30);

        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListenerImpl());
        this.getChildren().add(table);

    }

    private void showProjectView(GraphicsContext g2d, Project project) {
        Project projectData = getProjectFromProject(project);
        int startX = Integer.MAX_VALUE, endX = Integer.MIN_VALUE;
        g2d.setFill(Color.WHITESMOKE);
        g2d.fillRect(0, 0, 500, 300);
        g2d.setFill(Color.GREEN);
        ArrayList<Date> dateArrayList = projectData.getDatesList();
        ArrayList<String> monthsList = projectData.getMonthsList(dateArrayList);
        ArrayList<Stage> stageArrayList = projectData.getStages();
        int totalMonths = monthsList.size();
        int diff = 460 / totalMonths;
        drawButtomLines(g2d);

        Color color = Color.GREEN;
        String next = getNextMonth(monthsList.get(totalMonths - 1));
        int x = 20;
        for (int i = 0; i < totalMonths; i++) {
            g2d.setStroke(Color.GREEN);
            g2d.setFont(new Font("verdana", 8));

            if (i != 0) {
                g2d.setFill(Color.GRAY);
                g2d.fillRect(x, 243, 1, 10);
            }
            g2d.strokeText(monthsList.get(i), x - 20, 261);
            g2d.strokeText(next, 460, 261);

            int ite = 1;
            tempStageList = new ArrayList<>();
            for (int j = 0; j < stageArrayList.size(); j++) {

                Stage stage = stageArrayList.get(j);
                Date date = stage.getDate();
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                String mont1 = months[calendar.get(Calendar.MONTH)];
                int yer = calendar.get(Calendar.YEAR);
                int temp = 0;
                
                
                if (j > 0 && stage.getOldValue() < stageArrayList.get(j - 1).getOldValue()) {
                    color = Color.RED;
                } else {
                    color = Color.GREEN;
                }
                if (monthsList.get(i).equals(mont1 + " " + yer)) {
                    if (!tempStageList.contains(stage.getObjectValue())) {
                        tempStageList.add(stage.getObjectValue());
                    }
                    temp = (diff / 30) * calendar.get(Calendar.DAY_OF_MONTH);
                }

                if (temp > 0) {
                    g2d.setStroke(color);
                    g2d.setFont(new Font("verdana", 7));
                    g2d.strokeText(+(calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.YEAR), x + temp - 20, 275);
                    g2d.setFill(Color.YELLOW);
                    g2d.fillRect(x + temp, 250 - (((ite) * 9)), 1, ((ite) * 9));
                    g2d.setFill(color);
                    g2d.strokeText((stage.getOldValue()) + "", x + temp + 5, 250 - (((ite) * 9)));

                    g2d.setFill(color);
                    g2d.fillRect(x + temp + 1, 250 - ((ite * 9)) - 3, 3, 3);

                    	//FOR RED LINE
                    if (startX == Integer.MAX_VALUE) {
                        startX = x + temp;
                    }
                    if (endX < x + temp) {
                        endX = x + temp;
                    }
                    ite++;
                }
            }

            x += diff;

        }

        showDuration(g2d, startX, endX, project, projectData);
        showDurationn(g2d, startX, endX, project, projectData);


    }
    //day lines
    private void drawButtomLines(GraphicsContext g2d) {
        g2d.setFill(Color.GREEN);
        g2d.fillRect(20, 250, 460, 1);
        g2d.fillRect(20, 245, 1, 10);
        g2d.fillRect(480, 245, 1, 10);

        int x = 20;
        for (int i = 0; i < 100; i++) {
            if (x <= 480) {
                g2d.fillRect(x, 247, 1, 6);
            }
            x += 5;
        }
    }

    private void showDuration(GraphicsContext g2d, int startX, int endX, Project project, Project projectData) {
        g2d.setFill(Color.WHITESMOKE);
        g2d.fillRect(15, 15, 470, 20);
        g2d.setStroke(Color.BLACK);	// project id word
        g2d.setFont(new Font("verdana", 14));
        g2d.strokeText("Project ID    " + project.getCusID(), 20, 30); //stage name
        g2d.setStroke(Color.RED);// dipretion word
        g2d.setFill(Color.RED); //deuretion line
        int posX = startX + ((endX - startX) / 2) - 50;

        if (posX <= 350) {
            g2d.strokeText("Duration: " + projectData.getProjectDuration() + " Days", posX, 90);
        } else {
            g2d.strokeText("Duration: " + projectData.getProjectDuration() + " Days", 20, 60);
        }
        if (projectData.getProjectDuration() > 0) {
            g2d.fillRect(startX, 100, endX - startX, 2);
            g2d.fillRect(startX, 97, 2, 8);
            g2d.fillRect(endX, 97, 2, 8);
        }

    }
    private void showDurationn(GraphicsContext g2d, int startX, int endX, Project project, Project projectData) {
        g2d.setFill(Color.WHITESMOKE);        
        ArrayList<Stage> stageArrayList = projectData.getStages();
        int nBeforeRework = 0;
        int nAfterRework = 0;
        int breker = 0;
        int uu = 0; //toContinueInsecondLoop
        
        for (int j = 0; breker!=5 && j < stageArrayList.size(); j++) {
            Stage stage = stageArrayList.get(j);
            Date date = stage.getDate();
            if ( j > 0 && (stage.getOldValue() < stageArrayList.get(j - 1).getOldValue() )) {
            	nBeforeRework++;           
            } else if ( stageArrayList.get(j).getOldValue()==5) {
            	breker=5; 
            	}
            uu++;
            }
        for (int j = 0; breker==5 && uu < stageArrayList.size(); uu++) {
           Stage stage = stageArrayList.get(uu);
            Date date = stage.getDate();
            if ( uu > 0 && (stage.getOldValue() < stageArrayList.get(uu - 1).getOldValue() )) {
            	nAfterRework++;
          	}                       
            }
        g2d.fillRect(260, 0, 470, 70);        
        g2d.setFill(Color.BROWN);
        g2d.fillRect(260, 0, 470, 20);
        g2d.setFill(Color.BLUEVIOLET);
        g2d.fillRect(260, 50, 470, 20);
        g2d.setStroke(Color.BLACK);
        g2d.setFont(new Font("verdana", 14));
        g2d.setStroke(Color.WHITESMOKE);
        g2d.strokeText("ReWorks", 352, 15);
        g2d.strokeText("After Award", 405, 65);
        g2d.strokeText("Before Award", 270, 65);
        g2d.setStroke(Color.BLACK);
        String oo=Integer.toString(nBeforeRework);
        String pp=Integer.toString(nAfterRework);
        g2d.strokeText(pp, 435, 40);
        g2d.strokeText(oo, 315, 40);    
            
            
    }

    private Project getProjectFromProject(Project project) {
        for (int i = 0; i < projects.size(); i++) {
            if (projects.get(i).getNodeID().equals(project.getNodeID())) {
                return projects.get(i);
            }
        }
        return null;
    }

    private Color generateRandomColor() {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);

        Color color = Color.rgb(r, g, b);

        return color;
    }

    public static void readXLS() throws IOException, ParseException {
        projects = new ArrayList<>();
        FileInputStream projectFilePath = new FileInputStream("Projects.xls");
        HSSFWorkbook xss = new HSSFWorkbook(projectFilePath);
        Sheet sheet = xss.getSheet("Sheet1");
        DataFormatter format = new DataFormatter();

        for (Row row : sheet) {
            Project project = new Project();
            if (row.getRowNum() > 0) {
                for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                    // If the cell is missing from the file, generate a blank one
                    // (Works by specifying a MissingCellPolicy)
                    Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    // Print the cell for debugging
                    switch (cn) {
                        case 0:
                            project.setNodeID(cell.toString());
                            break;
                        case 1:
                            project.setCusID(cell.toString());
                            break;
                        case 2:
                            project.setStage(Integer.parseInt(format.formatCellValue(cell)));
                            break;
                        case 3:
                            project.setStartDate(cell.getDateCellValue());
                            break;
                        case 4:
                            project.setEndDate(cell.getDateCellValue());
                            break;
                        default:
                            ;
                    }
                }
                projects.add(project);
            }
        }

        //read stage file
        FileInputStream stageFile = new FileInputStream("Stages.xls");
        HSSFWorkbook xssw = new HSSFWorkbook(stageFile);
        Sheet sheet1 = xssw.getSheet("Sheet1");
        DataFormatter format1 = new DataFormatter();
        ArrayList<Stage> stages = new ArrayList<>();
        for (Row row : sheet1) {
            Stage stage = new Stage();
            if (row.getRowNum() > 0) {
                for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                    // If the cell is missing from the file, generate a blank one
                    // (Works by specifying a MissingCellPolicy)
                    Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    // Print the cell for debugging
                    switch (cn) {
                        case 0:
                            stage.setObjectValue(cell.toString());
                            break;
                        case 1:
                            stage.setDocumentNumber(cell.toString());
                            break;
                        case 5:
                            stage.setOldValue((int) cell.getNumericCellValue());
                            break;
                        case 6:
                            stage.setNewValue((int) cell.getNumericCellValue());
                            break;
                        default:
                            ;
                    }
                }
                stages.add(stage);
            }
        }

        FileInputStream stageFile1 = new FileInputStream("Stages_Detailed.xls");
        HSSFWorkbook xss1 = new HSSFWorkbook(stageFile1);
        Sheet sheet2 = xss1.getSheet("Sheet1");
        for (Row row : sheet2) {
            if (row.getRowNum() > 0) {
                Date teDate = null;
                String objectValue = "", documentNumber = "";
                for (int cn = 0; cn < row.getLastCellNum(); cn++) {
                    // If the cell is missing from the file, generate a blank one
                    // (Works by specifying a MissingCellPolicy)
                    Cell cell = row.getCell(cn, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    // Print the cell for debugging
                    switch (cn) {
                        case 0:
                            objectValue = cell.toString();
                            break;
                        case 1:
                            documentNumber = cell.toString();
                            break;
                        case 2:
                            teDate = cell.getDateCellValue();
                            break;
                        default:;
                    }
                }
                for (int i = 0; i < stages.size(); i++) {
                    Stage stage = stages.get(i);
                    if (stage.getObjectValue().equals(objectValue) && stage.getDocumentNumber().equals(documentNumber)) {
                        stage.setDate(teDate);

                    }

                }
            }

        }

        for (int i = 0; i < projects.size(); i++) {
            ArrayList<Stage> temp = new ArrayList<>();
            for (int j = 0; j < stages.size(); j++) {
                if (projects.get(i).getNodeID().equals(stages.get(j).getObjectValue())) {
                    temp.add(stages.get(j));

                }
            }

            projects.get(i).setStages(temp);

        }
    }

    private String getNextMonth(String month) {
        String temp = "";
        String year = "";
        int ite = 0;
        for (int i = 0; i < months.length; i++) {
            if (month.contains(months[i])) {
                year = month.split(" ")[1];
                ite = i;
            }
        }
        if (ite == 11) {
            return months[0] + " " + year;
        } else {
            return months[ite + 1] + " " + year;
        }
    }

    private class ChangeListenerImpl implements ChangeListener {

        public ChangeListenerImpl() {
        }

        @Override
        public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
            if (newValue != null) {
                Project project = (Project) newValue;
                text.setText("ProjectID:  " + project.getCusID());
                GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
                showProjectView(graphicsContext, project);

            }
        }
    }
}
