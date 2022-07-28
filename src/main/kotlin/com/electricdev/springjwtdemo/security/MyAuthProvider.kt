package com.electricdev.springjwtdemo.security

import com.electricdev.springjwtdemo.SpringJwtDemoApplication
import com.electricdev.springjwtdemo.services.UserDetailsService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component


@Component
class MyAuthProvider(
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder
): AuthenticationProvider {

    override fun authenticate(authentication: Authentication?): Authentication {
        val providedUsername = authentication?.principal.toString()
        val providedPassword = authentication?.credentials.toString()
        val userDetails = userDetailsService.loadUserByUsername(providedUsername)
        if (!passwordEncoder.matches(providedPassword, userDetails.password)) throw RuntimeException("Incorrect credentials")

        return UsernamePasswordAuthenticationToken(userDetails.username, userDetails.password, userDetails.authorities)
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return authentication!! == UsernamePasswordAuthenticationToken::class.java
    }
}