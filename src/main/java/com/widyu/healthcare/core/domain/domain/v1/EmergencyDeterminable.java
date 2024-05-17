package com.widyu.healthcare.core.domain.domain.v1;

public interface EmergencyDeterminable {
    void determineFromOxygenSaturation();
    void determineFromHeartBit();
}
