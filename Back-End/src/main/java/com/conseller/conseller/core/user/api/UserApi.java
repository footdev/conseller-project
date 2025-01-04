package com.conseller.conseller.core.user.api;

import com.conseller.conseller.core.user.api.dto.request.*;
import com.conseller.conseller.core.user.api.dto.response.*;
import com.conseller.conseller.core.user.domain.UserService;
import com.conseller.conseller.core.email.infrastructure.EmailService;
import com.conseller.conseller.global.api.dto.CommonResponse;
import com.conseller.conseller.global.api.dto.EmailResponse;
import com.conseller.conseller.global.security.infrastructure.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserApi {

    private final UserService userService;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<UserIdxResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        long userIdx = userService.signUp(signUpRequest.toUser());
        return ResponseEntity.ok()
                .body(new UserIdxResponse(userIdx));
    }

    //일반 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> logIn(@Valid @RequestBody LoginRequest loginRequest) {

        LoginResponse loginResponse = userService.login(loginRequest.toTargetUser());
        return ResponseEntity.ok()
                .body(loginResponse);
    }

    //액세스 토큰 재발급 요청
    @GetMapping("/refresh/{userIdx}")
    public ResponseEntity<Object> reCreateAccessToken(HttpServletRequest request, @PathVariable long userIdx) {

        log.info("액세스 토큰 재발급 요청");

        return ResponseEntity.ok()
                .body(userService.reCreateAccessToken(request, userIdx));
    }

    //닉네임 중복체크
    @PostMapping("/nickname")
    public ResponseEntity<Object> checkNickname(@RequestBody NicknameRequest nicknameRequest) {

        InfoValidationResponse infoValidationResponse = userService.checkNickname(nicknameRequest.getUserNickname());

        return ResponseEntity.ok()
                .body(infoValidationResponse);
    }

    //id 중복체크
    @PostMapping("/id")
    public ResponseEntity<Object> checkId(@RequestBody IdRequest idRequest) {

        InfoValidationResponse infoValidationResponse = userService.checkId(idRequest.getUserId());
        return ResponseEntity.ok()
                .body(infoValidationResponse);
    }

    //이메일 중복체크
    @PostMapping("/email")
    public ResponseEntity<Object> checkEmail(@RequestBody EmailRequest emailRequest) {

        InfoValidationResponse infoValidationResponse = userService.checkEmail(emailRequest.getUserEmail());

        return ResponseEntity.ok()
                .body(infoValidationResponse);
    }

    //전화번호 중복체크
    @PostMapping("/phone-number")
    public ResponseEntity<Object> checkPhoneNumber(@RequestBody PhoneNumberRequest phoneNumberRequest) {

        InfoValidationResponse infoValidationResponse = userService.checkPhoneNumber(phoneNumberRequest.getUserPhoneNumber());

        return ResponseEntity.ok()
                .body(infoValidationResponse);
    }

    //부분 암호화된 아이디 출력
    @PostMapping("/encode/id")
    public ResponseEntity<Object> getEncodeUserId(@RequestBody EmailAndNameRequest emailAndNameRequest) {
        return ResponseEntity.ok()
                .body(userService.getHiddenUserId(emailAndNameRequest));
    }

    // 임시 비밀번호 발급
    @PatchMapping("/encode/pw")
    public ResponseEntity<Object> changeTempPassword(@RequestBody EmailAndIdRequest emailAndIdRequest) throws Exception {

        String tempPassword = userService.generateTemporaryPassword(emailAndIdRequest);

        EmailResponse emailResponse = EmailResponse.builder()
                .userEmail(emailAndIdRequest.getUserEmail())
                .userPassword(tempPassword)
                .userName(emailAndIdRequest.getUserId())
                .build();

       emailService.sendEmail(emailResponse);

        return ResponseEntity.ok()
                .build();
    }

    //유저 정보 변경
    @PutMapping("/{userIdx}")
    public ResponseEntity<Void> updateUserInfo(@PathVariable long userIdx, @Valid @RequestBody UserInfoRequest userInfoRequest) {

        userService.updateUserInfo(userIdx, userInfoRequest);

        return ResponseEntity.ok().build();
    }

    //유저 정보 조회
    @GetMapping("/{userIdx}/userInfo")
    public ResponseEntity<UserInfoResponse> getUserInfoByUserIdx(@PathVariable long userIdx) {
        return ResponseEntity.ok()
                .body(UserInfoResponse.of(userService.getUserInfo(userIdx)));
    }

    //내 정보 변경용 비밀번호 확인
    @PostMapping("/valid")
    public ResponseEntity<CommonResponse> checkUserPassword(@RequestBody UserCheckPasswordRequest userCheckPasswordRequest) {
        userService.checkUserPassword(userCheckPasswordRequest);
        return ResponseEntity.ok().build();
    }

    //회원 탈퇴
    @DeleteMapping("/{userIdx}")
    public ResponseEntity<Object> deleteUser(@PathVariable long userIdx, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);

        userService.deleteUser(userIdx, token);
        return ResponseEntity.ok().build();
    }

    //fcm 토큰 발급
    @PostMapping("/firebaseToken/{userIdx}")
    public ResponseEntity<Object> setFirebaseToken(@PathVariable Long userIdx, @RequestBody FirebaseRequest request) {
        userService.setFirebaseToken(userIdx, request);

        return ResponseEntity.ok()
                .build();
    }

    // pattern 저장
    @PostMapping("/savepattern")
    public ResponseEntity<Void> patternSave(@Valid @RequestBody UserPatternRequest userPatternRequest) {
        userService.patternRegister(userPatternRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verifypattern")
    public ResponseEntity<LoginResponse> patternLogin(@Valid @RequestBody UserPatternRequest userPatternRequest){
        LoginResponse loginResponse = userService.loginPattern(userPatternRequest);
        userService.patternRegister(userPatternRequest);
        return ResponseEntity.ok()
                .body(loginResponse);
    }

    @GetMapping("/finger/{userIdx}")
    public ResponseEntity<LoginResponse> fingerLogin(@PathVariable Long userIdx) {
        LoginResponse loginResponse = userService.loginFinger(userIdx);
        return ResponseEntity.ok()
                .body(loginResponse);
    }

    // 캐시 충전 (임시)
    @PostMapping("/charge")
    public ResponseEntity<Object> chargeCash(@RequestBody ChargeCashRequest chargeCashRequest) {
        userService.chargeCash(chargeCashRequest);
        return ResponseEntity.ok().build();
    }
}
