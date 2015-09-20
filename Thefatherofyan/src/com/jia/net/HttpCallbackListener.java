package com.jia.net;

public interface HttpCallbackListener {

	void onFinish(String response);

	void onError(Exception e);
}
