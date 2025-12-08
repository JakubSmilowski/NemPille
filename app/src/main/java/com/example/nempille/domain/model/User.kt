package com.example.nempille.domain.model

//user model of the app, represent both: patient or caregiver for now
//pure kotlin for now

data class User(
    val id: Int, //unique ID inside database
    val name: String, //full name or nickname
    val email: String, //logged email
    val role: UserRole, //PATIENT or CAREGIVER (from the enum)

    //optional, nullable - can be missing
    val phone: String? = null,

    //no raw passwords
)