package pansong291.xposed.quickenergy.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import pansong291.xposed.quickenergy.R;
import pansong291.xposed.quickenergy.util.LanguageUtil;

public class HtmlViewerActivity extends Activity {
    MyWebView mWebView;
    ProgressBar pgb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageUtil.setLocale(this);
        setContentView(R.layout.activity_html_viewer);

        mWebView = findViewById(R.id.mwv_webview);
        pgb = findViewById(R.id.pgb_webview);

        mWebView.setWebChromeClient(
                new WebChromeClient() {
                    @Override
                    public void onProgressChanged(WebView view, int progress) {
                        pgb.setProgress(progress);
                        if (progress < 100) {
                            setTitle("Loading...");
                            pgb.setVisibility(View.VISIBLE);
                        } else {
                            setTitle(mWebView.getTitle() + MainActivity.version);
                            pgb.setVisibility(View.GONE);
                        }
                    }
                });
        mWebView.loadUrl(getIntent().getData().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 3, 0, getString(R.string.scroll_to_top));
        menu.add(0, 4, 0, getString(R.string.scroll_to_bottom));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 3:
                mWebView.scrollTo(0, 0);
                break;

            case 4:
                mWebView.scrollToBottom();
                break;
        }
        return true;
    }
}
