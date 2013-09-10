package com.octo.appaloosasdk.webservices;

import java.util.List;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.util.Log;

import com.octo.appaloosasdk.model.ApplicationAuthorization;
import com.octo.appaloosasdk.model.ApplicationCheckUpdate;
import com.octo.appaloosasdk.model.DownloadUrl;

/**
 * Base class for Appaloosa Web services
 * 
 * @author Jerome Van Der Linden
 * @author Christopher Parola
 */
public class WebServices {

	// ============================================================================================
	// CONSTANTS
	// ============================================================================================
	/** base url of appaloosa web services */
	public static final String WEBSERVICES_BASE_URL = "https://www.appaloosa-store.com/";

	/** Timeout when calling a web service (in ms). */
	private static final int WEBSERVICES_TIMEOUT = 30000;

	// ============================================================================================
	// SINGLETON
	// ============================================================================================
	private static final WebServices instance = new WebServices();

	private static final String TAG_APPALOOSA = "APPALOOSA";

	public static WebServices getInstance() {
		return instance;
	}

	public static interface Urls {
		public static final String GET_APPLICATION_CHECK_FOR_UPDATE = "%1$d/mobile_application_updates/is_update_needed?token=%2$s&application_id=%3$s&device_id=%4$s&version=%5$d";
		public static final String GET_APPLICATION_BINARY = "%1$d/mobile_applications/%2$d/install?token=%3$s";
		public static final String GET_APPLICATION_AUTHORIZATIONS = "%1$d/mobile_application_updates/is_authorized?token=%2$s&application_id=%3$s&device_id=%4$s&version=%5$d&locale=%6$s";
	}

	// ============================================================================================
	// ATTRIBUTES
	// ============================================================================================
	private RestTemplate mRestTemplate;

	// ============================================================================================
	// CONSTRUCTOR
	// ============================================================================================
	private WebServices() {
		mRestTemplate = new RestTemplate();

		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setReadTimeout(WEBSERVICES_TIMEOUT);
		httpRequestFactory.setConnectTimeout(WEBSERVICES_TIMEOUT);
		mRestTemplate.setRequestFactory(httpRequestFactory);

		// web services support json responses
		MappingJacksonHttpMessageConverter jsonConverter = new MappingJacksonHttpMessageConverter();
		StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();

		final List<HttpMessageConverter<?>> listHttpMessageConverters = mRestTemplate.getMessageConverters();
		listHttpMessageConverters.add(jsonConverter);
		listHttpMessageConverters.add(stringHttpMessageConverter);
		mRestTemplate.setMessageConverters(listHttpMessageConverters);
	}

	// ============================================================================================
	// PUBLIC
	// ============================================================================================
	public ApplicationCheckUpdate getApplicationCheckForUpdate(String packageName, long storeId, String storeToken, String encryptedDeviceId, int versionCode) {
		String URL = WebServices.WEBSERVICES_BASE_URL + String.format(WebServices.Urls.GET_APPLICATION_CHECK_FOR_UPDATE, storeId, storeToken, packageName, encryptedDeviceId, versionCode);
		Log.d(TAG_APPALOOSA, "Retrieve application update information from " + URL);

		return mRestTemplate.getForObject(URL, ApplicationCheckUpdate.class);
	}

	public DownloadUrl getApplicationDownloadUrl(long storeId, long applicationId, String storeToken) {
		String URL = WebServices.WEBSERVICES_BASE_URL + String.format(WebServices.Urls.GET_APPLICATION_BINARY, storeId, applicationId, storeToken);
		Log.d(TAG_APPALOOSA, "Retrieve application binary url from " + URL);

		DownloadUrl app = mRestTemplate.getForObject(URL, DownloadUrl.class);
		return app;
	}
	
	public ApplicationAuthorization getApplicationAuthorizations(String packageName, int versionCode, long storeId, String storeToken, String encryptedImei, String locale) {
		String URL = WebServices.WEBSERVICES_BASE_URL + String.format(WebServices.Urls.GET_APPLICATION_AUTHORIZATIONS, storeId, storeToken, packageName, encryptedImei, versionCode, locale);
		Log.d(TAG_APPALOOSA, "Retrieve application authorization from " + URL);

		ApplicationAuthorization appAuthoriezation = mRestTemplate.getForObject(URL, ApplicationAuthorization.class);
		return appAuthoriezation;
	}
}
