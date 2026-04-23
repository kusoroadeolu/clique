package io.github.kusoroadeolu.clique;

import io.github.kusoroadeolu.clique.configuration.ProgressBarConfiguration;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        var config = ProgressBarConfiguration.builder()
                .length(50)
                .complete('=')
                .incomplete('-')
                .tickPerUnit(5)
                .styleRange(0, 49, "[red]:bar :percent% :units/:total-units[/]")
                .styleRange(50, 99, "[yellow]:bar :percent% :units/:total-units[/]")
                .styleRange(100, 100, "[green]:bar :percent% :units/:total-units[/]")
                .build();
        var bar = Clique.progressBar(100, config);
        for (int i = 0; i < 100; ++i){
            bar.tick(1);
            Thread.sleep(100);
        }
    }
}
