package com.app.flyvent.controller;

import com.app.flyvent.domain.airline.Airline;
import com.app.flyvent.dto.web.NewAirlineParam;
import com.app.flyvent.dto.web.NewAirlineRes;
import com.app.flyvent.service.AirlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/airlines")
@RequiredArgsConstructor
public class AirlineController {
    private final AirlineService airlineService;

    @PostMapping("/new")
    @ResponseBody
    public NewAirlineRes postAirline(@RequestBody NewAirlineParam postAirlineReq) {
        Airline airline = postAirlineReq.toAirline();
        airlineService.register(airline);

        return new NewAirlineRes(airline);
    }
}
