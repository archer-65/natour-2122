package com.unina.natourkt.core.presentation.model.mapper

import com.unina.natourkt.core.domain.model.Compilation
import com.unina.natourkt.core.presentation.model.CompilationDialogItemUi
import javax.inject.Inject

class CompilationDialogItemUiMapper @Inject constructor() :
    UiMapper<Compilation, CompilationDialogItemUi> {

    override fun mapToUi(domainEntity: Compilation): CompilationDialogItemUi {
        return CompilationDialogItemUi(
            id = domainEntity.id,
            title = domainEntity.title,
            photo = domainEntity.photo
        )
    }
}