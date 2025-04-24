package ru.hpclab.hl.module1.service;

import org.springframework.stereotype.Service;
import ru.hpclab.hl.module1.client.AppointmentClient;
import ru.hpclab.hl.module1.client.DoctorClient;
import ru.hpclab.hl.module1.dto.AppointmentDTO;
import ru.hpclab.hl.module1.dto.DoctorDTO;
import ru.hpclab.hl.module1.service.statistics.ObservabilityService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentAvailabilityService {

    private final DoctorClient doctorClient;
    private final AppointmentClient appointmentClient;
    private final ObservabilityService observabilityService;

    public AppointmentAvailabilityService(
            DoctorClient doctorClient,
            AppointmentClient appointmentClient,
            ObservabilityService observabilityService
    ) {
        this.doctorClient = doctorClient;
        this.appointmentClient = appointmentClient;
        this.observabilityService = observabilityService;
    }

    public List<DoctorDTO> getAvailableDoctors(String specialization, LocalDate date) {

        List<AppointmentDTO> appointments = appointmentClient.getAppointments();

        observabilityService.start("availability.check");

        List<Long> allDoctorIds = doctorClient.getDoctorsBySpecialization(specialization)
                .stream()
                .map(DoctorDTO::getId)
                .toList();

        List<Long> busyDoctorIds = appointments.stream()
                .filter(app ->
                        specialization.equalsIgnoreCase(app.getSpecialization()) &&
                                app.getAppointmentDate().toLocalDate().equals(date)
                )
                .map(AppointmentDTO::getDoctorId)
                .distinct()
                .collect(Collectors.toList());

        List<DoctorDTO> availableDoctors = allDoctorIds.stream()
                .filter(id -> !busyDoctorIds.contains(id))
                .map(doctorClient::getDoctorById)
                .collect(Collectors.toList());

        observabilityService.stop("availability.check");

        return availableDoctors;
    }
}
