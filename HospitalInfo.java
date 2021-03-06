package com.example.astp;

public class HospitalInfo {
    private String hospitalName[] =
            {"고려중앙학원고려대학교의과대학부속병원(안암병원)", "가톨릭대학교 은평성모병원", "가톨릭대학교여의도성모병원",
                    "강동경희대학교의대병원", "강북삼성병원", "건국대학교병원",
                    "경찰병원", "경희대학교병원", "고려대학교의과대학부속구로병원",
                    "구로성심병원", "노원을지대학교병원", "녹색병원",
                    "대림성모병원", "명지성모병원", "부민병원",
                    "삼성서울병원", "삼육서울병원", "서울대학교병원",
                    "서울성심병원", "서울적십자병원", "서울특별시동부병원",
                    "서울특별시보라매병원", "서울특별시서남병원", "서울특별시서울의료원",
                    "성심의료재단강동성심병원", "성애의료재단성애병원", "세란병원",
                    "순천향대학교 부속 서울병원", "에이치플러스양지병원", "연세대학교의과대학강남세브란스병원",
                    "의료법인 청구성심병원", "의료법인동신의료재단동신병원", "의료법인풍산의료재단동부제일병원",
                    "의료법인한전의료재단한일병원", "이화여자대학교의과대학부속목동병원", "이화여자대학교의과대학부속서울병원",
                    "인제대학교상계백병원", "인제대학교서울백병원", "재단법인아산사회복지재단서울아산병원",
                    "중앙대학교병원", "학교법인가톨릭학원가톨릭대학교서울성모병원", "학교법인연세대학교의과대학세브란스병원",
                    "한국보훈복지의료공단중앙보훈병원", "한국원자력의학원원자력병원", "한림대학교강남성심병원",
                    "한양대학교병원", "혜민병원", "홍익병원",
                    "희명병원"
            };
    private String hospitalPhpid[] = {"A1100008", "A1121013", "A1100011",
            "A1100043", "A1100006", "A1100002",
            "A1100039", "A1100001", "A1100014",
            "A1100026", "A1100048", "A1100044",
            "A1100037", "A1100024", "A1100036",
            "A1100010", "A1100021", "A1100017",
            "A1100050", "A1100029", "A1100022",
            "A1100040", "A1100223", "A1100035",
            "A1100028", "A1100054", "A1100032",
            "A1100004", "A1100041", "A1100015",
            "A1100023", "A1100025", "A1100075",
            "A1100020", "A1100005", "A1120796",
            "A1100016", "A1100033", "A1100009",
            "A1100003", "A1100012", "A1100007",
            "A1100053", "A1100027", "A1100055",
            "A1100013", "A1100051", "A1100019",
            "A1100049"
    };
    public String hospitalAddress[] = {
            "서울특별시 성북구 고려대로 73, 고려대병원", "서울특별시 은평구 통일로 1021 (진관동)", "서울특별시 영등포구 63로 10, 여의도성모병원 (여의도동)",
            "서울특별시 강동구 동남로 892 (상일동)", "서울특별시 종로구 새문안로 29 (평동)", "서울특별시 광진구 능동로 120-1 (화양동)",
            "서울특별시 송파구 송이로 123, 국립경찰병원 (가락동)", "서울특별시 동대문구 경희대로 23 (회기동)", "서울특별시 구로구 구로동로 148, 고려대부속구로병원 (구로동)",
            "서울특별시 구로구 경인로 427, 구로성심병원 (고척동)", "서울특별시 노원구 한글비석로 68, 을지병원 (하계동)", "서울특별시 중랑구 사가정로49길 53 (면목동)",
            "서울특별시 영등포구 시흥대로 657 (대림동, 대림성모병원)", "서울특별시 영등포구 도림로 156, 명지성모병원 (대림동)", "서울특별시 강서구 공항대로 389, 부민병원 (등촌동)",
            "서울특별시 강남구 일원로 81 (일원동, 삼성의료원)", "서울특별시 동대문구 망우로 82 (휘경동)", "서울특별시 종로구 대학로 101 (연건동)",
            "서울특별시 동대문구 왕산로 259, 서울성심병원 (청량리동)", "서울특별시 종로구 새문안로 9, 적십자병원 (평동)", "서울특별시 동대문구 무학로 124 (용두동)",
            "서울특별시 동작구 보라매로5길 20 (신대방동)", "서울특별시 양천구 신정이펜1로 20 (신정동)", "서울특별시 중랑구 신내로 156 (신내동)",
            "서울특별시 강동구 성안로 150 (길동)", "서울특별시 영등포구 여의대방로53길 22 (신길동, 성애병원)", "서울특별시 종로구 통일로 256 (무악동)",
            "서울특별시 용산구 대사관로 59 (한남동)", "서울특별시 관악구 남부순환로 1636, 양지병원 (신림동)", "서울특별시 강남구 언주로 211, 강남세브란스병원 (도곡동)",
            "서울특별시 은평구 통일로 873 (갈현동)", "서울특별시 서대문구 연희로 272, 동신병원 본관동 (홍은동)", "서울특별시 중랑구 망우로 511 (망우동, 동부제일병원)",
            "서울특별시 도봉구 우이천로 308 (쌍문동)", "서울특별시 양천구 안양천로 1071 (목동)", "서울특별시 강서구 공항대로 260, 이화여자대학교 제2부속병원 (마곡동)",
            "서울특별시 노원구 동일로 1342, 상계백병원 (상계동)", "서울특별시 중구 마른내로 9 (저동2가)", "서울특별시 송파구 올림픽로43길 88, 서울아산병원 (풍납동)",
            "서울특별시 동작구 흑석로 102 (흑석동)", "서울특별시 서초구 반포대로 222 (반포동)", "서울특별시 서대문구 연세로 50-1 (신촌동)",
            "서울특별시 강동구 진황도로61길 53 (둔촌동)", "서울특별시 노원구 노원로 75, 한국원자력의학원 (공릉동)", "서울특별시 영등포구 신길로 1 (대림동, 강남성심병원)",
            "서울특별시 성동구 왕십리로 222-1 (사근동)", "서울특별시 광진구 자양로 85 (자양동)", "서울특별시 양천구 목동로 225, 홍익병원본관 (신정동)",
            "서울특별시 금천구 시흥대로 244 (시흥동)"
    };
    public double hLat[] = {37.58715608002366, 37.633608409726854, 37.51827233800711,
            37.553476404020664, 37.568497631233825, 37.54084479467721,
            37.496413213560785, 37.5938765502235, 37.49211114525054,
            37.49964578669388, 37.636442927386746, 37.58362083896108,
            37.49068925436284, 37.4938507104387, 37.556940892893586,
            37.48851613490445, 37.587992001305395, 37.57966608924356,
            37.58419129209865, 37.56715536263689, 37.57539886464885,
            37.4937184009319, 37.51201935883779, 37.61286931510163,
            37.53598408220376, 37.51205044957338, 37.57534016994642,
            37.53384172231443, 37.48427507045319, 37.492806984645476,
            37.62079154435882, 37.58110428173239, 37.60067564592665,
            37.64611570419094, 37.53654282637804, 37.557261149,
            37.6485812672986, 37.5650731684839, 37.526563966361216,
            37.50707428493414, 37.501800804785276, 37.56211711412639,
            37.528220900896635, 37.628815981330355, 37.4932492859,
            37.559944533564746, 37.535315660180416, 37.52844147447355,
            37.45567063464179
    };

    public double hLon[] = {127.02647086385966, 126.91615048739686, 126.93673129599131,
            127.15752179822283, 126.96793805451702, 127.0721229093036,
            127.12348793503201, 127.05183223390303, 126.8847449363546,
            126.86636039556485, 127.07000281991385, 127.08605546969357,
            126.90716948025135, 126.89925446922592, 126.85094950539181,
            127.08668245340024, 127.0653288266823, 126.99896308412191,
            127.04983805981972, 126.96699861289684, 127.03140257525507,
            126.92404876254014, 126.8331299304024, 127.0980910949257,
            127.13526354631517, 126.92236733617031, 126.9577071892358,
            127.00441798640304, 126.93253922577287, 127.04631254186797,
            126.91955399169245, 126.93658306608872, 127.1090292210168,
            127.02902417950423, 126.8862159683056, 126.8362659275,
            127.06311619032102, 126.98884533055192, 127.10823825113607,
            126.96079378447554, 127.00472725970137, 126.94082769649863,
            127.14671886173552, 127.08269315796588, 126.9086725295,
            127.04488284061982, 127.08360130258502, 126.8636640030062,
            126.90056251863875
    };

    public String Tel1[] = {"02-920-5374", "1811-7755", "02-3779-1188",
            "02-440-8282", "02-2001-1000", "02-2030-5555",
            "02-3400-1300", "02-958-8282", "02-2626-1557",
            "02-2067-1515", "02-970-8282",  "02-490-2112",
            "02-829-9129", "02-829-7800",  "02-2620-0119",
            "02-3410-2060", "02-2210-3566", "02-2072-2475",
            "02-966-1616", "02-2002-8888", "02-920-9119",
            "02-870-2119", "02-6300-9119", "02-2276-7403",
            "02-2224-2358", "02-840-7115", "02-737-0181",
            "02-709-9117", "070-4665-9119", "02-2019-3333",
            "02-383-0129", "02-396-9161", "02-490-8880",
            "02-901-3000", "02-2650-5911", "02-6986-5119",
            "02-950-1100", "02-2270-0119", "02-3010-3333",
            "02-6299-1339", "02-2258-2370", "02-2227-7777",
            "02-2225-1100", "02-970-2621", "02-829-5119",
            "02-2290-8284", "02-2049-9119", "02-2600-0777",
            "02-809-0122"
    };

    public HospitalInfo(){

    }

    public String getName(int i){
        return hospitalName[i];
    }
    public String getPhpid(int i){
        return hospitalPhpid[i];
    }
    public String getAddress(int i){
        return hospitalAddress[i];
    }
    public double getLatitude(int i){
        return hLat[i];
    }
    public double getLongitude(int i){
        return hLon[i];
    }
    public String getTel1(int i){
        return Tel1[i];
    }
}
