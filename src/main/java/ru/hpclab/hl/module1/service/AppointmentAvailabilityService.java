package ru.hpclab.hl.module1.service;

import org.springframework.stereotype.Service;
import ru.hpclab.hl.module1.client.AppointmentClient;
import ru.hpclab.hl.module1.client.DoctorClient;
import ru.hpclab.hl.module1.dto.AppointmentDTO;
import ru.hpclab.hl.module1.dto.DoctorDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentAvailabilityService {

    private final DoctorClient doctorClient;
    private final AppointmentClient appointmentClient;

    public AppointmentAvailabilityService(DoctorClient doctorClient, AppointmentClient appointmentClient) {
        this.doctorClient = doctorClient;
        this.appointmentClient = appointmentClient;
    }

    public List<DoctorDTO> getAvailableDoctors(String specialization, LocalDate date) {
        List<AppointmentDTO> appointments = appointmentClient.getAppointments();
        List<DoctorDTO> allDoctors = doctorClient.getDoctorsBySpecialization(specialization);

        List<Long> busyDoctorIds = appointments.stream()
                .filter(app ->
                        specialization.equalsIgnoreCase(app.getSpecialization()) &&
                                app.getAppointmentDate().toLocalDate().equals(date)
                )
                .map(AppointmentDTO::getDoctorId)
                .distinct()
                .collect(Collectors.toList());

        return allDoctors.stream()
                .filter(doc -> !busyDoctorIds.contains(doc.getId()))
                .collect(Collectors.toList());
    }
}
