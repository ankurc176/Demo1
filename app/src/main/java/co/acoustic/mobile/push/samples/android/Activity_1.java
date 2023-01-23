package co.acoustic.mobile.push.samples.android;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import co.acoustic.mobile.push.samples.android.layout.ResourcesHelper;
import co.acoustic.mobile.push.sdk.api.OperationCallback;
import co.acoustic.mobile.push.sdk.api.OperationResult;
import co.acoustic.mobile.push.sdk.api.message.MessageProcessor;
import co.acoustic.mobile.push.sdk.api.message.MessageSync;
import co.acoustic.mobile.push.sdk.plugin.inapp.InAppManager;
import co.acoustic.mobile.push.sdk.plugin.inapp.InAppMessageProcessor;
import co.acoustic.mobile.push.sdk.plugin.inapp.InAppStorage;
import co.acoustic.mobile.push.sdk.plugin.inbox.InboxMessageProcessor;

public class Activity_1 extends AppCompatActivity{

    protected ResourcesHelper resourcesHelper;
    private static final String TAG = "co.acoustic.mobile.push.samples.android.Activity_1";

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

//        List<String> values = new ArrayList<String>(1);
//        values.add("offer");
//        InAppManager.show(getApplicationContext(), InAppStorage.KeyName.RULE, values, getSupportFragmentManager(), 1);

        MessageSync.syncMessages(getApplicationContext(), new OperationCallback<MessageSync.SyncReport>() {
            @Override
            public void onSuccess(MessageSync.SyncReport syncReport, OperationResult result) {
                List<String> values = new ArrayList<String>(1);
                values.add("offer");
                //Test Branch 1
                InAppManager.show(getApplicationContext(), InAppStorage.KeyName.RULE, values, getSupportFragmentManager(), R.id.con);
                publishReport(syncReport);
            }

            @Override
            public void onFailure(final MessageSync.SyncReport syncReport, final OperationResult result) {
                Log.d(TAG, "Message sync failed: " + syncReport.getFailureCause());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Activity_1.this.getApplicationContext(), "mes" + ": " + syncReport.getFailureCause(), Toast.LENGTH_SHORT).show();
                    }
                });
                List<String> values = new ArrayList<String>(1);
                values.add("offer");
                InAppManager.show(getApplicationContext(), InAppStorage.KeyName.RULE, values, getSupportFragmentManager(), R.id.con);
            }
        });
    }
    private void publishReport(MessageSync.SyncReport syncReport) {
        final StringBuilder msg = new StringBuilder();
        for(MessageProcessor.ProcessReport processReport : syncReport.getReports()) {
            if(processReport instanceof InAppMessageProcessor.Report) {
                Log.d(TAG, "Message sync successful: Received "+processReport.getNewMessages().size()+" new inapp messages");
                msg.append((msg.length()!= 0 ? "\n" : "")+processReport.getNewMessages().size()+" inapp new messages");
            }
            if(processReport instanceof InboxMessageProcessor.Report) {
                Log.d(TAG, "Message sync successful: Received "+processReport.getNewMessages().size()+" new inbox messages");
                msg.append((msg.length()!= 0 ? "\n" : "")+processReport.getNewMessages().size()+" inbox new messages");
            }
            if(msg.length() > 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Activity_1.this.getApplicationContext(), msg.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}