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
        // persist the MRE first
        MedicalRecordEntry savedMre = mreRepo.save(mre);

        // attach to event and persist the event
        event.setMedicalRecordEntry(savedMre);
        EmergencyEvent savedEvent = eventRepo.saveAndFlush(event);

        return savedEvent;
    }

    // You can still keep the simple create(...) if you like:
    public EmergencyEvent create(EmergencyEvent event) {
        return eventRepo.save(event);
    }

    public List<EmergencyEvent> findAllByPatientId(Long patientId) {
        return eventRepo.findByMedicalRecordEntry_Patient_UserId(patientId);
    }

    public EmergencyEvent findById(Long id) {
        return eventRepo.findById(id).orElse(null);
    }

}