package tr.com.somecompany.someproject.lync;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

public final class LyncHttpOperationWrapper {

	private String requestBody;
	private String responseBody;
	private Header[] requestHeaders;
	private Header[] responseHeaders;
	private int statusCode;

	public LyncHttpOperationWrapper() {
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public Header[] getRequestHeaders() {
		return requestHeaders;
	}

	public void setRequestHeaders(Header[] requestHeaders) {
		this.requestHeaders = requestHeaders;
	}

	public Header[] getResponseHeaders() {
		return responseHeaders;
	}

	public void setResponseHeaders(Header[] responseHeaders) {
		this.responseHeaders = responseHeaders;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public Header[] getResponseHeaderGroup(String headerKeyword) {
		if (responseHeaders == null || responseHeaders.length == 0)
			return null;

		List<Header> retList = new ArrayList<Header>();
		for (Header header : responseHeaders) {
			if (header.getName().equalsIgnoreCase(headerKeyword)) {
				retList.add(header);
			}
		}
		return retList.toArray(new Header[retList.size()]);
	}
}
