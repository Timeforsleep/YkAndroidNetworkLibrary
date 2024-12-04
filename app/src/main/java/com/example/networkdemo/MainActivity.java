package com.example.networkdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.yknetworklibrary.APMRecord;
import com.example.yknetworklibrary.YkBaseServant;
import com.example.yknetworklibrary.YkNetworkManager;
import com.example.yknetworklibrary.YkPriority;
import com.example.yknetworklibrary.configs.YkNetworkConfig;

import java.io.IOException;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        button.setOnClickListener(
                v -> {
                    YkNetworkManager.getInstance().init();
                    // 创建一个 YkBaseServant 实例
                    YkBaseServant ykBaseServant = new YkBaseServant();

                    // 使用链式调用设置请求的 URL 和其他参数
                    ykBaseServant.url("https://jsonplaceholder.typicode.com/posts")
                            .method("GET")
                            .addHeader("Authorization", "Bearer token")
                            .priority(YkPriority.HIGH)
                            .setApmRecord(new APMRecord("https://jsonplaceholder.typicode.com/posts"))
                            .request(new YkBaseServant.RequestCallback() {
                                @Override
                                public void onSuccess(Response response) {
                                    try {
                                        Log.w("TAG", "onSuccess: " + response.body().string());
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                                @Override
                                public void onFailure(IOException e) {
                                    Log.w("TAG", "onFailure: ");
                                }

                                @Override
                                public void onError(int statusCode, String errorMessage) {
                                    Log.w("TAG", "onError: " + statusCode + "    " + errorMessage);
                                }
                            });
                }
        );
    }
}