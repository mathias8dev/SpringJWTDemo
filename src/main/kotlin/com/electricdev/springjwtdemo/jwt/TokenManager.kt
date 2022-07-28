package com.electricdev.springjwtdemo.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap


@Component
class TokenManager: Serializable {

    companion object {
        private const val serialVersionUID = 7008375124389347049L
    }
    @Value("\${jwt.secret}")
    private val jwtSecret: String? = null

    // 10 years
    private val expirationTime = 10*525600*60

    fun generateJwtToken(userDetails: UserDetails): String {
        val claims: Map<String, Any> = HashMap()
        (claims as HashMap).apply {
            this["singlePageAppSupport"] = true
            this["mobileAppSupport"] = true
        }
        return Jwts.builder().setClaims(claims).setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expirationTime * 1000))
            .signWith(SignatureAlgorithm.HS512, jwtSecret).compact()
    }

    fun validateJwtToken(token: String?, userDetails: UserDetails): Boolean? {
        val username = getUsernameFromToken(token)
        return username == userDetails.username
    }

    fun getUsernameFromToken(token: String?): String {
        val claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body
        return claims.subject
    }

}