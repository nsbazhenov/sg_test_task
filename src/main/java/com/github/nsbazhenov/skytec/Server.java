package com.github.nsbazhenov.skytec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.nsbazhenov.skytec.data.transfer.AddClanBalanceRq;
import com.github.nsbazhenov.skytec.data.model.Clan;
import com.github.nsbazhenov.skytec.data.model.Player;
import com.github.nsbazhenov.skytec.data.transfer.TakeClanGoldRq;
import com.github.nsbazhenov.skytec.data.status.HttpStatusCode;
import com.github.nsbazhenov.skytec.service.ClanService;
import com.github.nsbazhenov.skytec.service.PlayerService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import static com.github.nsbazhenov.skytec.utils.HttpUtils.queryParamsToMap;
import static java.util.Objects.nonNull;

/**
 * Http server operation interface.
 *
 * @author Bazhenov Nikita
 *
 */
public class Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectWriter OBJECT_WRITER = OBJECT_MAPPER.writer().withDefaultPrettyPrinter();
    private final HttpServer httpServer;

    public Server(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    /**
     * Creating the http server.
     */
    public static Server createServer(ClanService clanService, PlayerService playerService, int serverPort) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(serverPort), 0);
        httpServer.createContext("/clan", exchange -> getClanById(exchange, clanService));
        httpServer.createContext("/clans", exchange -> getAllClans(exchange, clanService));
        httpServer.createContext("/clan/add-gold", exchange -> addGold(exchange, clanService));
        httpServer.createContext("/clan/take-gold", exchange -> takeGold(exchange, clanService));
        httpServer.createContext("/player", exchange -> getPlayerById(exchange, playerService));
        httpServer.setExecutor(null);

        LOGGER.info("The server started on the port: {}", serverPort);
        return new Server(httpServer);
    }

    /**
     * Open the http server.
     */
    public void start() {
        httpServer.start();
    }

    /**
     * Closed the http server.
     */
    public void stop() {
        httpServer.stop(5);
    }

    /**
     * Processing method of getting the clan by ID.
     */
    private static void getClanById(HttpExchange exchange, ClanService clanService) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            Map<String, String> params = queryParamsToMap(exchange.getRequestURI().getRawQuery());
            long clanId = Long.parseLong(params.get("id"));
            Clan clan = clanService.getById(clanId);

            LOGGER.debug("Request for detailed information about the clan, id - {}", clanId);

            String response = OBJECT_WRITER.writeValueAsString(clan);

            if (nonNull(clan)) {
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(HttpStatusCode.OK.getValue(), response.getBytes().length);
            } else {
                exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.getValue(), -1);
                LOGGER.error(HttpStatusCode.NOT_FOUND.toString());
            }

            OutputStream output = exchange.getResponseBody();
            output.write(response.getBytes());
            output.flush();

            LOGGER.debug("Response for detailed information about the clan - {}", response);
        } else {
            exchange.sendResponseHeaders(HttpStatusCode.METHOD_NOT_ALLOWED.getValue(), -1);
            LOGGER.error(HttpStatusCode.METHOD_NOT_ALLOWED.toString());
        }
        exchange.close();
    }

    /**
     * Processing method of getting clans.
     */
    private static void getAllClans(HttpExchange exchange, ClanService clanService) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            List<Clan> clans = clanService.getAll();

            LOGGER.debug("Request for all clans");

            String response = OBJECT_WRITER.writeValueAsString(clans);

            if (nonNull(clans)) {
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(HttpStatusCode.OK.getValue(), response.getBytes().length);
            } else {
                exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.getValue(), -1);
                LOGGER.error(HttpStatusCode.NOT_FOUND.toString());
            }

            OutputStream output = exchange.getResponseBody();
            output.write(response.getBytes());
            output.flush();

            LOGGER.debug("Request for all clans, {}", response);
        } else {
            exchange.sendResponseHeaders(HttpStatusCode.METHOD_NOT_ALLOWED.getValue(), -1);
            LOGGER.error(HttpStatusCode.METHOD_NOT_ALLOWED.toString());
        }
        exchange.close();
    }

    /**
     * Processing method of adding gold to the clan.
     */
    private static void addGold(HttpExchange exchange, ClanService clanService) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            AddClanBalanceRq request = OBJECT_MAPPER.readValue(exchange.getRequestBody(), AddClanBalanceRq.class);
            Boolean result = clanService.addBalance(request);

            LOGGER.debug("Request to add the gold of the clan id - {}, by player - {}, amount - {}, result - {}",
                    request.getClanId(), request.getPlayerId(), request.getValue(), result);

            String response = OBJECT_WRITER.writeValueAsString(result);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.getValue(), response.getBytes().length);

            OutputStream output = exchange.getResponseBody();
            output.write(response.getBytes());
            output.flush();

            LOGGER.debug("Response to add the gold of the clan - {}", response);
        } else {
            exchange.sendResponseHeaders(HttpStatusCode.METHOD_NOT_ALLOWED.getValue(), -1);
            LOGGER.error(HttpStatusCode.METHOD_NOT_ALLOWED.toString());
        }
        exchange.close();
    }

    /**
     * Method of processing the reduction of gold to the clan.
     */
    private static void takeGold(HttpExchange exchange, ClanService clanService) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            TakeClanGoldRq request = OBJECT_MAPPER.readValue(exchange.getRequestBody(), TakeClanGoldRq.class);
            Boolean result = clanService.takeAwayBalance(request);

            LOGGER.debug("Request to reduce the gold of the clan - {}, from the player - {}, amount - {}, result - {}",
                    request.getClanId(), request.getPlayerId(), request.getValue(), result);

            String response = OBJECT_WRITER.writeValueAsString(result);

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.getValue(), response.getBytes().length);

            OutputStream output = exchange.getResponseBody();
            output.write(response.getBytes());
            output.flush();

            LOGGER.debug("Response to reduce the gold of the clan - {}", result);
        } else {
            exchange.sendResponseHeaders(HttpStatusCode.METHOD_NOT_ALLOWED.getValue(), -1);
            LOGGER.error(HttpStatusCode.METHOD_NOT_ALLOWED.toString());
        }
        exchange.close();
    }

    /**
     * Processing method of getting the player by ID.
     */
    private static void getPlayerById(HttpExchange exchange, PlayerService playerService) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            Map<String, String> params = queryParamsToMap(exchange.getRequestURI().getRawQuery());
            long playerId = Long.parseLong(params.get("id"));

            LOGGER.debug("Request for detailed information about the player, id - {}", playerId);

            Player player = playerService.getById(playerId);

            String response = OBJECT_WRITER.writeValueAsString(player);

            if (nonNull(player)) {
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(HttpStatusCode.OK.getValue(), response.getBytes().length);
            } else {
                exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.getValue(), -1);
                LOGGER.error(HttpStatusCode.NOT_FOUND.toString());
            }

            OutputStream output = exchange.getResponseBody();
            output.write(response.getBytes());
            output.flush();

            LOGGER.debug("Response for detailed information about the clan - {}", response);
        } else {
            exchange.sendResponseHeaders(HttpStatusCode.METHOD_NOT_ALLOWED.getValue(), -1);
            LOGGER.error(HttpStatusCode.METHOD_NOT_ALLOWED.toString());
        }
        exchange.close();
    }
}
