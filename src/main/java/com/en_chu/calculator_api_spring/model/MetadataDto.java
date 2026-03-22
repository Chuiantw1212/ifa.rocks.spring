package com.en_chu.calculator_api_spring.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true) // 忽略不認識的欄位
public class MetadataDto {
	// 每個設定檔都有的共用欄位
	private String id;
	private String name;

	// 給 opt_ 系列用的 (選單陣列)
	private List<Map<String, Object>> list;

	// 給 cfg_ 系列用的 (動態屬性容器)
	// 所有不屬於 id, name, list 的欄位，都會自動進到這個 Map
	private Map<String, Object> properties = new HashMap<>();

	@JsonAnySetter
	public void addProperty(String key, Object value) {
		this.properties.put(key, value);
	}
}