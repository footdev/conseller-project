package com.conseller.conseller.utils.dummy;

import com.conseller.conseller.category.mainCategory.MainCategoryRepository;
import com.conseller.conseller.category.subCategory.SubCategoryRepository;
import com.conseller.conseller.gifticon.dto.response.GifticonResponse;
import com.conseller.conseller.gifticon.enums.GifticonStatus;
import com.conseller.conseller.user.dto.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import static com.conseller.conseller.utils.DateTimeConverter.*;


@Service
@RequiredArgsConstructor
public class DummyService {

    private final DummyBulkRepository dummyBulkRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final MainCategoryRepository mainCategoryRepository;
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

        for (int i = 0; i < 100000; i++) {

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

        String[][] names = {{"아이스 카페 아메리카노 T", "카페 아메리카노 T", "아이스 카페 라떼 T", "아이스 자몽 허니 블랙 티 T", "아이스 스타벅스 돌체 라떼 T", "자바 칩 프라푸치노 T", "돌체 콜드 브루 T",
        "쿨 라임 피지오 T", "딸기 아사이 레모네이드 스탑거스 리프레셔 T", "아이스 카라멜 마끼아또 T", "아이스 제주 말차 라뗴 T", "아이스 카페 모카 T", "클래식 밀크 티 T", "카라멜 마끼아또 T",
        "카페 모카 T", "피치 요거트 블렌디드 T", "디카페인 카페 아메리카노 T", "아이스 디카페인 카페 아메리카노 T", "망고 패션 블렌디드 T", "바닐라 카페라떼 (R)", "로얄 밀크티 쉐이크", "순두부 찌개", "백반 정식","스타벅스 핫초코 라떼",
                "스타벅스 민트 모카 프라페", "스타벅스 아이스 블랙 티 레모네이드", "스타벅스 아이스 초코 밀크", "스타벅스 아이스 바닐라 콜드 브루", "스타벅스 아이스 헤이즐넛 카푸치노",
                "스타벅스 스파이시 차이 라떼", "스타벅스 아이스 화이트 모카", "스타벅스 라벤더 라떼", "스타벅스 아이스 망고 스무디", "스타벅스 바닐라 크림 콜드 브루", "스타벅스 자몽 허니 에이드",
                "스타벅스 청포도 그린 스무디", "스타벅스 레몬 민트 에이드", "스타벅스 블루베리 요거트 프라페", "스타벅스 화이트 초콜릿 모카", "스타벅스 아이스 레몬 티", "스타벅스 헤이즐넛 카라멜 라떼",
                "스타벅스 카라멜 프라푸치노", "스타벅스 피스타치오 크림 라떼"},
                {"김밥 + 라면 정식", "제육 덮밥", "오징어 덮밥", "김치 라면", "불고기 정식", "된장찌개", "김치찌개", "고추장 찌개", "두찜 한마리반 이용권", "불낙죽 (순한맛)", "소고기무국", "닭볶음탕",
                        "족발 大", "국물 떡볶이 + 순대 2인분", "기본 김밥", "참치 김밥", "스팸 볶음밥", "1인 보쌈 실속세트", "외식교환권 1만원권", "외식 교환권 3만원권","뚝배기 해물 순두부 찌개 세트", "한촌 설렁탕 소불고기 덮밥 정식", "빽다방 치즈 돈까스 세트", "애슐리 새우 덮밥 한정식",
                        "신선설농탕 콩나물 국밥 세트", "본죽 차돌박이 된장찌개", "두찜 순살 찜닭 세트", "서가앤쿡 갈비찜 정식", "육개장 전문점 육개장 세트", "버섯전골 전문점 버섯전골 정식", "매운 갈비찜 세트",
                        "원할머니보쌈 얼큰 알탕", "한정식 매운 돼지갈비찜 세트", "춘천 닭갈비", "교촌치킨 치킨 가라아게 세트", "왕새우 튀김 정식", "매콤 쭈꾸미 볶음", "고기원찬 소고기 찹스테이크 정식",
                        "참치명가 참치회 덮밥", "연어명가 연어회 덮밥", "하동관 특선 갈비탕", "김치볶음밥 정식", "사보텐 돈까스 정식", "카레라이스 세트", "해물파전 정식", "전주비빔밥 한상",
                        "봉평메밀촌 냉면 정식", "미스사이공 베트남 쌀국수", "오사카부 타코야키", "파파존스 토마토 스파게티"}};


        for (int i = 0; i < 500000; i++) {
            /*
            1. 메인 카테고리를 고른다.
            2. 메인의 하위 카테고리를 고른다.
            3. 이름 생성
            4. 유효기간 생성 (시작 ~ 끝)
            5. 이미지 url 고정
            6. 상태
            */
            int mainCategoryIdx = 1;
            int subCategoryIdx = random.nextInt(2);
            String name = names[subCategoryIdx][random.nextInt(names[subCategoryIdx].length)];
            LocalDateTime date = generateRandomDate();
            String startDate = convertString(date);
            String endDate = convertString(date.plusYears(1));
            endDate = convertString(convertDateTime(endDate));
            String imageUrl = "";
            String status = GifticonStatus.KEEP.getStatus();
            long userIdx = random.nextInt(600001);

            list.add(
                    GifticonResponse.builder()
                            .gifticonIdx(0)
                            .gifticonBarcode(String.valueOf(random.nextLong()))
                            .gifticonName(name)
                            .gifticonStartDate(startDate)
                            .gifticonEndDate(endDate)
                            .gifticonAllImageUrl(imageUrl)
                            .gifticonDataImageUrl("https://conseller-static-file.s3.ap-northeast-2.amazonaws.com/bc06373e-9ff7-408a-9ec8-8f74813e5ded.jpeg")
                            .gifticonStatus(status)
                            .userIdx(userIdx)
                            .subCategoryIdx(subCategoryIdx + 1)
                            .mainCategoryIdx(mainCategoryIdx)
                            .build()
            );
        }

        dummyBulkRepository.saveAllGifticons(list);
    }
}
