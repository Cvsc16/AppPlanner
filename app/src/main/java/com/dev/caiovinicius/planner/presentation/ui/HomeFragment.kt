package com.dev.caiovinicius.planner.presentation.ui

import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.dev.caiovinicius.planner.R
import com.dev.caiovinicius.planner.domain.utils.imageBase64ToBitmap
import com.dev.caiovinicius.planner.databinding.FragmentHomeBinding
import com.dev.caiovinicius.planner.domain.utils.toPlannerActivityDate
import com.dev.caiovinicius.planner.domain.utils.toPlannerActivityTime
import com.dev.caiovinicius.planner.presentation.ui.component.PlannerActivityAdapter
import com.dev.caiovinicius.planner.presentation.ui.component.PlannerActivityDatePickerDialogFragment
import com.dev.caiovinicius.planner.presentation.ui.component.PlannerActivityTimePickerDialogFragment
import com.dev.caiovinicius.planner.presentation.ui.extension.hideKeyboard
import com.dev.caiovinicius.planner.presentation.ui.viewmodel.PlannerActivityViewModel
import com.dev.caiovinicius.planner.presentation.ui.viewmodel.SetDate
import com.dev.caiovinicius.planner.presentation.ui.viewmodel.SetTime
import com.dev.caiovinicius.planner.presentation.ui.viewmodel.UserRegistrationViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    val userRegistrationViewModel: UserRegistrationViewModel by activityViewModels()
    val plannerActivityViewModel: PlannerActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        with(binding) {
            plannerActivityViewModel.fetchActivities()

            clHomeContainer.setOnClickListener {
                tietNewPlannerActivityName.clearFocus()
                requireContext().hideKeyboard(tietNewPlannerActivityName)
            }

            tietNewPlannerActivityName.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    tietNewPlannerActivityName.clearFocus()
                    requireContext().hideKeyboard(tietNewPlannerActivityName)
                }

                plannerActivityViewModel.updateNewActivity(
                    name = text.toString()
                )
            }


            tietNewPlannerActivityDate.setOnClickListener {
                PlannerActivityDatePickerDialogFragment(
                    onConfirm = { year, month, dayOfMonth ->
                        val filledCalendar = Calendar.getInstance().apply {
                            set(Calendar.YEAR, year)
                            set(Calendar.MONTH, month)
                            set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        }

                        tietNewPlannerActivityDate.setText(filledCalendar.toPlannerActivityDate())
                        plannerActivityViewModel.updateNewActivity(
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

            tietNewPlannerActivityTime.setOnClickListener {
                PlannerActivityTimePickerDialogFragment(
                    onConfirm = { hourOfDay, minute ->
                        val filledCalendar = Calendar.getInstance().apply {
                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                            set(Calendar.MINUTE, minute)
                        }

                        tietNewPlannerActivityTime.setText(filledCalendar.toPlannerActivityTime())
                        plannerActivityViewModel.updateNewActivity(
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

            btnSaveNewPlannerActivity.setOnClickListener {
                plannerActivityViewModel.saveNewActivity(
                    onSucess = {
                        clearNewPlannerActivityFields()
                    },
                    onError = {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.oops_houve_uma_falha_ao_salvar_a_atividade),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            }
        }
    }

    private fun FragmentHomeBinding.clearNewPlannerActivityFields() {
        tietNewPlannerActivityName.text = null
        tietNewPlannerActivityDate.text = null
        tietNewPlannerActivityTime.text = null

        tietNewPlannerActivityName.clearFocus()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            launch {
                userRegistrationViewModel.profile.collect { profile ->
                    binding.tvUserName.text = getString(R.string.ola_usuario, profile.name)
                    imageBase64ToBitmap(profile.image)?.let { imageBitmap ->
                        binding.ivUserPhoto.setImageBitmap(imageBitmap)
                    }
                }
            }
            launch {
                userRegistrationViewModel.isTokenValid.distinctUntilChanged { old, new ->
                    old == new
                }.collect { isTokenValid ->
                    if (isTokenValid == false) showNewTokenSnackBar()
                }
            }
            launch {
                plannerActivityViewModel.activities.collect { activities ->
                    with(binding) {
                        if (rvPlannerActivities.adapter == null)
                            rvPlannerActivities.adapter = PlannerActivityAdapter()

                        (rvPlannerActivities.adapter as PlannerActivityAdapter).submitList(
                            activities
                        )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showNewTokenSnackBar() {
        Snackbar.make(
            requireView(),
            "Oops... O seu token expirou.",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Obter novo token") {
            userRegistrationViewModel.obtainNewToken()
        }
            .setActionTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.lime_300
                )
            ).show()
    }

}