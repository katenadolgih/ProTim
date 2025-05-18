package org.hse.protim.pages;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import org.hse.protim.R;

public class LessonPage extends BaseActivity {

    private TextView lessonTextView;
    private TextView lessonTaskTextView;
    private VideoView lessonVideoView;
    private Button nextLessonButton;
    private ImageButton buttonBack;
    private TextView titleView;
    private ExoPlayer player;
    private PlayerView playerView;
//    private ImageButton lessonNavButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_page);

        init();
        handle();
        setupLessonContent();
//        setupVideo();
        setupButtons();
        setupExoPlayer();

        titleView.setText(R.string.lesson_number);
    }

    private void init() {
        lessonTextView = findViewById(R.id.lesson_text);
        lessonTaskTextView = findViewById(R.id.lesson_task_text);
//        lessonVideoView = findViewById(R.id.lesson_video);
        nextLessonButton = findViewById(R.id.btn_next_lesson);
        titleView = findViewById(R.id.title_text);
        playerView = findViewById(R.id.player_view);
        buttonBack = findViewById(R.id.button_back);


//        lessonNavButton = findViewById(R.id.lesson_nav).findViewById(R.id.btn_next_lesson);

    }

    private void handle() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> onBackPressed());
        }
    }

    private void setupLessonContent() {
        // Здесь можно установить текст урока и задание программно (например, из Intent)
        lessonTextView.setText("Текст урока, в котором описывается, как выполнять задание, на что обратить внимание и т.д.");
        lessonTaskTextView.setText("Создать собственный эскиз проекта с пояснением идей.");
    }

//    private void setupVideo() {
//        Uri videoUri = Uri.parse("https://xn--h1aiedfn.xn--p1ai/storage/2024/07/26/b52f19b892caeef33923d050acfc71928b3a58df.mp4");
//        lessonVideoView.setVideoURI(videoUri);
//
//        MediaController mediaController = new MediaController(this);
//        mediaController.setAnchorView(lessonVideoView);
//        lessonVideoView.setMediaController(mediaController);
//
//        lessonVideoView.setOnPreparedListener(mp -> {
//            mp.setLooping(false); // Можно поставить true, если нужно зацикливать
//            lessonVideoView.start();
//        });
//    }

    private void setupButtons() {
        nextLessonButton.setOnClickListener(v -> {
            // Здесь логика перехода к следующему уроку
            // Например: startActivity(new Intent(this, NextLessonPage.class));
        });

//        lessonNavButton.setOnClickListener(v -> {
//            // Логика переключения на предыдущий/другой урок
//        });
    }

    private void setupExoPlayer() {
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        // Видео из интернета
        Uri videoUri = Uri.parse("https://xn--h1aiedfn.xn--p1ai/storage/2024/07/26/b52f19b892caeef33923d050acfc71928b3a58df.mp4");

        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }
    }

}
