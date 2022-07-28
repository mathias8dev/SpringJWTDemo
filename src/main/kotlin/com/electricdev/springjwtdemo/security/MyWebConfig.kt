package com.electricdev.springjwtdemo.security

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.SecurityConfig
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class MyWebConfig(
    private val jwtFilter: JwtFilter,
    private val jwtAuthEntryPoint: JwtAuthenticationEntryPoint,
//    private val authProvider: MyAuthProvider
) {
    private val logger: Logger = LoggerFactory.getLogger(SecurityConfig::class.java)


    @Bean
    @Throws(Exception::class)
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain? {
        logger.debug("SecurityConfig initialized.")
        // We don't need CSRF for this example
        httpSecurity.csrf().disable().authorizeRequests()
            .antMatchers("/api/hello", "/api/login", "/api/create").permitAll()
            .anyRequest().authenticated()
            .and().exceptionHandling()
            .authenticationEntryPoint(jwtAuthEntryPoint)
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
        return httpSecurity.build()
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager? {
        logger.debug("AuthenticationManager invoked.")
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        logger.debug("PasswordEncoder invoked.")
        return BCryptPasswordEncoder()
    }


}