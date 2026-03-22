package com.en_chu.calculator_api_spring.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MetadataService {

	@Autowired
	private Firestore firestore;

	/**
	 * 抓取 metadata 集合下的所有文件，並組合成一個大 Map 格式會長這樣：{ "opt_gender": {...},
	 * "cfg_financial": {...} }
	 */
	public Map<String, Object> getAllMetadata() {
		Map<String, Object> allMetadata = new HashMap<>();
		try {
			// 1. 發起請求，抓取 metadata 集合內的所有文件
			ApiFuture<QuerySnapshot> future = firestore.collection("metadata").get();

			// 2. 等待結果 (Block until complete)
			List<QueryDocumentSnapshot> documents = future.get().getDocuments();

			for (QueryDocumentSnapshot document : documents) {
				// 3. 使用 document.getId() 作為 Key，內容作為 Value
				allMetadata.put(document.getId(), document.getData());
			}

			log.info("成功抓取 {} 筆元數據配置", allMetadata.size());
		} catch (Exception e) {
			log.error("抓取 Firebase Metadata 失敗: ", e);
		}
		return allMetadata;
	}
}