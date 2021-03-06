/*
 * Copyright 2011 Leonard Axelsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xlson.standalonewar;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.webapp.WebAppContext;

import java.net.URL;
import java.security.ProtectionDomain;

/**
 * Starter for embedded Jetty. This class is intended to be packaged into a
 * war-file and set as the Main-Class in MANIFEST.MF. Running the war will
 * start a Jetty instance on port 8080 with the containing war loaded.
 * If you specify a port environment variable that will be used for Jetty
 * instead of 8080 (see example).
 * <p/>
 * The base of this class comes from this blogpost:
 * http://eclipsesource.com/blogs/2009/10/02/executable-wars-with-jetty/
 * <p/>
 * Example:
 * java -jar -Dport=80 webapp.war
 * <p/>
 * webapp.war is loaded on http://localhost:8080/
 *
 * @author Leonard Axelsson
 */
public class Starter {

  private static final int DEFAULT_PORT = 8080;

  public static void main(String[] args) throws Exception {
    Server server = new Server();
    SocketConnector connector = new SocketConnector();
    connector.setMaxIdleTime(1000 * 60 * 60);
    connector.setSoLingerTime(-1);
    connector.setPort(Integer.getInteger("port", DEFAULT_PORT));
    server.setConnectors(new Connector[]{connector});

    WebAppContext context = new WebAppContext();
    context.setServer(server);
    context.setContextPath(System.getProperty("app.context", "/"));

    ProtectionDomain protectionDomain = Starter.class.getProtectionDomain();
    URL location = protectionDomain.getCodeSource().getLocation();
    context.setWar(location.toExternalForm());

    server.setHandler(context);
    try {
      server.start();
      System.in.read();
      server.join();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(100);
    }

  }
}
