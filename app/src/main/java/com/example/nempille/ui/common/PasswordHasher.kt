package com.example.nempille.ui.common

import org.mindrot.jbcrypt.BCrypt

//password hashing helper using bcrypt

object BcryptPasswordHasher {
    private const val COST = 12
    //10=fast, 12 is good default, 14+ is too slow

    fun hash(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt(COST))
    }

    fun verify (password: String, hash: String): Boolean {
        return BCrypt.checkpw(password, hash)
    }
}