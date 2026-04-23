package com.dev.caiovinicius.planner.presentation.ui

import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.dev.caiovinicius.planner.databinding.FragmentUpdatePlannerActivityDialogBinding
import com.dev.caiovinicius.planner.domain.model.PlannerActivity
import com.dev.caiovinicius.planner.domain.utils.createCalendarFromTimeInMillis
import com.dev.caiovinicius.planner.domain.utils.toPlannerActivityDate
import com.dev.caiovinicius.planner.domain.utils.toPlannerActivityTime
import com.dev.caiovinicius.planner.presentation.ui.component.PlannerActivityDatePickerDialogFragment
import com.dev.caiovinicius.planner.presentation.ui.component.PlannerActivityTimePickerDialogFragment
import com.dev.caiovinicius.planner.presentation.ui.extension.hideKeyboard
import com.dev.caiovinicius.planner.presentation.ui.viewmodel.PlannerActivityViewModel
import com.dev.caiovinicius.planner.presentation.ui.viewmodel.SetDate
import com.dev.caiovinicius.planner.presentation.ui.viewmodel.SetTime
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UpdatePlannerActivityDialogFragment(
    private val selectedActivity: PlannerActivity
) : BottomSheetDialogFragment() {

    private var _binding: FragmentUpdatePlannerActivityDialogBinding? = null
    private val binding get() = _binding!!

    private val plannerActivityViewModel: PlannerActivityViewModel by activityViewModels()

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        plannerActivityViewModel.clearSelectedActivity()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdatePlannerActivityDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        plannerActivityViewModel.setSelectedActivity(selectedActivity)

        with(binding) {
            val selectedActivityDateTimeCalendar =
                createCalendarFromTimeInMillis(selectedActivity.datetime)

            tietUpdatedPlannerActivityName.setText(selectedActivity.name)
            tietUpdatedPlannerActivityDate.setText(selectedActivityDateTimeCalendar.toPlannerActivityDate())
            tietUpdatedPlannerActivityTime.setText(selectedActivityDateTimeCalendar.toPlannerActivityTime())

            tietUpdatedPlannerActivityName.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    tietUpdatedPlannerActivityName.clearFocus()
                    requireContext().hideKeyboard(tietUpdatedPlannerActivityName)
                }

                plannerActivityViewModel.updateSelectedActivity(
                    name = text.toString()
                )
            }


            tietUpdatedPlannerActivityDate.setOnClickListener {
                PlannerActivityDatePickerDialogFragment(
                    initialDate = createCalendarFromTimeInMillis(selectedActivity.datetime),
                    onConfirm = { year, month, dayOfMonth ->
                        val filledCalendar = Calendar.getInstance().apply {
                            set(Calendar.YEAR, year)
                            set(Calendar.MONTH, month)
                            set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        }

                        tietUpdatedPlannerActivityDate.setText(filledCalendar.toPlannerActivityDate())
                        plannerActivityViewModel.updateSelectedActivity(
                            date = SetDate(
                                year = year,
                                month = month,
                                dayOfMonth = dayOfMonth
                            )
                        )
                    },
                    onCancel = {}
                ).show(
                    childFragmentManager,
                    PlannerActivityDatePickerDialogFragment.TAG
                )
            }

            tietUpdatedPlannerActivityTime.setOnClickListener {
                PlannerActivityTimePickerDialogFragment(
                    initialTime = createCalendarFromTimeInMillis(selectedActivity.datetime),
                    onConfirm = { hourOfDay, minute ->
                        val filledCalendar = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                            set(Calendar.MINUTE, minute)
                        }

                        tietUpdatedPlannerActivityTime.setText(filledCalendar.toPlannerActivityTime())
                        plannerActivityViewModel.updateSelectedActivity(
                            time = SetTime(
                                hourOfDay = hourOfDay,
                                minute = minute
                            )
                        )
                    },
                    onCancel = {}
                ).show(
                    childFragmentManager,
                    PlannerActivityTimePickerDialogFragment.TAG
                )
            }

            tvUpdatedPlannerActivityDelete.setOnClickListener {
                plannerActivityViewModel.delete(selectedActivity.uuid)
                dialog?.dismiss()
            }

            btnSaveUpdatedPlannerActivity.setOnClickListener {
                plannerActivityViewModel.saveUpdatedSelectedActivity()
                dialog?.dismiss()
            }
        }
    }

    companion object {
        const val TAG = "UpdatePlannerActivityDialogFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}