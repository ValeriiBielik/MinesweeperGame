package com.my.bielik.minesweeperapp;

import android.content.Context;
import android.content.SharedPreferences;

public class ScoreSaver {

    public static final String BEST_SCORES = "best_scores";

    public static final String SCORES_BEGINNER_DIFFICULTY = "scores_beginner_difficulty";
    public static final String SCORES_AMATEUR_DIFFICULTY = "scores_beginner_amateur";
    public static final String SCORES_PROFESSIONAL_DIFFICULTY = "scores_beginner_professional";

    private SharedPreferences preferences;
    private String scoreTime;

    public ScoreSaver(Context context) {
        preferences = context.getSharedPreferences(BEST_SCORES, Context.MODE_PRIVATE);
    }

    public void saveScore(int difficulty, String time) {
        SharedPreferences.Editor editor = preferences.edit();

        String[] newTimeContainer = time.split(":");
        int newTime = Integer.parseInt(newTimeContainer[0]) * 60 + Integer.parseInt(newTimeContainer[1]);

        for (int i = 0; i < 3; i++) {
            String difficultyLevel = changeScoreTime(difficulty, i);
            if (scoreTime.equals("")) {
                editor.putString(difficultyLevel, time);
                break;
            } else {
                String[] timeContainer = scoreTime.split(":");
                int currentTime = Integer.parseInt(timeContainer[0]) * 60 + Integer.parseInt(timeContainer[1]);
                if (newTime < currentTime) {
                    editor.putString(difficultyLevel, time);
                    time = scoreTime;
                }
            }
        }
        editor.apply();
    }

    private String changeScoreTime(int difficulty, int pos) {
        switch (difficulty) {
            case 0:
                scoreTime = preferences.getString(SCORES_BEGINNER_DIFFICULTY.concat(String.valueOf(pos)), "");
                return SCORES_BEGINNER_DIFFICULTY.concat(String.valueOf(pos));
            case 1:
                scoreTime = preferences.getString(SCORES_AMATEUR_DIFFICULTY.concat(String.valueOf(pos)), "");
                return SCORES_AMATEUR_DIFFICULTY.concat(String.valueOf(pos));
            case 2:
                scoreTime = preferences.getString(SCORES_PROFESSIONAL_DIFFICULTY.concat(String.valueOf(pos)), "");
                return SCORES_PROFESSIONAL_DIFFICULTY.concat(String.valueOf(pos));
            default:
                return "-1";
        }
    }

}
