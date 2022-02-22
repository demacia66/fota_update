package com.simit.fota.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"IMEI", "DeviceID","SWVersion", "salary", "Model" })
public class UDPReport {

    @JsonProperty("DeviceID")
    private String DeviceID;
    @JsonProperty("Model")
    private String Model;
    @JsonProperty("SWVersion")
    private String SWVersion;
    @JsonProperty("IMEI")
    private String IMEI;
    @JsonProperty("ProductVersion")
    private String ProductVersion;
    @JsonProperty("ICCID")
    private String ICCID;
    @JsonProperty("IMSI")
    private String IMSI;
    @JsonProperty("EARFCN")
    private String EARFCN;
    @JsonProperty("CELLID")
    private String CELLID;
    @JsonProperty("PCI")
    private String PCI;
    @JsonProperty("RSRP")
    private String RSRP;
    @JsonProperty("RSRQ")
    private String RSRQ;
    @JsonProperty("RSSI")
    private String RSSI;
    @JsonProperty("CSQ")
    private String CSQ;
    @JsonProperty("SNR")
    private String SNR;
    @JsonProperty("ECL")
    private String ECL;
    @JsonProperty("RRCSUCESS")
    private String RRCSUCESS;
    @JsonProperty("RRCSUM")
    private String RRCSUM;
    @JsonProperty("RRCFAILReason1")
    private String RRCFAILReason1;
    @JsonProperty("RRCFAILReason2")
    private String RRCFAILReason2;
    @JsonProperty("RRCFAILReason3")
    private String RRCFAILReason3;
    @JsonProperty("RRCFAILReason4")
    private String RRCFAILReason4;
    @JsonProperty("RRCFAILReason5")
    private String RRCFAILReason5;
    @JsonProperty("RRCFAILReason6")
    private String RRCFAILReason6;
    @JsonProperty("RRCFAILReason7")
    private String RRCFAILReason7;
    @JsonProperty("SubCarrSpacing")
    private String SubCarrSpacing;
    @JsonProperty("ToneNum")
    private String ToneNum;
    @JsonProperty("Pwr")
    private String Pwr;
    @JsonProperty("PreambleRep")
    private String PreambleRep;
    @JsonProperty("OperationMode")
    private String OperationMode;
    @JsonProperty("MultiCarrier")
    private String MultiCarrier;
    @JsonProperty("WorkTimes")
    private String WorkTimes;
    @JsonProperty("WorkHours")
    private String WorkHours;
    @JsonProperty("CurrentSIM")
    private String CurrentSIM;
    @JsonProperty("Operato1")
    private String Operato1;
    @JsonProperty("OP1_ARFCN1")
    private String OP1ARFCN1;
    @JsonProperty("OP1_RSRP1")
    private String OP1RSRP1;
    @JsonProperty("OP1_SNR1")
    private String OP1SNR1;
    @JsonProperty("OP1_ARFCN2")
    private String OP1ARFCN2;
    @JsonProperty("OP1_RSRP2")
    private String OP1RSRP2;
    @JsonProperty("OP1_SNR2")
    private String OP1SNR2;
    @JsonProperty("OP1_ARFCN3")
    private String OP1ARFCN3;
    @JsonProperty("OP1_RSRP3")
    private String OP1RSRP3;
    @JsonProperty("OP1_SNR3")
    private String OP1SNR3;
    @JsonProperty("Operato2")
    private String Operato2;
    @JsonProperty("OP2_ARFCN1")
    private String OP2ARFCN1;
    @JsonProperty("OP2_RSRP1")
    private String OP2RSRP1;
    @JsonProperty("OP2_SNR1")
    private String OP2SNR1;
    @JsonProperty("OP2_ARFCN2")
    private String OP2ARFCN2;
    @JsonProperty("OP2_RSRP2")
    private String OP2RSRP2;
    @JsonProperty("OP2_SNR2")
    private String OP2SNR2;
    @JsonProperty("OP2_ARFCN3")
    private String OP2ARFCN3;
    @JsonProperty("OP2_RSRP3")
    private String OP2RSRP3;
    @JsonProperty("OP2_SNR3")
    private String OP2SNR3;
    @JsonProperty("ResultKey")
    private String ResultKey;
    private Long ts;
}
