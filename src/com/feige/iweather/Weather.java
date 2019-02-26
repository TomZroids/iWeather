package com.feige.iweather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.weixvn.wae.manager.EngineManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceActivity.Header;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.dacer.androidcharts.LineView;
import com.feige.iweather.util.Utils;
import com.feige.iweather.web.UpdateWeather;
import com.feige.sweather.R;

public class Weather extends Activity {

	LinearLayout arc;

	private static final int[] ITEM_DRAWABLES = { R.drawable.composer_place,
			R.drawable.composer_sleep, R.drawable.composer_thought,
			R.drawable.composer_with };;

	final private String DATE_KEY[] = { "date_0", "date_1", "date_2", "date_3" };
	final private String WEATHER_KEY[] = { "weather_0", "weather_1",
			"weather_2", "weather_3" };
	final private String WIND_KEY[] = { "wind_0", "wind_1", "wind_2", "wind_3" };
	final private String TEMPERATURE_KEY[] = { "temperature_0",
			"temperature_1", "temperature_2", "temperature_3" };
	int f, b, c, d;
	public static Handler handler;
	public static Weather context;
	private String[] dateArray, weatherArray, windArray, temperatureArray;
	private SharedPreferences sp;
	private LinearLayout titleBarLayout;
	private LinearLayout changeCity;
	private TextView cityText;
	private ImageView share;
	private ImageView about;
	private ImageView refresh;
	private ProgressBar refreshing;
	private TextView updateTimeText;
	private LinearLayout currentWeatherLayout;
	private ImageView weatherIcon;
	private TextView currentTemperatureText;
	private TextView currentWeatherText;
	private TextView temperatureText;
	private TextView windText;
	private TextView dateText;

	public static boolean isForeground = false;
	private EditText msgText;

	private TextView pm25Text;
	private TextView pmText;

	private ListView weatherForecastList;
	private Intent intent;
	private Time time;
	private Runnable run;
	private Builder builder;
	private String currentWeekDay;
	private String city;
	private String currentTemperature;
	private int index = 0;
	Button button;

	private String pm_25;
	private String pm;

	private int tm;
	private String tm1;
	int[] tm_array = new int[4];

	private Vibrator vibrator;

	private String pm_2_5;
	int number2;
	int a;
	public static String My_temperature = "";
	int my_number;
	private String pm_warning;
	private ThreeDSlidingLayout slidingLayout;

	private ImageButton menuButton;

	private LinearLayout contentListView;

	LineView lineView;
	public static final ArrayList<ArrayList<Integer>> dataLists = null;
	int randomint = 3 + 1;

	private Effectstype effect;
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.feige.iweather.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	Animation operatingAnim;
	LinearInterpolator lin;
	Animation operatingAnim2;
	LinearInterpolator lin2;

	ImageView infoOperatingIV;
	ImageView infoOperatingIV2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather);

		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
		infoOperatingIV2 = (ImageView) findViewById(R.id.fuck2);
		operatingAnim2 = AnimationUtils.loadAnimation(this, R.anim.tip);
		lin2 = new LinearInterpolator();
		operatingAnim2.setInterpolator(lin);

		infoOperatingIV = (ImageView) findViewById(R.id.infoOperating);
		operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
		lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);

		slidingLayout = (ThreeDSlidingLayout) findViewById(R.id.slidingLayout);
		menuButton = (ImageButton) findViewById(R.id.button1);
		contentListView = (LinearLayout) findViewById(R.id.contentList);
		findViewById(R.layout.fragment_line);
		lineView = (LineView) findViewById(R.id.line_view);

		slidingLayout.setScrollEvent(contentListView);
		menuButton.setOnClickListener(new OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if (slidingLayout.isLeftLayoutVisible()) {
					slidingLayout.scrollToRightLayout();
					RayMenu.mRayLayout.switchState(false);
				} else {
					slidingLayout.scrollToLeftLayout();
					RayMenu.mRayLayout.switchState(true);
					randomSet(lineView);

				}
			}
		});

		EngineManager.getInstance().setContext(this.getApplicationContext())
				.setDB(null);
		titleBarLayout = (LinearLayout) findViewById(R.id.title_bar_layout);
		changeCity = (LinearLayout) findViewById(R.id.change_city_layout);
		cityText = (TextView) findViewById(R.id.city);
		share = (ImageView) findViewById(R.id.share);
		about = (ImageView) findViewById(R.id.about);

		pm25Text = (TextView) findViewById(R.id.pm25);
		pmText = (TextView) findViewById(R.id.pmtext);

		refresh = (ImageView) findViewById(R.id.refresh);
		refreshing = (ProgressBar) findViewById(R.id.refreshing);
		updateTimeText = (TextView) findViewById(R.id.update_time);
		currentWeatherLayout = (LinearLayout) findViewById(R.id.current_weather_layout);
		weatherIcon = (ImageView) findViewById(R.id.weather_icon);
		currentTemperatureText = (TextView) findViewById(R.id.current_temperature);
		currentWeatherText = (TextView) findViewById(R.id.current_weather);
		temperatureText = (TextView) findViewById(R.id.temperature);
		arc = (LinearLayout) findViewById(R.id.arc);

		windText = (TextView) findViewById(R.id.wind);
		dateText = (TextView) findViewById(R.id.date);
		weatherForecastList = (ListView) findViewById(R.id.weather_forecast_list);
		changeCity.setOnClickListener(new ButtonListener());
		share.setOnClickListener(new ButtonListener());
		about.setOnClickListener(new ButtonListener());
		refresh.setOnClickListener(new ButtonListener());
		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/HelveticaNeueLTPro-Lt.ttf");
		currentTemperatureText.setTypeface(face);
		setCurrentWeatherLayoutHight();
		handler = new MyHandler();
		context = this;
		time = new Time();

		run = new Runnable() {

			@Override
			public void run() {
				refreshing(false);
				Toast.makeText(Weather.this, "亲，你的网络出现问题了～", Toast.LENGTH_SHORT)
						.show();
			}
		};

		sp = getSharedPreferences("weather", Context.MODE_PRIVATE);
		if ("".equals(sp.getString("city", ""))) {

			intent = new Intent();
			intent.setClass(Weather.this, SelectCity.class);
			intent.putExtra("city", "");
			Weather.this.startActivityForResult(intent, 100);
			updateTimeText.setText("正在更新");
		} else {
			initData();
			updateWeatherImage();
			updateWeatherInfo();
		}

		RayMenu rayMenu = (RayMenu) findViewById(R.id.ray_menu);
		img();

		final int itemCount = ITEM_DRAWABLES.length;
		for (int i = 0; i < itemCount; i++) {
			ImageView item = new ImageView(this);
			item.setImageResource(ITEM_DRAWABLES[i]);
			final NiftyDialogBuilder dialogBuilder3 = NiftyDialogBuilder
					.getInstance(this);
			final int position = i;
			rayMenu.addItem(item, new OnClickListener() {

				@Override
				public void onClick(View v) {

					switch (position) {
					case 0:
						intent = new Intent();
						intent.setClass(Weather.this, SelectCity.class);
						Weather.this.startActivityForResult(intent, 100);
						break;

					case 1:
						Intent intent = new Intent();
						intent.setClass(Weather.this, WebStore.class);
						startActivity(intent);
						break;

					case 2:
						intent = new Intent(Intent.ACTION_SEND);
						intent.setType("image/*");
						intent.putExtra(Intent.EXTRA_SUBJECT, "好友分享");
						intent.putExtra(Intent.EXTRA_TEXT, "今天温度"
								+ temperatureArray[index] + ", "
								+ weatherArray[index] + "。 " + "空气质量： "
								+ pm_2_5 + "。" + pm_warning + "");
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						Weather.this.startActivity(Intent.createChooser(intent,
								"［分享给好友］爱别人就是爱自己"));
						break;

					case 3:

						effect = Effectstype.RotateLeft;
						dialogBuilder3
								.withTitle("    ")
								.withTitleColor("#FFE74C3C")
								.withDividerColor("#11000000")
								.withMessage(
										"                   雾霾围城   \n "
												+ "             V.2.1.23")
								.withMessageColor("#FFFFFFFF")
								.withDialogColor("#FFE74C3C").withDuration(700)
								.withEffect(effect).withButton1Text("OK")
								.isCancelableOnTouchOutside(true)
								.setButton1Click(new View.OnClickListener() {
									@Override
									public void onClick(View v) {

										dialogBuilder3.dismiss();
										;
									}
								}).show();
						break;
					default:
						break;
					}
				}
			});// Add a menu item
		}
	}

	private Animation createHintSwitchAnimation(boolean expanded) {
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 1 && !data.getStringExtra("city").equals(city)) {
			city = data.getStringExtra("city");
			cityText.setText(city);
			updateTimeText.setText("正在更新");
			if (Utils.checkNetwork(Weather.this) == false) {
				Toast.makeText(Weather.this, "亲，你的网络出现问题了～", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			updateWeather();
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			refreshing(false);
			switch (msg.what) {
			case 0:
				Toast.makeText(Weather.this, "更新失败,请稍候再试", Toast.LENGTH_SHORT)
						.show();
				break;
			case 1:
				try {
					handler.removeCallbacks(run);
					Bundle bundle = msg.getData();
					dateArray = bundle.getStringArray("date");
					weatherArray = bundle.getStringArray("weather");
					windArray = bundle.getStringArray("wind");
					temperatureArray = bundle.getStringArray("temperature");
					city = bundle.getString("city");
					tm = bundle.getInt("m_tm");

					currentTemperature = bundle
							.getString("current_temperature");
					pm_25 = bundle.getString("pm25");

					saveData();
					initData();
					updateWeatherImage();
					updateWeatherInfo();
				} catch (Exception e) {
					Toast.makeText(Weather.this, "请输入正确城市名", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case 2:
				try {
					builder = new Builder(Weather.this);
					builder.setTitle("提示");
					builder.setMessage("没有查询到[" + city + "]的天气信息。");
					builder.setPositiveButton("重试",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									intent = new Intent();
									intent.setClass(Weather.this,
											SelectCity.class);
									Weather.this.startActivityForResult(intent,
											100);
								}
							});

					builder.setCancelable(false);
					builder.show();
					break;
				} catch (Exception e) {
					Toast.makeText(Weather.this, "更新失败,请稍候再试2",
							Toast.LENGTH_SHORT).show();
				}

			case 3:
				m_dialog();

				break;

			case 4:
				name();

				break;
			case 5:
				name2();

				break;
			default:

				break;
			}
			super.handleMessage(msg);
		}
	}

	private void initData() {
		dateArray = new String[4];
		weatherArray = new String[4];
		windArray = new String[4];
		temperatureArray = new String[4];

		for (int i = 0; i < 4; i++) {
			dateArray[i] = sp.getString(DATE_KEY[i], "");
			weatherArray[i] = sp.getString(WEATHER_KEY[i], "");
			windArray[i] = sp.getString(WIND_KEY[i], "");
			temperatureArray[i] = sp.getString(TEMPERATURE_KEY[i], "");
			tm1 = temperatureArray[i];

			tm1 = tm1.substring(tm1.indexOf("~") + 1,
					tm1.length() - "°".length());
			tm_array[i] = Integer.parseInt(tm1);
		}
		city = sp.getString("city", "");

		pm_25 = sp.getString("pm25", "");
		pm = sp.getString("pm", "");
		pm_2_5 = sp.getString("pm_2_5", "");

		ArrayList<String> test = new ArrayList<String>();
		for (int i = 0; i < randomint; i++) {
			test.add(String.valueOf(i + 1));
		}
		lineView.setBottomTextList(test);
		lineView.setDrawDotLine(true);
		lineView.setShowPopup(LineView.SHOW_POPUPS_NONE);

		currentTemperature = sp.getString("current_temperature", "");
		time.setToNow();
		switch (time.weekDay) {
		case 0:
			currentWeekDay = "周日";
			break;
		case 1:
			currentWeekDay = "周一";
			break;
		case 2:
			currentWeekDay = "周二";
			break;
		case 3:
			currentWeekDay = "周三";
			break;
		case 4:
			currentWeekDay = "周四";
			break;
		case 5:
			currentWeekDay = "周五";
			break;
		case 6:
			currentWeekDay = "周六";
			break;
		default:
			break;
		}
		for (int i = 0; i < 4; i++) {
			if (dateArray[i].equals(currentWeekDay)) {
				index = i;
			}
		}
	}

	public String pm2(int pm_25) {
		if (pm_25 > 300) {
			pm = "严重污染";
			pm_warning = "今天雾霾严重污染，空气质量 —— 无法呼吸 。世界很危险，还是家里好。亲 ，出去记得戴口罩～";
		} else if (pm_25 > 200) {
			pm = "重度污染";
			pm_warning = "今天雾霾重度污染，空气质量 —— 很不好 。亲 ，口罩还是带上吧～";
		} else if (pm_25 > 150) {
			pm = "中度污染";
			pm_warning = " 又是雾霾天哟～  亲 ，口罩还是带上吧～ ";
		} else if (pm_25 > 100) {
			pm = "轻度污染";
			pm_warning = "又是雾霾天哟～ 别害怕，放心出去玩～";
		} else if (pm_25 > 50) {
			pm_warning = "今天空气一般啦～ 别害怕，放心出去玩～";
			pm = "良";
		} else {
			pm = "优";
			pm_warning = "难得空气好，亲，出去玩吧～ 别宅在家里了～";
		}
		pm_2_5 = pm;
		return pm_2_5;

	}

	private void updateWeatherImage() {
		String currentWeather = weatherArray[index];
		if (currentWeather.contains("转")) {
			currentWeather = currentWeather.substring(0,
					currentWeather.indexOf("转"));
		}
		time.setToNow();
		if (currentWeather.contains("晴")) {
			if (time.hour >= 7 && time.hour < 19) {
				weatherIcon.setImageResource(R.drawable.weather_img_fine_day);
			} else {
				weatherIcon.setImageResource(R.drawable.weather_img_fine_night);
			}
		} else if (currentWeather.contains("多云")) {
			if (time.hour >= 7 && time.hour < 19) {
				weatherIcon.setImageResource(R.drawable.weather_img_cloudy_day);
			} else {
				weatherIcon
						.setImageResource(R.drawable.weather_img_cloudy_night);
			}
		} else if (currentWeather.contains("阴")) {
			weatherIcon.setImageResource(R.drawable.weather_img_overcast);
		} else if (currentWeather.contains("雷")) {
			weatherIcon.setImageResource(R.drawable.weather_img_thunder_storm);
		} else if (currentWeather.contains("雨")) {
			if (currentWeather.contains("小雨")) {
				weatherIcon.setImageResource(R.drawable.weather_img_rain_small);
			} else if (currentWeather.contains("中雨")) {
				weatherIcon
						.setImageResource(R.drawable.weather_img_rain_middle);
			} else if (currentWeather.contains("大雨")) {
				weatherIcon.setImageResource(R.drawable.weather_img_rain_big);
			} else if (currentWeather.contains("暴雨")) {
				weatherIcon.setImageResource(R.drawable.weather_img_rain_storm);
			} else if (currentWeather.contains("雨夹雪")) {
				weatherIcon.setImageResource(R.drawable.weather_img_rain_snow);
			} else if (currentWeather.contains("冻雨")) {
				weatherIcon.setImageResource(R.drawable.weather_img_sleet);
			} else {
				weatherIcon
						.setImageResource(R.drawable.weather_img_rain_middle);
			}
		} else if (currentWeather.contains("雪")
				|| currentWeather.contains("冰雹")) {
			if (currentWeather.contains("小雪")) {
				weatherIcon.setImageResource(R.drawable.weather_img_snow_small);
			} else if (currentWeather.contains("中雪")) {
				weatherIcon
						.setImageResource(R.drawable.weather_img_snow_middle);
			} else if (currentWeather.contains("大雪")) {
				weatherIcon.setImageResource(R.drawable.weather_img_snow_big);
			} else if (currentWeather.contains("暴雪")) {
				weatherIcon.setImageResource(R.drawable.weather_img_snow_storm);
			} else if (currentWeather.contains("冰雹")) {
				weatherIcon.setImageResource(R.drawable.weather_img_hail);
			} else {
				weatherIcon
						.setImageResource(R.drawable.weather_img_snow_middle);
			}
		} else if (currentWeather.contains("雾")) {
			weatherIcon.setImageResource(R.drawable.weather_img_fog);
		} else if (currentWeather.contains("霾")) {
			weatherIcon.setImageResource(R.drawable.weather_img_fog);
		} else if (currentWeather.contains("沙尘暴")
				|| currentWeather.contains("浮尘")
				|| currentWeather.contains("扬沙")) {
			weatherIcon.setImageResource(R.drawable.weather_img_sand_storm);
		} else {
			weatherIcon.setImageResource(R.drawable.weather_img_fine_day);
		}
	}

	private void updateWeatherInfo() {
		cityText.setText(city);

		pm25Text.setText("雾霾指数：" + pm_25);
		pmText.setText("空气等级：" + pm2(Integer.parseInt(pm_25)));

		My_temperature = getNumbers(currentTemperature);

		a = Integer.parseInt(My_temperature);
		sent(a);
		number2 = a;
		arc.addView(new com.feige.wireframe.HomeArc(this, a));

		currentWeatherText.setText(weatherArray[index]);
		temperatureText.setText(temperatureArray[index]);

		windText.setText(windArray[index]);
		Time time = new Time();
		time.setToNow();
		th_mdialog();

		String date = new SimpleDateFormat("MM/dd").format(new Date());
		dateText.setText(currentWeekDay + " " + date);
		String updateTime = sp.getString("update_time", "");
		if (Integer.parseInt(updateTime.substring(0, 4)) == time.year
				&& Integer.parseInt(updateTime.substring(5, 7)) == time.month + 1
				&& Integer.parseInt(updateTime.substring(8, 10)) == time.monthDay) {
			updateTime = "今天" + updateTime.substring(updateTime.indexOf(" "));
			updateTimeText.setTextColor(getResources().getColor(R.color.white));
		} else {
			updateTime = updateTime.substring(5).replace("-", "月")
					.replace(" ", "日 ");
			updateTimeText.setTextColor(getResources().getColor(R.color.red));
			if (Utils.checkNetwork(Weather.this) == true) {
				updateWeather();
			}
		}
		updateTimeText.setText(updateTime + " 发布");
		weatherForecastList.setAdapter(new MyAdapter(Weather.this));
		Utils.setListViewHeightBasedOnChildren(weatherForecastList);
		return;
	}

	private void randomSet(LineView lineView) {
		ArrayList<Integer> List = new ArrayList<Integer>();
		int a = 0;
		int b = number2;
		for (int i = 0; i < 4; i++) {
			a = tm_array[i];
			List.add(a);
		}

		ArrayList<ArrayList<Integer>> dataLists = new ArrayList<ArrayList<Integer>>();
		dataLists.add(List);

		lineView.setDataList(dataLists);
	}

	private void setCurrentWeatherLayoutHight() {
		int statusBarHeight = 0;
		try {
			statusBarHeight = getResources().getDimensionPixelSize(
					Integer.parseInt(Class
							.forName("com.android.internal.R$dimen")
							.getField("status_bar_height")
							.get(Class.forName("com.android.internal.R$dimen")
									.newInstance()).toString()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		int displayHeight = ((WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
				.getHeight();
		titleBarLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		int titleBarHeight = titleBarLayout.getMeasuredHeight();

		LayoutParams linearParams = (LayoutParams) currentWeatherLayout
				.getLayoutParams();
		linearParams.height = displayHeight - statusBarHeight - titleBarHeight;
		currentWeatherLayout.setLayoutParams(linearParams);
	}

	private void th_mdialog() {

		Thread mThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					Thread.sleep(1500);
					Message message = new Message();
					message.what = 3;
					handler.sendMessage(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		});
		mThread.start();
	}

	private void img() {

		Thread mThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					Thread.sleep(2000);
					Message message = new Message();
					message.what = 4;
					handler.sendMessage(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		});
		mThread.start();
	}

	private void img2() {

		Thread mThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					Thread.sleep(10000);
					Message message = new Message();
					message.what = 5;
					handler.sendMessage(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		});
		mThread.start();
	}

	private void updateWeather() {
		refreshing(true);

		arc.removeAllViewsInLayout();

		handler.postDelayed(run, 60 * 1000);
		EngineManager.getInstance().getWebPageMannger()
				.getWebPage(UpdateWeather.class).setHtmlValue("city", city);
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				EngineManager.getInstance().getWebPageMannger()
						.updateWebPage(UpdateWeather.class, true);
			}
		});
		thread.start();
	}

	private void saveData() {
		String updateTime = new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date());
		Time time = new Time();
		time.setToNow();
		String hour, minute;
		hour = time.hour + "";
		minute = time.minute + "";
		if (hour.length() < 2) {
			hour = "0" + hour;
		}
		if (minute.length() < 2) {
			minute = "0" + minute;
		}
		updateTime = updateTime + " " + hour + ":" + minute;
		Editor editor = sp.edit();
		editor.putString("update_time", updateTime);
		for (int i = 0; i < 4; i++) {
			editor.putString(DATE_KEY[i], dateArray[i]);
			editor.putString(WEATHER_KEY[i], weatherArray[i]);
			editor.putString(WIND_KEY[i], windArray[i]);
			editor.putString(TEMPERATURE_KEY[i], temperatureArray[i]);
		}
		editor.putString("city", city);
		editor.putString("pm25", pm_25);

		editor.putString("current_temperature", currentTemperature);
		editor.commit();
	}

	private void refreshing(boolean isRefreshing) {
		if (isRefreshing) {

			arc.removeAllViewsInLayout();
			arc.refreshDrawableState();
			refresh.setVisibility(View.GONE);
			refreshing.setVisibility(View.VISIBLE);
		} else {
			refresh.setVisibility(View.VISIBLE);
			refreshing.setVisibility(View.GONE);
		}
	}

	class MyAdapter extends BaseAdapter {

		private Context mContext;

		private MyAdapter(Context mContext) {
			this.mContext = mContext;
		}

		@Override
		public int getCount() {
			return getData().size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.weather_forecast_item, null);
				holder = new ViewHolder();
				holder.date = (TextView) convertView
						.findViewById(R.id.weather_forecast_date);
				holder.img = (ImageView) convertView
						.findViewById(R.id.weather_forecast_img);
				holder.temperature = (TextView) convertView
						.findViewById(R.id.weather_forecast_temperature);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Typeface face = Typeface.createFromAsset(getAssets(),
					"fonts/fangzhenglantingxianhe_GBK.ttf");
			holder.date.setText(getData().get(position).get("date").toString());
			holder.img.setImageResource((Integer) getData().get(position).get(
					"img"));
			holder.temperature.setText(getData().get(position)
					.get("temperature").toString());
			holder.temperature.setTypeface(face);
			img2();
			return convertView;
		}

	}

	class ViewHolder {
		TextView date;
		ImageView img;
		TextView weather;
		TextView temperature;
		TextView wind;
	}

	private ArrayList<HashMap<String, Object>> getData() {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 4; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			if (dateArray[i].equals(currentWeekDay)) {
				map.put("date", "今天 ");
			} else {
				map.put("date", dateArray[i]);
			}
			map.put("img", getWeatherImg(weatherArray[i] + " "));
			map.put("temperature", temperatureArray[i]);
			list.add(map);
		}
		return list;
	}

	private int getWeatherImg(String weather) {
		int img = 0;
		if (weather.contains("转")) {
			weather = weather.substring(0, weather.indexOf("转"));
		}
		if (weather.contains("晴")) {
			img = R.drawable.weather_img_fine_day;
		} else if (weather.contains("多云")) {
			img = R.drawable.weather_img_cloudy_day;
		} else if (weather.contains("阴")) {
			img = R.drawable.weather_img_overcast;
		} else if (weather.contains("雷")) {
			img = R.drawable.weather_img_thunder_storm;
		} else if (weather.contains("小雨")) {
			img = R.drawable.weather_img_rain_small;
		} else if (weather.contains("中雨")) {
			img = R.drawable.weather_img_rain_middle;
		} else if (weather.contains("大雨")) {
			img = R.drawable.weather_img_rain_big;
		} else if (weather.contains("暴雨")) {
			img = R.drawable.weather_img_rain_storm;
		} else if (weather.contains("雨夹雪")) {
			img = R.drawable.weather_img_rain_snow;
		} else if (weather.contains("冻雨")) {
			img = R.drawable.weather_img_sleet;
		} else if (weather.contains("小雪")) {
			img = R.drawable.weather_img_snow_small;
		} else if (weather.contains("中雪")) {
			img = R.drawable.weather_img_snow_middle;
		} else if (weather.contains("大雪")) {
			img = R.drawable.weather_img_snow_big;
		} else if (weather.contains("暴雪")) {
			img = R.drawable.weather_img_snow_storm;
		} else if (weather.contains("冰雹")) {
			img = R.drawable.weather_img_hail;
		} else if (weather.contains("雾") || weather.contains("霾")) {
			img = R.drawable.weather_img_fog;
		} else if (weather.contains("沙尘暴") || weather.contains("浮尘")
				|| weather.contains("扬沙")) {
			img = R.drawable.weather_img_sand_storm;
		} else {
			img = R.drawable.weather_img_fine_day;
		}
		return img;
	}

	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.change_city_layout:
				intent = new Intent();
				intent.setClass(Weather.this, SelectCity.class);
				Weather.this.startActivityForResult(intent, 100);

				break;
			case R.id.refresh:
				if (Utils.checkNetwork(Weather.this) == false) {
					Toast.makeText(Weather.this, "网络异常,请检查网络设置",
							Toast.LENGTH_SHORT).show();
					return;
				}
				updateWeather();
				break;
			default:
				break;
			}
		}

	}

	public String getNumbers(String content) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}

		return "";

	}

	public int sent(int a) {

		int c = a;
		System.out.println("hhhhhhhhhh" + c);
		return c;

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click();
		}
		return false;
	}

	public void m_dialog() {

		final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder
				.getInstance(this);
		effect = Effectstype.Newspager;
		dialogBuilder.withTitleColor("#FFE74C3C").withDividerColor("#11000000")
				.withMessage(pm_warning).withMessageColor("#FFFFFFFF") // def |
																		// withMessageColor(int
				.withDialogColor("#FFE74C3C") // def | withDialogColor(int
				.withDuration(700) // def
				.withEffect(effect) // def Effectstype.Slidetop
				.withButton1Text("嗯，知道了") // def gone
				.withButton2Text("去商城看看") // def gone
				.isCancelableOnTouchOutside(true) // def | isCancelable(true)
				.setButton1Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						dialogBuilder.dismiss();
					}
				}).setButton2Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(Weather.this, WebStore.class);
						startActivity(intent);
					}
				}).show();

	}

	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer timer = null;
		if (isExit == false) {

			vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			long[] pattern = { 100, 400 };
			vibrator.vibrate(pattern, 1);

			isExit = true;

			final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder
					.getInstance(this);
			effect = Effectstype.Shake;
			dialogBuilder.withTitleColor("#FFFFFF") // def
					.withDividerColor("#11000000") // def
					.withMessage("    亲，您是要退出吗 ？ ") // .withMessage(null) no Msg
					.withMessageColor("#FFFFFFFF") // def | withMessageColor(int
					// resid)
					.withDialogColor("#FFE74C3C") // def | withDialogColor(int
					// resid)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.withButton1Text("是的") // def gone
					.withButton2Text("取消") // def gone
					.isCancelableOnTouchOutside(true) // def |

					.setButton1Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {

							finish();
							System.exit(0);
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {

							dialogBuilder.dismiss();
						}
					}).show();

			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;
				}
			}, 2000);
		} else {
		}
	}

	private void init() {
		JPushInterface.init(getApplicationContext());
	}

	@Override
	protected void onResume() {
		isForeground = true;
		super.onResume();
	}

	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public void name() {
		if (operatingAnim != null) {
			infoOperatingIV.startAnimation(operatingAnim);
			infoOperatingIV2.startAnimation(operatingAnim2);
		}
	}

	public void name2() {
		if (operatingAnim2 != null) {
			infoOperatingIV2.startAnimation(operatingAnim2);
		}
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				String messge = intent.getStringExtra(KEY_MESSAGE);
				String extras = intent.getStringExtra(KEY_EXTRAS);
				StringBuilder showMsg = new StringBuilder();
				showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
				if (!ExampleUtil.isEmpty(extras)) {
					showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
				}
				setCostomMsg(showMsg.toString());
			}
		}
	}

	private void setCostomMsg(String msg) {
		if (null != msgText) {
			msgText.setText(msg);
			msgText.setVisibility(android.view.View.VISIBLE);
		}
	}

}
