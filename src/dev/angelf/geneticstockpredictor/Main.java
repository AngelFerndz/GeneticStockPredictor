package dev.angelf.geneticstockpredictor;

import dev.angelf.geneticstockpredictor.bot.Member;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Main {

    static ArrayList<Member> members;
    static ArrayList<Member> modify;

    public static void main(String[] args) {
        initialize(100, 5.00);

        for(int i = 0; i < 100; i++) {
            double[] prices = generatePrices(1.00, 100);
            double currentPrice = prices[prices.length - 1];
            runPopulation(prices);
            testFitness(currentPrice);
            repopulate();
            refreshMembers(5.00);
        }

    }

    private static void initialize(int amount, double money) {
        members = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Member member = new Member(i, money);
            member.initialize();
            members.add(member);
        }
    }

    private static void refreshMembers(double money){
        for(Member m : members){
            m.money = money;
            m.shares = 0;
        }
    }

    private static double[] generatePrices(double initialPrice, int length){
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

    private static void runPopulation(double[] prices){
        for(int i = 0; i < prices.length - 1; i++){
            double[] temp = Arrays.copyOfRange(prices, 0, i + 1);
            for(Member m : members){
                m.run(temp);
            }
        }
    }

    private static void testFitness(double currentPrice){
        double counter = 0;

        for(Member m : members) {
            counter += m.getTotalValue(currentPrice);
        }

        double average = counter / members.size();
        modify = new ArrayList<>();
        double best = 0;
        double worst = 999999999;

        for(Member m : members) {
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
        System.out.println("Best Member   : " + best);
        System.out.println("Worst Member  : " + worst);
    }

    private static void repopulate() {
        for(Member m : modify){
            m.mutate();
        }
    }

}

