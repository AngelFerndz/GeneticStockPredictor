package dev.angelf.geneticstockpredictor.command;

import java.util.Scanner;

public class CommandHandler {

    private Scanner scanner;

    public CommandHandler() {
        scanner = new Scanner(System.in);
    }

    public void printHead(int amount, double cash) {
        System.out.println("Genetic Stock Predictor");
        System.out.println("By angelf.dev | Angel Fernandez");
        System.out.println("Generating [" + amount + "] Members.");
        System.out.println("Initial Investment: " + cash);
    }

    public void captureInputs() {
        System.out.print("Enter Money Amount: ");
        double cash = scanner.nextInt();
        System.out.print("Enter Amount of Bots: ");
        int amount = scanner.nextInt();
        System.out.print("Enter Amount of Training Cycles: ");
        int trainingCycles = scanner.nextInt();
    }

}
