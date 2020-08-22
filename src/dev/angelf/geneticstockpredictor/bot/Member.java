package dev.angelf.geneticstockpredictor.bot;

import java.util.Random;

public class Member {

    int shares;
    double money;
    double boughtPrice;

    // Genes
    float stopLoss;
    float observationLength;
    float hold;
    float avgRange;

    public Member(double Money) {
        shares = 0;
        money = Money;
    }

    public void initialize(){
        Random random = new Random();
        stopLoss = random.nextFloat();
        observationLength = random.nextFloat();
        hold = random.nextFloat();
        avgRange = random.nextFloat();
    }

    public void mutate(){
        Random random = new Random();
        int i = random.nextInt(4);
        switch (i){
            case 0:
                stopLoss = random.nextFloat();
                break;
            case 1:
                observationLength = random.nextFloat();
                break;
            case 2:
                hold = random.nextFloat();
                break;
            case 3:
                avgRange = random.nextFloat();
                break;
        }
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
                hold = member.hold;
                break;
        }
    }

    public void run(double[] prices){
        double currentPrice = prices[prices.length - 1];
        double average = getAverage(prices);

        if(currentPrice - boughtPrice > boughtPrice * stopLoss){
            // sell at stop loss
            sell(currentPrice);
        }

        if (getAverageTop(average) < currentPrice && currentPrice > boughtPrice) {
            // sell at a above average
            sell(currentPrice);
        }

        if(getAverageBottom(average) < currentPrice) {
            buy(currentPrice);
        }
    }

    private void sell(double currentPrice){
        while (shares > 0) {
            money += currentPrice;
            shares--;
        }
    }

    private void buy(double currentPrice){
        while (money > currentPrice) {
            money -= currentPrice;
            shares++;
        }
    }

    private double getAverage(double[] prices) {
        int currentTime = prices.length - 1;
        float observationTime = currentTime * observationLength;

        // Calculate Average
        double counter = 0;
        for (int i = currentTime; i > 0; i++) {
            counter += prices[i];
        }

        return counter / observationTime;
    }

    private double getAverageTop(double average){
        return (average * avgRange) + average;
    }

    private double getAverageBottom(double average){
        return (average * avgRange) - average;
    }

}
