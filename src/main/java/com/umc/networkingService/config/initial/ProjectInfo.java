package com.umc.networkingService.config.initial;

import com.umc.networkingService.domain.project.entity.ProjectType;
import com.umc.networkingService.global.common.enums.Semester;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ProjectInfo {

    // 3기
    TODAYSGYM("오늘의 짐", "오늘의 짐(GYM) - 아바타와 함께 성장하는 운동 기록 SNS", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/THIRD/%EC%98%A4%EB%8A%98%EC%9D%98+%EC%A7%90.png", List.of("운동기록", "캘린더", "아바타"), Semester.THIRD, List.of(ProjectType.AOS)),

    // 4기
    OWORI("오월이", " 모여봐요 우리가족, 우리가족만의 소중한 추억을 기록하고 소통해요", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FOURTH/%EC%98%A4%EC%9B%94%EC%9D%B4+%EB%A1%9C%EA%B3%A0.png", List.of("가족", "소통", "추억"), Semester.FOURTH, List.of(ProjectType.IOS)),
    ARF("알프", "교내 재학생 - 교환학생 커뮤니티 및 모임 플랫폼", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FOURTH/%EC%95%8C%ED%94%84.png", List.of("안전", "외국인", "대학생"), Semester.FOURTH, List.of(ProjectType.IOS)),
    MYOJIPSA("묘집사", "미션형 커뮤니티 서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FOURTH/%EB%AC%98%EC%A7%91%EC%82%AC.svg", List.of("커뮤니티", "토끼", "미션"), Semester.FOURTH, List.of(ProjectType.AOS)),


    // 5기
    // GACI
    CAMPUS_NOTE("캠퍼스 노트", "시간표 연동 강의 필기 앱, 캠퍼스 노트 어플리케이션", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/Campus+Note+-+%EC%98%AC%EC%9D%B8%EC%9B%90+%EA%B0%95%EC%9D%98+%ED%95%84%EA%B8%B0+%EC%95%B1.gif", List.of("필기", "노트", "대학"), Semester.FIFTH, List.of(ProjectType.AOS)),
    TEMBURIN("템버린", "일상생활에서 사용하고 있는 item의 교체 주기를 위한 알림을 제공하여 잊어버리지 않도록 도와주는 어플리케이션", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%ED%85%9C%EB%B2%84%EB%A6%B0+-+%EC%95%84%EC%9D%B4%ED%85%9C+%EC%9C%A0%ED%9A%A8%EA%B8%B0%EA%B0%84+%EC%A4%91%EB%B3%B5+%EC%95%8C%EB%A6%BC+%EC%84%9C%EB%B9%84%EC%8A%A4.svg", List.of("알림", "교체", "생활용품"), Semester.FIFTH, List.of(ProjectType.AOS)),
    HONJABDA("혼잡다", "카페 혼잡도를 미리 파악할 수 있는 서비스 어플리테이션", "", List.of("편리함", "시간 단축", "상생"), Semester.FIFTH, List.of(ProjectType.IOS)),
    TAPE("Tape", "주위 사람들과 음악 취향 경험을 공유하는 소셜 네트워크 서비스", "", List.of("음악", "공유", "소통"), Semester.FIFTH, List.of(ProjectType.AOS)),
    PLAN_ME("PLAN ME", "자유롭게 커스터마이징하는 나만의 플래너 서비스", "", List.of("편리함", "한번에", "특별한"), Semester.FIFTH, List.of(ProjectType.AOS)),
    UMJUMISIKHOI("음주미식회", "사용자들에게 음식과 맞는 주종을 추천해주고 커뮤니티를 통해서 다른 사람의 베스트 조합을 볼 수 있고 이야기를 나눌 수 있는 어플리케이션", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EC%9D%8C%EC%A3%BC%EB%AF%B8%EC%8B%9D%ED%9A%8C+-+%EC%9D%8C%EC%8B%9D%EA%B8%B0%EB%B0%98+%EC%A3%BC%EB%A5%98+%EC%B6%94%EC%B2%9C+%26+%EC%BB%A4%EB%AE%A4%EB%8B%88%ED%8B%B0+%EC%84%9C%EB%B9%84%EC%8A%A4.png", List.of("음식", "주류", "조합"), Semester.FIFTH, List.of(ProjectType.IOS)),
    HAGOSIMDA("하고심다", "나만의 정원을 가꾸는 일정 관리 서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%ED%95%98%EA%B3%A0%EC%8B%AC%EB%8B%A4-%EB%82%98%EB%A7%8C%EC%9D%98+%EC%A0%95%EC%9B%90%EC%9D%84+%EA%B0%80%EA%BE%B8%EB%8A%94+%EC%9D%BC%EC%A0%95+%EA%B4%80%EB%A6%AC+%EC%84%9C%EB%B9%84%EC%8A%A4.svg", List.of("나만의 정원", "일정관리", "투두리스트"), Semester.FIFTH, List.of(ProjectType.AOS, ProjectType.WEB)),
    MARIAJEU("마리아주", "어떠한 술과도 가장 행복한 추억을 줄 수 있는 마리아주 레시피 서비스", "", List.of("페어링(완전한 궁합)", "즐거움(놀이)", "공유 및 리뷰"), Semester.FIFTH, List.of(ProjectType.AOS, ProjectType.WEB)),
    UMC_NETWORKING_SERVICE("UMC", "연합 동아리 네트워킹 서비스", "", List.of("연합", "네트워킹", "히스토리"), Semester.FIFTH, List.of(ProjectType.IOS, ProjectType.WEB)),
    CLIMUS("클라이머스", "클라이머들이 꿈꾸던 볼더링 문제 풀이 플랫폼", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%5B%ED%81%B4%EB%B0%8B%5D+%ED%81%B4%EB%9D%BC%EC%9D%B4%EB%A8%B8%EB%A5%BC+%EC%9C%84%ED%95%9C+%EB%B3%BC%EB%8D%94%EB%A7%81+%EB%A3%A8%ED%8A%B8+%ED%92%80%EC%9D%B4+%ED%94%8C%EB%9E%AB%ED%8F%BC.svg", List.of("클라이밍", "볼더링"), Semester.FIFTH, List.of(ProjectType.AOS)),
    REVIEW_ZIP("Review.zip", "장소 리뷰 공유 서비스 a.k.a 립스타그램", "", List.of("장소", "리뷰", "친구"), Semester.FIFTH, List.of(ProjectType.WEB)),
    UMC_MATCHING_CENTER("UMC Matching Center", "UMC 팀 매칭을 하나의 웹 페이지로, 한 번에, 더 편하게", "", List.of("팀 매칭", "All in 1", "편리함"), Semester.FIFTH, List.of(ProjectType.WEB)),
    RE_MEMORY("RE:MEMORY", "단순히 기억을 보관하는 것을 넘어서, 추억과 감정을 선물하는 타임캡슐 서비스. RE:memory", "", List.of("타임캡슐", "편지", "롤링페이퍼"), Semester.FIFTH, List.of(ProjectType.WEB)),
    // SQUARE
    JUINJANG("주인장", "내 손안의 완벽한 부동산 임장", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EC%A3%BC%EC%9D%B8%EC%9E%A5%2C+%EB%82%B4+%EC%86%90%EC%95%88%EC%9D%98+%EC%99%84%EB%B2%BD%ED%95%9C+%EB%B6%80%EB%8F%99%EC%82%B0+%EC%9E%84%EC%9E%A5.svg", List.of("생산성", "임장", "체크리스트"), Semester.FIFTH, List.of(ProjectType.IOS)),
    BLOOD_TRAIL("BloodTrail", "헌혈 원포인트 어플", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/Blood+Trail+-+%ED%97%8C%ED%98%88+%EC%9B%90%ED%8F%AC%EC%9D%B8%ED%8A%B8+%ED%94%8C%EB%9E%AB%ED%8F%BC.gif", List.of("헌혈", "커뮤니티"), Semester.FIFTH, List.of(ProjectType.WEB)),
    NANGMAN_CAT("낭만 고양이", "마음을 전달하는 모바일 편지 서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EB%82%AD%EB%A7%8C+%EA%B3%A0%EC%96%91%EC%9D%B4+-+%EB%A7%88%EC%9D%8C%EC%9D%84+%EC%A0%84%EB%8B%AC%ED%95%98%EB%8A%94+%EB%AA%A8%EB%B0%94%EC%9D%BC+%ED%8E%B8%EC%A7%80+%EC%84%9C%EB%B9%84%EC%8A%A4.png", List.of("느린 우체통", "개성", "소통"), Semester.FIFTH, List.of(ProjectType.WEB)),
    UJU_JEONGGEOJANG("우주 정거장", "나를 위한 글쓰기", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EC%9A%B0%EC%A3%BC%EC%A0%95%EA%B1%B0%EC%9E%A5_%EB%82%98%EC%9D%98+%EC%83%9D%EA%B0%81%EC%9D%B4+%EC%A0%95%EC%B0%A9%ED%95%A0+%EA%B3%B3%2C+%EA%B8%80%EC%93%B0%EA%B8%B0+%ED%94%8C%EB%9E%AB%ED%8F%BC.svg", List.of("글쓰기", "몰입", "자기개발"),Semester.FIFTH, List.of(ProjectType.WEB)),
    HOMEAT("Homeat", "챌린지형 식비 관리 커뮤니티 어플", "", List.of("1인가구", "식비관리", "정보공유"), Semester.FIFTH, List.of(ProjectType.IOS)),
    FORGRAD("FORGRAD", "당신의 졸업을 위한 모든 것! 졸업정보서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%ED%8F%AC%EC%96%B4%EA%B7%B8%EB%9E%98%EB%93%9C+-+%EB%8B%B9%EC%8B%A0%EC%9D%98+%EC%A1%B8%EC%97%85%EC%9D%84+%EB%8F%95%EB%8A%94+%EC%A1%B8%EC%97%85%EC%A0%95%EB%B3%B4%EC%84%9C%EB%B9%84%EC%8A%A4.svg", List.of("졸업", "소통", "편리함"), Semester.FIFTH, List.of(ProjectType.AOS)),
    INTERPHONE("인터폰!", "자신에게 맞는 기업추천 서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EC%9D%B8%ED%84%B0%ED%8F%B0-%EC%9D%B8%ED%84%B4+%EB%AA%A8%EC%A7%91%EA%B3%B5%EA%B3%A0+%EB%B0%8F+%EC%BB%A4%EB%AE%A4%EB%8B%88%ED%8B%B0+%EC%84%9C%EB%B9%84%EC%8A%A4.svg", List.of("인턴", "대학생", "스펙쌓기"), Semester.FIFTH, List.of(ProjectType.WEB)),
    DONGNEHYEONG("동네형", "당신 근처의 ‘동네형’에게 운동을 배우세요.", "", List.of("헬스", "PT"), Semester.FIFTH, List.of(ProjectType.WEB)),
    CAREER_FESTIVAL("Career Festival", "내 커리어에 맞는 오프라인 행사 매칭 및 기록 서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/Career+Festival+-+%EB%82%B4+%EC%BB%A4%EB%A6%AC%EC%96%B4%EC%97%90+%EB%A7%9E%EB%8A%94+%EC%98%A4%ED%94%84%EB%9D%BC%EC%9D%B8+%ED%96%89%EC%82%AC+%EB%A7%A4%EC%B9%AD+%EB%B0%8F+%EA%B8%B0%EB%A1%9D+%EC%84%9C%EB%B9%84%EC%8A%A4.svg", List.of("커리어", "자기개발", "행사"), Semester.FIFTH, List.of(ProjectType.WEB)),
    TRAVEL_COMPASS("Travel Compass", "간편한 여행계획 설계 서비스", "", List.of("간편함", "여행계획", "한큐에딱"), Semester.FIFTH, List.of(ProjectType.WEB)),
    TICKET_TAKA("Ticket-taka", "우리 티켓으로 소통하자! 불필요한 소통은 줄이고 프로젝트에 집중할 수 있는 환경을 만들어주는 서비스", "", List.of("티켓", "프로젝트", "관리"),Semester.FIFTH, List.of(ProjectType.IOS)),
    BID("bid", "판매자 구매자의 경매 형 중고 거래 플랫폼!", "", List.of("경매 형", "기댓값", "중고 거래"), Semester.FIFTH, List.of(ProjectType.WEB)),
    ANDDEUL("안뜰", "가족 중심 네트워킹 어플", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EC%95%88%EB%9C%B0+-+%EA%B0%80%EC%A1%B1+%EC%A4%91%EC%8B%AC+SNS+%EC%84%9C%EB%B9%84%EC%8A%A4.png", List.of("따듯함", "일상공유", "마음전하기"), Semester.FIFTH, List.of(ProjectType.AOS)),
    AIRECIPE("아이레시피", "AI 챗봇 기반 맞춤형 레시피 서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EC%95%84%EC%9D%B4%EB%A0%88%EC%8B%9C%ED%94%BC(IRECIPE)+-+AI%EC%99%80+%ED%95%A8%EA%BB%98%ED%95%98%EB%8A%94+%EB%82%98%EB%A7%8C%EC%9D%98+%EB%83%89%EC%9E%A5%EA%B3%A0+%EB%A0%88%EC%8B%9C%ED%94%BC.png", List.of("친환경", "레시피", "간편함"), Semester.FIFTH, List.of(ProjectType.AOS)),
    BE_ILSANG("비일상", "친환경 실천을 위한 챌린지 및 커뮤니티 플랫폼 서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EB%B9%84%EC%9D%BC%EC%83%81+-+%EC%B9%9C%ED%99%98%EA%B2%BD+%EC%B1%8C%EB%A6%B0%EC%A7%80+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8.png", List.of("친환경", "챌린지"), Semester.FIFTH, List.of(ProjectType.IOS)),
    ARTORY("ARTORY", "나만의 문화 이야기", "", List.of("문화 생활", "기록", "커뮤니티"), Semester.FIFTH, List.of(ProjectType.WEB)),
    THE_SCULPTOR("The Sculptor", "스스로의 인생을 스스로 조각하는 인생의 조각가가 되어보세요.", "", List.of("자기개발", "일관성", "SNS"), Semester.FIFTH, List.of(ProjectType.AOS)),
    GONGJAKSO("공작소", "공모전/프로젝트 관련 공고를 확인하고 팀빌딩을 돕는 서비스, 공작소", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EA%B3%B5%EC%9E%91%EC%86%8C-%EA%B3%B5%EB%AA%A8%EC%A0%84-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8+%ED%8C%80%EB%B9%8C%EB%94%A9+%EC%84%9C%EB%B9%84%EC%8A%A4.png", List.of("팀 빌딩", "캘린더"), Semester.FIFTH, List.of(ProjectType.WEB)),
    // W
    STUDY_FLEX("STUDY FLEX", "팀 스터디를 통해 성장하고 싶은 당신, 캠퍼스별 팀스터디 매칭 관리 웹 서비스 Study Flex에서 성장하세요!", "", List.of("팀 스터디", "매칭"), Semester.FIFTH, List.of(ProjectType.WEB)),
    GUSTO("Gusto", "나만의 맛집을 저장하는 서비스!", "", List.of("나만의", "맛집", "지도"), Semester.FIFTH, List.of(ProjectType.AOS)),
    DDODDOGA("또또가", "리뷰어와 상점을 연결하는 리뷰어 마케팅 중개 서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EB%98%90%EB%98%90%EA%B0%80+-+%EB%82%B4%EB%8F%88%EB%82%B4%EC%82%B0+%EB%A6%AC%EB%B7%B0+%EC%BB%A4%EB%A8%B8%EC%8A%A4+%ED%94%8C%EB%9E%AB%ED%8F%BC.png", List.of("리뷰", "마케팅"), Semester.FIFTH, List.of(ProjectType.WEB)),
    CHOIAEEU_JANGSO("최애의 장소", "일본 애니메이션 성지순례 정보 제공 서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EC%B5%9C%EC%95%A0%EC%9D%98+%EC%9E%A5%EC%86%8C.png", List.of("성지순례", "최애"), Semester.FIFTH, List.of(ProjectType.AOS)),
    BRUSHWORK("BRUSHWORK",  "미대생 졸업작품 판매 웹 서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/Brushwork+-+%EB%AF%B8%EB%8C%80%EC%83%9D+%EC%A1%B8%EC%97%85%EC%9E%91%ED%92%88+%ED%8C%90%EB%A7%A4+%EC%84%9C%EB%B9%84%EC%8A%A4.png", List.of("미대생", "졸업작품", "채팅"), Semester.FIFTH, List.of(ProjectType.WEB)),
    PROUST("프루스트", "너의 취’향’을 찾고 싶어? ✨ -  향수 정보 커뮤니티 앱", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%ED%94%84%EB%A3%A8%EC%8A%A4%ED%8A%B8(PROUST).png", List.of("커뮤니티", "감성", "편리함"), Semester.FIFTH, List.of(ProjectType.WEB)),
    OLREA("올래", "청춘은 바로 지금! 시니어를 위한 교육 매칭 서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EC%98%AC%EB%9E%98+-+%EC%8B%9C%EB%8B%88%EC%96%B4%EB%A5%BC+%EC%9C%84%ED%95%9C+%EA%B5%90%EC%9C%A1+%EB%A7%A4%EC%B9%AD+%EC%9B%B9+%EC%84%9C%EB%B9%84%EC%8A%A4.png", List.of("시니어", "교육", "매칭"), Semester.FIFTH, List.of(ProjectType.WEB)),
    MART_ALL("마트올",  "동네마트 온라인 장보기", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EB%A7%88%ED%8A%B8%EC%98%AC.png", List.of("365", "멤버십", "소통"), Semester.FIFTH, List.of(ProjectType.AOS)),
    SUBMARINE("몰입", "submarine (써머린) : 잠수함이라는 뜻으로 심해에 빠진 듯한 몰입감을 줄 수 있는 앱", "", List.of("몰입", "챌린지"), Semester.FIFTH, List.of(ProjectType.IOS)),
    TO_GETHER("TO:gether", "TOgether, 모든 곳에서 함께하는 우리의 동반인 찾기 서비스", "", List.of("다양성", "편리함", "연결"), Semester.FIFTH, List.of(ProjectType.WEB)),
    BAB_ZIP("밥zip", "실시간 캠퍼스 식당 정보 서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EB%B0%A5ZIP.png", List.of("음식", "대학", "메이트"), Semester.FIFTH, List.of(ProjectType.WEB)),
    UMARK("umark", "대학생이 경험하고 느끼는 것을 담은 큐레이션 공간", "", List.of("대학생", "큐레이션", "북마크"), Semester.FIFTH, List.of(ProjectType.WEB)),
    // HEDGES
    TEACHER_FOR_BOSS("티쳐 포 보스", "자영업자 솔루션 서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%ED%8B%B0%EC%B3%90%ED%8F%AC%EB%B3%B4%EC%8A%A4.svg", List.of("자영업", "사장"), Semester.FIFTH, List.of(ProjectType.AOS)),
    GLOBAL_STUDENTS("Global Students", "유학생 개방형 커뮤니티", "", List.of("유학생", "개방형 커뮤티니", "생활편의"), Semester.FIFTH, List.of(ProjectType.WEB)),
    COUPLE("커플", "", "데이트 코스 랜덤 추천 서비스", List.of("데이트", "커플"), Semester.FIFTH, List.of(ProjectType.IOS)),
    A_WRITE("각사각", "당신의 감정을 사각사각, 국내 자기 성장 도우미 서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EC%82%AC%EA%B0%81%EC%82%AC%EA%B0%81+(A-write).png", List.of("통합성", "간편함", "기록적인"), Semester.FIFTH, List.of(ProjectType.AOS)),
    SURVEY_MATE("썰매", "설문 조사 중개를 위한 C2C 서비스",  "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EC%8D%B0%EB%A7%A4+(Survey+Mate).svg", List.of("설문 조사", "신뢰", "앱테크"), Semester.FIFTH, List.of(ProjectType.WEB)),
    YOUNET("Younet", "유학생을 위한 네트워크", "", List.of("유학생", "네트워크", "연결고리"), Semester.FIFTH, List.of(ProjectType.IOS)),
    CUMO("쿠모", "세상의 모든 종이 쿠폰을 모아주는 서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EC%BF%A0%EB%AA%A8(COUMO).svg", List.of("짠테크", "간편함", "쿠폰"), Semester.FIFTH, List.of(ProjectType.AOS, ProjectType.WEB)),
    EASY_EXCEL("이지 엑셀", "슬기로운 엑셀사용을 위한 엑셀 함수/단축키 통합정리 서비스", "", List.of("편리함", "액셀", "단축키"), Semester.FIFTH, List.of(ProjectType.WEB)),
    BONGURI("봉우리", "봉사활동을 쉽고 재미있게! 봉사활동 통합 플랫폼 서비스, 봉우리입니다", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EB%B4%89%EC%9A%B0%EB%A6%AC.svg", List.of("봉사", "통합", "유입&지속"), Semester.FIFTH, List.of(ProjectType.AOS)),
    BODY_CAPTURE("BodyCapture", "바디프로필 예약 ALL IN ONE 서비스", "", List.of("올인원", "AtoZ", "도우미"), Semester.FIFTH, List.of(ProjectType.IOS)),
    PACK_IT("Pack It", "집에 잠든 개인용기로 동네를 깨우다", "", List.of("친환경", "지역상생", "게이미피케이션"), Semester.FIFTH, List.of(ProjectType.AOS)),
    NANGJANGGO("냉장고를 부탁해", "나의 냉장고에 어떤 식재료가 있는지!  이번달에는 어떤 식재료를 사왔는지 한눈에 알 수 있는 서비스!", "", List.of("냉장고", "식재로"), Semester.FIFTH, List.of(ProjectType.WEB)),
    MUFFLER("머플러", "소비 목표 달성을 도와주는 가계부", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EB%A8%B8%ED%94%8C%EB%9F%AC(Money+Planner).svg", List.of("세세한 계획", "내 일정에 따라 나누는 계획"), Semester.FIFTH, List.of(ProjectType.IOS)),
    PEER_RE("PEER:Re", "건강한 협력을 위한 “선택형” 동료평가 서비스, PEER : Re (피어리)", "", List.of("간편함", "선택형", "동료평가"),Semester.FIFTH, List.of(ProjectType.WEB)),
    TEAMMATE("팀메이트", "모든 생활 체육을 함께 즐길 메이트를 쉽게 매칭해주는 서비스!", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%ED%8C%80%EB%A9%94%EC%9D%B4%ED%8A%B8.png", List.of("매칭", "운동", "개인정보보호"), Semester.FIFTH, List.of(ProjectType.WEB)),
    BAB_CHINGU("밥 친구", "식사 상황별 콘텐츠 추천 서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EB%B0%A5+%EC%B9%9C%EA%B5%AC.svg", List.of("콘텐츠", "추천", "개인화"), Semester.FIFTH, List.of(ProjectType.WEB)),
    // KSSS
    ABOUT_ME("AboutMe", "다양한 나, 다양한 관계의 시작", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/aboutMe.png", List.of("멀티프로필", "명함", "커스텀마이징"), Semester.FIFTH, List.of(ProjectType.AOS)),
    DRAW_DESKTOP("Draw Desktop", "바탕화면을 꾸미다", "", List.of("바탕화면", "스티커", "꾸미기"), Semester.FIFTH, List.of(ProjectType.WEB)),
    HERE_YOU("Here You", "기록으로 나를 찾는 여행 아카이빙 커뮤니티", "", List.of("기록", "여행", "커뮤티니"), Semester.FIFTH, List.of(ProjectType.WEB)),
    RHYTHM_PALETTE("Rhythm Palette", "리듬 팔레트는 공유하고 싶은 음악, 상황, 감정을 ai가 생성해주는 이미지와 함께 게시할 수 있는 SNS 서비스 입니다.", "", List.of("음악공유", "이미지생성", "SNS"), Semester.FIFTH, List.of(ProjectType.WEB)),
    WRITEROOM("Writeroom", "자유로운 창작의 공간", "", List.of("자유로움", "글쓰기 연습", "공간"), Semester.FIFTH, List.of(ProjectType.WEB)),
    DAON("다온", "암환자들을 위한 하루 기록 서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EB%8B%A4%EC%98%A8.svg", List.of("암","마이데이터", "일기"), Semester.FIFTH, List.of(ProjectType.AOS)),
    THE_GOODS("The GOODs", "개인 판매자 대상 온라인 주문서 플랫폼", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EB%8D%94+%EA%B5%BF%EC%A6%88.png", List.of("덕질", "굿즈", "오픈마켓"), Semester.FIFTH, List.of(ProjectType.WEB)),
    AVAV("아브아브", "얼음같은 사회를 녹여줄 레크레이션 종합 플랫폼 서비스 아브아브입니다!", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EC%95%84%EB%B8%8C%EC%95%84%EB%B8%8C(AvAb)-%EB%A0%88%ED%81%AC%EB%A0%88%EC%9D%B4%EC%85%98%EC%9D%84+%EB%B3%B4%EB%8B%A4+%EC%89%BD%EA%B2%8C.svg", List.of("레크레이션", "아이스브레이킹", "남녀노소"), Semester.FIFTH, List.of(ProjectType.WEB)),
    MEME("메메", "나만의 메이크업 메이트, 메메!", "", List.of("개인화", "매칭", "커뮤니티"), Semester.FIFTH, List.of(ProjectType.IOS)),
    BRANDOL("BRANDOL", "우리가 만들어가는 브랜드, BRANDOL - 브랜드 팬덤 커뮤니티 앱", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/BRANDOL.png", List.of("브랜딩", "콘텐츠", "팬"), Semester.FIFTH, List.of(ProjectType.AOS)),
    MY_PLACE("마플", "사용자가 등록한 관심 장소를 기반으로 장소를 제안하고 장소를 기록하는 서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EB%A7%88%ED%94%8C.png", List.of("관심장소", "기록"), Semester.FIFTH, List.of(ProjectType.IOS)),
    SPON_US("Spon-us", "대학생 단체와 기업 간의 협찬/제휴/연계프로그램 (이하 협력) 컨택을 도우는 서비스입니다.", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EC%8A%A4%ED%8F%AC%EB%84%88%EC%8A%A4.png", List.of("편리함", "매칭", "도우미"), Semester.FIFTH, List.of(ProjectType.IOS)),
    ISA_ZIP("이사.zip", "이사 혹은 주거지에 대한 정보와 편리함을 담고있는 어플", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EC%9D%B4%EC%82%ACzip.png", List.of("매칭", "편리성", "접근성"), Semester.FIFTH, List.of(ProjectType.AOS)),
    GOMIN_CHINGU("고민친구", "세상의 모든 고민이 거쳐가는 공간, 고민친구", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EA%B3%A0%EB%AF%BC%EC%B9%9C%EA%B5%AC.png", List.of("고민해결", "결정장애", "의사결정능력"), Semester.FIFTH, List.of(ProjectType.WEB)),
    // NEPTUNE
    MUGGLE("일상의 기록", "나의 일상을 자동으로 기록하는 편리한 일기 작성 서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EC%9D%BC%EC%83%81%EC%9D%98+%EA%B8%B0%EB%A1%9D+-+%EC%9D%BC%EC%83%81%EC%9D%84+%EB%82%A8%EA%B8%B0%EB%8A%94+%EA%B0%80%EC%9E%A5+%EA%B0%84%EB%8B%A8%ED%95%9C+%EB%B0%A9%EB%B2%95.png", List.of("편리함", "나만의 이야기", "아름다운 추억"), Semester.FIFTH, List.of(ProjectType.AOS)),
    BABIN("바빈", "교내 시설물 활성화 서비스", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EC%A7%80%EA%B8%88+%EC%9A%B0%EB%A6%AC+%ED%95%99%EA%B5%90%EB%8A%94+-+%EA%B5%90%EB%82%B4+%EC%8B%9C%EC%84%A4%EB%AC%BC+%EC%98%88%EC%95%BD+%EC%84%9C%EB%B9%84%EC%8A%A4.svg", List.of("교내 시설물 활성화", "보물 찾기"), Semester.FIFTH, List.of(ProjectType.WEB)),
    FRIEND("Friend", "부경대학교 랜덤매칭 서비스", "", List.of("부경대 핑캠 매칭", "지인 매칭 방지"), Semester.FIFTH, List.of(ProjectType.WEB)),
    CC("CC(Creative Connect)", "같이 만들어가는 1인미디어 커뮤니티, CC(Creative Connect)입니다", "", List.of("1인 크리에이터", "커뮤니티", "수익화"), Semester.FIFTH, List.of(ProjectType.WEB)),
    HOWS_THE_WEAR("hows the wear", "날씨보러 왔다가 옷 고르고 가는 플랫폼", "", List.of("편리함", "날씨와패션", "정보제공"), Semester.FIFTH, List.of(ProjectType.IOS)),
    NANEUN_JIBSA("나는 집사", "당신이 곁엔 항상 반려견이, 반려동물 입양과 커뮤니티", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EB%82%98%EB%8A%94+%EC%A7%91%EC%82%AC.svg", List.of("반려견", "입양", "I am 집사에요"), Semester.FIFTH, List.of(ProjectType.IOS)),
    LIFE_SHARING("LIFE SHARING", "일상생활 속 잠시 필요한 물품 바로 여기에! 국내 쉐어링 서비스", "", List.of("쉐어링", "일상생활", "공유경제"), Semester.FIFTH, List.of(ProjectType.AOS)),
    GROW_UP("그로우업", "자기계발의 모든 것", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EA%B7%B8%EB%A1%9C%EC%9A%B0%EC%97%85-%EC%9E%90%EA%B8%B0%EA%B3%84%EB%B0%9C+%EC%BB%A4%EB%AE%A4%EB%8B%88%ED%8B%B0.svg", List.of("갓생", "자기계발", "미라클모닝"), Semester.FIFTH, List.of(ProjectType.WEB)),
    LIVIEW("LIVIEW", "위치기반 일상기록 및 시각화 어플", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EB%9D%BC%EC%9D%B4%EB%B7%B0(LIVIEW)-%EC%A7%80%EB%8F%84%EB%A5%BC+%ED%86%B5%ED%95%B4+%ED%95%9C+%EB%88%88%EC%97%90+%EB%B3%B4%EB%8A%94+%EB%82%B4+%EC%82%AC%EC%A7%84%EA%B8%B0%EB%A1%9D.svg", List.of("자동일기 작성", "직관성", "기록"), Semester.FIFTH, List.of(ProjectType.WEB)),
    BOOK_SENTIMENT_LEAGUE("Book Sentiment League", "사용자들이 독후감(센티멘트)을 작성하고 공유하며 랭킹을 경쟁하는 플랫폼", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/Book+Sentiment+League+-+%EB%8F%85%ED%9B%84%EA%B0%90+%EA%B2%BD%EC%9F%81+%EC%BB%A4%EB%AE%A4%EB%8B%88%ED%8B%B0.png", List.of("독후감", "센티멘트", "랭킹"), Semester.FIFTH, List.of(ProjectType.WEB)),
    // CHEMI
    DDOKRIP("똑립", "사회초년생들의 똑! 부러지는 독립을 위한 정보 공유 커뮤니티 플랫폼", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EB%98%91%EB%A6%BD+-+%EC%82%AC%ED%9A%8C%EC%B4%88%EB%85%84%EC%83%9D%EC%9D%98+%EB%98%91!+%EB%B6%80%EB%9F%AC%EC%A7%80%EB%8A%94+%EB%8F%85%EB%A6%BD%EC%9D%84+%EC%9C%84%ED%95%9C+%EC%A0%95%EB%B3%B4+%EA%B3%B5%EC%9C%A0+%EC%BB%A4%EB%AE%A4%EB%8B%88%ED%8B%B0+%ED%94%8C%EB%9E%AB%ED%8F%BC.png", List.of("정보공유", "커뮤니티", "독립"), Semester.FIFTH, List.of(ProjectType.AOS)),
    DDUBUK("뚜벅", "뚜벅은 AR을 활용한 게이미피케이션 요소를 통해 걷기에 게임재미를 더한 플랫폼", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EB%9A%9C%EB%B2%85+-+AR%EC%82%B0%EC%B1%85%EB%A7%A4%EC%B9%AD+%ED%94%8C%EB%9E%AB%ED%8F%BC.png", List.of("걷기", "산책", "게임"), Semester.FIFTH, List.of(ProjectType.IOS)),
    JJIKJJIKI("찍찍이", "갤러리에서 다른 사진들과 소중한 추억 섞어 보관하지 말자!!!", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EC%B0%8D%EC%B0%8D%EC%9D%B4+-+%EC%86%8C%EC%A4%91%ED%95%9C+%EC%B6%94%EC%96%B5%EC%9D%98+%EC%98%A8%EC%A0%84%ED%95%9C+%EB%B3%B4%EC%A1%B4%EC%9D%84+%EA%BF%88%EA%BE%B8%EB%8A%94+%EC%84%9C%EB%B9%84%EC%8A%A4.svg", List.of("네컷사진", "앨범", "추억"), Semester.FIFTH, List.of(ProjectType.IOS)),
    CUPI("큐피", "당신의 호기심을 찐전문가가 해결해줍니다!", "", List.of("전문가", "답변"), Semester.FIFTH, List.of(ProjectType.AOS, ProjectType.WEB)),
    ONANDOFF("온앤오프", "회고를 통해 일잘러로 성장하고, 일의 on&off 를 도와주는 워라벨 서비스", "", List.of("회고", "직장인", "워라벨"), Semester.FIFTH, List.of(ProjectType.IOS)),
    VI_NO("VI.NO", "영상보다 글이 편한 당신을 위한, 영상 블로그화 솔루션","", List.of("영상정리", "영상요약", "생성형AI"), Semester.FIFTH, List.of(ProjectType.WEB)),
    MOAMOA("모아모아", "상품을 모아 사람을 모아 당신의 합리적 소비를 돕기 위한 공동 구매 플랫폼", "https://umc-service-bucket.s3.ap-northeast-2.amazonaws.com/project/FIFTH/5%EA%B8%B0+%ED%94%8C%EC%A0%9D+%EC%82%AC%EC%A7%84/%EB%AA%A8%EC%95%84%EB%AA%A8%EC%95%84+-+%EB%8B%B9%EC%8B%A0%EC%9D%98+%ED%95%A9%EB%A6%AC%EC%A0%81+%EC%86%8C%EB%B9%84%EB%A5%BC+%EB%8F%95%EB%8A%94+%EA%B3%B5%EB%8F%99+%EA%B5%AC%EB%A7%A4+%ED%94%8C%EB%9E%AB%ED%8F%BC.svg", List.of("공동 구매", "합리적 소비", "이웃"), Semester.FIFTH, List.of(ProjectType.WEB)),
    WITHYOU("with 'You'", "여행을 함께하는 그 사람(당신)과의 Travel Log / 추억 저장소", "", List.of("함께 쓰는 여행", "with who?!"), Semester.FIFTH, List.of(ProjectType.IOS)),
    ECOLINK("에코링크", "서울 내 다양한 지역에서 운영되고 있는 제로웨이스트 샵들의 위치와 판매 제품 정보, 매장 이용 방법, 그리고 매장 별 다양한 이벤트를 한눈에 확인할 수 있는 플랫폼 서비스", "", List.of("친환경"), Semester.FIFTH, List.of(ProjectType.WEB)),
    ;

    private final String name;
    private final String description;
    private final String image;
    private final List<String> tags;
    private final Semester semester;
    private final List<ProjectType> projectTypes;
}
