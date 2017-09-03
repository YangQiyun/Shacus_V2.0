package shacus.edu.seu.com.shacus.Network;

import org.json.JSONObject;

/**
 * Created by Mind on 2017/9/2.
 */
public interface okHttpUtil_JsonResponse extends NetworkResponse {
    void onResponse(JSONObject jsonObject);
}
