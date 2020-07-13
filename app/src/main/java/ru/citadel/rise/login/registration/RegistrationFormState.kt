package ru.citadel.rise.login.registration

data class RegistrationFormState(
    val emailError: Int? = null,
    val nameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)
