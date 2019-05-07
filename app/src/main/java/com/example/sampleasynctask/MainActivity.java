package com.example.sampleasynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

// AsyncTask는 Thread를 위한 코드와 UI접근 코드를 한꺼번에 넣을 수 있음
public class MainActivity extends AppCompatActivity {
    private BackgroundTask task;
    private ProgressBar progress;
    private TextView textView;
    int value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = (ProgressBar) findViewById(R.id.progress);
        textView = (TextView) findViewById(R.id.textView);

        Button executeButton = (Button) findViewById(R.id.executeButton);
        executeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task = new BackgroundTask();
                task.execute(100);
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.cancel(true);
            }
        });
    }

    class BackgroundTask extends AsyncTask <Integer, Integer, Integer> {

        // 백그라운드 작업 수행 전 호출
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            value = 0;
            progress.setProgress(value);
        }

        // 새로 만든 스레드에서 백그라운드 작업 수행
        @Override
        protected Integer doInBackground(Integer... integers) {
            while (!isCancelled()) {
                value ++;
                if (value >= 100) {
                    break;
                } else {
                    // 진행상태 UI에 업데이트
                    publishProgress(value);
                }

                try {
                    Thread.sleep(100);
                } catch (Exception e) {}
            }

            return value;
        }

        // 백그라운드 작업의 진행상태를 표시하기 위해 호출
        // doInBackground() 메소드에서 publishProgress() 호출 시 자동으로 호출
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progress.setProgress(values[0].intValue());
            textView.setText("Current Value : " + values[0].toString());

        }

        // 백그라운드 작업이 끝난 후에 호출
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            progress.setProgress(0);
            textView.setText("Finished");
        }

        // 작업 취소시 호출
        @Override
        protected void onCancelled() {
            super.onCancelled();
            progress.setProgress(0);
            textView.setText("Cancelled");
        }
    }
}
