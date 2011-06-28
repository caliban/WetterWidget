package ch.fancy.tools.proto;

public class MeteoModel 
{
	private String day1 = "28.gif";
	private String day2  = "28.gif";
	private String day3  = "28.gif"; 
	private String day4  = "28.gif"; 
	private String day5 = "28.gif";
	
	public MeteoModel(String[] args)
	{
		day1 = args[0];
		day2 = args[1];
		day3 = args[2];
		day4 = args[3];
		day5 = args[4];
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
	
	

}
