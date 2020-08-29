package dev.angelf.geneticstockpredictor.bot;

import java.util.Random;

public class Bot {

    public int id;
    public int shares;
    public double money;
    private double boughtPrice;

    // Genes
    float stopLoss;
    float observationLength;
    float hold;
    float avgRange;

    public Bot(int ID, double money) {
        shares = 0;
        this.id = ID;
        this.money = money;
    }

    public void initialize() {
        Random random = new Random();
        stopLoss = random.nextFloat();
        observationLength = random.nextFloat();
        hold = random.nextFloat();
        avgRange = random.nextFloat();
    }

    public void mutate() {
        Random random = new Random();
        int i = random.nextInt(4);
        switch (i) {
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

    public void merge(Bot member) {
        Random random = new Random();
        int i = random.nextInt(3);
        switch (i) {
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

    public void run(double[] prices) {
        double currentPrice = prices[prices.length - 1];
        double average = getAverage(prices);

        if(shares > 0) {
            if (currentPrice - boughtPrice > boughtPrice * stopLoss) {
                // sell at stop loss
                sell(currentPrice);
                //System.out.println("Sell: Stop Loss " + currentPrice + " / " + boughtPrice);
            }

            if (getAverageTop(average) < currentPrice && currentPrice > boughtPrice) {
                // sell at a above average
                sell(currentPrice);
                //System.out.println("Sell: Gains " + currentPrice + " / " + boughtPrice);
            }
        }

        if (getAverageBottom(average) < currentPrice) {
            buy(currentPrice);
            //System.out.println("Buy: " + currentPrice);
        }
    }

    public double getTotalValue(double currentPrice){
        double value = money;
        value += shares * currentPrice;
        return value;
    }

    private void sell(double currentPrice) {
        if (shares > 0) {
            money += currentPrice; // excluding 10% taxes
            shares--;
        }
    }

    private void buy(double currentPrice) {
        if (money > currentPrice) {
            boughtPrice = currentPrice;
            money -= currentPrice;
            shares++;
        }
    }

    private double getAverage(double[] prices) {
        int currentTime = prices.length - 1;
        float observationTime = currentTime * observationLength;

        // Calculate Average
        double counter = 0;
        for (int i = currentTime; i > 0; i--) {
            counter += prices[i];
        }

        return counter / observationTime;
    }

    private double getAverageTop(double average) {
        return (average * avgRange) + average;
    }

    private double getAverageBottom(double average) {
        return (average * avgRange) - average;
    }

    @Override
    public String toString() {
        return "Member{" + shares +
                ", " + money +
                ", " + boughtPrice +
                ", " + stopLoss +
                ", " + observationLength +
                ", " + hold +
                ", " + avgRange +
                '}';
    }

}
