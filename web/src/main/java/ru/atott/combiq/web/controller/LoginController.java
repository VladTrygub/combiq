package ru.atott.combiq.web.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.codehaus.jparsec.internal.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;
import ru.atott.combiq.service.site.UrlResolver;
import ru.atott.combiq.service.bean.User;
import ru.atott.combiq.service.bean.UserType;
import ru.atott.combiq.service.user.*;
import ru.atott.combiq.web.security.AuthService;
import ru.atott.combiq.service.site.RequestUrlResolver;
import ru.atott.combiq.web.utils.ViewUtils;
import ru.atott.combiq.web.view.InstantMessageHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class LoginController extends BaseController {

    @Value("${auth.github.clientId}")
    private String githubClientId;

    @Value("${auth.github.clientSecret}")
    private String githubClientSecret;

    @Value("${auth.vk.clientId}")
    private String vkClientId;

    @Value("${auth.vk.clientSecret}")
    private String vkClientSecret;

    @Value("${auth.stackexchange.clientId}")
    private String stackexchangeClientId;

    @Value("${auth.stackexchange.clientSecret}")
    private String stackexchangeClientSecret;

    @Value("${auth.stackexchange.clientKey}")
    private String stackexchangeClientKey;

    @Value("${auth.facebook.clientId}")
    private String facebookClientId;

    @Value("${auth.facebook.clientSecret}")
    private String facebookClientSecret;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private InstantMessageHolder instantMessageHolder;

    @Autowired
    private RememberMeServices rememberMeServices;

    @RequestMapping(value = "/login.do", method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("githubClientId", githubClientId);
        modelAndView.addObject("vkClientId", vkClientId);
        modelAndView.addObject("stackexchangeClientId", stackexchangeClientId);
        return modelAndView;
    }

    @RequestMapping(value = "/login/callback/vk.do", method = RequestMethod.GET)
    public RedirectView vkCallback(@RequestParam(value = "code") String code,
                                   @RequestParam(value = "state") String state,
                                   HttpServletRequest httpRequest,
                                   HttpServletResponse httpResponse) throws IOException, ServletException {
        UrlResolver urlResolver = new RequestUrlResolver(httpRequest);

        String sessionId = httpRequest.getSession(true).getId();
        String stateHash = StringUtils.substringBefore(state, ":");
        String actualStateHash = DigestUtils.sha256Hex(sessionId + authService.getLaunchDependentSalt());
        String redirectUrl = StringUtils.defaultIfBlank(StringUtils.substringAfter(state, ":"), "/");

        if (!Objects.equals(stateHash, actualStateHash)) {
            return new RedirectView("/");
        }

        String exchangeUrl = UriComponentsBuilder
                .fromHttpUrl("https://oauth.vk.com/access_token")
                .queryParam("client_id", vkClientId)
                .queryParam("client_secret", vkClientSecret)
                .queryParam("code", code)
                .queryParam("redirect_uri", urlResolver.externalize("/login/callback/vk.do"))
                .build()
                .toString();

        HttpClient client = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(exchangeUrl);
        httpPost.setHeader("Accept", "application/json");
        HttpResponse response = client.execute(httpPost);
        String responseJson = IOUtils.toString(response.getEntity().getContent());
        JsonObject responseJsonObject = ViewUtils.parseJson(responseJson);
        String accessToken = responseJsonObject.get("access_token").getAsString();

        String getUrl = UriComponentsBuilder
                .fromHttpUrl("https://api.vk.com/method/users.get")
                .queryParam("access_token", accessToken)
                .queryParam("fields", "photo_50,city,site")
                .build()
                .toUriString();
        HttpGet httpGet = new HttpGet(getUrl);
        httpGet.setHeader("Accept", "application/json");
        response = client.execute(httpGet);
        responseJson = IOUtils.toString(response.getEntity().getContent(), "utf-8");
        responseJsonObject = ViewUtils.parseJson(responseJson);

        JsonObject userInfo = responseJsonObject.getAsJsonArray("response").get(0).getAsJsonObject();
        String uid = userInfo.get("uid").getAsString();
        String firstName = userInfo.get("first_name").getAsString();
        String lastName = userInfo.get("last_name").getAsString();
        String city = getDefaultString(userInfo.get("city"));
        String photo = getDefaultString(userInfo.get("photo_50"));
        String site = getDefaultString(userInfo.get("site"));

        if (StringUtils.isNoneBlank(site) && StringUtils.startsWithIgnoreCase(site, "http://")) {
            site = "http://" + site;
        }

        User user = userService.findByLoginAndType(uid, UserType.vk);

        VkRegistrationContext vkRegistrationContext = new VkRegistrationContext();
        vkRegistrationContext.setUid(uid);
        vkRegistrationContext.setName((firstName + " " + lastName).trim());
        vkRegistrationContext.setLocation(city);
        vkRegistrationContext.setAvatarUrl(photo);

        if (user == null) {
            user = userService.registerUserViaVk(vkRegistrationContext);
        } else {
            user = userService.updateVkUser(vkRegistrationContext);
        }

        httpRequest.login(user.getQualifier().toString(), "vk");
        rememberMeServices.loginSuccess(httpRequest, httpResponse, authService.getAuthentication());

        return new RedirectView(redirectUrl);
    }

    @RequestMapping(value = "/login/callback/github.do", method = RequestMethod.GET)
    public RedirectView githubCallback(@RequestParam(value = "code") String code,
                                       @RequestParam(value = "state") String state,
                                       HttpServletRequest httpRequest,
                                       HttpServletResponse httpResponse) throws IOException, ServletException {
        String sessionId = httpRequest.getSession(true).getId();
        String stateHash = StringUtils.substringBefore(state, ":");
        String actualStateHash = DigestUtils.sha256Hex(sessionId + authService.getLaunchDependentSalt());
        String redirectUrl = StringUtils.defaultIfBlank(StringUtils.substringAfter(state, ":"), "/");

        if (!Objects.equals(stateHash, actualStateHash)) {
            return new RedirectView("/");
        }

        String exchangeUrl = UriComponentsBuilder
                .fromHttpUrl("https://github.com/login/oauth/access_token")
                .queryParam("client_id", githubClientId)
                .queryParam("client_secret", githubClientSecret)
                .queryParam("code", code)
                .build()
                .toUriString();

        HttpClient client = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(exchangeUrl);
        httpPost.setHeader("Accept", "application/json");
        HttpResponse response = client.execute(httpPost);
        String responseJson = IOUtils.toString(response.getEntity().getContent());
        JsonObject responseJsonObject = ViewUtils.parseJson(responseJson);
        String accessToken = responseJsonObject.get("access_token").getAsString();

        String getUrl = UriComponentsBuilder
                .fromHttpUrl("https://api.github.com/user")
                .queryParam("access_token", accessToken)
                .build()
                .toUriString();
        HttpGet httpGet = new HttpGet(getUrl);
        httpGet.setHeader("Accept", "application/json");
        response = client.execute(httpGet);
        responseJson = IOUtils.toString(response.getEntity().getContent());
        responseJsonObject = ViewUtils.parseJson(responseJson);

        String userLogin = responseJsonObject.get("login").getAsString().toLowerCase();
        User user = userService.findByLoginAndType(userLogin, UserType.github);

        GithubRegistrationContext registrationContext = new GithubRegistrationContext();
        registrationContext.setLogin(userLogin);
        registrationContext.setHome(getDefaultString(responseJsonObject.get("html_url")));
        registrationContext.setName(getDefaultString(responseJsonObject.get("name")));
        if (StringUtils.isBlank(registrationContext.getName())) {
            registrationContext.setName(userLogin);
        }
        registrationContext.setLocation(getDefaultString(responseJsonObject.get("location")));
        registrationContext.setAvatarUrl(getDefaultString(responseJsonObject.get("avatar_url")));
        registrationContext.setEmail(getDefaultString(responseJsonObject.get("email")));

        if (user == null) {
            user = userService.registerUserViaGithub(registrationContext);
        } else {
            user = userService.updateGithubUser(registrationContext);
        }

        httpRequest.login(user.getQualifier().toString(), "github");
        rememberMeServices.loginSuccess(httpRequest, httpResponse, authService.getAuthentication());

        return new RedirectView(redirectUrl);
    }

    @RequestMapping(value = "/login/callback/stackexchange.do", method = RequestMethod.GET)
    public RedirectView stackexchangeCallback(@RequestParam(value = "code") String code,
                                       @RequestParam(value = "state") String state,
                                       HttpServletRequest httpRequest,
                                       HttpServletResponse httpResponse) throws IOException, ServletException {
        String sessionId = httpRequest.getSession(true).getId();
        String stateHash = StringUtils.substringBefore(state, ":");
        String actualStateHash = DigestUtils.sha256Hex(sessionId + authService.getLaunchDependentSalt());
        String redirectUrl = StringUtils.defaultIfBlank(StringUtils.substringAfter(state, ":"), "/");

        if (!Objects.equals(stateHash, actualStateHash)) {
            return new RedirectView("/");
        }

        UrlResolver urlResolver = new RequestUrlResolver(httpRequest);

        String exchangeUrl = UriComponentsBuilder
                .fromHttpUrl("https://stackexchange.com/oauth/access_token")
                .queryParam("client_id", stackexchangeClientId)
                .queryParam("client_secret", stackexchangeClientSecret)
                .queryParam("code", code)
                .queryParam("redirect_uri", urlResolver.externalize("/login/callback/stackexchange.do"))
                .build()
                .toString();

        HttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(exchangeUrl);
        HttpResponse response = client.execute(httpPost);
        String responseAnswer = IOUtils.toString(response.getEntity().getContent());
        String accessToken = StringUtils.substringAfter(responseAnswer, "access_token=");

        String getUrl = UriComponentsBuilder
                .fromHttpUrl("https://api.stackexchange.com/2.2/me")
                .queryParam("key", stackexchangeClientKey)
                .queryParam("access_token", accessToken)
                .queryParam("site", "stackoverflow")
                .build()
                .toUriString();
        HttpGet httpGet = new HttpGet(getUrl);
        httpGet.setHeader("Accept", "application/json");
        response = client.execute(httpGet);
        String responseJson = IOUtils.toString(response.getEntity().getContent(), "utf-8");
        JsonObject responseJsonObject = ViewUtils.parseJson(responseJson);

        JsonArray items = responseJsonObject.get("items").getAsJsonArray();

        if (items.size() == 0) {
            instantMessageHolder.push("У вас отсутствует профиль на stackoverflow.com.");
        }

        JsonObject profile = items.get(0).getAsJsonObject();
        String userId = profile.get("user_id").getAsString();
        String displayName = profile.get("display_name").getAsString();
        String location = null;
        if (profile.has("location") && !profile.get("location").isJsonNull()) {
            location = profile.get("location").getAsString();
        }
        String avatarUrl = null;
        if (profile.has("profile_image") && !profile.get("profile_image").isJsonNull()) {
            avatarUrl = profile.get("profile_image").getAsString();
        }

        User user = userService.findByLoginAndType(userId, UserType.stackexchange);

        StackexchangeRegistrationContext registrationContext = new StackexchangeRegistrationContext();
        registrationContext.setUid(userId);
        registrationContext.setAvatarUrl(avatarUrl);
        registrationContext.setLocation(location);
        registrationContext.setName(displayName);

        if (user == null) {
            user = userService.registerUserViaStackexchange(registrationContext);
        } else {
            user = userService.updateStackexchangeUser(registrationContext);
        }

        httpRequest.login(user.getQualifier().toString(), "stackexchange");
        rememberMeServices.loginSuccess(httpRequest, httpResponse, authService.getAuthentication());

        return new RedirectView(redirectUrl);
    }

    @RequestMapping(value = "/login/callback/facebook.do", method = RequestMethod.GET)
    public RedirectView facebookCallback(@RequestParam(value = "code") String code,
                                         @RequestParam(value = "state") String state,
                                         HttpServletRequest httpRequest,
                                         HttpServletResponse httpResponse) throws IOException, ServletException {
        UrlResolver urlResolver = new RequestUrlResolver(httpRequest);

        String sessionId = httpRequest.getSession(true).getId();
        String stateHash = StringUtils.substringBefore(state, ":");
        String actualStateHash = DigestUtils.sha256Hex(sessionId + authService.getLaunchDependentSalt());
        String redirectUrl = StringUtils.defaultIfBlank(StringUtils.substringAfter(state, ":"), "/");

        if (!Objects.equals(stateHash, actualStateHash)) {
            return new RedirectView("/");
        }

        String exchangeUrl = UriComponentsBuilder
                .fromHttpUrl("https://graph.facebook.com/v2.3/oauth/access_token")
                .queryParam("client_id", facebookClientId)
                .queryParam("client_secret", facebookClientSecret)
                .queryParam("code", code)
                .queryParam("redirect_uri", urlResolver.externalize("/login/callback/facebook.do"))
                .build()
                .toString();

        HttpClient client = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(exchangeUrl);
        httpPost.setHeader("Accept", "application/json");
        HttpResponse response = client.execute(httpPost);
        String responseJson = IOUtils.toString(response.getEntity().getContent());
        JsonObject responseJsonObject = ViewUtils.parseJson(responseJson);
        String accessToken = responseJsonObject.get("access_token").getAsString();

        String getUrl = UriComponentsBuilder
                .fromHttpUrl("https://graph.facebook.com/v2.5/me")
                .queryParam("access_token", accessToken)
                .queryParam("fields", UrlResolver.encodeUrlComponent("name,link,picture{url}"))
                .queryParam("format", "json")
                .build()
                .toUriString();
        HttpGet httpGet = new HttpGet(getUrl);
        httpGet.setHeader("Accept", "application/json");
        response = client.execute(httpGet);
        responseJson = IOUtils.toString(response.getEntity().getContent(), "utf-8");

        JsonObject userInfo = ViewUtils.parseJson(responseJson);
        String uid = userInfo.get("id").getAsString();
        String name = userInfo.get("name").getAsString();
        String photo = null;
        if (userInfo.get("picture") != null && userInfo.get("picture").isJsonObject()) {
            JsonObject picture = userInfo.get("picture").getAsJsonObject();
            if (picture.get("data") != null && picture.get("data").isJsonObject()) {
                JsonObject data = picture.get("data").getAsJsonObject();
                photo = getDefaultString(data.get("url"));
            }
        }
        String site = getDefaultString(userInfo.get("link"));

        User user = userService.findByLoginAndType(uid, UserType.facebook);

        FacebookRegistrationContext facebookRegistrationContext = new FacebookRegistrationContext();
        facebookRegistrationContext.setId(uid);
        facebookRegistrationContext.setName(name);
        facebookRegistrationContext.setAvatarUrl(photo);

        if (user == null) {
            user = userService.registerUserViaFacebook(facebookRegistrationContext);
        } else {
            user = userService.updateFacebookUser(facebookRegistrationContext);
        }

        httpRequest.login(user.getQualifier().toString(), "facebook");
        rememberMeServices.loginSuccess(httpRequest, httpResponse, authService.getAuthentication());

        return new RedirectView(redirectUrl);
    }

    private String getDefaultString(JsonElement value) {
        if (value == null || value.isJsonNull()) {
            return null;
        }
        return StringUtils.defaultIfBlank(value.getAsString(), null);
    }
}
