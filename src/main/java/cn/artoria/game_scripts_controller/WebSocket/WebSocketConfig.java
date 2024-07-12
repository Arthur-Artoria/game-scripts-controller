package cn.artoria.game_scripts_controller.WebSocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    System.out.println("WebSocketConfig.registerWebSocketHandlers 注册");
    registry.addHandler(webSocketHandler(), "/websocket").setAllowedOriginPatterns("*").withSockJS();
  }
  
  @Bean
  public WebSocketHandler webSocketHandler() {
    return new Handler();
  }
}