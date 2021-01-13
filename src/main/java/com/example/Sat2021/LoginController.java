package com.example.Sat2021;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginForm(){

return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@ModelAttribute(name="loginForm") com.example.Sat2021.LoginForm loginForm, Model model) throws IOException {

String year = loginForm.getYear();
String month = loginForm.getMonth();
String day = loginForm.getDay();

String image = com.example.Sat2021.ImageOfTheDay.findImageOfTheDay(year, month, day);
String imageToday = com.example.Sat2021.ImageOfTheDay.today();
String imageYesterday = com.example.Sat2021.ImageOfTheDay.yesterday();
String imageDayBefore = com.example.Sat2021.ImageOfTheDay.dayBeforeYesterday();


model.addAttribute("year", year);
model.addAttribute("month", month);
model.addAttribute("day", day);

model.addAttribute("image", image);
model.addAttribute("imageToday", imageToday);
model.addAttribute("imageYesterday", imageYesterday);
model.addAttribute("imageDayBefore", imageDayBefore);

return "imagehome";
    }




}
