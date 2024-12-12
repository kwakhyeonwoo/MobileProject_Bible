package com.example.finalproject_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class firstPageView extends AppCompatActivity {

    private EditText editNickName, editHeight, editWeight, editAge;
    private RadioButton radioMale, radioFemale;
    private Button btnCreate, btnChangeColor;
    //이미지 변경 변수
    private ImageView imageProfile;

    private boolean isMaleSelected = true;
    private int selectedColor = Color.WHITE; // 기본 색상

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstpageview);

        // EditText와 버튼 연결
        editNickName = findViewById(R.id.editNickName);
        editHeight = findViewById(R.id.editHeight);
        editWeight = findViewById(R.id.editWeight);
        editAge = findViewById(R.id.editAge);

        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);

        btnCreate = findViewById(R.id.btnCreate);
        btnChangeColor = findViewById(R.id.btnChangeColor);
        imageProfile = findViewById(R.id.imageProfile);

        // 성별 선택 버튼
        radioMale.setOnClickListener(v -> isMaleSelected = true);
        radioFemale.setOnClickListener(v -> isMaleSelected = false);

        // 색상 변경 버튼 클릭 이벤트
        btnChangeColor.setOnClickListener(v -> showColorPickerDialog());

        // "생성" 버튼 클릭 이벤트
        btnCreate.setOnClickListener(v -> {
            String nickName = editNickName.getText().toString();
            String height = editHeight.getText().toString();
            String weight = editWeight.getText().toString();
            String age = editAge.getText().toString();

            // 유효성 검사
            if (TextUtils.isEmpty(nickName) || TextUtils.isEmpty(height) ||
                    TextUtils.isEmpty(weight) || TextUtils.isEmpty(age)) {
                Toast.makeText(this, "모든 필드를 입력해주세요!", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    // 문자열을 숫자로 변환
                    float parsedHeight = Float.parseFloat(height);
                    float parsedWeight = Float.parseFloat(weight);
                    int parsedAge = Integer.parseInt(age);

                    // UserModel 객체 생성
                    UserModel userModel = new UserModel(nickName, isMaleSelected, parsedHeight, parsedWeight, parsedAge);

                    // SharedPreferences에 데이터 저장
                    SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString("userName", userModel.getUserName());
                    editor.putBoolean("userGender", userModel.isUserGender());
                    editor.putFloat("userHeight", userModel.getUserHeight());
                    editor.putFloat("userWeight", userModel.getUserWeight());
                    editor.putInt("userAge", userModel.getUserAge());
                    editor.apply();

                    // 다음 화면으로 이동
                    Intent intent = new Intent(firstPageView.this, MainUserView.class);

                    startActivity(intent);

                    // 현재 Activity 종료
                    finish();

                } catch (NumberFormatException e) {
                    Toast.makeText(this, "키, 몸무게, 나이는 숫자로 입력해주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 키보드 닫기 기능
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void showColorPickerDialog() {
        String[] colors = {"Gray", "Red", "Blue", "Yellow", "Green"};
        int[] colorValues = {Color.GRAY, Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("색상 선택");
        builder.setItems(colors, (dialog, which) -> {
            selectedColor = colorValues[which];
            imageProfile.setColorFilter(selectedColor, PorterDuff.Mode.MULTIPLY); // 이미지 색상 변경

            // 색상 값을 SharedPreferences에 저장
            SharedPreferences preferences = getSharedPreferences("userPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("selectedColor", selectedColor);
            editor.apply();
        });
        builder.show();
    }
}
