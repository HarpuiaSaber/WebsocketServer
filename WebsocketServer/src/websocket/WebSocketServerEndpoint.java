package websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import service.DecisionTreeService;
import service.impl.DecisionTreeServiceImpl;

@ServerEndpoint(value = "/server")
public class WebSocketServerEndpoint {
	static Set<Session> users = Collections.synchronizedSet(new HashSet<Session>());

	private Session esp8266;

	private DecisionTreeService dService = new DecisionTreeServiceImpl();
	private boolean auto = false;

	@OnOpen
	public void handleOpen(Session session) {
		users.add(session);
	}

	@OnMessage
	public void handleMessage(String message, Session userSession) throws IOException {
		if (esp8266 == null) {
			if (message.equals("ESP8266")) {
				esp8266 = userSession;
			}
		}
		if (userSession.getId() == esp8266.getId()) {
			for (Session session : users) {
				if (!session.getId().equals(esp8266.getId())) {
					session.getBasicRemote().sendText(message);
				}
			}
			if (auto) {
				String[] data = message.split(",");
				String control = String.valueOf(dService.makeDecision(Float.parseFloat(data[0]), Float.parseFloat(data[1])));
				esp8266.getBasicRemote().sendText(control);
			}			
		} else {
			if (message.equals("2")) {
				auto = true;
			}
			esp8266.getBasicRemote().sendText(message);
		}
	}

	@OnClose
	public void handleClose(Session session) {
		users.remove(session);
	}

	@OnError
	public void handleError(Throwable t) {
		t.printStackTrace();
	}
}
