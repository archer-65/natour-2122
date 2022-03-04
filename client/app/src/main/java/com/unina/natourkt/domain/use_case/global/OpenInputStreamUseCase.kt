package com.unina.natourkt.domain.use_case.global

import android.net.Uri
import com.unina.natourkt.presentation.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OpenInputStreamUseCase @Inject constructor() {

    operator suspend fun invoke(uri: Uri) = withContext(Dispatchers.Default) {
        runCatching {
            MainActivity.instance.contentResolver.openInputStream(uri)
        }
    }
}