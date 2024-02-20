package com.umc.networkingService.domain.test.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.board.service.BoardFileService;
import com.umc.networkingService.domain.board.service.BoardService;
import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.service.BranchService;
import com.umc.networkingService.domain.branch.service.BranchUniversityService;
import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SemesterPart;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.repository.SemesterPartRepository;
import com.umc.networkingService.domain.member.service.AuthService;
import com.umc.networkingService.domain.member.service.MemberService;
import com.umc.networkingService.domain.schedule.dto.request.ScheduleRequest;
import com.umc.networkingService.domain.schedule.service.ScheduleService;
import com.umc.networkingService.domain.university.entity.University;
import com.umc.networkingService.domain.university.service.UniversityService;
import com.umc.networkingService.global.common.enums.Role;
import com.umc.networkingService.global.common.enums.Semester;
import com.umc.networkingService.global.common.exception.RestApiException;
import com.umc.networkingService.global.common.exception.code.GlobalErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

import static com.umc.networkingService.domain.board.dto.request.BoardRequest.BoardCreateRequest;

@Service
@RequiredArgsConstructor
public class TestService {
    private final MemberService memberService;
    private final AuthService authService;
    private final BranchService branchService;
    private final UniversityService universityService;
    private final SemesterPartRepository semesterPartRepository;
    private final BranchUniversityService branchUniversityService;
    private final MemberMapper memberMapper;

    private final BoardService boardService;
    private final BoardFileService boardFileService;
    private final ScheduleService scheduleService;

    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public void CheckFlag(Integer flag) {

        if (flag == 1)
            throw new RestApiException(GlobalErrorCode.TEMP_EXCEPTION);
    }

    @Transactional
    public String createDummyBoard(Member member) {
        List<Member> members = createDummyMember(member);
        if (members.isEmpty()) return "이미 존재합니다.";
        createBoard(members, BoardType.FREE, HostType.CAMPUS);
        createBoard(members, BoardType.FREE, HostType.BRANCH);
        if (!boardService.existsByBoardTypeAndHostType(BoardType.FREE, HostType.CENTER))
            createBoard(members, BoardType.FREE, HostType.CENTER);
        createBoard(members, BoardType.QUESTION, HostType.CAMPUS);
        createBoard(members, BoardType.QUESTION, HostType.BRANCH);
        if (!boardService.existsByBoardTypeAndHostType(BoardType.QUESTION, HostType.CENTER))
            createBoard(members, BoardType.QUESTION, HostType.CENTER);
        createOB(members, HostType.CAMPUS);
        if (!boardService.existsByBoardTypeAndHostType(BoardType.OB, HostType.CENTER))
            createOB(members, HostType.CENTER);
        createNoticeAndWorkbook(members);

        members.forEach(this::createSchedules);

        return "더미데이터 생성 완료";
    }

    @Transactional
    public List<Member> createDummyMember(Member loginMember) {
        Random random = new Random();
        List<Member> members = new ArrayList<>();

        //로그인 한 멤버의 지부와 학교 정보 불러오기
        Branch branch = branchService.loadEntity(loginMember.getBranch().getId());
        University university = universityService.loadEntity(loginMember.getUniversity().getId());

        if (memberService.existsByUniversityAndNicknameAndName(university, "시루", "김루시"))
            return List.of();

        //로그인한 멤버의 지부에 해당하는 university list를 불러오기
        List<University> universities = branchUniversityService.findUniversitiesByBranch(branch);

        //멤버 생성
        for (MemberDummyInfo memberInfo : MemberDummyInfo.values()) {
            //지부 내 학교 중 랜덤 선택
            int randomIndex = random.nextInt(universities.size());
            University randomUniv = universities.get(randomIndex);

            //멤버생성
            Member member = memberService.loadEntity(authService.saveNewDummyMember(UUID.randomUUID().toString()).getMemberId());

            //멤버 정보 생성 후 signup
            List<String> campusPositions = new ArrayList<>();
            List<String> centerPositions = new ArrayList<>();

            if (!memberInfo.getPosition().equals("챌린저")) {
                if (memberInfo.getPosition().equals("운영국장"))
                    centerPositions.add(memberInfo.getPosition());
                else {
                    campusPositions.add(memberInfo.getPosition());
                    randomUniv = university;
                }
            }

            if (!memberInfo.getSemesters().contains(Semester.FIFTH))
                randomUniv = university;

            MemberSignUpRequest request = MemberSignUpRequest.builder()
                    .name(memberInfo.getName())
                    .nickname(memberInfo.getNickname())
                    .universityName(randomUniv.getName())
                    .semesterParts(memberMapper.toSemesterPartInfos(createSemesterPart(member, memberInfo)))
                    .campusPositions(campusPositions)
                    .centerPositions(centerPositions)
                    .build();
            authService.signUp(member, request);

            members.add(member);
        }

        return members;
    }


    public List<SemesterPart> createSemesterPart(Member member, MemberDummyInfo memberInfo) {
        List<SemesterPart> semesterParts = new ArrayList<>();
        for (int i = 0; i < memberInfo.getSemesters().size(); i++)
            semesterParts.add(SemesterPart.builder().member(member).part(memberInfo.getParts().get(i))
                    .semester(memberInfo.getSemesters().get(i)).build());

        return semesterPartRepository.saveAll(semesterParts);
    }

    public void createBoard(List<Member> members, BoardType boardType, HostType hostType) {
        Random random = new Random();
        List<MultipartFile> files = new ArrayList<>();


        for (int i = 0; i < 25; i++) {
            //멤버 랜덤 선택
            int randomIndex = random.nextInt(members.size());
            Member randomMember = members.get(randomIndex);
            BoardCreateRequest request = BoardCreateRequest.builder()
                    .boardType(boardType.toString())
                    .hostType(hostType.toString())
                    .title("데모데이가 얼마 안남았어요..["+boardType + "]" + i)
                    .content("다들 준비는 잘 되가나욥?!?!?! 화이팅화이팅화이팅!!!!!!! 할수있다!!" + i)
                    .build();

            Board board = boardService.loadEntity(boardService.createBoard(randomMember, request, files).getBoardId());
            boardFileService.uploadBoardFilesForDummy(board, getRandomImages());
        }

    }

    private void createNoticeAndWorkbook(List<Member> members) {
        List<MultipartFile> files = new ArrayList<>();
        Member campusStaff = members.stream().filter(member -> member.getRole().equals(Role.CAMPUS_STAFF)).findFirst().get();
        Member branchStaff = members.stream().filter(member -> member.getRole().equals(Role.BRANCH_STAFF)).findFirst().get();
        Member centerStaff = members.stream().filter(member -> member.getRole().equals(Role.CENTER_STAFF)).findFirst().get();

        for (int i = 0; i < 25; i++) {
            BoardCreateRequest request = BoardCreateRequest.builder()
                    .hostType(HostType.CAMPUS.toString())
                    .boardType(BoardType.NOTICE.toString())
                    .title("데모데이 공지입니다." + i)
                    .content("우리학교 UMC 챌린저 분들 화이팅! 데모데이는 19~21일 입니다."+ i)
                    .build();

            BoardCreateRequest request2 = BoardCreateRequest.builder()
                    .hostType(HostType.BRANCH.toString())
                    .boardType(BoardType.NOTICE.toString())
                    .title("지부 네트워킹 데이 공지할게요" + i)
                    .content("여러분~~~~ 이제 UMC 5기도 마지막이에요ㅠㅠ." + i)
                    .build();

            BoardCreateRequest request4 = BoardCreateRequest.builder()
                    .hostType(HostType.CAMPUS.toString())
                    .boardType(BoardType.WORKBOOK.toString())
                    .title(i + "주차 워크북입니다.")
                    .content("워크북 다들화이팅!")
                    .build();


            boardService.createBoard(campusStaff, request, files);
            Board board1 = boardService.loadEntity(boardService.createBoard(campusStaff, request, files).getBoardId());
            boardFileService.uploadBoardFilesForDummy(board1, getRandomImages());
            Board board2 = boardService.loadEntity(boardService.createBoard(branchStaff, request2, files).getBoardId());
            boardFileService.uploadBoardFilesForDummy(board2, getRandomImages());

            if (!boardService.existsByBoardTypeAndHostType(BoardType.NOTICE, HostType.CENTER)) {
                BoardCreateRequest request3 = BoardCreateRequest.builder()
                        .hostType(HostType.CENTER.toString())
                        .boardType(BoardType.NOTICE.toString())
                        .title("UMC 서울 해커톤 개최!" + i)
                        .content("해커톤을 개최합니다 여러분 많은 참여 부탁드려요."+i)
                        .build();

                Board board3 = boardService.loadEntity(boardService.createBoard(centerStaff, request3, files).getBoardId());
                boardFileService.uploadBoardFilesForDummy(board3, getRandomImages());
            }
            Board board4 = boardService.loadEntity(boardService.createBoard(campusStaff, request4, files).getBoardId());
            boardFileService.uploadBoardFilesForDummy(board4, getRandomImages());

        }

    }

    private void createOB(List<Member> members, HostType hostType) {
        List<MultipartFile> files = new ArrayList<>();
        Member obMember = members.stream().filter(member -> !member.getRecentSemester().isActive()).findFirst().get();

        for (int i = 0; i < 25; i++) {
            BoardCreateRequest request = BoardCreateRequest.builder()
                    .hostType(hostType.toString())
                    .boardType(BoardType.OB.toString())
                    .title("저는 OB에요" + i)
                    .content("UMC 챌린저분들 화이팅!!!!!!!!!!!!!!" + i)
                    .build();
            Board board = boardService.loadEntity(boardService.createBoard(obMember, request, files).getBoardId());
            boardFileService.uploadBoardFilesForDummy(board, getRandomImages());

        }
    }

    public List<String> getRandomImages() {
        List<String> urls = getDummyImages(bucket);

        Random random = new Random();
        int numFilesToFetch = random.nextInt(3); // 0에서 2까지의 랜덤 개수로 설정
        
        //파일 목록을 섞어서 url list를 반환
        Collections.shuffle(urls);
        return urls.subList(0, Math.min(numFilesToFetch, urls.size()));
    }


    //bucket에서 파일 url을 모두 가져오기
    private List<String> getDummyImages(String bucketName) {
        List<String> fileUrls = new ArrayList<>();
        List<S3ObjectSummary> objectSummaries = amazonS3Client.listObjects(bucketName,"dummy/").getObjectSummaries();
        for (S3ObjectSummary objectSummary : objectSummaries) {
            fileUrls.add(amazonS3Client.getUrl(bucketName, objectSummary.getKey()).toString());
        }
        return fileUrls;
    }

    private void createSchedules(Member member) {
        Optional<HostType> hostType = getHostType(member);
        if (hostType.isPresent()) {
            if (hostType.get() != HostType.CENTER || !scheduleService.existsByHostType(hostType.get())) {
                createDemoDaySchedule(member, hostType.get());
                createHackathonSchedule(member, hostType.get());
                createDiningSchedule(member, hostType.get());
            }
        }
    }

    private void createDemoDaySchedule(Member member, HostType hostType) {
        ScheduleRequest.CreateSchedule request = ScheduleRequest.CreateSchedule.builder()
                .title("UMC 5기 데모데이")
                .content("데모데이가 코 앞으로 다가왔습니당")
                .startDateTime(LocalDateTime.of(2024, 2, 19, 0,0))
                .endDateTime(LocalDateTime.of(2024, 2, 21,23,59))
                .semesterPermission(List.of(Semester.FIFTH, Semester.FOURTH, Semester.THIRD, Semester.SECOND, Semester.FIRST))
                .hostType(hostType)
                .placeSetting("올댓마인드 문래점")
                .build();
        scheduleService.createSchedule(member, request);
    }

    private void createDiningSchedule(Member member, HostType hostType) {
        int day = getRandomNumber();
        ScheduleRequest.CreateSchedule request = ScheduleRequest.CreateSchedule.builder()
                .title("회식")
                .content("여러분들 회식 많이 참여해주세요")
                .startDateTime(LocalDateTime.of(2024, 2, day, 18,0))
                .endDateTime(LocalDateTime.of(2024, 2, day,23,59))
                .semesterPermission(List.of(Semester.FIFTH))
                .hostType(hostType)
                .placeSetting("장소 미정")
                .build();
        scheduleService.createSchedule(member, request);
    }

    private void createHackathonSchedule(Member member, HostType hostType) {
        int day = getRandomNumber();
        ScheduleRequest.CreateSchedule request = ScheduleRequest.CreateSchedule.builder()
                .title("UMC 헤커톤")
                .content("헤커톤 우승 상금 백만원!!!")
                .startDateTime(LocalDateTime.of(2024, 2, day, 14,0))
                .endDateTime(LocalDateTime.of(2024, 2, day + 1,14,0))
                .semesterPermission(List.of(Semester.FIFTH, Semester.FOURTH, Semester.THIRD, Semester.SECOND, Semester.FIRST))
                .hostType(hostType)
                .placeSetting("헤커톤 장소")
                .build();
        scheduleService.createSchedule(member, request);
    }

    private Optional<HostType> getHostType(Member member) {
        int rolePriority = member.getRole().getPriority();
        if (rolePriority > 4)
            return Optional.empty();
        if (rolePriority > 3)
            return Optional.of(HostType.CAMPUS);
        if (rolePriority > 2)
            return Optional.of(HostType.BRANCH);
        return Optional.of(HostType.CENTER);
    }

    // 1 ~ 29 중 랜덤 숫자 생성
    private int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(28) + 1;
    }
}
