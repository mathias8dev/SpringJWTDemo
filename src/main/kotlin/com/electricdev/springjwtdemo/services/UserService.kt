package com.electricdev.springjwtdemo.services

import com.electricdev.springjwtdemo.exceptions.DataNotFoundException
import com.electricdev.springjwtdemo.exceptions.UsernameAlreadyTakenException
import com.electricdev.springjwtdemo.models.User
import com.electricdev.springjwtdemo.repositories.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun findByUsername(username: String): User = userRepository.findByUsername(username).orElseThrow {
        DataNotFoundException("No user found")
    }

    fun createUser(user: User): User {
        userRepository.findByUsername(user.username).ifPresent {
            throw UsernameAlreadyTakenException(it.username)
        }
        user.password = passwordEncoder.encode(user.password)
        return userRepository.save(user)
    }

}