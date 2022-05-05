package com.toropov.my_outfit.helperClasses

class UserHelperClass() {
    var fullName = ""
    var username = ""
    var email = ""
    var password = ""

    constructor(_fullName: String, _username: String, _email: String, _password: String) : this() {
        this.fullName = _fullName
        this.username = _username
        this.email = _email
        this.password = _password
    }
}