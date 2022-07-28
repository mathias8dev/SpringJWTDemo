package com.electricdev.springjwtdemo

import com.electricdev.springjwtdemo.models.User
import com.electricdev.springjwtdemo.services.UserService
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringJwtDemoApplication(
    private val userService: UserService
) : CommandLineRunner {
    private val logger: Logger = LoggerFactory.getLogger(SpringJwtDemoApplication::class.java)

    override fun run(vararg args: String?) {

        val createdUser = userService.createUser(
            User(0,"admin","admin")
        )

        logger.debug("User created")
        logger.debug(Json.encodeToString(createdUser))

    }
}

fun main(args: Array<String>) {
    runApplication<SpringJwtDemoApplication>(*args)
}
