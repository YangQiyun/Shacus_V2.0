package shacus.edu.seu.com.shacus.Utils;

/**
 * Created by Mind on 2017/9/2.
 */

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/*
Skip to content
        This repository
        Search
        Pull requests
        Issues
        Marketplace
        Explore
@YangQiyun
Sign out
        Watch 7
        Star 280
        Fork 64 AllenCoder/SuperUtils
        Code  Issues 0  Pull requests 0  Projects 0  Wiki Insights
        Branch: master Find file Copy pathSuperUtils/apputils/src/activity_main/java/com/allen/apputils/WeakRefHander.java
        5a42ed5  on 11 Apr
@AllenCoder AllenCoder 新增 弱引用handler 3des加密工具类
        1 contributor
        RawBlameHistory
        93 lines (78 sloc)  2.11 KB

 * Copyright  2017 [AllenCoderr]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 作者: allen on 15/11/24.
 */

/**
 * 弱引用 handler 防止内存泄露
 */
public class WeakRefHander extends Handler {

    private final WeakReference<Callback> mRef;
    private final int mLoopTime;
    private int NO_LOOP = -1;
    private int what =0;

    /**
     * 循环
     *
     * @param loopAction
     * @param loopTime
     */

    public WeakRefHander(Callback loopAction, int loopTime) {
        super();
        this.mRef = new WeakReference<>(loopAction);
        this.mLoopTime = loopTime;

    }

    /**
     * 不循环
     *
     * @param loopAction
     */
    public WeakRefHander(Callback loopAction) {
        super();
        mRef = new WeakReference<>(loopAction);
        mLoopTime = NO_LOOP;
    }

    @Override
    public void handleMessage(Message msg) {
        Callback action = mRef.get();
        if (action != null) {
            action.handleMessage(msg);
            if (mLoopTime != NO_LOOP) {
                sendEmptyMessageDelayed(what, mLoopTime);
            }
        }
    }

    public void start() {
        removeMessages(0);
        sendEmptyMessageDelayed(0, 0);
    }

    public void start(int what, long delay) {
        this.what = what;
        removeMessages(what);
        sendEmptyMessageDelayed(what, delay);
    }

    public void stop() {
        removeMessages(what);
    }

    public void clear() {
        removeMessages(what);
        mRef.clear();
    }
}
