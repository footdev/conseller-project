package com.conseller.conseller.core.user.domain;

import com.conseller.conseller.core.auction.api.dto.mapper.AuctionMapper;
import com.conseller.conseller.core.auction.api.dto.response.AuctionItemData;
import com.conseller.conseller.core.barter.infrastructure.BarterGuestItem;
import com.conseller.conseller.core.barter.infrastructure.BarterRequestEntity;
import com.conseller.conseller.core.bid.api.dto.response.AuctionBidResponse;
import com.conseller.conseller.core.barter.api.dto.mapper.BarterMapper;
import com.conseller.conseller.core.barter.api.dto.response.MyBarterResponseDto;
import com.conseller.conseller.core.barter.api.dto.response.MyBarterRequestResponseDto;
import com.conseller.conseller.core.bid.infrastructure.AuctionBid;
import com.conseller.conseller.core.user.api.dto.request.*;
import com.conseller.conseller.core.user.api.dto.response.*;
import com.conseller.conseller.global.entity.BlackListEntity;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import com.conseller.conseller.core.gifticon.api.dto.response.GifticonData;
import com.conseller.conseller.core.gifticon.api.dto.response.GifticonResponse;
import com.conseller.conseller.core.gifticon.infrastructure.enums.GifticonStatus;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonRepository;
import com.conseller.conseller.core.store.infrastructure.StoreEntity;
import com.conseller.conseller.core.store.infrastructure.StoreRepository;
import com.conseller.conseller.core.store.api.dto.mapper.StoreMapper;
import com.conseller.conseller.core.store.api.dto.response.StoreItemData;
import com.conseller.conseller.core.user.infrastructure.UserEntity;
import com.conseller.conseller.core.user.infrastructure.UserRepository;
import com.conseller.conseller.core.user.api.dto.UserMapper;
import com.conseller.conseller.core.user.domain.enums.Login;
import com.conseller.conseller.global.utils.TemporaryValueGenerator;
import com.conseller.conseller.global.security.BlackListRepository;
import com.conseller.conseller.global.security.JwtToken;
import com.conseller.conseller.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.conseller.conseller.global.utils.DateTimeConverter.convertString;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final BlackListRepository blackListRepository;
    private final UserValidator userValidator;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final GifticonRepository gifticonRepository;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public UserEntity register(SignUpRequest signUpRequest) {

        userValidator.signUpDtoValidate(signUpRequest);

        UserEntity userEntity = UserMapper.INSTANCE.signUpDtoToUser(signUpRequest);

        //비밀번호 암호화 및 유저 권한 설정
        userEntity.encryptPassword(new BCryptPasswordEncoder());
        userEntity.addUserRole();

        return userRepository.save(userEntity);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        //유저 검증 및 반환
        UserEntity userEntity = userValidator.validateLogin(loginRequest);

        //해당 유저가 사용 가능한 유지인지 검증
        userValidator.validateUser(userEntity);

       return authenticateAndGetToken(userEntity, Login.GENERAL, loginRequest);
    }

    @Override
    public void updateUserInfo(long userIdx, UserInfoRequest userInfoRequest) {
        UserEntity userEntity = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        userEntity.updateUserInfo(userInfoRequest);
    }

    @Override
    public TemporaryPasswordResponse generateTemporaryPassword(EmailAndIdRequest emailAndIdRequest) {

        // 1. 임시 비밀번호 생성
        String tempPassword = TemporaryValueGenerator.generateTemporaryValue();

        // 2. 해당 이메일과 ID를 가진 유저 불러오기
        UserEntity userEntity = userRepository.findByUserEmailAndUserId(emailAndIdRequest.getUserEmail(), emailAndIdRequest.getUserId())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        // 3. 임시 비밀번호로 변경
        userEntity.updatePassword(tempPassword);

        return TemporaryPasswordResponse.builder()
                .temporaryPassword(tempPassword)
                .build();
    }

    @Override
    public PartialHiddenUserIdResponse getHiddenUserId(EmailAndNameRequest emailAndNameRequest) {

        //1. 이메일과 이름을 통해 유저 정보를 불러온다.
        UserEntity userEntity = userRepository.findByUserEmailAndUserName(emailAndNameRequest.getUserEmail(), emailAndNameRequest.getUserName())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        StringBuilder partialEncodeId = new StringBuilder();
        String userId = userEntity.getUserId();
        int length = userId.length();

        partialEncodeId.append("*".repeat(length / 2));
        partialEncodeId.append(userId.substring(length / 2));

        return PartialHiddenUserIdResponse.builder()
                .userEncodeId(partialEncodeId.toString())
                .build();
    }

    @Override
    public UserInfoResponse getUserInfo(long userIdx) {

        UserEntity userEntity = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        return UserMapper.INSTANCE.toUserInfoResponse(userEntity);
    }

    @Override
    public void uploadProfile(long userIdx, String profileUrl) {
        UserEntity userEntity = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        userEntity.setUserProfileUrl(profileUrl);
    }

    //비밀번호 암호화 된걸 가져와야함.
    @Override
    public void checkUserPassword(UserCheckPasswordRequest userCheckPasswordRequest) {
        //유저의 idx와 비밀번호를 통해 해당 유저가 존재하는지 확인하는 쿼리를 짜야함.
        UserEntity userEntity = userRepository.findByUserIdx(userCheckPasswordRequest.getUserIdx())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        if (!userEntity.checkPassword(new BCryptPasswordEncoder(), userCheckPasswordRequest.getUserPassword())) {
            throw new CustomException(CustomExceptionStatus.USER_INVALID);
        }
    }

    @Override
    public void deposit(long userIdx, long deposit) {
        UserEntity userEntity = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));
        userEntity.setUserDeposit(deposit);
    }

    @Override
    public List<GifticonResponse> getGifticons(long userIdx) {
        UserEntity userEntity = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        return userEntity.getGifticonEntities()
                .stream()
                .map(GifticonEntity::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<StoreItemData> getUserStores(long userIdx) {
        UserEntity userEntity = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        return userEntity.getStoreEntities().stream()
                .map(StoreMapper.INSTANCE::storeToItemData)
                .collect(Collectors.toList());
    }

    @Override
    public List<StoreItemData> getUserPurchaseStores(long userIdx) {
        List<StoreEntity> userPurchaseStoreEntities = storeRepository.findStoresByConsumerIdx(userIdx);

        return userPurchaseStoreEntities.stream()
                .map(StoreMapper.INSTANCE::storeToItemData)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuctionItemData> getUserAuctions(long userIdx) {
        UserEntity userEntity = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        return AuctionMapper.INSTANCE.auctionsToItemDatas(userEntity.getAuctionEntities());
    }

    @Override
    public List<AuctionBidResponse> getUserAuctionBids(long userIdx) {
        UserEntity userEntity = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));
        List<AuctionBidResponse> auctionBidResponses = new ArrayList<>();

        for (AuctionBid bid : userEntity.getAuctionBids()) {
            AuctionBidResponse bidResponse = AuctionBidResponse.builder()
                    .auctionBidIdx(bid.getAuctionBidIdx())
                    .auctionBidPrice(bid.getAuctionBidPrice())
                    .auctionBidStatus(bid.getAuctionBidStatus())
                    .auctionRegistedDate(convertString(bid.getAuctionRegistedDate()))
                    .auctionItemData(AuctionMapper.INSTANCE.auctionToItemData(bid.getAuctionEntity()))
                    .build();

            auctionBidResponses.add(bidResponse);
        }

        return auctionBidResponses;
    }

    @Override
    public List<MyBarterResponseDto> getUserBarters(long userIdx) {
        UserEntity userEntity = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        return userEntity.getBarterEntities().stream()
                .map(BarterMapper.INSTANCE::toMybarterResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MyBarterRequestResponseDto> getUserBarterRequests(long userIdx) {
        UserEntity userEntity = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        List<MyBarterRequestResponseDto> myBarterRequests = new ArrayList<>();

        for (BarterRequestEntity barterRequestEntity : userEntity.getBarterRequestEntities()) {

            List<GifticonResponse> barterGuestItems = new ArrayList<>();

            //물물 교환 요청 기프티콘들을 dto로 변환
            for (BarterGuestItem item : barterRequestEntity.getBarterGuestItemList()) {
                GifticonResponse gifticon = item.getGifticonEntity().toResponseDto();
                barterGuestItems.add(gifticon);
            }

            MyBarterRequestResponseDto myBarterRequest = MyBarterRequestResponseDto.builder()
                    .barterRequestIdx(barterRequestEntity.getBarterRequestIdx())
                    .barterIdx(barterRequestEntity.getBarterEntity().getBarterIdx())
                    .barterName(barterRequestEntity.getBarterEntity().getBarterName())
                    .barterStatus(barterRequestEntity.getBarterEntity().getBarterStatus())
                    .barterRequestStatus(barterRequestEntity.getBarterRequestStatus())
                    .barterGuestItems(barterGuestItems)
                    .myBarterResponseDto(BarterMapper.INSTANCE.toMybarterResponseDto(barterRequestEntity.getBarterEntity()))
                    .build();

            myBarterRequests.add(myBarterRequest);
        }

        return myBarterRequests;
    }

    @Override
    public void deleteUser(long userIdx, String token) {
        UserEntity userEntity = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        //블랙리스트에 토큰 저장
        BlackListEntity blackListEntity = BlackListEntity.builder()
                .accessToken(token)
                .userEntity(userEntity)
                .build();
        blackListRepository.save(blackListEntity);

        //액세스 토큰과 리프레쉬 토큰을 모두 삭제해야함.
        userEntity.setRefreshToken(null);
        userEntity.setUserDeletedDate(LocalDateTime.now());
    }

    @Override
    public void setFirebaseToken(Long userIdx, FirebaseRequest request) {
        UserEntity userEntity = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        userEntity.setFcm(request.getFirebaseToken());
    }

    @Override
    public GifticonPageResponse getGifticonPage(GifticonRequestDTO gifticonRequestDTO) {
        List<GifticonEntity> gifticonEntityList = gifticonRepository.findAll();
        List<GifticonEntity> myGifticonEntityList = new ArrayList<>();
        for(GifticonEntity gifticonEntity : gifticonEntityList) {
            if(gifticonEntity.getUserEntity().getUserIdx() == gifticonRequestDTO.getUserIdx()
                    && gifticonEntity.getGifticonStatus().equals(GifticonStatus.KEEP.getStatus())){
                myGifticonEntityList.add(gifticonEntity);
            }
        }
        myGifticonEntityList.sort((gifticon1, gifticon2) -> {
            LocalDateTime endDate1 = gifticon1.getGifticonEndDate();
            LocalDateTime endDate2 = gifticon2.getGifticonEndDate();
            return endDate1.compareTo(endDate2);
        });

        int pageStart = (gifticonRequestDTO.getPage()-1)*10;

        List<GifticonData> gifticonDataList = new ArrayList<>();
        for(int i=pageStart; i<pageStart+10; i++) {
            if(i >= myGifticonEntityList.size()) break;
            GifticonEntity gifticonEntity = myGifticonEntityList.get(i);
            GifticonData gifticonData = GifticonData.builder()
                    .gifticonIdx(gifticonEntity.getGifticonIdx())
                    .gifticonImageName(gifticonEntity.getGifticonDataImageUrl())
                    .gifticonName(gifticonEntity.getGifticonName())
                    .gifticonEndDate(convertString(gifticonEntity.getGifticonEndDate()))
                    .build();

            gifticonDataList.add(gifticonData);
        }

        Long count = (long) myGifticonEntityList.size();
        Integer totalPage = 0;
        if(count > 0) {
            totalPage = ((int)((long) count))/10 + 1;
        }

        return GifticonPageResponse.builder()
                .totalElements(count)
                .totalPages(totalPage)
                .items(gifticonDataList)
                .build();
    }

    @Override
    public AccessTokenResponse reCreateAccessToken(HttpServletRequest request, long userIdx) {
        // 0.요청이 들어온 유저의 정보를 db에서 가져온다.
        UserEntity userEntity = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        // 1. header에서 refresh token 추출
        String refreshToken = jwtTokenProvider.resolveToken(request);

        // 2. authentication 발급
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

        // 3. 토큰의 유효성 검사
        if (refreshToken != null
                && jwtTokenProvider.validateToken(refreshToken)
                && refreshToken.equals(userEntity.getRefreshToken())) {
            log.info("refresh token is valid.");
            // 3. access 토큰 재발급
            return AccessTokenResponse.builder()
                    .accessToken(jwtTokenProvider.createAccessToken(authentication))
                    .build();
        } else {
            throw new CustomException(CustomExceptionStatus.REFRESH_TOKEN_INVALID);
        }
    }

    private Authentication getAuthentication(String userId, String userPassword, Login loginType) {
        log.info("getAuthentication 호출");

        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken;
        if (loginType.equals(Login.GENERAL)) {
            authenticationToken =
                    new UsernamePasswordAuthenticationToken(userId, userPassword);
        } else {
            log.info("일반 로그인이 아닌 특수 로그인");
            UserDetails userDetails = customUserDetailsService.loadUserWithoutPassword(userId, userPassword);
            authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }

        log.info("authenticationToken:" + authenticationToken.getName());
        log.info(authenticationManagerBuilder.getObject().toString());

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 User에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        if (!loginType.equals(Login.GENERAL)) {
            return authenticationToken;
        }
        return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }

    @Override
    public InfoValidationRequest checkNickname(String nickname) {
        boolean nicknameExists = userRepository.existsByUserNickname(nickname);

        return InfoValidationRequest.builder()
                .status(nicknameExists ? 0 : 1)
                .message(nicknameExists ? "이미 존재하는 닉네임 입니다." : "사용할 수 있는 닉네임 입니다")
                .build();
    }

    @Override
    public InfoValidationRequest checkId(String id) {
        boolean idExists = userRepository.existsByUserId(id);

        log.info(id + "is" + idExists);

        return InfoValidationRequest.builder()
                .status(idExists ? 0 : 1)
                .message(idExists ? "이미 존재하는 아이디 입니다." : "사용할 수 있는 아이디 입니다")
                .build();
    }

    @Override
    public InfoValidationRequest checkEmail(String email) {
        boolean emailExists = userRepository.existsByUserEmail(email);

        return InfoValidationRequest.builder()
                .status(emailExists ? 0 : 1)
                .message(emailExists ? "이미 존재하는 이메일 입니다." : "사용할 수 있는 이메일 입니다")
                .build();
    }

    @Override
    public InfoValidationRequest checkPhoneNumber(String phoneNumber) {
        boolean phoneNumberExists = userRepository.existsByUserPhoneNumber(phoneNumber);

        return InfoValidationRequest.builder()
                .status(phoneNumberExists ? 0 : 1)
                .message(phoneNumberExists ? "이미 존재하는 전화번호 입니다." : "사용할 수 있는 전화번호 입니다")
                .build();
    }

    @Override
    public void patternRegister(UserPatternRequest userPatternRequest){

        // 입력 Idx 정보가 유효한지 확인
        UserEntity userEntity = userRepository.findByUserIdx(userPatternRequest.getUserIdx())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        userEntity.setUserPattern(userPatternRequest.getPattern());
    }

    @Override
    public LoginResponse loginPattern(UserPatternRequest userPatternRequest){

        // 입력 Idx 정보가 유효한지 확인
        UserEntity userEntity = userRepository.findByUserIdx(userPatternRequest.getUserIdx())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        // 패턴이 맞는지 확인
        if (!userEntity.getUserPattern().equals(userPatternRequest.getPattern())) {
            throw new CustomException(CustomExceptionStatus.PATTERN_INVALID);
        }

        //사용이 가능한 유저인지 검증
        userValidator.validateUser(userEntity);

        return authenticateAndGetToken(userEntity, Login.PATTERN, null);
    }

    @Override
    public LoginResponse loginFinger(long userIdx) {
        // 입력 Idx 정보가 유효한지 확인
        UserEntity userEntity = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.USER_INVALID));

        //사용 가능한 유저인지 검증
        userValidator.validateUser(userEntity);

        return authenticateAndGetToken(userEntity, Login.FINGER, null);
    }

    private LoginResponse authenticateAndGetToken(UserEntity userEntity, Login loginType, LoginRequest loginRequest) {

        //로그인 방식에 따라 달라져야함.

        // 입력된 id, password 기반으로 인증 후 인가 관련 인터페이스 생성
        String password = loginType.equals(Login.GENERAL) ? loginRequest.getUserPassword() : userEntity.getUserPattern();
        Authentication authentication = getAuthentication(userEntity.getUserId(), password, loginType);

        // 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.createToken(authentication);

        //4. refresh token db 저장
        userEntity.setRefreshToken(jwtToken.getRefreshToken());

        // 5. 토큰 정보로 response 생성 후 리턴
        return LoginResponse.builder()
                .userIdx(userEntity.getUserIdx())
                .userNickname(userEntity.getUserNickname())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }

}

