package com.conseller.conseller.utils.dummy;

import com.conseller.conseller.auction.domain.AuctionService;
import com.conseller.conseller.auction.api.dto.mapper.AuctionMapper;
import com.conseller.conseller.auction.api.dto.request.RegistAuctionRequest;
import com.conseller.conseller.auction.domain.enums.AuctionStatus;
import com.conseller.conseller.category.infrastructure.MainCategoryRepository;
import com.conseller.conseller.category.infrastructure.SubCategoryRepository;
import com.conseller.conseller.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.entity.Gifticon;
import com.conseller.conseller.user.infrastructure.UserEntity;
import com.conseller.conseller.exception.CustomException;
import com.conseller.conseller.exception.CustomExceptionStatus;
import com.conseller.conseller.gifticon.api.dto.response.GifticonResponse;
import com.conseller.conseller.gifticon.domain.enums.GifticonStatus;
import com.conseller.conseller.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.user.infrastructure.UserRepository;
import com.conseller.conseller.user.api.dto.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.conseller.conseller.utils.DateTimeConverter.*;


@Service
@Log4j2
@RequiredArgsConstructor
public class DummyService {

    private final DummyBulkRepository dummyBulkRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final UserRepository userRepository;
    private final GifticonRepository gifticonRepository;

    private final AuctionService auctionService;

    private final Random random = new Random();

    @Transactional
    public void createDummyUsersAndSaveAll() {

        String[] firstNames = {"김", "이", "박", "최", "정", "현", "길", "조", "신", "허", "한", "안", "민", "오", "구", "소", "도", "연", "손", "주"};
        String[] secondNames = {"가", "강", "건", "경", "고", "관", "광", "구", "규", "근", "기", "길", "나", "남", "노", "누", "다",
                "단", "달", "담", "대", "덕", "도", "동", "두", "라", "래", "로", "루", "리", "마", "만", "명", "무", "문", "미", "민", "바", "박",
                "백", "범", "별", "병", "보", "빛", "사", "산", "상", "새", "서", "석", "선", "설", "섭", "성", "세", "소", "솔", "수", "숙", "순",
                "숭", "슬", "승", "시", "신", "아", "안", "애", "엄", "여", "연", "영", "예", "오", "옥", "완", "요", "용", "우", "원", "월", "위",
                "유", "윤", "율", "으", "은", "의", "이", "익", "인", "일", "잎", "자", "잔", "장", "재", "전", "정", "제", "조", "종", "주", "준",
                "중", "지", "진", "찬", "창", "채", "천", "철", "초", "춘", "충", "치", "탐", "태", "택", "판", "하", "한", "해", "혁", "현", "형",
                "혜", "호", "홍", "화", "환", "회", "효", "훈", "휘", "희", "운", "모", "배", "부", "림", "봉", "혼", "황", "량", "린", "을", "비",
                "솜", "공", "면", "탁", "온", "디", "항", "후", "려", "균", "묵", "송", "욱", "휴", "언", "령", "섬", "들", "견", "추", "걸", "삼",
                "열", "웅", "분", "변", "양", "출", "타", "흥", "겸", "곤", "번", "식", "란", "더", "손", "술", "훔", "반", "빈", "실", "직", "흠",
                "흔", "악", "람", "뜸", "권", "복", "심", "헌", "엽", "학", "개", "롱", "평", "늘", "늬", "랑", "얀", "향", "울", "련"};
        String[] banks = {"국민", "하나", "토스", "카카오뱅크", "기업", "농협", "대구", "신한", "우체국", "우리", "SC제일", "한국시티", "광주", "부산", "전북", "제주"};

        List<SignUpRequest> list = new ArrayList<>();

        for (int i = 0; i < 50000; i++) {

            String firstName = firstNames[random.nextInt(firstNames.length)];
            String secondName = secondNames[random.nextInt(secondNames.length)];
            String lastName = secondNames[random.nextInt(secondNames.length)];
            String userName = firstName + secondName + lastName;

            int userAge = 20 + random.nextInt(41);
            String userPhoneNumber = "010-" + (1000 + random.nextInt(9000)) + "-" + (1000 + random.nextInt(9000));
            String userAccountBank = banks[random.nextInt(banks.length)];
            String userAccount = "100-" + (1000 + random.nextInt(9000)) + "-" + (10000 + random.nextInt(90000));
            String userPassword = "pass" + random.nextInt(10000);

            list.add(
                    SignUpRequest.builder()
                            .userId("user" + i)
                            .userAge(userAge)
                            .userName(userName)
                            .userNickname(userName + (100 + random.nextInt(900)))
                            .userGender(random.nextInt(2) == 0 ? 'M' : 'F')
                            .userPhoneNumber(userPhoneNumber)
                            .userAccountBank(userAccountBank)
                            .userAccount(userAccount)
                            .userPassword(userPassword)
                            .build()
            );
        }
        dummyBulkRepository.saveAllUsers(list);

    }

    @Transactional
    public void createDummyGifticonsAndSaveAll() {
        List<GifticonResponse> list = new ArrayList<>();

//        String[][] names = {{"아이스 카페 아메리카노 T", "카페 아메리카노 T", "아이스 카페 라떼 T", "아이스 자몽 허니 블랙 티 T", "아이스 스타벅스 돌체 라떼 T", "자바 칩 프라푸치노 T", "돌체 콜드 브루 T",
//        "쿨 라임 피지오 T", "딸기 아사이 레모네이드 스탑거스 리프레셔 T", "아이스 카라멜 마끼아또 T", "아이스 제주 말차 라뗴 T", "아이스 카페 모카 T", "클래식 밀크 티 T", "카라멜 마끼아또 T",
//        "카페 모카 T", "피치 요거트 블렌디드 T", "디카페인 카페 아메리카노 T", "아이스 디카페인 카페 아메리카노 T", "망고 패션 블렌디드 T", "바닐라 카페라떼 (R)", "로얄 밀크티 쉐이크", "순두부 찌개", "백반 정식","스타벅스 핫초코 라떼",
//                "스타벅스 민트 모카 프라페", "스타벅스 아이스 블랙 티 레모네이드", "스타벅스 아이스 초코 밀크", "스타벅스 아이스 바닐라 콜드 브루", "스타벅스 아이스 헤이즐넛 카푸치노",
//                "스타벅스 스파이시 차이 라떼", "스타벅스 아이스 화이트 모카", "스타벅스 라벤더 라떼", "스타벅스 아이스 망고 스무디", "스타벅스 바닐라 크림 콜드 브루", "스타벅스 자몽 허니 에이드",
//                "스타벅스 청포도 그린 스무디", "스타벅스 레몬 민트 에이드", "스타벅스 블루베리 요거트 프라페", "스타벅스 화이트 초콜릿 모카", "스타벅스 아이스 레몬 티", "스타벅스 헤이즐넛 카라멜 라떼",
//                "스타벅스 카라멜 프라푸치노", "스타벅스 피스타치오 크림 라떼"},
//                {"김밥 + 라면 정식", "제육 덮밥", "오징어 덮밥", "김치 라면", "불고기 정식", "된장찌개", "김치찌개", "고추장 찌개", "두찜 한마리반 이용권", "불낙죽 (순한맛)", "소고기무국", "닭볶음탕",
//                        "족발 大", "국물 떡볶이 + 순대 2인분", "기본 김밥", "참치 김밥", "스팸 볶음밥", "1인 보쌈 실속세트", "외식교환권 1만원권", "외식 교환권 3만원권","뚝배기 해물 순두부 찌개 세트", "한촌 설렁탕 소불고기 덮밥 정식", "빽다방 치즈 돈까스 세트", "애슐리 새우 덮밥 한정식",
//                        "신선설농탕 콩나물 국밥 세트", "본죽 차돌박이 된장찌개", "두찜 순살 찜닭 세트", "서가앤쿡 갈비찜 정식", "육개장 전문점 육개장 세트", "버섯전골 전문점 버섯전골 정식", "매운 갈비찜 세트",
//                        "원할머니보쌈 얼큰 알탕", "한정식 매운 돼지갈비찜 세트", "춘천 닭갈비", "교촌치킨 치킨 가라아게 세트", "왕새우 튀김 정식", "매콤 쭈꾸미 볶음", "고기원찬 소고기 찹스테이크 정식",
//                        "참치명가 참치회 덮밥", "연어명가 연어회 덮밥", "하동관 특선 갈비탕", "김치볶음밥 정식", "사보텐 돈까스 정식", "카레라이스 세트", "해물파전 정식", "전주비빔밥 한상",
//                        "봉평메밀촌 냉면 정식", "미스사이공 베트남 쌀국수", "오사카부 타코야키", "파파존스 토마토 스파게티"}};

        String[][][] names = {
                // 음식 카테고리
                {
                        // 한식
                        {"한미족발 족발 大", "갈비명가 매운 갈비찜 세트", "김밥천국 제육 덮밥", "김밥천국 오징어 덮밥", "김밥 천국 김치 라면",
                                "본죽 불낙죽 (순한맛)", "봉구스 밥버거 스팸 밥버거", "원할머니 보쌈 1인 보쌈 실속세트", "순두부 찌개", "백반 정식",
                                "불고기 정식", "된장찌개", "김치찌개", "고추장 찌개", "뚝배기 해물 순두부 찌개 세트", "한촌 설렁탕 소불고기 덮밥 정식",
                                "신선설농탕 콩나물 국밥 세트", "본죽 차돌박이 된장찌개", "서가앤쿡 갈비찜 정식", "육개장 전문점 육개장 세트", "매운 갈비찜 세트",
                                "찌개마을 얼큰 알탕", "한정식 매운 돼지갈비찜 세트", "전주비빔밥 한상", "본죽 전복죽", "본죽 소고기 미역국", "한촌설렁탕 설렁탕", "한촌설렁탕 소불고기 정식",
                                "비비고 왕교자", "비비고 김치찌개", "육수당 육개장", "육수당 갈비탕",
                                "놀부 부대찌개", "놀부 보쌈정식", "하남돼지집 삼겹살", "원할머니보쌈 보쌈정식",
                                "새마을식당 7분돼지김치", "새마을식당 열탄불고기"},

                        // 양식
                        {"아웃백 스테이크", "아웃백 투움바 파스타", "빕스 부채살 스테이크",
                                "빕스 쉬림프 스테이크", "TGI 프라이데이 치킨 윙", "TGI 프라이데이 베이비 백 립",
                                "하늘채 해물 파스타", "더플레이스 라자냐", "매드포갈릭 갈릭 스테이크",
                                "매드포갈릭 까르보나라", "온더보더 치미창가", "파스토 스파게티",
                                "베라 파스타 알프레도", "리틀파파포 파스타", "바피아노 라비올리", "더블유호텔 리조또", "그릴드 연어 스테이크"},

                        // 중식
                        {"홍콩반점0410 짜장면", "홍콩반점0410 짬뽕", "홍콩반점0410 탕수육",
                                "차이나팩토리 꿔바로우", "차이나팩토리 깐풍기", "중국집 차돌짬뽕",
                                "명륜성 중화 볶음밥", "팔선생 짬뽕탕", "팔선생 마라샹궈",
                                "더차이나 짜장면", "더차이나 탕수육", "봉추찜닭 마파두부", "차이나당 칠리새우",
                                "진화성 짜장면 + 짬뽕 2인 세트", "지린성 고추짜장 2인 세트", "홍콩반점0410 짜장면 + 짬뽕 2인 세트"},

                        // 일식
                        {"스시로 연어 초밥", "스시로 참치 초밥", "스시로 장어 초밥",
                                "스시히로바 모둠 초밥", "스시히로바 사케동", "쿠우쿠우 스시 뷔페 이용권",
                                "오니기리와이규동 규동", "오니기리와이규동 가츠동", "돈까스클럽 등심돈까스",
                                "돈까스클럽 치즈돈까스", "사보텐 히레카츠", "사보텐 로스카츠",
                                "미소야 냉모밀", "미소야 우동", "하코야 돈코츠 라멘", "하코야 미소 라멘",
                                "이자카야 유자카와 사시미", "이자카야 사케동", "스시한판 참치 초밥",
                                "스시한판 연어 초밥"},

                        // 패스트푸드
                        {"맥도날드 빅맥 세트", "맥도날드 맥스파이시 상하이 버거 세트", "버거킹 와퍼 세트",
                                "버거킹 몬스터X", "롯데리아 새우버거 세트", "롯데리아 데리버거 세트",
                                "맘스터치 싸이순살 버거 세트", "맘스터치 인크레더블 버거 세트", "KFC 징거버거 세트",
                                "KFC 타워버거 세트", "도미노 피자 블랙타이거 슈림프", "도미노 피자 페퍼로니",
                                "파파존스 수퍼 파파스 피자", "파파존스 불고기 피자", "미스터피자 포테이토 피자",
                                "미스터피자 페퍼로니 피자", "피자헛 슈퍼슈프림 피자", "피자헛 트리플 치즈 피자",
                                "교촌치킨 허니콤보", "교촌치킨 레드콤보"}
                },

                // 카페/디저트 카테고리
                {
                        // 커피
                        {"아이스 카페 아메리카노 T", "카페 아메리카노 T", "아이스 카페 라떼 T", "아이스 스타벅스 돌체 라떼 T", "돌체 콜드 브루 T","스타벅스 아이스 바닐라 콜드 브루", "스타벅스 아이스 초코 밀크", "스타벅스 민트 모카 프라페", "스타벅스 핫초코 라떼", "디카페인 카페 아메리카노 T", "아이스 디카페인 카페 아메리카노 T", "바닐라 카페라떼 (R)", "카라멜 마끼아또 T", "카페 모카 T", "아이스 카페 모카 T", "스타벅스 아이스 화이트 모카", "스타벅스 헤이즐넛 카라멜 라떼", "스타벅스 카라멜 프라푸치노", "스타벅스 피스타치오 크림 라떼"},

                        // 차/티
                        {"스타벅스 아이스 자몽 허니 블랙 티 T", "스타벅스 딸기 아사이 레모네이드 스탑거스 리프레셔 T", "스타벅스 쿨 라임 피지오 T", "스타벅스 클래식 밀크 티 T", "로얄 밀크티 쉐이크", "스타벅스 아이스 블랙 티 레모네이드", "스타벅스 자몽 허니 에이드", "스타벅스 청포도 그린 스무디", "스타벅스 레몬 민트 에이드", "스타벅스 아이스 레몬 티", "스타벅스 스파이시 차이 라떼", "스타벅스 라벤더 라떼"},

                        // 아이스크림
                        {"베스킨라빈스 초코나무숲", "베스킨라빈스 뉴욕 치즈케이크", "베스킨라빈스 엄마는 외계인",
                                "베스킨라빈스 아몬드 봉봉", "배스킨라빈스 민트초코", "나뚜루 녹차 아이스크림",
                                "하겐다즈 바닐라 아이스크림", "하겐다즈 스트로베리 아이스크림", "메로나",
                                "구구콘", "월드콘 초코", "스크류바"},

                        // 베이커리
                        {"스타벅스 베이글", "스타벅스 모닝빵", "파리바게트 슈크림 빵", "파리바게트 소보루 빵", "성심당 보문산 메아리", "성심당 야채 고로케", "성심당 팥 빵"},

                        // 케이크
                        {"스타벅스 크레이프 케이크", "스타벅스 블루베리 케이크", "투썸 플레이스 아이스박스 한조각", "투썸 플레이스 초코 딸기 케이크"}
                }
        };


        for (int i = 0; i < 500000; i++) {
            /*
            1. 메인 카테고리를 고른다.
            2. 메인의 하위 카테고리를 고른다.
            3. 이름 생성
            4. 유효기간 생성 (시작 ~ 끝)
            5. 이미지 url 고정
            6. 상태
            */
            int mainCategoryIdx = random.nextInt(2);;
            int subCategoryIdx = random.nextInt(5);

            String name = names[mainCategoryIdx][subCategoryIdx][random.nextInt(names[mainCategoryIdx][subCategoryIdx].length)];

            LocalDateTime date = generateRandomDate();
            String startDate = convertString(date);
            String endDate = convertString(date.plusYears(1));
            endDate = convertString(convertDateTime(endDate));

            String imageUrl = "";
            String status = GifticonStatus.KEEP.getStatus();

            long userIdx = random.nextInt(1000001);

            list.add(
                    GifticonResponse.builder()
                            .gifticonIdx(0)
                            .gifticonBarcode(String.valueOf(Math.abs(random.nextLong())))
                            .gifticonName(name)
                            .gifticonStartDate(startDate)
                            .gifticonEndDate(endDate)
                            .gifticonAllImageUrl(imageUrl)
                            .gifticonDataImageUrl("https://conseller-static-file.s3.ap-northeast-2.amazonaws.com/bc06373e-9ff7-408a-9ec8-8f74813e5ded.jpeg")
                            .gifticonStatus(status)
                            .userIdx(userIdx)
                            .subCategoryIdx(subCategoryIdx + 1)
                            .mainCategoryIdx(mainCategoryIdx + 1)
                            .build()
            );
        }

        dummyBulkRepository.saveAllGifticons(list);
    }

    @Transactional
    public void createDummyAuctionsAndSaveAll() {
        List<AuctionEntity> auctionEntities = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<?>> futures = new ArrayList<>();

        for (long i = 2; i < 50000; i++) {
            long finalI = i;
            long userIdx = ThreadLocalRandom.current().nextLong(1, 1000001);
            long finalI1 = i;
            long finalI2 = i;
            Future<?> future = executor.submit(() -> {
                // 유저 정보 조회
                UserEntity userEntity = userRepository.findByUserIdx(userIdx)
                        .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

                // 유저의 기프티콘 조회
                List<Gifticon> gifticons = gifticonRepository.findAllByUserUserIdx(userIdx);

                // 유저의 모든 기프티콘에 대한 리퀘스트 dto 생성
                final long userIdxL = finalI;
                List<RegistAuctionRequest> requests = gifticons.stream()
                        .map(gifticon -> new RegistAuctionRequest(50000, 5000, "판매합니다!! " + finalI1, gifticon.getGifticonIdx(), userIdxL))
                        .collect(Collectors.toList());

                // 경매글 엔티티로 변환 후 저장
                for (int j = 0; j < requests.size(); j++) {

                    //기프티콘의 유효기간이 하루 이하로 남았다면 Continue
                    if (gifticons.get(j).getGifticonEndDate().isBefore(LocalDateTime.now().plusDays(1))) {
                        continue;
                    }

                    //갖고 있는 기프티콘이 없다면 break
                    if (gifticons.size() == 0) break;
                    if (gifticons.get(j).getGifticonEndDate() == null) continue;

                    //기프티콘의 상태가 KEEP이 아니라면 Continue
                    if (!gifticons.get(j).getGifticonStatus().equals(GifticonStatus.KEEP.getStatus())) {
                        continue;
                    }

                    LocalDateTime endDate = gifticons.get(j).getGifticonEndDate().minusDays(1);
                    AuctionEntity auctionEntity = AuctionMapper.INSTANCE.registAuctionRequestToAuction(requests.get(j), userEntity, gifticons.get(j));
                    auctionEntity.setAuctionStartDate(generateRandomDate10Month());
                    auctionEntity.setAuctionEndDate(endDate);
                    auctionEntity.setAuctionStatus(AuctionStatus.IN_PROGRESS.getStatus());

                    if (auctionEntity.getAuctionEndDate() == null) continue;

                    auctionEntities.add(auctionEntity);
                }
            });

            futures.add(future);
        }

        // 모든 작업이 완료될 때까지 대기
        for (Future<?> future : futures) {
            try {
                future.get();  // 작업이 완료될 때까지 블로킹
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("작업 중 오류 발생", e);
            }
        }

        // 모든 작업이 완료된 후 저장
        log.info("더미 경매글 개수 : " + auctionEntities.size());
        log.info("기프티콘 이름 : {}", auctionEntities.get(0).getGifticon().getGifticonName());
        log.info("경매 시작일 : {}", auctionEntities.get(0).getAuctionStartDate());
        log.info("경매 종료일 : {}", auctionEntities.get(0).getAuctionEndDate());
        log.info("경매 하한가 : {}", auctionEntities.get(0).getLowerPrice());
        log.info("경매 상한가 : {}", auctionEntities.get(0).getUpperPrice());
        log.info("경매글 내용 : {}", auctionEntities.get(0).getAuctionText());
        log.info("경매글 등록자 : {}", auctionEntities.get(0).getUserEntity().getUsername());
        log.info("경매 기프티콘 정보 : {}", auctionEntities.get(0).getGifticon().getGifticonEndDate());
        log.info("경매 기프티콘 정보 : {}", auctionEntities.get(0).getGifticon().getGifticonEndDate().minusDays(1));
        dummyBulkRepository.saveAllAuctions(auctionEntities);
        log.info("경매글 더미 데이터 생성 완료");

        // Executor 종료
        executor.shutdown();
    }
}
