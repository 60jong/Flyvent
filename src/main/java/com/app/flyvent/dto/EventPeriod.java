package com.app.flyvent.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class EventPeriod {
    private LocalDate startDate;
    private LocalDate endDate;
}
