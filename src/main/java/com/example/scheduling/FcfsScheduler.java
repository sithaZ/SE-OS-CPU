package com.example.scheduling;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.example.model.Process;

public class FcfsScheduler {
    public static void schedule(List<Process> processes) {
        Collections.sort(processes, Comparator.comparingInt(Process::getArrivalTime));
        int currentTime = 0;
        for (Process p : processes) {
            if (currentTime < p.getArrivalTime()) {
                currentTime = p.getArrivalTime();
            }
            // This block correctly sets the response time
            if (!p.hasStarted()) {
                p.setResponseTime(currentTime - p.getArrivalTime());
                p.setHasStarted(true);
            }
            currentTime += p.getBurstTime();
            p.setFinishTime(currentTime);
            p.setTurnaroundTime(p.getFinishTime() - p.getArrivalTime());
            p.setWaitingTime(p.getTurnaroundTime() - p.getBurstTime());
        }
    }
}