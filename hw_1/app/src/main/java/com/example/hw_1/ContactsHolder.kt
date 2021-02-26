package com.example.hw_1

object ContactsHolder {
    var contacts: MutableList<ContactsData>? = null
        set(value) {
            if (value != null) {
                field?.addAll(value)
            }
        }
}