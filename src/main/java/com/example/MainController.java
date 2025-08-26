package com.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.example.model.Process;
import com.example.scheduling.FcfsScheduler;
import com.example.scheduling.MLFQScheduler;
import com.example.scheduling.RoundRobinScheduler;
import com.example.scheduling.SRTScheduler;
import com.example.scheduling.SjfScheduler;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class MainController {

    @FXML
    private TextField arrivalTimeField;
    @FXML
    private TextField burstTimeField;
    @FXML
    private ComboBox<String> algorithmComboBox;
    @FXML
    private TextField quantumField;
    @FXML
    private TableView<Process> processTable;
    @FXML
    private TableColumn<Process, String> nameColumn;
    @FXML
    private TableColumn<Process, Number> arrivalColumn;
    @FXML
    private TableColumn<Process, Number> burstColumn;
    @FXML
    private TableColumn<Process, Number> finishTimeColumn;
    @FXML
    private TableColumn<Process, Number> turnaroundTimeColumn;
    @FXML
    private TableColumn<Process, Number> waitingTimeColumn;
    @FXML
    private TableColumn<Process, Number> responseTimeColumn;
    @FXML
    private HBox ganttChartBox;
    @FXML
    private Label avgWaitingTimeLabel;
    @FXML
    private Label avgTurnaroundTimeLabel;
    @FXML
    private Label avgResponseTimeLabel;

    private final ObservableList<Process> processList = FXCollections.observableArrayList();
    private TextField quantumField2;
    private TextField agingField;
    private Button exportButton;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        arrivalColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getArrivalTime()));
        burstColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBurstTime()));
        finishTimeColumn
                .setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getFinishTime()));
        turnaroundTimeColumn
                .setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTurnaroundTime()));
        waitingTimeColumn
                .setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getWaitingTime()));
        responseTimeColumn
                .setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getResponseTime()));

        processTable.setItems(processList);
        processTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        algorithmComboBox.getItems().addAll("FCFS", "SJF (Non-Preemptive)", "Round Robin", "SRT (Preemptive)", "Multilevel Feedback Queue (MLFQ)");

        quantumField2 = new TextField();
        agingField = new TextField();
        quantumField.getParent().setVisible(false);
        quantumField.getParent().setManaged(false);

        algorithmComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            boolean isRoundRobin = "Round Robin".equals(newValue);
            boolean isMLFQ = "Multilevel Feedback Queue (MLFQ)".equals(newValue);
            Pane quantumPane = (Pane) quantumField.getParent();

            if (isRoundRobin) {
                quantumField.setPromptText("Time Quantum");
                if (quantumPane.getChildren().contains(quantumField2)) {
                    quantumPane.getChildren().remove(quantumField2);
                }
                if (quantumPane.getChildren().contains(agingField)) {
                    quantumPane.getChildren().remove(agingField);
                }
            } else if (isMLFQ) {
                quantumField.setPromptText("Queue 0 (Priority: Highest) - RR");
                quantumField2.setPromptText("Queue 1 (Priority: Mediuim) - RR");
                agingField.setPromptText("Queue 2 (Priority: Lowest) - FCFS");
                if (!quantumPane.getChildren().contains(quantumField2)) {
                    quantumPane.getChildren().add(quantumField2);
                }
                if (!quantumPane.getChildren().contains(agingField)) {
                    quantumPane.getChildren().add(agingField);
                }
            }

            quantumPane.setVisible(isRoundRobin || isMLFQ);
            quantumPane.setManaged(isRoundRobin || isMLFQ);
        });

        exportButton = new Button("Export to CSV");
        exportButton.getStyleClass().add("export-button");
        exportButton.getStyleClass().add("export-button"); 
        exportButton.setOnAction(e -> exportToCSV());
        Pane parent = (Pane) avgWaitingTimeLabel.getParent();
        if (parent != null && !parent.getChildren().contains(exportButton)) {
            parent.getChildren().add(exportButton);
        }

        // Wrap Gantt chart in a ScrollPane to prevent it from breaking the layout
        Pane ganttParent = (Pane) ganttChartBox.getParent();
        if (ganttParent != null) {
            int ganttChartIndex = ganttParent.getChildren().indexOf(ganttChartBox);
            if (ganttChartIndex != -1) {
                ganttParent.getChildren().remove(ganttChartIndex);
                ScrollPane ganttScrollPane = new ScrollPane(ganttChartBox);
                ganttScrollPane.setFitToHeight(true);
                ganttParent.getChildren().add(ganttChartIndex, ganttScrollPane);
            }
        }
    }

    @FXML
    private void solve() {
        String[] arrivalTimesStr = arrivalTimeField.getText().trim().split("\\s+");
        String[] burstTimesStr = burstTimeField.getText().trim().split("\\s+");

        if (arrivalTimesStr.length != burstTimesStr.length || arrivalTimesStr[0].isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "The number of arrival times must match the number of burst times.");
            return;
        }

        processList.clear();
        try {
            for (int i = 0; i < arrivalTimesStr.length; i++) {
                String name = "P" + (i + 1);
                int arrival = Integer.parseInt(arrivalTimesStr[i]);
                int burst = Integer.parseInt(burstTimesStr[i]);
                processList.add(new Process(name, arrival, burst));
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Please enter valid numbers for all times.");
            return;
        }

        String selectedAlgorithm = algorithmComboBox.getSelectionModel().getSelectedItem();
        if (selectedAlgorithm == null) {
            showAlert(Alert.AlertType.WARNING, "Please select an algorithm.");
            return;
        }

        List<Process> processesToSchedule = new ArrayList<>(processList);

        switch (selectedAlgorithm) {
            case "FCFS":
                FcfsScheduler.schedule(processesToSchedule);
                break;
            case "SJF (Non-Preemptive)":
                SjfScheduler.schedule(processesToSchedule);
                break;
            case "Round Robin":
                try {
                    int quantum = Integer.parseInt(quantumField.getText());
                    if (quantum <= 0) {
                        showAlert(Alert.AlertType.ERROR, "Time quantum must be a positive integer.");
                        return;
                    }
                    RoundRobinScheduler.schedule(processesToSchedule, quantum);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Please enter a valid number for the time quantum.");
                    return;
                }
                break;
            case "SRT (Preemptive)":
                SRTScheduler.schedule(processesToSchedule);
                break;
            case "Multilevel Feedback Queue (MLFQ)":
                try {
                    if (quantumField.getText().trim().isEmpty() || quantumField2.getText().trim().isEmpty() || agingField.getText().trim().isEmpty()) {
                        showAlert(Alert.AlertType.ERROR, "For MLFQ, please enter values for both time quantums and the aging threshold.");
                        return;
                    }
                    int quantum1 = Integer.parseInt(quantumField.getText().trim());
                    int quantum2 = Integer.parseInt(quantumField2.getText().trim());
                    int agingThreshold = Integer.parseInt(agingField.getText().trim());
                    if (quantum1 <= 0 || quantum2 <= 0 || agingThreshold <= 0) {
                        showAlert(Alert.AlertType.ERROR, "Time quantums and aging threshold must be positive integers.");
                        return;
                    }
                    MLFQScheduler.schedule(processesToSchedule, quantum1, quantum2, agingThreshold);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Please enter valid numbers for the time quantums and aging threshold.");
                    return;
                }
                break;
        }

        updateTableAndAverages();
        updateGanttChart();
    }

    private void exportToCSV() {
        if (processList.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No data to export.");
            return;
        }

        try (FileWriter writer = new FileWriter("process_results.csv")) {
            writer.append("Name,Arrival Time,Burst Time,Finish Time,Turnaround Time,Waiting Time,Response Time\n");

            for (Process p : processList) {
                writer.append(p.getName()).append(",")
                      .append(String.valueOf(p.getArrivalTime())).append(",")
                      .append(String.valueOf(p.getBurstTime())).append(",")
                      .append(String.valueOf(p.getFinishTime())).append(",")
                      .append(String.valueOf(p.getTurnaroundTime())).append(",")
                      .append(String.valueOf(p.getWaitingTime())).append(",")
                      .append(String.valueOf(p.getResponseTime())).append("\n");
            }
            showAlert(Alert.AlertType.INFORMATION, "Data exported successfully to process_results.csv");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error exporting data to CSV file.");
            e.printStackTrace();
        }
    }

    private void updateTableAndAverages() {
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;
        double totalResponseTime = 0;

        if (processList.isEmpty())
            return;

        for (Process p : processList) {
            totalWaitingTime += p.getWaitingTime();
            totalTurnaroundTime += p.getTurnaroundTime();
            totalResponseTime += p.getResponseTime();
        }

        avgWaitingTimeLabel.setText(String.format("Average Waiting Time: %.2f", totalWaitingTime / processList.size()));
        avgTurnaroundTimeLabel
                .setText(String.format("Average Turnaround Time: %.2f", totalTurnaroundTime / processList.size()));
        avgResponseTimeLabel
                .setText(String.format("Average Response Time: %.2f", totalResponseTime / processList.size()));

        processTable.refresh();
    }

    private void updateGanttChart() {
        ganttChartBox.getChildren().clear();
        double scale = 10.0;
        List<Process> sortedByFinish = new ArrayList<>(processList);
        sortedByFinish.sort(Comparator.comparingInt(Process::getFinishTime));
        for (Process p : sortedByFinish) {
            Rectangle block = new Rectangle(p.getBurstTime() * scale, 40);
            block.setFill(Color.hsb((p.getName().hashCode() * 100) % 360, 0.8, 0.9));
            block.setStroke(Color.BLACK);
            Label label = new Label(p.getName());
            label.setFont(new Font(14));

            StackPane stack = new StackPane(block, label);
            ganttChartBox.getChildren().add(stack);

            Label timeLabel = new Label(String.valueOf(p.getFinishTime()));
            ganttChartBox.getChildren().add(timeLabel);
        }
    }

    @FXML
    private void resetApplication() {
        processList.clear();
        ganttChartBox.getChildren().clear();
        arrivalTimeField.clear();
        burstTimeField.clear();
        quantumField.clear();
        if (quantumField2 != null) {
            quantumField2.clear();
        }
        if (agingField != null) {
            agingField.clear();
        }
        avgWaitingTimeLabel.setText("Average Waiting Time: ");
        avgTurnaroundTimeLabel.setText("Average Turnaround Time: ");
        avgResponseTimeLabel.setText("Average Response Time: ");
        algorithmComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.showAndWait();
    }
}
