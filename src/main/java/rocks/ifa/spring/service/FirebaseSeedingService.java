package rocks.ifa.spring.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.WriteBatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor // ✅ 使用 Lombok 實現建構函式注入
public class FirebaseSeedingService {

    private final Firestore firestore; // ✅ 改為 private final
    private final ObjectMapper objectMapper; // ✅ 改為 private final

	/**
	 * 1. 同步一般設定檔 (Metadata) 邏輯：先刪除 metadata 集合中所有舊文件，再重新寫入 JSON 內容。 特別注意：生命表
	 * (opt_life_table) 位於獨立集合，不受此處刪除影響。
	 */
	public void syncMetadataConfigs() {
		try {
			// --- 第一步：清空舊有的 Metadata ---
			log.info("🧹 [Metadata] 開始清理舊有的設定文件...");
			WriteBatch deleteBatch = firestore.batch();

			// 獲取 metadata 集合下的所有文件
			List<QueryDocumentSnapshot> documents = firestore.collection("metadata").get().get().getDocuments();
			for (QueryDocumentSnapshot doc : documents) {
				deleteBatch.delete(doc.getReference());
			}

			if (!documents.isEmpty()) {
				deleteBatch.commit().get();
				log.info("✅ 已成功刪除 {} 個舊設定文件", documents.size());
			} else {
				log.info("ℹ️ Metadata 集合目前為空，無需清理");
			}

			// --- 第二步：重新讀取並寫入設定檔 ---
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resolver.getResources("classpath:init-data/*.json");

			log.info("📂 [Metadata] 掃描到 {} 個檔案，準備重新寫入...", resources.length);

			for (Resource resource : resources) {
				try (InputStream is = resource.getInputStream()) {
					Map<String, Object> data = objectMapper.readValue(is, new TypeReference<Map<String, Object>>() {
					});
					String docId = (String) data.get("id");

					if (docId == null)
						continue;

					// 🛑 關鍵：如果是生命表，跳過不在此處處理
					if ("opt_life_table".equals(docId)) {
						continue;
					}

					// 將設定檔內容寫入 metadata 集合
					firestore.collection("metadata").document(docId).set(data).get();
					log.info("✅ 同步成功 (Metadata): [{}]", docId);
				}
			}
			log.info("✨ 一般設定檔全量同步完成！");
		} catch (Exception e) {
			log.error("❌ Metadata 同步失敗: ", e);
			throw new RuntimeException("Metadata Sync Failed", e);
		}
	}

	/**
	 * 2. 同步生命表 (Life Table) 邏輯：掃描所有 JSON，只處理 id 為 "opt_life_table" 的檔案。
	 * 注意：生命表採取覆蓋更新 (Batch Set)，不執行全量刪除以節省讀寫量。
	 */
	public void syncLifeTable() {
		try {
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resolver.getResources("classpath:init-data/*.json");

			log.info("📂 [LifeTable] 尋找生命表設定檔...");

			boolean found = false;

			for (Resource resource : resources) {
				try (InputStream is = resource.getInputStream()) {
					Map<String, Object> data = objectMapper.readValue(is, new TypeReference<Map<String, Object>>() {
					});
					String docId = (String) data.get("id");

					if ("opt_life_table".equals(docId)) {
						found = true;
						log.info("🚀 找到生命表檔案，啟動批次更新程序...");
						processLifeTableBatch(docId, data);
						break;
					}
				}
			}

			if (!found) {
				log.warn("⚠️ 未找到 id 為 'opt_life_table' 的設定檔");
			}

		} catch (Exception e) {
			log.error("❌ LifeTable 同步失敗: ", e);
			throw new RuntimeException("LifeTable Sync Failed", e);
		}
	}

	/**
	 * 內部私有方法：處理生命表批次寫入
	 */
	private void processLifeTableBatch(String collectionName, Map<String, Object> sourceData) throws Exception {
		List<Map<String, Object>> list = objectMapper.convertValue(sourceData.get("list"),
				new TypeReference<List<Map<String, Object>>>() {
				});

		if (list == null || list.isEmpty()) {
			log.warn("生命表 list 為空，不進行寫入");
			return;
		}

		log.info("📊 準備處理 {} 筆生命表數據...", list.size());

		WriteBatch batch = firestore.batch();
		int batchCount = 0;
		int totalCount = 0;

		for (Map<String, Object> row : list) {
			Integer year = (Integer) row.get("year");
			String gender = (String) row.get("gender");
			Integer age = (Integer) row.get("age");

			Object lifespanObj = row.get("expected_lifespan");
			Double lifespan = (lifespanObj instanceof Number) ? ((Number) lifespanObj).doubleValue() : 0.0;

			String docKey = year + "_" + gender + "_" + age;

			Map<String, Object> docData = new HashMap<>();
			docData.put("year", year);
			docData.put("gender", gender);
			docData.put("age", age);
			docData.put("expected_lifespan", lifespan);

			DocumentReference docRef = firestore.collection(collectionName).document(docKey);
			batch.set(docRef, docData);

			batchCount++;
			totalCount++;

			if (batchCount >= 500) {
				batch.commit().get();
				batch = firestore.batch();
				batchCount = 0;
			}
		}

		if (batchCount > 0) {
			batch.commit().get();
		}

		log.info("✨ 生命表同步完成！(已將 {} 筆資料更新至 {})", totalCount, collectionName);
	}
}
