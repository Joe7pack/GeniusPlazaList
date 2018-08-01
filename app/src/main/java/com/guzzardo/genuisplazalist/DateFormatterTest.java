package com.guzzardo.genuisplazalist;

import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DateFormatterTest {

    public static void main(String s[]) {

        try {
            String string = "1988-06-02";
            //DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            //DateFormat format = new SimpleDateFormat("MMM d, yyyy");
            //DateFormat format = new SimpleDateFormat("EEE, MMM d, yyyy");
            //Date date = format.parse(string);
            //System.out.println(date); // Sat Jan 02 00:00:00 GMT 2010

            //LocalDateTime ldt = LocalDateTime.now().plusDays(1);
            //DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
            //System.out.println(ldt);
            // Output "2018-05-12T17:21:53.658"
            //String formatter = formmat1.format(ldt);
            //System.out.println(formatter);

            //String date = "2016-08-16";
            //default, ISO_LOCAL_DATE
            //LocalDate localDate = LocalDate.parse(date);
            //System.out.println("local date: "+ localDate);

            /*

            String date = "2011-12-03";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM uuuu");
            LocalDate localDate = LocalDate.parse(date, formatter);
            System.out.println(localDate);
            System.out.println(formatter.format(localDate));
            */

            /* This code requires an Android device that runs at API level 26 or higher
            String strDate = "2015-02-01";
            LocalDate aLD = LocalDate.parse(strDate);
            DateTimeFormatter dTF = DateTimeFormatter.ofPattern("MMMM d, uuuu");
            System.out.println(aLD + " formats as " + dTF.format(aLD));
            */


            DateFormat formatter1;
            formatter1 = new SimpleDateFormat("MM/DD/yyyy");
            System.out.println((Date)formatter1.parse("08/16/2011"));

            DateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
            Date date = fmt.parse("2001-07-04");
            //ParsePosition pp = new ParsePosition(0);
            System.out.println("formatter 2: " +fmt.format(date));


            SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd");
            Date date2 = fmt2.parse("2001-07-04");

            SimpleDateFormat fmtOut = new SimpleDateFormat("MMMM d, yyyy");
            System.out.println("formatter 3: " +fmtOut.format(date2));

            System.out.println("formatter 3: " +fmtOut.format(date2));

            System.out.println("formatter 3: " +fmtOut.format(date2));



// 2018-05-12
        } catch (Exception e) {
            System.out.println("error: " +e);
        }

    }

}

