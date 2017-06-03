package com.csjbot.rosclient.core.inter;

/**
 * Copyright (c) 2016, SuZhou CsjBot. All Rights Reserved. <br/>
 * www.csjbot.com<br/>
 * <p>
 * Created by 浦耀宗 at 2016/11/07 0007-19:19.<br/>
 * Email: puyz@csjbot.com<br/>
 */
public interface ActionListener {
    public void onSuccess();

    public void onFailed(int errorCode);
}
