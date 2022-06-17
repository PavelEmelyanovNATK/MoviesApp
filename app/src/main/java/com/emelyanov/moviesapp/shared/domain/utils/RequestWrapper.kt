package com.emelyanov.moviesapp.shared.domain.utils

import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @throws ServerNotRespondingException
 * @throws ConnectionErrorException
 * @throws NotFoundException
 * @throws BadRequestException
 * @throws Exception
 */
suspend fun <T> requestWrapper(request: suspend () -> T) : T {
    return try {
        request()
    } catch (ex: SocketTimeoutException) {
        throw ServerNotRespondingException(ex)
    } catch(ex: SocketException) {
        throw ConnectionErrorException(ex)
    } catch (ex: UnknownHostException) {
        throw ConnectionErrorException(ex)
    } catch (ex: HttpException) {
        throw when(ex.code()) {
            400 -> BadRequestException()
            404 -> NotFoundException()
            else -> ex
        }
    }
}

suspend fun requestExceptionHandler(
    onServerNotResponding: (ServerNotRespondingException) -> Unit = {},
    onConnectionError: (ConnectionErrorException) -> Unit = {},
    onNotFound: (NotFoundException) -> Unit = {},
    onBadRequest: (BadRequestException) -> Unit = {},
    onAnother: (Exception) -> Unit = {},
    block: suspend () -> Unit
) {
    try {
        block()
    } catch (ex: ServerNotRespondingException) {
        onServerNotResponding(ex)
    } catch (ex: ConnectionErrorException) {
        onConnectionError(ex)
    } catch (ex: NotFoundException) {
        onNotFound(ex)
    } catch (ex: BadRequestException) {
        onBadRequest(ex)
    } catch (ex: Exception) {
        onAnother(ex)
    }
}

class ServerNotRespondingException : Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
class ConnectionErrorException : Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
class NotFoundException : Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
class BadRequestException : Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String, cause: Throwable) : super(message, cause)
}