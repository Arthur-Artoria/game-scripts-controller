package cn.artoria.game_scripts_controller.WebSocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class Handler extends TextWebSocketHandler {
  @Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		System.out.println("收到消息：" + message.getPayload());
	}
}
