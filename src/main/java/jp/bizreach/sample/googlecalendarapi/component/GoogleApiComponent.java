package jp.bizreach.sample.googlecalendarapi.component;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import jp.bizreach.sample.googlecalendarapi.SampleReceiver;

import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;

@Component
public class GoogleApiComponent {

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	private HttpTransport HTTP_TRANSPORT;

	private SampleReceiver receiver;

	@PostConstruct
	public void init() throws IOException, GeneralSecurityException {
		HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	}

	public void getCalendar() throws IOException {
		Credential credential = authorize();
		Calendar client =
				new com.google.api.services.calendar.Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
						.setApplicationName("sample application").build();
		CalendarList calendarList = client.calendarList().list().execute();
		for (CalendarListEntry entry : calendarList.getItems()) {
			String calendarId = entry.getId();
			String calendarTitle = entry.getSummary();
			System.out.println("calendar id = " + calendarId);
			System.out.println("calendar title = " + calendarTitle);
		}
	}

	private Credential authorize() throws IOException {
		// 認証用インスタンスを作成
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, GoogleApiComponent.class.getResourceAsStream("/client_secrets.json"));

		if (clientSecrets.getDetails().getClientId().startsWith("Enter")
				|| clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
			throw new IllegalStateException();
		}

		// 認証成功情報を保管しておくファイル
		File credentialFile = new File("/tmp/credential.json");
		FileCredentialStore credentialStore =
				new FileCredentialStore(credentialFile, JSON_FACTORY);

		// 認証フロー用インスタンスを作成
		GoogleAuthorizationCodeFlow flow =
				new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
						Collections.singleton(CalendarScopes.CALENDAR)).setCredentialStore(credentialStore).build();

		receiver = new SampleReceiver();
		Credential credential =
				new AuthorizationCodeInstalledApp(flow, receiver).authorize("sampleuser");
		return credential;
	}

	public void receiveOauth2Callback(HttpServletRequest request) {
		receiver.receiveOauth2Callback(request);
	}

}
