package com.bigv.app.yocc.activity;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mithsoft.lib.componants.Toasty;

import java.io.IOException;

import com.bigv.app.yocc.R;
import com.bigv.app.yocc.controller.SyncServer;
import com.bigv.app.yocc.pojo.CallDetailsPojo;
import com.bigv.app.yocc.pojo.ResultPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.utils.MyAsyncTask;
import com.bigv.app.yocc.utils.MyBaseActivity;

/**
 * Created by MiTHUN on 25/11/17.
 */

public class PlayAudioFileActivity extends MyBaseActivity implements View.OnTouchListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {

    private final Handler handler = new Handler();
    ProgressDialog bdialog;
    ScrollView scrollView;
    CallDetailsPojo callDetailsPojo;
    Button btnSubmit;
    EditText edUserName, edComment;
    SeekBar seekBar;
    ImageView imgPlay;
    TextView tvUserName;
    MediaPlayer player;
    TextView tvTimeElapsed, tvTotalDuration;
    String filePathUrl = "";
    boolean isComp;
    private int mediaFileLengthInMilliseconds;


    @Override
    protected void genrateId() {

        setContentView(R.layout.play_file_activity);

        scrollView = findViewById(R.id.scrollView);
        btnSubmit = findViewById(R.id.btnSubmit);
        edUserName = findViewById(R.id.edUserName);
        edComment = findViewById(R.id.edComment);
        seekBar = findViewById(R.id.seekBar);
        imgPlay = findViewById(R.id.imgPlay);
        tvUserName = findViewById(R.id.tvUser);
        tvTimeElapsed = findViewById(R.id.tvTimeElapsed);
        tvTotalDuration = findViewById(R.id.tvTotalDuration);
        player = new MediaPlayer();
        bdialog = new ProgressDialog(PlayAudioFileActivity.this);
        bdialog.setCancelable(false);
        bdialog.setCanceledOnTouchOutside(false);

        //setToolbarTitle("Play File");
        setToolbarTitle(getResources().getString(R.string.str_title_play_file));
    }


    @Override
    protected void registerEvents() {

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePathUrl.isEmpty()) {
                    if (player.isPlaying()) {

                    } else {

                        getAudioUrlFromServer();
//                        ServerGetRequestTask serverGetRequestTask = new ServerGetRequestTask(new ServerGetRequestTask.TaskListener() {
//                            @Override
//                            public void onFinished(String result) {
//                                try {
//                                    JSONObject jsonObject = new JSONObject(result);
//                                    if (jsonObject != null) {
//                                        String s = jsonObject.getString("CallFile");
//                                        if (s != null && !s.isEmpty()) {
//                                            filePathUrl = s;
//                                            playAudio();
//                                        }
//                                    } else
//                                        CommonHelper.getInstance().showErrorDialog("Unable to play audio", PlayAudioFileActivity.this);
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }, PlayAudioFileActivity.this);
//
//                        if (AppController.getInstance().isInternetAvailable()) {
//                            serverGetRequestTask.setUrl(String.format(GlobalConstants.GET_FILE_URL, callDetailsPojo.getCallFile()));
//                            serverGetRequestTask.execute();
//                        }
                    }
                } else {
                    if (player.isPlaying()) {
                        player.pause();
                        imgPlay.setImageResource(R.drawable.ic_play_file);
                        primarySeekBarProgressUpdater();
                    } else if (isComp) {
                        player.start();
                        imgPlay.setImageResource(R.drawable.ic_pause_file);
                        primarySeekBarProgressUpdater();
                    } else {
                        playAudio();
                    }
                }
            }
        });
        seekBar.setProgress(1);
        seekBar.setEnabled(false);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                long currentPositionPlayer = player.getCurrentPosition();
                if (seekBar.getMax() > 0 && progress > 0) {
                    String currentPosition = progressToTimer(currentPositionPlayer);
                    tvTimeElapsed.setText(currentPosition);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }


    private void getAudioUrlFromServer() {

        new MyAsyncTask(this, true, new MyAsyncTask.AsynTaskListener() {

            ResultPojo resultPojo = null;

            @Override
            public void doInBackgroundOpration(SyncServer syncServer) {

                resultPojo = syncServer.getFileDownloadData(callDetailsPojo.getCdTrNo());
            }

            @Override
            public void onFinished() {

                if (!AUtils.isNull(resultPojo) && resultPojo.getStatus().equals(AUtils.STATUS_SUCCESS)) {

                    filePathUrl = resultPojo.getMessage();
//                    filePathUrl = "http://yoccapp.com/WAV/Voicelog-9028784872-9922953883-152552.wav";
                    playAudio();
                } else {

                    Toasty.error(PlayAudioFileActivity.this, getString(R.string.fileNotFound), Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();
    }


    @Override
    public void initData() {

        callDetailsPojo = (CallDetailsPojo) getIntent().getSerializableExtra(AUtils.CALL_DETAILS_POJO);

        if (!AUtils.isNull(callDetailsPojo)) {

            if (!AUtils.isNullString(callDetailsPojo.getCallerName())) {

                tvUserName.setText(callDetailsPojo.getCallerName() + " - ");
            }
            if (!AUtils.isNullString(callDetailsPojo.getCallerNumber())) {
                if (!AUtils.isNullString(callDetailsPojo.getCallerName())) {

                    tvUserName.setText(tvUserName.getText() + callDetailsPojo.getCallerNumber());
                } else {
                    tvUserName.setText(callDetailsPojo.getCallerNumber());
                }
            }
        } else {
            Toasty.error(this, getString(R.string.fileNotFound), Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    public String progressToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = String.format("%02d", hours) + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + String.format("%02d", minutes) + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    private void playAudio() {
        final ProgressDialog dialog = new ProgressDialog(this);
        try {
            seekBar.setEnabled(true);
            seekBar.setMax(99);
            seekBar.setOnTouchListener(this);
            player.reset();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(PlayAudioFileActivity.this, Uri.parse(filePathUrl));
//            player.setDataSource(PlayAudioFileActivity.this, Uri.parse("http://yoccapp.com/WAV/Voicelog-9028784872-9922953883-152552.wav"));
            player.setOnBufferingUpdateListener(this);
            player.setOnCompletionListener(this);
            player.prepareAsync();

            dialog.setMessage("Please wait playing audio");
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
//            player.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//                @Override
//                public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
//                    if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
//                        dialog.dismiss();
//                        dialog.setMessage("Buffering audio");
//                        dialog.show();
//                    } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
//                        dialog.dismiss();
//                    }
//                    return false;
//                }
//            });
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaFileLengthInMilliseconds = mp.getDuration();
                    isComp = true;
                    tvTotalDuration.setText("" + getTotalTime(mediaFileLengthInMilliseconds));
                    tvTimeElapsed.setText("00:00");
                    dialog.dismiss();
                    mp.start();
                    imgPlay.setImageResource(R.drawable.ic_pause_file);
                    primarySeekBarProgressUpdater();
                }
            });
            player.setWakeMode(getApplicationContext(), PowerManager.SCREEN_BRIGHT_WAKE_LOCK);

        } catch (IOException e) {
            e.printStackTrace();
            dialog.dismiss();
        }
    }

    private String getTotalTime(long is) {
        long t = is / 1000;
        long h = 0;
        long min = t / 60;
        if (min > 60) {
            h = min / 60;
            min = min % 60;
        }
        long sec = t % 60;
        String time;
        if (h != 0)
            time = String.format("%02d:%02d:%02d", h, min, sec);
        else
            time = String.format("%02d:%02d", min, sec);
        return time;
    }

    private void primarySeekBarProgressUpdater() {
        seekBar.setProgress((int) (((float) player
                .getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This

        if (player.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification, 1000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null && player.isPlaying()) {
            player.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null && isComp) {

            player.start();
            primarySeekBarProgressUpdater();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if (v.getId() == R.id.seekBar) {
            /**
             * Seekbar onTouch event handler. Method which seeks MediaPlayer to
             * seekBar primary progress position
             */
            if (player.isPlaying()) {
                SeekBar sb = (SeekBar) v;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100)
                        * sb.getProgress();
                player.seekTo(playPositionInMillisecconds);
            }
        }
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
        seekBar.setSecondaryProgress(percent);

        if (percent < 100) {
            bdialog.setMessage("Please wait, buffering audio");
            if (!bdialog.isShowing())
                bdialog.show();
        } else {

            if (bdialog.isShowing())
                bdialog.dismiss();

        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        imgPlay.setImageResource(R.drawable.ic_play_file);
        tvTimeElapsed.setText("00:00");
        seekBar.setMax(0);
        seekBar.setProgress(0);
        isComp = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
        }
    }
}