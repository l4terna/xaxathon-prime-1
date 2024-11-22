package com.laterna.xaxaxa.task;

import com.laterna.xaxaxa.service.SportsDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SportsPDFTask {
    private final SportsDataService sportsDataService;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void runTask() {
        sportsDataService.saveData();
    }
}
