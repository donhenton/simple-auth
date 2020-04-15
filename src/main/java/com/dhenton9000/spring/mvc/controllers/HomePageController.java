package com.dhenton9000.spring.mvc.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
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
    private final StringKeyGenerator secureKeyGenerator
            = new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96);
    @Autowired
    private Environment env;
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/home")
    public ModelAndView homePage() {
        String urlCodeAuth = "/app/getauth.html";
        String urlPKCE = "/app/getPKCE.html";
        ModelAndView m = new ModelAndView("tiles.home");
        m.addObject("urlCodeAuth", urlCodeAuth);
        m.addObject("urlPKCE", urlPKCE);
        return m;
    }

    /**
     * compose the referral to okta code code authorization flow.
     *
     *
     * @param sessionState
     * @return
     */
    @RequestMapping("/getPKCE")
    public String getPKCE(@ModelAttribute("sessionholder") SessionStateHolder sessionState) {

        String clientId = env.getProperty("client-id-pkce"); // points to spa app
        //note that env can be overriden by command line environment assignments
        //and is part of the spring boot system of parameter use

        String issuer = env.getProperty("issuer");
        String authorizationUri = issuer  + "/oauth2/default/v1/authorize";
        StringBuilder sb = new StringBuilder();
        sb.append("redirect:");
        sb.append(authorizationUri);

        String nonce = UUID.randomUUID().toString();
        String state = UUID.randomUUID().toString();
        String challenge = null;

        try {
            //SecureRandom sr = new SecureRandom();
            // byte[] code = new byte[32];
            // sr.nextBytes(code);
            String verifier = this.secureKeyGenerator.generateKey();
            sessionState.setpKCEverifier(verifier);

            challenge = createHash(verifier);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("digest problem " + e.getClass().getName()
                    + " " + e.getMessage());
        }

        String redirectUri = env.getProperty("redirect-uri-pkce");
        redirectUri = redirectUri.trim();
        try {
            redirectUri = URLEncoder.encode(redirectUri, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            log.error("cannot peform url encode ");
        }

        sb.append("?");
        sb.append("client_id=");
        sb.append(clientId);
        sb.append("&response_type=code&scope=openid email groups profile");
        sb.append("&redirect_uri=");
        sb.append(redirectUri);
        sb.append("&code_challenge=");
        sb.append(challenge);
        sb.append("&code_challenge_method=S256");
        sb.append("&state=");
        sb.append(state);
        sb.append("&nonce=");
        sb.append(nonce);

        return sb.toString();
    }

    /**
     * compose the referral to okta code code authorization flow.
     *
     *
     * @param sessionState
     * @return
     */
    @RequestMapping("/getauth")
    public String getAuth(@ModelAttribute("sessionholder") SessionStateHolder sessionState) {
        String clientId = env.getProperty("client-id");
        //note that env can be overriden by command line environment assignments
        //and is part of the spring boot system of parameter use

        String clientSecret = env.getProperty("client-secret");
        String issuer = env.getProperty("issuer");
        ///oauth2/v1/authorize is legit as a authorizationUri
        //BUT WILL NOT PRODUCE FAT TOKENS WITH GROUPS AND SUCH
        String authorizationUri = issuer  + "/oauth2/default/v1/authorize";
        String jwkUri = issuer + "/oauth2/v1/keys";
      //  String userInfoUri = issuer + "/oauth2/v1/userinfo";
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
        sb.append("&response_type=code&scope=openid offline_access email groups profile");
        sb.append("&redirect_uri=");
        sb.append(redirectUri);
        sb.append("&state=");
        sb.append(state);
        sb.append("&nonce=");
        sb.append(nonce);

        return sb.toString();
    }

    @RequestMapping("/process")
    public String doProcess(Model model,
            @RequestParam String code,
            @RequestParam String state,
            @ModelAttribute("sessionholder") SessionStateHolder sessionState) {

        String issuer = env.getProperty("issuer");

        String storedState = sessionState.getState();
        if (storedState != null && storedState.equals(state)) {
            sessionState.setState(null);
            String clientId = env.getProperty("client-id");

            String clientSecret = env.getProperty("client-secret");
            String originalInput = clientId + ":" + clientSecret;
            String authString = Base64.getEncoder().encodeToString(originalInput.getBytes());
            String tokenUri = issuer + "/oauth2/default/v1/token";
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
                sessionState.setAccessToken((String) result.get("access_token"));
                sessionState.setRefreshToken((String) result.get("refresh_token"));
                sessionState.setIdToken((String) result.get("id_token"));

  
                int time = (Integer) result.get("expires_in");

                sessionState.setExpiresSeconds(time);
                 

            } else {
                log.error("token call Errored with code " + postResponse.getStatusCode());
            }

        } else {
            throw new RuntimeException("state not found");
        }
        /////////////////////////////////////////////////////////////////////////////

        String userUri = issuer + "/oauth2/default/v1/userinfo";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setBearerAuth(sessionState.getAccessToken());
        HttpEntity<Object> userEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Map> getResponse = restTemplate.exchange(userUri, HttpMethod.POST, userEntity, Map.class);

        if (getResponse.getStatusCode().equals(HttpStatus.OK)) {
            Map result = getResponse.getBody();
            model.addAttribute("userInfo", result);
            
           // log.debug("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");
           // log.debug("\n" + sessionState.getAccessToken() + "\n");
           // result.keySet().forEach(k -> {

            //    log.debug("key " + k + " " + result.get(k));
           // });
        } else {
            log.error("user info Errored with code " + getResponse.getStatusCode());
        }
              model.addAttribute("accessToken", sessionState.getAccessToken());
                model.addAttribute("idToken", sessionState.getIdToken());

        return "tiles.process";
    }

    /**
     * this actually points to an okta app configured for SPA stuff.
     *
     * @param model
     * @param code
     * @param state
     * @param sessionState
     * @return
     */
    @RequestMapping("/processPKCE")
    public String doProcessPKCE(Model model,
            @RequestParam String code,
            @RequestParam String state,
            HttpServletRequest request,
            HttpServletResponse response,
            @ModelAttribute("sessionholder") SessionStateHolder sessionState) {
        log.debug("\n@@@@@@@@@@@@@@@PKCE PROCESS@@@@@@@@@@@@@@@@@@@@@@\n");
        model.addAttribute("code", code);
        model.addAttribute("state", state);
        String issuer = env.getProperty("issuer");

        Arrays.asList(request.getCookies()).forEach(c -> {

            log.debug("cookie " + c.getName() + " ==> " + c.getValue());

        });

        String storedState = sessionState.getState();
        String verifier = sessionState.getpKCEverifier();
        if (storedState != null && storedState.equals(state)) {
            sessionState.setState(null);
            String clientId = env.getProperty("client-id-pkce");
            String tokenUri = issuer + "/oauth2/default/v1/token";
//
//            String clientSecret = env.getProperty("client-secret");
//            String originalInput = clientId + ":" + clientSecret;
//            String authString = Base64.getEncoder().encodeToString(originalInput.getBytes());
//            
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("user-agent", "Macintosh; Intel Mac OS X 10_14_6) "
                    + "AppleWebKit/537.36 (KHTML, like Gecko) "
                    + "Chrome/80.0.3987.163 Safari/537.36");
            httpHeaders.add("x-okta-user-agent-extended", "okta-auth-js-3.0.1");

            httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            //   httpHeaders.setBasicAuth(authString);
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String redirectUri = env.getProperty("redirect-uri-pkce");
            redirectUri = redirectUri.trim();
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("grant_type", "authorization_code");
            map.add("redirect_uri", redirectUri);
            map.add("code", code);
            map.add("client_id", clientId);
            map.add("code_verifier", verifier);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, httpHeaders);

            ResponseEntity<Map> postResponse = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, Map.class);

            if (postResponse.getStatusCode().equals(HttpStatus.OK)) {
                Map result = postResponse.getBody();
                sessionState.setAccessToken((String) result.get("access_token"));
                sessionState.setRefreshToken((String) result.get("refresh_token"));
                sessionState.setIdToken((String) result.get("id_token"));
                int time = (Integer) result.get("expires_in");

                sessionState.setExpiresSeconds(time);
                result.keySet().forEach(k -> {

                    log.debug("user key " + k + " " + result.get(k));

                });

            } else {
                log.error("PKCE token call Errored with code " + postResponse.getStatusCode());
            }

        } else {
            throw new RuntimeException("state not found");
        }

        return "tiles.process";
    }

    @ModelAttribute("sessionholder")
    public SessionStateHolder sessionHolder() {
        return new SessionStateHolder();
    }

    private static String createHash(String value) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(value.getBytes(StandardCharsets.US_ASCII));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }

}
/*
            String userUri = issuer + "/oauth2/v1/userinfo";
            httpHeaders = new HttpHeaders();       
            httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            httpHeaders.setBearerAuth(sessionState.getAccessToken());
             HttpEntity<Object> userEntity = new HttpEntity<>(httpHeaders);
           
            userEntity.getHeaders().keySet().forEach(k -> {

                    log.debug("key " + k + " " + userEntity.getHeaders().get(k));

                });
            ResponseEntity<Map> getResponse = restTemplate.exchange(userUri, HttpMethod.GET, userEntity, Map.class);

            if (getResponse.getStatusCode().equals(HttpStatus.OK)) {
                Map result = getResponse.getBody();
               // result.keySet().forEach(k -> {

                  //  log.debug("key " + k + " " + result.get(k));

               // });
 
              } else {
                log.error("user info Errored with code " + postResponse.getStatusCode());
            }
            return "tiles.process";
        
 */
