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
    private final SimpleIntegerProperty responseTime;
    private int remainingBurstTime;
    private int lastExecutionTime;
    private boolean hasStarted = false;

    public Process(String name, int arrivalTime, int burstTime) {
        this.name = new SimpleStringProperty(name);
        this.arrivalTime = new SimpleIntegerProperty(arrivalTime);
        this.burstTime = new SimpleIntegerProperty(burstTime);
        this.finishTime = new SimpleIntegerProperty(0);
        this.turnaroundTime = new SimpleIntegerProperty(0);
        this.waitingTime = new SimpleIntegerProperty(0);
        this.responseTime = new SimpleIntegerProperty(0);
        this.remainingBurstTime = burstTime;
        this.lastExecutionTime = 0;
    }

    // Getters
    public String getName() { return name.get(); }
    public int getArrivalTime() { return arrivalTime.get(); }
    public int getBurstTime() { return burstTime.get(); }
    public int getFinishTime() { return finishTime.get(); }
    public int getTurnaroundTime() { return turnaroundTime.get(); }
    public int getWaitingTime() { return waitingTime.get(); }
    public int getResponseTime() { return responseTime.get(); }
    public int getRemainingBurstTime() { return remainingBurstTime; }
    public int getLastExecutionTime() { return lastExecutionTime; }
    public boolean hasStarted() { return hasStarted; }

    // Setters
    public void setFinishTime(int finishTime) { this.finishTime.set(finishTime); }
    public void setTurnaroundTime(int turnaroundTime) { this.turnaroundTime.set(turnaroundTime); }
    public void setWaitingTime(int waitingTime) { this.waitingTime.set(waitingTime); }
    public void setResponseTime(int responseTime) { this.responseTime.set(responseTime); }
    public void setRemainingBurstTime(int remainingBurstTime) { this.remainingBurstTime = remainingBurstTime; }
    public void setLastExecutionTime(int currentTime) { this.lastExecutionTime = currentTime; }
    public void setHasStarted(boolean hasStarted) { this.hasStarted = hasStarted; }
}