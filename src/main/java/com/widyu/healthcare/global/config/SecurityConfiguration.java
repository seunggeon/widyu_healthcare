import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;

    public SecurityConfiguration(CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler) {

        this.customizeAuthenticationSuccessHandler = customizeAuthenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(request -> request.requestMatchers("/").permitAll()
                        .requestMatchers("/admin/*").hasRole("ADMIN")
                        .requestMatchers("/user/*").hasAnyRole("ADMIN", "USER").anyRequest().authenticated())
                .oauth2Login(oauth -> oauth.redirectionEndpoint(endPoint -> endPoint.baseUri("/login/oauth2/code/cognito"))
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userAuthoritiesMapper(userAuthoritiesMapper()))
                        .successHandler(customizeAuthenticationSuccessHandler))
                .logout(logout -> logout.logoutSuccessUrl("/"));
        return http.build();
    }

    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return (authorities) -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            try {
                OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) new ArrayList<>(authorities).get(0);

                mappedAuthorities = ((ArrayList<?>) oidcUserAuthority.getAttributes().get("cognito:groups")).stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toSet());
            } catch (Exception exception) {
                System.out.println("Not Authorized!");

                System.out.println(exception.getMessage());
            }

            return mappedAuthorities;
        };
    }

}