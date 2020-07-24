package com.fastcon.etherapp.exception

class NetworkExceptions {
    companion object {


        private var exceptions = HashMap<String, String>()

        private const val codeOk: Int = 200
        private const val okRequest: String =
            "Good Response"
        private const val codeBadRequest: Int = 400
        private const val badRequest: String =
            "Bad Request — Client sent an invalid request — such as lacking required request body or parameter"
        private const val codeUnauthorized: Int = 401
        private const val unauthorized: String =
            "Unauthorized — Client failed to authenticate with the server"
        private const val codeForbidden: Int = 403
        private const val forbidden: String =
            "Forbidden — Client authenticated but does not have permission to access the requested resource"
        private const val codeNotFound: Int = 404
        private const val notFound: String = "Not Found — The requested resource does not exist"
        private const val codePreconditionFailed = 412
        private const val preconditionFailed =
            "Precondition Failed — One or more conditions in the request header fields evaluated to false"
        private const val codeInternalServerError = 500
        private const val internalServerError =
            "Internal Server Error — A generic error occurred on the server"
        private const val codeServiceUnavailable = 503
        private const val serviceUnavailableException =
            "Service Unavailable — The requested service is not available"

        init {
            addExceptions(exceptions)
        }

        private fun addExceptions(exceptions: HashMap<String, String>) {
            exceptions[codeOk.toString()] = okRequest
            exceptions[codeBadRequest.toString()] = badRequest
            exceptions[codeServiceUnavailable.toString()] = serviceUnavailableException
            exceptions[codeInternalServerError.toString()] = internalServerError
            exceptions[codePreconditionFailed.toString()] = preconditionFailed
            exceptions[codeForbidden.toString()] = forbidden
            exceptions[codeNotFound.toString()] = notFound
            exceptions[codeUnauthorized.toString()] = unauthorized
        }


        fun getException(code: Int): String? {
            return exceptions[code.toString()]
        }
    }
}


