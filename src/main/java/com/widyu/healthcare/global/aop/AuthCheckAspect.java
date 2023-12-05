//package com.widyu.healthcare.global.aop;
//
//import org.springframework.web.context.request.RequestContextHolder;
//
//@Aspect
//@Component
//@Log4j2
//@SuppressWarnings("unchecked")
//public class AuthCheckAspect {
//    @Before("@annotation(com.delfood.aop.MemberLoginCheck)")
//    public void UsersLoginCheck(JoinPoint jp) throws Throwable {
//        log.debug("AOP - Member Login Check Started");
//
//        HttpSession session = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
//        String userIdx = SessionUtil.getLoginUserIdx(session);
//
//        if (userIdx == null) {
//            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "NO_LOGIN") {};
//        }
//    }
//    @Before("@annotation(com.delfood.aop.LoginCheck) && @ annotation(loginCheck)")
//    public void loginCheck(JoinPoint jp, LoginCheck loginCheck) throws Throwable {
//            log.debug("AOP - Login Check Started");
//
//            HttpSession session =
//            ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest()
//            .getSession();
//
//            if (LoginCheck.UserType.SENIOR.equals(loginCheck.type())) {
//                UsersLoginCheck(jp);
//            }
//        }
//}