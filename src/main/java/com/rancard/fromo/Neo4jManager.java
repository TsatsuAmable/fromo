package com.rancard.fromo;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import net.thisptr.jackson.jq.JsonQuery;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import net.thisptr.jackson.jq.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class Neo4jManager {
    private static Logger log = LoggerFactory.getLogger(Neo4jManager.class);

    public static List<JsonNode> runGraphQuery(String data) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl("http://localhost:7474/db/data/transaction/commit");
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String auth = "neo4j" + ":" + "password";
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            headers.set("Authorization", authHeader);

String body = data;
            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            String url = uriBuilder.build().toUriString();
            log.info(String.format("Calling: %s body: %s", url, body));
            ResponseEntity<String> responseEntity = restTemplate
                    .postForEntity(url, entity, String.class);
            if (responseEntity.getStatusCodeValue() == 200) {

                Object jsonResponse = responseEntity.getBody();
                log.info(String.format("Response from graph : %s", jsonResponse));

                JsonNode in = mapper.readTree((String) jsonResponse);
                JsonQuery jsonQuery = JsonQuery.compile(".data");
                log.info("Returning reuslts of JsonQuery "+ jsonQuery.apply(in));
                return jsonQuery.apply(in);

            }
        } catch (IOException | RestClientException e) {
            log.warn(String.format("Error occurred %s", e.getMessage()));
        }

        return new ArrayList<>();
    }


}
