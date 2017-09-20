package shacus.edu.seu.com.shacus.test.subconversationlist;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;


import io.rong.imkit.fragment.SubConversationListFragment;
import shacus.edu.seu.com.shacus.R;

/**
 * Created by Bob on 15/9/16.
 */
public class SubConversationListDynamicActivtiy extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rong_activity);


        SubConversationListFragment fragment = new SubConversationListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();
    }
}
