package com.unina.natourkt.core.presentation.util

import com.unina.natourkt.R
import com.unina.natourkt.core.util.DataState

class UiTextCauseMapper {

    companion object {
        fun mapToText(cause: DataState.Cause): UiText {
            return when (cause) {
                // Auth
                DataState.Cause.UserNotConfirmed -> UiText.StringResource(R.string.user_not_confirmed)
                DataState.Cause.UserNotFound -> UiText.StringResource(R.string.user_not_found)
                DataState.Cause.UsernameExists -> UiText.StringResource(R.string.username_exists)
                DataState.Cause.AliasExists -> UiText.StringResource(R.string.username_exists)
                DataState.Cause.AuthGeneric -> UiText.StringResource(R.string.auth_failed_generic)
                DataState.Cause.InvalidCredentials -> UiText.StringResource(R.string.incorrect_parameters)
                DataState.Cause.InvalidParameter -> UiText.StringResource(R.string.incorrect_parameters)
                DataState.Cause.InvalidPassword -> UiText.StringResource(R.string.password_not_valid_for_user)

                // Network
                DataState.Cause.BadRequest -> UiText.StringResource(R.string.bad_request)
                DataState.Cause.CodeDelivery -> UiText.StringResource(R.string.error_confirmation_code_deliver)
                DataState.Cause.CodeExpired -> UiText.StringResource(R.string.expired_confirmation_code)
                DataState.Cause.CodeMismatch -> UiText.StringResource(R.string.wrong_confirmation_code)
                DataState.Cause.Conflict -> UiText.StringResource(R.string.conflict)
                DataState.Cause.DataCorrupted -> UiText.StringResource(R.string.data_corrupted)
                DataState.Cause.Forbidden -> UiText.StringResource(R.string.forbidden)
                DataState.Cause.InternalServerError -> UiText.StringResource(R.string.internal_error)
                DataState.Cause.NetworkError -> UiText.StringResource(R.string.internet_error)
                DataState.Cause.NotAcceptable -> UiText.StringResource(R.string.not_acceptable)
                DataState.Cause.NotFound -> UiText.StringResource(R.string.not_found)
                DataState.Cause.ServiceUnavailable -> UiText.StringResource(R.string.service_unavailable)
                DataState.Cause.Timeout -> UiText.StringResource(R.string.timeout)
                DataState.Cause.Unauthorized -> UiText.StringResource(R.string.unauthorized)

                // Other
                DataState.Cause.SomethingWentWrong -> UiText.unknownError()
                
                DataState.Cause.FileError -> UiText.StringResource(R.string.file_not_valid)
                DataState.Cause.StorageError -> UiText.StringResource(R.string.storage_error)
            }
        }
    }
}