package benchmark

import kotlinx.coroutines.runBlocking
import dev.pellet.server.PelletBuilder.httpRouter
import dev.pellet.server.PelletBuilder.pelletServer
import dev.pellet.server.PelletConnector
import dev.pellet.server.codec.http.ContentTypes
import dev.pellet.server.responder.http.PelletHTTPRouteContext
import dev.pellet.server.routing.http.HTTPRouteResponse
import dev.pellet.server.routing.http.PelletHTTPRoutePath
import dev.pellet.server.routing.stringDescriptor

private val idVariable = stringDescriptor("id")

fun main() = runBlocking {
    val userIdPath = PelletHTTPRoutePath.Builder()
        .addComponents("/user")
        .addVariable(idVariable)
        .build()
    val httpRouter = httpRouter {
        get("/", ::handleRoot)
        get(userIdPath, ::handleUserId)
        post("/user", ::handleUserPost)
    }
    val pellet = pelletServer {
        logRequests = false
        httpConnector {
            endpoint = PelletConnector.Endpoint(
                hostname = "0.0.0.0",
                port = 3000
            )
            router = httpRouter
        }
    }
    pellet.start().join()
}

suspend fun handleRoot(
    context: PelletHTTPRouteContext
): HTTPRouteResponse {
    return HTTPRouteResponse.Builder()
        .statusCode(200)
        .build()
}

suspend fun handleUserId(
    context: PelletHTTPRouteContext
): HTTPRouteResponse {
    val userId = context.pathParameter(idVariable).getOrThrow()
    return HTTPRouteResponse.Builder()
        .statusCode(200)
        .entity(userId, ContentTypes.Text.Plain)
        .build()
}

suspend fun handleUserPost(
    context: PelletHTTPRouteContext
): HTTPRouteResponse {
    return HTTPRouteResponse.Builder()
        .statusCode(200)
        .build()
}