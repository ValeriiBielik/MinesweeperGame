package com.my.bielik.minesweeperapp.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;

import com.my.bielik.minesweeperapp.activities.MainActivity;

public class GameWinnerDialog extends AppCompatDialogFragment {

    private int difficulty;
    private String time;

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        difficulty = args.getInt("difficulty");
        time = args.getString("time");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("You win!")
                .setMessage(MainActivity.convertDifficultyToString(String.valueOf(difficulty)) + " - " + time)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }
}
