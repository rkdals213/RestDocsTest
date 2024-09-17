package kgp.liivm.restdocstest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/api")
class Controller {

    @GetMapping("/{teamId}")
    fun getData(
        @PathVariable teamId: Long,
        @RequestParam teamName: String,
    ): ResponseEntity<ApiResponse<Team>> {

        val response = getData()

        return ResponseEntity.ok(response)
    }

    @PostMapping
    fun postData(
        @RequestBody request: Request,
    ): ResponseEntity<ApiResponse<Team>> {

        val response = getData()

        return ResponseEntity.ok(response)

    }

    private fun getData(): ApiResponse<Team> {
        val response = apiResponse<Team> {
            status = 200
            message = "OK"
            data = Team(
                name = "My Team",
                status = Status.ACTIVE,
                registeredDateTime = Instant.now(),
                members = listOf(
                    Member(
                        name = "Kang",
                        registeredDateTime = Instant.now(),
                        age = 33,
                        isMarried = false
                    ),
                    Member(
                        name = "Kwun",
                        registeredDateTime = Instant.now(),
                        age = 30,
                        isMarried = false
                    )
                )
            )
        }
        return response
    }
}

class ApiResponse<T>(
    var status: Int = 200,
    var message: String = "",
    var data: T? = null
)

fun <T> apiResponse(block: ApiResponse<T>.() -> Unit): ApiResponse<T> {
    val apiResponse = ApiResponse<T>()
    apiResponse.block()
    return apiResponse
}

data class Team(
    val name: String,
    val registeredDateTime: Instant,
    val status: Status,
    val members: List<Member>,
)

data class Member(
    val name: String,
    val registeredDateTime: Instant,
    val age: Int,
    val isMarried: Boolean
)

data class Request(
    val teamId: Long,
    val teamName: String
)

enum class Status {
    ACTIVE, INACTIVE
}
