package com.umc.networkingService.domain.test.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.umc.networkingService.config.initial.BranchInfo;
import com.umc.networkingService.config.initial.UniversityInfo;
import com.umc.networkingService.domain.board.entity.Board;
import com.umc.networkingService.domain.board.entity.BoardType;
import com.umc.networkingService.domain.board.entity.HostType;
import com.umc.networkingService.domain.board.service.BoardFileService;
import com.umc.networkingService.domain.board.service.BoardService;
import com.umc.networkingService.domain.branch.entity.Branch;
import com.umc.networkingService.domain.branch.service.BranchService;
import com.umc.networkingService.domain.member.dto.request.MemberSignUpRequest;
import com.umc.networkingService.domain.member.entity.Member;
import com.umc.networkingService.domain.member.entity.SemesterPart;
import com.umc.networkingService.domain.member.mapper.MemberMapper;
import com.umc.networkingService.domain.member.repository.SemesterPartRepository;
import com.umc.networkingService.domain.member.service.AuthService;
import com.umc.networkingService.domain.member.service.MemberService;
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
    private final MemberMapper memberMapper;

    private final BoardService boardService;
    private final BoardFileService boardFileService;

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
        createBoard(members, BoardType.FREE, HostType.CAMPUS);
        createBoard(members, BoardType.FREE, HostType.BRANCH);
        createBoard(members, BoardType.FREE, HostType.CENTER);
        createBoard(members, BoardType.QUESTION, HostType.CAMPUS);
        createBoard(members, BoardType.QUESTION, HostType.BRANCH);
        createBoard(members, BoardType.QUESTION, HostType.CENTER);
        createOB(members, HostType.CAMPUS);
        createOB(members, HostType.BRANCH);
        createOB(members, HostType.CENTER);
        createNoticeAndWorkbook(members);


        return "더미데이터 생성 완료";
    }

    @Transactional
    public List<Member> createDummyMember(Member loginMember) {
        Random random = new Random();
        List<Member> members = new ArrayList<>();

        //로그인 한 멤버의 지부와 학교 정보 불러오기
        Branch branch = branchService.loadEntity(loginMember.getBranch().getId());
        University university = universityService.loadEntity(loginMember.getUniversity().getId());

        //로그인한 멤버의 지부에 해당하는 university list를 불러오기
        List<University> universities = new ArrayList<>();
        List<UniversityInfo> univInfos = BranchInfo.getBranchInfo(branch.getName()).getUniversities();
        for (UniversityInfo univInfo : univInfos)
            universities.add(universityService.findUniversityByName(univInfo.getName()));

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

            if (!memberInfo.getRole().equals("챌린저")) {
                if (memberInfo.getRole().equals("운영국장"))
                    centerPositions.add(memberInfo.getRole());
                else {
                    campusPositions.add(memberInfo.getRole());
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

    private void createBoard(List<Member> members, BoardType boardType, HostType hostType) {
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
            boardFileService.uploadBoardFilesForDummy(board, getRandomThumbnail());
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

            BoardCreateRequest request3 = BoardCreateRequest.builder()
                    .hostType(HostType.CENTER.toString())
                    .boardType(BoardType.NOTICE.toString())
                    .title("UMC 서울 해커톤 개최!" + i)
                    .content("해커톤을 개최합니다 여러분 많은 참여 부탁드려요."+i)
                    .build();

            BoardCreateRequest request4 = BoardCreateRequest.builder()
                    .hostType(HostType.CAMPUS.toString())
                    .boardType(BoardType.WORKBOOK.toString())
                    .title(i + "주차 워크북입니다.")
                    .content("워크북 다들화이팅!")
                    .build();


            boardService.createBoard(campusStaff, request, files);
            Board board1 = boardService.loadEntity(boardService.createBoard(campusStaff, request, files).getBoardId());
            boardFileService.uploadBoardFilesForDummy(board1, getRandomThumbnail());
            Board board2 = boardService.loadEntity(boardService.createBoard(branchStaff, request2, files).getBoardId());
            boardFileService.uploadBoardFilesForDummy(board2, getRandomThumbnail());
            Board board3 = boardService.loadEntity(boardService.createBoard(centerStaff, request3, files).getBoardId());
            boardFileService.uploadBoardFilesForDummy(board3, getRandomThumbnail());
            Board board4 = boardService.loadEntity(boardService.createBoard(campusStaff, request4, files).getBoardId());
            boardFileService.uploadBoardFilesForDummy(board4, getRandomThumbnail());

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
            boardFileService.uploadBoardFilesForDummy(board, getRandomThumbnail());

        }
    }

    public List<String> getRandomThumbnail() {
        List<String> urls = getDummyThumbnails(bucket);

        Random random = new Random();
        int numFilesToFetch = random.nextInt(3); // 0에서 2까지의 랜덤 개수로 설정
        
        //파일 목록을 섞어서 url list를 반환
        Collections.shuffle(urls);
        return urls.subList(0, Math.min(numFilesToFetch, urls.size()));
    }


    //bucket에서 파일 url을 모두 가져오기
    private List<String> getDummyThumbnails(String bucketName) {
        List<String> fileUrls = new ArrayList<>();
        List<S3ObjectSummary> objectSummaries = amazonS3Client.listObjects(bucketName,"dummy/").getObjectSummaries();
        for (S3ObjectSummary objectSummary : objectSummaries) {
            fileUrls.add(amazonS3Client.getUrl(bucketName, objectSummary.getKey()).toString());
        }
        return fileUrls;
    }


}
