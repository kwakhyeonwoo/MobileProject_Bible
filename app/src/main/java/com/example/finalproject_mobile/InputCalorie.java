package com.example.finalproject_mobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class InputCalorie extends AppCompatActivity {

    private int nowCalorie = 0; // 현재 칼로리 값
    private boolean showFoodInput = false;
    private boolean delFoodInput = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inputcalorie);

        Button addFoodButton = findViewById(R.id.addFoodButton);
        Button burnCalorieButton = findViewById(R.id.burnCalorieButton);
        LinearLayout inputLayout = findViewById(R.id.inputLayout);
        TextView currentCalorieText = findViewById(R.id.currentCalorieText);

        currentCalorieText.setText("현재 칼로리: " + nowCalorie);

        addFoodButton.setOnClickListener(v -> {
            showFoodInput = true;
            delFoodInput = false;
            showInputDialog(inputLayout, currentCalorieText);
        });

        burnCalorieButton.setOnClickListener(v -> {
            showFoodInput = false;
            delFoodInput = true;
            showInputDialog(inputLayout, currentCalorieText);
        });
    }

    private void showInputDialog(LinearLayout inputLayout, TextView currentCalorieText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(showFoodInput ? "음식 추가" : "소모 칼로리");

        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setPadding(50, 40, 50, 10);

        EditText inputField1 = new EditText(this);
        inputField1.setHint(showFoodInput ? "음식 이름" : "운동 이름");
        inputField1.setInputType(InputType.TYPE_CLASS_TEXT);
        dialogLayout.addView(inputField1);

        EditText inputField2 = new EditText(this);
        inputField2.setHint("칼로리 입력");
        inputField2.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialogLayout.addView(inputField2);

        builder.setView(dialogLayout);

        builder.setPositiveButton("추가", (dialog, which) -> {
            String inputName = inputField1.getText().toString();
            String calorieInput = inputField2.getText().toString();

            if (!calorieInput.isEmpty()) {
                int calorieValue = Integer.parseInt(calorieInput);

                // MainUserView로 이동
                Intent intent = new Intent(InputCalorie.this, MainUserView.class);
                intent.putExtra("currentCalorie", nowCalorie);
                startActivity(intent);
                finish();

                currentCalorieText.setText("현재 칼로리: " + nowCalorie);
            }
        });

        builder.setNegativeButton("취소", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
