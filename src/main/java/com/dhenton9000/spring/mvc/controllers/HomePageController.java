package com.dhenton9000.spring.mvc.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * The controller for the homepage
 *
 * @author Don
 *
 */
@Controller
public class HomePageController {

    private static Logger log = LoggerFactory.getLogger(HomePageController.class);
    @Autowired
    private Environment env;

    @RequestMapping("/home")
    public ModelAndView homePage() {
        String url = "/app/getauth.html";
        return new ModelAndView("tiles.home", "url", url);
    }

    /**
     * compose the referral to okta
     *
     * @return
     */
    @RequestMapping("/getauth")
    public String getAuth() {
        String clientId = env.getProperty("client-id");
        log.debug("clientId "+clientId);
        //note that env can be overriden by command line environment assignments
        //and is part of the spring boot system of parameter use
        
        String clientSecret = env.getProperty("client-secret");
        String issuer = env.getProperty("issuer");
        String authorizationUri = issuer + "/oauth2/v1/authorize";
        String tokenUri = issuer + "/oauth2/v1/token";
        String jwkUri = issuer + "/oauth2/v1/keys";
        String userInfoUri = issuer + "/oauth2/v1/userinfo";
        String nonce = UUID.randomUUID().toString();
        String state = UUID.randomUUID().toString();
        String redirectUri = env.getProperty("redirect-uri");
        redirectUri = "http://local.awsdhenton.com:9000/app/process.html";
        redirectUri = redirectUri.trim();
        try {
            redirectUri = URLEncoder.encode(redirectUri, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
           log.error("cannot peform url encode ");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("redirect:");
        sb.append(authorizationUri);

        sb.append("?");
        sb.append("client_id=");
        sb.append(clientId);
        sb.append("&response_type=code&scope=openid");
        
        //sb.append("openid offline groups email profile");
        sb.append("&redirect_uri=");
        sb.append(redirectUri);
        sb.append("&state=");
        sb.append(state);
        sb.append("&nonce");
        sb.append(nonce);

        return sb.toString();
    }
    
    
    @RequestMapping("/process")
    public String doProcess(Model model, @RequestParam String code, @RequestParam  String state) {
        
        model.addAttribute("code",code);
        model.addAttribute("state",state);
        return "tiles.process";
    }

}
