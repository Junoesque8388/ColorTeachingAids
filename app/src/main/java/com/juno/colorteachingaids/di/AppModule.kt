package com.juno.colorteachingaids.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * A Hilt module for application-level bindings. This module is intentionally left empty
 * as dependencies are provided via @Inject constructor where possible, which is the
 * preferred approach.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule
