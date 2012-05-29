package ch.fancy.tools.proto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class FetchData implements LocationListener {

	// pattern für die gifs, (1-28).gif
	private static final Pattern gifpattern = Pattern.compile("\\d{1,2}\\.gif");
	private static final Pattern dayAndDatePattern = Pattern
			.compile("\\w\\w\\s\\d\\d\\.\\d\\d");
	private static final Pattern tempPattern = Pattern
			.compile("\\d{1,2} \\| \\d{1,2} &deg;C");

	// url für die daten abzuholen. TODO lokalisieren des de zu fr/it sofern
	// textdaten übernommen werden.
	private static final String URL = "http://www.meteoschweiz.admin.ch/web/de/wetter/detailprognose/lokalprognose.html?language=de&plz={0}&x=0&y=0";

	private double latitude;

	private double longitude;

	public FetchData(Context context) {
		// register this fetchdata as a location listener
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		String provider = locationManager.getBestProvider(criteria, true);

		if (provider == null) {
			Log.e("meteowidget", "No location provider found!");
			return;
		}
		// update jede stunde, sofernt die position um mehr als 2000 meter
		// verschoben ist
		locationManager.requestLocationUpdates(provider, 3600000,
				2000, this);
	}

	public MeteoModel fetch(Context context) {
		// <img class="symbol" title="teilweise sonnig" alt="teilweise sonnig"
		// src="/images/weathersymbols/3.gif">
		// /html/body/div[9]/div/table[2]/tbody/tr[2]/td[6]/img

		// String expression = "/html/body/div[9]/div/table[2]/tbody/tr/td";
		// String expression = "blubb";
		// XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			String[] gifs = new String[6];
			String[] days = new String[6];
			String[] temps = new String[6];
			Address address = findPlz(context);
			String html = convertStreamToString(getData(context,
					address.getPostalCode()));
			// NodeList nodes = (NodeList) xpath.evaluate(expression, new
			// InputSource(getData()), XPathConstants.NODESET);
			// there is a 0.gif lurking around, remove it.
			html = html.replace("/images/0.gif", "");
			Matcher matcher = gifpattern.matcher(html);
			for (int i = 0; i < 6; i++) {
				matcher.find();
				String match = matcher.group();
				// Node node = nodes.item(i).getFirstChild();
				// String text = node.getTextContent();
				Log.d("########################### fetchdata gifs ", "||||"
						+ match);
				gifs[i] = match;

			}
			matcher = dayAndDatePattern.matcher(html);
			for (int i = 0; i < 6; i++) {
				matcher.find();
				String match = matcher.group();
				// Node node = nodes.item(i).getFirstChild();
				// String text = node.getTextContent();
				Log.d("########################### fetchdata days", "||||"
						+ match);
				days[i] = match;

			}
			matcher = tempPattern.matcher(html);
			for (int i = 0; i < 6; i++) {
				matcher.find();
				String match = matcher.group();
				// Node node = nodes.item(i).getFirstChild();
				// String text = node.getTextContent();
				Log.d("########################### fetchdata temps", "||||"
						+ match);
				temps[i] = match.replace("&deg;", "°");

			}

			MeteoModel model = new MeteoModel(gifs, days, temps);
			model.setLocation(address.getLocality());
			model.setZip(address.getPostalCode());
			return model;
		} catch (IOException e) {
			Log.e("error, IN MY APP?", e.getMessage());
			e.printStackTrace();
			throw new IllegalStateException(e);
		}

	}

	/**
	 * fetch the data from meteo. apache httpclient used.
	 */
	private InputStream getData(Context context, String zip) throws IOException {
		HttpClient client = new DefaultHttpClient();
		String tempUrl = MessageFormat.format(URL, zip);
		HttpGet first = new HttpGet(tempUrl);

		try {
			HttpResponse response = client.execute(first);
			return response.getEntity().getContent();

		} catch (Exception e) {
			Log.e("error?", e.getMessage());
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}

	/**
	 * converts a stream to a string.
	 */
	public String convertStreamToString(InputStream is) throws IOException {

		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				if (is != null) {
					is.close();
				}
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	/**
	 * attempt to get a position and get the nearest address
	 */
	public Address findPlz(Context context) throws IOException {
		Address defaultAdress = new Address(null);
		defaultAdress.setPostalCode("3000");
		defaultAdress.setLocality("Bern");
		Geocoder coder = new Geocoder(context);
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		String provider = locationManager.getBestProvider(criteria, true);

		if (provider == null) {
			Log.e("meteowidget", "No location provider found!");
			return defaultAdress;
		}
		// update jede stunde, sofernt die position um mehr als 2000 meter
		// verschoben ist
		Location lastLocation = locationManager.getLastKnownLocation(provider);
		if (lastLocation != null) {
			// current location is null, fallback to stored location.
			longitude = lastLocation.getLongitude();
			latitude = lastLocation.getLatitude();
		}
		//suspect to ioexception... TODO investigate
		List<Address> list = coder.getFromLocation(latitude, longitude, 1);
		if (!list.isEmpty()) {
			Log.d("meteowidget", "########################### nearest plz is: "
					+ list.get(0).getPostalCode());
			return list.get(0);
		} else {
			Log.d("meteowidget", "adress list is empty");
			return defaultAdress;
		}

	}

	/**
	 * notify with a new location
	 */
	public void onLocationChanged(Location arg0) {
		// update the current position.
		Log.d("meteowidget", "########################### got update long: "
				+ arg0.getLongitude() + " lat: " + arg0.getLatitude());
		this.latitude = arg0.getLatitude();
		this.longitude = arg0.getLongitude();

	}

	public void onProviderDisabled(String provider) {
		// bö?

	}

	public void onProviderEnabled(String provider) {
		// bö?
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// bö?
	}
}
