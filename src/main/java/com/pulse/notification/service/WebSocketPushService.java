package com.pulse.notification.service;


import com.pulse.consent.dto.ConsentNotification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketPushService {

    private final SimpMessagingTemplate broker;

    public WebSocketPushService(SimpMessagingTemplate broker) {
        this.broker = broker;
    }

    public void notifyPatientConsentPending(Long patientId,
                                            Long consentId,
                                            Long doctorId) {

        ConsentNotification n = new ConsentNotification();
        n.setType("CONSENT_REQUEST");
        n.setConsentId(consentId);
        n.setDoctorId(doctorId);
        n.setPatientId(patientId);          // <─ add / keep this line
        n.setStatus("PENDING");             // <─ new line

        broker.convertAndSendToUser(
                patientId.toString(),
                "/queue/patient.consents",
                n
        );
    }


    public void notifyDoctorConsentResult(Long doctorId,
                                          Long consentId,
                                          Long patientId,
                                          String status) {

        ConsentNotification n = new ConsentNotification();
        n.setType("CONSENT_RESULT");
        n.setConsentId(consentId);
        n.setDoctorId(doctorId);        // ←  add / keep this line
        n.setPatientId(patientId);
        n.setStatus(status);            // ACTIVE or REJECTED

        broker.convertAndSendToUser(
                doctorId.toString(),
                "/queue/doctor.consents",
                n
        );
    }
}
