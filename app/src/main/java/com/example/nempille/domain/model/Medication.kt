package com.example.nempille.domain.model
//medication belonging to user
//domain-level model of medication
//not a Room entity or network DTO
//clean representation used by ViewModels
data class Medication(
    val id: Long, //id pf medication
    val userId: Long, //id of user (patient) who takes THIS medication

    val name:String,//ibumetin etc
    val dosage: String, //free text(1 pill or 10mg)
    val frequencyPerDay: Int, //how many times per day

    //optional description/instructions for user
    val notes: String? =  null, //take with food, in the morning etc
)