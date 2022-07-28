package com.electricdev.springjwtdemo.models

import kotlinx.serialization.Serializable
import javax.persistence.*

@Entity
@Table(name="users")
@Serializable
class User(
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    var id: Long,
    var username: String,
    var password: String
)