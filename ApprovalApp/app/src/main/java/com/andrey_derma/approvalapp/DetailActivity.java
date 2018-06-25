package com.andrey_derma.approvalapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.andrey_derma.approvalapp.model.ApprovalRequest;
import com.andrey_derma.approvalapp.model.ApprovalRequestDO;
import com.andrey_derma.approvalapp.model.ApprovalResponse;
import com.andrey_derma.approvalapp.network.NetworkService;
import com.andrey_derma.approvalapp.network.RetrofitAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

public class DetailActivity extends AppCompatActivity {
    String id;
    DynamoDBMapper dynamoDBMapper;
    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.button_reject)
    Button buttonReject;
    @BindView(R.id.button_approve)
    Button buttonApprove;

    String pipelineName;
    String stageName;
    String token;
    String actionName;
    ProgressDialog dialog;
    ApprovalRequest approvalRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        System.out.println(id);
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
        dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .build();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
        new GetUserListTask().execute();
    }

    @OnClick(R.id.button_reject)
    void rejected()
    {
        dialog = ProgressDialog.show(this, "", "Please wait..", true);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                ApprovalSetData("Rejected");
                NetworkService networkService = RetrofitAdapter.getClient().create(NetworkService.class);
                networkService.setApprovalRequestData(approvalRequest)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new ResourceSubscriber<ApprovalResponse>() {
                    @Override
                    public void onNext(ApprovalResponse approvalResponse) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        System.out.println("OK DONE COMRADE!!");
                        Intent intent = new Intent();
                        intent.putExtra("finish", "true");
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(DetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        }, 3000);
    }

    @OnClick(R.id.button_approve)
    void approved()
    {
        dialog = ProgressDialog.show(this, "", "Please wait..", true);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                ApprovalSetData("Approved");
                NetworkService networkService = RetrofitAdapter.getClient().create(NetworkService.class);
                networkService.setApprovalRequestData(approvalRequest)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new ResourceSubscriber<ApprovalResponse>() {
                    @Override
                    public void onNext(ApprovalResponse approvalResponse) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        System.out.println("OK DONE COMRADE!!");
                        Intent intent = new Intent();
                        intent.putExtra("finish", "true");
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(DetailActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        }, 3000);
    }

    void ApprovalSetData(String action)
    {
        approvalRequest = new ApprovalRequest();
        approvalRequest.setAction(action);
        approvalRequest.setActionName(actionName);
        approvalRequest.setToken(token);
        approvalRequest.setPipelineName(pipelineName);
        approvalRequest.setStageName(stageName);
        approvalRequest.setId(id);
        approvalRequest.setMessage(editText.getText().toString());
    }

    private class GetUserListTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... inputs) {
            ApprovalRequestDO approvalRequestDO = dynamoDBMapper.load(
                    ApprovalRequestDO.class,
                    id);
            pipelineName = approvalRequestDO.get_actionPipelineName();
            stageName = approvalRequestDO.get_actionStagename();
            token = approvalRequestDO.get_token();
            actionName = approvalRequestDO.get_actionName();
            return null;
        }

        protected void onPostExecute(Void result) {

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
