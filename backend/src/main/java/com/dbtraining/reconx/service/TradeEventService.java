package com.dbtraining.reconx.service;

import com.dbtraining.reconx.repository.entity.Trade;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class TradeEventService {

    private final List<SseEmitter> emittersList = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe() {

        SseEmitter emitter = new SseEmitter();

        emittersList.add(emitter);

        emitter.onCompletion(() -> emittersList.remove(emitter));

        emitter.onTimeout(() -> emittersList.remove(emitter));

        emitter.onError((ex) -> emittersList.remove(emitter));

        return emitter;
    }

    public void publishTrade(Trade trade) {

        List<SseEmitter> deadEmitters = new ArrayList<>();

        for (SseEmitter emitter : emittersList) {

            try {

                emitter.send(SseEmitter.event().name("trade").data(trade)
                );

            } catch (Exception ex) {

                deadEmitters.add(emitter);

            }
        }

        emittersList.removeAll(deadEmitters);
    }
}