package com.slobodator.environment.monitoring.warehouse.util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class UdpClient {
  int port;

  public String sendMessage(String msg) throws Exception {
    try (DatagramSocket socket = new DatagramSocket()) {
      InetAddress address = InetAddress.getByName("localhost");

      byte[] sendData = msg.getBytes();
      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
      socket.send(sendPacket);

      byte[] receiveData = new byte[1024];
      DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
      socket.receive(receivePacket);
      return new String(receivePacket.getData()).trim();
    }
  }
}