package com.customwrld.bot.pigeon.payloads;

import com.customwrld.bot.util.Server;
import com.customwrld.pigeon.annotations.Payload;
import com.customwrld.pigeon.annotations.RequestConstructor;
import com.customwrld.pigeon.annotations.ResponseConstructor;
import com.customwrld.pigeon.annotations.Transmit;
import com.customwrld.pigeon.feedback.FeedbackState;
import com.customwrld.pigeon.feedback.RequiredState;
import com.customwrld.pigeon.payloads.bases.FeedbackPayload;
import com.sun.management.OperatingSystemMXBean;
import lombok.Getter;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;


@Getter @Payload
public class AtomStatsRequestPayload extends FeedbackPayload {

    public AtomStatsRequestPayload() {
        this.payloadId = "atom-atomStatsRequest";
        this.payloadTimeout = 10000;
    }

    //Request

    @RequestConstructor
    public AtomStatsRequestPayload(Consumer<AtomStatsRequestPayload> feedback) {
        this();
        this.payloadState = FeedbackState.REQUEST;
        this.feedbackID = UUID.randomUUID();

        this.feedback = feedback;
    }

    //Response

    @Transmit(direction = RequiredState.RESPONSE) Long latency;
    @Transmit(direction = RequiredState.RESPONSE) Long uptime;
    @Transmit(direction = RequiredState.RESPONSE) Long usedMemory;
    @Transmit(direction = RequiredState.RESPONSE) Double usedCpu;
    @Transmit(direction = RequiredState.RESPONSE) List<Server> servers;

    @Override
    public void receive() {}

}