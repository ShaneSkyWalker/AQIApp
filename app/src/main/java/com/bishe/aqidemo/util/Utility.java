package com.bishe.aqidemo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bishe.aqidemo.db.Pm25DB;
import com.bishe.aqidemo.model.MeasureData;
import com.bishe.aqidemo.model.Node;
import com.bishe.aqidemo.model.Rank;
import com.bishe.aqidemo.model.WeatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangxiangyu on 16/5/11.
 * In AQIDemo
 */
public class Utility {
    /**
     * 解析Json返回的节点数据
     */
    public synchronized static List<Node> handleNodeResponse(JSONArray jsonArray) throws JSONException {
        List<Node> nodeList = new ArrayList<>();
        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject nodeObject = jsonArray.getJSONObject(i);
                Node node = new Node();
                node.setId(nodeObject.getInt("node_id"));
                node.setName(nodeObject.getString("node_name"));
                node.setLoc(nodeObject.getString("node_loc"));
                node.setLon(nodeObject.getDouble("node_lon"));
                node.setLat(nodeObject.getDouble("node_lat"));
                node.setVis(nodeObject.getBoolean("node_vis"));
                node.setCid(nodeObject.getInt("node_cid"));
                nodeList.add(node);
            }
            return nodeList;
        }
        return new ArrayList<>();
    }

    /**
     * 解析Json返回的节点测量数据
     */
    public synchronized static List<MeasureData> handleMeasureDataResponse(JSONArray jsonArray) throws JSONException {
        List<MeasureData> measureDataList = new ArrayList<>();
        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject dataObject = jsonArray.getJSONObject(i);
                MeasureData measureData = new MeasureData();
                measureData.setId(dataObject.getInt("measure_id"));
                measureData.setNid(dataObject.getInt("measure_nid"));
                measureData.setPm2_5(dataObject.getDouble("measure_pm25"));
                measureData.setPm10(dataObject.getDouble("measure_pm10"));
                measureData.setTime(dataObject.getString("measure_time"));
                measureDataList.add(measureData);
            }
            return measureDataList;
        }
        return new ArrayList<>();
    }
    /**
     * 解析Json返回的Api天气数据
     */
    public synchronized static WeatherData handleWeatherDataResponse(String nodeName, JSONArray HeArray) throws JSONException {
        JSONObject zero = HeArray.getJSONObject(0);
        JSONObject aqi = zero.getJSONObject("aqi");
        JSONObject city = aqi.getJSONObject("city");
        JSONObject basic = zero.getJSONObject("basic");
        JSONObject update = basic.getJSONObject("update");
        JSONObject now = zero.getJSONObject("now");
        JSONObject cond = now.getJSONObject("cond");
        WeatherData weatherData = new WeatherData();
        weatherData.setCityName(basic.getString("city"));
        weatherData.setNodeName(nodeName);
        weatherData.setPm10(city.getDouble("pm10"));
        weatherData.setPm25(city.getDouble("pm25"));
        weatherData.setAqi(city.getInt("aqi"));
        weatherData.setQuality(city.getString("qlty"));
        weatherData.setCode(cond.getInt("code"));
        weatherData.setUpdate(update.getString("loc"));
        weatherData.setTmp(now.getDouble("tmp"));
        weatherData.setHum(now.getDouble("hum"));
        //pm25DB.saveWeatherData(weatherData);
        Log.i("TAG", String.valueOf(weatherData.getPm10()));
        return weatherData;
    }

    public static String getSearchAddress(String userId, String query, int level) {
        String string = "http://10.0.2.2:8080/AqiWeb/searchServlet?userId=" + userId + "&query=" + query + "&level=" + level;
        return string;
    }
    /**
     * 解析Json返回排行榜信息
     */
    public synchronized static ArrayList<Rank> handleList(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject body = jsonObject.getJSONObject("showapi_res_body");
            JSONArray list = body.getJSONArray("list");
            ArrayList<Rank> ranks = new ArrayList<Rank>();
            for (int i = 0; i < list.length(); i++) {
                JSONObject object = list.getJSONObject(i);
                Rank rank = new Rank();
                rank.setRank(String.valueOf(i+1));
                rank.setLoc(object.getString("area"));
                rank.setAqi(object.getString("aqi"));
                rank.setPm10(object.getString("pm10"));
                rank.setPm25(object.getString("pm2_5"));
                ranks.add(rank);
            }
            return ranks;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}