package com.example.vacationplanner.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vacationplanner.R
import com.example.vacationplanner.model.VacationData
import com.example.vacationplanner.view.VacationAdapter
import com.example.vacationplanner.viewmodels.VacationViewModel
import com.example.vacationplanner.viewmodels.VacationViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar


class MainFragment : Fragment() {

    private val vacationViewModel: VacationViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, VacationViewModelFactory(activity.application)).get(
            VacationViewModel::class.java)
    }

    private var vacationList: MutableList<VacationData> = mutableListOf()
    private lateinit var addButton : FloatingActionButton


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vacationViewModel.error.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(
                    requireContext(),
                    "Wait until you get closer to the start date of your vacation",
                    Toast.LENGTH_SHORT
                ).show()
                vacationViewModel.onDisplayedError()
            }
        }

        vacationViewModel.navigate.observe(viewLifecycleOwner) {
            if (it != null) {

                val bundle = Bundle()
                bundle.putParcelable("vacationdata", it)
                view.findNavController()
                    .navigate(R.id.mainToWeather, bundle)
                vacationViewModel.onNavigated()
            }
        }

        vacationViewModel.error.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(
                    this.context,
                    "Wait until you get closer to the start date of your vacation",
                    Toast.LENGTH_SHORT
                ).show()
                vacationViewModel.onDisplayedError()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)

        val vacationAdapter = VacationAdapter(vacationViewModel)
        vacationAdapter.onItemClick = fun(it: VacationData) {
            vacationViewModel.onItemClicked(it)
        }

        val recView = rootView.findViewById<RecyclerView>(R.id.recycleview)
        recView.adapter = vacationAdapter
        recView.layoutManager = LinearLayoutManager(requireContext())

        vacationViewModel.allVacations.observe(viewLifecycleOwner) { vacation ->
            vacation?.let {
                vacationAdapter.vacationList = it.toMutableList()
                vacationAdapter.notifyDataSetChanged()
            }
        }

        addButton = rootView.findViewById(R.id.floatingButton)
        addButton.setOnClickListener {
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.add_item, null)

            val cityName = dialogView.findViewById<EditText>(R.id.citynameEditText)
            val startDate = dialogView.findViewById<EditText>(R.id.startdateEditText)
            val nrDays = dialogView.findViewById<EditText>(R.id.nrofdaysEditText)

            startDate.setOnClickListener {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, pickerYear, monthOfYear, dayOfMonth ->
                        val dat = (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + pickerYear)
                        startDate.setText(dat)
                    },
                    year,
                    month,
                    day
                )
                datePickerDialog.show()
            }


            val addDialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Ok") { dialog, _ ->
                    val name = cityName.text.toString()
                    val date = startDate.text.toString()
                    val days = nrDays.text.toString().toInt()
                    val vacationData = VacationData(name, date, null, null, days)

                    vacationViewModel.insert(vacationData)
                    vacationList.add(vacationData)
                    vacationAdapter.notifyDataSetChanged()
                    Toast.makeText(context, "Adding vacation...", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                    Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show()
                }
                .create()

            addDialog.show()
        }

        return rootView
    }


}