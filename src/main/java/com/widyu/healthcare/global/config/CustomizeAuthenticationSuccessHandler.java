@Component
public class CustomizeAuthenticationSuccessHandler implements 쪼AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        for (GrantedAuthority auth : authentication.getAuthorities()) {

            DefaultOidcUser defaultOidcUser = (DefaultOidcUser) authentication.getPrincipal();

            Map<String, Object> userAttributes = defaultOidcUser.getAttributes();

            System.out.println(userAttributes);

            if ("ROLE_ADMIN".equals(auth.getAuthority())) {
                System.out.println(userAttributes.get("cognito:username") + " Is Admin!");
                response.sendRedirect("/admin/greetMe");
            } else if ("ROLE_USER".equals(auth.getAuthority())) {
                System.out.println(userAttributes.get("cognito:username") + " Is User!");
                response.sendRedirect("/user/greetMe");
            }
        }
    }
}