package ru.hpclab.hl.module1.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.dto.DoctorDTO;
import ru.hpclab.hl.module1.service.AppointmentAvailabilityService;
import ru.hpclab.hl.module1.service.statistics.ObservabilityService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/availability")
public class AppointmentAvailabilityController {

    private final AppointmentAvailabilityService service;
    private final ObservabilityService observabilityService;

    public AppointmentAvailabilityController(
            AppointmentAvailabilityService service,
            ObservabilityService observabilityService
    ) {
        this.service = service;
        this.observabilityService = observabilityService;
    }

    @GetMapping("/check")
    public List<DoctorDTO> getAvailableDoctors(
            @RequestParam String specialization,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        observabilityService.start("controller.availability.check");

        List<DoctorDTO> result = service.getAvailableDoctors(specialization, date);

        observabilityService.stop("controller.availability.check");

        return result;
    }
}
