package com.rpla.core_domain.base

import com.rpla.core_domain.entity.ResultBundle

abstract class BaseUseCase<in Request, Result> {

    suspend operator fun invoke(request: Request): ResultBundle<Result> {
        return run(request)
    }

    protected abstract suspend fun run(request: Request): ResultBundle<Result>
}