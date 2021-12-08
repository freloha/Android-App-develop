package com.example.astp;


public class realTimeHospitalInfo {
    public String totalData[] = new String[49];
    public String dutyName[] = new String[49];
    public String dutyTel[] = new String[49];
    public String hpid[] = new String[49];
    public String hv1[] = new String[49];
    public String hv10[] = new String[49];
    public String hv11[] = new String[49];
    public String hv12[] = new String[49];
    public String hv2[] = new String[49];
    public String hv3[] = new String[49];
    public String hv4[] = new String[49];
    public String hv5[] = new String[49];
    public String hv6[] = new String[49];
    public String hv7[] = new String[49];
    public String hv8[] = new String[49];
    public String hv9[] = new String[49];
    public String hvamyn[] = new String[49];
    public String hvangioayn[] = new String[49];
    public String hvcc[] = new String[49];
    public String hvccc[] = new String[49];
    public String hvctayn[] = new String[49];
    public String hvec[] = new String[49];
    public String hvgc[] = new String[49];
    public String hvicc[] = new String[49];
    public String hvidate[] = new String[49];
    public String hvmriayn[] = new String[49];
    public String hvncc[] = new String[49];
    public String hvoc[] = new String[49];
    public String hvventiayn[] = new String[49];
    public String phpid[] = new String[49];
    public String rnum[] = new String[49];
    // 총 30개

    public realTimeHospitalInfo(){

    }

    public realTimeHospitalInfo(String[] data){
        for(int i = 0; i < 49; i++){
            totalData[i] = data[i];
            String split[] = new String[30];
            split = data[i].split("\n");
            int j = 0;
            dutyName[i] = split[j++];
            dutyTel[i] = split[j++];
            hpid[i] = split[j++];
            hv1[i] = split[j++];
            hv10[i] = split[j++];
            hv11[i] = split[j++];
            hv12[i] = split[j++];
            hv2[i]= split[j++];
            hv3[i] = split[j++];
            hv4[i] = split[j++];
            hv5[i] = split[j++];
            hv6[i] = split[j++];
            hv7[i] = split[j++];
            hv8[i] = split[j++];
            hv9[i] = split[j++];
            hvamyn[i] = split[j++];
            hvangioayn[i] = split[j++];
            hvcc[i] = split[j++];
            hvccc[i] = split[j++];
            hvctayn[i] = split[j++];
            hvec[i] = split[j++];
            hvgc[i] = split[j++];
            hvicc[i] = split[j++];
            hvidate[i] = split[j++];
            hvmriayn[i] = split[j++];
            hvncc[i] = split[j++];
            hvoc[i] = split[j++];
            hvventiayn[i] = split[j++];
            phpid[i] = split[j++];
            rnum[i] = split[j++];
        }
    }

    public int findPhphid(String pid){
        int answer = 0;
        for(int i = 0; i < 49; i++){
            if(pid.equals(phpid[i])) {
                answer = i;
                break;
            }
        }
        return answer;
    }
}
