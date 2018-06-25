package com.andrey_derma.approvalapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.andrey_derma.approvalapp.adapter.ApprovalAdapter;
import com.andrey_derma.approvalapp.model.ApprovalRequestDO;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ApprovalAdapter.ClickListener, SwipeRefreshLayout.OnRefreshListener {

    DynamoDBMapper dynamoDBMapper;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    private AWSCredentialsProvider credentialsProvider;
    private AWSConfiguration configuration;

    LinearLayoutManager linearLayoutManager;
    ApprovalAdapter approvalAdapter;
    List<ApprovalRequestDO> approvalRequestDOList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                credentialsProvider = AWSMobileClient.getInstance().getCredentialsProvider();
                configuration = AWSMobileClient.getInstance().getConfiguration();
                Log.d("YourMainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();

        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
        dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .build();

        setupView();
    }

    void setupView() {
        swipeContainer.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(this);
        approvalAdapter = new ApprovalAdapter(this, approvalRequestDOList);
        approvalAdapter.setOnItemClickListener(this);

        recyclerView.setAdapter(approvalAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        //getAllData();
        new GetUserListTask().execute();

    }

    @Override
    public void onRefresh() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        new GetUserListTask().execute();
    }

    @Override
    public void onItemClick(int position, View v) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("id", approvalRequestDOList.get(position).getId());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                new GetUserListTask().execute();
            }
        }
    }

    private class GetUserListTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... inputs) {

            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            //returns a list of items from the table. each item is of Approvals
            //approvalRequestDOList = dynamoDBMapper.scan(ApprovalRequestDO.class, scanExpression);
            approvalRequestDOList.clear();
            approvalRequestDOList.addAll(dynamoDBMapper.scan(ApprovalRequestDO.class, scanExpression));
//            List<ApprovalRequestDO> _approvalRequestDOList = dynamoDBMapper.scan(ApprovalRequestDO.class, scanExpression);
//            for (ApprovalRequestDO approvalRequestDO : _approvalRequestDOList) {
//                ApprovalRequestDO aa = new ApprovalRequestDO();
//                aa.setStatus("SSS");
//                aa.setDate(approvalRequestDO.getDate());
//                System.out.println("YO " + aa.getDate());
//                approvalRequestDOList.add(aa);
//            }
            return null;
        }

        protected void onPostExecute(Void result) {
            swipeContainer.setRefreshing(false);
            approvalAdapter.notifyDataSetChanged();
        }
    }
}
