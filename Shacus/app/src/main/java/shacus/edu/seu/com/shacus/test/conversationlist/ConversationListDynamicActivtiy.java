package shacus.edu.seu.com.shacus.test.conversationlist;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;


import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import shacus.edu.seu.com.shacus.R;

/**
 * Created by Bob on 15/9/11.
 */
public class ConversationListDynamicActivtiy extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rong_activity);

        ConversationListFragment fragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();
        fragment.setUri(uri);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //rong_content 为你要加载的 id
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();
    }
}
