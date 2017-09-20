package shacus.edu.seu.com.shacus.Network;

import org.json.JSONException;

/**
 * Created by Mind on 2017/9/2.
 */
public interface okHttpUtil_RawResponse extends NetworkResponse{
    void onResponse(String string)throws JSONException;

}
