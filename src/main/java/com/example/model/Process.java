package com.example.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Process {
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty arrivalTime;
    private final SimpleIntegerProperty burstTime;
    private final SimpleIntegerProperty finishTime;
    private final SimpleIntegerProperty turnaroundTime;
    private final SimpleIntegerProperty waitingTime;
    private int remainingBurstTime; // Added for Round Robin

    public Process(String name, int arrivalTime, int burstTime) {
        this.name = new SimpleStringProperty(name);
        this.arrivalTime = new SimpleIntegerProperty(arrivalTime);
        this.burstTime = new SimpleIntegerProperty(burstTime);
        this.finishTime = new SimpleIntegerProperty(0);
        this.turnaroundTime = new SimpleIntegerProperty(0);
        this.waitingTime = new SimpleIntegerProperty(0);
        this.remainingBurstTime = burstTime; // Initialize remaining time
    }

    // Getters
    public String getName() { return name.get(); }
    public int getArrivalTime() { return arrivalTime.get(); }
    public int getBurstTime() { return burstTime.get(); }
    public int getFinishTime() { return finishTime.get(); }
    public int getTurnaroundTime() { return turnaroundTime.get(); }
    public int getWaitingTime() { return waitingTime.get(); }
    public int getRemainingBurstTime() { return remainingBurstTime; }

    // Setters
    public void setFinishTime(int finishTime) { this.finishTime.set(finishTime); }
    public void setTurnaroundTime(int turnaroundTime) { this.turnaroundTime.set(turnaroundTime); }
    public void setWaitingTime(int waitingTime) { this.waitingTime.set(waitingTime); }
    public void setRemainingBurstTime(int remainingBurstTime) { this.remainingBurstTime = remainingBurstTime; }
}