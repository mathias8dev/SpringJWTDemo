package com.electricdev.springjwtdemo.security

import com.electricdev.springjwtdemo.SpringJwtDemoApplication
import com.electricdev.springjwtdemo.jwt.TokenManager
import io.jsonwebtoken.ExpiredJwtException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtFilter(
    private val userDetailsService: UserDetailsService,
    private val tokenManager: TokenManager
) : OncePerRequestFilter() {
    private val logger: Logger = LoggerFactory.getLogger(SpringJwtDemoApplication::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        logger.debug("The request url is ${request.requestURL}")

        val tokenHeader: String? = request.getHeader("Authorization")
        var username: String? = null
        var token: String? = null
        if (tokenHeader != null && tokenHeader.startsWith("Bearer")) {
            token = tokenHeader.substring(7)
            try {
                username = tokenManager.getUsernameFromToken(token)
            } catch (e: IllegalArgumentException) {
                println("Unable to get JWT Token")
            } catch (e: ExpiredJwtException) {
                println("JWT Token has expired")
            }
        } else println("Bearer String not found in token")

        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(username)
            if (tokenManager.validateJwtToken(token, userDetails)!!) {

                val authenticationToken = UsernamePasswordAuthenticationToken(
                    userDetails.username,
                    userDetails.password,
                    userDetails.authorities,
                )
                authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authenticationToken
            }

        }
        filterChain.doFilter(request, response)
    }

}

