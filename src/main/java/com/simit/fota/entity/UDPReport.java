package com.simit.fota.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UDPReport {
    private String Start;
    private String Ver;
    private String Len;
    private String CMD;
    private String DeviceID;
    private String IMEI;
    private String productVersion;
    private String ICCID;
    private String IMSI;
    private String EARFCN;
    private String CELLID;
    private String PCI;
    private String RSRP;
    private String RSRQ;
    private String RSSI;
    private String CSQ;
    private String SNR;
    private String ECL;
    private String RRCSUCESS;
    private String PRCSUM;
    private String RRCFAILReason1;
    private String RRCFAILReason2;
    private String RRCFAILReason3;
    private String RRCFAILReason4;
    private String RRCFAILReason5;
    private String RRCFAILReason6;
    private String RRCFAILReason7;
    private String SubCarrSpacing;
    private String ToneNum;
    private String Pwr;
    private String PreambleRep;
    private String OperationMode;
    private String MultiCarriere;
    private String workTimes;
    private String workTime;
    private String SIM;
    private String Operato1;
    private String OP1ARFCN1;
    private String OP1RSRP1;
    private String OP1_SNR1;
    private String OP1_ARFCN2;
    private String OP1_RSRP2;
    private String OP1_SNR2;
    private String OP1_ARFCN3;
    private String OP1_RSRP3;
    private String OP1_SNR3;
    private String Operato2;
    private String OP2_ARFCN1;
    private String OP2_RSRP1;
    private String OP2_SNR1;
    private String OP2_ARFCN2;
    private String OP2_RSRP2;
    private String OP2_SNR2;
    private String OP2_ARFCN3;
    private String OP2_RSRP3;
    private String OP2_SNR3;
    private String SIM_change_Result;
    private String Reserve1;
    private String Reserve2;
    private String CheckNum;
    private String STOP;
    private String ts;
}
