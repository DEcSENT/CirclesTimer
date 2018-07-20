/*
 * Copyright (c) 2018 by Denis Verentsov (decsent@yandex.ru)
 * All rights reserved.
 */

package com.dvinc.workouttimer.domain.usecase.workout

import com.dvinc.workouttimer.domain.common.extension.getIoToMainTransformerFlowable
import com.dvinc.workouttimer.domain.model.workout.Workout
import com.dvinc.workouttimer.domain.repository.workout.WorkoutRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class WorkoutUseCase @Inject constructor(
        private val workoutRepository: WorkoutRepository
) {

    fun obtainWorkouts(): Flowable<List<Workout>>{
        return workoutRepository.obtainWorkouts()
                .compose(getIoToMainTransformerFlowable())
    }

    fun selectActiveWorkout(workoutId: Int): Completable {
        //TODO: flow for workout updating
        return Completable.complete()
    }
}
 