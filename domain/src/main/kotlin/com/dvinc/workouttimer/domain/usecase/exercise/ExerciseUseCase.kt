/*
 * Copyright (c) 2018 by Denis Verentsov (decsent@yandex.ru)
 * All rights reserved.
 */

package com.dvinc.workouttimer.domain.usecase.exercise

import com.dvinc.workouttimer.domain.common.extension.getIoToMainTransformerFlowable
import com.dvinc.workouttimer.domain.model.exercise.Exercise
import com.dvinc.workouttimer.domain.repository.exercise.ExerciseRepository
import io.reactivex.Flowable
import javax.inject.Inject

class ExerciseUseCase @Inject constructor(
        private val exercisesRepository: ExerciseRepository
) {

    fun obtainExercisesForCurrentWorkout(): Flowable<List<Exercise>> {
        return exercisesRepository.obtainExercises()
                .compose(getIoToMainTransformerFlowable())
    }
}
 