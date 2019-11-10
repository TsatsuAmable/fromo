package com.rancard.fromo.controllers;

import com.rancard.fromo.Neo4jManager;

import com.rancard.fromo.models.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/transfers")
public class TransferController {

    private static final Logger log = LoggerFactory.getLogger(TransferController.class);

    @RequestMapping(value = "/log/transfer", method = RequestMethod.GET)
    public ResponseEntity<Transfer> getTransfer(@RequestParam(value = "currency", defaultValue = "GHS") String currency,
                                                @RequestParam(value = "amount") String amount,
                                                @RequestParam(value = "sender") String sender,
                                                @RequestParam(value = "receiver") String receiver
    ) {
        log.info("***************Running get transfer controller/n");
        Transfer transfer = new Transfer(currency, amount, sender, receiver);
        return new ResponseEntity<>(transfer, HttpStatus.OK);
    }

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            value = "/log/transfer")
    public ResponseEntity<Transfer> logTransfer(@RequestBody Transfer transfer) {

        String query = "{\n"

                + "  \"statements\" : [ {\n"
                + "   \"statement\": \"MERGE (sender:Msisdn {msisdnId:'@@sender_msisdnId@@'})"
                + " MERGE (transfer:Transfer {transferId:'@@transferId@@',amount:'@@amount@@',currency:'@@currency@@' })"
                + " ON CREATE SET transfer.logDate=timestamp()"
                + " MERGE (sender)-[sends:SENDS]->(transfer) ON CREATE SET sends.logDate=timestamp()"
                + " MERGE (receiver:Msisdn {msisdnId:'@@receiver_msisdnId@@'})"
                + " MERGE (receiver)-[receives:RECEIVES]->(transfer) ON CREATE SET receives.logDate=timestamp()"
                + " RETURN sender,transfer, receiver\"\n"
                + "  } ]\n"
                + "}";

        query = query
                .replaceAll("@@sender_msisdnId@@", String.valueOf(transfer.getSender()))
                .replaceAll("@@transferId@@", String.valueOf(new Transfer().getId()))
                .replaceAll("@@amount@@", String.valueOf(transfer.getAmount()))
                .replaceAll("@@receiver_msisdnId@@", String.valueOf(transfer.getReceiver()))
                .replaceAll("@@currency@@", String.valueOf(transfer.getCurrency()));

        log.info("++++++++++++Transfer Id: " + new Transfer().getId());
        log.info("CYPHER QUERY: " + query);

        log.info("***************Running post transfer controller/n");
        if (!StringUtils.isEmpty(query)) {
            Neo4jManager.runGraphQuery(query);
        }
        return new ResponseEntity<>(transfer, HttpStatus.OK);
    }
}
