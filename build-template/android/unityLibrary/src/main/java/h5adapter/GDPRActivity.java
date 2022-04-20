package h5adapter;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.blowfire.app.framework.BFGdprConsent;
import com.blowfire.app.framework.activity.BFActivity;
import com.blowfire.common.utils.BFLog;
import com.sportbrain.jewelpuzzle.R;

import androidx.annotation.Dimension;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import h5adapter.utils.Dimensions;
import h5adapter.utils.SelectorDrawable;
import h5adapter.utils.Toasts;
import h5adapter.utils.Utils;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

public class GDPRActivity extends BFActivity {

    private static final String TAG = GDPRActivity.class.getSimpleName();

    private boolean continueClicked = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gdpr_activity);
        initViews();
        CCNativeAPIProxy.logEvent("GDPR_Alert_Show", true, true);
    }

    private void initViews() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        final ImageView first = findViewById(R.id.first);
        final ImageView second = findViewById(R.id.second);
        final TextView content = findViewById(R.id.content);
        content.setTextSize(COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.gdpr_content_size));
        final ImageView more = findViewById(R.id.learn_more);

        first.setImageDrawable(new SelectorDrawable(ContextCompat.getDrawable(this, R.drawable.continue_btn)));
        second.setImageDrawable(new SelectorDrawable(ContextCompat.getDrawable(this, R.drawable.read)));

        first.setOnClickListener(new View.OnClickListener() {
            long clickTime;

            @Override public void onClick(View view) {
                if (Math.abs(clickTime - System.currentTimeMillis()) < 200) {
                    return;
                }
                clickTime = System.currentTimeMillis();

                if (!continueClicked) {
                    first.setImageDrawable(new SelectorDrawable(ContextCompat.getDrawable(GDPRActivity.this, R.drawable.agree)));
                    second.setImageDrawable(new SelectorDrawable(ContextCompat.getDrawable(GDPRActivity.this, R.drawable.no_thx)));
                    more.setVisibility(View.VISIBLE);
                    content.setText(R.string.agree_thx);
                    continueClicked = true;
                    CCNativeAPIProxy.logEvent("GDPR_Alert_Continue_Click", true, true);
                } else {
                    BFLog.d(TAG, "gpdr enable");
                    BFGdprConsent.setGranted(true);
                    GDPRActivity.this.finish();
                    CCNativeAPIProxy.logEvent("GDPR_Alert_Agree_Click", true, true);
                }
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            long clickTime;

            @Override public void onClick(View view) {
                if (Math.abs(clickTime - System.currentTimeMillis()) < 200) {
                    return;
                }
                clickTime = System.currentTimeMillis();

                if (!continueClicked) {
                    BFLog.d(TAG, "go to website");
                    Utils.openBrowser(GDPRActivity.this, "https://blowfire.weebly.com/");
                    CCNativeAPIProxy.logEvent("GDPR_Alert_Read_Click", true, true);
                } else {
                    BFLog.d(TAG, "gpdr denied");
                    BFGdprConsent.setGranted(false);
                    CCNativeAPIProxy.logEvent("GDPR_Alert_Deny_Click", true, true);
                    GDPRActivity.this.finish();
                }
            }
        });

        more.setImageDrawable(new SelectorDrawable(ContextCompat.getDrawable(GDPRActivity.this, R.drawable.learn)));
        more.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                BFLog.d(TAG, "go to website");
                Utils.openBrowser(GDPRActivity.this, "https://blowfire.weebly.com/");
                CCNativeAPIProxy.logEvent("GDPR_Alert_More_Click", true, true);
            }
        });
    }

    @Override public void onBackPressed() {

    }
}
