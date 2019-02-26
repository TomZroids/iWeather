package com.feige.iweather.web;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.weixvn.wae.webpage.WebPage;

import com.feige.iweather.Weather;

import android.os.Bundle;
import android.os.Message;

public class UpdateWeather extends WebPage {

    String m_tm ;
	@Override
	public void onStart() {
		String city = getHtmlValue("city");
		// 这里的AK替换成自己申请的百度API KEY
		final String ak = "A72e372de05e63c8740b2622d0ed8ab1";
		this.uri = "http://api.map.baidu.com/telematics/v3/weather";
		this.type = RequestType.GET;
	    System.out.println(uri);
		this.params = getParams();
		params.put("location", city);
		params.put("ak", ak);
	}

	@Override
	public void onSuccess(Document doc) {
		analyze(doc);
	}

	private void analyze(Document doc) {
		System.out.println(doc);
		Message msg = Weather.handler.obtainMessage();
		String status = doc.getElementsByTag("status").get(0).text();
		if ("success".equals(status)) {
			// 查询成功
			msg.what = 1;
			String city = doc.getElementsByTag("currentcity").get(0).text();
		    System.out.println(city+"fuck3333333333");
			Element weatherDataElem = doc.getElementsByTag("weather_data").get(
					0);
			Elements dateElem = weatherDataElem.getElementsByTag("date");
			Elements weatherElem = weatherDataElem.getElementsByTag("weather");
			Elements windElem = weatherDataElem.getElementsByTag("wind");
			Elements temperatureElem = weatherDataElem
					.getElementsByTag("temperature");
			
			String pm25 = doc.getElementsByTag("pm25").get(0).text();// 获取 pm25
			
		    System.out.println(pm25+"3333333333333333333333");
			String[] dateArray = new String[4];
			String[] weatherArray = new String[4];
			String[] windArray = new String[4];
			String[] temperatureArray = new String[4];
			String[] array = new String[4] ;// 温度 
			
			String currentTemperature = null;
			for (int i = 0; i < 4; i++) {
				String date = dateElem.get(i).text();
				if (i == 0) {
					if (date.contains("实时")) {
						currentTemperature = date.substring(
								date.indexOf("：") + 1, date.indexOf("℃")) + "°";
					}
					date = date.substring(0, 2);
				}
				dateArray[i] = date;
				weatherArray[i] = weatherElem.get(i).text();
				windArray[i] = windElem.get(i).text();
				String temperature = temperatureElem.get(i).text();
				if (temperature.contains("~")) {
					String highTem = temperature.substring(0,
							temperature.indexOf(" "));
					//最高温度 获取 4 个 
//					m_tm = highTem;
//					
			
			 
//					System.out.println(array[i]+"333333333");
//					System.out.println(highTem+"fiegeifeigeifiegeigeifiegie");
					String lowTem = temperature.substring(
							temperature.lastIndexOf(" ") + 1,
							temperature.indexOf("℃"));
					temperature = lowTem + "~" + highTem + "°";
					
				} else {
					temperature = temperature.replace("℃", "°");
				}
				temperatureArray[i] = temperature;
						array [i] = m_tm ;
				
			}
			if (currentTemperature == null) {
				currentTemperature = temperatureArray[0];
			}
			Bundle bundle = new Bundle();
			bundle.putStringArray("date", dateArray);
			bundle.putStringArray("weather", weatherArray);
			bundle.putStringArray("wind", windArray);
			bundle.putStringArray("temperature", temperatureArray);
			bundle.putString("city", city);
			bundle.putString("current_temperature", currentTemperature);
			
			bundle.putString("pm25", pm25);//pm25
//			bundle.putStringArray("m_tm", array);// 温度 
			
			msg.setData(bundle);
		} else if ("No result available".equals(status)) {
			// 没有天气信息
			msg.what = 2;
		} else {
			// 其他错误
			msg.what = 0;
		}
		Weather.handler.sendMessage(msg);
	}
}
