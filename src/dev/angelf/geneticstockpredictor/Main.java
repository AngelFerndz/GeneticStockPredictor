package dev.angelf.geneticstockpredictor;

import dev.angelf.geneticstockpredictor.bot.Bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static ArrayList<Bot> members;
    static ArrayList<Bot> modify;
    static Scanner scanner;

    static int amount;
    static double cash;
    static int trainingCycles;

    /*
     * TODO try catch inputs and provide clear output
     *  Fix changes in price to reflect real world data
     * */

    public static void main(String[] args) {

        scanner = new Scanner(System.in);

        print_head();
        capture_inputs();
        initialize();
        run();

    }

    private static void print_head() {
        System.out.println("Genetic Stock Predictor");
        System.out.println("By angelf.dev | Angel Fernandez");
    }

    private static void capture_inputs() {
        System.out.print("Enter Money Amount: ");
        cash = scanner.nextInt();
        System.out.print("Enter Amount of Bots: ");
        amount = scanner.nextInt();
        System.out.print("Enter Amount of Training Cycles: ");
        trainingCycles = scanner.nextInt();
    }

    private static void initialize() {
        double money = cash / amount;
        generate_members(amount, money);
        System.out.println("Generating [" + amount + "] Members.");
        System.out.println("Initial Investment: " + cash);
    }

    private static void run() {
        double[] prices = generate_prices(1.00, 365);
        double currentPrice = prices[prices.length - 1];

        // Continuous run
        for (int i = 0; i < trainingCycles; i++) {
            run_population(prices);
            test_fitness(currentPrice, cash);
            repopulate();

            currentPrice = prices[prices.length - 1];
            prices = generate_prices(currentPrice, 100);
        }
    }

    private static void generate_members(int amount, double money) {
        members = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Bot member = new Bot(i, money);
            member.initialize();
            members.add(member);
        }
    }

    private static void refresh_members(double money) {
        for (Bot m : members) {
            m.money = money;
            m.shares = 0;
        }
    }

    private static double[] generate_prices(double initialPrice, int length) {
        Random random = new Random();
        double[] prices = new double[length];
        double currentPrice = initialPrice;

        // minimum and maximum prices
        double min = 1;
        double max = 300;

        // Generate values
        for (int i = 0; i < prices.length; i++) {
            if (currentPrice < min) {
                // avoid the price going beneath the min
                currentPrice += random.nextFloat();
            } else if (currentPrice > max) {
                // avoid the price going above the max
                currentPrice -= random.nextFloat();
            } else {
                // 54% upward trend based on historical data from S&P500, DOW & NASDAQ
                if (random.nextInt(100) < 54) {
                    currentPrice += random.nextFloat();
                } else {
                    currentPrice -= random.nextFloat();
                }
            }
            prices[i] = currentPrice;
        }

        return prices;
    }

    private static void run_population(double[] prices) {
        for (int i = 0; i < prices.length - 1; i++) {
            double[] temp = Arrays.copyOfRange(prices, 0, i + 1);
            for (Bot m : members) {
                m.run(temp);
            }
        }
    }

    private static void test_fitness(double currentPrice, double cash) {
        double counter = 0;

        for (Bot m : members) {
            counter += m.getTotalValue(currentPrice);
        }

        double average = counter / members.size();
        modify = new ArrayList<>();
        double best = 0;
        double worst = 999999999;

        for (Bot m : members) {
            double totalValue = m.getTotalValue(currentPrice);

            if (totalValue > average) {
                if (totalValue > best) {
                    best = totalValue;
                }
                //System.out.print(m.id + ", ");
            } else {
                if (totalValue < worst) {
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

    private static void repopulate() {
        Random random = new Random();

        if (random.nextBoolean()) {
            for (Bot m : modify) {
                m.mutate();
            }
        } else {
            for (Bot a : modify) {
                for (Bot b : members) {
                    a.merge(b);
                }
            }
        }

    }

}
