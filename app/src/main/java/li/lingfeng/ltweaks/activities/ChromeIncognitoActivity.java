package li.lingfeng.ltweaks.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import li.lingfeng.ltweaks.R;
import li.lingfeng.ltweaks.prefs.IntentActions;
import li.lingfeng.ltweaks.prefs.PackageNames;
import li.lingfeng.ltweaks.utils.Logger;

/**
 * Created by lilingfeng on 2017/7/18.
 */

public class ChromeIncognitoActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String action = getIntent().getAction();
        String url = null;
        if (Intent.ACTION_PROCESS_TEXT.equals(action)) {
            String text = getIntent().getStringExtra(Intent.EXTRA_PROCESS_TEXT);
            if (Patterns.WEB_URL.matcher(text).matches()) {
                Logger.i("Incognito url: " + text);
                url = text;
            } else {
                Logger.i("Incognito text search: " + text);
                url = "https://www.google.com/search?gws_rd=cr&q=" + Uri.encode(text);
            }
        } else if (Intent.ACTION_VIEW.equals(action)
                || IntentActions.ACTION_CHROME_INCOGNITO.equals(action)) {
            url = getIntent().getDataString();
            Logger.i("Incognito url: " + url);
        }

        if (StringUtils.isEmpty(url)) {
            Toast.makeText(this, R.string.not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        boolean isFromLTweaksExternal = getIntent().getBooleanExtra("from_ltweaks_external", true);
        Logger.d("isFromLTweaksExternal " + isFromLTweaksExternal);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setPackage(PackageNames.CHROME);
        intent.putExtra("com.google.android.apps.chrome.EXTRA_OPEN_NEW_INCOGNITO_TAB", true);
        intent.putExtra("com.android.browser.application_id", PackageNames.CHROME);
        intent.putExtra("from_ltweaks", true);
        intent.putExtra("from_ltweaks_external", isFromLTweaksExternal);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}