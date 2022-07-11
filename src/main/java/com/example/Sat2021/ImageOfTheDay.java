package com.example.Sat2021;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Scanner;

@RestController
public class ImageOfTheDay {

    public static String getYear() {
        Scanner in = new Scanner(System.in);
        String month = in.nextLine();
        return month;
    }

    public static String getMonth() {
        Scanner in = new Scanner(System.in);
        String month = in.nextLine();
        return month;
    }

    public static String getDay() {
        Scanner in = new Scanner(System.in);
        String day = in.nextLine();
        return day;
    }

    @RequestMapping("/image")
    public static String findImageOfTheDay(String year, String month, String day) throws IOException {

//        String year = getYear();
//        String month = getMonth();
//        String day = getDay();

        String url = "https://api.nasa.gov/planetary/apod?api_key=uGoJdNtkFJktcbymjfJAtg0UlgfORjg1d6qDVaaB&date=" + year + "-" + month + "-" + day;
        HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(url).openConnection();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(urlConnection.getInputStream());



        LocalDate now = LocalDate.now();


        int currentyear = now.getYear();
        String currentYear = String.valueOf(currentyear);

        int currentmonth = now.getMonthValue();
        String currentMonth = String.valueOf(currentmonth);

        int currentday = now.getDayOfMonth();
        String currentDay = String.valueOf(currentday);

        String gurll  = rootNode.at("/url").toString();
        String urll = gurll.substring(1, (gurll.length() - 1));
        return urll;



//        return
//                "<html>\n" + "<header><title> Satellites | Images </title>" + "\n" + "<link rel=\"shortcut icon\" type=\"image/png\" href=\"https://clipart.info/images/ccovers/1559839401black-star-png-10.png\">" + "  <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\" integrity=\"sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T\" crossorigin=\"anonymous\">\n" + "\n" + "    <!-- metadata -->\n" + "    <meta charset=\"utf-8\"/>\n"  + "</header>\n" +
//                        "<body\n" + "<nav class='navbar navbar-expand-lg navbar-dark bg-info'>" + "<a class='navbar-brand' href='/'>" + "<img src='https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRL0GHfo_JPhu5qbZbhq9jHtjtWSrOtH5lHjw&usqp=CAU' height='30px' width='30px'>" + "</a>" + "<a class='navbar-brand' href='/mainpage.html'>" + "Satellites!" + "</a>\n" + "<div>\n" +  "<button>\n" +   "<a href='/image'>" +" Image Of The Day" + "</a>\n" + "</button>\n"  + "</div>\n" + "<div>\n" +  "<button>\n" +   "<a href='/satellite'>" +" Satellites" + "</a>\n" + "</button>\n"  + "</div>\n" + "<div>\n" +  "<button>\n" +   "<a href='/background.html'>" +" Background Info" + "</a>\n" + "</button>\n"  + "</div>\n" +
//                        "</nav>\n"
//                        + "<h2 class='container'>\n" + "Image of the Day!" + "</h2>\n" +
//                        "<div class='container'>\n" +
//                        "<img src=" + gurll + " height='500px' width='500px'>\n" +
//                        "</div>\n" +
//                        "<h6>\n" + "Image Url: " + "<a href=" + gurll + ">\n " + gurll + "</a>\n"  + "</h6>\n"   + "<div>\n" + "<div class='col-lg-4 col-md-6 col-sm-6'>\n" + "<div class='rounded'>\n"  + "<img src=" + ImageOfTheDay.today() + " height='200px' width='100%'>\n" + "<p>\n" + "Today's Satellite Image: " + currentDate +  "</p>\n" + "</div>\n"  + "</div>\n" +                         "<div class='col-lg-4 col-md-6 col-sm-6'>\n" + "<div class='rounded'>\n"  + "<img src=" + ImageOfTheDay.yesterday() + " height='200px' width='100%'>\n" + "<p>\n" + "Yesterday's Satellite Image: " + yesterDayDate +  "</p>\n" + "</div>\n"  + "</div>\n" +                 "<div class='col-lg-4 col-md-6 col-sm-6'>\n" + "<div class='rounded'>\n"  + "<img src=" + ImageOfTheDay.dayBeforeYesterday() + " height='200px' width='100%'>\n" + "<p>\n" + "Day before Yesterday's Satellite Image: " + dayBeforeYesterDayDate +  "</p>\n" + "</div>\n"  + "</div>\n" +               "</div>\n" + "</body>\n" + "</html>";


    }



    public static String today() throws IOException {

        LocalDate noww = LocalDate.now();
        int currentyear = noww.getYear();
        String currentYear = String.valueOf(currentyear);

        int currentmonth = noww.getMonthValue();
        String currentMonth = String.valueOf(currentmonth);

        int currentday = noww.getDayOfMonth();
        String currentDay = String.valueOf(currentday);


        String imageUrlToday = "https://api.nasa.gov/planetary/apod?api_key=uGoJdNtkFJktcbymjfJAtg0UlgfORjg1d6qDVaaB&date=" + currentYear + "-" + currentMonth + "-" + currentDay;
        HttpsURLConnection urlConnectionn = (HttpsURLConnection) new URL(imageUrlToday).openConnection();
        ObjectMapper objectMappers = new ObjectMapper();
        JsonNode rootNodee = objectMappers.readTree(urlConnectionn.getInputStream());

        String urlToday  = rootNodee.at("/url").toString();
        String urll = urlToday.substring(1, (urlToday.length() - 1));
        return urll;


    }

    public static String yesterday() throws IOException {

        LocalDate noww = LocalDate.now();
        int currentyear = noww.getYear();
        String currentYear = String.valueOf(currentyear);

        int currentmonth = noww.getMonthValue();
        String currentMonth = String.valueOf(currentmonth);

        int currentday = noww.getDayOfMonth();
        String currentDay = String.valueOf(currentday);

        int yesterday = currentday - 1;
        String yesterDay = String.valueOf(yesterday);


        String imageUrlToday = "https://api.nasa.gov/planetary/apod?api_key=uGoJdNtkFJktcbymjfJAtg0UlgfORjg1d6qDVaaB&date=" + currentYear + "-" + currentMonth + "-" + yesterDay;
        HttpsURLConnection urlConnectionn = (HttpsURLConnection) new URL(imageUrlToday).openConnection();
        ObjectMapper objectMappers = new ObjectMapper();
        JsonNode rootNodee = objectMappers.readTree(urlConnectionn.getInputStream());

        String urlYesterday=  rootNodee.at("/url").toString();
        String urll = urlYesterday.substring(1, (urlYesterday.length() - 1));




        return urll;


    }


    public static String dayBeforeYesterday() throws IOException {

        LocalDate noww = LocalDate.now();
        int currentyear = noww.getYear();
        String currentYear = String.valueOf(currentyear);

        int currentmonth = noww.getMonthValue();
        String currentMonth = String.valueOf(currentmonth);

        int currentday = noww.getDayOfMonth();
        String currentDay = String.valueOf(currentday);

        int yesterday = currentday - 1;
        String yesterDay = String.valueOf(yesterday);

        int daybefyesterday = currentday - 2;
        String dayBeforeYesterDay = String.valueOf(daybefyesterday);



        String imageUrlToday = "https://api.nasa.gov/planetary/apod?api_key=uGoJdNtkFJktcbymjfJAtg0UlgfORjg1d6qDVaaB&date=" + currentYear + "-" + currentMonth + "-" + dayBeforeYesterDay;
        HttpsURLConnection urlConnectionn = (HttpsURLConnection) new URL(imageUrlToday).openConnection();
        ObjectMapper objectMappers = new ObjectMapper();
        JsonNode rootNodee = objectMappers.readTree(urlConnectionn.getInputStream());

        String urlDayBeforeYesterday=  rootNodee.at("/url").toString();
        return urlDayBeforeYesterday;


    }




}
