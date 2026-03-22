package com.en_chu.calculator_api_spring.service;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.en_chu.calculator_api_spring.model.LifeExpectancyRes;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LifeExpectancyService {

	@Autowired
	private Firestore firestore;

	private static final String COLLECTION_NAME = "opt_life_table";

	/**
	 * 查詢預期壽命 邏輯： 1. 若年齡 < 100，ID 為 "YEAR_GENDER_AGE" 2. 若年齡 >= 100，ID 為
	 * "YEAR_GENDER_null" (根據規格需求)
	 */
	public LifeExpectancyRes getLifeExpectancy(Integer year, String gender, Integer age) {

		// --- 1. 處理 Key 的生成邏輯 ---
		String ageSuffix;
		if (age >= 100) {
			ageSuffix = "null"; // 100歲以上統一查這筆
		} else {
			ageSuffix = String.valueOf(age);
		}

		// e.g. 30歲 -> "2025_MALE_30"
		// e.g. 105歲 -> "2025_MALE_null"
		String docKey = year + "_" + gender + "_" + ageSuffix;

		try {
			// --- 2. 查詢 Firestore ---
			DocumentSnapshot doc = firestore.collection(COLLECTION_NAME).document(docKey).get().get();

			if (doc.exists()) {
				Double val = doc.getDouble("expected_lifespan");
				BigDecimal lifespan = (val != null) ? BigDecimal.valueOf(val) : BigDecimal.ZERO;

				// --- 3. 回傳結果 ---
				// 注意：這裡我們回傳 "使用者查詢的原始年齡 (age)"，而不是 "null"
				// 這樣前端收到 response 時才知道他是查 105 歲的結果
				return new LifeExpectancyRes(year, gender, age, lifespan);
			} else {
				log.warn("查無預期壽命資料: {}", docKey);
				return null;
			}

		} catch (InterruptedException | ExecutionException e) {
			log.error("查詢 Firestore 發生錯誤: ", e);
			// 恢復中斷狀態 (Java Best Practice)
			Thread.currentThread().interrupt();
			throw new RuntimeException("資料庫連線異常");
		}
	}
}