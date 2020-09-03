package com.example.route

import com.example.dto.AuthenticationRequestDto
import com.example.dto.RegistrationRequestDto
import com.example.dto.UserResponseDto
import com.example.model.UserModel
import com.example.service.UserService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

class RoutingV1(
    private val userService: UserService
) {
    fun setup(configuration: Routing) {
        with(configuration) {
            route("/api/v1/") {
                route("/") {
                    post("/registration") {
                        val input = call.receive<RegistrationRequestDto>()
                        val response = userService.register(input)
                        call.respond(response)
                    }

                    post("/authentication") {
                        val input = call.receive<AuthenticationRequestDto>()
                        val response = userService.authenticate(input)
                        call.respond(response)
                    }
                }

                authenticate {
                    route("/me") {
                        get {
                            val me = call.authentication.principal<UserModel>()!!
                            call.respond(UserResponseDto.fromModel(me))
                        }
                    }
                }
            }
        }
    }
}