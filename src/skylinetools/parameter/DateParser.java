package skylinetools.parameter;

import beast.core.Input;
import beast.core.parameter.RealParameter;
import beast.core.util.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


/**
 * Class for converting formatted date strings to decimal year dates, e.g. 01-Jan-2015 = 2015.0
 *
 * Can be used as input to TreeDateSlicer or any other class that needs a decimal date.
 * Heavily borrows from TimeParameter.java in feast by Tim Vaughan
 *
 * See https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html for the date format string.
 *
 * Note that the date strings are only converted on initialisation. Any operations on the values after that will leave
 * the input date strings unaffected.
 *
 * @author Louis du Plessis
 *         Date: 2018/12/28
 */
public class DateParser extends RealParameter {

    public Input<String> DateFormatInput = new Input<>("format","Date format (yyyy-MM-dd)");
    public Input<String> DatesInput = new Input<>("dates","Dates to be parsed");


    public DateParser()  {
        valuesInput.setRule(Input.Validate.OPTIONAL);
    }

    @Override
    public void initAndValidate() {

        for (String date : DatesInput.get().trim().split("\\s+")) {
            valuesInput.setValue(parseDate(date), this);
        }

        super.initAndValidate();
    }


    protected double getYearDate(LocalDate date) {
        return date.getYear() + (date.getDayOfYear() - 1.0) / (date.isLeapYear() ? 366.0 : 365.0);
    }

    /**
     *
     * (Partly copied from TimeParameter.java in feast by Tim Vaughan)
     *
     * @param dateString
     * @return
     */
    protected double parseDate(String dateString) {

        double yeardate;

        try {
            if (DateFormatInput.get() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateFormatInput.get());
                LocalDate date = LocalDate.parse(dateString, formatter);
                yeardate = getYearDate(date);

                Log.warning.println("Using format '" + DateFormatInput.get() + "' to parse '" + dateString +
                        "' as: " + yeardate);
            } else {
                yeardate = Double.parseDouble(dateString);
            }
        } catch (NumberFormatException | DateTimeParseException ex) {
            throw new IllegalArgumentException("Error parsing input dates - " +
                    "must be either a number or a properly formatted date string (" + dateString + " failed).");
        }

        return yeardate;
    }



}
