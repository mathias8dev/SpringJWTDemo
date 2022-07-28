package com.electricdev.springjwtdemo.services

import com.electricdev.springjwtdemo.exceptions.DataNotFoundException
import com.electricdev.springjwtdemo.repositories.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsService(
    private val userRepository: UserRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String?): User {
        val user = userRepository.findByUsername(username!!).orElseThrow {
            DataNotFoundException("No user with the corresponding username was found")
        }
        return User(
            user.username,
            user.password,
            ArrayList()
        )
    }
}