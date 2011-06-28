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
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class FetchData {

	private static final Pattern pattern = Pattern.compile("\\d{1,2}\\.gif");
	private static final String URL = "http://www.meteoschweiz.admin.ch/web/de/wetter/detailprognose/lokalprognose.html?language=de&plz={0}&x=0&y=0";

	public MeteoModel fetch(Context context) {
		// <img class="symbol" title="teilweise sonnig" alt="teilweise sonnig"
		// src="/images/weathersymbols/3.gif">
		// /html/body/div[9]/div/table[2]/tbody/tr[2]/td[6]/img

		// String expression = "/html/body/div[9]/div/table[2]/tbody/tr/td";
		// String expression = "blubb";
		// XPath xpath = XPathFactory.newInstance().newXPath();
		try {
			String[] temp = new String[5];

			String html = convertStreamToString(getData(context));
			// NodeList nodes = (NodeList) xpath.evaluate(expression, new
			// InputSource(getData()), XPathConstants.NODESET);
			html = html.replace("/images/0.gif", "");
			Matcher matcher = pattern.matcher(html);
			for (int i = 0; i < 5; i++) {
				matcher.find();
				String match = matcher.group();
				// Node node = nodes.item(i).getFirstChild();
				// String text = node.getTextContent();
				Log.d("fetchdata", "||||" + match);
				temp[i] = match;

			}
			return new MeteoModel(temp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("error?", e.getMessage());
			e.printStackTrace();
			throw new IllegalStateException(e);
		}

	}

	private InputStream getData(Context context) throws IOException {
		HttpClient client = new DefaultHttpClient();
		String tempUrl = MessageFormat.format(URL, findPlz(context));
		HttpGet first = new HttpGet(tempUrl);

		try {
			HttpResponse response = client.execute(first);
			return response.getEntity().getContent();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("error?", e.getMessage());
			e.printStackTrace();
			throw new IllegalStateException(e);
		}
	}

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
	
	public String findPlz(Context context) throws IOException
	{
		Geocoder coder = new Geocoder(context);
		LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE); 
		Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if(location == null)
		{
			//bah, didn't work 
			return "3000";
		}
		double longitude = location.getLongitude();
		double latitude = location.getLatitude();
		List<Address> list = coder.getFromLocation(latitude, longitude, 1);
		Log.d("meteowidget", "nearest plz is: "+list.get(0).getPostalCode());
		return list.get(0).getPostalCode();
	}
}
