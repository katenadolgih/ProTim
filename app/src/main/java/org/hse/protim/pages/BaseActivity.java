package org.hse.protim.pages;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Здесь не вызываем setContentView, так как layout устанавливается в дочерних классах
    }
//новый коммит
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Добавьте анимацию, если нужно
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}