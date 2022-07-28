package com.electricdev.springjwtdemo.repositories

import com.electricdev.springjwtdemo.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface UserRepository: JpaRepository<User, Long> {

    fun findByUsername(username: String): Optional<User>
    fun findByUsernameAndPassword(username: String, password: String): Optional<User>
}