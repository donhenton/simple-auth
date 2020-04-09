package com.dhenton9000.spring.mvc.controllers;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

/**
 * The controller for the homepage
 *
 * @author Don
 *
 */
@Controller
@SessionAttributes("sessionholder")
public class HomePageController {

    private static Logger log = LoggerFactory.getLogger(HomePageController.class);
    @Autowired
    private Environment env;
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/home")
    public ModelAndView homePage() {
        String url = "/app/getauth.html";
        return new ModelAndView("tiles.home", "url", url);
    }

    /**
     * compose the referral to okta
     *
     * @param sessionState
     * @return
     */
    @RequestMapping("/getauth")
    public String getAuth(@ModelAttribute("sessionholder") SessionStateHolder sessionState) {
        String clientId = env.getProperty("client-id");
        log.debug("clientId " + clientId);
        //note that env can be overriden by command line environment assignments
        //and is part of the spring boot system of parameter use

        String clientSecret = env.getProperty("client-secret");
        String issuer = env.getProperty("issuer");
        String authorizationUri = issuer + "/oauth2/v1/authorize";
        String jwkUri = issuer + "/oauth2/v1/keys";
        String userInfoUri = issuer + "/oauth2/v1/userinfo";
        String nonce = UUID.randomUUID().toString();
        String state = UUID.randomUUID().toString();

        sessionState.setState(state);

        String redirectUri = env.getProperty("redirect-uri");
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
        sb.append("&response_type=code&scope=openid offline_access");

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
    public String doProcess(Model model,
            @RequestParam String code,
            @RequestParam String state,
            @ModelAttribute("sessionholder") SessionStateHolder sessionState) {

        model.addAttribute("code", code);
        model.addAttribute("state", state);

        /*
        
        
        curl --request POST \
  --url https://${yourOktaDomain}/oauth2/default/v1/token \
  --header 'accept: application/json' \
  --header 'authorization: Basic MG9hY...' \
  --header 'content-type: application/x-www-form-urlencoded' \
  --data 'grant_type=authorization_code&redirect_uri=http%3A%2F%2Flocalhost%3A8080&code=P59yPm1_X1gxtdEOEZjn'
        
        
        
        
         */
        String storedState = sessionState.getState();
        if (storedState != null && storedState.equals(state)) {
            sessionState.setState(null);
            String clientId = env.getProperty("client-id");
            String issuer = env.getProperty("issuer");
            String clientSecret = env.getProperty("client-secret");
            String originalInput = clientId + ":" + clientSecret;
            String authString = Base64.getEncoder().encodeToString(originalInput.getBytes());
            String tokenUri = issuer + "/oauth2/v1/token";
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            httpHeaders.setBasicAuth(authString);
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String redirectUri = env.getProperty("redirect-uri");
            redirectUri = redirectUri.trim();
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("grant_type", "authorization_code");
            map.add("redirect_uri", redirectUri);
            map.add("code", code);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, httpHeaders);

            ResponseEntity<Map> postResponse = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, Map.class);

            if (postResponse.getStatusCode().equals(HttpStatus.OK)) {
                Map result = postResponse.getBody();
                result.keySet().forEach(k -> {

                    log.debug("key " + k + " " + result.get(k));

                });

            } else {
                log.error("Errored with code " + postResponse.getStatusCode());
            }

            return "tiles.process";
        } else {
            throw new RuntimeException("state not found");
        }

    }

    @ModelAttribute("sessionholder")
    public SessionStateHolder sessionHolder() {
        return new SessionStateHolder();
    }

}
