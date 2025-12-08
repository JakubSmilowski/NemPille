package com.example.nempille.data.mapper

import com.example.nempille.data.local.entity.UserEntity
import com.example.nempille.domain.model.User
import com.example.nempille.domain.model.UserRole

fun UserEntity.toDomain(): User =
    User(
        id = id,
        name = name,
        email = email,   // added
        role = UserRole.valueOf(role),
        phone = phoneNumber   // added
    )

fun User.toEntity(): UserEntity =
    UserEntity(
        id = id,
        name = name,
        email = email,         // added
        phoneNumber = phone,
        role = role.name
    )
