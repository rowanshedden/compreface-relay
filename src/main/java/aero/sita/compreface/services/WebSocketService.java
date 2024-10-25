package aero.sita.compreface.services;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import aero.sita.compreface.configuration.WebSocketConfig;
import aero.sita.compreface.models.dto.CompreFaceFeederServiceAction;
import aero.sita.compreface.utils.MiscUtil;

@Component
@ConditionalOnBean(WebSocketConfig.class)
public class WebSocketService extends TextWebSocketHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

	public WebSocketService() {
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.info(String.format("WebSocket session: %s", session.toString()));
		sessions.add(session);
		super.afterConnectionEstablished(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		logger.info(String.format("WebSocket session: %s status: %s", session.toString(), status.toString()));
		sessions.remove(session);
		super.afterConnectionClosed(session, status);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) {
		if (sessions.isEmpty()) {
			logger.info("WebSocket sessions: none");
			return;
		}
		try {
			if (session != null) {
				logger.info(String.format("WebSocket receive: session id [%s], from [%s], message [%s]", session.getId(), session.getRemoteAddress(),
						message.getPayload()));
				TextMessage response = null;
				CompreFaceFeederServiceAction action = (CompreFaceFeederServiceAction) MiscUtil.fromJson(message.getPayload(), CompreFaceFeederServiceAction.class);
                assert action != null;
                if (action.getAction().equalsIgnoreCase(CompreFaceFeederServiceAction.FETCH)) {
					response = new TextMessage("TODO-fetch-something".getBytes());
					for (WebSocketSession webSocketSession : sessions) {
						if (session.getId().equals(webSocketSession.getId())) {
							webSocketSession.sendMessage(response);
							logger.info(String.format("WebSocket send: session id [%s], message [%s]", webSocketSession.getId(), message.getPayload()));
						}
					}
				}
			} else {
				for (WebSocketSession webSocketSession : sessions) {
					webSocketSession.sendMessage(message);
					logger.info(String.format("WebSocket send: session id [%s], message [%s]", webSocketSession.getId(), message.getPayload()));
				}
			}
		} catch (Exception e) {
			logger.error("WebSocket error: {}", e.getLocalizedMessage());
		}
	}

}