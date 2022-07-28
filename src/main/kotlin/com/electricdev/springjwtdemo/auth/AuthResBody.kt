package com.electricdev.springjwtdemo.auth

class AuthResBody(
    val username: String,
    val token: String,
    val authenticated: Boolean
)