package com.cinescope.cinescope.data.mapper

import com.cinescope.cinescope.data.remote.dto.GenreDto
import com.cinescope.cinescope.domain.model.Genre

object GenreMapper {
    fun GenreDto.toDomain(): Genre {
        return Genre(
            id = id,
            name = name
        )
    }
}
