package tr.com.somecompany.someproject.lync.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import tr.com.somecompany.someproject.lync.LyncClient;
import tr.com.somecompany.someproject.lync.LyncConstants;

@WebServlet(value = "/lyncGateway", loadOnStartup = 1)
public class LyncGatewayServlet extends HttpServlet {

	private static final long serialVersionUID = 8369129717173142132L;
	private static LyncClient lyncClient;
	private static ObjectMapper mapper;

	private static final String QUERY_PARAM_SUBJECT = "subject";
	private static final String QUERY_PARAM_MESSAGE = "message";
	private static final String QUERY_PARAM_SIP_FOR_PRESENCE = "sipForPresence";
	private static final String QUERY_PARAM_SIP_FOR_SEARCH = "sipForSearch";
	private static final String QUERY_PARAM_SIP_FOR_CONTACT_NOTE = "sipForContactNote";
	private static final String QUERY_PARAM_SIP_FOR_CONTACT_PHOTO = "sipForContactPhoto";

	static {
		lyncClient = new LyncClient();
		mapper = new ObjectMapper();
		lyncClient.preapreClient();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> responseMap = new HashMap<String, String>();
		String subject = request.getParameter(QUERY_PARAM_SUBJECT);
		String message = request.getParameter(QUERY_PARAM_MESSAGE);

		// start messaging
		if (!StringUtils.isEmpty(subject) && !StringUtils.isEmpty(message))
			startMessaging(subject, message, response, responseMap);

		// query presence
		String sipForPresence = request.getParameter(QUERY_PARAM_SIP_FOR_PRESENCE);
		if (!StringUtils.isEmpty(sipForPresence)) {
			queryPresence(sipForPresence, response, responseMap);
		}

		// query contact note
		String sipForContactNote = request.getParameter(QUERY_PARAM_SIP_FOR_CONTACT_NOTE);
		if (!StringUtils.isEmpty(sipForContactNote)) {
			queryContactNote(sipForContactNote, response, responseMap);
		}

		// query contact photo
		Map<String, Object> responseMapContactPhoto = new HashMap<String, Object>();
		String sipForContactPhoto = request.getParameter(QUERY_PARAM_SIP_FOR_CONTACT_PHOTO);
		if (!StringUtils.isEmpty(sipForContactPhoto)) {
			queryContactPhoto(sipForContactPhoto, response, responseMapContactPhoto);
		}
	}

	public void startMessaging(String subject, String message, HttpServletResponse response, Map<String, String> responseMap)
			throws ServletException, IOException {
		response.setContentType("application/json");
		try {
			if (!lyncClient.peekAuthenticationMap()) {
				String clientId = lyncClient.createApplication();
				System.out.println("clientId: " + clientId);
				responseMap = tryToSendMessage(subject, message);
			} else {
				synchronized (lyncClient) {
					responseMap = tryToSendMessage(subject, message);
					if (Integer.valueOf(responseMap.get("ResponseCode")) != LyncConstants.HTTP_RESPONSE_CODE_CREATED) {
						lyncClient.removeTimedOutToken();
						lyncClient.createApplication();
						responseMap = tryToSendMessage(subject, message);
					}
				}
			}
			mapper.writeValue(response.getOutputStream(), responseMap);
		} catch (Exception e) {
			responseMap.clear();
			responseMap.put("Error", e.getMessage());
			mapper.writeValue(response.getOutputStream(), responseMap);
		}
	}

	public void queryPresence(String sip, HttpServletResponse response, Map<String, String> responseMap) throws JsonGenerationException,
			JsonMappingException, IOException {
		response.setContentType("application/json");
		try {
			if (!lyncClient.peekAuthenticationMap()) {
				String clientId = lyncClient.createApplication();
				System.out.println("clientId: " + clientId);
				responseMap = tryToQueryPresence(responseMap, sip);
			} else {
				synchronized (lyncClient) {
					responseMap = tryToQueryPresence(responseMap, sip);
					if (Integer.valueOf(responseMap.get("ResponseCode")) != LyncConstants.HTTP_RESPONSE_CODE_OK) {
						lyncClient.removeTimedOutToken();
						lyncClient.createApplication();
						responseMap = tryToQueryPresence(responseMap, sip);
					}
				}
			}
			mapper.writeValue(response.getOutputStream(), responseMap);
		} catch (Exception e) {
			responseMap.clear();
			responseMap.put("Error", e.getMessage());
			mapper.writeValue(response.getOutputStream(), responseMap);
		}
	}

	public void queryContactNote(String sip, HttpServletResponse response, Map<String, String> responseMap) throws JsonGenerationException,
			JsonMappingException, IOException {
		response.setContentType("application/json");
		try {
			if (!lyncClient.peekAuthenticationMap()) {
				String clientId = lyncClient.createApplication();
				System.out.println("clientId: " + clientId);
				responseMap = tryToQueryContactNote(responseMap, sip);
			} else {
				synchronized (lyncClient) {
					responseMap = tryToQueryContactNote(responseMap, sip);
					if (Integer.valueOf(responseMap.get("ResponseCode")) != LyncConstants.HTTP_RESPONSE_CODE_OK) {
						lyncClient.removeTimedOutToken();
						lyncClient.createApplication();
						responseMap = tryToQueryContactNote(responseMap, sip);
					}
				}
			}
			mapper.writeValue(response.getOutputStream(), responseMap);
		} catch (Exception e) {
			responseMap.clear();
			responseMap.put("Error", e.getMessage());
			mapper.writeValue(response.getOutputStream(), responseMap);
		}
	}

	public void queryContactPhoto(String sip, HttpServletResponse response, Map<String, Object> responseMap) throws JsonGenerationException,
			JsonMappingException, IOException {
		response.setContentType("image/jpeg");
		try {
			if (!lyncClient.peekAuthenticationMap()) {
				String clientId = lyncClient.createApplication();
				System.out.println("clientId: " + clientId);
				responseMap = tryToQueryContactPhoto(responseMap, sip);
			} else {
				synchronized (lyncClient) {
					responseMap = tryToQueryContactPhoto(responseMap, sip);
					if (Integer.valueOf(responseMap.get("ResponseCode").toString()) != LyncConstants.HTTP_RESPONSE_CODE_OK) {
						lyncClient.removeTimedOutToken();
						lyncClient.createApplication();
						responseMap = tryToQueryContactPhoto(responseMap, sip);
					}
				}
			}
			// mapper.writeValue(response.getOutputStream(), responseMap);
			response.setHeader("Content-Type", "image/jpeg");
			// response.setHeader("Content-Length",
			// String.valueOf(image.getLength()));
			// response.setHeader("Content-Disposition", "inline; filename=\"" +
			// image.getFilename() + "\"");

			BufferedInputStream input = null;
			BufferedOutputStream output = null;

			try {
				input = new BufferedInputStream((InputStream) responseMap.get("ContactPhoto"));
				output = new BufferedOutputStream(response.getOutputStream());
				byte[] buffer = new byte[8192];
				for (int length = 0; (length = input.read(buffer)) > 0;) {
					output.write(buffer, 0, length);
				}
			} finally {
				if (output != null)
					try {
						output.close();
					} catch (IOException logOrIgnore) {
					}
				if (input != null)
					try {
						input.close();
					} catch (IOException logOrIgnore) {
					}
			}
		} catch (Exception e) {
			responseMap.clear();
			responseMap.put("Error", e.getMessage());
			mapper.writeValue(response.getOutputStream(), responseMap);
		}
	}

	public Map<String, String> tryToSendMessage(String subject, String message) {
		int responseCode = 500;
		responseCode = lyncClient.sendMessage(subject, message);
		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("Subject", subject);
		responseMap.put("Message", message);
		responseMap.put("ResponseCode", String.valueOf(responseCode));
		return responseMap;
	}

	public Map<String, String> tryToQueryPresence(Map<String, String> responseMap, String sip) {
		JsonNode responseSearchJsonNode = null;
		String presenceText = "Unknown";
		try {
			responseSearchJsonNode = lyncClient.doSearchRequest(sip);
			presenceText = lyncClient.doPresenceRequest(responseSearchJsonNode);
			responseMap.put("Sip", sip);
			responseMap.put("Presence", presenceText);
			responseMap.put("ResponseCode", "200");
		} catch (Exception e) {
			responseMap.put("ResponseCode", "500");
			e.printStackTrace();
		}
		return responseMap;
	}

	public Map<String, String> tryToQueryContactNote(Map<String, String> responseMap, String sip) {
		JsonNode responseSearchJsonNode = null;
		String contactNote = "Unknown";
		try {
			responseSearchJsonNode = lyncClient.doSearchRequest(sip);
			contactNote = lyncClient.doContactNoteRequest(responseSearchJsonNode);
			responseMap.put("Sip", sip);
			responseMap.put("ContactNote", contactNote);
			responseMap.put("ResponseCode", "200");
		} catch (Exception e) {
			responseMap.put("ResponseCode", "500");
			e.printStackTrace();
		}
		return responseMap;
	}

	public Map<String, Object> tryToQueryContactPhoto(Map<String, Object> responseMap, String sip) {
		JsonNode responseSearchJsonNode = null;
		InputStream contactPhotoStream = null;
		try {
			responseSearchJsonNode = lyncClient.doSearchRequest(sip);
			contactPhotoStream = lyncClient.doContactPhotoRequest(responseSearchJsonNode);
			responseMap.put("Sip", sip);
			responseMap.put("ContactPhoto", contactPhotoStream);
			responseMap.put("ResponseCode", "200");
		} catch (Exception e) {
			responseMap.put("ResponseCode", "500");
			e.printStackTrace();
		}
		return responseMap;
	}

	@Override
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		super.doPost(arg0, arg1);
	}

}
