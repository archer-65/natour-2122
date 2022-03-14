package com.unina.natourkt.core.presentation.model.mapper

import com.unina.natourkt.core.domain.model.Compilation
import com.unina.natourkt.core.presentation.model.CompilationItemUi
import javax.inject.Inject

class CompilationItemUiMapper @Inject constructor() : UiMapper<Compilation, CompilationItemUi> {

    override fun mapToUi(domainEntity: Compilation): CompilationItemUi {
        return CompilationItemUi(
            id = domainEntity.id,
            title = domainEntity.title,
            description = domainEntity.description,
            photo = domainEntity.photo,
            authorId = domainEntity.author.id,
            authorPhoto = domainEntity.author.profilePhoto
        )
    }
}