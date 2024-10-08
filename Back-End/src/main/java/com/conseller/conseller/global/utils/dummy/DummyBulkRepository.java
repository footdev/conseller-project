package com.conseller.conseller.global.utils.dummy;

import com.conseller.conseller.core.auction.infrastructure.AuctionEntity;
import com.conseller.conseller.core.gifticon.api.dto.response.GifticonResponse;
import com.conseller.conseller.core.user.api.dto.request.SignUpRequest;
import com.conseller.conseller.core.user.domain.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.conseller.conseller.global.utils.DateTimeConverter.*;

@Repository
@RequiredArgsConstructor
public class DummyBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveAllUsers(List<SignUpRequest> users) {
        String sql = "INSERT INTO user (created_date, user_account, user_account_bank, user_age, user_deposit, user_gender, " +
                "user_id, user_name, user_nickname, user_password, user_phone_number, user_status) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.batchUpdate(
                    sql,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
                            ps.setString(2, users.get(i).getUserAccount());
                            ps.setString(3, users.get(i).getUserAccountBank());
                            ps.setInt(4, users.get(i).getUserAge());
                            ps.setLong(5, 0);
                            ps.setString(6, String.valueOf(users.get(i).getUserGender()));
                            ps.setString(7, users.get(i).getUserId());
                            ps.setString(8, users.get(i).getUserName());
                            ps.setString(9, users.get(i).getUserNickname());
                            ps.setString(10, users.get(i).getUserPassword());
                            ps.setString(11, users.get(i).getUserPhoneNumber());
                            ps.setString(12, UserStatus.ACTIVE.getStatus());
                        }

                        @Override
                        public int getBatchSize() {
                            return users.size();
                        }
                    }
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void saveAllGifticons(List<GifticonResponse> gifticons) {
        String sql = "INSERT INTO gifticon (gifticon_barcode, gifticon_name, gifticon_start_date, gifticon_end_date, gifticon_all_image_url, gifticon_status, " +
                "user_idx, sub_category_idx, main_category_idx) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.batchUpdate(
                    sql,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setString(1, gifticons.get(i).getGifticonBarcode());
                            ps.setString(2, gifticons.get(i).getGifticonName());
                            ps.setTimestamp(3, convertTimestamp(gifticons.get(i).getGifticonStartDate()));
                            ps.setTimestamp(4, convertTimestamp(gifticons.get(i).getGifticonEndDate()));
                            ps.setString(5, gifticons.get(i).getGifticonAllImageUrl());
                            ps.setString(6, gifticons.get(i).getGifticonStatus());
                            ps.setLong(7, gifticons.get(i).getUserIdx());
                            ps.setInt(8, gifticons.get(i).getSubCategoryIdx());
                            ps.setInt(9, gifticons.get(i).getMainCategoryIdx());
                        }

                        @Override
                        public int getBatchSize() {
                            return gifticons.size();
                        }
                    }
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void saveAllAuctions(List<AuctionEntity> auctionEntities) {
        String sql = "INSERT INTO auction (" +
                "auction_end_date, " +
                "auction_start_date, " +
                "auction_status, " +
                "auction_text, " +
                "lower_price, " +
                "upper_price, " +
                "gifticon_idx, " +
                "user_idx" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.batchUpdate(
                    sql,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setTimestamp(1, convertTimestamp(convertString(auctionEntities.get(i).getAuctionEndDate())));
                            ps.setTimestamp(2, convertTimestamp(convertString(auctionEntities.get(i).getAuctionStartDate())));
                            ps.setString(3, auctionEntities.get(i).getAuctionStatus());
                            ps.setString(4, auctionEntities.get(i).getAuctionText());
                            ps.setInt(5, auctionEntities.get(i).getLowerPrice());
                            ps.setInt(6, auctionEntities.get(i).getUpperPrice());
                            ps.setLong(7, auctionEntities.get(i).getGifticonEntity().getGifticonIdx());
                            ps.setLong(8, auctionEntities.get(i).getUserEntity().getUserIdx());
                        }

                        @Override
                        public int getBatchSize() {
                            return auctionEntities.size();
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
