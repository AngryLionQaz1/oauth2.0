package com.snow.oauth.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;
    @Autowired
    private AuthenticationManager authenticationManager;

    //jwt 模式
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;


    /**
     * 令牌端点安全约束
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        /*
         *  /oauth/authorize : 授权端点
         *  /oauth/token : 令牌端点
         *  /oauth/confirm_access : 用户确认授权提交端点
         *  /oauth/error : 授权服务错误信息端点
         *  /oauth/check_token : 用于资源服务访问的令牌解析端点
         *  /oauth/token_key : 提供公钥的端点，如果使用的是JWT令牌。
         *
         */
        security
                .tokenKeyAccess("permitAll()")    // 公钥公开
                .checkTokenAccess("permitAll()")    // 检测token公开
                .allowFormAuthenticationForClients();    // 表单认证
    }

    /**
     * 客户端详情服务
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()    // 使用内存存储
                .withClient("c1")    // 客户端名称
                .secret(new BCryptPasswordEncoder().encode("secret"))    // 密钥
                .resourceIds("order")    // 允许访问的资源
                .authorizedGrantTypes("authorization_code","password","client_credentials","implicit","refresh_token")    // 允许的oauth授权类型
                .scopes("all")    // 允许的授权范围
                .autoApprove(false)    // 跳转到授权页面
                .redirectUris("http://www.baidu.com");
    }

    /**
     * 令牌访问端点和令牌服务
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)    // 密码模式
                .authorizationCodeServices(authorizationCodeServices)    // 授权码模式
                .tokenServices(tokenServices())    // 令牌管理模式
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    /**
     * 令牌管理模式
     * @return
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices(){
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        //客户端详情服务
        defaultTokenServices.setClientDetailsService(clientDetailsService);
        //支持刷新令牌
        defaultTokenServices.setSupportRefreshToken(true);
        //令牌存储策略
        defaultTokenServices.setTokenStore(tokenStore);

        //jwt 模式
        /*令牌增强*/
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter));
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);
        //jwt 模式


        //令牌默认有效期2小时
        defaultTokenServices.setAccessTokenValiditySeconds(7200);
        //刷新令牌默认有效期3天
        defaultTokenServices.setRefreshTokenValiditySeconds(259200);
        return defaultTokenServices;
    }


    /**
     * 授权码的使用模式
     * @return
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(){
         return new InMemoryAuthorizationCodeServices();

    }
}
