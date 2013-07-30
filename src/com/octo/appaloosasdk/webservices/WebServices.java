package com.octo.appaloosasdk.webservices;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.util.Log;

import com.octo.appaloosasdk.model.Application;
import com.octo.appaloosasdk.model.ApplicationAuthorization;
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
	//public static final String WEBSERVICES_BASE_URL = "https://www.appaloosa-store.com/";
	public static final String WEBSERVICES_BASE_URL = "http://appaloosa-int.herokuapp.com/";

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

	/**
	 * 
	 */
	public static interface Urls {
		public static final String GET_APPLICATION_INFORMATION = "%1$d/mobile_applications/%2$s.json?token=%3$s";
		public static final String GET_APPLICATION_BINARY = "%1$d/mobile_applications/%2$d/install?token=%3$s";
		public static final String GET_APPLICATION_AUTHORIZATIONS = "%1$d/mobile_applications/authorized?token=%2$s&package=%3$s&imei=%4$s&version=%5$d";
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
	public Application getApplicationInformation(String packageName, long storeId, String storeToken) {
		String URL = WEBSERVICES_BASE_URL + String.format(Urls.GET_APPLICATION_INFORMATION, storeId, packageName.replaceAll("\\.", "%2E"), storeToken);
		Log.d(TAG_APPALOOSA, "Retrieve application informations from " + URL);
		try {
			return mRestTemplate.getForObject(new URI(URL), Application.class);
		}
		catch (URISyntaxException e) {
			Log.e(TAG_APPALOOSA, "Bad URL " + URL, e);
		}
		return null;
	}

	public DownloadUrl getApplicationDownloadUrl(long storeId, long applicationId, String storeToken) {
		String URL = WebServices.WEBSERVICES_BASE_URL + String.format(WebServices.Urls.GET_APPLICATION_BINARY, storeId, applicationId, storeToken);
		Log.d(TAG_APPALOOSA, "Retrieve application binary url from " + URL);

		DownloadUrl app = mRestTemplate.getForObject(URL, DownloadUrl.class);
		return app;
	}
	
	public ApplicationAuthorization getApplicationAuthorizations(String packageName, int versionCode, long storeId, String storeToken, String imei) {
		
		String URL = WebServices.WEBSERVICES_BASE_URL + String.format(WebServices.Urls.GET_APPLICATION_AUTHORIZATIONS, storeId, storeToken, packageName, imei, versionCode);
		Log.d(TAG_APPALOOSA, "Retrieve application authorization from " + URL);

		ApplicationAuthorization appAuthoriezation = mRestTemplate.getForObject(URL, ApplicationAuthorization.class);
		return appAuthoriezation;
	}
}
