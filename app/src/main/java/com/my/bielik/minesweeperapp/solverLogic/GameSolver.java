package com.my.bielik.minesweeperapp.solverLogic;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.my.bielik.minesweeperapp.GameFieldAdapter;

import java.util.List;

public class GameSolver {

    private GridLayoutManager layoutManager;

    public GameSolver(GridLayoutManager layoutManager){
        this.layoutManager = layoutManager;
    }

    // TODO: 23.05.2019 replace solving logic from adapter 
    public void solveGame(){
    }


    public static class Prob {
        public static Double sum(Double previousProb, Double currentProb){
            return 1 - (1 - previousProb) * (1 - currentProb);
        }
        public static List<Double> correct(List<Double> probs, int mines){
            Double sum = 0.0;
            if (probs.size() != 0) {
                for (Double prob : probs){
                    sum += prob;
                }
                sum = mines / sum;
                for (int i = 0; i < probs.size(); i++){
                    probs.set(i, probs.get(i) * sum);
                }
            }
            return probs;
        }
    }
}
