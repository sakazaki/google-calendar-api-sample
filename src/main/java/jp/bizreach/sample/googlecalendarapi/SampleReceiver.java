package jp.bizreach.sample.googlecalendarapi;

import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;

import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;

public class SampleReceiver implements VerificationCodeReceiver {
	/** Lock on the code and error. */
	private final Lock lock = new ReentrantLock();

	/** Condition for receiving an authorization response. */
	final Condition gotAuthorizationResponse = lock.newCondition();

	/** Verification code or {@code null} for none. */
	private String code;

	/** Error code or {@code null} for none. */
	private String error;

	@Override
	public String getRedirectUri() throws IOException {
		return "http://localhost:8515/oauth2callback";
	}

	@Override
	public String waitForCode() throws IOException {
		lock.lock();
		try {
			while (code == null && error == null) {
				gotAuthorizationResponse.awaitUninterruptibly();
			}
			if (error != null) {
				throw new IOException("User authorization failed (" + error + ")");
			}
			return code;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void stop() throws IOException {
		// do nothing
	}

	public void receiveOauth2Callback(HttpServletRequest request) {
		lock.lock();
		try {
			error = request.getParameter("error");
			code = request.getParameter("code");
			gotAuthorizationResponse.signal();
		} finally {
			lock.unlock();
		}
	}
}
