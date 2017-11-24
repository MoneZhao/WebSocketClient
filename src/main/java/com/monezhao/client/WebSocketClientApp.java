package com.monezhao.client;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Mone Zhao.
 */
public class WebSocketClientApp {

  private static Logger logger = LoggerFactory.getLogger(WebSocketClientApp.class);

  public static void main(String[] args) {
    try {
      //focus on onMessage() method;
      WebSocketClient webSocketClient = new WebSocketClient(
          new URI("ws://10.1.5.100:4726/usrtable?appId=lianshidemo")
      ) {
        @Override
        public void onOpen(ServerHandshake serverHandshake) {
          logger.info("Connect to server: {}", getURI());
        }

        @Override
        public void onMessage(String s) {
          logger.info("Receive message: {}", s);
        }

        @Override
        public void onClose(int i, String s, boolean b) {
          logger.info("Connection close!");
        }

        @Override
        public void onError(Exception e) {
          logger.error(e.getMessage());
        }
      };

      webSocketClient.connectBlocking();

      //check if webSocketClient.getReadyState() is OPEN, which means connection available
      if (Objects.equals(webSocketClient.getReadyState(), WebSocket.READYSTATE.OPEN)) {
        webSocketClient.send("send message");
      } else {
        logger.info("Connection state: {}", webSocketClient.getReadyState());
      }

      TimeUnit.SECONDS.sleep(2);
      //connection close
      webSocketClient.close();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
