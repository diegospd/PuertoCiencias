/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.foro;

import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultWebRequestor;
import com.restfb.FacebookClient;
import com.restfb.WebRequestor;
import java.io.IOException;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author diego
 */
@ManagedBean
public class Facebook {

      private static final String FB_API_SECRET = "c2828bdb97414306bf4de89fb429d6ac";
      private static final String FB_APP_ID = "732943860124575";
      

      private FacebookClient.AccessToken getFacebookUserToken(String code, String redirectUrl) throws IOException {
            
            
            String appId = "YOUR_APP_ID";
            String secretKey = "YOUR_SECRET_KEY";

            WebRequestor wr = new DefaultWebRequestor();
            WebRequestor.Response accessTokenResponse = wr.executeGet(
                    "https://graph.facebook.com/oauth/access_token?client_id=" + appId + "&redirect_uri=" + redirectUrl
                    + "&client_secret=" + secretKey + "&code=" + code);

            return DefaultFacebookClient.AccessToken.fromQueryString(accessTokenResponse.getBody());
      }
}
