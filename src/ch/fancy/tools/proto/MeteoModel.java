package ch.fancy.tools.proto;

public class MeteoModel 
{
	private String day1 = "28.gif";
	private String day2  = "28.gif";
	private String day3  = "28.gif"; 
	private String day4  = "28.gif"; 
	private String day5 = "28.gif";
	
	private String dateDay1 = "Mo 01.01";
	private String dateDay2  = "Di 02.01";
	private String dateDay3  = "Mi 03.01"; 
	private String dateDay4  = "Do 04.01"; 
	private String dateDay5 = "Fr 05.01";
	
	private String location = "UNKNOWN"; 
	
	private String zip = "9999"; 
	
	public MeteoModel(String[] gifs, String[] days )
	{
		day1 = gifs[0];
		day2 = gifs[1];
		day3 = gifs[2];
		day4 = gifs[3];
		day5 = gifs[4];
		dateDay1 = days[0];
		dateDay2 = days[1];
		dateDay3 = days[2];
		dateDay4 = days[3];
		dateDay5 = days[4];
	}
	public MeteoModel()
	{
		//default show storm :)
	}
	public String getDay1() {
		return day1;
	}
	public void setDay1(String day1) {
		this.day1 = day1;
	}
	public String getDay2() {
		return day2;
	}
	public void setDay2(String day2) {
		this.day2 = day2;
	}
	public String getDay3() {
		return day3;
	}
	public void setDay3(String day3) {
		this.day3 = day3;
	}
	public String getDay4() {
		return day4;
	}
	public void setDay4(String day4) {
		this.day4 = day4;
	}
	public String getDay5() {
		return day5;
	}
	public void setDay5(String day5) {
		this.day5 = day5;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getDateDay1() {
		return dateDay1;
	}
	public void setDateDay1(String dateDay1) {
		this.dateDay1 = dateDay1;
	}
	public String getDateDay2() {
		return dateDay2;
	}
	public void setDateDay2(String dateDay2) {
		this.dateDay2 = dateDay2;
	}
	public String getDateDay3() {
		return dateDay3;
	}
	public void setDateDay3(String dateDay3) {
		this.dateDay3 = dateDay3;
	}
	public String getDateDay4() {
		return dateDay4;
	}
	public void setDateDay4(String dateDay4) {
		this.dateDay4 = dateDay4;
	}
	public String getDateDay5() {
		return dateDay5;
	}
	public void setDateDay5(String dateDay5) {
		this.dateDay5 = dateDay5;
	} 
	
	
	
	
	

}
