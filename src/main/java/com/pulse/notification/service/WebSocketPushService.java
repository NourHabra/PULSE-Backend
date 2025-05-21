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

    /* doctor → patient : “please approve” */
    public void notifyPatientConsentPending(Long patientId,
                                            Long consentId,
                                            Long doctorId) {

        ConsentNotification msg = new ConsentNotification();
        msg.setType("CONSENT_REQUEST");
        msg.setConsentId(consentId);
        msg.setDoctorId(doctorId);
        msg.setPatientId(patientId);

        broker.convertAndSend("/topic/patient." + patientId, msg);
    }

    /* patient → doctor : “approved / denied” */
    public void notifyDoctorConsentResult(Long doctorId,
                                          Long consentId,
                                          Long patientId,
                                          String status) {

        ConsentNotification msg = new ConsentNotification();
        msg.setType("CONSENT_RESULT");
        msg.setConsentId(consentId);
        msg.setDoctorId(doctorId);
        msg.setPatientId(patientId);
        msg.setStatus(status);          // ACTIVE or REJECTED

        broker.convertAndSend("/topic/doctor." + doctorId, msg);
    }
}