package com.example.finalproject_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainUserView extends AppCompatActivity {

    private EditText todayCalorieEditText;
    private TextView currentCalorieTextView;
    private Button suggestCalorieButton, myInfoButton;
    private ImageView characterImageView; // 수정된 부분: characterImageView로 이미지 설정
    private UserModel userModel;
    private int nowCalorie = 0;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainuserview);

        // BottomNavigationView 참조
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 메뉴 아이템 클릭 이벤트 처리
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    // 홈 화면 동작

                    return true;

                case R.id.nav_search:
                    // 검색 화면 동작

                    return true;

                case R.id.nav_profile:
                    // 프로필 화면 동작
                    Intent intent =new Intent(MainUserView.this, UserSetting.class);
                    startActivity(intent);
                    finish();

                    return true;

                default:
                    return false;
            }
        });

        // Intent로 전달된 Bitmap 받기
        Intent intent = getIntent();
        Bitmap imageBitmap = (Bitmap) intent.getParcelableExtra("imageBitmap");

        // 이미지뷰에 Bitmap 설정
        characterImageView = findViewById(R.id.characterImageView);
        if (imageBitmap != null) {
            characterImageView.setImageBitmap(imageBitmap); // 이미지 설정
        }

        // SharedPreferences에서 선택된 색상 정보 읽어오기
        SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        int selectedColor = preferences.getInt("selectedColor", Color.WHITE); // 기본값은 흰색

        // 이미지에 색상 필터 적용
        characterImageView.setColorFilter(selectedColor, PorterDuff.Mode.MULTIPLY); // 색상 적용

        // Initialize views
        todayCalorieEditText = findViewById(R.id.todayCalorieEditText);
        currentCalorieTextView = findViewById(R.id.currentCalorieText);
        suggestCalorieButton = findViewById(R.id.suggestCalorieButton);
        myInfoButton = findViewById(R.id.myInfoButton);

        // Get user data from SharedPreferences
        userModel = getUserData();

        // Set up character image click event
        characterImageView.setOnClickListener(v -> {
            Intent intentCalorie = new Intent(MainUserView.this, InputCalorie.class);

            startActivity(intentCalorie);
        });

        // Handle suggest calorie button click
        suggestCalorieButton.setOnClickListener(v -> suggestCalorie());

        // Handle my info button click
        myInfoButton.setOnClickListener(v -> showUserInfo());
    }


    // Method to get user data from SharedPreferences
    private UserModel getUserData() {
        SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        String userName = preferences.getString("userName", "");
        boolean userGender = preferences.getBoolean("userGender", true);
        float userHeight = preferences.getFloat("userHeight", 0);
        float userWeight = preferences.getFloat("userWeight", 0);
        int userAge = preferences.getInt("userAge", 0);

        return new UserModel(userName, userGender, userHeight, userWeight, userAge);
    }

    // Suggest calorie based on user data
    private void suggestCalorie() {
        double heightValue = userModel.getUserHeight();
        double weightValue = userModel.getUserWeight();
        double ageValue = userModel.getUserAge();
        double recommendedCalories;

        if (userModel.isUserGender()) {
            // Male recommended calories
            double manBaseCal = 10 * weightValue + 6.25 * heightValue - 5 * ageValue + 5;
            recommendedCalories = manBaseCal * 1.55;
        } else {
            // Female recommended calories
            double womanBaseCal = 10 * weightValue + 6.25 * heightValue - 5 * ageValue - 161;
            recommendedCalories = womanBaseCal * 1.55;
        }

        String message = "권장 칼로리는 " + String.format("%.0f", recommendedCalories) + "입니다.";
        showAlert("권장 칼로리", message);
    }

    // Show user info in alert dialog
    private void showUserInfo() {
        String message = "\n닉네임: " + userModel.getUserName() + "\n" +
                "성별: " + (userModel.isUserGender() ? "남성" : "여성") + "\n" +
                "키: " + userModel.getUserHeight() + " cm\n" +
                "몸무게: " + userModel.getUserWeight() + " kg\n" +
                "나이: " + userModel.getUserAge() + "세";

        showAlert(userModel.getUserName() + "님의 정보", message);
    }

    // Show alert dialog with title and message
    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("확인", null)
                .show();
    }

    //외부 공간 클릭시 패드 내려감
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            View view = getCurrentFocus();
            if (view != null){
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if(imm != null){
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

}
