package shacus.edu.seu.com.shacus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import shacus.edu.seu.com.shacus.R;


public class WebviewActivity extends AppCompatActivity {
    private WebView webView;
    private String url;
    private TextView back;
    private TextView titleText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        back=(TextView) findViewById(R.id.orderfriends_toolbar_back);
        back.setText("╳");
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        titleText=(TextView)findViewById(R.id.orderfriends_toolbar_title);

        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        Intent intent = super.getIntent();
        Bundle bundle=intent.getExtras();
        url = bundle.getString("Url");
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                titleText.setText(title);
            }
        });
    }
}
