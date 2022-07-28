package com.electricdev.springjwtdemo.controller

import com.electricdev.springjwtdemo.SpringJwtDemoApplication
import com.electricdev.springjwtdemo.auth.AuthReqBody
import com.electricdev.springjwtdemo.auth.AuthResBody
import com.electricdev.springjwtdemo.jwt.TokenManager
import com.electricdev.springjwtdemo.models.User
import com.electricdev.springjwtdemo.repositories.UserRepository
import com.electricdev.springjwtdemo.services.UserDetailsService
import com.electricdev.springjwtdemo.services.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class JwtController(
    private val userDetailsService: UserDetailsService,
    private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val tokenManager: TokenManager,
) {

    private val logger: Logger = LoggerFactory.getLogger(SpringJwtDemoApplication::class.java)


    @GetMapping("/hello")
    fun hello(): Any {
        return "Hello world"
    }

    @PostMapping("/create")
    fun create(@RequestPart authReqBody: AuthReqBody): Any {
        return userService.createUser(User(0, authReqBody.username, authReqBody.password))
    }

    @PostMapping("/login")
    fun login(@RequestBody authReqBody: AuthReqBody): Any {
        logger.debug("Login controller method")
        logger.debug("The username is ${authReqBody.username} and password is ${authReqBody.password}")
        try {
            val auth = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(authReqBody.username, authReqBody.password)
            )

            if (auth.isAuthenticated) logger.debug("${auth.principal} authenticated successfully")
            else logger.debug("An unexpected error occurred")
        }catch (e: DisabledException) {
            throw RuntimeException("User is disabled")
        }catch (e: BadCredentialsException) {
            throw RuntimeException("Bad credentials")
        }

        val userDetails = userDetailsService.loadUserByUsername(authReqBody.username)
        val jwtToken: String = tokenManager.generateJwtToken(userDetails)

        return AuthResBody(
            userDetails.username,
            jwtToken,
            true
        )
    }
}