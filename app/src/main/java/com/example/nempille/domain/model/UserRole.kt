package com.example.nempille.domain.model

//enum describes role of logged-in user, who uses the app
enum class UserRole {
    PATIENT, //elderly person,taking medications
    CAREGIVER //nurse or family members;who is managing meds
}

//why enum? 1.fixed set of roles
//2.type safety, no misspelling
//we can later add doctor or admin, for example