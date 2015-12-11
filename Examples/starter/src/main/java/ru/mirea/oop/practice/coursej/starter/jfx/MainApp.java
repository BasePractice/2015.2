package ru.mirea.oop.practice.coursej.starter.jfx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.mirea.oop.practice.coursej.api.ext.BotsExtension;
import ru.mirea.oop.practice.coursej.api.ext.MazeExtension;
import ru.mirea.oop.practice.coursej.starter.jfx.students.Repository;
import ru.mirea.oop.practice.coursej.starter.jfx.students.RepositoryFile;
import ru.mirea.oop.practice.coursej.starter.jfx.students.Student;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public final class MainApp extends Application {
    private final ObservableList<MazeExtension> mazeExtensions = FXCollections.observableArrayList();
    private final ObservableList<BotsExtension> botsExtensions = FXCollections.observableArrayList();
    private final StackPane main = new StackPane();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Курсовые");
        final Scene scene = new Scene(main);
        primaryStage.setScene(scene);
        jbInit();
        updateCourses();
        primaryStage.show();
    }

    private void jbInit() throws FileNotFoundException {
        TabPane tabPane = new TabPane();
        final Tab tabStudents = new Tab("Студенты");
        tabStudents.setClosable(false);
        Repository<Student> repo = new RepositoryFile(new FileInputStream("C:\\GitHub\\2015.2\\.cources.csv"));
        final HashMap<String, String> headers = new HashMap<>();
        Student.createHeaders(headers);
        final TableView<Student> table = createTable(repo, headers);
//        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println(newValue);
//        });
        final ContextMenu contextMenu = new ContextMenu();
        final MenuItem view = new MenuItem("Проверить");
        view.setOnAction(event -> {
            final Student student = table.getSelectionModel().getSelectedItem();
            System.out.println(student);
        });
        contextMenu.getItems().add(view);
        table.setContextMenu(contextMenu);
        final StackPane content = new StackPane();
        content.getChildren().addAll(table);
        tabStudents.setContent(content);

        tabPane.getTabs().add(tabStudents);
//        final Tab tabMazes = new Tab("Лабиринт");
//        tabMazes.setClosable(false);
//        final Tab tabBots = new Tab("Бот");
//        tabBots.setClosable(false);
//        tabPane.getTabs().addAll(tabMazes, tabBots);
//        main.getChildren().clear();
//        final ListView<MazeExtension> listView = new ListView<>(mazeExtensions);
//        listView.setCellFactory(param -> new ListCell<MazeExtension>() {
//            @Override
//            protected void updateItem(MazeExtension item, boolean empty) {
//                super.updateItem(item, empty);
//                if (!empty) {
//                    setText(item.name());
//                    setTooltip(new Tooltip(item.description()));
//                }
//            }
//        });
//        SplitPane splitPane = new SplitPane();
//        splitPane.setOrientation(Orientation.HORIZONTAL);
//        splitPane.getItems().add(listView);
//        splitPane.getItems().add(new Label("LABEL"));
//
//        final VBox content = new VBox();
//        content.getChildren().addAll(splitPane);
//        tabMazes.setContent(content);
//        main.getChildren().add(tabPane);

        main.getChildren().add(tabPane);
    }

    private void updateCourses() {
        update(mazeExtensions, MazeExtension.class);
        update(botsExtensions, BotsExtension.class);
    }

    private static <E> void update(ObservableList<E> list, Class<E> klass) {
        list.clear();
        ServiceLoader<E> mazeLoader = ServiceLoader.load(klass);
        for (E ext : mazeLoader) {
            list.add(ext);
        }
    }

    private static <E> TableView<E> createTable(Repository<E> repository, Map<String, String> headers) {
        final TableView<E> tableView = new TableView<>(repository.list());
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            final TableColumn<E, Object> tableColumn = new TableColumn<>(entry.getValue());
            tableColumn.setCellValueFactory(new PropertyValueFactory<>(entry.getKey()));
            tableView.getColumns().add(tableColumn);
        }
        return tableView;
    }
}
