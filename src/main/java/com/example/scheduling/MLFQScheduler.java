package com.example.scheduling;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.example.model.Process;

public class MLFQScheduler {

    public static void schedule(List<Process> processes, int timeQuantum1, int timeQuantum2, int agingThreshold) {
        Queue<Process> q1 = new LinkedList<>();
        Queue<Process> q2 = new LinkedList<>();
        Queue<Process> q3 = new LinkedList<>();

        List<Process> completedProcesses = new ArrayList<>();
        int currentTime = 0;

        for (Process p : processes) {
            p.setRemainingBurstTime(p.getBurstTime());
            p.setLastExecutionTime(p.getArrivalTime());
            p.setHasStarted(false);
        }

        List<Process> processList = new ArrayList<>(processes);
        processList.sort((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));

        int processIndex = 0;
        while (completedProcesses.size() < processes.size()) {
            while (processIndex < processList.size() && processList.get(processIndex).getArrivalTime() <= currentTime) {
                q1.add(processList.get(processIndex));
                processIndex++;
            }

            Process currentProcess = null;
            int timeQuantum = 0;
            int currentQueue = 0;

            if (!q1.isEmpty()) {
                currentProcess = q1.poll();
                timeQuantum = timeQuantum1;
                currentQueue = 1;
            } else if (!q2.isEmpty()) {
                currentProcess = q2.poll();
                timeQuantum = timeQuantum2;
                currentQueue = 2;
            } else if (!q3.isEmpty()) {
                currentProcess = q3.poll();
                currentQueue = 3;
            } else {
                if (processIndex < processList.size()) {
                    currentTime = processList.get(processIndex).getArrivalTime();
                } else {
                    break;
                }
                continue;
            }

            if (!currentProcess.hasStarted()) {
                currentProcess.setResponseTime(currentTime - currentProcess.getArrivalTime());
                currentProcess.setHasStarted(true);
            }

            int executeTime = currentProcess.getRemainingBurstTime();
            if (currentQueue < 3) { // For Q1 and Q2, apply time quantum
                executeTime = Math.min(executeTime, timeQuantum);
            }

            currentTime += executeTime;
            currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - executeTime);
            currentProcess.setLastExecutionTime(currentTime);

            while (processIndex < processList.size() && processList.get(processIndex).getArrivalTime() <= currentTime) {
                q1.add(processList.get(processIndex));
                processIndex++;
            }

            if (currentProcess.getRemainingBurstTime() <= 0) {
                int finishTime = currentTime;
                int turnaroundTime = finishTime - currentProcess.getArrivalTime();
                int waitingTime = turnaroundTime - currentProcess.getBurstTime();

                currentProcess.setFinishTime(finishTime);
                currentProcess.setTurnaroundTime(turnaroundTime);
                currentProcess.setWaitingTime(waitingTime);
                completedProcesses.add(currentProcess);
            } else {
                if (currentQueue == 1) {
                    q2.add(currentProcess);
                } else if (currentQueue == 2) {
                    q3.add(currentProcess);
                } else {
                    q3.add(currentProcess);
                }
            }

            promoteProcesses(q2, q1, currentTime, agingThreshold);
            promoteProcesses(q3, q2, currentTime, agingThreshold);
        }
    }

    private static void promoteProcesses(Queue<Process> fromQueue, Queue<Process> toQueue, int currentTime, int agingThreshold) {
        List<Process> toPromote = new ArrayList<>();
        for (Process p : fromQueue) {
            if (currentTime - p.getLastExecutionTime() > agingThreshold) {
                toPromote.add(p);
            }
        }
        for (Process p : toPromote) {
            fromQueue.remove(p);
            toQueue.add(p);
        }
    }
}