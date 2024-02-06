package com.umc.networkingService.config.initial;

import com.umc.networkingService.domain.project.entity.Type;
import com.umc.networkingService.global.common.enums.Semester;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ProjectInfo {

    // 4기
//    YOUR_WEATHER("유어웨더", "오늘 당신의 감정 날씨", "하루 3번 감정과 컨디션을 기록하고, 간편하게 순간을 기록하는 어플리케이션", "", Semester.FOURTH, List.of(Type.AOS)),
//    LOVE_KEEPER("러브키퍼", "연인 간 싸움 방지 서비스", "소중한 연인과의 관계를 유지하고 싸움을 방지하는 어플리케이션", "", Semester.FOURTH, List.of(Type.WEB)),
//    RE_FIT("RE-FIT", "친환경 디지털 옷장 서비스", "패스트패션은 그만! 지속 가능한 슬로우패션을 지향하는 친환경 옷장 어플리케이션", "", Semester.FOURTH, List.of(Type.AOS)),


    // 5기
    // GACI
    CAMPUS_NOTE("캠퍼스 노트", "올인원 강의 필기 서비스", "시간표 연동 강의 필기 앱, 캠퍼스 노트 어플리케이션", "", List.of("필기", "노트", "대학"), Semester.FIFTH, List.of(Type.AOS)),
    TEMBURIN("템버린", "item 교체주기 알림 서비스", "일상생활에서 사용하고 있는 item의 교체 주기를 위한 알림을 제공하여 잊어버리지 않도록 도와주는 어플리케이션", "", List.of("알림", "교체", "생활용품"), Semester.FIFTH, List.of(Type.AOS)),
    HONJABDA("혼잡다", "카페 혼잡도 파악 서비스", "카페 혼잡도를 미리 파악할 수 있는 서비스 어플리테이션", "", List.of("편리함", "시간 단축", "상생"), Semester.FIFTH, List.of(Type.IOS)),
    TAPE("Tape", "주위 사람들과 음악 취향을 공유하는 소셜 네트워크 서비스", "주위 사람들과 음악 취향 경험을 공유하는 소셜 네트워크 서비스", "", List.of("음악", "공유", "소통"), Semester.FIFTH, List.of(Type.AOS)),
    PLAN_ME("PLAN ME", "맞춤형 플래너, 더 나은 내일", "자유롭게 커스터마이징하는 나만의 플래너 서비스", "", List.of("편리함", "한번에", "특별한"), Semester.FIFTH, List.of(Type.AOS)),
    UMJUMISIKHOI("음주미식회", "음식 기반 주류 추천 서비스", "사용자들에게 음식과 맞는 주종을 추천해주고 커뮤니티를 통해서 다른 사람의 베스트 조합을 볼 수 있고 이야기를 나눌 수 있는 어플리케이션", "", List.of("음식", "주류", "조합"), Semester.FIFTH, List.of(Type.IOS)),
    HAGOSIMDA("하고심다", "나만의 정원을 가꾸는 일정 관리 서비스", "모종을 심는 것처럼 목표를 달성할 수 있는 투두리스트", "", List.of("나만의 정원", "일정관리", "투두리스트"), Semester.FIFTH, List.of(Type.AOS, Type.WEB)),
    MARIAJEU("마리아주", "와인 특성에 따른 술안주 레시피 추천 서비스", "어떠한 술과도 가장 행복한 추억을 줄 수 있는 마리아주 레시피 서비스", "", List.of("페어링(완전한 궁합)", "즐거움(놀이)", "공유 및 리뷰"), Semester.FIFTH, List.of(Type.AOS, Type.WEB)),
    UMC_NETWORKING_SERVICE("UMC", "연합 동아리 네트워킹 서비스", "UMC, 공부만 하고 계신건가요? 다른 학교 학생들과 재밌는 네트워킹 시간을 가져봐요.", "", List.of("연합", "네트워킹", "히스토리"), Semester.FIFTH, List.of(Type.IOS, Type.WEB)),
    CLIMUS("클라이머스", "클라이머들이 꿈꾸던 볼더링 문제 풀이 플랫폼", "", "", List.of("클라이밍", "볼더링"), Semester.FIFTH, List.of(Type.AOS)),
    REVIEW_ZIP("Review.zip", "장소 리뷰 공유 서비스 a.k.a 립스타그램", "친구들의 장소 리뷰를 공유 받거나 공유해보세요 :)", "", List.of("장소", "리뷰", "친구"), Semester.FIFTH, List.of(Type.WEB)),
    UMC_MATCHING_CENTER("UMC Matching Center", "당신은 팀 매칭을 위해 얼마나 많은 클릭을 하고 계신가요?", "UMC 팀 매칭을 하나의 웹 페이지로, 한 번에, 더 편하게", "", List.of("팀 매칭", "All in 1", "편리함"), Semester.FIFTH, List.of(Type.WEB)),
    RE_MEMORY("RE:MEMORY", "편지/롤링페이퍼 타임캡슐 서비스", "단순히 기억을 보관하는 것을 넘어서, 추억과 감정을 선물하는 타임캡슐 서비스. RE:memory", "", List.of("타임캡슐", "편지", "롤링페이퍼"), Semester.FIFTH, List.of(Type.WEB)),
    // SQUARE
    JUINJANG("주인장", "내 손안의 완벽한 부동산 임장", "당신의 임장을 편리하게 기록하세요:)", "", List.of("생산성", "임장", "체크리스트"), Semester.FIFTH, List.of(Type.IOS)),
    BLOOD_TRAIL("BloodTrail", "헌혈 원포인트 어플", "헌혈과 커뮤니티를 한번에, 헌혈을 대표할 앱 그 자체가 되자", "", List.of("헌혈", "커뮤니티"), Semester.FIFTH, List.of(Type.WEB)),
    NANGMAN_CAT("낭만 고양이", "마음을 전달하는 모바일 편지 서비스", "마음을 전달하는 모바일 편지 서비스", "", List.of("느린 우체통", "개성", "소통"), Semester.FIFTH, List.of(Type.WEB)),
    UJU_JEONGGEOJANG("우주 정거장", "나를 위한 글쓰기", "나만의 글을 기록하는 곳으로, 사람들이 글쓰는 것을 재밌어하고 글을 통해 스스로 성장해가는 경험을 느끼게 하고 싶어요. 또한 내가 쓴 글을 책으로 내보면 어떨까? 하는 생각으로 전자책 제작 및 제공 서비스도 구현하고자 합니다.", "", List.of("글쓰기", "몰입", "자기개발"),Semester.FIFTH, List.of(Type.WEB)),
    HOMEAT("Homeat", "챌린지형 식비 관리 커뮤니티 어플", "‘Homeat(홈잇)’은 ‘Home’과 ‘eat’을 합친 것으로, 집밥의 의미를 담고 있습니다.", "", List.of("1인가구", "식비관리", "정보공유"), Semester.FIFTH, List.of(Type.IOS)),
    FORGRAD("FORGRAD", "졸업정보어플", "당신의 졸업을 위한 모든 것! 졸업정보서비스", "", List.of("졸업", "소통", "편리함"), Semester.FIFTH, List.of(Type.AOS)),
    INTERPHONE("인터폰!", "자신에게 맞는 기업추천 서비스", "인턴십을 하고자 하는 사람들과 기업간의 정보 불균형이 해결되는 그날까지 !!", "", List.of("인턴", "대학생", "스펙쌓기"), Semester.FIFTH, List.of(Type.WEB)),
    DONGNEHYEONG("동네형", "당신 근처의 ‘동네형’에게 운동을 배우세요.", "", "", List.of("헬스", "PT"), Semester.FIFTH, List.of(Type.WEB)),
    CAREER_FESTIVAL("Career Festival", "내 커리어에 맞는 오프라인 행사 매칭 및 기록 서비스", "내 커리어에 맞는 오프라인 행사 매칭 및 기록 서비스", "", List.of("커리어", "자기개발", "행사"), Semester.FIFTH, List.of(Type.WEB)),
    TRAVEL_COMPASS("Travel Compass", "간편한 여행계획 설계 서비스", "당신의 여행 계획! 간편하게 이 웹사이트 사용해서 계획하시는건 어떠신가요~", "", List.of("간편함", "여행계획", "한큐에딱"), Semester.FIFTH, List.of(Type.WEB)),
    TICKET_TAKA("Ticket-taka", "팀 프로젝트 서포팅 어플", "우리 티켓으로 소통하자! 불필요한 소통은 줄이고 프로젝트에 집중할 수 있는 환경을 만들어주는 서비스", "", List.of("티켓", "프로젝트", "관리"),Semester.FIFTH, List.of(Type.IOS)),
    BID("bid", "경매 형 중고 거래 플랫폼", "판매자 구매자의 경매 형 중고 거래 플랫폼!", "", List.of("경매 형", "기댓값", "중고 거래"), Semester.FIFTH, List.of(Type.WEB)),
    ANDDEUL("안뜰", "가족 중심 네트워킹 어플", "가족 간의 단단한 네트워킹을 위한 소규모 SNS", "", List.of("따듯함", "일상공유", "마음전하기"), Semester.FIFTH, List.of(Type.AOS)),
    AIRECIPE("아이레시피", "AI 챗봇 기반 맞춤형 레시피 서비스", "AI 챗봇 기반 맞춤형 레시피 서비스", "", List.of("친환경", "레시피", "간편함"), Semester.FIFTH, List.of(Type.AOS)),
    BE_ILSANG("비일상", "친환경 챌린지 프로젝트", "친환경 실천을 위한 챌린지 및 커뮤니티 플랫폼 서비스", "", List.of("친환경", "챌린지"), Semester.FIFTH, List.of(Type.IOS)),
    ARTORY("ARTORY", "나만의 문화 이야기", "문화가 가진 스토리를 나만의 문화로 이야기하다", "", List.of("문화 생활", "기록", "커뮤니티"), Semester.FIFTH, List.of(Type.WEB)),
    THE_SCULPTOR("The Sculptor", "당신의 인생을 조각할", "스스로의 인생을 스스로 조각하는 인생의 조각가가 되어보세요.", "", List.of("자기개발", "일관성", "SNS"), Semester.FIFTH, List.of(Type.AOS)),
    GONGJAKSO("공작소", "공모전/프로젝트 팀빌딩 서비스", "공모전/프로젝트 관련 공고를 확인하고 팀빌딩을 돕는 서비스, 공작소", "", List.of("팀 빌딩", "캘린더"), Semester.FIFTH, List.of(Type.WEB)),
    // W
    STUDY_FLEX("STUDY FLEX", "캠퍼스별 팀스터디 매칭 웹 서비스", "팀 스터디를 통해 성장하고 싶은 당신, 캠퍼스별 팀스터디 매칭 관리 웹 서비스 Study Flex에서 성장하세요!", "", List.of("팀 스터디", "매칭"), Semester.FIFTH, List.of(Type.WEB)),
    GUSTO("Gusto", "나만의 맛집 지도", "나만의 맛집을 저장하는 서비스!", "", List.of("나만의", "맛집", "지도"), Semester.FIFTH, List.of(Type.AOS)),
    DDODDOGA("또또가", "또 가고싶은 곳만 리뷰한다.", "리뷰어와 상점을 연결하는 리뷰어 마케팅 중개 서비스", "", List.of("리뷰", "마케팅"), Semester.FIFTH, List.of(Type.WEB)),
    CHOIAEEU_JANGSO("최애의 장소", "", "일본 애니메이션 성지순례 정보 제공 서비스", "", List.of("성지순례", "최애"), Semester.FIFTH, List.of(Type.AOS)),
    BRUSHWORK("BRUSHWORK", "나를 그리는 공간, 브러시워크", "미대생 졸업작품 판매 웹 서비스", "", List.of("미대생", "졸업작품", "채팅"), Semester.FIFTH, List.of(Type.WEB)),
    PROUST("프루스트", "너의 취’향’을 찾고 싶어? -  향수 정보 앱", "너의 취’향’을 찾고 싶어? ✨ -  향수 정보 커뮤니티 앱", "", List.of("커뮤니티", "감성", "편리함"), Semester.FIFTH, List.of(Type.WEB)),
    OLREA("올래", "시니어를 위한 교육 매칭 서비스", "청춘은 바로 지금! 시니어를 위한 교육 매칭 서비스", "", List.of("시니어", "교육", "매칭"), Semester.FIFTH, List.of(Type.WEB)),
    MART_ALL("마트올", "동네마트 온라인 장보기", "동네마트 온라인 장보기", "", List.of("365", "멤버십", "소통"), Semester.FIFTH, List.of(Type.AOS)),
    SUBMARINE("몰입", "몰입하는 습관을 길러주는", "submarine (써머린) : 잠수함이라는 뜻으로 심해에 빠진 듯한 몰입감을 줄 수 있는 앱", "", List.of("몰입", "챌린지"), Semester.FIFTH, List.of(Type.IOS)),
    TO_GETHER("TO:gether", "동반인 찾기 서비스", "TOgether, 모든 곳에서 함께하는 우리의 동반인 찾기 서비스", "", List.of("다양성", "편리함", "연결"), Semester.FIFTH, List.of(Type.WEB)),
    BAB_ZIP("밥zip", "실시간 캠퍼스 식당 정보 서비스", "캠퍼스 라이프의 완벽한 동반자, 실시간 캠퍼스 식당 정보 서비스 - 혼잡도 체크, 밥 메이트 연결, 식당 정보를 한눈에!", "", List.of("음식", "대학", "메이트"), Semester.FIFTH, List.of(Type.WEB)),
    UMARK("umark", "대학생을 위한 큐레이션 공간", "대학생이 경험하고 느끼는 것을 담은 큐레이션 공간", "", List.of("대학생", "큐레이션", "북마크"), Semester.FIFTH, List.of(Type.WEB)),
    // HEDGES
    TEACHER_FOR_BOSS("티쳐 포 보스", "사장님은 혼자가 아니예요! 선생님께 물어보세요.", "자영업자 솔루션 서비스", "", List.of("자영업", "사장"), Semester.FIFTH, List.of(Type.AOS)),
    GLOBAL_STUDENTS("Global Students", "유학생 개방형 커뮤니티", "거기 너!(유학생) 내 친구가 되라!(International Friend 1 day from today)", "", List.of("유학생", "개방형 커뮤티니", "생활편의"), Semester.FIFTH, List.of(Type.WEB)),
    COUPLE("커플", "", "데이트 코스 랜덤 추천 서비스", "", List.of("데이트", "커플"), Semester.FIFTH, List.of(Type.IOS)),
    A_WRITE("각사각", "국내 자기 성장 서비스", "당신의 감정을 사각사각, 국내 자기 성장 도우미 서비스", "", List.of("통합성", "간편함", "기록적인"), Semester.FIFTH, List.of(Type.AOS)),
    SURVEY_MATE("썰매", "설문 조사 중개를 위한 C2C 서비스", "설문 조사자에게 데이터, 설문 응답자에게는 리워드가 눈덩이처럼 쌓인다.", "", List.of("설문 조사", "신뢰", "앱테크"), Semester.FIFTH, List.of(Type.WEB)),
    YOUNET("Younet", "유학생을 위한 네트워크", "유학생활의 모든 정보를 이곳에서!", "", List.of("유학생", "네트워크", "연결고리"), Semester.FIFTH, List.of(Type.IOS)),
    CUMO("쿠모", "세상의 모든 종이 쿠폰을 모아주는 서비스", "", "", List.of("짠테크", "간편함", "쿠폰"), Semester.FIFTH, List.of(Type.AOS, Type.WEB)),
    EASY_EXCEL("이지 엑셀", "슬기로운 엑셀사용을 위한 엑셀 함수/단축키 통합정리 서비스", "", "", List.of("편리함", "액셀", "단축키"), Semester.FIFTH, List.of(Type.WEB)),
    BONGURI("봉우리", "봉사활동 통합 플랫폼", "봉사활동을 쉽고 재미있게! 봉사활동 통합 플랫폼 서비스, 봉우리입니다", "", List.of("봉사", "통합", "유입&지속"), Semester.FIFTH, List.of(Type.AOS)),
    BODY_CAPTURE("BodyCapture", "바디프로필 예약 ALL IN ONE 서비스", "바디프로필 예약, 이젠 한 곳에서 모두 끝낼 수 있다면? 바디프로필 예약의 AtoZ Body Capture!", "", List.of("올인원", "AtoZ", "도우미"), Semester.FIFTH, List.of(Type.IOS)),
    PACK_IT("Pack It", "집에 잠든 개인용기로 동네를 깨우다", "", "", List.of("친환경", "지역상생", "게이미피케이션"), Semester.FIFTH, List.of(Type.AOS)),
    NANGJANGGO("냉장고를 부탁해", "나의 냉장고, 장보기 관리 서비스", "나의 냉장고에 어떤 식재료가 있는지!  이번달에는 어떤 식재료를 사왔는지 한눈에 알 수 있는 서비스!", "", List.of("냉장고", "식재로"), Semester.FIFTH, List.of(Type.WEB)),
    MUFFLER("머플러", "소비 목표 달성을 도와주는 가계부", "매일매일의 소비금액 목표를 달성할 수 있도록 도와준다! 세세한 소비계획으로 충동소비를 줄이고 계획적 소비를 하자.", "", List.of("세세한 계획", "내 일정에 따라 나누는 계획"), Semester.FIFTH, List.of(Type.IOS)),
    PEER_RE("PEER:Re", "건강한 협력을 위한 “선택형” 동료평가 서비스", "건강한 협력을 위한 “선택형” 동료평가 서비스, PEER : Re (피어리)", "", List.of("간편함", "선택형", "동료평가"),Semester.FIFTH, List.of(Type.WEB)),
    TEAMMATE("팀메이트", "모든 운동을 위한 구인/구팀 매칭 서비스!", "모든 생활 체육을 함께 즐길 메이트를 쉽게 매칭해주는 서비스!", "", List.of("매칭", "운동", "개인정보보호"), Semester.FIFTH, List.of(Type.WEB)),
    BAB_CHINGU("밥 친구", "식사 상황별 콘텐츠 추천 서비스", "밥 먹으면서 뭐 볼까, 하는 고민은 이제 그만! 밥 친구가 대신 결정해 드립니다.", "", List.of("콘텐츠", "추천", "개인화"), Semester.FIFTH, List.of(Type.WEB)),
    // KSSS
    ABOUT_ME("AboutMe", "다양한 나, 다양한 관계의 시작", "다양한 나, 다양한 관계의 시작", "", List.of("멀티프로필", "명함", "커스텀마이징"), Semester.FIFTH, List.of(Type.AOS)),
    DRAW_DESKTOP("Draw Desktop", "바탕화면을 꾸미다", "개성 있게 간편하면서 직관적으로 바탕 화면을 꾸밀 수 있는 요소를 제공 /  간편한 메모 기능과 꾸미기 기능, 달력과 같은 여러 기능을 제공하면서 디자인 적으로 꾸밀 수 있는 요소를 추가", "", List.of("바탕화면", "스티커", "꾸미기"), Semester.FIFTH, List.of(Type.WEB)),
    HERE_YOU("Here You", "여행의 이유", "기록으로 나를 찾는 여행 아카이빙 커뮤니티", "", List.of("기록", "여행", "커뮤티니"), Semester.FIFTH, List.of(Type.WEB)),
    RHYTHM_PALETTE("Rhythm Palette", "", "리듬 팔레트는 공유하고 싶은 음악, 상황, 감정을 ai가 생성해주는 이미지와 함께 게시할 수 있는 SNS 서비스 입니다.", "", List.of("음악공유", "이미지생성", "SNS"), Semester.FIFTH, List.of(Type.WEB)),
    WRITEROOM("Writeroom", "자유로운 창작의 공간", "라이트룸은 글쓰기 연습을 하고 싶지만 두려움을 가진 사용자에게 자유도 높은 공간을 제공하여 글에 대한 진입 장벽을 허무는 것을 목표로 삼고 있습니다.", "", List.of("자유로움", "글쓰기 연습", "공간"), Semester.FIFTH, List.of(Type.WEB)),
    DAON("다온", "암환자들을 위한 하루 기록 서비스", "다온은,일기장을 통해 환자의 경험과 감정을 글로 표현, 공유하고 캘린더를 통해 의료 마이데이터 기반의 치료 일정 및 건강 체크의 용이성을 높여 그들이 갖는 신체적, 심리적 어려움 해소에 도움이 되고자 기획된 아이디어 입니다.", "", List.of("암","마이데이터", "일기"), Semester.FIFTH, List.of(Type.AOS)),
    THE_GOODS("The GOODs", "개인 판매자 대상 온라인 주문서 플랫폼", "덕심으로 똘똘 뭉친 굿즈 거래, 굿즈에 대한 모든 것!", "", List.of("덕질", "굿즈", "오픈마켓"), Semester.FIFTH, List.of(Type.WEB)),
    AVAV("아브아브", "레크레이션 종합 플랫폼 서비스", "얼음같은 사회를 녹여줄 레크레이션 종합 플랫폼 서비스 아브아브입니다!", "", List.of("레크레이션", "아이스브레이킹", "남녀노소"), Semester.FIFTH, List.of(Type.WEB)),
    MEME("메메", "나만의 메이크업 메이트", "나만의 메이크업 메이트, 메메!", "", List.of("개인화", "매칭", "커뮤니티"), Semester.FIFTH, List.of(Type.IOS)),
    BRANDOL("BRANDOL", "브랜드 팬덤 커뮤니티", "우리가 만들어가는 브랜드, BRANDOL - 브랜드 팬덤 커뮤니티 앱", "", List.of("브랜딩", "콘텐츠", "팬"), Semester.FIFTH, List.of(Type.AOS)),
    MY_PLACE("마플", "사용자 저장 장소 기반 기록 + 일정 관리 서비스", "사용자가 등록한 관심 장소를 기반으로 장소를 제안하고 장소를 기록하는 서비스", "", List.of("관심장소", "기록"), Semester.FIFTH, List.of(Type.IOS)),
    SPON_US("Spon-us", "기업과 대학생이 만나는 자리", "대학생 단체와 기업 간의 협찬/제휴/연계프로그램 (이하 협력) 컨택을 도우는 서비스입니다.", "", List.of("편리함", "매칭", "도우미"), Semester.FIFTH, List.of(Type.IOS)),
    ISA_ZIP("이사.zip", "이사를 위한 정보", "이사 혹은 주거지에 대한 정보와 편리함을 담고있는 어플", "", List.of("매칭", "편리성", "접근성"), Semester.FIFTH, List.of(Type.AOS)),
    GOMIN_CHINGU("고민친구", "세상의 모든 고민이 거쳐가는 공간", "세상의 모든 고민이 거쳐가는 공간, 고민친구", "", List.of("고민해결", "결정장애", "의사결정능력"), Semester.FIFTH, List.of(Type.WEB)),
    // NEPTUNE
    MUGGLE("일상의 기록", "Record of a day", "나의 일상을 자동으로 기록하는 편리한 일기 작성 서비스", "", List.of("편리함", "나만의 이야기", "아름다운 추억"), Semester.FIFTH, List.of(Type.AOS)),
    BABIN("바빈", "교내 시설물 활성화 서비스", "교내 시설물을 쉽고 간편하게 이용하기, 교내 시설물을 쉽고 간편하게 관리하기", "", List.of("교내 시설물 활성화", "보물 찾기"), Semester.FIFTH, List.of(Type.WEB)),
    FRIEND("Friend", "부경대학교 랜덤매칭 서비스", "부경대 학생들과 랜덤매칭을 하며, 새로운 친구들과 놀수있는 서비스를 제공합니다.", "", List.of("부경대 핑캠 매칭", "지인 매칭 방지"), Semester.FIFTH, List.of(Type.WEB)),
    CC("CC(Creative Connect)", "1인 크리에이터 커뮤니티", "같이 만들어가는 1인미디어 커뮤니티, CC(Creative Connect)입니다", "", List.of("1인 크리에이터", "커뮤니티", "수익화"), Semester.FIFTH, List.of(Type.WEB)),
    HOWS_THE_WEAR("hows the wear", "날씨정보와 패션정보를 한 번에", "날씨보러 왔다가 옷 고르고 가는 플랫폼", "", List.of("편리함", "날씨와패션", "정보제공"), Semester.FIFTH, List.of(Type.IOS)),
    NANEUN_JIBSA("나는 집사", "반려동물 입양 서비스" ,"당신이 곁엔 항상 반려견이, 반려동물 입양과 커뮤니티", "", List.of("반려견", "입양", "I am 집사에요"), Semester.FIFTH, List.of(Type.IOS)),
    LIFE_SHARING("LIFE SHARING", "국내 쉐어링 서비스", "일상생활 속 잠시 필요한 물품 바로 여기에! 국내 쉐어링 서비스", "", List.of("쉐어링", "일상생활", "공유경제"), Semester.FIFTH, List.of(Type.AOS)),
    GROW_UP("그로우업", "자기계발의 모든 것", "그로우업 갓생을 살고 싶은 자!", "", List.of("갓생", "자기계발", "미라클모닝"), Semester.FIFTH, List.of(Type.WEB)),
    LIVIEW("LIVIEW", "일기, 세상에서 가장 재미있는 책", "위치기반 일상기록 및 시각화 어플", "", List.of("자동일기 작성", "직관성", "기록"), Semester.FIFTH, List.of(Type.WEB)),
    BOOK_SENTIMENT_LEAGUE("Book Sentiment League", "독후감 경쟁 커뮤니티", "사용자들이 독후감(센티멘트)을 작성하고 공유하며 랭킹을 경쟁하는 플랫폼", "", List.of("독후감", "센티멘트", "랭킹"), Semester.FIFTH, List.of(Type.WEB)),
    // CHEMI
    DDOKRIP("똑립", "똑! 부러지는 독립생활", "사회초년생들의 똑! 부러지는 독립을 위한 정보 공유 커뮤니티 플랫폼", "", List.of("정보공유", "커뮤니티", "독립"), Semester.FIFTH, List.of(Type.AOS)),
    DDUBUK("뚜벅", "AR을 활용한 산책촉진 플랫폼", "뚜벅은 AR을 활용한 게이미피케이션 요소를 통해 걷기에 게임재미를 더한 플랫폼입니다.", "", List.of("걷기", "산책", "게임"), Semester.FIFTH, List.of(Type.IOS)),
    JJIKJJIKI("찍찍이", "갤러리에서 다른 사진들과 소중한 추억 섞어 보관하지 말자!!!", "갤러리에서 다른 사진들과 소중한 추억을 섞어 보관하지 말고, 소중한 추억은 온전하게 보존하자", "", List.of("네컷사진", "앨범", "추억"), Semester.FIFTH, List.of(Type.IOS)),
    CUPI("큐피", "당신의 호기심을 찐전문가가 해결해줍니다!", "궁금한 것을 대학교수가 답해줍니다!", "", List.of("전문가", "답변"), Semester.FIFTH, List.of(Type.AOS, Type.WEB)),
    ONANDOFF("온앤오프", "일과 라이프의 밸런스를 도와주는 워라밸 서비스", "회고를 통해 일잘러로 성장하고, 일의 on&off 를 도와주는 워라벨 서비스", "", List.of("회고", "직장인", "워라벨"), Semester.FIFTH, List.of(Type.IOS)),
    VI_NO("VI.NO", "영상보다 글이 편한 당신을 위한, 영상 블로그화 솔루션", "", "", List.of("영상정리", "영상요약", "생성형AI"), Semester.FIFTH, List.of(Type.WEB)),
    MOAMOA("모아모아", "상품을 모아 사람을 모아 당신의 합리적 소비를 돕기 위한 공동 구매 플랫폼", "지역 기반으로 동네 이웃들과 함께 공동 구매를 가능하게 도와주는 플랫폼", "", List.of("공동 구매", "합리적 소비", "이웃"), Semester.FIFTH, List.of(Type.WEB)),
    WITHYOU("with 'You'", "함께 여행하는 당신과의 Travel Log", "여행을 함께하는 그 사람(당신)과의 Travel Log / 추억 저장소", "", List.of("함께 쓰는 여행", "with who?!"), Semester.FIFTH, List.of(Type.IOS)),
    ECOLINK("에코링크", "Zero-Waste 샵 조회 플랫폼", "서울 내 다양한 지역에서 운영되고 있는 제로웨이스트 샵들의 위치와 판매 제품 정보, 매장 이용 방법, 그리고 매장 별 다양한 이벤트를 한눈에 확인할 수 있는 플랫폼 서비스입니다.", "", List.of("친환경"), Semester.FIFTH, List.of(Type.WEB)),
    ;

    private final String name;
    private final String slogan;
    private final String description;
    private final String image;
    private final List<String> tags;
    private final Semester semester;
    private final List<Type> types;


}
