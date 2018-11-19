/*
 * Copyright (c) 2018 by Denis Verentsov (decsent@yandex.ru)
 * All rights reserved.
 */

package com.dvinc.workouttimer.presentation.ui.exercise

import android.animation.Animator
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.dvinc.workouttimer.R
import com.dvinc.workouttimer.domain.model.workout.Workout
import com.dvinc.workouttimer.presentation.common.adapter.divider.HorizontalDivider
import com.dvinc.workouttimer.presentation.common.application.WorkoutApp
import com.dvinc.workouttimer.presentation.common.extension.*
import com.dvinc.workouttimer.presentation.common.view.ADD_BUTTON_ANIMATION_DURATION
import com.dvinc.workouttimer.presentation.common.view.EXERCISE_ITEM_LEFT_PADDING
import com.dvinc.workouttimer.presentation.common.view.SimpleAnimationListener
import com.dvinc.workouttimer.presentation.common.adapter.item.exercise.ExerciseItem
import com.dvinc.workouttimer.presentation.common.viewmodel.ViewModelFactory
import com.dvinc.workouttimer.presentation.model.exercise.ExerciseUi
import com.dvinc.workouttimer.presentation.ui.base.BaseFragment
import com.dvinc.workouttimer.presentation.ui.new_exercise.NewExerciseFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_exercise.fragment_exercise_recycler as exercisesRecycler
import kotlinx.android.synthetic.main.fragment_exercise.fragment_exercise_add_button as exerciseAddButton
import kotlinx.android.synthetic.main.fragment_exercise.fragment_exercise_active_workout_name as workoutName
import kotlinx.android.synthetic.main.fragment_exercise.fragment_exercise_active_workout_description as workoutDescription
import kotlinx.android.synthetic.main.fragment_exercise.fragment_exercise_active_workout_total_time as workoutTotalTime
import kotlinx.android.synthetic.main.fragment_exercise.fragment_exercise_active_workout_total_exercises_count as workoutExercises
import kotlinx.android.synthetic.main.fragment_exercise.fragment_exercise_active_workout_group as activeWorkoutGroup
import javax.inject.Inject

class ExerciseFragment : BaseFragment() {

    companion object {
        const val TAG = "ExerciseFragment"
    }

    @Inject
    lateinit var viewModeFactory: ViewModelFactory

    private lateinit var exerciseViewModel: ExerciseViewModel

    private val exercisesAdapter: GroupAdapter<ViewHolder> = GroupAdapter()

    override fun getFragmentLayoutId() = R.layout.fragment_exercise

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initExercisesList()
        setupScrollListener()
        setupAddButtonClickListener()
    }

    override fun injectDependencies() {
        context?.let {
            WorkoutApp.get(it).getExerciseComponent()?.inject(this)
        }
    }

    override fun clearDependencies() {
        context?.let {
            WorkoutApp.get(it).clearExerciseComponent()
        }
    }

    override fun initViewModel() {
        exerciseViewModel = obtainViewModel(viewModeFactory)
        observe(exerciseViewModel.exercisesData, ::showExercises)
        observe(exerciseViewModel.activeWorkoutData, ::showActiveWorkoutInfo)
    }

    private fun showExercises(exercises: List<ExerciseUi>) {
        exercisesAdapter.clear()
        exercisesAdapter.addAll(exercises.map {
            ExerciseItem(it)
        })
    }

    private fun showActiveWorkoutInfo(workout: Workout) {
        activeWorkoutGroup.makeVisible()
        with(workout) {
            workoutName.text = name
            workoutDescription.text = description
            workoutTotalTime.text = exerciseTotalTime.toString()
            workoutExercises.text = exerciseCount.toString()
        }
    }

    private fun initExercisesList() {
        exercisesRecycler.adapter = exercisesAdapter
        context?.let {
            exercisesRecycler.addItemDecoration(
                    HorizontalDivider(it, leftPadding = EXERCISE_ITEM_LEFT_PADDING.dp()))
        }
    }

    private fun setupScrollListener() {
        exercisesRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                    hideAddExerciseButton()
                else if (dy < 0) {
                    showAddExerciseButton()
                }
            }
        })
    }

    private fun setupAddButtonClickListener() {
        exerciseAddButton.setOnClickListener {
            showNewExerciseDialog()
        }
    }

    private fun hideAddExerciseButton() {
        exerciseAddButton
                .animateFadeOutWithDuration(ADD_BUTTON_ANIMATION_DURATION)
                .setListener(object : SimpleAnimationListener() {
                    override fun onAnimationEnd(animation: Animator?) {
                        exerciseAddButton.makeVisible()
                    }
                })
    }

    private fun showAddExerciseButton() {
        exerciseAddButton
                .animateFadeInWithDuration(ADD_BUTTON_ANIMATION_DURATION)
                .setListener(object : SimpleAnimationListener() {
                    override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {
                        exerciseAddButton.makeGone()
                    }
                })
    }

    private fun showNewExerciseDialog() {
        //TODO: Refactor navigation system
        NewExerciseFragment.newInstance().show(fragmentManager, NewExerciseFragment.TAG)
    }
}
