package com.rpla.fakestore.core.domain.base

import com.rpla.fakestore.core.domain.entity.ResultBundle

abstract class BaseUseCase<in Request, Result> {
    suspend operator fun invoke(request: Request): ResultBundle<Result> = run(request)

    protected abstract suspend fun run(request: Request): ResultBundle<Result>
}
