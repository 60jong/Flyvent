package com.app.flyvent.service;

import com.app.flyvent.domain.Event;
import com.app.flyvent.domain.EventDestination;
import com.app.flyvent.domain.EventType;
import com.app.flyvent.domain.airline.Airline;
import com.app.flyvent.domain.destination.Destination;
import com.app.flyvent.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {
    private final EventRepository eventRepository;
    private final AirlineService airlineService;
    private final DestinationService destinationService;
    private final MailService mailService;

    public void save(Event event) {
        eventRepository.save(event);
    }

    public int crawl(Long airlineId) {
        Airline airline = airlineService.findById(airlineId);

        List<Event> events = Collections.emptyList();

        try {
            events = airline.crawlEvents();
        } catch (InterruptedException e) {
            // Crawl 시에 Thread.sleep() 예외 처리
            e.printStackTrace();
        }
        return saveNew(events);
    }

    public int saveNew(List<Event> events) {
        int newEvents = 0;
        for (Event event : events) {
            if (this.has(event)) {
                continue;
            }
            setEventType(event);

            if (event.isTypeOf(EventType.TRAVEL)) {
                List<Destination> destinations = destinationService.findDestinationsIn(event.getText());

                destinations.stream()
                        .forEach(destination -> new EventDestination(event, destination));
            }
            save(event);
            mailService.sendMail(event);
            newEvents++;
        }

        return newEvents;
    }

    public boolean has(Event event) {
        if (eventRepository.existsByUrl(event.getUrl())) {
            return true;
        }

        return false;
    }

    public void setEventType(Event event) {
        EventType eventType = EventType.ETC;

        if (destinationService.canFindDestinationIn(event.getText())) {
            eventType = EventType.TRAVEL;
        }

        event.setEventType(eventType);
    }
}
