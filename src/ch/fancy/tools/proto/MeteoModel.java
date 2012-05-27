package ch.fancy.tools.proto;

/**
 * pojo mit den gefundenen werten. 
 * @author caliban
 *
 */
public class MeteoModel {
	private static final String defaultSym = "28.gif";
	private static final String defaultDate = "Mo 01.01";
	private static final String defaultTemp = "0 | 0 °C";

	private String[] dates = new String[] { defaultDate, defaultDate, defaultDate,
			defaultDate, defaultDate, defaultDate };
	private String[] symboles = new String[]  { defaultSym, defaultSym, defaultSym,
			defaultSym, defaultSym, defaultSym };
	private String[] temps = new String[]{ defaultTemp, defaultTemp, defaultTemp,
			defaultTemp, defaultTemp, defaultTemp };
	private String location = "UNKNOWN";

	private String zip = "9999";

	public MeteoModel()
	{
		//default zeugs
	}

	public MeteoModel(String[] gifs, String[] days, String[] temps) {
		assert gifs.length == 6; 
		assert days.length == 6; 
		assert temps.length == 6; 
		this.symboles = gifs; 
		this.dates = days; 
		this.temps = temps; 
	}

	public String[] getDates()
	{
		return dates; 
	}
	public String[] getSyms()
	{
		return symboles;
	}
	public String[] getTemps()
	{
		return temps; 
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}


	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}


}
