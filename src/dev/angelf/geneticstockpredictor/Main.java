package dev.angelf.geneticstockpredictor;

import dev.angelf.geneticstockpredictor.bot.Member;

import java.util.ArrayList;

public class Main {

    static ArrayList<Member> members;

    public static void main(String[] args) {
        initialize(100, 20);
    }

    private static void initialize(int amount, double money){
        members = new ArrayList<>();
        for(int i = 0; i < amount; i++){
            Member member = new Member(money);
            member.initialize();
            members.add(member);
        }
    }

}

