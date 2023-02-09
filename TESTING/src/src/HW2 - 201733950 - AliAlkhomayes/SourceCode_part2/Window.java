package fapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class Window extends BorderPane {
    //make TreeView of folder
    private TreeView<File> treeView;
    private TextArea textArea;
    private Button selectFolder;

    private VBox vBox;

    public Window() {
        //initialize TreeView
        treeView = new TreeView<>();
        textArea = new TextArea();
        textArea.setEditable(false);
        selectFolder = new Button("Select Folder");

        vBox = new VBox();
        vBox.getChildren().addAll(treeView, selectFolder);
        vBox.setAlignment(Pos.CENTER_RIGHT);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));

        this.setLeft(vBox);
        this.setCenter(textArea);
        this.setPadding(new Insets(15));

        selectFolder.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select Folder");
            String inputDirectoryLocation = directoryChooser.showDialog(this.getScene().getWindow()).getAbsolutePath();

            File file = new File(inputDirectoryLocation);
            if (file.exists()) {
                TreeItem<File> root = new TreeItem<>(file);
                root.setExpanded(true);
                treeView.setRoot(root);
                treeView.setShowRoot(false);
                treeView.setCellFactory(ex -> new TreeCell<>() {
                    @Override
                    protected void updateItem(File item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                });
                treeView.getSelectionModel().selectedItemProperty().
                        addListener((observable, oldValue, newValue) -> {
                            if (newValue != null) {
                                File file1 = newValue.getValue();
                                textArea.clear();
                                addChildren(file1, textArea);
                            }
                        });
                addChildren(file, root);
            }
        });
    }
    //print all inner folder and file names in textArea
    private void addChildren(File file, TextArea textArea) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    long size = getFileSize(f);
                    Directory directory = new Directory(f.getName(), size);
                    textArea.appendText(directory.showDetails());
                    addChildren(f, textArea);
                } else {
                    long size = getFileSize(f);
                    Files files1 = new Files(f.getName(), size, getFileExtension(f));
                    textArea.appendText(files1.showDetails());
                }
            }
        }

    }

    private String getFileExtension(File f) {
        String name = f.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    //write a method to get file size
    private long getFileSize(File file) {
        long size = 0;
        if (file.isFile()) {
            size = file.length();
        } else {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    size += getFileSize(f);
                }
            }
        }
        size = size / 1024;
        return size;
    }

    private void addChildren(File file, TreeItem<File> root) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File file1 : files) {
                TreeItem<File> treeItem = new TreeItem<>(file1);
                root.getChildren().add(treeItem);
                if (file1.isDirectory()) {
                    addChildren(file1, treeItem);
                }
            }
        }
    }

}
