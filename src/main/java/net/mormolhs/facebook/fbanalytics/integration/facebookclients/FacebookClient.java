package net.mormolhs.facebook.fbanalytics.integration.facebookclients;


import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.conf.Configuration;
import facebook4j.conf.ConfigurationBuilder;
import net.mormolhs.facebook.fbanalytics.resources.GlobalParameters;

/**
 * Created by toikonomakos on 3/16/14.
 */

public class FacebookClient {

    public Facebook getConnectionToFacebookApi() {

        // Create conf builder and set authorization and access keys
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true);
        configurationBuilder.setOAuthAccessToken(GlobalParameters.ACCESS_TOKEN);
//        configurationBuilder.setOAuthPermissions("email, publish_stream, id, name, first_name, last_name, read_stream , generic");
//        configurationBuilder.setUseSSL(true);
        configurationBuilder.setJSONStoreEnabled(true);

        // Generate facebook instance.
        Configuration configuration = configurationBuilder.build();
        FacebookFactory ff = new FacebookFactory(configuration);
        Facebook facebook = ff.getInstance();
        facebook.setOAuthAppId("", "");
        return facebook;
    }
}
