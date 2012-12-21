package com.octo.appaloosasdk.async;

import java.util.List;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Application;
import android.app.Service;

import com.octo.android.robospice.SpringAndroidSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.binary.InFileBigInputStreamObjectPersister;
import com.octo.android.robospice.persistence.json.jackson.JacksonObjectPersisterFactory;

/**
 * Robospice {@link Service} used to call remote web services with Spring Android<br/>
 * Define a {@link RestTemplate} with json capabilities and cache manager with json and binary support (even if the sdk does not cache informations)
 * 
 * @author Jerome Van Der Linden
 * 
 */
public class AppaloosaSpiceService extends SpringAndroidSpiceService {

	/** Timeout when calling a web service (in ms). */
	private static final int WEBSERVICES_TIMEOUT = 30000;

	@Override
	public RestTemplate createRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();

		MappingJacksonHttpMessageConverter jsonConverter = new MappingJacksonHttpMessageConverter();
		FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
		StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();

		final List<HttpMessageConverter<?>> listHttpMessageConverters = restTemplate.getMessageConverters();
		listHttpMessageConverters.add(jsonConverter);
		listHttpMessageConverters.add(formHttpMessageConverter);
		listHttpMessageConverters.add(stringHttpMessageConverter);

		restTemplate.setMessageConverters(listHttpMessageConverters);

		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setReadTimeout(WEBSERVICES_TIMEOUT);
		httpRequestFactory.setConnectTimeout(WEBSERVICES_TIMEOUT);
		restTemplate.setRequestFactory(httpRequestFactory);

		return restTemplate;
	}

	@Override
	public CacheManager createCacheManager(Application application) {
		CacheManager cacheManager = new CacheManager();

		JacksonObjectPersisterFactory inJSonFileObjectPersisterFactory = new JacksonObjectPersisterFactory(application);
		inJSonFileObjectPersisterFactory.setAsyncSaveEnabled(true);
		cacheManager.addPersister(inJSonFileObjectPersisterFactory);

		InFileBigInputStreamObjectPersister inFileInputStreamObjectPersister = new InFileBigInputStreamObjectPersister(application);
		cacheManager.addPersister(inFileInputStreamObjectPersister);

		return cacheManager;
	}

}
