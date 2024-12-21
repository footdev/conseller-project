package com.conseller.conseller.core.user.infrastructure;

import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterEntity;
import com.conseller.conseller.core.barter.infrastructure.entity.BarterRequestEntity;
import com.conseller.conseller.core.bid.infrastructure.BiddingEntity;
import com.conseller.conseller.core.gifticon.infrastructure.GifticonEntity;
import com.conseller.conseller.core.user.domain.User;
import com.conseller.conseller.global.entity.BaseTimeEntity;
import com.conseller.conseller.core.inquiry.infrastructure.InquiryEntity;
import com.conseller.conseller.core.notification.infrastructure.NotificationEntity;
import com.conseller.conseller.core.store.infrastructure.StoreEntity;
import com.conseller.conseller.core.user.api.dto.request.UserInfoRequest;
import com.conseller.conseller.core.user.domain.enums.Authority;
import com.conseller.conseller.core.user.domain.enums.UserStatus;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "\"USER\"")
public class UserEntity extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_phone_number", nullable = false)
    private String userPhoneNumber;

    @Column(name = "user_nickname", nullable = false)
    private String userNickname;

    @Column(name = "user_gender", nullable = false)
    private Character userGender;

    @Column(name = "user_age", nullable = false)
    private Integer userAge;

    @Column(name = "user_deleted_date")
    private LocalDateTime userDeletedDate;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_cash")
    private Long userCash;

    @Builder.Default
    @Column(name = "user_status", nullable = false)
    private String userStatus = UserStatus.ACTIVE.getStatus();

    @Column(name = "user_restrict_end_date")
    private LocalDateTime userRestrictEndDate;

    @Builder.Default
    @Column(name = "user_restrict_count")
    private Integer userRestrictCount = 0;

    @Column(name = "user_profile_url")
    private String userProfileUrl;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "fcm_token")
    private String fcm;

    @Column(name = "user_pattern")
    private String userPattern;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    public String getName() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //비밀번호 암호화
    public void encryptPassword(PasswordEncoder passwordEncoder) {
        this.userPassword = passwordEncoder.encode(this.userPassword);
    }

    //해당 비밀번호가 맞는지 확인
    public boolean checkPassword(PasswordEncoder passwordEncoder, String userPassword) {
        return passwordEncoder.matches(userPassword, this.userPassword);
    }

    public void addUserRole() {
        this.roles.add(Authority.USER.name());
    }

    public void addAdminRole() {
        this.roles.add(Authority.ADMIN.name());
    }

    public void updatePassword(String password, PasswordEncoder passwordEncoder) {
        this.userPassword = password;
        encryptPassword(passwordEncoder);
    }

    public void updateUserInfo(UserInfoRequest userInfoRequest) {

        if (!checkPassword(new BCryptPasswordEncoder(), userInfoRequest.getUserPassword())
        && !this.userPassword.equals(userInfoRequest.getUserPassword())) {
            this.userPassword = userInfoRequest.getUserPassword();
            encryptPassword(new BCryptPasswordEncoder());
        }

        this.userNickname = userInfoRequest.getUserNickname();
        this.userEmail = userInfoRequest.getUserEmail();
    }

    public User toDomain() {
        return User.builder()
                .userIdx(this.userIdx)
                .userId(this.userId)
                .userPassword(this.userPassword)
                .userEmail(this.userEmail)
                .userPhoneNumber(this.userPhoneNumber)
                .userNickname(this.userNickname)
                .userGender(this.userGender)
                .userAge(this.userAge)
                .userCash(this.userCash)
                .userDeletedDate(this.userDeletedDate)
                .userName(this.userName)
                .userStatus(UserStatus.valueOf(this.userStatus))
                .userRestrictEndDate(this.userRestrictEndDate)
                .userRestrictCount(this.userRestrictCount)
                .userProfileUrl(this.userProfileUrl)
                .refreshToken(this.refreshToken)
                .fcm(this.fcm)
                .userPattern(this.userPattern)
                .build();
    }

    public static UserEntity of(User user) {
        return UserEntity.builder()
                .userIdx(user.getUserIdx())
                .userId(user.getUserId())
                .userPassword(user.getUserPassword())
                .userEmail(user.getUserEmail())
                .userPhoneNumber(user.getUserPhoneNumber())
                .userNickname(user.getUserNickname())
                .userGender(user.getUserGender())
                .userAge(user.getUserAge())
                .userCash(user.getUserCash())
                .userDeletedDate(user.getUserDeletedDate())
                .userName(user.getUserName())
                .userStatus(user.getUserStatus().getStatus())
                .userRestrictEndDate(user.getUserRestrictEndDate())
                .userRestrictCount(user.getUserRestrictCount())
                .userProfileUrl(user.getUserProfileUrl())
                .refreshToken(user.getRefreshToken())
                .fcm(user.getFcm())
                .userPattern(user.getUserPattern())
                .build();
    }
}
