package com.electricdev.springjwtdemo.security


import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.Serializable
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint, Serializable {

    companion object {
        private const val serialVersionUID = -7858869558953243875L;
        val logger: Logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint::class.java)
    }

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {

        logger.error("Unauthorized error: {}", authException!!.message)
        response!!.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You're not authorized to perform this transaction.");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.setContentType(MediaType.ALL_VALUE);
//        You can also write JSON object below to send proper response as you send from REST resources.
//        response.getWriter().write("You're not authorized to perform this transaction.");
    }
}