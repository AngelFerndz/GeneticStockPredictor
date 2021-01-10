package dev.angelf.geneticstockpredictor;

import dev.angelf.geneticstockpredictor.bot.Bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Simulation {
    private ArrayList<Bot> members;
    private ArrayList<Bot> modify;
    private Scanner scanner;

    public Simulation (){
        scanner = new Scanner(System.in);
    }

    public void initialize(int amount, double cash) {
        printHead(amount, cash);
        double money = cash / amount;
        generateMembers(amount, money);
    }

    public void captureInputs(){
        System.out.print("Enter Money Amount: ");
        double cash = scanner.nextInt();
        System.out.print("Enter Amount of Bots: ");
        int amount = scanner.nextInt();
        System.out.print("Enter Amount of Training Cycles: ");
        int trainingCycles = scanner.nextInt();
    }

    public void printHead(int amount, double cash){
        System.out.println("Genetic Stock Predictor");
        System.out.println("By angelf.dev | Angel Fernandez");
        System.out.println("Generating [" + amount + "] Members." );
        System.out.println("Initial Investment: " + cash);
    }

    public void generateMembers(int amount, double money){
        members = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Bot member = new Bot(i, money);
            member.initialize();
            members.add(member);
        }
    }

    public void refreshMembers(double money){
        for(Bot m : members){
            m.money = money;
            m.shares = 0;
        }
    }

    public double[] generatePrices(double initialPrice, int length){
        Random random = new Random();
        double[] prices = new double[length];
        double currentPrice = initialPrice;

        // Generate values
        for(int i = 0; i < prices.length; i++){
            if(currentPrice < 1){
                currentPrice += random.nextFloat();
            } else if (currentPrice > 300){
                currentPrice -= random.nextFloat();
            } else {
                if(random.nextInt(100) < 54){
                    currentPrice  += random.nextFloat();
                } else {
                    currentPrice -=  random.nextFloat();
                }
            }
            prices[i] = currentPrice;
        }

        return prices;
    }

    public void runPopulation(double[] prices){
        for(int i = 0; i < prices.length - 1; i++){
            double[] temp = Arrays.copyOfRange(prices, 0, i + 1);
            for(Bot m : members){
                m.run(temp);
            }
        }
    }

    public void testFitness(double currentPrice, double cash){
        double counter = 0;

        for(Bot m : members) {
            counter += m.getTotalValue(currentPrice);
        }

        double average = counter / members.size();
        modify = new ArrayList<>();
        double best = 0;
        double worst = 999999999;

        for(Bot m : members) {
            double totalValue = m.getTotalValue(currentPrice);

            if(totalValue> average){
                if(totalValue > best){
                    best = totalValue;
                }
                //System.out.print(m.id + ", ");
            } else {
                if(totalValue < worst){
                    worst = totalValue;
                }
                modify.add(m);
            }
        }

        System.out.println();
        System.out.println("Modified      : " + modify.size() + "/" + members.size());
        System.out.println("Average Value : " + average);
        System.out.println("Total Value   : " + counter);
        System.out.println("Best Member   : " + best);
        System.out.println("Worst Member  : " + worst);
        System.out.println("Gains         : " + (counter - cash));
        System.out.println("10% Taxes     : " + (counter - cash) * .1);
    }

    public void repopulate() {
        Random random = new Random();

        if (random.nextBoolean()) {
            for(Bot m : modify){
                m.mutate();
            }
        } else {
            for (Bot a : modify){
                for (Bot b : members){
                    a.merge(b);
                }
            }
        }

    }
}
