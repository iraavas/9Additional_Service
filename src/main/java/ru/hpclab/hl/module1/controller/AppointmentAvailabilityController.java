package ru.hpclab.hl.module1.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.dto.DoctorDTO;
import ru.hpclab.hl.module1.service.AppointmentAvailabilityService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/availability")
public class AppointmentAvailabilityController {

    private final AppointmentAvailabilityService service;

    public AppointmentAvailabilityController(AppointmentAvailabilityService service) {
        this.service = service;
    }

    @GetMapping("/check")
    public List<DoctorDTO> getAvailableDoctors(
            @RequestParam String specialization,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return service.getAvailableDoctors(specialization, date);
    }
}
