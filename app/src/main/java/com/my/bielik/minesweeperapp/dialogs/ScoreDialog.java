package com.my.bielik.minesweeperapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;

import com.my.bielik.minesweeperapp.activities.MainActivity;

public class ScoreDialog extends AppCompatDialogFragment {

    private String difficulty;
    private String[] scoreArray;

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        difficulty = args.getString("difficulty");
        scoreArray = args.getStringArray("score_set");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Best scores - " + MainActivity.convertDifficultyToString(String.valueOf(difficulty)))
                .setMessage("1) " + scoreArray[0] + "\n2) "  + scoreArray[1] + "\n3) " + scoreArray[2])
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}
