package shacus.edu.seu.com.shacus.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import shacus.edu.seu.com.shacus.R;


/**
 * Created by Bob on 15/8/18.
 * 聚合会话列表
 * 什么是聚合会话列表？
 */
public class SubConversationListActivtiy extends FragmentActivity {

    private TextView mTitle;
    private RelativeLayout mBack;
    /**
     * 聚合类型
     */
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subconversationlist);

        mTitle = (TextView) findViewById(R.id.txt1);
        mBack = (RelativeLayout) findViewById(R.id.back);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getActionBarTitle();
    }

    /**
     * 通过 intent 中的数据，得到当前的 targetId 和 type
     */
    private void getActionBarTitle() {

        Intent intent = getIntent();

        type = intent.getData().getQueryParameter("type");

        if (type.equals("group")) {
            mTitle.setText("聚合群组");
        } else if (type.equals("private")) {
            mTitle.setText("聚合单聊");
        } else if (type.equals("discussion")) {
            mTitle.setText("聚合讨论组");
        } else if (type.equals("system")) {
            mTitle.setText("聚合系统会话");
        } else {
            mTitle.setText("聚合");
        }

    }
}

