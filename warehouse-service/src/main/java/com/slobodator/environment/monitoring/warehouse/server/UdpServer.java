package com.slobodator.environment.monitoring.warehouse.server;

import com.slobodator.environment.monitoring.warehouse.config.AppConfig;
import com.slobodator.environment.monitoring.warehouse.handler.SensorInputHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@SuppressWarnings("unused")
@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class UdpServer {
  AppConfig appConfig;
  SensorInputHandler sensorInputHandler;
  Environment environment;

  @SuppressWarnings("unused")
  @PostConstruct
  public void init() {
    log.info("Starting UDP server on port {}", appConfig.portsMapping());

    var connections = appConfig.portsMapping()
        .entrySet()
        .stream()
        .map(
            (e) -> {
              var sensorType = e.getKey();
              var port = e.getValue();
              log.info("Running the server '{}' on port '{}'", sensorType, port);
              return reactor.netty.udp.UdpServer.create()
                  .handle(
                      (in, out) ->
                          out.sendObject(
                              in.receiveObject()
                                  .flatMap(o -> {
                                        if (o instanceof DatagramPacket datagramPacket) {
                                          return sensorInputHandler
                                              .handle(datagramPacket, sensorType)
                                              .map(
                                                  str -> new DatagramPacket(
                                                      Unpooled.copiedBuffer(str + "\n", CharsetUtil.UTF_8),
                                                      datagramPacket.sender()
                                                  )
                                              );
                                        } else {
                                          return Mono.error(new Exception("Unexpected type of the message: " + o));
                                        }
                                      }
                                  )
                          )
                  )
                  .host("0.0.0.0")
                  .port(port)
                  .bindNow(Duration.ofSeconds(10));
            }
        )
        .toList();

    // a workaround to allow both production and test modes
    if (!environment.acceptsProfiles(Profiles.of("test"))) {
      // blocking the last one to keep the JVM running
      connections.getLast().onDispose().block();
    }
  }
}
