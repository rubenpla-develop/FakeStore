package com.rpla.core.domain.base

import com.rpla.core.domain.entity.ResultBundle

abstract class BaseUseCase<in Request, Result> {
    suspend operator fun invoke(request: Request): ResultBundle<Result> = run(request)

    protected abstract suspend fun run(request: Request): ResultBundle<Result>
}
