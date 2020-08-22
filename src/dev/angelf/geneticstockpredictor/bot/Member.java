package dev.angelf.geneticstockpredictor.bot;

import java.util.Random;

public class Member {

    double money;
    double boughtPrice;

    // Genes
    float stopLoss;
    float observationLength;
    float holdTime;

    public Member() {

    }

    public void initialize(){
        Random random = new Random();
        stopLoss = random.nextFloat();
        observationLength = random.nextFloat();
        holdTime = random.nextFloat();
    }

    public void mutate(){
        Random random = new Random();
        stopLoss = stopLoss * random.nextFloat();
        observationLength = observationLength * random.nextFloat();
        holdTime = holdTime * random.nextFloat();
    }

    public void merge(Member member){
        Random random = new Random();
        int i = random.nextInt(3);
        switch (i){
            case 0:
                stopLoss = member.stopLoss;
                break;
            case 1:
                observationLength = member.observationLength;
                break;
            case 2:
                holdTime = member.holdTime;
                break;
        }
    }

    public void run(double[] prices){
        double currentPrice = prices[prices.length - 1];
        int currentTime = prices.length - 1;
        float observationTime = currentTime * observationLength;
        double average = 0;
        double averageRange = 0;

        for (int i = 0; i > observationTime; i++) {

        }

        if(currentPrice - boughtPrice > boughtPrice * stopLoss){
            // sell at a loss
        }

        if (false) {
            // sell at a gain
        }

        if(false){
            // buy if lower than average
        }

    }
}
