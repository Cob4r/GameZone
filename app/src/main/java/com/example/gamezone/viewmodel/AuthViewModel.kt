package com.example.gamezone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamezone.data.model.User
import com.example.gamezone.data.repository.UserRepository
import com.example.gamezone.util.Validation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: UserRepository) : ViewModel() {

    data class RegisterState(
        val name: String = "",
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val phone: String = "",
        val genres: List<String> = emptyList(),
        val errors: Map<String, String> = emptyMap(),
        val success: Boolean = false
    )

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState

    fun updateField(field: String, value: String) {
        _uiState.update {
            when (field) {
                "name" -> it.copy(name = value)
                "email" -> it.copy(email = value)
                "password" -> it.copy(password = value)
                "confirmPassword" -> it.copy(confirmPassword = value)
                "phone" -> it.copy(phone = value)
                else -> it
            }
        }
    }

    fun toggleGenre(genre: String) {
        _uiState.update {
            val list = it.genres.toMutableList()
            if (list.contains(genre)) list.remove(genre) else list.add(genre)
            it.copy(genres = list)
        }
    }

    fun register() {
        val s = _uiState.value
        val errors = mutableMapOf<String, String>()

        if (!Validation.isNameValid(s.name)) errors["name"] = "Nombre inválido"
        if (!Validation.isEmailValid(s.email)) errors["email"] = "Correo debe terminar en @duoc.cl"
        if (!Validation.isPasswordValid(s.password)) errors["password"] = "Contraseña débil"
        if (!Validation.doPasswordsMatch(s.password, s.confirmPassword)) errors["confirmPassword"] = "No coinciden"
        if (!Validation.isPhoneValid(s.phone)) errors["phone"] = "Teléfono inválido"
        if (s.genres.isEmpty()) errors["genres"] = "Selecciona al menos un género"

        if (errors.isNotEmpty()) {
            _uiState.update { it.copy(errors = errors) }
            return
        }

        viewModelScope.launch {
            val user = User(
                name = s.name,
                email = s.email,
                password = s.password,
                phone = s.phone,
                genres = s.genres
            )
            repository.addUser(user)
            _uiState.update { it.copy(success = true, errors = emptyMap()) }
        }
    }
    fun login() {
        val s = _uiState.value
        val errors = mutableMapOf<String, String>()

        if (!s.email.contains("@duoc.cl")) {
            errors["login"] = "El correo debe ser institucional (@duoc.cl)"
        } else if (s.password.isBlank()) {
            errors["login"] = "Debe ingresar una contraseña"
        } else if (s.password.length < 6) {
            errors["login"] = "Contraseña demasiado corta"
        }

        if (errors.isNotEmpty()) {
            _uiState.update { it.copy(errors = errors, success = false) }
            return
        }

        // 🔹 Simulación de login correcto
        if (s.email == "user@duoc.cl" && s.password == "123456") {
            _uiState.update { it.copy(success = true, errors = emptyMap()) }
        } else {
            _uiState.update { it.copy(errors = mapOf("login" to "Credenciales inválidas")) }
        }
    }

}