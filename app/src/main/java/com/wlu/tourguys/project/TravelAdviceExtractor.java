package com.wlu.tourguys.project;

import org.json.JSONArray;
import org.json.JSONObject;

public class TravelAdviceExtractor {

    public static String extractTravelAdviceText(String jsonResponse) {
        try {
            // 将响应字符串解析为 JSON 对象
            JSONObject json = new JSONObject(jsonResponse);

            // 从 JSON 对象中获取候选数组
            JSONArray candidates = json.getJSONArray("candidates");

            // 获取第一个候选项的内容部分
            if (candidates.length() > 0) {
                JSONObject firstCandidate = candidates.getJSONObject(0);
                JSONObject content = firstCandidate.getJSONObject("content");

                // 获取内容的部件数组
                JSONArray parts = content.getJSONArray("parts");

                // 获取第一个部件的文本信息
                if (parts.length() > 0) {
                    JSONObject firstPart = parts.getJSONObject(0);
                    return firstPart.getString("text"); // 返回提取的文本内容
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ""; // 如果提取失败，返回空字符串
    }
}
