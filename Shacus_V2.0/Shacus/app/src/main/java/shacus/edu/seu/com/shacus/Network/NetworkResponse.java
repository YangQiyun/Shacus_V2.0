package shacus.edu.seu.com.shacus.Network;

import java.io.IOException;

/**
 * Created by Mind on 2017/9/2.
 */
public interface NetworkResponse {
    void onFailure(final IOException e);
}
