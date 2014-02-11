package tr.com.somecompany.someproject.lync;

import java.util.Date;

import org.codehaus.jackson.JsonNode;

public final class LyncAuthentication {

	private String accessToken;
	private Date tokenCreationTime;
	private Long expiresIn;
	private JsonNode response5JsonNode;

	public LyncAuthentication(String accessToken, Date tokenCreationTime, Long expiresIn) {
		super();
		this.accessToken = accessToken;
		this.tokenCreationTime = tokenCreationTime;
		this.expiresIn = expiresIn;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Date getTokenCreationTime() {
		return tokenCreationTime;
	}

	public void setTokenCreationTime(Date tokenCreationTime) {
		this.tokenCreationTime = tokenCreationTime;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public JsonNode getResponse5JsonNode() {
		return response5JsonNode;
	}

	public void setResponse5JsonNode(JsonNode response5JsonNode) {
		this.response5JsonNode = response5JsonNode;
	}

}
