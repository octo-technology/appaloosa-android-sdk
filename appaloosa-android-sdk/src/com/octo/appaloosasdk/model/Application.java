package com.octo.appaloosasdk.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Application {

	private long id;

	private String name;

	@JsonProperty("application_id")
	private String packageName;

	private String version;

	@JsonProperty("binary_size")
	private long binarySize;

	@JsonProperty("min_os_version")
	private String minimumOSVersion;

	@JsonProperty("os_name")
	private String osName;

	/**
	 * 
	 * @return the id of the application on appaloosa
	 */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 
	 * @return the name of the application
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return the name of the package of the application (found in manifest)
	 */
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * 
	 * @return the versionCode of the application on Appaloosa (found in manifest)
	 */
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 
	 * @return the size of the apk binary file
	 */
	public long getBinarySize() {
		return binarySize;
	}

	public void setBinarySize(long binarySize) {
		this.binarySize = binarySize;
	}

	/**
	 * 
	 * @return the minimum os version of the application
	 */
	public String getMinimumOSVersion() {
		return minimumOSVersion;
	}

	public void setMinimumOSVersion(String minimumOSVersion) {
		this.minimumOSVersion = minimumOSVersion;
	}

	/**
	 * 
	 * @return the name of the os of the application (Should be "Android")
	 */
	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

}
