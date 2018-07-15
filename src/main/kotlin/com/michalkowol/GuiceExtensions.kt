package com.michalkowol

import com.google.inject.Injector

inline fun <reified T> Injector.getInstance(): T = getInstance(T::class.java)
