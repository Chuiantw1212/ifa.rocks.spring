package com.en_chu.calculator_api_spring.util;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class FlexibleDateDeserializer extends JsonDeserializer<LocalDate> {

	@Override
	public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		String dateString = p.getText();

		// 1. 嘗試解析標準 YYYY-MM-DD
		try {
			return LocalDate.parse(dateString);
		} catch (DateTimeParseException e) {
			// 2. 失敗則嘗試解析 YYYY-MM，並預設為 1 號
			try {
				return YearMonth.parse(dateString).atDay(1);
			} catch (DateTimeParseException ex) {
				throw new RuntimeException("日期格式錯誤，請使用 YYYY-MM-DD 或 YYYY-MM");
			}
		}
	}
}