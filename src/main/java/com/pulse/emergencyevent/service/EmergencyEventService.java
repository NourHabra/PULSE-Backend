package com.pulse.emergencyevent.service;



import com.pulse.emergencyevent.model.EmergencyEvent;
import com.pulse.emergencyevent.repository.EmergencyEventRepository;
import com.pulse.prescription.model.Prescription;
import org.springframework.stereotype.Service;
import com.pulse.medicalrecord.repository.MedicalRecordEntryRepository;
import com.pulse.user.model.EmergencyWorker;
import org.springframework.stereotype.Service;
import com.pulse.medicalrecord.model.MedicalRecordEntry;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class EmergencyEventService {

    private final EmergencyEventRepository eventRepo;
    private final MedicalRecordEntryRepository mreRepo;

    public EmergencyEventService(EmergencyEventRepository eventRepo,
                                 MedicalRecordEntryRepository mreRepo) {
        this.eventRepo = eventRepo;
        this.mreRepo = mreRepo;
    }
    @Transactional
    public EmergencyEvent createEventWithMre(EmergencyEvent event, MedicalRecordEntry mre) {
        MedicalRecordEntry savedMre = mreRepo.save(mre);

        event.setMedicalRecordEntry(savedMre);
        EmergencyEvent savedEvent = eventRepo.saveAndFlush(event);

        return savedEvent;
    }
    public EmergencyEvent create(EmergencyEvent event) {
        return eventRepo.save(event);
    }

    public List<EmergencyEvent> findAllByPatientId(Long patientId) {
        return eventRepo.findByMedicalRecordEntry_Patient_UserId(patientId);
    }

    public EmergencyEvent findById(Long id) {
        return eventRepo.findById(id).orElse(null);
    }

    public Optional<EmergencyEvent> findByMreId(Long mreId) {
        return eventRepo.findByMedicalRecordEntry_MedicalRecordEntryId(mreId);
    }

}