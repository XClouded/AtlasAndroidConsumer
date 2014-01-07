/**
 * 
 */
package atlasapp.common;

import java.util.Date;

/**
 * @author nghia
 * 
 */
public class ATLHumanizedTime extends Date {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	// makesure you use GMT date
	public static String toStringWithHumanizedTimeDiffrence(Date date) {
		if (date == null) {
			return "";
		}
		long oldTime = date.getTime() / 1000;
		long currentTime = DateTimeUtilities.toGMTDate(new Date()).getTime() / 1000;
		long delta = currentTime - oldTime;
		String positivity = delta > 0 ? "ago" : "later";
		if (delta < 0) {
			delta *= -1;
		}

		int secondsInADay = 3600 * 24;
		int secondsInAYear = 3600 * 24 * 365;
		int yearsDiff = (int) delta / secondsInAYear;
		int daysDiff = (int) delta / secondsInADay;
		int hoursDiff = (int) ((delta - (daysDiff * secondsInADay)) / 3600);
		int minutesDiff = (int) ((delta - ((daysDiff * secondsInADay) + (hoursDiff * 60))) / 60);
		int secondsDiff = (int) (delta - ((daysDiff * secondsInADay)
				+ (hoursDiff * 3600) + (minutesDiff * 60)));

		if (yearsDiff > 1)
			return yearsDiff + " " + "years" + " " + positivity;
		else if (yearsDiff == 1)
			return "a year" + " " + positivity;

		if (daysDiff > 0) {
			if (hoursDiff == 0)
				return (daysDiff == 1 ? "a day" : daysDiff + " " + "days")
						+ " " + positivity;
			else
				return (daysDiff == 1 ? "a day" : daysDiff + " " + "days")
						+ " " + positivity;
		} else {
			if (hoursDiff == 0) {
				if (minutesDiff == 0)
					return (secondsDiff == 1 ? "a second" : secondsDiff + " "
							+ "seconds")
							+ " " + positivity;
				else
					return (minutesDiff == 1 ? "a minute" : minutesDiff + " "
							+ "minutes")
							+ " " + positivity;
			} else {
				if (hoursDiff == 1)
					return "About a hour" + " " + positivity;
				else
					return hoursDiff + " " + "hours" + " " + positivity;
			}
		}

	}
}
