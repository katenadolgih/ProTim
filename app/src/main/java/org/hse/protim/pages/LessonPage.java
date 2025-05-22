package org.hse.protim.pages;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import org.hse.protim.DTO.lesson.LessonDTO;
import org.hse.protim.DTO.lesson.LessonId;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.lesson.LessonClient;

import java.util.Objects;

public class LessonPage extends BaseActivity {

    private TextView lessonTextView;
    private TextView lessonTaskTextView;
    private Button nextLessonButton;
    private ImageButton prevLessonButton;
    private ImageButton buttonBack;
    private TextView titleView;
    private ExoPlayer player;
    private PlayerView playerView;
    private Long lessonId;
    private LessonClient lessonClient;
    private RetrofitProvider retrofitProvider;
    private TextView titleText;
//    private ImageButton lessonNavButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_page);

        init();
        handle();
        setupButtons();
        setMainFields();

        titleView.setText(R.string.lesson_number);
    }

    private void init() {
        lessonTextView = findViewById(R.id.lesson_text);
        lessonTaskTextView = findViewById(R.id.lesson_task_text);
//        lessonVideoView = findViewById(R.id.lesson_video);
        nextLessonButton = findViewById(R.id.btn_next_lesson);
        prevLessonButton = findViewById(R.id.btn_prev_lesson);
        titleView = findViewById(R.id.title_text);
        playerView = findViewById(R.id.player_view);
        buttonBack = findViewById(R.id.button_back);
        lessonId = getIntent().getLongExtra("lessonId", 0);
        retrofitProvider = new RetrofitProvider(LessonPage.this);
        lessonClient = new LessonClient(retrofitProvider);
        titleText = findViewById(R.id.title_text);
//        lessonNavButton = findViewById(R.id.lesson_nav).findViewById(R.id.btn_next_lesson);

    }

    private void setMainFields() {
        lessonClient.getLessonContent(lessonId, new LessonClient.GetLessonContentCallback() {
            @Override
            public void onSuccess(LessonDTO lessonDTO) {
                lessonTextView.setText(lessonDTO.lessonText());
                lessonTaskTextView.setText(lessonDTO.lessonTaskText());
                titleText.setText(lessonDTO.lessonNameWithNumber());
                setupExoPlayer(lessonDTO.lessonVideoPath());
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(LessonPage.this, message, Toast.LENGTH_LONG).show());
            }
        });
    }
    private void handle() {
        if (buttonBack != null) {
            buttonBack.setOnClickListener(v -> onBackPressed());
        }
    }


    private void setupButtons() {
        nextLessonButton.setOnClickListener(v ->
                lessonClient.getAfterLesson(lessonId, new LessonClient.GetBeforeAfterLessonCallback() {
            @Override
            public void onSuccess(LessonId getLessonId) {
                Long newLessonId = getLessonId.lessonId();
                if (!Objects.equals(lessonId, newLessonId)) {
                    lessonClient.updateLastLesson(newLessonId, new LessonClient.UpdateLastLessonCallback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(String message) {
                            Activity activity = (Activity) v.getContext();
                            activity.runOnUiThread(() ->
                                    Toast.makeText(activity,
                                                    message,
                                                    Toast.LENGTH_SHORT)
                                            .show());
                        }
                    });
                    Intent intent = new Intent(LessonPage.this, LessonPage.class);
                    intent.putExtra("lessonId", newLessonId);
                    startActivity(intent);
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(LessonPage.this, message, Toast.LENGTH_LONG).show();
            }
        }));

        prevLessonButton.setOnClickListener(v ->
                lessonClient.getBeforeLesson(lessonId, new LessonClient.GetBeforeAfterLessonCallback() {
            @Override
            public void onSuccess(LessonId getLessonId) {
                Long newLessonId = getLessonId.lessonId();
                if (!Objects.equals(newLessonId, lessonId)) {
                    Intent intent = new Intent(LessonPage.this, LessonPage.class);
                    intent.putExtra("lessonId", newLessonId);
                    startActivity(intent);
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(LessonPage.this, message, Toast.LENGTH_LONG).show();
            }
        }));
    }

    private void setupExoPlayer(String url) {
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        Uri videoUri = Uri.parse(url);

        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        player.setMediaItem(mediaItem);
        player.prepare();
//        player.play();
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
