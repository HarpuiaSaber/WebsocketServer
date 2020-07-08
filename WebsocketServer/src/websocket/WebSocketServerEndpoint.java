package websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
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
import utils.DateTimeUtils;

@ServerEndpoint(value = "/server")
public class WebSocketServerEndpoint {
	static Set<Session> users = Collections.synchronizedSet(new HashSet<Session>());

	private DecisionTreeService dService = new DecisionTreeServiceImpl();
	private boolean auto = false;

	@OnOpen
	public void handleOpen(Session session) {
		users.add(session);
	}

	@OnMessage
	public void handleMessage(String message, Session userSession) throws IOException {
		int sendData = 0;
		if (message.equals("on")) {
			sendData = 1;
		} else if (message.equals("auto")) {
			auto = true;
		} else {
			sendData = 0;
		}
		if (auto) {
			String[] data = message.split(",");
			sendData = dService.makeDecision(Float.parseFloat(data[0]), Float.parseFloat(data[1]));
		}
		for (Session session : users) {
			if (!session.getId().equals(userSession.getId())) {
				session.getBasicRemote().sendText(message);
			}
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
