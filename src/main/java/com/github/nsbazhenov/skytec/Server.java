package com.github.nsbazhenov.skytec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.nsbazhenov.skytec.data.model.AddClanBalanceRq;
import com.github.nsbazhenov.skytec.data.model.Clan;
import com.github.nsbazhenov.skytec.data.model.Player;
import com.github.nsbazhenov.skytec.data.model.TakeClanGoldRq;
import com.github.nsbazhenov.skytec.service.ClanService;
import com.github.nsbazhenov.skytec.service.PlayerService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;

import static com.github.nsbazhenov.skytec.HttpUtils.queryToMap;
import static java.util.Objects.isNull;

public class Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final HttpServer httpServer;
    public Server(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    public static Server createServer(ClanService clanService, PlayerService playerService, int serverPort) throws IOException {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(serverPort), 0);
            httpServer.createContext("/clan", exchange -> getClanById(exchange, clanService));
            httpServer.createContext("/clan/add-gold", exchange -> addGold(exchange, clanService));
            httpServer.createContext("/clan/take-gold", exchange -> takeGold(exchange, clanService));
            httpServer.createContext("/player", exchange -> getPlayerById(exchange, playerService));
            httpServer.setExecutor(null);

            LOGGER.info("The server started on the port: {}", serverPort);
            return new Server(httpServer);
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(5);
    }


    private static void getClanById(HttpExchange exchange, ClanService clanService) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            Map<String, String> params = queryToMap(exchange.getRequestURI().getRawQuery());
            UUID clanId = UUID.fromString(params.get("id"));
            Clan clan = clanService.getById(clanId);

            LOGGER.debug("Request for detailed information about the clan, id - {}", clanId);

            ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
            String response = ow.writeValueAsString(clan);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            if (isNull(clan)) {
                //вернуть 404
            }            OutputStream output = exchange.getResponseBody();
            output.write(response.getBytes());
            output.flush();

            LOGGER.debug("Response for detailed information about the clan - {}", response);
        } else {
            exchange.sendResponseHeaders(405, -1);
            LOGGER.error("405 Method Not Allowed");
        }
        exchange.close();
    }

    private static void addGold(HttpExchange exchange, ClanService clanService) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            AddClanBalanceRq request = objectMapper.readValue(exchange.getRequestBody(), AddClanBalanceRq.class);
            Boolean result = clanService.addBalance(request);

            LOGGER.debug("Request to add the gold of the clan id - {}, by player - {}, amount - {}, result - {}",
                    request.getClanId(), request.getPlayerId(), request.getValue(), result);

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String response = ow.writeValueAsString(result);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(response.getBytes());
            output.flush();

            LOGGER.debug("Response to add the gold of the clan - {}", response);
        } else {
            exchange.sendResponseHeaders(405, -1);
            LOGGER.error("405 Method Not Allowed");
        }
        exchange.close();
    }

    private static void takeGold(HttpExchange exchange, ClanService clanService) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            TakeClanGoldRq request = objectMapper.readValue(exchange.getRequestBody(), TakeClanGoldRq.class);
            Boolean result = clanService.takeAwayBalance(request);

            LOGGER.debug("Request to reduce the gold of the clan - {}, from the player - {}, amount - {}, result - {}",
                    request.getClanId(), request.getPlayerId(), request.getValue(), result);

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String response = ow.writeValueAsString(result);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);

            OutputStream output = exchange.getResponseBody();
            output.write(response.getBytes());
            output.flush();

            LOGGER.debug("Response to reduce the gold of the clan - {}", response);
        } else {
            exchange.sendResponseHeaders(405, -1);
            LOGGER.error("405 Method Not Allowed");
        }
        exchange.close();
    }

    private static void getPlayerById(HttpExchange exchange, PlayerService playerService) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            Map<String, String> params = queryToMap(exchange.getRequestURI().getRawQuery());
            UUID playerId = UUID.fromString(params.get("id"));
            Player result = playerService.getById(playerId);
            LOGGER.debug("Request for detailed information about the player, id - {}", playerId);

            ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
            String response = ow.writeValueAsString(result);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(response.getBytes());
            output.flush();

            LOGGER.debug("Response for detailed information about the clan - {}", response);
        } else {
            exchange.sendResponseHeaders(405, -1);
            LOGGER.error("405 Method Not Allowed");
        }
        exchange.close();
    }
}
