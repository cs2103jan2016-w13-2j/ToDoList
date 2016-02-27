package todolist.view;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import todolist.MainApp;
import todolist.model.Command;
import todolist.model.CommandHandlerStub;
import todolist.model.Feedback;
import todolist.model.Task;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class MainViewController {

    private static final double PROPORTION_NUMBER_COLUMN = 0.05;
    private static final double PROPORTION_TASK_TITLE_COLUMN = 0.30;
    private static final double PROPORTION_START_TIME_COLUMN = 0.20;
    private static final double PROPORTION_END_TIME_COLUMN = 0.20;
    private static final double PROPORTION_CATEGORY_COLUMN = 0.20;
    private static final double PROPORTION_PRIORITY_COLUMN = 0.05;

    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, String> numberColumn;

    @FXML
    private TableColumn<Task, String> taskTitleColumn;

    @FXML
    private TableColumn<Task, String> startTimeColumn;

    @FXML
    private TableColumn<Task, String> endTimeColumn;

    @FXML
    private TableColumn<Task, String> categoryColumn;

    @FXML
    private TableColumn<Task, String> priorityColumn;

    private MainApp mainApplication;

    public MainViewController() {
        // ...
    }

    public void setCommandLineCallback(TextField commandField) {
        // Set Callback for TextField
        EventHandler<ActionEvent> commandHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String commandString = commandField.getText();
                Command command = new Command(commandString);
                System.out.println(command.getCommand());

                // Pass Command Line input for processing
                CommandHandlerStub handler = new CommandHandlerStub(mainApplication);
                Feedback feedback = handler.execute(command);
                System.out.println(feedback.getStatus());
            }
        };

        commandField.setOnAction(commandHandler);
    }

    public void setMainApp(MainApp mainApp) {
        mainApplication = mainApp;
        taskTable.setItems(mainApplication.getDisplayTasks());
    }

    @FXML
    private void initialize() {

        setNumberColumn();
        setTitleColumn();
        setStartTimeColumn();
        setEndTimeColumn();
        setCategoryColumn();
        setPriorityColumn();

        setResizing();
    }

    private void setNumberColumn() {
        numberColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Task, String> param) {
                        String cellValue = getNumberValue(param);
                        return new ReadOnlyObjectWrapper<String>(cellValue);
                    }

                    private String getNumberValue(CellDataFeatures<Task, String> param) {
                        int number = taskTable.getItems().indexOf(param.getValue()) + 1;
                        return Integer.toString(number);
                    }
                });
    }

    private void setTitleColumn() {
        taskTitleColumn.setCellValueFactory(cellData -> cellData.getValue().taskTitleProperty());
    }

    private void setStartTimeColumn() {
        startTimeColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Task, String> param) {
                        String shortDateTime = getDateTimeValue(param);

                        return new SimpleStringProperty(shortDateTime);
                    }

                    private String getDateTimeValue(CellDataFeatures<Task, String> param) {
                        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM,
                                FormatStyle.SHORT);
                        String dateTime = param.getValue().getStartTime().format(dateTimeFormat);
                        return formatDateTime(dateTime);
                    }
                });
    }

    private void setEndTimeColumn() {
        endTimeColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Task, String> param) {
                        String shortDateTime = getDateTimeValue(param);

                        return new SimpleStringProperty(shortDateTime);
                    }

                    private String getDateTimeValue(CellDataFeatures<Task, String> param) {
                        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM,
                                FormatStyle.SHORT);
                        String dateTime = param.getValue().getEndTime().format(dateTimeFormat);
                        return formatDateTime(dateTime);
                    }
                });
    }

    private void setCategoryColumn() {
        categoryColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Task, String> param) {
                        String cellValue = getCategoryValue(param);
                        return new SimpleStringProperty(cellValue);
                    }

                    private String getCategoryValue(CellDataFeatures<Task, String> param) {
                        return param.getValue().getCategory().getCategoryName();
                    }
                });
    }

    private void setPriorityColumn() {
        priorityColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>() {

                    @Override
                    public ObservableValue<String> call(CellDataFeatures<Task, String> param) {
                        String cellValue = getPriorityValue(param);
                        return new SimpleStringProperty(cellValue);
                    }

                    private String getPriorityValue(CellDataFeatures<Task, String> param) {
                        int priority = param.getValue().getPriority().getPriorityLevel();
                        return Integer.toString(priority);
                    }
                });
    }

    private void setResizing() {
        DoubleBinding numberBinder = taskTable.widthProperty().multiply(PROPORTION_NUMBER_COLUMN);
        numberColumn.prefWidthProperty().bind(numberBinder);

        DoubleBinding titleBinder = taskTable.widthProperty().multiply(PROPORTION_TASK_TITLE_COLUMN);
        taskTitleColumn.prefWidthProperty().bind(titleBinder);

        DoubleBinding startTimeBinder = taskTable.widthProperty().multiply(PROPORTION_START_TIME_COLUMN);
        startTimeColumn.prefWidthProperty().bind(startTimeBinder);

        DoubleBinding endTimeBinder = taskTable.widthProperty().multiply(PROPORTION_END_TIME_COLUMN);
        endTimeColumn.prefWidthProperty().bind(endTimeBinder);

        DoubleBinding categoryBinder = taskTable.widthProperty().multiply(PROPORTION_CATEGORY_COLUMN);
        categoryColumn.prefWidthProperty().bind(categoryBinder);

        DoubleBinding priorityBinder = taskTable.widthProperty().multiply(PROPORTION_PRIORITY_COLUMN);
        priorityColumn.prefWidthProperty().bind(priorityBinder);

        taskTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private String formatDateTime(String dateTime) {
        String[] dateTimeSplit = dateTime.split(" ");
        dateTimeSplit[1] = dateTimeSplit[1].replace(",", "");
        String shortDateTime = dateTimeSplit[0] + "-" + dateTimeSplit[1] + " " + dateTimeSplit[3] + dateTimeSplit[4];
        return shortDateTime;
    }
}
